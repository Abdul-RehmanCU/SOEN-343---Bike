import { Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const GuestHomeLink = ({ className = '' }) => {
  const { user } = useAuth();
  const isGuest = !user || !['RIDER', 'OPERATOR'].includes(user.role);

  if (!isGuest) {
    return null;
  }

  return (
    <div className={className}>
      <Link
        to="/"
        className="inline-flex items-center gap-2 rounded-full border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-100 dark:border-gray-600 dark:text-gray-200 dark:hover:bg-gray-800"
      >
        <svg className="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
        </svg>
        Back to Home
      </Link>
    </div>
  );
};

export default GuestHomeLink;
