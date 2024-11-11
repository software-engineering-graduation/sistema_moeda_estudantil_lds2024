// FormularioTransacao.tsx
import api from '@/api'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import { useToast } from "@/hooks/use-toast"
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { AxiosError } from 'axios'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'

interface Aluno {
    id: number
    nome: string
}

interface TransacaoCreate {
    origem: number
    destino: number
    valor: number
    mensagem: string
}

const buscarAlunos = async (): Promise<Aluno[]> => {
    const { data } = await api.get('/alunos')
    return data
}

const criarTransacao = async (transacao: TransacaoCreate): Promise<void> => {
    await api.post('/transacoes/transferir', transacao)
}

export default function FormularioTransacao() {
    const { toast } = useToast()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const { user } = useAuth()
    const [transacao, setTransacao] = useState<TransacaoCreate>({
        origem: user?.id,
        destino: 0,
        valor: 0,
        mensagem: '',
    })

    const { data: alunos = [], isLoading } = useQuery<Aluno[]>({
        queryKey: ['alunos'],
        queryFn: buscarAlunos,
    })

    const criarMutacao = useMutation({
        mutationFn: criarTransacao,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['transacoes'] })
            queryClient.invalidateQueries({ queryKey: ['user'] })
            toast({
                title: "Sucesso",
                description: "Transferência realizada com sucesso!",
            })
            navigate('/transacoes')
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao realizar transferência: ${error instanceof AxiosError ? error.response?.data.message : 'Erro desconhecido'}`,
                variant: "destructive",
            })
        },
    })

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        criarMutacao.mutate(transacao)
    }

    if (isLoading) {
        return <div>Carregando...</div>
    }

    return (
        <form onSubmit={handleSubmit} className="space-y-4" >
            <h1 className="text-3xl font-bold mb-4" > Nova Transferência </h1>

            <div>
                <Label htmlFor="destino">Aluno Destinatário</Label>
                <Select
                    onValueChange={(value) => setTransacao(prev => ({ ...prev, destino: Number(value) }))}
                    required
                >
                    <SelectTrigger>
                        <SelectValue placeholder="Selecione um aluno" />
                    </SelectTrigger>
                    <SelectContent>
                        {
                            alunos.map(aluno => (
                                <SelectItem key={aluno.id} value={aluno.id.toString()} >
                                    {aluno.nome}
                                </SelectItem>
                            ))
                        }
                    </SelectContent>
                </Select>
            </div>

            <div>
                <Label htmlFor="valor">Valor</Label>
                <Input
                    id="valor"
                    type="number"
                    value={transacao.valor}
                    onChange={(e) => setTransacao(prev => ({ ...prev, valor: Number(e.target.value) }))}
                    min="0"
                    step="0.01"
                    required
                />
            </div>

            <div>
                <Label htmlFor="mensagem">Mensagem</Label>
                <Input
                    id="mensagem"
                    value={transacao.mensagem}
                    onChange={(e) => setTransacao(prev => ({ ...prev, mensagem: e.target.value }))}
                    required
                />
            </div>

            <Button type="submit">Enviar Moedas</Button>
        </form>
    )
}