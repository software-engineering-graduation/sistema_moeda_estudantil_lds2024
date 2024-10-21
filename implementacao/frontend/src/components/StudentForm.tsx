import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useToast } from '@/hooks/use-toast'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import axios from 'axios'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select'

interface Aluno {
    id?: number
    nome: string
    email: string
    senha: string
    cpf: string
    rg: string
    endereco: string
    curso: string
    instituicao: Instituicao | null
}

interface Instituicao {
    id: number
    nome: string
}

const buscarAluno = async (id: string): Promise<Aluno> => {
    const { data } = await axios.get(`http://localhost:8080/alunos/${id}`)
    return data
}

const criarAluno = async (aluno: Aluno): Promise<Aluno> => {
    const { data } = await axios.post('http://localhost:8080/alunos', aluno)
    return data
}

const atualizarAluno = async (aluno: Aluno): Promise<Aluno> => {
    const { data } = await axios.put(`http://localhost:8080/alunos/${aluno.id}`, aluno)
    return data
}

const atualizarSenha = async ({ id, senhaAntiga, novaSenha }: { id: number, senhaAntiga: string, novaSenha: string }): Promise<void> => {
    await axios.put(`http://localhost:8080/alunos/${id}/senha`, { senhaAntiga, novaSenha })
}

const buscarInstituicoes = async (): Promise<Instituicao[]> => {
    const { data } = await axios.get('http://localhost:8080/instituicoes')
    return data
}

export default function FormularioAluno() {
    const { toast } = useToast()
    const { id } = useParams<{ id: string }>()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const [aluno, setAluno] = useState<Aluno>({
        nome: '',
        email: '',
        senha: '',
        cpf: '',
        rg: '',
        endereco: '',
        curso: '',
        instituicao: null,
    })
    const [instituicoes, setInstituicoes] = useState<Instituicao[]>([])
    const [senhaAntiga, setSenhaAntiga] = useState('')
    const [novaSenha, setNovaSenha] = useState('')

    const { data, isLoading: estaCarregandoAluno } = useQuery<Aluno, Error>({
        queryKey: ['aluno', id],
        queryFn: () => buscarAluno(id!),
        enabled: !!id,
    })

    const { data: instituicoesData } = useQuery<Instituicao[], Error>({
        queryKey: ['instituicoes'],
        queryFn: buscarInstituicoes,
    })

    useEffect(() => {
        if (instituicoesData) {
            setInstituicoes(instituicoesData)
        }
    }, [instituicoesData])

    useEffect(() => {
        if (data) {
            setAluno(data)
        }
    }, [data])

    const criarMutacao = useMutation<Aluno, Error, Aluno>({
        mutationFn: criarAluno,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['alunos'] })
            navigate('/alunos')
            toast({
                title: "Sucesso",
                description: "Aluno criado com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao criar aluno: ${error.message}`,
                variant: "destructive",
            })
        },
    })

    const atualizarMutacao = useMutation<Aluno, Error, Aluno>({
        mutationFn: atualizarAluno,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['alunos'] })
            navigate('/alunos')
            toast({
                title: "Sucesso",
                description: "Aluno atualizado com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao atualizar aluno: ${error.message}`,
                variant: "destructive",
            })
        },
    })

    const atualizarSenhaMutacao = useMutation<void, Error, { id: number, senhaAntiga: string, novaSenha: string }>({
        mutationFn: atualizarSenha,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['students'] })
            navigate('/alunos')
            toast({
                title: "Sucesso",
                description: "Senha atualizada com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao atualizar senha: ${error.message}`,
                variant: "destructive",
            })
        }
    })

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        if (id) {
            atualizarMutacao.mutate(aluno)
        } else {
            criarMutacao.mutate(aluno)
        }
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement> | string, selectName?: string) => {
        if (typeof e === 'string' && selectName) {
            setAluno(prev => ({ ...prev, [selectName]: e }))
        } else if (typeof e !== 'string') {
            const { name, value } = e.target
            setAluno(prev => ({ ...prev, [name]: value }))
        }
    }

    const handleAtualizarSenha = (e: React.FormEvent) => {
        e.preventDefault()
        if (id) {
            atualizarSenhaMutacao.mutate({ id: Number(id), senhaAntiga, novaSenha })
        }
    }

    if (estaCarregandoAluno) return <div>Carregando...</div>

    return (
        <div className="space-y-8">
            <form onSubmit={handleSubmit} className="space-y-4">
                <h1 className="text-3xl font-bold mb-4">{id ? 'Editar Aluno' : 'Adicionar Novo Aluno'}</h1>
                <div>
                    <Label htmlFor="nome">Nome</Label>
                    <Input id="nome" name="nome" value={aluno.nome} onChange={handleChange} required />
                </div>
                <div>
                    <Label htmlFor="email">Email</Label>
                    <Input id="email" name="email" type="email" value={aluno.email} onChange={handleChange} required />
                </div>
                {!id && (
                    <div>
                        <Label htmlFor="senha">Senha</Label>
                        <Input id="senha" name="senha" type="password" value={aluno.senha} onChange={handleChange} required />
                    </div>
                )}
                <div>
                    <Label htmlFor="cpf">CPF</Label>
                    <Input id="cpf" name="cpf" value={aluno.cpf} onChange={handleChange} required />
                </div>
                <div>
                    <Label htmlFor="rg">RG</Label>
                    <Input id="rg" name="rg" value={aluno.rg} onChange={handleChange} />
                </div>
                <div>
                    <Label htmlFor="endereco">Endereço</Label>
                    <Input id="endereco" name="endereco" value={aluno.endereco} onChange={handleChange} />
                </div>
                <div>
                    <Label htmlFor="curso">Curso</Label>
                    <Input id="curso" name="curso" value={aluno.curso} onChange={handleChange} />
                </div>
                <div>
                    <Label htmlFor="instituicaoId">Instituição</Label>
                    <Select
                        onValueChange={(value) => handleChange(value, 'instituicaoId')}
                        value={aluno.instituicao?.id.toString()}
                    >
                        <SelectTrigger className="w-full">
                            <SelectValue placeholder="Selecione uma instituição" />
                        </SelectTrigger>
                        <SelectContent>
                            {instituicoes.map(instituicao => (
                                <SelectItem key={instituicao.id} value={instituicao.id.toString()}>
                                    {instituicao.nome}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>
                <Button type="submit">{id ? 'Atualizar' : 'Criar'} Aluno</Button>
            </form>

            {id && (
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