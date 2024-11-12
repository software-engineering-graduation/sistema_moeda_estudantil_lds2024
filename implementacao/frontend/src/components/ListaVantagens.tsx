import api from '@/api'
import { Button } from '@/components/ui/button'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { useToast } from "@/hooks/use-toast"
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useNavigate, useParams } from 'react-router-dom'

interface Vantagem {
    id: number
    descricao: string
    foto: string
    custoMoedas: number
    quantidadeDisponivel: number
}

const buscarVantagens = async (empresaId: string): Promise<Vantagem[]> => {
    const { data } = await api.get(`/empresas/${empresaId}/vantagens`)
    return data
}

const excluirVantagem = async (empresaId: string, vantagemId: number): Promise<void> => {
    await api.delete(`/empresas/${empresaId}/vantagens/${vantagemId}`)
}

export default function ListaVantagens() {
    const { id: empresaId } = useParams<{ id: string }>()
    const navigate = useNavigate()
    const { toast } = useToast()
    const queryClient = useQueryClient()

    const { data: vantagens = [], isLoading, error } = useQuery<Vantagem[], Error>({
        queryKey: ['vantagens', empresaId],
        queryFn: () => buscarVantagens(empresaId!),
    })

    const excluirMutacao = useMutation({
        mutationFn: (vantagemId: number) => excluirVantagem(empresaId!, vantagemId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['vantagens', empresaId] })
            toast({
                title: "Sucesso",
                description: "Vantagem excluída com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao excluir vantagem: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })

    if (isLoading) return <div>Carregando...</div>
    if (error) return <div>Ocorreu um erro: {error.message}</div>

    return (
        <div>
            <h2 className="text-2xl font-bold mb-4">Vantagens da Empresa</h2>
            <Button onClick={() => navigate(`/empresas/${empresaId}/vantagens/nova`)} className="mb-4">
                Adicionar Nova Vantagem
            </Button>
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead>Descrição</TableHead>
                        <TableHead>Custo (Moedas)</TableHead>
                        <TableHead>Quantidade Disponível</TableHead>
                        <TableHead>Ações</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {vantagens.map((vantagem) => (
                        <TableRow key={vantagem.id}>
                            <TableCell>{vantagem.descricao}</TableCell>
                            <TableCell>{vantagem.custoMoedas}</TableCell>
                            <TableCell>{vantagem.quantidadeDisponivel}</TableCell>
                            <TableCell>
                                <Button onClick={() => navigate(`/empresas/${empresaId}/vantagens/${vantagem.id}`)} className="mr-2">
                                    Editar
                                </Button>
                                {/* <Button variant="destructive" onClick={() => excluirMutacao.mutate(vantagem.id)}>
                                    Excluir
                                </Button> */}
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    )
}