import { Button } from '@/components/ui/button'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { Link, Route, BrowserRouter as Router, Routes } from 'react-router-dom'
import FormularioEmpresa from './components/FormularioEmpresa'
import FormularioFuncionarioEmpresa from './components/FormularioFuncionarioEmpresa'
import FormularioVantagem from './components/FormularioVantagem'
import ListaEmpresas from './components/ListaEmpresas'
import ListaFuncionariosEmpresa from './components/ListaFuncionariosEmpresa'
import ListaVantagens from './components/ListaVantagens'
import StudentForm from './components/StudentForm'
import StudentList from './components/StudentList'
import { Toaster } from './components/ui/toaster'
import Login from './components/Login'
import { ProtectedRoute } from './components/ProtectedRoute'
import { AuthProvider } from './contexts/AuthContext'

const queryClient = new QueryClient()

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Router>
          <div className="min-h-screen bg-background font-sans antialiased">
            <header className="bg-primary text-primary-foreground shadow-md">
              <nav className="container mx-auto px-4 py-4">
                <ul className="flex space-x-4">
                  <li><Link to="/"><Button>Home</Button></Link></li>
                  <li><Link to="/alunos"><Button>Alunos</Button></Link></li>
                  <li><Link to="/empresas"><Button>Empresas</Button></Link></li>
                </ul>
              </nav>
            </header>
            <main className="container mx-auto px-4 py-8">
              <Routes>
                <Route path="/login" element={<Login />} />

                {/* Protect other routes */}
                <Route path="/" element={<ProtectedRoute><h1 className="text-4xl font-bold">Bem-vindo ao Sistema de Moeda Estudantil</h1></ProtectedRoute>} />
                <Route path="/alunos" element={<ProtectedRoute><StudentList /></ProtectedRoute>} />
                <Route path="/alunos/novo" element={<ProtectedRoute><StudentForm /></ProtectedRoute>} />
                <Route path="/alunos/:id" element={<ProtectedRoute><StudentForm /></ProtectedRoute>} />
                <Route path="/empresas">
                  <Route index element={<ProtectedRoute><ListaEmpresas /></ProtectedRoute>} />
                  <Route path="nova" element={<ProtectedRoute><FormularioEmpresa /></ProtectedRoute>} />
                  <Route path=":id" element={<ProtectedRoute><FormularioEmpresa /></ProtectedRoute>} />
                  <Route path=":id/funcionarios" element={<ProtectedRoute><ListaFuncionariosEmpresa /></ProtectedRoute>} />
                  <Route path=":id/funcionarios/novo" element={<ProtectedRoute><FormularioFuncionarioEmpresa /></ProtectedRoute>} />
                  <Route path=":id/funcionarios/:funcionarioId" element={<ProtectedRoute><FormularioFuncionarioEmpresa /></ProtectedRoute>} />
                  <Route path="/empresas/:id/vantagens" element={<ProtectedRoute><ListaVantagens /></ProtectedRoute>} />
                  <Route path="/empresas/:id/vantagens/nova" element={<ProtectedRoute><FormularioVantagem /></ProtectedRoute>} />
                  <Route path="/empresas/:id/vantagens/:vantagemId" element={<ProtectedRoute><FormularioVantagem /></ProtectedRoute>} />
                </Route>
              </Routes>
            </main>
          </div>
        </Router>
        <ReactQueryDevtools initialIsOpen={false} />
      </AuthProvider>
      <Toaster />
    </QueryClientProvider>
  )
}