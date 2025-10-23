import { Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const Home = () => {
  const { user } = useAuth();

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <div className="bg-gradient-to-br from-primary-900 via-primary-800 to-primary-700 text-white py-24">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h1 className="text-6xl font-bold mb-6">Welcome to QwikRide</h1>
            <p className="text-xl mb-10 text-primary-100 max-w-2xl mx-auto">
              Affordable, eco-friendly bike sharing for your city
            </p>
            {!user && (
              <div className="flex justify-center space-x-4">
                <Link to="/register" className="bg-white text-primary-900 px-8 py-4 rounded-lg font-semibold text-lg hover:bg-gray-100 transition-colors shadow-lg">
                  Get Started
                </Link>
                <Link to="/login" className="bg-primary-800 text-white px-8 py-4 rounded-lg font-semibold text-lg hover:bg-primary-900 transition-colors border-2 border-white/20">
                  Login
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <h2 className="text-4xl font-bold text-center mb-16 text-primary-900">Why Choose QwikRide?</h2>
        <div className="grid md:grid-cols-3 gap-8">
          <div className="card text-center">
            <div className="text-6xl mb-4">🌍</div>
            <h3 className="text-2xl font-bold mb-3 text-primary-900">Eco-Friendly</h3>
            <p className="text-gray-600 leading-relaxed">Reduce CO2 emissions by 75% compared to cars</p>
          </div>
          <div className="card text-center">
            <div className="text-6xl mb-4">💰</div>
            <h3 className="text-2xl font-bold mb-3 text-primary-900">Affordable</h3>
            <p className="text-gray-600 leading-relaxed">6x cheaper than traveling by car</p>
          </div>
          <div className="card text-center">
            <div className="text-6xl mb-4">📍</div>
            <h3 className="text-2xl font-bold mb-3 text-primary-900">Convenient</h3>
            <p className="text-gray-600 leading-relaxed">City-wide docking stations for easy access</p>
          </div>
        </div>
      </div>

      {/* Stats Section */}
      <div className="bg-primary-50 py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-3 gap-12 text-center">
            <div>
              <div className="text-5xl font-bold text-primary-900 mb-2">1000+</div>
              <div className="text-gray-600 text-lg">Bikes Available</div>
            </div>
            <div>
              <div className="text-5xl font-bold text-primary-900 mb-2">50+</div>
              <div className="text-gray-600 text-lg">Docking Stations</div>
            </div>
            <div>
              <div className="text-5xl font-bold text-primary-900 mb-2">75%</div>
              <div className="text-gray-600 text-lg">CO2 Reduction</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;