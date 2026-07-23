interface RefreshButtonProps {
  refreshing: boolean;
  loading: boolean;
  onClick: () => void;
}

export default function RefreshButton({
  refreshing,
  loading,
  onClick,
}: RefreshButtonProps) {
  return (
    <button
      onClick={onClick}
      disabled={refreshing || loading}
      style={{
        padding: "8px 12px",
        borderRadius: 10,
        cursor:
          refreshing || loading
            ? "not-allowed"
            : "pointer",
      }}
      title="Fetch RSS and store new articles"
    >
      {refreshing ? "Refreshing…" : "Refresh now"}
    </button>
  );
}