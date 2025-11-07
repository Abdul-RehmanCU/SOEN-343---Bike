import { motion } from 'framer-motion';
import { useState } from 'react';

const MapLegend = () => {
  const [isExpanded, setIsExpanded] = useState(true);

  return (
    <div className="absolute bottom-4 left-4 z-[1000] bg-white dark:bg-gray-800 rounded-lg shadow-lg border border-gray-200 dark:border-gray-700 overflow-hidden">
      <button
        onClick={() => setIsExpanded(!isExpanded)}
        className="w-full px-4 py-2 flex items-center justify-between hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
      >
        <div className="flex items-center gap-2">
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span className="font-semibold text-sm">Legend</span>
        </div>
        <motion.svg
          animate={{ rotate: isExpanded ? 180 : 0 }}
          transition={{ duration: 0.2 }}
          className="w-4 h-4"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
        </motion.svg>
      </button>

      {isExpanded && (
        <motion.div
          initial={{ height: 0, opacity: 0 }}
          animate={{ height: 'auto', opacity: 1 }}
          exit={{ height: 0, opacity: 0 }}
          className="border-t border-gray-200 dark:border-gray-700 p-4"
        >
          {/* Station Status Colors */}
          <div className="mb-4">
            <h4 className="text-xs font-semibold text-gray-700 dark:text-gray-300 mb-2 uppercase">
              Station Status
            </h4>
            <div className="space-y-2">
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded-full bg-green-500 border-2 border-white shadow"></div>
                <span className="text-sm text-gray-600 dark:text-gray-400">
                  Balanced (25-85% full)
                </span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded-full bg-yellow-500 border-2 border-white shadow"></div>
                <span className="text-sm text-gray-600 dark:text-gray-400">
                  At-risk (&lt;25% or &gt;85% full)
                </span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded-full bg-red-500 border-2 border-white shadow"></div>
                <span className="text-sm text-gray-600 dark:text-gray-400">
                  Empty or Full (0% or 100%)
                </span>
              </div>
            </div>
          </div>

          {/* Bike Types */}
          <div className="mb-4">
            <h4 className="text-xs font-semibold text-gray-700 dark:text-gray-300 mb-2 uppercase">
              Bike Types
            </h4>
            <div className="space-y-2">
              <div className="flex items-center gap-2">
                <div className="w-5 h-5 bg-blue-400 rounded flex items-center justify-center text-white font-bold text-xs">S</div>
                <span className="text-sm text-gray-600 dark:text-gray-400">
                  Standard Bike
                </span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-5 h-5 bg-yellow-400 rounded flex items-center justify-center text-white font-bold text-xs">E</div>
                <span className="text-sm text-gray-600 dark:text-gray-400">
                  E-Bike (Electric)
                </span>
              </div>
            </div>
          </div>

          {/* Dock Status */}
          <div>
            <h4 className="text-xs font-semibold text-gray-700 dark:text-gray-300 mb-2 uppercase">
              Dock Status
            </h4>
            <div className="space-y-2">
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 bg-gray-800 dark:bg-gray-700 rounded border-2 border-gray-600"></div>
                <span className="text-sm text-gray-600 dark:text-gray-400">
                  Occupied (bike present)
                </span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 bg-gray-200 dark:bg-gray-600 rounded border-2 border-gray-300 dark:border-gray-500"></div>
                <span className="text-sm text-gray-600 dark:text-gray-400">
                  Empty (available)
                </span>
              </div>
            </div>
          </div>

          {/* Instructions */}
          <div className="mt-4 pt-4 border-t border-gray-200 dark:border-gray-700">
            <p className="text-xs text-gray-500 dark:text-gray-400 italic">
              Click on station markers to view details and perform actions
            </p>
          </div>
        </motion.div>
      )}
    </div>
  );
};

export default MapLegend;

