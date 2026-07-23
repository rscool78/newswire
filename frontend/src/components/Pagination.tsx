import type { PageMeta } from "../types/news";

interface PaginationProps {
  page: number;
  pageMeta: PageMeta | null;
  loading: boolean;
  refreshing: boolean;
  onPrevious: () => void;
  onNext: () => void;
}

export default function Pagination({
  page,
  pageMeta,
  loading,
  refreshing,
  onPrevious,
  onNext,
}: PaginationProps) {
  const previousDisabled =
    loading || refreshing || page === 0;

  const nextDisabled =
    loading ||
    refreshing ||
    (pageMeta
      ? page >= pageMeta.totalPages - 1
      : false);

  return (
    <div
      style={{
        marginTop: 12,
        display: "flex",
        gap: 8,
        alignItems: "center",
      }}
    >
      <button
        type="button"
        onClick={onPrevious}
        disabled={previousDisabled}
        style={{
          padding: "6px 10px",
          borderRadius: 10,
        }}
      >
        ← Prev
      </button>

      <div style={{ fontSize: 13, opacity: 0.8 }}>
        Page{" "}
        <strong>
          {(pageMeta?.number ?? page) + 1}
        </strong>

        {pageMeta ? (
          <>
            {" "}
            of <strong>{pageMeta.totalPages}</strong>
            {" • "}
            <strong>{pageMeta.totalElements}</strong>{" "}
            articles
          </>
        ) : null}
      </div>

      <button
        type="button"
        onClick={onNext}
        disabled={nextDisabled}
        style={{
          padding: "6px 10px",
          borderRadius: 10,
          marginLeft: "auto",
        }}
      >
        Next →
      </button>
    </div>
  );
}