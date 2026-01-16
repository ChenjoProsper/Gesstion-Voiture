import React, { createContext, useContext, useState, useCallback } from 'react';
import { Order, OrderDocument, CartItem } from '@/types/vehicle';

interface OrderContextType {
  orders: Order[];
  currentOrder: Order | null;
  createOrder: (
    items: CartItem[],
    total: number,
    taxAmount: number,
    country: string,
    paymentMethod: 'comptant' | 'credit'
  ) => Order;
  getOrder: (orderId: string) => Order | undefined;
}

const OrderContext = createContext<OrderContextType | undefined>(undefined);

// Document Builder Pattern
const buildDocuments = (order: Omit<Order, 'documents'>): OrderDocument[] => {
  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR',
      maximumFractionDigits: 0
    }).format(price);
  };

  const vehiclesList = order.items.map(item => 
    `${item.vehicle.brand} ${item.vehicle.model} (x${item.quantity})`
  ).join(', ');

  const immatriculation: OrderDocument = {
    id: `doc-imm-${order.id}`,
    type: 'immatriculation',
    name: "Demande d'immatriculation",
    generatedAt: new Date(),
    content: `
      <div class="document-header">
        <h1>DEMANDE D'IMMATRICULATION</h1>
        <p>Référence: IMM-${order.id}</p>
      </div>
      <div class="document-body">
        <h2>Véhicule(s) concerné(s)</h2>
        <p>${vehiclesList}</p>
        <h2>Informations du demandeur</h2>
        <p>Date de la demande: ${new Date().toLocaleDateString('fr-FR')}</p>
        <h2>Déclaration</h2>
        <p>Je soussigné(e), demande l'immatriculation du/des véhicule(s) mentionné(s) ci-dessus.</p>
      </div>
    `
  };

  const cession: OrderDocument = {
    id: `doc-cess-${order.id}`,
    type: 'cession',
    name: 'Certificat de cession',
    generatedAt: new Date(),
    content: `
      <div class="document-header">
        <h1>CERTIFICAT DE CESSION</h1>
        <p>Référence: CESS-${order.id}</p>
      </div>
      <div class="document-body">
        <h2>Véhicule(s) cédé(s)</h2>
        <p>${vehiclesList}</p>
        <h2>Vendeur</h2>
        <p>AUTO-PATTERN LUXURY</p>
        <p>Siège social: Paris, France</p>
        <h2>Acquéreur</h2>
        <p>[Nom de l'acquéreur]</p>
        <h2>Conditions de la vente</h2>
        <p>Prix de vente: ${formatPrice(order.total + order.taxAmount)}</p>
        <p>Date de cession: ${new Date().toLocaleDateString('fr-FR')}</p>
      </div>
    `
  };

  const commande: OrderDocument = {
    id: `doc-cmd-${order.id}`,
    type: 'commande',
    name: 'Bon de commande',
    generatedAt: new Date(),
    content: `
      <div class="document-header">
        <h1>BON DE COMMANDE</h1>
        <p>Commande N° ${order.id}</p>
      </div>
      <div class="document-body">
        <h2>Détail de la commande</h2>
        <table>
          <thead>
            <tr>
              <th>Véhicule</th>
              <th>Options</th>
              <th>Quantité</th>
              <th>Prix unitaire</th>
            </tr>
          </thead>
          <tbody>
            ${order.items.map(item => `
              <tr>
                <td>${item.vehicle.brand} ${item.vehicle.model}</td>
                <td>${item.options.map(o => o.name).join(', ') || 'Aucune'}</td>
                <td>${item.quantity}</td>
                <td>${formatPrice(item.vehicle.price)}</td>
              </tr>
            `).join('')}
          </tbody>
        </table>
        <h2>Récapitulatif</h2>
        <p>Sous-total HT: ${formatPrice(order.total)}</p>
        <p>TVA (${order.country}): ${formatPrice(order.taxAmount)}</p>
        <p><strong>Total TTC: ${formatPrice(order.total + order.taxAmount)}</strong></p>
        <h2>Mode de paiement</h2>
        <p>${order.paymentMethod === 'comptant' ? 'Paiement au comptant' : 'Demande de crédit'}</p>
      </div>
    `
  };

  return [immatriculation, cession, commande];
};

export const OrderProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [currentOrder, setCurrentOrder] = useState<Order | null>(null);

  const createOrder = useCallback((
    items: CartItem[],
    total: number,
    taxAmount: number,
    country: string,
    paymentMethod: 'comptant' | 'credit'
  ): Order => {
    const orderId = `ORD-${Date.now().toString(36).toUpperCase()}`;
    
    const orderBase = {
      id: orderId,
      items,
      total,
      taxAmount,
      country,
      paymentMethod,
      createdAt: new Date()
    };

    const documents = buildDocuments(orderBase);

    const newOrder: Order = {
      ...orderBase,
      documents
    };

    setOrders(prev => [...prev, newOrder]);
    setCurrentOrder(newOrder);
    
    return newOrder;
  }, []);

  const getOrder = useCallback((orderId: string) => {
    return orders.find(o => o.id === orderId);
  }, [orders]);

  return (
    <OrderContext.Provider
      value={{
        orders,
        currentOrder,
        createOrder,
        getOrder
      }}
    >
      {children}
    </OrderContext.Provider>
  );
};

export const useOrder = () => {
  const context = useContext(OrderContext);
  if (!context) {
    throw new Error('useOrder must be used within an OrderProvider');
  }
  return context;
};
