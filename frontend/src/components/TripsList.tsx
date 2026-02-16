import React, { useState, useEffect } from 'react';
import { fetchTrips, Trip } from '../data/api';
import { TripCard } from './TripCard';

export function TripsList() {
  const [trips, setTrips] = useState<Trip[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchTrips()
      .then(setTrips)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-xl text-gray-600">Loading trips...</div>
      </div>
    );
  }

  return (
    <div>
      {/* Hero Section */}
      <div className="bg-gradient-to-r from-[#1565C0] to-[#0D47A1] text-white py-8">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <h1 className="text-2xl font-bold mb-1">Discover Your Next Adventure</h1>
          <p className="text-base text-blue-100">
            Explore breathtaking destinations around the world
          </p>
        </div>
      </div>

      {/* Trips Grid */}
      <div className="max-w-7xl mx-auto px-4 py-6">
        <h2 className="text-xl font-bold text-gray-900 mb-4">
          Available Trips
        </h2>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {trips.map(trip => (
            <TripCard key={trip.id} trip={trip} />
          ))}
        </div>
      </div>
    </div>
  );
}
