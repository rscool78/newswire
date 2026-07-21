import type { CSSProperties } from "react";
import type { NewsItem } from "../types/news";

type NewsCardProps = {
  item: NewsItem;
};

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

  return `${Math.floor(hr / 24)}d ago`;
}

const badgeStyle: CSSProperties = {
  fontSize: 12,
  padding: "2px 8px",
  borderRadius: 999,
  background: "rgba(0,0,0,0.06)",
};

export default function NewsCard({ item }: NewsCardProps) {
  const published = new Date(item.publishedAt);

  const publishedText = isNaN(published.getTime())
    ? "Unknown time"
    : published.toLocaleString();

  return (
    <li
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
          href={item.url}
          target="_blank"
          rel="noreferrer"
          style={{
            fontWeight: 650,
            textDecoration: "none",
            color: "inherit",
            lineHeight: 1.25,
          }}
        >
          {item.title}
        </a>

        <span
          style={{
            marginLeft: "auto",
            fontSize: 12,
            opacity: 0.7,
            whiteSpace: "nowrap",
          }}
          title={publishedText}
        >
          {relativeFromNow(item.publishedAt)}
        </span>
      </div>

      <div
        style={{
          marginTop: 8,
          display: "flex",
          gap: 8,
          flexWrap: "wrap",
        }}
      >
        <span style={badgeStyle}>{item.sourceName}</span>
        <span style={badgeStyle}>{item.category}</span>

        <span style={{ fontSize: 12, opacity: 0.7 }}>
          {publishedText}
        </span>
      </div>

      {item.summary && (
        <div
          style={{
            marginTop: 10,
            fontSize: 13,
            opacity: 0.92,
            lineHeight: 1.4,
          }}
        >
          {item.summary.length > 240
            ? item.summary.slice(0, 240) + "…"
            : item.summary}
        </div>
      )}
    </li>
  );
}