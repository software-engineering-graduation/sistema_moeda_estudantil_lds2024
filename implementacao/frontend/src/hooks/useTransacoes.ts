// hooks/useTransacoes.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import api from '@/api'
import { useToast } from '@/hooks/use-toast'

export interface Transacao {
    id: number
    origem: {
        id: number
        nome: string
        tipo: string
    }
    destino: {
        id: number
        nome: string
        tipo: string
    }
    valor: number
    mensagem: string
    data: string
}

export const useTransacoes = () => {
    return useQuery<Transacao[]>({
        queryKey: ['transacoes'],
        queryFn: async () => {
            const { data } = await api.get('/transacoes')
            return data
        }
    })
}

export const useReabastecerProfessores = () => {
    const { toast } = useToast()
    const queryClient = useQueryClient()

    return useMutation({
        mutationFn: async () => {
            await api.post('/transacoes/reabastecer')
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['transacoes'] })
            queryClient.invalidateQueries({ queryKey: ['user'] })
            toast({
                title: "Sucesso",
                description: "Saldo dos professores reabastecido!",
            })
        },
        onError: (error: Error) => {
            toast({
                title: "Erro",
                description: `Erro ao reabastecer: ${error.message}`,
                variant: "destructive",
            })
        }
    })
}