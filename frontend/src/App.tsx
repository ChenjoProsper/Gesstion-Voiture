import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { CartProvider } from "@/context/CartContext";
import { AuthProvider } from "@/context/AuthContext";
import { StockProvider } from "@/context/StockContext";
import { OrderProvider } from "@/context/OrderContext";
import Index from "./pages/Index";
import Catalogue from "./pages/Catalogue";
import Cart from "./pages/Cart";
import Auth from "./pages/Auth";
import Documents from "./pages/Documents";
import Admin from "./pages/Admin";
import Societe from "./pages/Societe";
import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <AuthProvider>
      <StockProvider>
        <OrderProvider>
          <CartProvider>
            <TooltipProvider>
              <Toaster />
              <Sonner />
              <BrowserRouter>
                <Routes>
                  <Route path="/" element={<Index />} />
                  <Route path="/catalogue" element={<Catalogue />} />
                  <Route path="/panier" element={<Cart />} />
                  <Route path="/connexion" element={<Auth />} />
                  <Route path="/documents" element={<Documents />} />
                  <Route path="/admin" element={<Admin />} />
                  <Route path="/societe" element={<Societe />} />
                  <Route path="*" element={<NotFound />} />
                </Routes>
              </BrowserRouter>
            </TooltipProvider>
          </CartProvider>
        </OrderProvider>
      </StockProvider>
    </AuthProvider>
  </QueryClientProvider>
);

export default App;
