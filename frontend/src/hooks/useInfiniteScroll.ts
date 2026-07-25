import { useEffect, useRef } from "react";

type UseInfiniteScrollOptions = {
  enabled: boolean;
  loading: boolean;
  onLoadMore: () => Promise<void>;
};

/*
 * Encapsulates the IntersectionObserver used for
 * infinite scrolling.
 *
 * The hook returns a ref that should be attached
 * to an invisible "sentinel" element located at
 * the bottom of the scrolling content.
 */
export function useInfiniteScroll({
  enabled,
  loading,
  onLoadMore,
}: UseInfiniteScrollOptions) {
  const sentinelRef =
    useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const element = sentinelRef.current;

    /*
     * Do not observe when:
     * - there are no more pages
     * - another request is already running
     */
    if (!element || !enabled || loading) {
      return;
    }

    const observer = new IntersectionObserver(
      (entries) => {
        const first = entries[0];

        if (first.isIntersecting) {
          void onLoadMore();
        }
      },
      {
        root: null,
        rootMargin: "600px",
        threshold: 0,
      },
    );

    observer.observe(element);

    return () => observer.disconnect();
  }, [
    enabled,
    loading,
    onLoadMore,
  ]);

  return sentinelRef;
}