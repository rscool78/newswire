/*export default function App() {
  return (
    <div style={{ padding: 20 }}>
      <h1>Newswire React Frontend</h1>
      <p>React is wired up correctly.</p>
    </div>
  );
}*/

import { useEffect, useState } from "react";

type NewsItem = { title: string; url: string };

export default function App() {
  const [items, setItems] = useState<NewsItem[]>([]);
  const [status, setStatus] = useState<"loading" | "ready" | "error">("loading");

  useEffect(() => {
    fetch("/api/news")
      .then((r) => r.json())
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

      <ul>
        {items.map((x, i) => (
          <li key={i}>
            <a href={x.url} target="_blank" rel="noreferrer">{x.title}</a>
          </li>
        ))}
      </ul>
    </div>
  );
}


