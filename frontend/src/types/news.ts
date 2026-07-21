export type Category =
  | "FINANCE"
  | "POLITICS"
  | "HEALTHCARE"
  | "TECHNOLOGY"
  | "MILITARY"
  | "MILITARY_INTELLIGENCE"
  | "WORLD_POPULATION"
  | "WORLD_ECONOMIES";

export type NewsItem = {
  id?: number;            // optional if you don’t want to rely on it yet
  title: string;
  url: string;
  sourceName: string;
  category: Category;
  summary?: string | null; // ✅ add this
  publishedAt: string; // ISO string
};

export type Status = {
  lastRun: string | null;
  lastSuccess: string | null;
  lastError: string | null;
};

export type PageMeta = {
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
};

export type PagedResponse<T> = {
  items: T[];
  page: PageMeta;
};