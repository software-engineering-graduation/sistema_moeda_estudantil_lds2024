import { Navigate } from 'react-router-dom'

interface ProtectedRouteProps {
    children: React.ReactNode
}

export function ProtectedRoute({ children }: ProtectedRouteProps) {
    const token = localStorage.getItem('token')

    if (!token) {
        // Redirect to login if there's no token
        return <Navigate to="/login" replace />
    }

    return <>{children}</>
}