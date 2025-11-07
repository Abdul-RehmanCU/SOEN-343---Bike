import { useEffect, useMemo, useState } from 'react';
import { motion } from 'framer-motion';
import { useAuth } from '../hooks/useAuth';
import { prcService } from '../services/api';

const currencyFormatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
  maximumFractionDigits: 2,
});

const formatMoney = (value) => {
  if (value === null || value === undefined) {
    return currencyFormatter.format(0);
  }
  const numeric = Number(value);
  if (Number.isNaN(numeric)) {
    return currencyFormatter.format(0);
  }
  return currencyFormatter.format(numeric);
};

const formatDate = (value) => {
  if (!value) {
    return 'Ongoing';
  }
  return new Date(value).toLocaleDateString(undefined, {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  });
};

const formatDateTime = (value) => {
  if (!value) {
    return '—';
  }
  return new Date(value).toLocaleString();
};

const formatDistance = (kilometres) => {
  if (kilometres === null || kilometres === undefined) {
    return '—';
  }
  const numeric = Number(kilometres);
  if (Number.isNaN(numeric)) {
    return '—';
  }
  return `${numeric.toFixed(1)} km`;
};

const Pricing = () => {
  const { user } = useAuth();
  const isAuthenticatedRider = useMemo(() => {
    if (!user) {
      return false;
    }
    return user.role === 'RIDER' || user.role === 'OPERATOR';
  }, [user]);

  const [plans, setPlans] = useState([]);
  const [plansLoading, setPlansLoading] = useState(true);
  const [plansError, setPlansError] = useState(null);

  const [history, setHistory] = useState([]);
  const [historyLoading, setHistoryLoading] = useState(false);
  const [historyError, setHistoryError] = useState(null);

  const [selectedEntryId, setSelectedEntryId] = useState(null);
  const [summary, setSummary] = useState(null);
  const [summaryLoading, setSummaryLoading] = useState(false);
  const [summaryError, setSummaryError] = useState(null);

  useEffect(() => {
    const fetchPlans = async () => {
      setPlansLoading(true);
      setPlansError(null);
      try {
        const { data } = await prcService.getPricingPlans();
        setPlans(data ?? []);
      } catch (error) {
        console.error('Failed to load pricing plans', error);
        setPlansError('Unable to load pricing plans at this time.');
      } finally {
        setPlansLoading(false);
      }
    };

    fetchPlans();
  }, []);

  useEffect(() => {
    if (!isAuthenticatedRider || !user?.id) {
      setHistory([]);
      setSelectedEntryId(null);
      return;
    }

    const fetchHistory = async () => {
      setHistoryLoading(true);
      setHistoryError(null);
      try {
        const { data } = await prcService.getBillingHistory(user.id);
        setHistory(data ?? []);
        setSelectedEntryId((current) => {
          if (!data || data.length === 0) {
            return null;
          }
          if (current && data.some((entry) => entry.ledgerEntryId === current)) {
            return current;
          }
          return data[0].ledgerEntryId;
        });
      } catch (error) {
        console.error('Failed to load billing history', error);
        setHistoryError('Unable to load billing history.');
        setHistory([]);
        setSelectedEntryId(null);
      } finally {
        setHistoryLoading(false);
      }
    };

    fetchHistory();
  }, [isAuthenticatedRider, user]);

  useEffect(() => {
    if (!selectedEntryId) {
      setSummary(null);
      return;
    }

    let isCancelled = false;

    const fetchSummary = async () => {
      setSummaryLoading(true);
      setSummaryError(null);
      try {
        const { data } = await prcService.getTripSummary(selectedEntryId);
        if (!isCancelled) {
          setSummary(data);
        }
      } catch (error) {
        console.error('Failed to load trip summary', error);
        if (!isCancelled) {
          setSummaryError('Unable to load trip summary.');
          setSummary(null);
        }
      } finally {
        if (!isCancelled) {
          setSummaryLoading(false);
        }
      }
    };

    fetchSummary();

    return () => {
      isCancelled = true;
    };
  }, [selectedEntryId]);

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="min-h-screen px-4 sm:px-6 lg:px-8 py-8"
    >
      <div className="max-w-7xl mx-auto space-y-12">
        <header className="space-y-3">
          <h1 className="text-4xl font-bold text-gray-900 dark:text-white">Pricing & Billing</h1>
          <p className="text-gray-600 dark:text-gray-400 max-w-3xl">
            Explore current pricing plans, compare example trip costs, and (when signed in) review your recent billing
            activity.
          </p>
        </header>

        <section>
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-semibold text-gray-900 dark:text-white">Current Pricing Plans</h2>
            {plansLoading && (
              <span className="text-sm text-gray-500 dark:text-gray-400">Loading plans…</span>
            )}
          </div>

          {plansError && (
            <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700 dark:border-red-800 dark:bg-red-900/30 dark:text-red-200">
              {plansError}
            </div>
          )}

          {!plansLoading && plans.length === 0 && !plansError && (
            <div className="rounded-lg border border-gray-200 bg-white px-4 py-6 text-center text-gray-500 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-300">
              No published pricing plans are available yet.
            </div>
          )}

          <div className="grid grid-cols-1 gap-6 md:grid-cols-2 xl:grid-cols-3">
            {plans.map((plan) => (
              <motion.div
                key={plan.planVersionId}
                whileHover={{ scale: 1.01 }}
                transition={{ type: 'spring', stiffness: 260, damping: 20 }}
                className="flex h-full flex-col rounded-2xl border border-gray-200 bg-white p-6 shadow-lg shadow-gray-200/30 dark:border-gray-700 dark:bg-gray-800 dark:shadow-none"
              >
                <div className="flex flex-col gap-3">
                  <div className="flex items-start justify-between gap-4">
                    <div>
                      <h3 className="text-2xl font-semibold text-gray-900 dark:text-white">{plan.planName}</h3>
                      {plan.description && (
                        <p className="mt-1 text-sm text-gray-600 dark:text-gray-400">{plan.description}</p>
                      )}
                    </div>
                    <div className="rounded-lg bg-gray-100 px-3 py-1 text-xs font-medium uppercase tracking-wide text-gray-600 dark:bg-gray-700 dark:text-gray-300">
                      Effective {formatDate(plan.effectiveFrom)}
                    </div>
                  </div>

                  <div className="grid grid-cols-1 gap-4 rounded-xl bg-gray-50 p-4 dark:bg-gray-900/40 sm:grid-cols-3">
                    <div>
                      <span className="text-xs uppercase tracking-wide text-gray-500 dark:text-gray-400">Base Fee</span>
                      <p className="text-lg font-semibold text-gray-900 dark:text-white">{formatMoney(plan.baseFee)}</p>
                    </div>
                    <div>
                      <span className="text-xs uppercase tracking-wide text-gray-500 dark:text-gray-400">Per Minute</span>
                      <p className="text-lg font-semibold text-gray-900 dark:text-white">{formatMoney(plan.perMinuteRate)}</p>
                    </div>
                    <div>
                      <span className="text-xs uppercase tracking-wide text-gray-500 dark:text-gray-400">E-Bike Surcharge</span>
                      <p className="text-lg font-semibold text-gray-900 dark:text-white">{formatMoney(plan.ebikeSurcharge)}</p>
                    </div>
                  </div>

                  <div className="rounded-xl border border-dashed border-gray-200 p-4 dark:border-gray-700">
                    <h4 className="text-sm font-semibold text-gray-900 dark:text-white">Example Trips</h4>
                    <div className="mt-3 grid grid-cols-1 gap-3 sm:grid-cols-2">
                      {(plan.exampleCosts ?? []).map((example) => (
                        <div
                          key={`${example.durationMinutes}-${example.ebike}`}
                          className="rounded-lg bg-gray-100 px-3 py-2 text-sm text-gray-700 dark:bg-gray-900/60 dark:text-gray-300"
                        >
                          <div className="flex items-center justify-between">
                            <span>{example.durationMinutes} min</span>
                            {example.ebike ? (
                              <span className="rounded bg-purple-100 px-2 py-0.5 text-xs font-semibold text-purple-700 dark:bg-purple-900 dark:text-purple-200">
                                E-Bike
                              </span>
                            ) : (
                              <span className="rounded bg-blue-100 px-2 py-0.5 text-xs font-semibold text-blue-700 dark:bg-blue-900 dark:text-blue-200">
                                Standard
                              </span>
                            )}
                          </div>
                          <p className="mt-2 text-lg font-semibold text-gray-900 dark:text-white">
                            {formatMoney(example.estimatedTotal)}
                          </p>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div className="text-xs text-gray-500 dark:text-gray-400">
                    {plan.effectiveTo ? (
                      <>Plan scheduled to end on {formatDate(plan.effectiveTo)}.</>
                    ) : (
                      <>Plan is currently active.</>
                    )}
                  </div>
                </div>
              </motion.div>
            ))}
          </div>
        </section>

        {isAuthenticatedRider && (
          <section className="space-y-6">
            <div className="flex items-center justify-between">
              <h2 className="text-2xl font-semibold text-gray-900 dark:text-white">My Billing History</h2>
              {historyLoading && (
                <span className="text-sm text-gray-500 dark:text-gray-400">Refreshing…</span>
              )}
            </div>

            {historyError && (
              <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700 dark:border-red-800 dark:bg-red-900/30 dark:text-red-200">
                {historyError}
              </div>
            )}

            {!historyLoading && history.length === 0 && !historyError && (
              <div className="rounded-lg border border-gray-200 bg-white px-4 py-6 text-center text-gray-500 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-300">
                You do not have any billing entries yet.
              </div>
            )}

            {history.length > 0 && (
              <div className="grid gap-6 lg:grid-cols-2">
                <div className="space-y-3">
                  {history.map((entry) => {
                    const isActive = entry.ledgerEntryId === selectedEntryId;
                    return (
                      <button
                        key={entry.ledgerEntryId}
                        type="button"
                        onClick={() => setSelectedEntryId(entry.ledgerEntryId)}
                        className={`w-full rounded-2xl border px-4 py-4 text-left transition-transform ${
                          isActive
                            ? 'border-primary-500 bg-primary-50 shadow-lg shadow-primary-200/50 dark:border-primary-400 dark:bg-primary-900/20'
                            : 'border-gray-200 bg-white hover:-translate-y-0.5 hover:border-primary-200 hover:shadow-md dark:border-gray-700 dark:bg-gray-800'
                        }`}
                      >
                        <div className="flex items-center justify-between gap-4">
                          <div>
                            <p className="text-lg font-semibold text-gray-900 dark:text-white">{entry.planName}</p>
                            <p className="text-sm text-gray-500 dark:text-gray-400">{entry.summary}</p>
                          </div>
                          <div className="text-right">
                            <p className="text-lg font-semibold text-gray-900 dark:text-white">{formatMoney(entry.total)}</p>
                            <p className="text-xs text-gray-500 dark:text-gray-400">{formatDateTime(entry.startTime)}</p>
                          </div>
                        </div>
                        <div className="mt-3 flex flex-wrap items-center gap-3 text-xs text-gray-500 dark:text-gray-400">
                          <span>{entry.durationMinutes} minutes</span>
                          <span>•</span>
                          <span>{formatDistance(entry.distanceKm)}</span>
                          <span>•</span>
                          <span>Status: {entry.paymentStatus}</span>
                        </div>
                      </button>
                    );
                  })}
                </div>

                <div className="rounded-2xl border border-gray-200 bg-white p-6 shadow-lg dark:border-gray-700 dark:bg-gray-800">
                  {summaryLoading && (
                    <div className="text-sm text-gray-500 dark:text-gray-400">Loading trip summary…</div>
                  )}

                  {summaryError && (
                    <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700 dark:border-red-800 dark:bg-red-900/30 dark:text-red-200">
                      {summaryError}
                    </div>
                  )}

                  {!summaryLoading && summary && !summaryError && (
                    <div className="space-y-5">
                      <div className="flex items-center justify-between">
                        <div>
                          <p className="text-sm text-gray-500 dark:text-gray-400">Plan</p>
                          <p className="text-xl font-semibold text-gray-900 dark:text-white">{summary.planName}</p>
                        </div>
                        <div className="text-right">
                          <p className="text-sm text-gray-500 dark:text-gray-400">Total</p>
                          <p className="text-2xl font-bold text-gray-900 dark:text-white">{formatMoney(summary.total)}</p>
                        </div>
                      </div>

                      <div className="grid gap-3 rounded-xl bg-gray-50 p-4 dark:bg-gray-900/40">
                        <div className="flex justify-between text-sm text-gray-600 dark:text-gray-300">
                          <span>Start</span>
                          <span>{formatDateTime(summary.startTime)}</span>
                        </div>
                        <div className="flex justify-between text-sm text-gray-600 dark:text-gray-300">
                          <span>End</span>
                          <span>{formatDateTime(summary.endTime)}</span>
                        </div>
                        <div className="flex justify-between text-sm text-gray-600 dark:text-gray-300">
                          <span>Duration</span>
                          <span>{summary.durationMinutes} minutes</span>
                        </div>
                        <div className="flex justify-between text-sm text-gray-600 dark:text-gray-300">
                          <span>Distance</span>
                          <span>{formatDistance(summary.distanceKm)}</span>
                        </div>
                      </div>

                      <div>
                        <h3 className="text-sm font-semibold text-gray-900 dark:text-white">Charge Breakdown</h3>
                        <div className="mt-3 space-y-2">
                          {(summary.charges ?? []).map((charge) => (
                            <div
                              key={`${charge.code}-${charge.amount}`}
                              className="flex items-start justify-between rounded-lg border border-dashed border-gray-200 px-3 py-2 text-sm dark:border-gray-700"
                            >
                              <div>
                                <p className="font-medium text-gray-900 dark:text-white">{charge.code}</p>
                                {charge.meta && Object.keys(charge.meta).length > 0 && (
                                  <div className="mt-1 flex flex-wrap gap-2 text-xs text-gray-500 dark:text-gray-400">
                                    {Object.entries(charge.meta).map(([key, value]) => (
                                      <span
                                        key={`${charge.code}-${key}`}
                                        className="rounded bg-gray-100 px-2 py-0.5 dark:bg-gray-900/60"
                                      >
                                        {key}: {value}
                                      </span>
                                    ))}
                                  </div>
                                )}
                              </div>
                              <p className="text-sm font-semibold text-gray-900 dark:text-white">{formatMoney(charge.amount)}</p>
                            </div>
                          ))}
                        </div>
                      </div>
                    </div>
                  )}

                  {!summaryLoading && !summary && !summaryError && (
                    <div className="text-sm text-gray-500 dark:text-gray-400">
                      Select a billing entry to view detailed charges.
                    </div>
                  )}
                </div>
              </div>
            )}
          </section>
        )}
      </div>
    </motion.div>
  );
};

export default Pricing;
