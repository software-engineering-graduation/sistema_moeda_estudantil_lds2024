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
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import {
    createColumnHelper,
    flexRender,
    getCoreRowModel,
    useReactTable,
} from '@tanstack/react-table'
import { useNavigate } from 'react-router-dom'

interface Aluno {
    id: number
    nome: string
    email: string
    cpf: string
    curso: string
    saldoMoedas: number
}

const buscarAlunos = async (): Promise<Aluno[]> => {
    const { data } = await api.get('/alunos')
    return data
}

const excluirAluno = async (id: number): Promise<void> => {
    await api.delete(`/alunos/${id}`)
}

const columnHelper = createColumnHelper<Aluno>()

export default function ListaAlunos() {
    const navigate = useNavigate()
    const queryClient = useQueryClient()

    const excluirMutacao = useMutation<void, Error, number>({
        mutationFn: excluirAluno,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['alunos'] })
        },
    })

    const columns = [
        columnHelper.accessor('nome', {
            cell: info => info.getValue(),
            header: () => <span>Nome</span>,
        }),
        columnHelper.accessor('email', {
            cell: info => info.getValue(),
            header: () => <span>E-mail</span>,
        }),
        columnHelper.accessor('cpf', {
            cell: info => info.getValue(),
            header: () => <span>CPF</span>,
        }),
        columnHelper.accessor('curso', {
            cell: info => info.getValue(),
            header: () => <span>Curso</span>,
        }),
        columnHelper.accessor('saldoMoedas', {
            cell: info => info.getValue(),
            header: () => <span>Saldo de Moedas</span>,
        }),
        columnHelper.accessor('id', {
            cell: info => (
                <div className="space-x-2">
                    <Button onClick={() => navigate(`/alunos/${info.getValue()}`)}>
                        Editar
                    </Button>
                    <Button 
                        variant="destructive" 
                        onClick={() => {
                            if (window.confirm('Tem certeza que deseja excluir este aluno?')) {
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

    const { data: alunos = [], isLoading, error } = useQuery<Aluno[], Error>({
        queryKey: ['alunos'],
        queryFn: buscarAlunos,
    })

    const table = useReactTable({
        data: alunos,
        columns,
        getCoreRowModel: getCoreRowModel(),
    })

    if (isLoading) return <div>Carregando...</div>
    if (error) return <div>Ocorreu um erro: {error.message}</div>

    return (
        <div>
            <h1 className="text-3xl font-bold mb-4">Alunos</h1>
            <Button onClick={() => navigate('/alunos/novo')} className="mb-4">Adicionar Novo Aluno</Button>
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