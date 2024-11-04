// api/auth.ts
import { User } from '@/contexts/AuthContext'
import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080'
})

interface LoginCredentials {
    username: string
    password: string
}

export const authApi = {
    login: async (credentials: LoginCredentials): Promise<string> => {
        const basicAuth = btoa(`${credentials.username}:${credentials.password}`)
        const { data } = await api.post('/authenticate', null, {
            headers: {
                'Authorization': `Basic ${basicAuth}`
            }
        })
        return data
    },

    getCurrentUser: async (): Promise<User> => {
        const { data } = await api.get('/authenticate/me', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
        return data
    }
}