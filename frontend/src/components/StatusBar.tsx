import type { Status } from "../types/news";

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

  return `${Math.floor(hr / 24)}d ago`;
}

interface StatusBarProps {
  status: Status;
}

export default function StatusBar({ status }: StatusBarProps) {
  return (
    <div style={{ marginTop: 8, fontSize: 13, opacity: 0.75 }}>
      <span>
        Last success: <strong>{formatWhen(status.lastSuccess)}</strong>{" "}
        {status.lastSuccess
          ? `(${relativeFromNow(status.lastSuccess)})`
          : ""}
      </span>

      <span style={{ marginLeft: 12 }}>
        Last run: <strong>{formatWhen(status.lastRun)}</strong>
      </span>

      {status.lastError && (
        <span style={{ marginLeft: 12 }}>
          Error: <strong>{status.lastError}</strong>
        </span>
      )}
    </div>
  );
}