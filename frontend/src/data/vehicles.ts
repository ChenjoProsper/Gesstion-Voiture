import { Vehicle, VehicleOption, Subsidiary } from '@/types/vehicle';

export const vehicles: Vehicle[] = [
  {
    id: '1',
    type: 'automobile',
    brand: 'Mercedes-Benz',
    model: 'AMG GT',
    power: 585,
    price: 189900,
    fuelType: 'essence',
    stock: 2,
    stockDays: 15,
    image: '/vehicles/mercedes-amg.jpg',
    year: 2024,
    color: 'Noir Obsidienne',
    description: 'La Mercedes-AMG GT incarne la quintessence du luxe sportif allemand.'
  },
  {
    id: '2',
    type: 'automobile',
    brand: 'Tesla',
    model: 'Model S Plaid',
    power: 1020,
    autonomy: 600,
    price: 139990,
    fuelType: 'electrique',
    stock: 5,
    stockDays: 8,
    image: '/vehicles/tesla-s.jpg',
    year: 2024,
    color: 'Blanc Nacré',
    description: 'Performance électrique ultime avec une accélération fulgurante.'
  },
  {
    id: '3',
    type: 'automobile',
    brand: 'Porsche',
    model: 'Taycan Turbo S',
    power: 761,
    autonomy: 450,
    price: 186944,
    fuelType: 'electrique',
    stock: 1,
    stockDays: 45,
    image: '/vehicles/porsche-taycan.jpg',
    year: 2024,
    color: 'Gris Volcanique',
    description: 'L\'électrique selon Porsche : sport, luxe et innovation.'
  },
  {
    id: '4',
    type: 'automobile',
    brand: 'BMW',
    model: 'M8 Competition',
    power: 625,
    price: 169950,
    fuelType: 'essence',
    stock: 3,
    stockDays: 90,
    image: '/vehicles/bmw-m8.jpg',
    year: 2024,
    color: 'Bleu Frozen',
    description: 'Le grand tourisme par excellence, alliant puissance et élégance.'
  },
  {
    id: '5',
    type: 'scooter',
    brand: 'BMW',
    model: 'CE 04',
    power: 42,
    autonomy: 130,
    price: 12100,
    fuelType: 'electrique',
    stock: 8,
    stockDays: 5,
    image: '/vehicles/bmw-ce04.jpg',
    year: 2024,
    color: 'Blanc Alpin',
    description: 'Le scooter électrique futuriste de BMW pour la mobilité urbaine.'
  },
  {
    id: '6',
    type: 'scooter',
    brand: 'Vespa',
    model: 'GTS 300 Super',
    power: 24,
    price: 6499,
    fuelType: 'essence',
    stock: 12,
    stockDays: 120,
    image: '/vehicles/vespa-gts.jpg',
    year: 2024,
    color: 'Rouge Racing',
    description: 'L\'icône italienne revisitée avec style et performance.'
  },
  {
    id: '7',
    type: 'automobile',
    brand: 'Audi',
    model: 'e-tron GT RS',
    power: 646,
    autonomy: 472,
    price: 152600,
    fuelType: 'electrique',
    stock: 2,
    stockDays: 30,
    image: '/vehicles/audi-etron.jpg',
    year: 2024,
    color: 'Gris Daytona',
    description: 'La sportive électrique d\'Audi, alliance de technologie et de design.'
  },
  {
    id: '8',
    type: 'scooter',
    brand: 'NIU',
    model: 'NQi GT Pro',
    power: 5,
    autonomy: 100,
    price: 4599,
    fuelType: 'electrique',
    stock: 15,
    stockDays: 60,
    image: '/vehicles/niu-nqi.jpg',
    year: 2024,
    color: 'Noir Mat',
    description: 'Le scooter électrique intelligent et connecté.'
  }
];

export const vehicleOptions: VehicleOption[] = [
  { id: 'opt-1', name: 'Sièges Sport', price: 2500, incompatibleWith: ['opt-2'] },
  { id: 'opt-2', name: 'Sièges Cuir Premium', price: 3800, incompatibleWith: ['opt-1'] },
  { id: 'opt-3', name: 'Toit Panoramique', price: 1800 },
  { id: 'opt-4', name: 'Pack Son Premium', price: 2200 },
  { id: 'opt-5', name: 'Aide à la Conduite', price: 3500 },
  { id: 'opt-6', name: 'Peinture Métallisée', price: 890 },
  { id: 'opt-7', name: 'Jantes Alliage 21"', price: 2100 },
  { id: 'opt-8', name: 'Régulateur Adaptatif', price: 1500 },
];

export const taxRates: Record<string, number> = {
  'France': 0.20,
  'Allemagne': 0.19,
  'Belgique': 0.21,
  'Suisse': 0.077,
  'Espagne': 0.21,
  'Italie': 0.22,
  'Cameroun': 0.1925,
  'Sénégal': 0.18,
  'Côte d\'Ivoire': 0.18,
  'Maroc': 0.20,
};

// Sample subsidiaries structure for Société users (Pattern Composite)
export const sampleSubsidiaries: Subsidiary[] = [
  {
    id: 'sub-1',
    name: 'Siège Social',
    location: 'Paris',
    parentId: null,
    children: [
      {
        id: 'sub-2',
        name: 'Filiale Nord',
        location: 'Lille',
        parentId: 'sub-1',
        children: [
          {
            id: 'sub-4',
            name: 'Agence Tourcoing',
            location: 'Tourcoing',
            parentId: 'sub-2',
            children: []
          }
        ]
      },
      {
        id: 'sub-3',
        name: 'Filiale Sud',
        location: 'Marseille',
        parentId: 'sub-1',
        children: [
          {
            id: 'sub-5',
            name: 'Agence Nice',
            location: 'Nice',
            parentId: 'sub-3',
            children: []
          },
          {
            id: 'sub-6',
            name: 'Agence Montpellier',
            location: 'Montpellier',
            parentId: 'sub-3',
            children: []
          }
        ]
      }
    ]
  }
];
