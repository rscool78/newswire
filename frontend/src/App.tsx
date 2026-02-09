import { useEffect, useMemo, useState } from "react";

type Category =
  | "FINANCE"
  | "POLITICS"
  | "HEALTHCARE"
  | "TECHNOLOGY"
  | "MILITARY"
  | "MILITARY_INTELLIGENCE"
  | "WORLD_POPULATION"
  | "WORLD_ECONOMIES";

type NewsItem = {
  title: string;
  url: string;
  sourceName: string;
  category: Category;
  publishedAt: string; // ISO string
};

type Status = {
  lastRun: string | null;
  lastSuccess: string | null;
  lastError: string | null;
};

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
  const [items, setItems] = useState<NewsItem[]>([]);
  const [status, setStatus] = useState<Status>({
    lastRun: null,
    lastSuccess: null,
    lastError: null,
  });

  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [category, setCategory] = useState<"ALL" | Category>("ALL");

  async function loadNews(selectedCategory: "ALL" | Category = category) {
    setLoading(true);
    setError(null);
    try {
      const qs =
        selectedCategory === "ALL"
          ? "/api/news?size=50"
          : `/api/news?size=50&category=${encodeURIComponent(selectedCategory)}`;
      const r = await fetch(qs);
      if (!r.ok) throw new Error(`GET ${qs} failed: ${r.status}`);
      const data = await r.json();
      setItems(Array.isArray(data) ? data : []);
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

  async function refreshNow() {
    setRefreshing(true);
    setError(null);
    try {
      const r = await fetch("/api/news/refresh", { method: "POST" });
      if (!r.ok) throw new Error(`POST /api/news/refresh failed: ${r.status}`);
      // After refresh: reload status + news
      await Promise.all([loadStatus(), loadNews(category)]);
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
    loadNews(category);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [category]);

  const categoriesInList = useMemo(() => {
    const s = new Set<Category>();
    for (const x of items) s.add(x.category);
    return Array.from(s).sort();
  }, [items]);

  return (
    <div style={{ padding: 24, fontFamily: "system-ui" }}>
      <div style={{ display: "flex", alignItems: "baseline", gap: 16 }}>
        <h1 style={{ margin: 0 }}>Newswire</h1>

        <div style={{ marginLeft: "auto", display: "flex", gap: 10, alignItems: "center" }}>
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
              {(categoriesInList.length ? categoriesInList : (["FINANCE", "POLITICS", "TECHNOLOGY"] as Category[])).map(
                (c) => (
                  <option key={c} value={c}>
                    {c}
                  </option>
                )
              )}
            </select>
          </label>

          <button
            onClick={refreshNow}
            disabled={refreshing}
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

      <ul style={{ marginTop: 14, paddingLeft: 18 }}>
        {items.map((x, i) => (
          <li key={i} style={{ marginBottom: 12 }}>
            <a href={x.url} target="_blank" rel="noreferrer">
              {x.title}
            </a>
            <div style={{ fontSize: 12, opacity: 0.75, marginTop: 2 }}>
              {x.sourceName} • {x.category} •{" "}
              {(() => {
                const d = new Date(x.publishedAt);
                return isNaN(d.getTime()) ? "Unknown time" : d.toLocaleString();
              })()}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

