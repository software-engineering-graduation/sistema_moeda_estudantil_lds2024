import { useQuery } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import {
    createColumnHelper,
    flexRender,
    getCoreRowModel,
    useReactTable,
} from '@tanstack/react-table'
import { Button } from '@/components/ui/button'
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from '@/components/ui/table'

interface Company {
    id: number
    nome: string
    email: string
    cnpj: string
    saldoMoedas: number
}

const fetchCompanies = async (): Promise<Company[]> => {
    const response = await fetch('http://localhost:8080/empresas')
    if (!response.ok) {
        throw new Error('Network response was not ok')
    }
    return response.json()
}

const columnHelper = createColumnHelper<Company>()

export default function CompanyList() {
    const navigate = useNavigate()

    const columns = [
        columnHelper.accessor('nome', {
            cell: info => info.getValue(),
            header: () => <span>Name</span>,
        }),
        columnHelper.accessor('email', {
            cell: info => info.getValue(),
            header: () => <span>Email</span>,
        }),
        columnHelper.accessor('cnpj', {
            cell: info => info.getValue(),
            header: () => <span>CNPJ</span>,
        }),
        columnHelper.accessor('saldoMoedas', {
            cell: info => info.getValue(),
            header: () => <span>Balance</span>,
        }),
        columnHelper.accessor('id', {
            cell: info => (
                <Button onClick={() => navigate(`/companies/${info.getValue()}`)}>
                    Edit
                </Button>
            ),
            header: () => <span>Actions</span>,
        }),
    ]

    const { data: companies = [], isLoading, error } = useQuery<Company[]>({
        queryKey: ['companies'],
        queryFn: fetchCompanies,
    })

    const table = useReactTable({
        data: companies,
        columns,
        getCoreRowModel: getCoreRowModel(),
    })

    if (isLoading) return <div>Loading...</div>
    if (error) return <div>An error occurred: {(error as Error).message}</div>

    return (
        <div>
            <h1 className="text-3xl font-bold mb-4">Companies</h1>
            <Button onClick={() => navigate('/companies/new')} className="mb-4">Add New Company</Button>
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