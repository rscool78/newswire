import { useCallback } from "react";

import NewsCard from "./components/NewsCard";
import StatusBar from "./components/StatusBar";
import SearchBar from "./components/SearchBar";
import CategoryFilter from "./components/CategoryFilter";
import RefreshButton from "./components/RefreshButton";
import Pagination from "./components/Pagination";
import { useInfiniteScroll } from "./hooks/useInfiniteScroll";
import { useNews } from "./hooks/useNews";
import { useStatus } from "./hooks/useStatus";

export default function App() {
  /*
   * useNews manages all article-related state and behavior.
   *
   * This includes:
   * - loading articles from the backend
   * - article search
   * - category filtering
   * - pagination
   * - infinite scrolling
   * - manual refresh
   * - loading and error states
   */
  const {
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
  } = useNews();

  /*
   * useStatus manages the backend refresh status.
   *
   * The status contains values such as:
   * - last refresh run
   * - last successful refresh
   * - last refresh error
   */
  const {
    status,
    loadStatus,
  } = useStatus();

    /*
    * The hook returns a ref that is attached to an
    * invisible element at the bottom of the page.
    *
    * When that element approaches the viewport,
    * the next page is automatically requested.
    */
    const loadMoreRef = useInfiniteScroll({
      enabled: hasMore,
      loading:
        loading ||
        loadingMore ||
        refreshing,
      onLoadMore: loadMore,
    });

      /*
    * Refresh the backend feed, reload the current article page,
    * and then update the Last Run and Last Success timestamps.
    */
    const handleRefresh = useCallback(async () => {
      await refreshNow();
      await loadStatus();
    }, [loadStatus, refreshNow]);

       

  return (
    <div
      style={{
        /*
         * Keep the page content centered and prevent
         * it from becoming too wide on large screens.
         */
        width: "100%",
        maxWidth: 1200,
        margin: "0 auto",
        padding: "24px 32px",
        boxSizing: "border-box",
        fontFamily: "system-ui",
      }}
    >
      <header
        style={{
          /*
           * Place the title/status on the left and
           * toolbar controls on the right.
           */
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          gap: 24,

          /*
           * Allow the toolbar to move below the title
           * when the browser window becomes narrow.
           */
          flexWrap: "wrap",
        }}
      >
        <div>
          <h1 style={{ margin: 0 }}>
            Newswire
          </h1>

          {/*
           * Displays backend ingestion information
           * below the application title.
           */}
          <StatusBar status={status} />
        </div>

        <div
          style={{
            /*
             * Arrange search, category, and refresh
             * controls in one responsive row.
             */
            display: "flex",
            gap: 10,
            alignItems: "center",
            flexWrap: "wrap",
          }}
        >
          {/*
           * Controlled search input.
           *
           * query comes from useNews and setQuery updates it.
           * The hook handles the search debounce.
           */}
          <SearchBar
            value={query}
            onChange={setQuery}
            disabled={loading || refreshing}
          />

          {/*
           * Controlled category selector.
           *
           * Changing the category causes useNews to
           * reset to page zero and reload articles.
           */}
          <CategoryFilter
            value={category}
            onChange={setCategory}
            disabled={loading || refreshing}
          />

          {/*
           * Triggers backend ingestion and reloads
           * both articles and status information.
           */}
          <RefreshButton
            refreshing={refreshing}
            loading={loading}
            onClick={handleRefresh}
          />
        </div>
      </header>

      {/*
       * Show backend or network errors when present.
       */}
      {error ? (
        <p style={{ marginTop: 12 }}>
          <strong>Warning:</strong>{" "}
          {error}
        </p>
      ) : null}

      {/*
       * Show an initial loading message while a full
       * page of articles is being retrieved.
       */}
      {loading ? (
        <p style={{ marginTop: 12 }}>
          Loading…
        </p>
      ) : null}

      {/*
       * Previous and Next page controls.
       *
       * The Pagination component determines whether
       * each button should be disabled.
       */}
      <Pagination
        page={page}
        pageMeta={pageMeta}
        loading={loading}
        refreshing={refreshing}
        onPrevious={goPrev}
        onNext={goNext}
      />

      {/*
       * Render the article list.
       *
       * NewsCard is responsible for displaying the
       * title, summary, source, category, and date.
       */}
      <ul
        style={{
          marginTop: 14,
          paddingLeft: 0,
          listStyle: "none",
        }}
      >
        {items.map((item, index) => (
          <NewsCard
            /*
             * Prefer the database ID as the React key.
             *
             * The URL and index are used only as a fallback
             * when the article does not have an ID.
             */
            key={
              item.id ??
              `${item.url}-${index}`
            }
            item={item}
          />
        ))}
      </ul>

      {/*
       * Invisible infinite-scroll sentinel.
       *
       * The useInfiniteScroll hook watches this element
       * and calls loadMore when it approaches the viewport.
       */}
      <div
        ref={loadMoreRef}
        style={{ height: 1 }}
      />

      {/*
       * Show a separate message when additional pages
       * are loading below the existing article list.
       */}
      {hasMore && loadingMore ? (
        <p
          style={{
            marginTop: 10,
            opacity: 0.7,
          }}
        >
          Loading more…
        </p>
      ) : null}
    </div>
  );
}