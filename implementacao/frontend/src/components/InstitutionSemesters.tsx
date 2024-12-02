// src/components/InstitutionSemesters.tsx
import { Button } from '@/components/ui/button'
import { useToast } from '@/hooks/use-toast'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useState } from 'react'
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { useNavigate, useParams } from 'react-router-dom'
import api from '@/api'
import { Spinner } from './ui/spinner'

interface Institution {
    id: number
    nome: string
    endereco: string
    semestres: Semester[]
}

interface Semester {
    id: number
    dataInicio: string
    dataFim: string
    ativo: boolean
}

const fetchInstitution = async (institutionId: string): Promise<Institution> => {
    const { data } = await api.get(`/instituicoes/${institutionId}`)
    return data
}

const startNewSemester = async (institutionId: number) => {
    await api.post(`/instituicoes/${institutionId}/novoSemestre`)
}

export default function InstitutionSemesters() {
    const { toast } = useToast()
    const queryClient = useQueryClient()
    const navigate = useNavigate()
    const { institutionId } = useParams<{ institutionId: string }>()
    const [loadingInstitutionId, setLoadingInstitutionId] = useState<number | null>(null)
    const [confirmDialog, setConfirmDialog] = useState<{ open: boolean, institutionId: number | null }>({ open: false, institutionId: null })

    const { data: institution, isLoading, error } = useQuery<Institution>({
        queryKey: ['institution', institutionId],
        queryFn: () => fetchInstitution(institutionId!),
    })

    const mutation = useMutation({
        mutationFn: startNewSemester,
        onMutate: (institutionId) => {
            setLoadingInstitutionId(institutionId)
        },
        onSettled: () => {
            setLoadingInstitutionId(null)
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['institution', institutionId] })
            toast({
                title: "Sucesso",
                description: "Novo semestre iniciado com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao iniciar novo semestre: ${error}`,
                variant: "destructive",
            })
        }
    })

    const handleStartNewSemester = (hasActiveSemester: boolean) => {
        if (hasActiveSemester) {
            setConfirmDialog({ open: true, institutionId: Number(institutionId) })
        } else {
            mutation.mutate(Number(institutionId))
        }
    }

    const confirmStartNewSemester = () => {
        if (confirmDialog.institutionId !== null) {
            mutation.mutate(confirmDialog.institutionId)
        }
        setConfirmDialog({ open: false, institutionId: null })
    }

    if (isLoading) return (
        <div className="flex justify-center items-center h-full">
            <Spinner />
        </div>
    )

    if (error) return <div>Ocorreu um erro: {error.message}</div>

    const currentSemester = institution?.semestres?.find(s => s.ativo)

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Gerenciamento de Semestres - {institution?.nome}</h1>
            <div className="mb-4">
                <p><strong>Endereço:</strong> {institution?.endereco}</p>
                {currentSemester ? (
                    <div>
                        <p><strong>Semestre Atual:</strong></p>
                        <p>Início: {new Date(currentSemester.dataInicio).toLocaleDateString()}</p>
                        <p>Fim: {new Date(currentSemester.dataFim).toLocaleDateString()}</p>
                    </div>
                ) : (
                    <p><strong>Semestre Atual:</strong> Nenhum semestre ativo</p>
                )}
            </div>
            <Button onClick={() => handleStartNewSemester(!!currentSemester)}>
                {loadingInstitutionId === Number(institutionId) ? 'Iniciando...' : 'Iniciar Novo Semestre'}
            </Button>
            <Button onClick={() => navigate(`/instituicoes/${institutionId}/professores`)} className="ml-2">
                Gerenciar Professores
            </Button>
            <Button onClick={() => navigate(`/instituicoes/${institutionId}`)} className="ml-2">
                Editar Instituição
            </Button>
            <Dialog open={confirmDialog.open} onOpenChange={(open) => setConfirmDialog({ open, institutionId: null })}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Confirmar Ação</DialogTitle>
                    </DialogHeader>
                    <div className="space-y-4">
                        <p>Já existe um semestre ativo. Tem certeza de que deseja iniciar um novo semestre?</p>
                        <p>Essa ação adicionará <strong>1000</strong> moedas a todos os professores dessa instituição.</p>
                    </div>
                    <DialogFooter className="flex justify-end space-x-2">
                        <Button onClick={() => setConfirmDialog({ open: false, institutionId: null })} variant="outline">Cancelar</Button>
                        <Button onClick={confirmStartNewSemester}>Confirmar</Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    )
}