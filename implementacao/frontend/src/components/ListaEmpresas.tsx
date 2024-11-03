import api from '@/api'
import { Button } from '@/components/ui/button'
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from '@/components/ui/table'
import { useToast } from "@/hooks/use-toast"
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import {
    createColumnHelper,
    flexRender,
    getCoreRowModel,
    useReactTable,
} from '@tanstack/react-table'
import { useNavigate } from 'react-router-dom'

interface Empresa {
    id: number
    nome: string
    cnpj: string
}

const buscarEmpresas = async (): Promise<Empresa[]> => {
    const { data } = await api.get('/empresas')
    return data
}

const excluirEmpresa = async (id: number): Promise<void> => {
    await api.delete(`/empresas/${id}`)
}

const columnHelper = createColumnHelper<Empresa>()

export default function ListaEmpresas() {
    const { toast } = useToast()
    const navigate = useNavigate()
    const queryClient = useQueryClient()

    const excluirMutacao = useMutation({
        mutationFn: excluirEmpresa,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['empresas'] })
            toast({
                title: "Sucesso",
                description: "Empresa excluída com sucesso!",
            })
        },
        onError: (error) => {
            toast({
                title: "Erro",
                description: `Erro ao excluir empresa: ${(error as Error).message}`,
                variant: "destructive",
            })
        },
    })

    const columns = [
        columnHelper.accessor('nome', {
            cell: info => info.getValue(),
            header: () => <span>Nome</span>,
        }),
        columnHelper.accessor('cnpj', {
            cell: info => info.getValue(),
            header: () => <span>CNPJ</span>,
        }),
        columnHelper.accessor('id', {
            cell: info => (
                <div className="space-x-2">
                    <Button onClick={() => navigate(`/empresas/${info.getValue()}`)}>
                        Editar
                    </Button>
                    <Button onClick={() => navigate(`/empresas/${info.getValue()}/funcionarios`)}>
                        Gerenciar Funcionários
                    </Button>
                    <Button onClick={() => navigate(`/empresas/${info.getValue()}/vantagens`)}>
                        Gerenciar Vantagens
                    </Button>
                    <Button
                        variant="destructive"
                        onClick={() => {
                            if (window.confirm('Tem certeza que deseja excluir esta empresa?')) {
                                excluirMutacao.mutate(info.getValue())
                            }
                        }}
                    >
                        Excluir
                    </Button>
                </div>
            ),
            header: () => <span>Ações</span>,
        }),
    ]

    const { data: empresas = [], isLoading, error } = useQuery<Empresa[], Error>({
        queryKey: ['empresas'],
        queryFn: buscarEmpresas,
    })

    const table = useReactTable({
        data: empresas,
        columns,
        getCoreRowModel: getCoreRowModel(),
    })

    if (isLoading) return <div>Carregando...</div>
    if (error) return <div>Ocorreu um erro: {error.message}</div>

    return (
        <div>
            <h1 className="text-3xl font-bold mb-4">Empresas</h1>
            <Button onClick={() => navigate('/empresas/nova')} className="mb-4">Adicionar Nova Empresa</Button>
            <Table>
                <TableHeader>
                    {table.getHeaderGroups().map(headerGroup => (
                        <TableRow key={headerGroup.id}>
                            {headerGroup.headers.map(header => (
                                <TableHead key={header.id}>
                                    {header.isPlaceholder
                                        ? null
                                        : flexRender(
                                            header.column.columnDef.header,
                                            header.getContext()
                                        )}
                                </TableHead>
                            ))}
                        </TableRow>
                    ))}
                </TableHeader>
                <TableBody>
                    {table.getRowModel().rows.map(row => (
                        <TableRow key={row.id}>
                            {row.getVisibleCells().map(cell => (
                                <TableCell key={cell.id}>
                                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                </TableCell>
                            ))}
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    )
}