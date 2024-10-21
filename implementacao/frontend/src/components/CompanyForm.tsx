import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import axios from 'axios'
import { useToast } from "@/hooks/use-toast"

interface Empresa {
    id?: number
    nome: string
    email: string
    senha: string
    cnpj: string
}

const buscarEmpresa = async (id: string): Promise<Empresa> => {
    const { data } = await axios.get(`http://localhost:8080/empresas/${id}`)
    return data
}

const criarEmpresa = async (empresa: Empresa): Promise<Empresa> => {
    const { data } = await axios.post('http://localhost:8080/empresas', empresa)
    return data
}

const atualizarEmpresa = async (empresa: Empresa): Promise<Empresa> => {
    const { data } = await axios.put(`http://localhost:8080/empresas/${empresa.id}`, empresa)
    return data
}

export default function FormularioEmpresa() {
    const { toast } = useToast()
    const { id } = useParams<{ id: string }>()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const [empresa, setEmpresa] = useState<Empresa>({
        nome: '',
        email: '',
        senha: '',
        cnpj: '',
    })

    const { isLoading: estaCarregandoEmpresa, data } = useQuery<Empresa, Error>({
        queryKey: ['empresa', id],
        queryFn: () => buscarEmpresa(id!),
        enabled: !!id,
    })

    useEffect(() => {
        if (data) {
            setEmpresa(data)
        }
    }, [data])

    const criarMutacao = useMutation({
        mutationFn: criarEmpresa,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['empresas'] })
            navigate('/empresas')
            toast({
                title: "Sucesso",
                description: "Empresa criada com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao criar empresa: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })

    const atualizarMutacao = useMutation({
        mutationFn: atualizarEmpresa,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['empresas'] })
            navigate('/empresas')
            toast({
                title: "Sucesso",
                description: "Empresa atualizada com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao atualizar empresa: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        if (id) {
            atualizarMutacao.mutate(empresa)
        } else {
            criarMutacao.mutate(empresa)
        }
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setEmpresa(prev => ({ ...prev, [name]: value }))
    }

    if (estaCarregandoEmpresa) return <div>Carregando...</div>

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <h1 className="text-3xl font-bold mb-4">{id ? 'Editar Empresa' : 'Adicionar Nova Empresa'}</h1>
            <div>
                <Label htmlFor="nome">Nome</Label>
                <Input id="nome" name="nome" value={empresa.nome} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="email">Email</Label>
                <Input id="email" name="email" type="email" value={empresa.email} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="senha">Senha</Label>
                <Input id="senha" name="senha" type="password" value={empresa.senha} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="cnpj">CNPJ</Label>
                <Input id="cnpj" name="cnpj" value={empresa.cnpj} onChange={handleChange} required />
            </div>
            <Button type="submit">{id ? 'Atualizar' : 'Criar'} Empresa</Button>
        </form>
    )
}