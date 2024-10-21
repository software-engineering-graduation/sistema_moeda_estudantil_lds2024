import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'

interface Student {
    id?: number
    nome: string
    email: string
    senha: string
    cpf: string
    rg: string
    endereco: string
    curso: string
    instituicaoId: number
}

const fetchStudent = async (id: string): Promise<Student> => {
    const response = await fetch(`http://localhost:8080/alunos/${id}`)
    if (!response.ok) {
        throw new Error('Network response was not ok')
    }
    return response.json()
}

const createStudent = async (student: Student): Promise<Student> => {
    const response = await fetch('http://localhost:8080/alunos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(student),
    })
    if (!response.ok) {
        throw new Error('Network response was not ok')
    }
    return response.json()
}

const updateStudent = async (student: Student): Promise<Student> => {
    const response = await fetch(`http://localhost:8080/alunos/${student.id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(student),
    })
    if (!response.ok) {
        throw new Error('Network response was not ok')
    }
    return response.json()
}

export default function StudentForm() {
    const { id } = useParams<{ id: string }>()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const [student, setStudent] = useState<Student>({
        nome: '',
        email: '',
        senha: '',
        cpf: '',
        rg: '',
        endereco: '',
        curso: '',
        instituicaoId: 0,
    })

    const { data, isLoading: isFetchingStudent } = useQuery<Student, Error>({
        queryKey: ['student', id],
        queryFn: () => fetchStudent(id!),
        enabled: !!id,
    })

    useEffect(() => {
        if (data) {
            setStudent(data)
        }
    }, [data])

    const createMutation = useMutation<Student, Error, Student>({
        mutationFn: createStudent,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['students'] })
            navigate('/students')
        },
    })

    const updateMutation = useMutation<Student, Error, Student>({
        mutationFn: updateStudent,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['students'] })
            navigate('/students')
        },
    })

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        if (id) {
            updateMutation.mutate(student)
        } else {
            createMutation.mutate(student)
        }
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setStudent(prev => ({ ...prev, [name]: value }))
    }

    if (isFetchingStudent) return <div>Loading...</div>

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <h1 className="text-3xl font-bold mb-4">{id ? 'Edit Student' : 'Add New Student'}</h1>
            <div>
                <Label htmlFor="nome">Name</Label>
                <Input id="nome" name="nome" value={student.nome} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="email">Email</Label>
                <Input id="email" name="email" type="email" value={student.email} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="senha">Password</Label>
                <Input id="senha" name="senha" type="password" value={student.senha} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="cpf">CPF</Label>
                <Input id="cpf" name="cpf" value={student.cpf} onChange={handleChange} required />
            </div>
            <div>
                <Label htmlFor="rg">RG</Label>
                <Input id="rg" name="rg" value={student.rg} onChange={handleChange} />
            </div>
            <div>
                <Label htmlFor="endereco">Address</Label>
                <Input id="endereco" name="endereco" value={student.endereco} onChange={handleChange} />
            </div>
            <div>
                <Label htmlFor="curso">Course</Label>
                <Input id="curso" name="curso" value={student.curso} onChange={handleChange} />
            </div>
            <div>
                <Label htmlFor="instituicaoId">Institution ID</Label>
                <Input id="instituicaoId" name="instituicaoId" type="number" value={student.instituicaoId} onChange={handleChange} required />
            </div>
            <Button type="submit">{id ? 'Update' : 'Create'} Student</Button>
        </form>
    )
}