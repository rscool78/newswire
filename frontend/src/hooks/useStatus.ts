import { useCallback, useEffect, useState } from "react";

import { getStatus } from "../services/newsApi";
import type { Status } from "../types/news";

const EMPTY_STATUS: Status = {
  lastRun: null,
  lastSuccess: null,
  lastError: null,
};

type UseStatusResult = {
  status: Status;
  loadStatus: () => Promise<void>;
};

export function useStatus(
  pollingIntervalMs = 30_000,
): UseStatusResult {
  const [status, setStatus] = useState<Status>(EMPTY_STATUS);

  const loadStatus = useCallback(async () => {
    try {
      const data = await getStatus();

      setStatus({
        lastRun: data?.lastRun ?? null,
        lastSuccess: data?.lastSuccess ?? null,
        lastError: data?.lastError ?? null,
      });
    } catch {
      // Status failures should not prevent the news UI from working.
    }
  }, []);

  useEffect(() => {
    void loadStatus();

    const timer = window.setInterval(
      () => void loadStatus(),
      pollingIntervalMs,
    );

    return () => window.clearInterval(timer);
  }, [loadStatus, pollingIntervalMs]);

  return {
    status,
    loadStatus,
  };
}