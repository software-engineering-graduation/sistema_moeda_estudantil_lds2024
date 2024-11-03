import api from '@/api'
import { Button } from '@/components/ui/button'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { useQuery } from '@tanstack/react-query'
import { useNavigate, useParams } from 'react-router-dom'

interface FuncionarioEmpresa {
    id: number
    nome: string
    email: string
    tipo: string
}

const buscarFuncionarios = async (empresaId: string): Promise<FuncionarioEmpresa[]> => {
    const { data } = await api.get(`/empresas/${empresaId}/funcionarios`)
    return data
}

export default function ListaFuncionariosEmpresa() {
    const { id: empresaId } = useParams<{ id: string }>()
    const navigate = useNavigate()

    const { data: funcionarios = [], isLoading, error } = useQuery<FuncionarioEmpresa[], Error>({
        queryKey: ['funcionarios', empresaId],
        queryFn: () => buscarFuncionarios(empresaId!),
    })

    if (isLoading) return <div>Carregando...</div>
    if (error) return <div>Ocorreu um erro: {error.message}</div>

    return (
        <div>
            <h2 className="text-2xl font-bold mb-4">Funcionários da Empresa</h2>
            <Button onClick={() => navigate(`/empresas/${empresaId}/funcionarios/novo`)} className="mb-4">
                Adicionar Novo Funcionário
            </Button>
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead>Nome</TableHead>
                        <TableHead>Email</TableHead>
                        <TableHead>Ações</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {funcionarios.map((funcionario) => (
                        <TableRow key={funcionario.id}>
                            <TableCell>{funcionario.nome}</TableCell>
                            <TableCell>{funcionario.email}</TableCell>
                            <TableCell>
                                <Button onClick={() => navigate(`/empresas/${empresaId}/funcionarios/${funcionario.id}`)}>
                                    Editar
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    )
}