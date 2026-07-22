import {
  useCallback,
  useEffect,
  useMemo,
  useState,
} from "react";

import {
  getNews,
  refreshNews,
} from "../services/newsApi";

import type {
  Category,
  NewsItem,
  PageMeta,
} from "../types/news";

const PAGE_SIZE = 50;
const SEARCH_DELAY_MS = 400;

type NewsCategory = "ALL" | Category;

type UseNewsResult = {
  items: NewsItem[];
  page: number;
  pageMeta: PageMeta | null;

  category: NewsCategory;
  setCategory: (category: NewsCategory) => void;

  query: string;
  setQuery: (query: string) => void;

  loading: boolean;
  loadingMore: boolean;
  refreshing: boolean;
  error: string | null;
  hasMore: boolean;

  goPrev: () => Promise<void>;
  goNext: () => Promise<void>;
  loadMore: () => Promise<void>;
  refreshNow: () => Promise<void>;
};

function getErrorMessage(
  error: unknown,
  fallback: string,
): string {
  return error instanceof Error
    ? error.message
    : fallback;
}

export function useNews(): UseNewsResult {
  const [items, setItems] = useState<NewsItem[]>([]);
  const [page, setPage] = useState(0);
  const [pageMeta, setPageMeta] =
    useState<PageMeta | null>(null);

  const [category, setCategory] =
    useState<NewsCategory>("ALL");

  const [query, setQuery] = useState("");
  const [debouncedQuery, setDebouncedQuery] =
    useState("");

  const [loading, setLoading] = useState(false);
  const [loadingMore, setLoadingMore] =
    useState(false);
  const [refreshing, setRefreshing] =
    useState(false);

  const [error, setError] =
    useState<string | null>(null);

  const hasMore = useMemo(() => {
    if (!pageMeta) {
      return false;
    }

    return page < pageMeta.totalPages - 1;
  }, [page, pageMeta]);

  const loadNewsPage = useCallback(
    async (
      selectedCategory: NewsCategory,
      pageNumber: number,
      append = false,
    ): Promise<void> => {
      if (append) {
        setLoadingMore(true);
      } else {
        setLoading(true);
      }

      setError(null);

      try {
        const data = await getNews({
          category: selectedCategory,
          page: pageNumber,
          size: PAGE_SIZE,
          query: debouncedQuery,
        });

        const newItems = Array.isArray(data.items)
          ? data.items
          : [];

        setPageMeta(data.page ?? null);
        setPage(pageNumber);

        if (append) {
          setItems((currentItems) => [
            ...currentItems,
            ...newItems,
          ]);
        } else {
          setItems(newItems);
        }
      } catch (loadError: unknown) {
        setError(
          getErrorMessage(
            loadError,
            "Failed to load news",
          ),
        );
      } finally {
        if (append) {
          setLoadingMore(false);
        } else {
          setLoading(false);
        }
      }
    },
    [debouncedQuery],
  );

  const goPrev = useCallback(async () => {
    const previousPage = Math.max(0, page - 1);

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });

    setItems([]);

    await loadNewsPage(
      category,
      previousPage,
    );
  }, [category, loadNewsPage, page]);

  const goNext = useCallback(async () => {
    const finalPage = pageMeta
      ? Math.max(0, pageMeta.totalPages - 1)
      : page + 1;

    const nextPage = Math.min(
      finalPage,
      page + 1,
    );

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });

    setItems([]);

    await loadNewsPage(category, nextPage);
  }, [
    category,
    loadNewsPage,
    page,
    pageMeta,
  ]);

  const loadMore = useCallback(async () => {
    if (
      !hasMore ||
      loading ||
      loadingMore ||
      refreshing
    ) {
      return;
    }

    await loadNewsPage(
      category,
      page + 1,
      true,
    );
  }, [
    category,
    hasMore,
    loadNewsPage,
    loading,
    loadingMore,
    page,
    refreshing,
  ]);

  const refreshNow = useCallback(async () => {
    setRefreshing(true);
    setError(null);

    try {
      await refreshNews();

      await loadNewsPage(
        category,
        page,
      );
    } catch (refreshError: unknown) {
      setError(
        getErrorMessage(
          refreshError,
          "Refresh failed",
        ),
      );
    } finally {
      setRefreshing(false);
    }
  }, [
    category,
    loadNewsPage,
    page,
  ]);

  useEffect(() => {
    const timer = window.setTimeout(() => {
      setDebouncedQuery(query);
    }, SEARCH_DELAY_MS);

    return () => window.clearTimeout(timer);
  }, [query]);

  useEffect(() => {
    setPage(0);
    setPageMeta(null);
    setItems([]);

    void loadNewsPage(category, 0);
  }, [
    category,
    debouncedQuery,
    loadNewsPage,
  ]);

  return {
    items,
    page,
    pageMeta,

    category,
    setCategory,

    query,
    setQuery,

    loading,
    loadingMore,
    refreshing,
    error,
    hasMore,

    goPrev,
    goNext,
    loadMore,
    refreshNow,
  };
}