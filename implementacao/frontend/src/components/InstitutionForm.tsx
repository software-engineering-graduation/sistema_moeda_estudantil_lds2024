// src/components/InstitutionForm.tsx
import api from '@/api'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useToast } from "@/hooks/use-toast"
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Plus, Trash2, } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

interface Instituicao {
    id?: number
    nome: string
    endereco: string
    professores: Professor[]
}

interface Professor {
    id?: number
    nome: string
    email: string
    senha: string
    departamento: string
    saldoMoedas?: number
}

const buscarInstituicao = async (id: string): Promise<Instituicao> => {
    const { data } = await api.get(`/instituicoes/${id}`)
    return data
}

const criarInstituicao = async (instituicao: Instituicao): Promise<Instituicao> => {
    const { data } = await api.post('/instituicoes', instituicao)
    return data
}

const atualizarInstituicao = async (instituicao: Instituicao): Promise<Instituicao> => {
    const { data } = await api.put(`/instituicoes/${instituicao.id}`, instituicao)
    return data
}

export default function InstitutionForm() {
    const { toast } = useToast()
    const { id } = useParams<{ id: string }>()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const [instituicao, setInstituicao] = useState<Instituicao>({
        nome: '',
        endereco: '',
        professores: []
    })
    const { isLoading: estaCarregandoInstituicao, data } = useQuery<Instituicao, Error>({
        queryKey: ['instituicao', id],
        queryFn: () => buscarInstituicao(id!),
        enabled: !!id,
    })
    useEffect(() => {
        if (data) {
            setInstituicao(data)
        }
    }, [data])
    const criarMutacao = useMutation({
        mutationFn: criarInstituicao,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['instituicoes'] })
            navigate('/instituicoes')
            toast({
                title: "Sucesso",
                description: "Instituição criada com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao criar instituição: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })
    const atualizarMutacao = useMutation({
        mutationFn: atualizarInstituicao,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['instituicoes'] })
            navigate('/instituicoes')
            toast({
                title: "Sucesso",
                description: "Instituição atualizada com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao atualizar instituição: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        if (id) {
            atualizarMutacao.mutate(instituicao)
        } else {
            criarMutacao.mutate(instituicao)
        }
    }
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        if (name.startsWith('professores')) {
            const [_, index, field] = name.split('-')
            setInstituicao(prev => {
                const newProfessores = [...prev.professores]
                newProfessores[parseInt(index)] = { ...newProfessores[parseInt(index)], [field]: value }
                return { ...prev, professores: newProfessores }
            })
        } else {
            setInstituicao(prev => ({ ...prev, [name]: value }))
        }
    }

    const handleProfessorChange = (index: number, e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setInstituicao(prev => {
            const newProfessores = [...prev.professores]
            newProfessores[index] = { ...newProfessores[index], [name]: value }
            return { ...prev, professores: newProfessores }
        })
    }

    const adicionarProfessor = () => {
        setInstituicao(prev => ({
            ...prev,
            professores: [...prev.professores, { nome: '', email: '', senha: '', departamento: '' }]
        }))
    }

    const removerProfessor = (index: number) => {
        setInstituicao(prev => ({
            ...prev,
            professores: prev.professores.filter((_, i) => i !== index)
        }))
    }

    if (estaCarregandoInstituicao) return <div>Carregando...</div>
    return (
        <form onSubmit={handleSubmit} className="space-y-6">
            <h1 className="text-3xl font-bold mb-4">{id ? 'Editar Instituição' : 'Adicionar Nova Instituição'}</h1>

            <div className="space-y-4">
                <div>
                    <Label htmlFor="nome">Nome da Instituição</Label>
                    <Input
                        id="nome"
                        name="nome"
                        value={instituicao.nome}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div>
                    <Label htmlFor="endereco">Endereço</Label>
                    <Input
                        id="endereco"
                        name="endereco"
                        value={instituicao.endereco}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="space-y-4">
                    <div className="flex justify-between items-center">
                        <h2 className="text-xl font-semibold">Professores</h2>
                        <Button
                            type="button"
                            onClick={adicionarProfessor}
                            variant="outline"
                            size="sm"
                        >
                            <Plus className="w-4 h-4 mr-2" />
                            Adicionar Professor
                        </Button>
                    </div>

                    {instituicao.professores.map((professor, index) => (
                        <div key={index} className="space-y-4 p-4 border rounded-lg">
                            <div className="flex justify-between items-center">
                                <h3 className="text-lg font-medium">Professor {index + 1}</h3>
                                {instituicao.professores.length > 1 && (
                                    <Button
                                        type="button"
                                        onClick={() => removerProfessor(index)}
                                        variant="ghost"
                                        size="sm"
                                    >
                                        <Trash2 className="w-4 h-4" />
                                    </Button>
                                )}
                            </div>
                            <div>
                                <Label htmlFor={`professor-nome-${index}`}>Nome</Label>
                                <Input
                                    id={`professor-nome-${index}`}
                                    name="nome"
                                    value={professor.nome}
                                    onChange={(e) => handleProfessorChange(index, e)}
                                    required
                                />
                            </div>
                            <div>
                                <Label htmlFor={`professor-email-${index}`}>Email</Label>
                                <Input
                                    id={`professor-email-${index}`}
                                    name="email"
                                    type="email"
                                    value={professor.email}
                                    onChange={(e) => handleProfessorChange(index, e)}
                                    required
                                />
                            </div>
                            <div>
                                <Label htmlFor={`professor-senha-${index}`}>Senha</Label>
                                <Input
                                    id={`professor-senha-${index}`}
                                    name="senha"
                                    type="password"
                                    value={professor.senha}
                                    onChange={(e) => handleProfessorChange(index, e)}
                                    required
                                />
                            </div>
                            <div>
                                <Label htmlFor={`professor-departamento-${index}`}>Departamento</Label>
                                <Input
                                    id={`professor-departamento-${index}`}
                                    name="departamento"
                                    value={professor.departamento}
                                    onChange={(e) => handleProfessorChange(index, e)}
                                    required
                                />
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            <Button type="submit" className="w-full">
                {id ? 'Atualizar' : 'Criar'} Instituição
            </Button>
        </form>
    )
}