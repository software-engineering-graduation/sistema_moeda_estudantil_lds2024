// contexts/AuthContext.tsx
import api from '@/api'
import { authApi } from '@/api/auth'
import { useToast } from '@/hooks/use-toast'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { createContext, useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'

export interface User {
    id: number
    nome: string
    email: string
    tipo: 'ADMIN' | 'PROFESSOR' | 'ALUNO' | 'EMPRESA'
    saldoMoedas: number
}

interface AuthContextType {
    isAuthenticated: boolean
    token: string | null
    user: User | null
    login: (token: string) => void
    logout: () => void
}

const AuthContext = createContext<AuthContextType | null>(null)

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'))
    const queryClient = useQueryClient()

    const { data: user } = useQuery<User>({
        queryKey: ['user'],
        queryFn: authApi.getCurrentUser,
        enabled: !!token,
        retry: false,
        staleTime: 1000 * 60 * 5, // 5 minutes
    })

    const login = (newToken: string) => {
        localStorage.setItem('token', newToken)
        setToken(newToken)
        api.defaults.headers.common['Authorization'] = `Bearer ${newToken}`
    }

    const logout = () => {
        localStorage.removeItem('token')
        queryClient.invalidateQueries({ queryKey: ['user'] })
        queryClient.invalidateQueries({ queryKey: ['transacoes'] })
        setToken(null)
        delete api.defaults.headers.common['Authorization']
        queryClient.clear() // Limpa o cache ao fazer logout
    }

    return (
        <AuthContext.Provider value={{
            isAuthenticated: !!token,
            token,
            user: user ?? null,
            login,
            logout
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export function useLogout() {
    const queryClient = useQueryClient()
    const navigate = useNavigate()
    const { toast } = useToast()
    const { logout: clearAuth } = useAuth()

    return () => {
        clearAuth()
        queryClient.clear() // Limpa todo o cache
        navigate('/login')
        toast({
            title: "Sucesso",
            description: "Logout realizado com sucesso!",
        })
    }
}

export const useAuth = () => {
    const context = useContext(AuthContext)
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider')
    }
    return context
}