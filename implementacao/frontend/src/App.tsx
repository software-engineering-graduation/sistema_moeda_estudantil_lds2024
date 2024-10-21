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

const queryClient = new QueryClient()

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
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
              <Route path="/" element={<h1 className="text-4xl font-bold">Bem-vindo ao Sistema de Moeda Estudantil</h1>} />
              <Route path="/alunos" element={<StudentList />} />
              <Route path="/alunos/novo" element={<StudentForm />} />
              <Route path="/alunos/:id" element={<StudentForm />} />
              <Route path="/empresas">
                <Route index element={<ListaEmpresas />} />
                <Route path="nova" element={<FormularioEmpresa />} />
                <Route path=":id" element={<FormularioEmpresa />} />
                <Route path=":id/funcionarios" element={<ListaFuncionariosEmpresa />} />
                <Route path=":id/funcionarios/novo" element={<FormularioFuncionarioEmpresa />} />
                <Route path=":id/funcionarios/:funcionarioId" element={<FormularioFuncionarioEmpresa />} />
                <Route path="/empresas/:id/vantagens" element={<ListaVantagens />} />
                <Route path="/empresas/:id/vantagens/nova" element={<FormularioVantagem />} />
                <Route path="/empresas/:id/vantagens/:vantagemId" element={<FormularioVantagem />} />
              </Route>
            </Routes>
          </main>
        </div>
      </Router>
      <ReactQueryDevtools initialIsOpen={false} />
      <Toaster />
    </QueryClientProvider>
  )
}