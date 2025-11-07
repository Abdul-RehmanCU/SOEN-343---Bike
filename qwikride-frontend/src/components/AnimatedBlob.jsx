import { motion } from 'framer-motion';

export const AnimatedBlob = ({ 
  position = { top: '33%', right: '33%' },
  size = '450px',
  duration = 10,
  movement = { x: [0, 30, 0], y: [0, -20, 0] }
}) => (
  <motion.div 
    animate={{ 
      scale: [1, 1.15, 1], 
      opacity: [0.3, 0.5, 0.3],
      x: movement.x,
      y: movement.y
    }}
    transition={{ duration, repeat: Infinity }}
    className="absolute bg-gradient-to-br from-gray-500/35 via-gray-400/25 to-transparent dark:from-gray-400/45 dark:via-gray-300/30 blur-3xl rounded-full"
    style={{ 
      ...position,
      width: size, 
      height: size 
    }}
  />
);