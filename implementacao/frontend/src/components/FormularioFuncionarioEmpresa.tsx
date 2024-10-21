import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useToast } from "@/hooks/use-toast"
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import axios from 'axios'

interface FuncionarioEmpresa {
    id?: number
    nome: string
    email: string
    senha: string
    tipo: string
}

const buscarFuncionario = async (empresaId: string, funcionarioId: string): Promise<FuncionarioEmpresa> => {
    const { data } = await axios.get(`http://localhost:8080/empresas/${empresaId}/funcionarios/${funcionarioId}`)
    return data
}

const criarFuncionario = async (empresaId: string, funcionario: FuncionarioEmpresa): Promise<FuncionarioEmpresa> => {
    const { data } = await axios.post(`http://localhost:8080/empresas/${empresaId}/funcionarios`, funcionario)
    return data
}

const atualizarFuncionario = async (empresaId: string, funcionario: FuncionarioEmpresa): Promise<FuncionarioEmpresa> => {
    const { data } = await axios.put(`http://localhost:8080/empresas/${empresaId}/funcionarios/${funcionario.id}`, funcionario)
    return data
}

const atualizarSenha = async (empresaId: string, funcionarioId: string, senhaAntiga: string, novaSenha: string): Promise<void> => {
    await axios.put(`http://localhost:8080/empresas/${empresaId}/funcionarios/${funcionarioId}/senha`, { senhaAntiga, novaSenha })
}

export default function FormularioFuncionarioEmpresa() {
    const { toast } = useToast()
    const { id: empresaId, funcionarioId } = useParams<{ id: string; funcionarioId: string }>()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const [funcionario, setFuncionario] = useState<FuncionarioEmpresa>({
        nome: '',
        email: '',
        senha: '',
        tipo: 'FUNCIONARIO_EMPRESA',
    })
    const [senhaAntiga, setSenhaAntiga] = useState('')
    const [novaSenha, setNovaSenha] = useState('')

    const { isLoading: estaCarregandoFuncionario, data } = useQuery<FuncionarioEmpresa, Error>({
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
        mutationFn: (novoFuncionario: FuncionarioEmpresa) => criarFuncionario(empresaId!, novoFuncionario),
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
        mutationFn: (funcionarioAtualizado: FuncionarioEmpresa) => atualizarFuncionario(empresaId!, funcionarioAtualizado),
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

    const atualizarSenhaMutacao = useMutation({
        mutationFn: () => atualizarSenha(empresaId!, funcionarioId!, senhaAntiga, novaSenha),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['funcionarios', empresaId] })
            toast({
                title: "Sucesso",
                description: "Senha atualizada com sucesso!",
            })
            setSenhaAntiga('')
            setNovaSenha('')
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao atualizar senha: ${(error as Error).message}`,
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

    const handleAtualizarSenha = (e: React.FormEvent) => {
        e.preventDefault()
        atualizarSenhaMutacao.mutate()
    }

    if (estaCarregandoFuncionario) return <div>Carregando...</div>

    return (
        <div className="space-y-8">
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
                <Button type="submit">{funcionarioId ? 'Atualizar' : 'Criar'} Funcionário</Button>
            </form>

            {funcionarioId && (
                <form onSubmit={handleAtualizarSenha} className="space-y-4">
                    <h2 className="text-2xl font-bold mb-4">Atualizar Senha</h2>
                    <div>
                        <Label htmlFor="senhaAntiga">Senha Antiga</Label>
                        <Input id="senhaAntiga" name="senhaAntiga" type="password" value={senhaAntiga} onChange={(e) => setSenhaAntiga(e.target.value)} required />
                    </div>
                    <div>
                        <Label htmlFor="novaSenha">Nova Senha</Label>
                        <Input id="novaSenha" name="novaSenha" type="password" value={novaSenha} onChange={(e) => setNovaSenha(e.target.value)} required />
                    </div>
                    <Button type="submit">Atualizar Senha</Button>
                </form>
            )}
        </div>
    )
}