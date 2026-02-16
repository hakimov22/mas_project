import React from 'react';
import { Plane, User } from 'lucide-react';
import { Link, useLocation } from 'react-router-dom';
import { useCustomer } from '../context/CustomerContext';

export function Header() {
  const location = useLocation();
  const isActive = (path: string) => location.pathname === path;
  const { customers, selectedCustomerId, setSelectedCustomerId, loading } = useCustomer();

  return (
    <header className="bg-white shadow-sm sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <Link to="/" className="flex items-center gap-2">
            <Plane className="w-8 h-8 text-[#1565C0]" />
            <span className="text-xl font-semibold text-[#1565C0]">TravelAgency</span>
          </Link>

          <nav className="flex items-center gap-8">
            <Link
              to="/"
              className={`text-base font-medium px-4 py-2 rounded-lg ${isActive('/') ? 'text-[#1565C0] bg-blue-50' : 'text-gray-700 hover:text-[#1565C0] hover:bg-gray-100'}`}
            >
              Trips
            </Link>
            <Link
              to="/my-bookings"
              className={`text-base font-medium px-4 py-2 rounded-lg ${isActive('/my-bookings') ? 'text-[#1565C0] bg-blue-50' : 'text-gray-700 hover:text-[#1565C0] hover:bg-gray-100'}`}
            >
              My Reservations
            </Link>
            <Link
              to="/reservations-table"
              className={`text-base font-medium px-4 py-2 rounded-lg ${isActive('/reservations-table') ? 'text-[#1565C0] bg-blue-50' : 'text-gray-700 hover:text-[#1565C0] hover:bg-gray-100'}`}
            >
              Reservations Table
            </Link>

            {/* Customer Selector */}
            <div className="flex items-center gap-2 ml-4 pl-4 border-l border-gray-200">
              <User className="w-4 h-4 text-gray-500" />
              <select
                value={selectedCustomerId}
                onChange={(e) => setSelectedCustomerId(Number(e.target.value))}
                disabled={loading}
                className="text-sm font-medium text-gray-700 bg-gray-100 border border-gray-300 rounded-md px-3 py-1.5 focus:outline-none focus:ring-2 focus:ring-[#1565C0] focus:border-transparent"
              >
                {loading ? (
                  <option>Loading...</option>
                ) : (
                  customers.map(customer => (
                    <option key={customer.id} value={customer.id}>
                      {customer.name}
                    </option>
                  ))
                )}
              </select>
            </div>
          </nav>
        </div>
      </div>
    </header>
  );
}
