import React from 'react';
import { Link } from 'react-router-dom';
import { MapPin, Calendar, Users } from 'lucide-react';
import { Trip } from '../data/api';

interface TripCardProps {
  trip: Trip;
}

export function TripCard({ trip }: TripCardProps) {
  const getBadgeColor = (type: Trip['type']) => {
    switch (type) {
      case 'Adventure': return 'bg-[#4CAF50] text-white';
      case 'Cultural': return 'bg-purple-600 text-white';
      case 'Vacation': return 'bg-[#FF8F00] text-white';
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow p-5">
      <div className="flex items-start justify-between mb-3">
          <span className={`${getBadgeColor(trip.type)} px-3 py-1 rounded-full text-xs font-semibold`}>
            {trip.type}
          </span>
        <span className="text-xl font-bold text-[#1565C0]">${trip.finalPrice}</span>
      </div>

        <h3 className="text-lg font-bold text-gray-900 mb-2">{trip.name}</h3>
        
      <div className="flex items-center gap-1 text-gray-600 text-sm mb-3">
        <MapPin className="w-4 h-4 flex-shrink-0" />
          <span>{trip.destination}, {trip.country}</span>
        </div>

      <div className="flex items-center gap-4 text-sm text-gray-500 mb-4">
          <div className="flex items-center gap-1">
            <Calendar className="w-4 h-4" />
            <span>{trip.duration} days</span>
          </div>
          <div className="flex items-center gap-1">
            <Users className="w-4 h-4" />
            <span>{trip.availableSpots} spots</span>
          </div>
        </div>

          <Link
            to={`/trip/${trip.id}`}
        className="block w-full text-center bg-[#1565C0] text-white py-2 rounded-lg text-sm font-medium hover:bg-[#0D47A1] transition-colors"
          >
        Select a trip
          </Link>
    </div>
  );
}
