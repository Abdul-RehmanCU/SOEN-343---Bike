import { useAuth } from '../hooks/useAuth';
import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';

const Dashboard = () => {
  const { user } = useAuth();

  const pageVariants = {
    initial: { opacity: 0, x: -50 },
    animate: { opacity: 1, x: 0 },
    exit: { opacity: 0, x: 50 }
  };

  const riderCards = [
    { 
      title: 'Bike Management', 
      desc: 'Reserve, checkout, and return bikes at stations', 
      primary: true,
      link: '/bikes',
      icon: <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" /></svg>
    },
    { 
      title: 'Ride History', 
      desc: 'View your journey statistics and past rides', 
      primary: false,
      icon: <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" /></svg>
    },
    { 
      title: 'Wallet', 
      desc: 'Manage your payment methods and balance', 
      primary: false,
      icon: <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" /></svg>
    }
  ];

  const operatorCards = [
    { 
      title: 'Bike Management', 
      desc: 'Reserve, checkout, return, and rebalance bikes', 
      primary: true,
      link: '/bikes',
      icon: <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" /></svg>
    },
    { 
      title: 'Stations', 
      desc: 'Monitor and manage docking station network', 
      primary: false,
      icon: <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" /></svg>
    },
    { 
      title: 'Fleet Status', 
      desc: 'Track and maintain bike inventory', 
      primary: false,
      icon: <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 17V7m0 10a2 2 0 01-2 2H5a2 2 0 01-2-2V7a2 2 0 012-2h2a2 2 0 012 2m0 10a2 2 0 002 2h2a2 2 0 002-2M9 7a2 2 0 012-2h2a2 2 0 012 2m0 10V7m0 10a2 2 0 002 2h2a2 2 0 002-2V7a2 2 0 00-2-2h-2a2 2 0 00-2 2" /></svg>
    }
  ];

  const cards = user?.role === 'RIDER' ? riderCards : operatorCards;

  return (
    <motion.div
      variants={pageVariants}
      initial="initial"
      animate="animate"
      exit="exit"
      transition={{ duration: 0.4, ease: 'easeInOut' }}
      className="min-h-[calc(100vh-5rem)] px-4 sm:px-6 lg:px-8 py-16 relative overflow-hidden"
    >
      <div className="absolute inset-0 bg-[linear-gradient(to_right,#80808012_1px,transparent_1px),linear-gradient(to_bottom,#80808012_1px,transparent_1px)] bg-[size:48px_48px] dark:bg-[linear-gradient(to_right,#ffffff20_1px,transparent_1px),linear-gradient(to_bottom,#ffffff20_1px,transparent_1px)]"></div>
      
      <motion.div 
        animate={{ 
          x: [0, 60, 0],
          y: [0, -45, 0],
          scale: [1, 1.18, 1],
          opacity: [0.35, 0.5, 0.35]
        }}
        transition={{ duration: 12, repeat: Infinity, ease: "easeInOut" }}
        className="absolute top-0 right-1/4 w-[550px] h-[550px] bg-gradient-to-br from-gray-500/38 via-gray-400/28 to-transparent dark:from-gray-400/48 dark:via-gray-300/32 rounded-full blur-3xl"
      />
      
      <motion.div 
        animate={{ 
          x: [0, -60, 0],
          y: [0, 45, 0],
          scale: [1.18, 1, 1.18],
          opacity: [0.3, 0.45, 0.3]
        }}
        transition={{ duration: 15, repeat: Infinity, ease: "easeInOut" }}
        className="absolute bottom-0 left-1/4 w-[500px] h-[500px] bg-gradient-to-br from-gray-400/33 via-gray-500/25 to-transparent dark:from-gray-500/43 dark:via-gray-400/30 rounded-full blur-3xl"
      />
      
      <div className="max-w-7xl mx-auto relative z-10">
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="mb-14"
        >
          <div className="flex flex-col md:flex-row md:items-end md:justify-between mb-6">
            <div>
              <h1 className="text-5xl md:text-6xl font-black bg-gradient-to-r from-primary-900 via-primary-700 to-primary-900 dark:from-gray-50 dark:via-gray-200 dark:to-gray-50 bg-clip-text text-transparent tracking-tighter leading-tight mb-3">
                {user?.fullName}
              </h1>
              <p className="text-lg md:text-xl text-gray-600 dark:text-gray-400 font-light">
                {user?.role === 'RIDER' 
                  ? 'Your sustainable journey continues' 
                  : 'System management dashboard'}
              </p>
            </div>
            <div className="mt-4 md:mt-0 flex items-center gap-3">
              <span className="text-sm text-gray-500 dark:text-gray-400 font-light">
                {new Date().toLocaleDateString('en-US', { weekday: 'long', month: 'short', day: 'numeric' })}
              </span>
              <div className="w-px h-4 bg-gray-300 dark:bg-gray-700"></div>
              <span className="text-sm text-gray-500 dark:text-gray-400 font-light">
                {new Date().toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' })}
              </span>
            </div>
          </div>
        </motion.div>

        <div className="grid md:grid-cols-3 gap-7 mb-10">
          {cards.map((card, index) => (
            <motion.div 
              key={index}
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.2 + index * 0.15 }}
              whileHover={{ scale: 1.04, y: -8 }}
            >
              <div className="relative card h-full flex flex-col group cursor-pointer overflow-hidden">
                <motion.div
                  className="absolute inset-0 bg-gradient-to-r from-transparent via-white/10 dark:via-white/5 to-transparent"
                  animate={{
                    x: ['-100%', '100%'],
                  }}
                  transition={{
                    duration: 3,
                    repeat: Infinity,
                    repeatDelay: 2,
                    ease: "easeInOut"
                  }}
                />
                
                <div className="relative z-10">
                  <div className="flex items-start justify-between mb-6">
                    <motion.div 
                      whileHover={{ rotate: [0, -10, 10, -10, 0], scale: 1.1 }}
                      transition={{ duration: 0.5 }}
                      className="w-16 h-16 bg-gradient-to-br from-primary-900 to-primary-700 dark:from-gray-50 dark:to-white rounded-2xl shadow-2xl flex items-center justify-center text-white dark:text-primary-900 group-hover:shadow-3xl transition-shadow"
                    >
                      {card.icon}
                    </motion.div>
                    {card.primary && (
                      <motion.div
                        initial={{ scale: 0 }}
                        animate={{ scale: 1 }}
                        transition={{ type: 'spring', delay: 0.3 }}
                        className="px-2.5 py-1 bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-300 rounded-md text-xs font-semibold"
                      >
                        Recommended
                      </motion.div>
                    )}
                  </div>
                  
                  <h3 className="text-2xl font-bold mb-3 text-primary-900 dark:text-gray-50 group-hover:text-primary-800 dark:group-hover:text-white transition-colors">{card.title}</h3>
                  <p className="text-gray-600 dark:text-gray-400 text-sm mb-7 flex-grow leading-relaxed font-light">{card.desc}</p>
                  
                  {card.link ? (
                    <Link to={card.link} className={`${card.primary ? 'btn-primary' : 'btn-secondary'} w-full flex items-center justify-center gap-2 group-hover:gap-4 transition-all`}>
                      <span>{card.primary ? 'Open Now' : 'View Details'}</span>
                      <svg className="w-4 h-4 group-hover:translate-x-2 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                      </svg>
                    </Link>
                  ) : (
                    <button className={`${card.primary ? 'btn-primary' : 'btn-secondary'} w-full flex items-center justify-center gap-2 group-hover:gap-4 transition-all`}>
                      <span>{card.primary ? 'Open Now' : 'View Details'}</span>
                      <svg className="w-4 h-4 group-hover:translate-x-2 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                      </svg>
                    </button>
                  )}
                </div>
              </div>
            </motion.div>
          ))}
        </div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 0.7 }}
          className="grid grid-cols-3 gap-4"
        >
          {[
            { 
              label: 'Rides', 
              value: '127',
              icon: <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" /></svg>
            },
            { 
              label: 'CO₂ Saved', 
              value: '45kg',
              icon: <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
            },
            { 
              label: 'Distance', 
              value: '234km',
              icon: <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" /></svg>
            }
          ].map((stat, i) => (
            <motion.div 
              key={i}
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.4, delay: 0.8 + i * 0.1 }}
              whileHover={{ y: -3, scale: 1.03 }}
              className="bg-white/60 dark:bg-gray-800/60 backdrop-blur-sm rounded-lg p-4 border border-gray-200 dark:border-gray-700 hover:border-gray-300 dark:hover:border-gray-600 transition-all group relative overflow-hidden"
            >
              <motion.div
                className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 dark:via-white/5 to-transparent"
                animate={{ x: ['-100%', '100%'] }}
                transition={{ duration: 2, repeat: Infinity, repeatDelay: 3 }}
              />
              <div className="relative flex items-center gap-3">
                <div className="w-8 h-8 bg-gray-100 dark:bg-gray-700 rounded-lg flex items-center justify-center text-gray-600 dark:text-gray-400 group-hover:bg-primary-900 group-hover:text-white dark:group-hover:bg-white dark:group-hover:text-primary-900 transition-all">
                  {stat.icon}
                </div>
                <div className="flex-1">
                  <div className="text-2xl font-bold text-primary-900 dark:text-gray-100 tracking-tight">{stat.value}</div>
                  <div className="text-xs text-gray-600 dark:text-gray-400">{stat.label}</div>
                </div>
              </div>
            </motion.div>
          ))}
        </motion.div>
      </div>
    </motion.div>
  );
};

export default Dashboard;
