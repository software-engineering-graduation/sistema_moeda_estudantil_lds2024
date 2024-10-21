import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

interface Company {
    id?: number
    nome: string
    email: string
    senha: string
    cnpj: string
}

const fetchCompany = async (id: string): Promise<Company> => {
    const response = await fetch(`http://localhost:8080/empresas/${id}`)
    if (!response.ok) {
        throw new Error('Network response was not ok')
    }
    return response.json()
}

const createCompany = async (company: Company): Promise<Company> => {
    const response = await fetch('http://localhost:8080/empresas', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(company),
    })
    if (!response.ok) {
        throw new Error('Network response was not ok')
    }
    return response.json()
}

const updateCompany = async (company: Company): Promise<Company> => {
    const response = await fetch(`http://localhost:8080/empresas/${company.id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(company),
    })
    if (!response.ok) {
        throw new Error('Network response was not ok')
    }
    return response.json()
}

export default function CompanyForm() {
    const { id } = useParams<{ id: string }>()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const [company, setCompany] = useState<Company>({
        nome: '',
        email: '',
        senha: '',
        cnpj: '',
    })

    const { isLoading: isFetchingCompany, data } = useQuery<Company, Error>({
        queryKey: ['company', id],
        queryFn: () => fetchCompany(id!),
        enabled: !!id,
    })

    useEffect(() => {
        if (data) {
            setCompany(data)
        }
    }, [data])

    const createMutation = useMutation({
        mutationFn: createCompany,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['companies'] })
            navigate('/companies')
        },
    })

    const updateMutation = useMutation({
        mutationFn: updateCompany,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['companies'] })
            navigate('/companies')
        },
    })

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        if (id) {
            updateMutation.mutate(company)
        } else {
            createMutation.mutate(company)
        }
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setCompany(prev => ({ ...prev, [name]: value }))
    }

    if (isFetchingCompany) return <div>Loading...</div>

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <h1 className="text-3xl font-bold mb-4">{id ? 'Edit Company' : 'Add New Company'}</h1>
            <div>
                <Label htmlFor="nome">Name</Label>
                <Input id="nome" name="nome" value={company.nome} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="email">Email</Label>
                <Input id="email" name="email" type="email" value={company.email} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="senha">Password</Label>
                <Input id="senha" name="senha" type="password" value={company.senha} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="cnpj">CNPJ</Label>
                <Input id="cnpj" name="cnpj" value={company.cnpj} onChange={handleChange} required />
            </div>
            <Button type="submit">{id ? 'Update' : 'Create'} Company</Button>
        </form>
    )
}