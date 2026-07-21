import { useEffect, useRef, useState } from "react";

import type {
  Category,
  NewsItem,
  PageMeta,
  PagedResponse,
  Status,
} from "../types/news";

// Not used
//import NewsCard from "../components/NewsCard"; 

function formatWhen(iso: string | null): string {
  if (!iso) return "—";
  const d = new Date(iso);
  if (isNaN(d.getTime())) return "—";
  return d.toLocaleString();
}

function relativeFromNow(iso: string | null): string {
  if (!iso) return "";
  const d = new Date(iso);
  if (isNaN(d.getTime())) return "";
  const ms = Date.now() - d.getTime();
  const sec = Math.floor(ms / 1000);
  if (sec < 10) return "just now";
  if (sec < 60) return `${sec}s ago`;
  const min = Math.floor(sec / 60);
  if (min < 60) return `${min}m ago`;
  const hr = Math.floor(min / 60);
  if (hr < 48) return `${hr}h ago`;
  const days = Math.floor(hr / 24);
  return `${days}d ago`;
}

export default function App() {
  const PAGE_SIZE = 50;

  const [page, setPage] = useState(0);
  const [pageMeta, setPageMeta] = useState<PageMeta | null>(null);
  const [items, setItems] = useState<NewsItem[]>([]);
  const [status, setStatus] = useState<Status>({
    lastRun: null,
    lastSuccess: null,
    lastError: null,
  });

  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(false);
  const [loadingMore, setLoadingMore] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [category, setCategory] = useState<"ALL" | Category>("ALL");

  //const [source, setSource] = useState<string | null>(null);

  const [query, setQuery] = useState("");
  const [debouncedQuery, setDebouncedQuery] = useState("");

  const loadMoreRef = useRef<HTMLDivElement | null>(null);
  const loadingMoreRef = useRef(false);
  const hasMore = pageMeta ? page < pageMeta.totalPages - 1 : false;
  
  async function loadNews(
    selectedCategory: "ALL" | Category = category,
    pageNumber: number = page,
    opts?: { append?: boolean }
  ) {
    setLoading(true);
    setError(null);

    try {
      const qParam = debouncedQuery.trim()
        ? `&q=${encodeURIComponent(debouncedQuery.trim())}`
        : "";
      
      const base =
        selectedCategory === "ALL"
          ? `/api/news?page=${pageNumber}&size=${PAGE_SIZE}${qParam}`
          : `/api/news?page=${pageNumber}&size=${PAGE_SIZE}&category=${encodeURIComponent(
        selectedCategory
      )}${qParam}`;

      const r = await fetch(base);
      if (!r.ok) throw new Error(`GET ${base} failed: ${r.status}`);

      const data = (await r.json()) as PagedResponse<NewsItem>;
      const newItems = Array.isArray(data?.items) ? data.items : [];

      setPageMeta(data?.page ?? null);

      if (opts?.append) {
        setItems((prev) => [...prev, ...newItems]);
      } else {
        setItems(newItems);
      }
    } catch (e: any) {
      setError(e?.message ?? "Failed to load news");
    } finally {
      setLoading(false);
    }
  }

  async function loadStatus() {
    try {
      const r = await fetch("/api/news/status");
      if (!r.ok) throw new Error(`GET /api/news/status failed: ${r.status}`);
      const data = (await r.json()) as Status;
      setStatus({
        lastRun: data?.lastRun ?? null,
        lastSuccess: data?.lastSuccess ?? null,
        lastError: data?.lastError ?? null,
      });
    } catch {
      // don't hard-fail UI if status endpoint is down
    }
  }

  async function goPrev() {
    window.scrollTo({ top: 0, behavior: "smooth" });

    const newPage = Math.max(0, page - 1);
    setPage(newPage);
    setItems([]);
    await loadNews(category, newPage);
  }

  async function goNext() {
    window.scrollTo({ top: 0, behavior: "smooth" });

    const newPage = pageMeta ? Math.min(pageMeta.totalPages - 1, page + 1) : page + 1;
    setPage(newPage);
    setItems([]);
    await loadNews(category, newPage);
  } 

  async function refreshNow() {
    setRefreshing(true);
    setError(null);
    try {
      const r = await fetch("/api/news/refresh", { method: "POST" });
      if (!r.ok) throw new Error(`POST /api/news/refresh failed: ${r.status}`);
      // After refresh: reload status + news
      await Promise.all([loadStatus(), loadNews(category, page)]);
    } catch (e: any) {
      setError(e?.message ?? "Refresh failed");
      await loadStatus();
    } finally {
      setRefreshing(false);
    }
  }

  // initial load
  useEffect(() => {
    loadNews("ALL");
    loadStatus();
    // refresh status every 15s so header stays current
    const t = setInterval(loadStatus, 30000); // 30 seconds
    return () => clearInterval(t);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // reload news when category changes
  useEffect(() => {
    setPage(0); // Set page to zero
    setPageMeta(null); // clear old pageMeta
    setItems([]); // prevents showing "Page 6 of 10" from the prior category while new one loads.
    loadingMoreRef.current = false;
    loadNews(category, 0); //Reset to zero. Removed to avoid duplicate load. Page effect handle it.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [category]);

  useEffect(() => {
    setPage(0);
    setPageMeta(null);
    setItems([]);
    loadingMoreRef.current = false;
    loadNews(category, 0);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [debouncedQuery]);

 
  useEffect(() => {
  const timer = setTimeout(() => {
      setDebouncedQuery(query);
    }, 400); // 400ms delay

    return () => clearTimeout(timer);
  }, [query]);

  // Load when page changes(but avoid double-load)
  // useEffect(() => {
  //loadNews(category, page);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  // }, [page]);

  useEffect(() => {
    if (!loadMoreRef.current) return;
    if (!hasMore) return;

    const el = loadMoreRef.current;

    const obs = new IntersectionObserver(
      (entries) => {
        const first = entries[0];
        if (first.isIntersecting && !loading && !refreshing && !loadingMoreRef.current) {
          const nextPage = page + 1;
          loadingMoreRef.current = true;
          setLoadingMore(true);

          loadNews(category, nextPage, { append: true })
            .then(() => setPage(nextPage))
            .finally(() => {
              loadingMoreRef.current = false;
              setLoadingMore(false);
            });
        }
      },
      { root: null, rootMargin: "600px", threshold: 0 }
    );

    obs.observe(el);
    return () => obs.disconnect();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [category, page, hasMore, loading, refreshing, debouncedQuery]);

  //const categoriesInList = useMemo(() => {
   // const s = new Set<Category>();
   // for (const x of items) s.add(x.category);
   // return Array.from(s).sort();
//  }, [items]);

  return (
    <div style={{ padding: 24, fontFamily: "system-ui" }}>
      <div style={{ display: "flex", alignItems: "baseline", gap: 16 }}>
        <h1 style={{ margin: 0 }}>Newswire</h1>

        <div style={{ marginLeft: "auto", display: "flex", gap: 10, alignItems: "center" }}>
            <input
              placeholder="Search news..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              style={{
                padding: "6px 8px",
                borderRadius: 8,
                border: "1px solid #ccc",
                minWidth: 220
              }}
              disabled={loading || refreshing}
            />
          <label style={{ fontSize: 13, opacity: 0.8 }}>
            Category{" "}
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value as any)}
              style={{ padding: "6px 8px", borderRadius: 8 }}
              disabled={loading || refreshing}
            >
              <option value="ALL">All</option>
              {/* use categories from current list if available; fallback to a few known */}
              {([
                "FINANCE",
                 "POLITICS",
                 "HEALTHCARE",
                 "TECHNOLOGY",
                 "MILITARY", 
                 "MILITARY_INTELLIGENCE", 
                 "WORLD_POPULATION", 
                 "WORLD_ECONOMIES"
                ] as Category[]).map((c) => (
                  <option key={c} value={c}>
                    {c}
                  </option>
                )
              )}
            </select>
          </label>

          <button
            onClick={refreshNow}
            disabled={refreshing || loading}
            style={{
              padding: "8px 12px",
              borderRadius: 10,
              cursor: refreshing ? "not-allowed" : "pointer",
            }}
            title="Fetch RSS and store new articles"
          >
            {refreshing ? "Refreshing…" : "Refresh now"}
          </button>
        </div>
      </div>

      <div style={{ marginTop: 8, fontSize: 13, opacity: 0.75 }}>
        <span>
          Last success: <strong>{formatWhen(status.lastSuccess)}</strong>{" "}
          {status.lastSuccess ? `(${relativeFromNow(status.lastSuccess)})` : ""}
        </span>
        <span style={{ marginLeft: 12 }}>
          Last run: <strong>{formatWhen(status.lastRun)}</strong>
        </span>
        {status.lastError ? (
          <span style={{ marginLeft: 12 }}>
            Error: <strong>{status.lastError}</strong>
          </span>
        ) : null}
      </div>

      {error ? (
        <p style={{ marginTop: 12 }}>
          <strong>Warning:</strong> {error}
        </p>
      ) : null}

      {loading ? <p style={{ marginTop: 12 }}>Loading…</p> : null}

            <div style={{ marginTop: 12, display: "flex", gap: 8, alignItems: "center" }}>
        <button
          onClick={goPrev}
          disabled={loading || refreshing || page === 0}
          style={{ padding: "6px 10px", borderRadius: 10 }}
        >
          ← Prev
        </button>

        <div style={{ fontSize: 13, opacity: 0.8 }}>
          Page <strong>{(pageMeta?.number ?? page) + 1}</strong>
          {pageMeta ? (
            <>
              {" "}
              of <strong>{pageMeta.totalPages}</strong> •{" "}
              <strong>{pageMeta.totalElements}</strong> articles
            </>
          ) : null}
        </div>

        <button
          onClick={goNext}
          disabled={loading || refreshing || (pageMeta ? page >= pageMeta.totalPages - 1 : false)}
          style={{ padding: "6px 10px", borderRadius: 10, marginLeft: "auto" }}
        >
          Next →
        </button>
      </div>
      
      <ul style={{ marginTop: 14, paddingLeft: 0, listStyle: "none" }}>
        {items.map((x, i) => (
          <li
            key={x.id ?? `${x.url}-${i}`}
            style={{
              marginBottom: 12,
              padding: 14,
              borderRadius: 14,
              border: "1px solid rgba(0,0,0,0.08)",
              boxShadow: "0 1px 2px rgba(0,0,0,0.04)",
            }}
          >
            <div style={{ display: "flex", gap: 10, alignItems: "baseline" }}>
              <a
                href={x.url}
                target="_blank"
                rel="noreferrer"
                style={{
                  fontWeight: 650,
                  textDecoration: "none",
                  color: "inherit",
                  lineHeight: 1.25,
                }}
                onMouseOver={(e) => (e.currentTarget.style.textDecoration = "underline")}
                onMouseOut={(e) => (e.currentTarget.style.textDecoration = "none")}
              >
                {x.title}
              </a>

              <span
                style={{
                  marginLeft: "auto",
                  fontSize: 12,
                  opacity: 0.7,
                  whiteSpace: "nowrap",
                }}
                title={(() => {
                  const d = new Date(x.publishedAt);
                  return isNaN(d.getTime()) ? "Unknown time" : d.toLocaleString();
                })()}
              >
                {relativeFromNow(x.publishedAt)}
              </span>
            </div>

            <div style={{ marginTop: 8, display: "flex", gap: 8, flexWrap: "wrap" }}>
              <span
                style={{
                  fontSize: 12,
                  padding: "2px 8px",
                  borderRadius: 999,
                  background: "rgba(0,0,0,0.06)",
                }}
                title="Source"
              >
                {x.sourceName}
              </span>

              <span
                style={{
                  fontSize: 12,
                  padding: "2px 8px",
                  borderRadius: 999,
                  background: "rgba(0,0,0,0.06)",
                }}
                title="Category"
              >
                {x.category}
              </span>

              <span style={{ fontSize: 12, opacity: 0.7 }}>
                {(() => {
                  const d = new Date(x.publishedAt);
                  return isNaN(d.getTime()) ? "Unknown time" : d.toLocaleString();
                })()}
              </span>
            </div>

            {x.summary ? (
              <div style={{ marginTop: 10, fontSize: 13, opacity: 0.92, lineHeight: 1.4 }}>
                {x.summary.length > 240 ? x.summary.slice(0, 240) + "…" : x.summary}
              </div>
            ) : null}
          </li>
        ))}
      </ul>
      <div ref={loadMoreRef} style={{ height: 1 }} />
      {hasMore && (loadingMore ? (
        <p style={{ marginTop: 10, opacity: 0.7 }}>
          Loading more…
        </p>
      ) : null)}
    </div>
  );
}

