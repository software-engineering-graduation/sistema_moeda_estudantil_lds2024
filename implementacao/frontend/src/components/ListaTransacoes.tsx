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
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { useAuth } from '@/contexts/AuthContext'
import { Transacao, useTransacoes } from '@/hooks/useTransacoes'
import {
    createColumnHelper,
    flexRender,
    getCoreRowModel,
    useReactTable,
} from '@tanstack/react-table'
import { useNavigate } from 'react-router-dom'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import api from '@/api'

const transacaoColumnHelper = createColumnHelper<Transacao>()
const cupomColumnHelper = createColumnHelper<CupomResgate>()

interface CupomResgate {
    id: number
    codigo: string
    valor: number
    data: string
    vantagem: {
        descricao: string
    }
    empresa: {
        nome: string
    }
    aluno: {
        nome: string
    }
}

export default function ListaTransacoes() {
    const navigate = useNavigate()
    const { user } = useAuth()
    const { data: transacoes = [], isLoading: isLoadingTransacoes } = useTransacoes()
    const queryClient = useQueryClient()

    const { data: cupons = [], isLoading: isLoadingCupons } = useQuery<CupomResgate[]>({
        queryKey: ['cupons-resgate', user?.id],
        queryFn: async () => {
            const response = await api.get(`/vantagens/byUser/${user?.id}`)
            return response.data
        },
        enabled: user?.tipo === 'ALUNO' || user?.tipo === 'ADMIN'
    })

    const transacaoColumns = [
        transacaoColumnHelper.accessor('data', {
            cell: info => new Date(info.getValue()).toLocaleDateString(),
            header: () => <span>Data</span>,
        }),
        transacaoColumnHelper.accessor('origem.nome', {
            cell: info => info.getValue(),
            header: () => <span>Origem</span>,
        }),
        transacaoColumnHelper.accessor('destino.nome', {
            cell: info => info.getValue(),
            header: () => <span>Destino</span>,
        }),
        transacaoColumnHelper.accessor('valor', {
            cell: info => info.getValue().toFixed(2),
            header: () => <span>Valor</span>,
        }),
        transacaoColumnHelper.accessor('mensagem', {
            cell: info => info.getValue(),
            header: () => <span>Mensagem</span>,
        }),
    ]

    const cupomColumns = [
        cupomColumnHelper.accessor('data', {
            cell: info => new Date(info.getValue()).toLocaleDateString(),
            header: () => <span>Data</span>,
        }),
        ...(user?.tipo === 'ADMIN' ? [
            cupomColumnHelper.accessor('aluno.nome', {
                cell: info => info.getValue(),
                header: () => <span>Aluno</span>,
            })
        ] : []),
        cupomColumnHelper.accessor('vantagem.descricao', {
            cell: info => info.getValue(),
            header: () => <span>Vantagem</span>,
        }),
        cupomColumnHelper.accessor('empresa.nome', {
            cell: info => info.getValue(),
            header: () => <span>Empresa</span>,
        }),
        cupomColumnHelper.accessor('valor', {
            cell: info => info.getValue() ? info.getValue().toFixed(2) : "",
            header: () => <span>Valor</span>,
        }),
        cupomColumnHelper.accessor('codigo', {
            cell: info => info.getValue(),
            header: () => <span>Código do Cupom</span>,
        }),
    ]

    const filteredTransacoes = transacoes.filter(transacao => {
        if (user?.tipo === 'ADMIN') return true
        return transacao.origem.id === user?.id || transacao.destino.id === user?.id
    })

    const transacoesTable = useReactTable({
        data: filteredTransacoes,
        columns: transacaoColumns,
        getCoreRowModel: getCoreRowModel(),
    })

    const cuponsTable = useReactTable({
        data: cupons,
        columns: cupomColumns,
        getCoreRowModel: getCoreRowModel(),
    })

    if (isLoadingTransacoes || isLoadingCupons) return <div>Carregando...</div>

    return (
        <div>
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-bold">{user?.tipo === 'ALUNO' ? 'Transferências Recebidas' : user?.tipo === 'PROFESSOR' ? 'Transferências Realizadas' : 'Todas as Transferências'}</h1>
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

            <Tabs defaultValue="transacoes" className="w-full">
                <TabsList className="grid w-full grid-cols-2">
                    <TabsTrigger
                        value="transacoes"
                        onClick={() => {
                            queryClient.invalidateQueries({ queryKey: ['transacoes'] })
                            queryClient.invalidateQueries({ queryKey: ['cupons-resgate'] })
                        }}
                    >
                        {user?.tipo === 'ALUNO' ? 'Transferências Recebidas' : user?.tipo === 'PROFESSOR' ? 'Transferências Realizadas' : 'Todas as Transferências'}
                    </TabsTrigger>
                    {(user?.tipo === 'ALUNO' || user?.tipo === 'ADMIN') && (
                        <TabsTrigger
                            value="cupons"
                            onClick={() => {
                                queryClient.invalidateQueries({ queryKey: ['transacoes'] })
                                queryClient.invalidateQueries({ queryKey: ['cupons-resgate'] })
                            }}
                        >
                            {user?.tipo === 'ALUNO' ? 'Cupons Resgatados' : 'Todas os Resgates'}
                        </TabsTrigger>
                    )}
                </TabsList>

                <TabsContent value="transacoes">
                    <Table>
                        <TableHeader>
                            {transacoesTable.getHeaderGroups().map(headerGroup => (
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
                            {transacoesTable.getRowModel().rows.map(row => (
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
                </TabsContent>

                <TabsContent value="cupons">
                    <Table>
                        <TableHeader>
                            {cuponsTable.getHeaderGroups().map(headerGroup => (
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
                            {cuponsTable.getRowModel().rows.map(row => (
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
                </TabsContent>
            </Tabs>
        </div>
    )
}