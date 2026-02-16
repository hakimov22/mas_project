import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { fetchAllReservations, Reservation } from '../data/api';
import { useCustomer } from '../context/CustomerContext';

export function ReservationsTable() {
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [loading, setLoading] = useState(true);
  const location = useLocation();
  const { selectedCustomerId } = useCustomer();

  useEffect(() => {
    setLoading(true);
    fetchAllReservations()
      .then(data => setReservations(data))
      .finally(() => setLoading(false));
  }, [location.key, selectedCustomerId]);

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING': return 'bg-[#FFC107] text-gray-900';
      case 'CONFIRMED': return 'bg-[#4CAF50] text-white';
      case 'CANCELLED': return 'bg-[#F44336] text-white';
      default: return 'bg-gray-500 text-white';
    }
  };

  return (
    <div className="bg-gray-50 py-6">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-2">All Reservations</h1>
        <p className="text-sm text-gray-600 mb-4">Simple table showing customer-trip links (many-to-many).</p>

        {loading ? (
          <div className="bg-white rounded-xl shadow-md p-6">Loading...</div>
        ) : (
          <div className="bg-white rounded-xl shadow-md overflow-x-auto">
            <table className="w-full table-auto text-left">
              <thead className="bg-gray-100 text-gray-700 text-sm">
                <tr>
                  <th className="px-4 py-2 font-semibold">Reservation #</th>
                  <th className="px-4 py-2 font-semibold">Customer</th>
                  <th className="px-4 py-2 font-semibold">Trip</th>
                  <th className="px-4 py-2 font-semibold">Booked</th>
                  <th className="px-4 py-2 font-semibold">People</th>
                  <th className="px-4 py-2 font-semibold">Status</th>
                </tr>
              </thead>
              <tbody className="text-sm text-gray-800">
                {reservations.length > 0 ? (
                  reservations.map(r => (
                    <tr key={r.id} className="border-t">
                      <td className="px-4 py-2 font-medium text-[#1565C0]">{r.id}</td>
                      <td className="px-4 py-2">{r.customerName ?? 'Unknown'}</td>
                      <td className="px-4 py-2">{r.tripName ?? 'Unknown'}</td>
                      <td className="px-4 py-2">{r.bookingDate}</td>
                      <td className="px-4 py-2">{r.numberOfPeople}</td>
                      <td className="px-4 py-2">
                        <span className={`${getStatusColor(r.status)} px-2 py-1 rounded-full text-xs font-semibold`}>
                          {r.status}
                        </span>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td className="px-4 py-6 text-center text-gray-500" colSpan={6}>
                      No reservations found.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
