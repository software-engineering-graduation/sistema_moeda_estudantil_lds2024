// ListaTransacoes.tsx
import { Button } from '@/components/ui/button'
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from '@/components/ui/table'
import { useAuth } from '@/contexts/AuthContext'
import { Transacao, useTransacoes } from '@/hooks/useTransacoes'
import {
    createColumnHelper,
    flexRender,
    getCoreRowModel,
    useReactTable,
} from '@tanstack/react-table'
import { useNavigate } from 'react-router-dom'

const columnHelper = createColumnHelper<Transacao>()

export default function ListaTransacoes() {
    const navigate = useNavigate()
    const { user } = useAuth()
    const { data: transacoes = [], isLoading } = useTransacoes()

    const columns = [
        columnHelper.accessor('data', {
            cell: info => new Date(info.getValue()).toLocaleDateString(),
            header: () => <span>Data</span>,
        }),
        columnHelper.accessor('origem.nome', {
            cell: info => info.getValue(),
            header: () => <span>Origem</span>,
        }),
        columnHelper.accessor('destino.nome', {
            cell: info => info.getValue(),
            header: () => <span>Destino</span>,
        }),
        columnHelper.accessor('valor', {
            cell: info => info.getValue().toFixed(2),
            header: () => <span>Valor</span>,
        }),
        columnHelper.accessor('mensagem', {
            cell: info => info.getValue(),
            header: () => <span>Mensagem</span>,
        }),
    ]

    const filteredTransacoes = transacoes.filter(transacao => {
        if (user?.tipo === 'ADMIN') return true
        return transacao.origem.id === user?.id || transacao.destino.id === user?.id
    })

    const table = useReactTable({
        data: filteredTransacoes,
        columns,
        getCoreRowModel: getCoreRowModel(),
    })

    if (isLoading) return <div>Carregando...</div>

    return (
        <div>
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">Transações</h1>
                <div className="space-x-4">
                    {user?.tipo === 'PROFESSOR' && (
                        <Button onClick={() => navigate('/transacoes/nova')}>
                            Nova Transferência
                        </Button>
                    )}
                </div>
            </div>

            {user?.tipo !== 'ADMIN' && (
                <div className="mb-4 p-4 bg-secondary rounded-lg">
                    <h2 className="text-xl font-semibold mb-2">Saldo Atual</h2>
                    <p className="text-2xl">{user?.saldoMoedas?.toFixed(2)} moedas</p>
                </div>
            )}

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