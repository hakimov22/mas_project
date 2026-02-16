import React from 'react';
import { Reservation } from '../data/api';

interface TripReservationsTableProps {
  reservations: Reservation[];
}

export function TripReservationsTable({ reservations }: TripReservationsTableProps) {
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING': return 'bg-[#FFC107] text-gray-900';
      case 'CONFIRMED': return 'bg-[#4CAF50] text-white';
      case 'CANCELLED': return 'bg-[#F44336] text-white';
      default: return 'bg-gray-500 text-white';
    }
  };

  return (
    <div className="mt-12 bg-white rounded-xl shadow-md p-8">
      <h2 className="text-2xl font-bold text-gray-900 mb-6">
        Reservations for this Trip
      </h2>

      {reservations.length > 0 ? (
        <table className="w-full">
          <thead>
            <tr className="border-b">
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Reservation #</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Customer</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">People</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Status</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Date</th>
            </tr>
          </thead>
          <tbody>
            {reservations.map(r => (
              <tr key={r.id} className="border-b hover:bg-gray-50">
                <td className="py-4 px-4 font-medium">{r.id}</td>
                <td className="py-4 px-4">{r.customerName}</td>
                <td className="py-4 px-4">{r.numberOfPeople}</td>
                <td className="py-4 px-4">
                  <span className={`${getStatusColor(r.status)} px-3 py-1 rounded-full text-xs font-semibold`}>
                    {r.status}
                  </span>
                </td>
                <td className="py-4 px-4">{r.bookingDate}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p className="text-center py-8 text-gray-500">No reservations yet.</p>
      )}
    </div>
  );
}
