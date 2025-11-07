import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { PAGE_VARIANTS } from '../constants/animations';
import { useAuth } from '../hooks/useAuth';
import { prcService } from '../services/api';

const Home = () => {
  const { user } = useAuth();
  const isGuest = !user || !['RIDER', 'OPERATOR'].includes(user.role);
  const [previewPlans, setPreviewPlans] = useState([]);
  const [previewError, setPreviewError] = useState(null);

  const formatMoney = (raw) => {
    const value = Number(raw ?? 0);
    if (Number.isNaN(value)) {
      return '$0.00';
    }
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      maximumFractionDigits: 2,
    }).format(value);
  };

  useEffect(() => {
    if (!isGuest) {
      return;
    }

    let cancelled = false;
    (async () => {
      try {
        const { data } = await prcService.getPricingPlans();
        if (!cancelled) {
          setPreviewPlans(Array.isArray(data) ? data.slice(0, 3) : []);
          setPreviewError(null);
        }
      } catch (error) {
        console.error('Unable to fetch pricing preview', error);
        if (!cancelled) {
          setPreviewError('Pricing is currently unavailable.');
          setPreviewPlans([]);
        }
      }
    })();

    return () => {
      cancelled = true;
    };
  }, [isGuest]);

  return (
    <motion.div
      variants={PAGE_VARIANTS}
      initial="initial"
      animate="animate"
      exit="exit"
      transition={{ duration: 0.4, ease: 'easeInOut' }}
      className="bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100"
    >
      <section className="relative min-h-[calc(100vh-5rem)] flex items-center justify-center overflow-hidden px-4 sm:px-6 lg:px-8">
        <div className="absolute inset-0 bg-[linear-gradient(to_right,#80808012_1px,transparent_1px),linear-gradient(to_bottom,#80808012_1px,transparent_1px)] bg-[size:64px_64px] dark:bg-[linear-gradient(to_right,#ffffff20_1px,transparent_1px),linear-gradient(to_bottom,#ffffff20_1px,transparent_1px)]" />

        <motion.div
          animate={{
            x: [0, 80, 0],
            y: [0, -60, 0],
            scale: [1, 1.25, 1],
            opacity: [0.4, 0.6, 0.4],
          }}
          transition={{ duration: 14, repeat: Infinity, ease: 'easeInOut' }}
          className="absolute top-1/4 right-1/4 h-[600px] w-[600px] rounded-full bg-gradient-to-br from-gray-500/40 via-gray-400/30 to-transparent blur-3xl dark:from-gray-400/50 dark:via-gray-300/35"
        />

        <motion.div
          animate={{
            x: [0, -80, 0],
            y: [0, 60, 0],
            scale: [1.25, 1, 1.25],
            opacity: [0.35, 0.55, 0.35],
          }}
          transition={{ duration: 18, repeat: Infinity, ease: 'easeInOut' }}
          className="absolute bottom-1/4 left-1/4 h-[550px] w-[550px] rounded-full bg-gradient-to-br from-gray-400/35 via-gray-500/28 to-transparent blur-3xl dark:from-gray-500/45 dark:via-gray-400/32"
        />

        {[...Array(12)].map((_, i) => (
          <motion.div
            key={i}
            animate={{
              y: [0, -60, 0],
              opacity: [0.15, 0.4, 0.15],
              rotate: [0, 180, 360],
            }}
            transition={{
              duration: 5 + i * 0.8,
              repeat: Infinity,
              ease: 'easeInOut',
              delay: i * 0.4,
            }}
            className="absolute rounded-sm bg-primary-900/20 dark:bg-gray-400/15"
            style={{
              left: `${8 + i * 8}%`,
              top: `${12 + (i % 4) * 20}%`,
              width: i % 2 === 0 ? '4px' : '6px',
              height: i % 2 === 0 ? '4px' : '6px',
            }}
          />
        ))}

        <div className="relative z-10 mx-auto w-full max-w-5xl text-center">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, delay: 0.2 }}
          >
            <motion.h1
              className="mb-8 text-6xl font-black tracking-tighter text-transparent md:text-7xl lg:text-8xl bg-gradient-to-r from-primary-900 via-primary-700 to-primary-900 dark:from-gray-50 dark:via-gray-100 dark:to-gray-50 bg-clip-text"
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.6, delay: 0.4 }}
            >
              QwikRide
            </motion.h1>

            <motion.div
              initial={{ opacity: 0, width: 0 }}
              animate={{ opacity: 1, width: '200px' }}
              transition={{ duration: 0.8, delay: 0.6 }}
              className="mx-auto mb-8 h-1 bg-gradient-to-r from-transparent via-primary-500 to-transparent dark:via-gray-400"
            />

            <motion.p
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ duration: 0.8, delay: 0.7 }}
              className="mx-auto mb-14 max-w-3xl text-xl font-light leading-relaxed text-gray-600 dark:text-gray-300 md:text-2xl"
            >
              Experience the future of urban transportation with eco-conscious bike sharing and responsive customer care.
            </motion.p>

            {isGuest ? (
              <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: 0.9 }}
                className="flex justify-center gap-5"
              >
                <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
                  <Link to="/register" className="btn-primary">
                    Create account
                  </Link>
                </motion.div>
                <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
                  <Link to="/pricing" className="btn-secondary">
                    Explore pricing
                  </Link>
                </motion.div>
              </motion.div>
            ) : (
              <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: 0.9 }}
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
              >
                <Link to="/dashboard" className="btn-primary inline-flex items-center gap-2">
                  Dashboard
                  <svg className="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                  </svg>
                </Link>
              </motion.div>
            )}
          </motion.div>
        </div>
      </section>

      {isGuest && (
        <>
          <motion.section
            id="guest-about"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 0.5 }}
            className="mx-auto max-w-5xl px-6 py-20 text-center"
          >
            <h2 className="text-3xl font-bold">Why riders choose QwikRide</h2>
            <p className="mx-auto mt-4 max-w-3xl text-lg text-gray-600 dark:text-gray-300">
              Our network of smart docks keeps you moving—no traffic, no emissions, and no complicated checkouts. Unlock a bike in seconds, enjoy polished customer support, and keep tabs on every trip through transparent billing.
            </p>
          </motion.section>

          <motion.section
            id="guest-how-it-works"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 0.5 }}
            className="bg-gray-50 px-6 py-20 dark:bg-gray-800"
          >
            <div className="mx-auto max-w-6xl">
              <h2 className="mb-12 text-center text-3xl font-bold">Ride in three simple steps</h2>
              <div className="grid gap-8 md:grid-cols-3">
                {[
                  {
                    title: 'Locate',
                    description: 'Check the live station map to spot available bikes near you.',
                  },
                  {
                    title: 'Reserve',
                    description: 'Book and unlock within the app using your phone—no kiosk wait times.',
                  },
                  {
                    title: 'Ride',
                    description: 'Cruise across the city and return at any dock when you are done.',
                  },
                ].map((item) => (
                  <div
                    key={item.title}
                    className="rounded-2xl bg-white p-6 shadow-sm transition hover:shadow-md dark:bg-gray-900"
                  >
                    <p className="text-sm font-semibold uppercase tracking-wide text-primary-600">Step</p>
                    <h3 className="mt-2 text-xl font-semibold">{item.title}</h3>
                    <p className="mt-2 text-gray-600 dark:text-gray-400">{item.description}</p>
                  </div>
                ))}
              </div>
            </div>
          </motion.section>

          <motion.section
            id="guest-pricing"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 0.5 }}
            className="mx-auto max-w-6xl px-6 py-20"
          >
            <h2 className="mb-10 text-center text-3xl font-bold">Membership options</h2>
            {previewError && (
              <p className="mb-6 text-center text-sm text-red-600 dark:text-red-400">{previewError}</p>
            )}
            <div className="grid gap-8 md:grid-cols-3">
              {(previewPlans.length > 0 ? previewPlans : [
                {
                  planVersionId: 'placeholder-casual',
                  planName: 'Casual Rider',
                  baseFee: 0,
                  perMinuteRate: 0.35,
                  description: 'Pay as you go—perfect for occasional rides.',
                },
                {
                  planVersionId: 'placeholder-monthly',
                  planName: 'Urban Pass',
                  baseFee: 15,
                  perMinuteRate: 0.15,
                  description: 'Unlimited unlocks plus reduced per-minute pricing.',
                },
                {
                  planVersionId: 'placeholder-annual',
                  planName: 'Annual Explorer',
                  baseFee: 99,
                  perMinuteRate: 0.1,
                  description: 'Best value for commuters and enthusiasts alike.',
                },
              ]).map((plan) => (
                <div
                  key={plan.planVersionId}
                  className="rounded-2xl border border-gray-200 p-6 shadow-sm transition hover:shadow-md dark:border-gray-700 dark:bg-gray-800"
                >
                  <h3 className="text-2xl font-semibold text-center">{plan.planName}</h3>
                  <p className="mt-4 text-center text-3xl font-bold">{formatMoney(plan.baseFee)}</p>
                  <p className="mt-2 text-center text-sm text-gray-600 dark:text-gray-400">
                    {plan.perMinuteRate !== null && plan.perMinuteRate !== undefined
                      ? `${formatMoney(plan.perMinuteRate)} per minute`
                      : 'Flat fare pricing'}
                  </p>
                  {plan.description && (
                    <p className="mt-6 text-center text-gray-600 dark:text-gray-400">{plan.description}</p>
                  )}
                  <div className="mt-8 text-center">
                    <Link
                      to="/register"
                      className="inline-flex rounded-xl bg-primary-600 px-6 py-2 text-white transition hover:bg-primary-700"
                    >
                      Join now
                    </Link>
                  </div>
                </div>
              ))}
            </div>
            <p className="mt-8 text-center text-sm text-gray-500 dark:text-gray-400">
              Want the full breakdown? Visit the{' '}
              <Link to="/pricing" className="text-primary-600 underline-offset-4 hover:underline dark:text-primary-300">
                pricing hub
              </Link>
              .
            </p>
          </motion.section>

          <motion.section
            id="guest-map"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.3 }}
            transition={{ duration: 0.5 }}
            className="bg-gray-50 px-6 py-20 dark:bg-gray-800"
          >
            <div className="mx-auto max-w-6xl">
              <h2 className="mb-10 text-center text-3xl font-bold">Station coverage</h2>
              <div className="flex h-80 items-center justify-center rounded-2xl bg-gray-200 text-gray-600 dark:bg-gray-900 dark:text-gray-300">
                Interactive city map coming soon.
              </div>
            </div>
          </motion.section>
        </>
      )}
    </motion.div>
  );
};

export default Home;