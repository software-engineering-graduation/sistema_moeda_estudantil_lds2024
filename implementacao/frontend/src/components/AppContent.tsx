import { useAuth } from "@/contexts/AuthContext";
import { useLocation, Link, Route, Routes, useNavigate } from "react-router-dom";
import { Button } from "./ui/button";
import FormularioEmpresa from "./FormularioEmpresa";
import FormularioFuncionarioEmpresa from "./FormularioFuncionarioEmpresa";
import FormularioVantagem from "./FormularioVantagem";
import ListaEmpresas from "./ListaEmpresas";
import ListaFuncionariosEmpresa from "./ListaFuncionariosEmpresa";
import ListaVantagens from "./ListaVantagens";
import Login from "./Login";
import { ProtectedRoute } from "./ProtectedRoute";
import StudentForm from "./StudentForm";
import StudentList from "./StudentList";
import ListaTransacoes from "./ListaTransacoes";
import FormularioTransacao from "./FormularioTransacao";
import ListaVantagensAluno from "./ListaVantagensAluno";
import InstitutionSemesters from "./InstitutionSemesters";

const AppContent: React.FC = () => {
    const { isAuthenticated, user, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const isLoginPage = location.pathname === '/login';

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <div className="min-h-screen bg-background font-sans antialiased">
            {isAuthenticated && !isLoginPage && (
                <header className="bg-primary text-primary-foreground shadow-md">
                    <nav className="container mx-auto px-4 py-4">
                        <div className="flex justify-between items-center">
                            <ul className="flex space-x-4">
                                <li><Link to="/"><Button>Home</Button></Link></li>
                                {user?.tipo === 'ADMIN' && (
                                    <>
                                        <li><Link to="/alunos"><Button>Alunos</Button></Link></li>
                                        <li><Link to="/instituicoes/semestres"><Button>Instituições</Button></Link></li>
                                    </>
                                )}
                                {(user?.tipo === 'ADMIN' || user?.tipo === 'EMPRESA') && (
                                    <li><Link to="/empresas"><Button>Empresas</Button></Link></li>
                                )}
                                {(user?.tipo === 'ALUNO') && (
                                    <li>
                                        <Link to="/vantagens">
                                            <Button>Vantagens</Button>
                                        </Link>
                                    </li>
                                )}
                                {user?.tipo !== 'EMPRESA' && (
                                    <li><Link to="/transacoes"><Button>Transações</Button></Link></li>
                                )}
                            </ul>
                            <div className="flex items-center space-x-4">
                                <div className="text-sm">
                                    <span className="font-medium">{user?.nome}</span>
                                    <span className="mx-2">|</span>
                                    <span className="text-primary-foreground/80">{user?.tipo}</span>
                                    {user?.tipo !== 'ADMIN' && user?.saldoMoedas && user?.saldoMoedas !== undefined && (
                                        <>
                                            <span className="mx-2">|</span>
                                            <span>{user.saldoMoedas.toFixed(2)} moedas</span>
                                        </>
                                    )}
                                </div>
                                <Button
                                    variant="secondary"
                                    onClick={handleLogout}
                                >
                                    Sair
                                </Button>
                            </div>
                        </div>
                    </nav>
                </header>
            )}
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
                    <Route path="/transacoes" element={<ProtectedRoute><ListaTransacoes /></ProtectedRoute>} />
                    <Route path="/vantagens" element={<ProtectedRoute><ListaVantagensAluno /></ProtectedRoute>}/>
                    <Route path="/instituicoes/semestres" element={<ProtectedRoute><InstitutionSemesters /></ProtectedRoute>} />
                    <Route path="/transacoes/nova" element={<ProtectedRoute><FormularioTransacao /></ProtectedRoute>} />
                </Routes>
            </main>
        </div>
    );
}

export default AppContent;