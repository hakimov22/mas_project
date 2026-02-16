import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Mail, Calendar, Users, MapPin } from 'lucide-react';
import { fetchCustomer, fetchCustomerReservations, cancelReservation, Customer, Reservation } from '../data/api';
import { useCustomer } from '../context/CustomerContext';

export function MyBookings() {
  const { selectedCustomerId } = useCustomer();
  const [customer, setCustomer] = useState<Customer | null>(null);
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    Promise.all([
      fetchCustomer(selectedCustomerId),
      fetchCustomerReservations(selectedCustomerId)
    ]).then(([customerData, reservationsData]) => {
      setCustomer(customerData);
      setReservations(reservationsData);
    }).finally(() => setLoading(false));
  }, [selectedCustomerId]);

  const handleCancel = async (reservationId: string) => {
    const result = await cancelReservation(reservationId);
    if (result.success) {
      const updated = await fetchCustomerReservations(selectedCustomerId);
      setReservations(updated);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING': return 'bg-[#FFC107] text-gray-900';
      case 'CONFIRMED': return 'bg-[#4CAF50] text-white';
      case 'CANCELLED': return 'bg-[#F44336] text-white';
      default: return 'bg-gray-500 text-white';
    }
  };

  if (loading) {
    return <div className="flex items-center justify-center min-h-screen"><div>Loading...</div></div>;
  }

  return (
    <div className="bg-gray-50 min-h-screen py-8">
      <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">My Reservations</h1>

        {/* Customer Info Card */}
        {customer && (
          <div className="bg-white rounded-xl shadow-md p-6 mb-8">
            <div className="flex items-center gap-6">
              <div className="w-16 h-16 bg-gradient-to-br from-[#1565C0] to-[#0D47A1] rounded-full flex items-center justify-center text-white text-2xl font-bold">
                {customer.name.charAt(0)}
              </div>
              <div className="flex-1">
                <h2 className="text-xl font-bold text-gray-900 mb-2">{customer.name}</h2>
                <div className="flex flex-wrap gap-4 text-sm text-gray-600">
                  <div className="flex items-center gap-2">
                    <Mail className="w-4 h-4" />
                    <span>{customer.email}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <Calendar className="w-4 h-4" />
                    <span>Member since {customer.registrationDate}</span>
                  </div>
                </div>
              </div>
              <div className="text-center">
                <div className="text-3xl font-bold text-[#1565C0]">{reservations.length}</div>
                <div className="text-sm text-gray-600">Total Reservations</div>
              </div>
            </div>
          </div>
        )}

        {/* Reservations List */}
        <div className="space-y-4">
          {reservations.length > 0 ? (
            reservations.map(reservation => (
              <div key={reservation.id} className="bg-white rounded-xl shadow-md p-6">
                  <div className="flex items-center justify-between">
                    <div className="flex-1">
                      <div className="flex items-center gap-3 mb-2">
                        <h3 className="text-lg font-bold text-gray-900">{reservation.tripName}</h3>
                        <span className={`${getStatusColor(reservation.status)} px-3 py-1 rounded-full text-xs font-semibold`}>
                          {reservation.status}
                        </span>
                      </div>
                      <div className="flex flex-wrap gap-4 text-sm text-gray-600">
                        <div className="flex items-center gap-1">
                          <MapPin className="w-4 h-4" />
                          <span>{reservation.destination}, {reservation.country}</span>
                        </div>
                        <div className="flex items-center gap-1">
                          <Users className="w-4 h-4" />
                          <span>{reservation.numberOfPeople} {reservation.numberOfPeople === 1 ? 'person' : 'people'}</span>
                        </div>
                        <div className="flex items-center gap-1">
                          <Calendar className="w-4 h-4" />
                          <span>Booked {reservation.bookingDate}</span>
                        </div>
                      </div>
                    </div>
                    <div className="text-right ml-4">
                      <div className="text-2xl font-bold text-[#1565C0] mb-1">${reservation.totalPrice}</div>
                      <div className="text-sm text-gray-600">{reservation.id}</div>
                  </div>
                </div>

                {/* Buttons - Always visible */}
                <div className="flex flex-wrap gap-3 mt-4 pt-4 border-t border-gray-100">
                      <Link
                        to={`/trip/${reservation.tripId}`}
                    className="bg-[#1565C0] text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-[#0D47A1] transition-colors"
                      >
                    Trip Details
                      </Link>
                      
                      {reservation.status === 'PENDING' && (
                        <button 
                          onClick={() => handleCancel(reservation.id)}
                          className="inline-flex items-center gap-2 bg-[#F44336] text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-[#D32F2F] transition-colors"
                        >
                          Cancel Reservation
                        </button>
                      )}
                    </div>
              </div>
            ))
          ) : (
            <div className="bg-white rounded-xl shadow-md p-12 text-center">
              <h3 className="text-xl font-bold text-gray-900 mb-2">No Reservations Yet</h3>
              <p className="text-gray-600 mb-6">Start exploring our amazing trips!</p>
              <Link
                to="/"
                className="inline-block bg-[#FF8F00] text-white px-6 py-3 rounded-lg font-medium hover:bg-[#E68000] transition-colors"
              >
                Browse Trips
              </Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
