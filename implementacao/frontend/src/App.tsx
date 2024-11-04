import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { BrowserRouter as Router } from 'react-router-dom'
import AppContent from './components/AppContent'
import { Toaster } from './components/ui/toaster'
import { AuthProvider } from './contexts/AuthContext'

const queryClient = new QueryClient()

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Router>
          <AppContent />
        </Router>
        <ReactQueryDevtools initialIsOpen={false} />
      </AuthProvider>
      <Toaster />
    </QueryClientProvider>
  )
}