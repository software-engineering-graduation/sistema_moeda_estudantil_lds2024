// src/components/InstitutionList.tsx
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
import { useToast } from "@/hooks/use-toast"
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
interface Instituicao {
    id: number
    nome: string
    endereco: string
}

const buscarInstituicoes = async (): Promise<Instituicao[]> => {
    const { data } = await api.get('/instituicoes')
    return data
}

const excluirInstituicao = async (id: number): Promise<void> => {
    await api.delete(`/instituicoes/${id}`)
}

export default function InstitutionList() {
    const { toast } = useToast()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const excluirMutacao = useMutation({
        mutationFn: excluirInstituicao,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['instituicoes'] })
            toast({
                title: "Sucesso",
                description: "Instituição excluída com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao excluir instituição: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })
    const { data: instituicoes = [], isLoading, error } = useQuery<Instituicao[], Error>({
        queryKey: ['instituicoes'],
        queryFn: buscarInstituicoes,
    })
    if (isLoading) return <div>Carregando...</div>
    if (error) return <div>Ocorreu um erro: {error.message}</div>
    return (
        <div>
            <h1 className="text-3xl font-bold mb-4">Instituições</h1>
            <Button onClick={() => navigate('/instituicoes/nova')} className="mb-4">Adicionar Nova Instituição</Button>
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead>Nome</TableHead>
                        <TableHead>Endereço</TableHead>
                        <TableHead>Ações</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {instituicoes.map((instituicao) => (
                        <TableRow key={instituicao.id}>
                            <TableCell>{instituicao.nome}</TableCell>
                            <TableCell>{instituicao.endereco}</TableCell>
                            <TableCell>
                                <Button onClick={() => navigate(`/instituicoes/${instituicao.id}`)}>Editar</Button>
                                <Button onClick={() => navigate(`/instituicoes/${instituicao.id}/semestres`)} className="ml-2">Gerenciar Semestres</Button>
                                <Button onClick={() => navigate(`/instituicoes/${instituicao.id}/professores`)} className="ml-2">Gerenciar Professores</Button>
                                <Button
                                    variant="destructive"
                                    onClick={() => {
                                        if (window.confirm('Tem certeza que deseja excluir esta instituição?')) {
                                            excluirMutacao.mutate(instituicao.id)
                                        }
                                    }}
                                    className="ml-2"
                                >
                                    Excluir
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    )
}