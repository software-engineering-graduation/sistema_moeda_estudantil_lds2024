// hooks/useAuth.ts
import { authApi } from '@/api/auth'
import { useAuth } from '@/contexts/AuthContext'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { useToast } from './use-toast'

export function useLogin() {
    const queryClient = useQueryClient()
    const navigate = useNavigate()
    const { toast } = useToast()
    const { login: setAuth } = useAuth()

    return useMutation({
        mutationFn: authApi.login,
        onSuccess: (token) => {
            setAuth(token)
            queryClient.invalidateQueries({ queryKey: ['user'] })
            queryClient.invalidateQueries({ queryKey: ['empresa'] })
            queryClient.invalidateQueries({ queryKey: ['empresas'] })
            queryClient.invalidateQueries({ queryKey: ['funcionario'] })
            queryClient.invalidateQueries({ queryKey: ['alunos'] })
            queryClient.invalidateQueries({ queryKey: ['aluno'] })
            queryClient.invalidateQueries({ queryKey: ['funcionarios'] })
            queryClient.invalidateQueries({ queryKey: ['vantagens'] })
            queryClient.invalidateQueries({ queryKey: ['instituicoes'] })
            queryClient.invalidateQueries({ queryKey: ['transacoes'] })
            navigate('/')
            toast({
                title: "Sucesso",
                description: "Login realizado com sucesso!",
            })
        },
        onError: (error: Error) => {
            toast({
                title: "Erro",
                description: `Erro ao fazer login: ${error.message}`,
                variant: "destructive",
            })
        }
    })
}