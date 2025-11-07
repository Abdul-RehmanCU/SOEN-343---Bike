import { Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { motion } from 'framer-motion';
import { PAGE_VARIANTS } from '../constants/animations';
import { GridBackground, AnimatedBlob } from '../components';

const Home = () => {
  const { user } = useAuth();

  return (
    <motion.div
      variants={PAGE_VARIANTS}
      initial="initial"
      animate="animate"
      exit="exit"
      transition={{ duration: 0.4, ease: 'easeInOut' }}
      className="min-h-[calc(100vh-5rem)] flex items-center justify-center px-4 sm:px-6 lg:px-8 relative overflow-hidden"
    >
      <GridBackground gridSize="64px" />
      
      <AnimatedBlob 
        position={{ top: '25%', right: '25%' }}
        size="600px"
        duration={14}
        movement={{ x: [0, 80, 0], y: [0, -60, 0] }}
      />
      
      <AnimatedBlob 
        position={{ bottom: '25%', left: '25%' }}
        size="550px"
        duration={18}
        movement={{ x: [0, -80, 0], y: [0, 60, 0] }}
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
            ease: "easeInOut",
            delay: i * 0.4
          }}
          className="absolute bg-primary-900/20 dark:bg-gray-400/15 rounded-sm"
          style={{
            left: `${8 + i * 8}%`,
            top: `${12 + (i % 4) * 20}%`,
            width: i % 2 === 0 ? '4px' : '6px',
            height: i % 2 === 0 ? '4px' : '6px',
          }}
        />
      ))}

      <div className="max-w-5xl mx-auto w-full text-center relative z-10">
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, delay: 0.2 }}
        >
          <motion.h1 
            className="text-6xl md:text-7xl lg:text-8xl font-black mb-8 bg-gradient-to-r from-primary-900 via-primary-700 to-primary-900 dark:from-gray-50 dark:via-gray-100 dark:to-gray-50 bg-clip-text text-transparent tracking-tighter leading-none"
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
            className="h-1 bg-gradient-to-r from-transparent via-primary-500 to-transparent dark:via-gray-400 mx-auto mb-8"
          />
          
          <motion.p 
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.8, delay: 0.7 }}
            className="text-xl md:text-2xl mb-14 text-gray-600 dark:text-gray-300 font-light max-w-3xl mx-auto leading-relaxed"
          >
            Experience the future of urban transportation with eco-friendly bike sharing
          </motion.p>
          
          {!user ? (
            <motion.div 
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.9 }}
              className="flex justify-center gap-5"
            >
              <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
                <Link to="/register" className="btn-primary">
                  Get Started
                </Link>
              </motion.div>
              <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
                <Link to="/login" className="btn-secondary">
                  Sign In
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
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                </svg>
              </Link>
            </motion.div>
          )}
        </motion.div>
      </div>
    </motion.div>
  );
};

export default Home;