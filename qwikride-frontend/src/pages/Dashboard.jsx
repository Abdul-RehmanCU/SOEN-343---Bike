import { useAuth } from '../hooks/useAuth';

const Dashboard = () => {
  const { user } = useAuth();

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="mb-10">
          <h1 className="text-4xl font-bold text-primary-900">
            Welcome, {user?.fullName}!
          </h1>
          <p className="text-gray-600 mt-2 text-lg">
            {user?.role === 'RIDER' 
              ? 'Find a bike and start your eco-friendly journey' 
              : 'Manage stations and bikes across the city'}
          </p>
        </div>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {user?.role === 'RIDER' ? (
            <>
              <div className="card hover:scale-105 transition-transform">
                <h3 className="text-2xl font-bold mb-3 text-primary-900">🗺️ Find a Bike</h3>
                <p className="text-gray-600 mb-6">View nearby stations and available bikes</p>
                <button className="btn-primary w-full">View Map</button>
              </div>

              <div className="card hover:scale-105 transition-transform">
                <h3 className="text-2xl font-bold mb-3 text-primary-900">🚴 My Rides</h3>
                <p className="text-gray-600 mb-6">View your ride history and statistics</p>
                <button className="btn-secondary w-full">View History</button>
              </div>

              <div className="card hover:scale-105 transition-transform">
                <h3 className="text-2xl font-bold mb-3 text-primary-900">💳 Wallet</h3>
                <p className="text-gray-600 mb-6">Manage payment methods and balance</p>
                <button className="btn-secondary w-full">Manage Wallet</button>
              </div>
            </>
          ) : (
            <>
              <div className="card hover:scale-105 transition-transform">
                <h3 className="text-2xl font-bold mb-3 text-primary-900">🏢 Stations</h3>
                <p className="text-gray-600 mb-6">Manage docking stations</p>
                <button className="btn-primary w-full">View Stations</button>
              </div>

              <div className="card hover:scale-105 transition-transform">
                <h3 className="text-2xl font-bold mb-3 text-primary-900">🚲 Bikes</h3>
                <p className="text-gray-600 mb-6">Monitor and maintain bikes</p>
                <button className="btn-secondary w-full">View Bikes</button>
              </div>

              <div className="card hover:scale-105 transition-transform">
                <h3 className="text-2xl font-bold mb-3 text-primary-900">⚖️ Rebalance</h3>
                <p className="text-gray-600 mb-6">Move bikes between stations</p>
                <button className="btn-secondary w-full">Rebalance</button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;