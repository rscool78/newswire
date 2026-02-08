import { useEffect, useState } from "react";

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

export default function App() {
  const [items, setItems] = useState<NewsItem[]>([]);
  const [status, setStatus] = useState<"loading" | "ready" | "error">("loading");

  useEffect(() => {
    fetch("/api/news")
      .then((r) => {
        if (!r.ok) throw new Error(String(r.status));
        return r.json();
      })
      .then((data) => {
        setItems(Array.isArray(data) ? data : data.items ?? []);
        setStatus("ready");
      })
      .catch(() => setStatus("error"));
  }, []);

  return (
    <div style={{ padding: 20, fontFamily: "system-ui" }}>
      <h1>Newswire</h1>

      {status === "loading" && <p>Loading…</p>}
      {status === "error" && <p>Backend not reachable at <code>/api/news</code></p>}

      <ul style={{ paddingLeft: 18 }}>
        {items.map((x, i) => (
          <li key={i} style={{ marginBottom: 10 }}>
            <a href={x.url} target="_blank" rel="noreferrer">
              {x.title}
            </a>
            <div style={{ fontSize: 12, opacity: 0.75, marginTop: 2 }}>
              {(() => {
                const d = new Date(x.publishedAt);
                const when = isNaN(d.getTime()) ? "Unknown time" : d.toLocaleString();
                return `${x.sourceName} • ${x.category} • ${when}`;
              })()}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
