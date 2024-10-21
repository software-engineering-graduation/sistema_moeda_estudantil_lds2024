import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useToast } from "@/hooks/use-toast"
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import axios from 'axios'

interface Vantagem {
    id?: number
    descricao: string
    foto: string
    custoMoedas: number
    quantidadeDisponivel: number
}

const buscarVantagem = async (empresaId: string, vantagemId: string): Promise<Vantagem> => {
    const { data } = await axios.get(`http://localhost:8080/empresas/${empresaId}/vantagens/${vantagemId}`)
    return data
}

const criarVantagem = async (empresaId: string, vantagem: Vantagem): Promise<Vantagem> => {
    const { data } = await axios.post(`http://localhost:8080/empresas/${empresaId}/vantagens`, vantagem)
    return data
}

const atualizarVantagem = async (empresaId: string, vantagem: Vantagem): Promise<Vantagem> => {
    const { data } = await axios.put(`http://localhost:8080/empresas/${empresaId}/vantagens/${vantagem.id}`, vantagem)
    return data
}

export default function FormularioVantagem() {
    const { toast } = useToast()
    const { id: empresaId, vantagemId } = useParams<{ id: string; vantagemId: string }>()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const [vantagem, setVantagem] = useState<Vantagem>({
        descricao: '',
        foto: '',
        custoMoedas: 0,
        quantidadeDisponivel: 0,
    })

    const [imagePreview, setImagePreview] = useState<string | null>(null)

    useEffect(() => {
        setImagePreview(vantagem.foto)
    }, [vantagem.foto])

    const { isLoading: estaCarregandoVantagem, data } = useQuery<Vantagem, Error>({
        queryKey: ['vantagem', empresaId, vantagemId],
        queryFn: () => buscarVantagem(empresaId!, vantagemId!),
        enabled: !!empresaId && !!vantagemId,
    })

    useEffect(() => {
        if (data) {
            setVantagem(data)
        }
    }, [data])

    const criarMutacao = useMutation({
        mutationFn: (novaVantagem: Vantagem) => criarVantagem(empresaId!, novaVantagem),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['vantagens', empresaId] })
            navigate(`/empresas/${empresaId}/vantagens`)
            toast({
                title: "Sucesso",
                description: "Vantagem criada com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao criar vantagem: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })

    const atualizarMutacao = useMutation({
        mutationFn: (vantagemAtualizada: Vantagem) => atualizarVantagem(empresaId!, vantagemAtualizada),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['vantagens', empresaId] })
            navigate(`/empresas/${empresaId}/vantagens`)
            toast({
                title: "Sucesso",
                description: "Vantagem atualizada com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao atualizar vantagem: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        if (vantagemId) {
            atualizarMutacao.mutate(vantagem)
        } else {
            criarMutacao.mutate(vantagem)
        }
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setVantagem(prev => ({ ...prev, [name]: name === 'custoMoedas' || name === 'quantidadeDisponivel' ? parseInt(value) : value }))

        if (name === 'foto') {
            setImagePreview(value)
        }
    }

    if (estaCarregandoVantagem) return <div>Carregando...</div>

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <h1 className="text-3xl font-bold mb-4">{vantagemId ? 'Editar Vantagem' : 'Adicionar Nova Vantagem'}</h1>
            <div>
                <Label htmlFor="descricao">Descrição</Label>
                <Input id="descricao" name="descricao" value={vantagem.descricao} onChange={handleChange} required />
            </div>
            <div className="space-y-2">
                <Label htmlFor="foto">URL da Foto</Label>
                <Input
                    id="foto"
                    name="foto"
                    value={vantagem.foto}
                    onChange={handleChange}
                    placeholder="Insira a URL da imagem"
                    required
                />
                {imagePreview && (
                    <div className="mt-2">
                        <p className="text-sm text-gray-500 mb-1">Pré-visualização da imagem:</p>
                        <img
                            src={imagePreview}
                            alt="Pré-visualização"
                            className="max-w-xs h-auto border border-gray-300 rounded"
                            onError={() => setImagePreview(null)}
                        />
                    </div>
                )}
                {!imagePreview && vantagem.foto && (
                    <p className="text-sm text-red-500">Não foi possível carregar a imagem. Verifique se a URL está correta.</p>
                )}
            </div>
            <div>
                <Label htmlFor="custoMoedas">Custo (Moedas)</Label>
                <Input id="custoMoedas" name="custoMoedas" type="number" value={vantagem.custoMoedas} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="quantidadeDisponivel">Quantidade Disponível</Label>
                <Input id="quantidadeDisponivel" name="quantidadeDisponivel" type="number" value={vantagem.quantidadeDisponivel} onChange={handleChange} required />
            </div>
            <Button type="submit">{vantagemId ? 'Atualizar' : 'Criar'} Vantagem</Button>
        </form>
    )
}