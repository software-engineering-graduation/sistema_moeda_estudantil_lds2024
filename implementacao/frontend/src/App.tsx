import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { Button } from '@/components/ui/button'
import StudentList from './components/StudentList'
import CompanyList from './components/CompanyList'
import StudentForm from './components/StudentForm'
import CompanyForm from './components/CompanyForm'

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
                <li><Link to="/students"><Button>Students</Button></Link></li>
                <li><Link to="/companies"><Button>Companies</Button></Link></li>
              </ul>
            </nav>
          </header>
          <main className="container mx-auto px-4 py-8">
            <Routes>
              <Route path="/" element={<h1 className="text-4xl font-bold">Welcome to Student Currency System</h1>} />
              <Route path="/students" element={<StudentList />} />
              <Route path="/students/new" element={<StudentForm />} />
              <Route path="/students/:id" element={<StudentForm />} />
              <Route path="/companies" element={<CompanyList />} />
              <Route path="/companies/new" element={<CompanyForm />} />
              <Route path="/companies/:id" element={<CompanyForm />} />
            </Routes>
          </main>
        </div>
      </Router>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  )
}