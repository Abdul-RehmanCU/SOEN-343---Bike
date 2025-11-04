import { motion } from "framer-motion";
import { Link } from "react-router-dom";

export default function Guest() {
  const fadeInUp = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 },
  };

  return (
    <div className="bg-white dark:bg-gray-900 text-gray-800 dark:text-gray-100">
      {/* Hero Section */}
      <section className="min-h-screen flex flex-col items-center justify-center text-center px-6">
        <motion.h1
          variants={fadeInUp}
          initial="hidden"
          animate="visible"
          transition={{ duration: 0.5 }}
          className="text-5xl font-extrabold mb-4"
        >
          Welcome to QwikRide 🚴‍♂️
        </motion.h1>
        <motion.p
          variants={fadeInUp}
          initial="hidden"
          animate="visible"
          transition={{ delay: 0.2, duration: 0.5 }}
          className="text-lg text-gray-600 dark:text-gray-300 max-w-2xl"
        >
          Discover a faster, greener, and smarter way to move around the city.
        </motion.p>
        <div className="mt-8">
  <a
    href="#pricing"
    className="border border-blue-600 text-blue-600 hover:bg-blue-50 dark:hover:bg-gray-800 px-6 py-3 rounded-xl transition"
  >
    View Pricing
  </a>
</div>
      </section>

      {/* About Section */}
      <motion.section
        id="about"
        variants={fadeInUp}
        initial="hidden"
        whileInView="visible"
        viewport={{ once: true }}
        transition={{ duration: 0.4 }}
        className="max-w-5xl mx-auto py-20 px-6"
      >
        <h2 className="text-3xl font-bold text-center mb-8">About QwikRide</h2>
        <p className="text-gray-600 dark:text-gray-300 text-center max-w-3xl mx-auto">
          QwikRide is a community-driven bikeshare platform that helps you find, reserve, and ride bikes across the city seamlessly. Whether you’re a daily commuter or a casual explorer, our stations are always nearby.
        </p>
      </motion.section>

      {/* How It Works Section */}
      <motion.section
        id="how-it-works"
        variants={fadeInUp}
        initial="hidden"
        whileInView="visible"
        viewport={{ once: true }}
        transition={{ duration: 0.4 }}
        className="bg-gray-50 dark:bg-gray-800 py-20"
      >
        <div className="max-w-6xl mx-auto px-6">
          <h2 className="text-3xl font-bold text-center mb-10">How It Works</h2>
          <div className="grid md:grid-cols-3 gap-8">
            {[
              { step: "1", title: "Find a Station", desc: "Locate nearby bikes easily using our interactive map." },
              { step: "2", title: "Reserve & Unlock", desc: "Reserve your bike through the app and unlock it instantly." },
              { step: "3", title: "Ride & Return", desc: "Enjoy your ride and return it to any QwikRide station." },
            ].map((item) => (
              <div
                key={item.step}
                className="bg-white dark:bg-gray-900 p-6 rounded-2xl shadow-sm hover:shadow-md transition"
              >
                <p className="text-blue-600 font-bold text-2xl mb-2">{item.step}</p>
                <h3 className="text-xl font-semibold mb-2">{item.title}</h3>
                <p className="text-gray-600 dark:text-gray-400">{item.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </motion.section>

      {/* Pricing Section */}
      <motion.section
        id="pricing"
        variants={fadeInUp}
        initial="hidden"
        whileInView="visible"
        viewport={{ once: true }}
        transition={{ duration: 0.4 }}
        className="max-w-6xl mx-auto py-20 px-6"
      >
        <h2 className="text-3xl font-bold text-center mb-10">Membership Plans</h2>
        <div className="grid md:grid-cols-3 gap-8">
          {[
            { title: "Casual", price: "$0", desc: "Pay per ride. Ideal for occasional users." },
            { title: "Monthly", price: "$15", desc: "Unlimited rides for 30 days." },
            { title: "Annual", price: "$99", desc: "Best value for frequent riders." },
          ].map((plan) => (
            <div
              key={plan.title}
              className="border border-gray-200 dark:border-gray-700 rounded-2xl p-6 shadow-sm hover:shadow-md transition"
            >
              <h3 className="text-2xl font-semibold mb-4 text-center">{plan.title}</h3>
              <p className="text-center text-3xl font-bold mb-2">{plan.price}</p>
              <p className="text-center text-gray-600 dark:text-gray-400 mb-6">{plan.desc}</p>
              <div className="text-center">
                <Link
                  to="/register"
                  className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-xl transition"
                >
                  Get Started
                </Link>
              </div>
            </div>
          ))}
        </div>
      </motion.section>

      {/* Station Map Section */}
      <motion.section
        id="map"
        variants={fadeInUp}
        initial="hidden"
        whileInView="visible"
        viewport={{ once: true }}
        transition={{ duration: 0.4 }}
        className="bg-gray-50 dark:bg-gray-800 py-20"
      >
        <div className="max-w-6xl mx-auto px-6">
          <h2 className="text-3xl font-bold text-center mb-10">Station Map</h2>
          <div className="bg-gray-200 dark:bg-gray-900 h-96 rounded-2xl flex items-center justify-center">
            <p className="text-gray-600 dark:text-gray-300">
              [Interactive Map Placeholder]
            </p>
          </div>
        </div>
      </motion.section>

      {/* Footer */}
      <footer className="py-10 text-center text-gray-500 text-sm border-t border-gray-200 dark:border-gray-700">
        © {new Date().getFullYear()} QwikRide. All rights reserved.
      </footer>
    </div>
  );
}
