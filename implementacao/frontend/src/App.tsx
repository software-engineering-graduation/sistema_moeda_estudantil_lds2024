import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { Button } from '@/components/ui/button'
import StudentList from './components/StudentList'
import CompanyList from './components/CompanyList'
import StudentForm from './components/StudentForm'
import CompanyForm from './components/CompanyForm'
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
              <Route path="/" element={<h1 className="text-4xl font-bold">Welcome to Student Currency System</h1>} />
              <Route path="/alunos" element={<StudentList />} />
              <Route path="/alunos/novo" element={<StudentForm />} />
              <Route path="/alunos/:id" element={<StudentForm />} />
              <Route path="/empresas" element={<CompanyList />} />
              <Route path="/empresas/nova" element={<CompanyForm />} />
              <Route path="/empresas/:id" element={<CompanyForm />} />
            </Routes>
          </main>
        </div>
      </Router>
      <ReactQueryDevtools initialIsOpen={false} />
      <Toaster />
    </QueryClientProvider>
  )
}