import React, { createContext, useContext, useState, useCallback } from 'react';

interface SolderedVehicle {
  id: string;
  discount: number; // Percentage discount
  solderedAt: Date;
}

interface StockCommand {
  type: 'SOLDER' | 'UNSOLDER';
  vehicleId: string;
  discount?: number;
  timestamp: Date;
}

interface StockContextType {
  solderedVehicles: SolderedVehicle[];
  commandHistory: StockCommand[];
  solderVehicle: (vehicleId: string, discount?: number) => void;
  unsolderVehicle: (vehicleId: string) => void;
  isSoldered: (vehicleId: string) => boolean;
  getDiscount: (vehicleId: string) => number;
  undoLastCommand: () => void;
  canUndo: boolean;
}

const StockContext = createContext<StockContextType | undefined>(undefined);

export const StockProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [solderedVehicles, setSolderedVehicles] = useState<SolderedVehicle[]>([]);
  const [commandHistory, setCommandHistory] = useState<StockCommand[]>([]);

  const solderVehicle = useCallback((vehicleId: string, discount: number = 15) => {
    const command: StockCommand = {
      type: 'SOLDER',
      vehicleId,
      discount,
      timestamp: new Date()
    };
    setCommandHistory(prev => [...prev, command]);
    
    setSolderedVehicles(prev => {
      if (prev.find(v => v.id === vehicleId)) return prev;
      return [...prev, { id: vehicleId, discount, solderedAt: new Date() }];
    });
  }, []);

  const unsolderVehicle = useCallback((vehicleId: string) => {
    const vehicle = solderedVehicles.find(v => v.id === vehicleId);
    if (!vehicle) return;

    const command: StockCommand = {
      type: 'UNSOLDER',
      vehicleId,
      discount: vehicle.discount,
      timestamp: new Date()
    };
    setCommandHistory(prev => [...prev, command]);
    
    setSolderedVehicles(prev => prev.filter(v => v.id !== vehicleId));
  }, [solderedVehicles]);

  const isSoldered = useCallback((vehicleId: string) => {
    return solderedVehicles.some(v => v.id === vehicleId);
  }, [solderedVehicles]);

  const getDiscount = useCallback((vehicleId: string) => {
    const vehicle = solderedVehicles.find(v => v.id === vehicleId);
    return vehicle?.discount || 0;
  }, [solderedVehicles]);

  const undoLastCommand = useCallback(() => {
    const lastCommand = commandHistory[commandHistory.length - 1];
    if (!lastCommand) return;

    if (lastCommand.type === 'SOLDER') {
      setSolderedVehicles(prev => prev.filter(v => v.id !== lastCommand.vehicleId));
    } else {
      setSolderedVehicles(prev => [
        ...prev,
        { id: lastCommand.vehicleId, discount: lastCommand.discount || 15, solderedAt: new Date() }
      ]);
    }
    
    setCommandHistory(prev => prev.slice(0, -1));
  }, [commandHistory]);

  return (
    <StockContext.Provider
      value={{
        solderedVehicles,
        commandHistory,
        solderVehicle,
        unsolderVehicle,
        isSoldered,
        getDiscount,
        undoLastCommand,
        canUndo: commandHistory.length > 0
      }}
    >
      {children}
    </StockContext.Provider>
  );
};

export const useStock = () => {
  const context = useContext(StockContext);
  if (!context) {
    throw new Error('useStock must be used within a StockProvider');
  }
  return context;
};
