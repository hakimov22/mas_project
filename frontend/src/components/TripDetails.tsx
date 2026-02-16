import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { MapPin } from 'lucide-react';
import { fetchTrip, fetchTripReservations, createReservation, Trip, Reservation } from '../data/api';
import { useCustomer } from '../context/CustomerContext';
import { TripBookingPanel } from './TripBookingPanel';
import { TripReservationsTable } from './TripReservationsTable';

export function TripDetails() {
  const { id } = useParams<{ id: string }>();
  const { selectedCustomerId, selectedCustomer } = useCustomer();
  const [trip, setTrip] = useState<Trip | null>(null);
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [loading, setLoading] = useState(true);
  const [numberOfPeople, setNumberOfPeople] = useState(1);
  const [booking, setBooking] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (id) {
      Promise.all([
        fetchTrip(Number(id)),
        fetchTripReservations(Number(id))
      ]).then(([tripData, reservationsData]) => {
        setTrip(tripData);
        setReservations(reservationsData);
      }).finally(() => setLoading(false));
    }
  }, [id]);

  // Reset booking form when user changes
  useEffect(() => {
    setNumberOfPeople(1);
    setMessage('');
  }, [selectedCustomerId]);

  // Reset selection when the trip changes
  useEffect(() => {
    if (!trip) return;
    setNumberOfPeople(1);
    setMessage('');
  }, [trip?.id]);

  // Reset selection when availability changes (e.g., after booking)
  useEffect(() => {
    if (!trip) return;
    setNumberOfPeople(1);
  }, [trip?.availableSpots]);

  const handleBook = async () => {
    if (!trip) return;
    setBooking(true);
    setMessage('');
    try {
      const result = await createReservation(selectedCustomerId, trip.id, numberOfPeople);
      if (result.success) {
        setMessage(`Booked for ${selectedCustomer?.name}! Reservation: ${result.reservationNumber}`);
        setNumberOfPeople(1);
        const updated = await fetchTripReservations(trip.id);
        setReservations(updated);
        const updatedTrip = await fetchTrip(trip.id);
        if (updatedTrip) setTrip(updatedTrip);
      } else {
        setMessage(result.error || 'Booking failed');
      }
    } catch (e) {
      setMessage('Error creating reservation');
    }
    setBooking(false);
  };

  if (loading) return <div className="flex items-center justify-center min-h-screen">Loading...</div>;
  if (!trip) return (
    <div className="max-w-7xl mx-auto px-4 py-12 text-center">
      <h2 className="text-2xl font-bold mb-4">Trip Not Found</h2>
      <Link to="/" className="text-[#1565C0]">Return to Trips</Link>
    </div>
  );

  const getBadgeColor = (type: Trip['type']) => {
    switch (type) {
      case 'Adventure': return 'bg-[#4CAF50] text-white';
      case 'Cultural': return 'bg-purple-600 text-white';
      case 'Vacation': return 'bg-[#FF8F00] text-white';
    }
  };

  return (
    <div className="bg-gray-50 min-h-screen py-8">
      <div className="max-w-7xl mx-auto px-4">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Left Column - Trip Info */}
          <div className="lg:col-span-2 space-y-6">
            <div className="bg-white rounded-xl shadow-md p-8">
              <div className="flex items-start justify-between mb-4">
                <div>
                  <h1 className="text-3xl font-bold text-gray-900 mb-2">{trip.name}</h1>
                  <div className="flex items-center gap-2 text-gray-600">
                    <MapPin className="w-5 h-5" />
                    <span className="text-lg">{trip.destination}, {trip.country}</span>
                  </div>
                </div>
                <span className={`${getBadgeColor(trip.type)} px-4 py-2 rounded-full text-sm font-semibold`}>
                  {trip.type}
                </span>
              </div>

              <p className="text-gray-700 leading-relaxed mb-6">{trip.description}</p>

              {/* Basic Info Grid */}
              <div className="grid grid-cols-2 md:grid-cols-3 gap-4 p-6 bg-gray-50 rounded-lg mb-6">
                <div>
                  <p className="text-sm text-gray-600">Departure</p>
                  <p className="font-semibold">{trip.departureDate}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Return</p>
                  <p className="font-semibold">{trip.returnDate}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Duration</p>
                  <p className="font-semibold">{trip.duration} days</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Price</p>
                  <p className="text-[#1565C0] font-bold text-xl">${trip.finalPrice}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Max Participants</p>
                  <p className="font-semibold">{trip.maxParticipants}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Available Spots</p>
                  <p className="text-[#4CAF50] font-bold text-xl">{trip.availableSpots}</p>
                </div>
              </div>

              {/* Type-Specific Info */}
              <div className="p-6 bg-blue-50 rounded-lg border border-blue-200">
                <h3 className="text-lg font-bold text-gray-900 mb-4">
                  {trip.type} Trip Details
                </h3>

                {trip.type === 'Adventure' && (
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <p className="text-sm text-gray-600">Difficulty Level</p>
                      <p className={`inline-block px-3 py-1 rounded-full text-sm font-semibold mt-1 ${
                        trip.difficultyLevel === 'EASY' ? 'bg-green-100 text-green-800' :
                        trip.difficultyLevel === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {trip.difficultyLevel}
                      </p>
                    </div>
                    <div>
                      <p className="text-sm text-gray-600">Equipment Included</p>
                      <p className="font-semibold mt-1">{trip.equipmentIncluded ? 'Yes' : 'No'}</p>
                    </div>
                  </div>
                )}

                {trip.type === 'Cultural' && (
                  <div className="space-y-4">
                    <div>
                      <p className="text-sm text-gray-600">Guided Tours</p>
                      <p className="font-semibold">{trip.guidedTours ? 'Yes' : 'No'}</p>
                    </div>
                    {trip.historicalSites && trip.historicalSites.length > 0 && (
                      <div>
                        <p className="text-sm text-gray-600 mb-2">Historical Sites</p>
                        <div className="flex flex-wrap gap-2">
                          {trip.historicalSites.map((site, i) => (
                            <span key={i} className="bg-purple-100 text-purple-800 px-3 py-1 rounded-full text-sm">
                              {site}
                            </span>
                          ))}
                        </div>
                      </div>
                    )}
                  </div>
                )}

                {trip.type === 'Vacation' && (
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <p className="text-sm text-gray-600">Resort Name</p>
                      <p className="font-semibold">{trip.resortName}</p>
                    </div>
                    <div>
                      <p className="text-sm text-gray-600">All Inclusive</p>
                      <p className="font-semibold">{trip.allInclusive ? 'Yes' : 'No'}</p>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>

          {/* Right Column - Book */}
          <div>
            <TripBookingPanel
              trip={trip}
              selectedCustomer={selectedCustomer}
              numberOfPeople={numberOfPeople}
              onChangePeople={setNumberOfPeople}
              booking={booking}
              message={message}
              onBook={handleBook}
            />
          </div>
        </div>

        <TripReservationsTable reservations={reservations} />
      </div>
    </div>
  );
}
