import React from 'react';
import { Trip, Customer } from '../data/api';

interface TripBookingPanelProps {
  trip: Trip;
  selectedCustomer: Customer | null;
  numberOfPeople: number;
  onChangePeople: (value: number) => void;
  booking: boolean;
  message: string;
  onBook: () => void;
}

export function TripBookingPanel({
  trip,
  selectedCustomer,
  numberOfPeople,
  onChangePeople,
  booking,
  message,
  onBook
}: TripBookingPanelProps) {
  return (
    <div className="bg-gradient-to-br from-[#1565C0] to-[#0D47A1] rounded-xl shadow-md p-6 text-white sticky top-24">
      <h3 className="text-xl font-bold mb-2">Book This Trip</h3>
      <p className="text-sm text-white/80 mb-6">Booking as: <strong>{selectedCustomer?.name}</strong></p>

      <div className="mb-6">
        <label className="block text-sm mb-2">Number of People</label>
        <select
          value={numberOfPeople}
          onChange={(e) => onChangePeople(Number(e.target.value))}
          className="w-full px-4 py-3 rounded-lg text-gray-900 font-medium"
        >
          {Array.from({ length: Math.min(10, trip.availableSpots || 1) }, (_, i) => i + 1).map(num => (
            <option key={num} value={num}>{num} {num === 1 ? 'person' : 'people'}</option>
          ))}
        </select>
      </div>

      <div className="bg-white/10 rounded-lg p-4 mb-6">
        <div className="flex justify-between mb-2">
          <span className="text-sm">Price per person</span>
          <span className="font-semibold">${trip.finalPrice}</span>
        </div>
        <div className="flex justify-between mb-2">
          <span className="text-sm">People</span>
          <span className="font-semibold">×{numberOfPeople}</span>
        </div>
        <div className="border-t border-white/20 pt-2 mt-2 flex justify-between">
          <span className="font-semibold">Total</span>
          <span className="text-2xl font-bold">${trip.finalPrice * numberOfPeople}</span>
        </div>
      </div>

      <button
        onClick={onBook}
        disabled={booking || trip.availableSpots === 0}
        className={`w-full text-white font-bold py-4 rounded-lg ${
          trip.availableSpots === 0
            ? 'bg-gray-400 cursor-not-allowed'
            : 'bg-[#FF8F00] hover:bg-[#E68000]'
        }`}
      >
        {booking ? 'Booking...' : 'Book Now'}
      </button>

      {message && <p className="text-sm text-center mt-4 bg-white/20 p-2 rounded">{message}</p>}
    </div>
  );
}
