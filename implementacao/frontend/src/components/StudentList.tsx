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

interface Student {
    id: number
    nome: string
    email: string
    cpf: string
    curso: string
    saldoMoedas: number
}

const fetchStudents = async (): Promise<Student[]> => {
    const response = await fetch('http://localhost:8080/alunos')
    if (!response.ok) {
        throw new Error('Network response was not ok')
    }
    return response.json()
}

const columnHelper = createColumnHelper<Student>()

export default function StudentList() {
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
        columnHelper.accessor('cpf', {
            cell: info => info.getValue(),
            header: () => <span>CPF</span>,
        }),
        columnHelper.accessor('curso', {
            cell: info => info.getValue(),
            header: () => <span>Course</span>,
        }),
        columnHelper.accessor('saldoMoedas', {
            cell: info => info.getValue(),
            header: () => <span>Balance</span>,
        }),
        columnHelper.accessor('id', {
            cell: info => (
                <Button onClick={() => navigate(`/students/${info.getValue()}`)}>
                    Edit
                </Button>
            ),
            header: () => <span>Actions</span>,
        }),
    ]

    const { data: students = [], isLoading, error } = useQuery<Student[], Error>({
        queryKey: ['students'],
        queryFn: fetchStudents,
    })

    const table = useReactTable({
        data: students,
        columns,
        getCoreRowModel: getCoreRowModel(),
    })

    if (isLoading) return <div>Loading...</div>
    if (error) return <div>An error occurred: {error.message}</div>

    return (
        <div>
            <h1 className="text-3xl font-bold mb-4">Students</h1>
            <Button onClick={() => navigate('/students/new')} className="mb-4">Add New Student</Button>
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