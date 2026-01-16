//export type VehicleType = 'automobile' | 'scooter';
export type FuelType = 'essence' | 'electrique';
export type UserType = 'particulier' | 'societe' | 'admin';
/*
export interface Vehicle {
  id: number;
  reference: string;
  marque: string;
  modele: string;
  prixBase: number;
  cylindree: number;
  nombrePortes: number;
  espaceCoffre: number;
  imageData: string[];
  typeVehicule: 'AUTO_ELECTRIQUE' | 'AUTO_ESSENCE' | 'SCOOTER_ELECTRIQUE' | 'SCOOTER_ESSENCE';
  description: string;
}

export interface Vehicle {
  id: string;
  type: VehicleType;
  brand: string;
  model: string;
  power: number; // cv
  autonomy?: number; // km for electric
  price: number;
  fuelType: FuelType;
  stock: number;
  stockDays: number; // days in stock
  image: string;
  year: number;
  color: string;
  description: string;
}
  */

export type VehicleType = 'AUTO_ELECTRIQUE' | 'AUTO_ESSENCE' | 'SCOOTER_ELECTRIQUE' | 'SCOOTER_ESSENCE';

export interface Vehicle {
  id: number; // integer($int64) en backend
  reference: string;
  marque: string;
  modele: string;
  prixBase: number;
  cylindree: number;
  imageLink: string; // Chemin de l'image fourni par le backend
  nombrePortes: number;
  espaceCoffre: number;
  imageData: string[];
  typeVehicule: "AUTO_ELECTRIQUE";
  description: string;
  // Champs optionnels pour la logique front si nécessaire
  stock?: number;
}

export interface CartItem {
  vehicle: Vehicle;
  quantity: number;
  options: VehicleOption[];
}

export interface VehicleOption {
  id: string;
  name: string;
  price: number;
  incompatibleWith?: string[];
}

export interface User {
  id: string;
  email: string;
  name: string;
  type: UserType;
  company?: string;
}

export interface Subsidiary {
  id: string;
  name: string;
  location: string;
  parentId: string | null;
  children: Subsidiary[];
}

export interface Document {
  id: string;
  type: 'immatriculation' | 'cession' | 'commande';
  name: string;
  createdAt: Date;
  vehicleId: string;
}

export interface OrderDocument {
  id: string;
  type: 'immatriculation' | 'cession' | 'commande';
  name: string;
  content: string;
  generatedAt: Date;
}

export interface Order {
  id: string;
  items: CartItem[];
  total: number;
  taxAmount: number;
  country: string;
  paymentMethod: 'comptant' | 'credit';
  createdAt: Date;
  documents: OrderDocument[];
}
