import React, { createContext, useContext, useState, useCallback } from 'react';
import { CartItem, Vehicle, VehicleOption } from '@/types/vehicle';

interface CartState {
  items: CartItem[];
  history: CartItem[][];
}

interface CartContextType {
  items: CartItem[];
  addToCart: (vehicle: Vehicle) => void;
  removeFromCart: (vehicleId: string) => void;
  updateQuantity: (vehicleId: string, quantity: number) => void;
  addOption: (vehicleId: string, option: VehicleOption) => void;
  removeOption: (vehicleId: string, optionId: string) => void;
  clearCart: () => void;
  undo: () => void;
  canUndo: boolean;
  total: number;
  itemCount: number;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, setState] = useState<CartState>({ items: [], history: [] });

  const saveHistory = useCallback(() => {
    setState(prev => ({
      ...prev,
      history: [...prev.history.slice(-9), prev.items]
    }));
  }, []);

  const addToCart = useCallback((vehicle: Vehicle) => {
    saveHistory();
    setState(prev => {
      const existingItem = prev.items.find(item => item.vehicle.id === vehicle.id);
      if (existingItem) {
        return {
          ...prev,
          items: prev.items.map(item =>
            item.vehicle.id === vehicle.id
              ? { ...item, quantity: item.quantity + 1 }
              : item
          )
        };
      }
      return {
        ...prev,
        items: [...prev.items, { vehicle, quantity: 1, options: [] }]
      };
    });
  }, [saveHistory]);

  const removeFromCart = useCallback((vehicleId: string) => {
    saveHistory();
    setState(prev => ({
      ...prev,
      items: prev.items.filter(item => item.vehicle.id !== vehicleId)
    }));
  }, [saveHistory]);

  const updateQuantity = useCallback((vehicleId: string, quantity: number) => {
    saveHistory();
    if (quantity <= 0) {
      removeFromCart(vehicleId);
      return;
    }
    setState(prev => ({
      ...prev,
      items: prev.items.map(item =>
        item.vehicle.id === vehicleId ? { ...item, quantity } : item
      )
    }));
  }, [saveHistory, removeFromCart]);

  const addOption = useCallback((vehicleId: string, option: VehicleOption) => {
    saveHistory();
    setState(prev => ({
      ...prev,
      items: prev.items.map(item => {
        if (item.vehicle.id !== vehicleId) return item;
        
        // Remove incompatible options
        const filteredOptions = item.options.filter(
          opt => !option.incompatibleWith?.includes(opt.id)
        );
        
        return {
          ...item,
          options: [...filteredOptions, option]
        };
      })
    }));
  }, [saveHistory]);

  const removeOption = useCallback((vehicleId: string, optionId: string) => {
    saveHistory();
    setState(prev => ({
      ...prev,
      items: prev.items.map(item =>
        item.vehicle.id === vehicleId
          ? { ...item, options: item.options.filter(opt => opt.id !== optionId) }
          : item
      )
    }));
  }, [saveHistory]);

  const clearCart = useCallback(() => {
    saveHistory();
    setState(prev => ({ ...prev, items: [] }));
  }, [saveHistory]);

  const undo = useCallback(() => {
    setState(prev => {
      if (prev.history.length === 0) return prev;
      const lastState = prev.history[prev.history.length - 1];
      return {
        items: lastState,
        history: prev.history.slice(0, -1)
      };
    });
  }, []);

  const total = state.items.reduce((sum, item) => {
    const optionsTotal = item.options.reduce((optSum, opt) => optSum + opt.price, 0);
    return sum + (item.vehicle.price + optionsTotal) * item.quantity;
  }, 0);

  const itemCount = state.items.reduce((sum, item) => sum + item.quantity, 0);

  return (
    <CartContext.Provider
      value={{
        items: state.items,
        addToCart,
        removeFromCart,
        updateQuantity,
        addOption,
        removeOption,
        clearCart,
        undo,
        canUndo: state.history.length > 0,
        total,
        itemCount
      }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};
