import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import DashboardHome from './DashboardHome';
import MapDashboard from './MapDashboard';

const Dashboard = () => {
  const [view, setView] = useState('home'); // 'home' or 'map'

  return (
    <div className="relative">
      {/* View Toggle Button - Only show when in map view */}
      <AnimatePresence>
        {view === 'map' && (
          <motion.button
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            onClick={() => setView('home')}
            className="fixed top-24 right-6 z-[100] bg-white/95 dark:bg-gray-800/95 backdrop-blur-xl px-4 py-2.5 rounded-xl shadow-lg border border-gray-200/50 dark:border-gray-700/50 hover:shadow-xl transition-all group"
          >
            <div className="flex items-center gap-2">
              <svg className="w-4 h-4 text-gray-600 dark:text-gray-400 group-hover:text-primary-900 dark:group-hover:text-gray-100 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
              <span className="text-sm font-medium text-gray-700 dark:text-gray-300 group-hover:text-primary-900 dark:group-hover:text-gray-100 transition-colors">
                Back to Home
              </span>
            </div>
          </motion.button>
        )}
      </AnimatePresence>

      {/* Content */}
      <AnimatePresence mode="wait">
        {view === 'home' ? (
          <motion.div
            key="home"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.3 }}
          >
            <DashboardHome onSwitchToMap={() => setView('map')} />
          </motion.div>
        ) : (
          <motion.div
            key="map"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.3 }}
          >
            <MapDashboard />
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default Dashboard;
