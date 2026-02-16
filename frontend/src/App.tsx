import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Header } from './components/Header';
import { TripsList } from './components/TripsList';
import { TripDetails } from './components/TripDetails';
import { MyBookings } from './components/MyBookings';
import { ReservationsTable } from './components/ReservationsTable';
import { CustomerProvider } from './context/CustomerContext';

export default function App() {
  return (
    <CustomerProvider>
    <Router>
      <div className="min-h-screen bg-gray-50">
        <Routes>
          <Route path="/" element={<><Header /><TripsList /></>} />
          <Route path="/trip/:id" element={<><Header /><TripDetails /></>} />
          <Route path="/my-bookings" element={<><Header /><MyBookings /></>} />
          <Route path="/reservations-table" element={<><Header /><ReservationsTable /></>} />
        </Routes>
      </div>
    </Router>
    </CustomerProvider>
  );
}
