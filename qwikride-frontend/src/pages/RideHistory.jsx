import { useState, useEffect } from 'react';
import { useAuth } from '../hooks/useAuth';
import { motion } from 'framer-motion';
import { rideHistoryService } from '../services/api';
import { format } from 'date-fns';

const RideHistory = () => {
  const { user } = useAuth();
  const [rideHistory, setRideHistory] = useState([]);
  const [statistics, setStatistics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({
    startDate: '',
    endDate: '',
    status: '',
    bikeType: '',
  });

  useEffect(() => {
    if (user?.id) {
      fetchRideHistory();
      fetchStatistics();
    }
  }, [user?.id]);

  // Re-fetch when filters change
  useEffect(() => {
    if (user?.id) {
      fetchRideHistory();
    }
  }, [filters]);

  const fetchRideHistory = async () => {
    if (!user?.id) {
      setError('User not found. Please log in again.');
      setLoading(false);
      return;
    }
    
    try {
      setLoading(true);
      setError(null);
      console.log('Fetching ride history for user ID:', user.id);
      const response = await rideHistoryService.getUserRideHistory(user.id, filters);
      console.log('Ride history response:', response.data);
      setRideHistory(response.data || []);
    } catch (err) {
      console.error('Error fetching ride history:', err);
      if (err.response?.status === 403) {
        setError('Access denied. Please log in again.');
      } else if (err.response?.status === 404) {
        setError('No ride history found.');
        setRideHistory([]);
      } else if (err.code === 'ECONNREFUSED' || err.message?.includes('Failed to fetch')) {
        setError('Failed to connect to server. Please ensure the backend is running.');
      } else {
        setError('Failed to load ride history. Please try again.');
      }
      setRideHistory([]);
    } finally {
      setLoading(false);
    }
  };

  const fetchStatistics = async () => {
    if (!user?.id) return;
    
    try {
      const response = await rideHistoryService.getRideStatistics(user.id);
      setStatistics(response.data);
    } catch (err) {
      console.error('Error fetching statistics:', err);
      // Don't show error for statistics, it's not critical
    }
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const applyFilters = () => {
    fetchRideHistory();
  };

  const clearFilters = () => {
    setFilters({
      startDate: '',
      endDate: '',
      status: '',
      bikeType: '',
    });
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
      return format(new Date(dateString), 'MMM dd, yyyy HH:mm');
    } catch {
      return dateString;
    }
  };

  const formatDuration = (minutes) => {
    if (!minutes) return 'N/A';
    const hrs = Math.floor(minutes / 60);
    const mins = Math.floor(minutes % 60);
    if (hrs > 0) {
      return `${hrs}h ${mins}m`;
    }
    return `${mins}m`;
  };

  const pageVariants = {
    initial: { opacity: 0, x: -50 },
    animate: { opacity: 1, x: 0 },
    exit: { opacity: 0, x: 50 }
  };

  return (
    <motion.div
      variants={pageVariants}
      initial="initial"
      animate="animate"
      exit="exit"
      transition={{ duration: 0.4 }}
      className="min-h-[calc(100vh-5rem)] px-4 sm:px-6 lg:px-8 py-8"
    >
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-8"
        >
          <h1 className="text-4xl md:text-5xl font-black bg-gradient-to-r from-primary-900 via-primary-700 to-primary-900 dark:from-gray-50 dark:via-gray-200 dark:to-gray-50 bg-clip-text text-transparent mb-2">
            Ride History
          </h1>
          <p className="text-gray-600 dark:text-gray-400">
            View your past rides and journey statistics
          </p>
        </motion.div>

        {/* Statistics Cards */}
        {statistics && (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
            className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8"
          >
            <div className="card p-6">
              <div className="text-sm text-gray-600 dark:text-gray-400 mb-1">Total Rides</div>
              <div className="text-3xl font-bold text-primary-900 dark:text-gray-100">
                {statistics.totalRides || 0}
              </div>
            </div>
            <div className="card p-6">
              <div className="text-sm text-gray-600 dark:text-gray-400 mb-1">Total Distance</div>
              <div className="text-3xl font-bold text-primary-900 dark:text-gray-100">
                {statistics.totalDistance ? `${statistics.totalDistance.toFixed(1)} km` : '0 km'}
              </div>
            </div>
            <div className="card p-6">
              <div className="text-sm text-gray-600 dark:text-gray-400 mb-1">Total Cost</div>
              <div className="text-3xl font-bold text-primary-900 dark:text-gray-100">
                ${statistics.totalCost ? statistics.totalCost.toFixed(2) : '0.00'}
              </div>
            </div>
            <div className="card p-6">
              <div className="text-sm text-gray-600 dark:text-gray-400 mb-1">Avg Duration</div>
              <div className="text-3xl font-bold text-primary-900 dark:text-gray-100">
                {statistics.averageDuration ? formatDuration(statistics.averageDuration) : '0m'}
              </div>
            </div>
          </motion.div>
        )}

        {/* Filters */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
          className="card p-6 mb-8"
        >
          <h2 className="text-xl font-bold mb-4 text-primary-900 dark:text-gray-100">Filters</h2>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Start Date
              </label>
              <input
                type="date"
                name="startDate"
                value={filters.startDate}
                onChange={handleFilterChange}
                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                End Date
              </label>
              <input
                type="date"
                name="endDate"
                value={filters.endDate}
                onChange={handleFilterChange}
                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Status
              </label>
              <select
                name="status"
                value={filters.status}
                onChange={handleFilterChange}
                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              >
                <option value="">All Statuses</option>
                <option value="COMPLETED">Completed</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Bike Type
              </label>
              <select
                name="bikeType"
                value={filters.bikeType}
                onChange={handleFilterChange}
                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              >
                <option value="">All Types</option>
                <option value="STANDARD">Standard</option>
                <option value="EBIKE">E-Bike</option>
              </select>
            </div>
          </div>
          <div className="flex gap-4 mt-4">
            <button
              onClick={applyFilters}
              className="btn-primary px-6 py-2"
            >
              Apply Filters
            </button>
            <button
              onClick={clearFilters}
              className="btn-secondary px-6 py-2"
            >
              Clear
            </button>
          </div>
        </motion.div>

        {/* Ride History List */}
        {loading ? (
          <div className="text-center py-12">
            <div className="text-xl text-gray-600 dark:text-gray-400">Loading ride history...</div>
          </div>
        ) : error ? (
          <div className="card p-6 text-center">
            <div className="text-red-600 dark:text-red-400">{error}</div>
            <button
              onClick={fetchRideHistory}
              className="btn-primary mt-4"
            >
              Retry
            </button>
          </div>
        ) : rideHistory.length === 0 ? (
          <div className="card p-12 text-center">
            <div className="text-gray-600 dark:text-gray-400 text-lg mb-4">
              No ride history found
            </div>
            <p className="text-gray-500 dark:text-gray-500">
              Start your first ride to see your history here!
            </p>
          </div>
        ) : (
          <div className="space-y-4">
            {rideHistory.map((ride, index) => (
              <motion.div
                key={ride.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.1 }}
                className="card p-6 hover:shadow-lg transition-shadow"
              >
                <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-3">
                      <div className={`px-3 py-1 rounded-full text-xs font-semibold ${
                        ride.status === 'COMPLETED' 
                          ? 'bg-green-100 dark:bg-green-900 text-green-800 dark:text-green-200'
                          : ride.status === 'IN_PROGRESS'
                          ? 'bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200'
                          : 'bg-gray-100 dark:bg-gray-700 text-gray-800 dark:text-gray-200'
                      }`}>
                        {ride.status}
                      </div>
                      {ride.bikeType && (
                        <div className="px-3 py-1 rounded-full bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-300 text-xs font-semibold">
                          {ride.bikeType}
                        </div>
                      )}
                    </div>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                      <div>
                        <div className="text-gray-600 dark:text-gray-400 mb-1">Start Time</div>
                        <div className="font-semibold text-gray-900 dark:text-gray-100">
                          {formatDate(ride.startTime)}
                        </div>
                      </div>
                      {ride.endTime && (
                        <div>
                          <div className="text-gray-600 dark:text-gray-400 mb-1">End Time</div>
                          <div className="font-semibold text-gray-900 dark:text-gray-100">
                            {formatDate(ride.endTime)}
                          </div>
                        </div>
                      )}
                      {ride.durationMinutes && (
                        <div>
                          <div className="text-gray-600 dark:text-gray-400 mb-1">Duration</div>
                          <div className="font-semibold text-gray-900 dark:text-gray-100">
                            {formatDuration(ride.durationMinutes)}
                          </div>
                        </div>
                      )}
                      {ride.distanceKm && (
                        <div>
                          <div className="text-gray-600 dark:text-gray-400 mb-1">Distance</div>
                          <div className="font-semibold text-gray-900 dark:text-gray-100">
                            {ride.distanceKm.toFixed(2)} km
                          </div>
                        </div>
                      )}
                    </div>
                    <div className="mt-4 grid grid-cols-2 gap-4 text-sm">
                      {ride.startStationId && (
                        <div>
                          <div className="text-gray-600 dark:text-gray-400 mb-1">Start Station</div>
                          <div className="font-semibold text-gray-900 dark:text-gray-100">
                            Station #{ride.startStationId}
                          </div>
                        </div>
                      )}
                      {ride.endStationId && (
                        <div>
                          <div className="text-gray-600 dark:text-gray-400 mb-1">End Station</div>
                          <div className="font-semibold text-gray-900 dark:text-gray-100">
                            Station #{ride.endStationId}
                          </div>
                        </div>
                      )}
                    </div>
                  </div>
                  {ride.cost && (
                    <div className="text-right">
                      <div className="text-sm text-gray-600 dark:text-gray-400 mb-1">Cost</div>
                      <div className="text-2xl font-bold text-primary-900 dark:text-gray-100">
                        ${ride.cost.toFixed(2)}
                      </div>
                    </div>
                  )}
                </div>
              </motion.div>
            ))}
          </div>
        )}
      </div>
    </motion.div>
  );
};

export default RideHistory;

