import React, { createContext, useContext, useState, useEffect } from 'react';
import { Customer, fetchAllCustomers } from '../data/api';

interface CustomerContextType {
  customers: Customer[];
  selectedCustomerId: number;
  selectedCustomer: Customer | null;
  setSelectedCustomerId: (id: number) => void;
  loading: boolean;
}

const CustomerContext = createContext<CustomerContextType | undefined>(undefined);

export function CustomerProvider({ children }: React.PropsWithChildren) {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState<number>(1);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAllCustomers()
      .then(data => {
        setCustomers(data);
        if (data.length > 0 && !data.find(c => c.id === selectedCustomerId)) {
          setSelectedCustomerId(data[0].id);
        }
      })
      .finally(() => setLoading(false));
  }, []);

  const selectedCustomer = customers.find(c => c.id === selectedCustomerId) || null;

  return (
    <CustomerContext.Provider value={{ 
      customers, 
      selectedCustomerId, 
      selectedCustomer,
      setSelectedCustomerId, 
      loading 
    }}>
      {children}
    </CustomerContext.Provider>
  );
}

export function useCustomer() {
  const context = useContext(CustomerContext);
  if (context === undefined) {
    throw new Error('useCustomer must be used within a CustomerProvider');
  }
  return context;
}
