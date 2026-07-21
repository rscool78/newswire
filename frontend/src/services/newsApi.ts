import type {
  Category,
  NewsItem,
  PagedResponse,
  Status,
} from "../types/news";

type GetNewsOptions = {
  category: "ALL" | Category;
  page: number;
  size: number;
  query?: string;
};

export async function getNews({
  category,
  page,
  size,
  query = "",
}: GetNewsOptions): Promise<PagedResponse<NewsItem>> {
  const params = new URLSearchParams({
    page: String(page),
    size: String(size),
  });

  if (category !== "ALL") {
    params.set("category", category);
  }

  if (query.trim()) {
    params.set("q", query.trim());
  }

  const response = await fetch(`/api/news?${params.toString()}`);

  if (!response.ok) {
    throw new Error(`GET /api/news failed: ${response.status}`);
  }

  return response.json() as Promise<PagedResponse<NewsItem>>;
}

export async function getStatus(): Promise<Status> {
  const response = await fetch("/api/news/status");

  if (!response.ok) {
    throw new Error(
      `GET /api/news/status failed: ${response.status}`,
    );
  }

  return response.json() as Promise<Status>;
}

export async function refreshNews(): Promise<string> {
  const response = await fetch("/api/news/refresh", {
    method: "POST",
  });

  if (!response.ok) {
    throw new Error(
      `POST /api/news/refresh failed: ${response.status}`,
    );
  }

  return response.text();
}