// src/components/ProfessorList.tsx
import api from '@/api'
import { Button } from '@/components/ui/button'
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from '@/components/ui/table'
import { useQuery } from '@tanstack/react-query'
import { Link, useNavigate, useParams } from 'react-router-dom'

interface Professor {
    id: number
    nome: string
    email: string
    saldoMoedas: number
}

const getProfessors = async (institutionId: string) => {
    const { data } = await api.get(`/instituicoes/${institutionId}/professores`)
    return data
}

export default function ProfessorList() {
    const { institutionId } = useParams<{ institutionId: string }>()
    const navigate = useNavigate()
    const { data: professors = [], isLoading, error } = useQuery<Professor[]>({
        queryKey: ['professors', institutionId],
        queryFn: async () => getProfessors(institutionId!),
        enabled: !!institutionId,
    })

    if (isLoading) return <div>Carregando...</div>
    if (error) return <div>Ocorreu um erro: {error.message}</div>

    return (
        <div>
            <h1 className="text-3xl font-bold mb-4">Professores</h1>
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead>Nome</TableHead>
                        <TableHead>Email</TableHead>
                        <TableHead>Saldo</TableHead>
                        <TableHead>Ações</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {professors.map((professor) => (
                        <TableRow key={professor.id}>
                            <TableCell>{professor.nome}</TableCell>
                            <TableCell>{professor.email}</TableCell>
                            <TableCell>{professor.saldoMoedas}</TableCell>
                            <TableCell>
                                <Link to={`/instituicoes/${institutionId}/professores/${professor.id}`} className="btn">Editar</Link>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    )
}