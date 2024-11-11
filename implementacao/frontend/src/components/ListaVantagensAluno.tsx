// ListaVantagensAluno.tsx
import api from '@/api'
import { Button } from '@/components/ui/button'
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle
} from '@/components/ui/card'
import { useAuth } from '@/contexts/AuthContext'
import { useToast } from '@/hooks/use-toast'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

interface Vantagem {
    id: number
    descricao: string
    foto: string
    custoMoedas: number
    quantidadeDisponivel: number
    empresa: {
        id: number
        nome: string
    }
}

interface CupomResgate {
    id: number
    codigo: string
    valor: number
    vantagem: Vantagem
    empresa: {
        nome: string
    }
}

export default function ListaVantagensAluno() {
    const { user } = useAuth()
    const { toast } = useToast()
    const queryClient = useQueryClient()

    const { data: vantagens = [], isLoading } = useQuery<Vantagem[]>({
        queryKey: ['vantagens'],
        queryFn: async () => {
            const response = await api.get('/vantagens')
            return response.data
        },
    })

    const resgateMutation = useMutation({
        mutationFn: async ({ alunoId, vantagemId }: { alunoId: number, vantagemId: number }) => {
            const response = await api.post(`/vantagens/resgatar?alunoId=${alunoId}&vantagemId=${vantagemId}`)
            return response.data
        },
        onSuccess: (data: CupomResgate) => {
            queryClient.invalidateQueries({ queryKey: ['vantagens'] })
            queryClient.invalidateQueries({ queryKey: ['user'] })
            queryClient.invalidateQueries({ queryKey: ['transacoes'] })
            toast({
                title: "Vantagem resgatada com sucesso!",
                description: `Seu código de cupom é: ${data.codigo}`,
            })
        },
        onError: (error: any) => {
            toast({
                title: "Erro ao resgatar vantagem",
                description: error.response?.data?.message || "Ocorreu um erro ao resgatar a vantagem",
                variant: "destructive",
            })
        },
    })

    const handleResgate = (vantagemId: number) => {
        if (!user?.id) return
        resgateMutation.mutate({ alunoId: user.id, vantagemId })
    }

    if (isLoading) return <div>Carregando...</div>

    return (
        <div>
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">Vantagens Disponíveis</h1>
                <div className="p-4 bg-secondary rounded-lg">
                    <span className="font-semibold">Saldo atual: </span>
                    <span>{user?.saldoMoedas?.toFixed(2)} moedas</span>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {vantagens.map(vantagem => (
                    <Card key={vantagem.id}>
                        {vantagem.foto && (
                            <img
                                src={vantagem.foto}
                                alt={vantagem.descricao}
                                className="w-full h-48 object-cover rounded-t-lg"
                            />
                        )}
                        <CardHeader>
                            <CardTitle>{vantagem.descricao}</CardTitle>
                        </CardHeader>
                        <CardContent>
                            <p className="font-semibold">Custo: {vantagem.custoMoedas} moedas</p>
                            <p>Quantidade disponível: {vantagem.quantidadeDisponivel}</p>
                        </CardContent>
                        <CardFooter>
                            <Button
                                onClick={() => handleResgate(vantagem.id)}
                                disabled={
                                    resgateMutation.isPending ||
                                    vantagem.quantidadeDisponivel <= 0 ||
                                    (user?.saldoMoedas || 0) < vantagem.custoMoedas
                                }
                                className="w-full"
                            >
                                {resgateMutation.isPending ? 'Resgatando...' : 'Resgatar'}
                            </Button>
                        </CardFooter>
                    </Card>
                ))}
            </div>
        </div>
    )
}