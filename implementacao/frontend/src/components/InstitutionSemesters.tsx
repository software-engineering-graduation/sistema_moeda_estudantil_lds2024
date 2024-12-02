// src/components/InstitutionSemesters.tsx
import api from '@/api'
import { Button } from '@/components/ui/button'
import { Spinner } from '@/components/ui/spinner'
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from '@/components/ui/table'
import { useToast } from '@/hooks/use-toast'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useState } from 'react'
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog'

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

const startNewSemester = async (institutionId: number) => {
    await api.post(`/instituicoes/${institutionId}/novoSemestre`)
}

export default function InstitutionSemesters() {
    const { toast } = useToast()
    const queryClient = useQueryClient()
    const [loadingInstitutionId, setLoadingInstitutionId] = useState<number | null>(null)
    const [confirmDialog, setConfirmDialog] = useState<{ open: boolean, institutionId: number | null }>({ open: false, institutionId: null })

    const handleStartNewSemester = (institutionId: number, hasActiveSemester: boolean) => {
        if (hasActiveSemester) {
            setConfirmDialog({ open: true, institutionId })
        } else {
            mutation.mutate(institutionId)
        }
    }

    const confirmStartNewSemester = () => {
        if (confirmDialog.institutionId !== null) {
            mutation.mutate(confirmDialog.institutionId)
        }
        setConfirmDialog({ open: false, institutionId: null })
    }

    const { data: institutions = [], isLoading } = useQuery<Institution[]>({
        queryKey: ['institutions'],
        queryFn: async () => {
            const { data } = await api.get('/instituicoes')
            return data
        }
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
            queryClient.invalidateQueries({ queryKey: ['institutions'] })
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

    if (isLoading) return (
        <div className="flex justify-center items-center h-full">
            <Spinner />
        </div>
    )

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Gerenciamento de Semestres</h1>
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead>Instituição</TableHead>
                        <TableHead>Endereço</TableHead>
                        <TableHead>Semestre Atual</TableHead>
                        <TableHead>Ações</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {institutions.map((institution) => {
                        const currentSemester = institution.semestres?.find(s => s.ativo)
                        return (
                            <TableRow key={institution.id}>
                                <TableCell>{institution.nome}</TableCell>
                                <TableCell>{institution.endereco}</TableCell>
                                <TableCell>
                                    {currentSemester ? (
                                        <div>
                                            <p>Início: {new Date(currentSemester.dataInicio).toLocaleDateString()}</p>
                                            <p>Fim: {new Date(currentSemester.dataFim).toLocaleDateString()}</p>
                                        </div>
                                    ) : (
                                        <span>Nenhum semestre ativo</span>
                                    )}
                                </TableCell>
                                <TableCell>
                                    <Button
                                        onClick={() => handleStartNewSemester(institution.id, !!currentSemester)}
                                        disabled={loadingInstitutionId === institution.id}
                                    >
                                        {loadingInstitutionId === institution.id ? 'Iniciando...' : 'Iniciar Novo Semestre'}
                                    </Button>
                                </TableCell>
                            </TableRow>
                        )
                    })}
                </TableBody>
            </Table>
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
                        <Button onClick={confirmStartNewSemester} variant="destructive">Confirmar</Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    )
}