// src/components/FormularioFuncionarioEmpresa.tsx
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useToast } from "@/hooks/use-toast"
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import axios from 'axios'

interface Funcionario {
    id?: number
    nome: string
    email: string
    senha: string
    cargo: string
}

const buscarFuncionario = async (empresaId: string, funcionarioId: string): Promise<Funcionario> => {
    const { data } = await axios.get(`http://localhost:8080/empresas/${empresaId}/funcionarios/${funcionarioId}`)
    return data
}

const criarFuncionario = async (empresaId: string, funcionario: Funcionario): Promise<Funcionario> => {
    const { data } = await axios.post(`http://localhost:8080/empresas/${empresaId}/funcionarios`, funcionario)
    return data
}

const atualizarFuncionario = async (empresaId: string, funcionario: Funcionario): Promise<Funcionario> => {
    const { data } = await axios.put(`http://localhost:8080/empresas/${empresaId}/funcionarios/${funcionario.id}`, funcionario)
    return data
}

export default function FormularioFuncionarioEmpresa() {
    const { toast } = useToast()
    const { id: empresaId, funcionarioId } = useParams<{ id: string; funcionarioId: string }>()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const [funcionario, setFuncionario] = useState<Funcionario>({
        nome: '',
        email: '',
        senha: '',
        cargo: '',
    })

    const { isLoading: estaCarregandoFuncionario, data } = useQuery<Funcionario, Error>({
        queryKey: ['funcionario', empresaId, funcionarioId],
        queryFn: () => buscarFuncionario(empresaId!, funcionarioId!),
        enabled: !!empresaId && !!funcionarioId,
    })

    useEffect(() => {
        if (data) {
            setFuncionario(data)
        }
    }, [data])

    const criarMutacao = useMutation({
        mutationFn: (novoFuncionario: Funcionario) => criarFuncionario(empresaId!, novoFuncionario),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['funcionarios', empresaId] })
            navigate(`/empresas/${empresaId}/funcionarios`)
            toast({
                title: "Sucesso",
                description: "Funcionário criado com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao criar funcionário: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })

    const atualizarMutacao = useMutation({
        mutationFn: (funcionarioAtualizado: Funcionario) => atualizarFuncionario(empresaId!, funcionarioAtualizado),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['funcionarios', empresaId] })
            navigate(`/empresas/${empresaId}/funcionarios`)
            toast({
                title: "Sucesso",
                description: "Funcionário atualizado com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao atualizar funcionário: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        if (funcionarioId) {
            atualizarMutacao.mutate(funcionario)
        } else {
            criarMutacao.mutate(funcionario)
        }
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setFuncionario(prev => ({ ...prev, [name]: value }))
    }

    if (estaCarregandoFuncionario) return <div>Carregando...</div>

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <h1 className="text-3xl font-bold mb-4">{funcionarioId ? 'Editar Funcionário' : 'Adicionar Novo Funcionário'}</h1>
            <div>
                <Label htmlFor="nome">Nome</Label>
                <Input id="nome" name="nome" value={funcionario.nome} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="email">Email</Label>
                <Input id="email" name="email" type="email" value={funcionario.email} onChange={handleChange} required />
            </div>
            {!funcionarioId && (
                <div>
                    <Label htmlFor="senha">Senha</Label>
                    <Input id="senha" name="senha" type="password" value={funcionario.senha} onChange={handleChange} required />
                </div>
            )}
            <div>
                <Label htmlFor="cargo">Cargo</Label>
                <Input id="cargo" name="cargo" value={funcionario.cargo} onChange={handleChange} required />
            </div>
            <Button type="submit">{funcionarioId ? 'Atualizar' : 'Criar'} Funcionário</Button>
        </form>
    )
}