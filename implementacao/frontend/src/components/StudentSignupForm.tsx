import { unsegureApi } from '@/api'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Spinner } from '@/components/ui/spinner'
import { useToast } from '@/hooks/use-toast'
import { useMutation, useQuery } from '@tanstack/react-query'
import { Controller, useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select'

interface Instituicao {
  id: number
  nome: string
}

interface StudentSignup {
  nome: string
  email: string
  senha: string
  cpf: string
  rg: string
  endereco: string
  curso: string
  instituicaoId: number
}

const createStudent = async (student: StudentSignup) => {
  const { data } = await unsegureApi.post('/alunos', student)
  return data
}

export default function StudentSignupForm() {
  const navigate = useNavigate()
  const { toast } = useToast()
  const { control, handleSubmit, formState: { errors } } = useForm<StudentSignup>({
    defaultValues: {
      nome: '',
      email: '',
      senha: '',
      cpf: '',
      rg: '',
      endereco: '',
      curso: '',
      instituicaoId: 0
    }
  })

  const { data: instituicoes = [], isLoading: isLoadingInstituicoes, isError: isErrorInstituicoes } = useQuery<Instituicao[]>({
    queryKey: ['instituicoes'],
    queryFn: async () => {
      const { data } = await unsegureApi.get('/instituicoes')
      return data
    }
  })

  const signupMutation = useMutation({
    mutationFn: createStudent,
    onSuccess: () => {
      toast({
        title: "Sucesso",
        description: "Cadastro realizado com sucesso!",
      })
      navigate('/login')
    },
    onError: (error) => {
      toast({
        title: "Erro",
        description: `Erro ao realizar cadastro: ${error.message}`,
        variant: "destructive",
      })
    }
  })

  const onSubmit = (data: StudentSignup) => {
    signupMutation.mutate(data)
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6 max-w-md mx-auto p-6 bg-white shadow-md rounded-md">
      <h1 className="text-2xl font-bold mb-6">Cadastro de Aluno</h1>

      <div>
        <Label htmlFor="nome">Nome</Label>
        <Controller
          name="nome"
          control={control}
          rules={{ required: 'Nome é obrigatório' }}
          render={({ field }) => (
            <Input {...field} id="nome" className="mt-1" />
          )}
        />
        {errors.nome && <p className="text-red-500">{errors.nome.message}</p>}
      </div>

      <div>
        <Label htmlFor="email">Email</Label>
        <Controller
          name="email"
          control={control}
          rules={{ required: 'Email é obrigatório' }}
          render={({ field }) => (
            <Input {...field} id="email" type="email" className="mt-1" />
          )}
        />
        {errors.email && <p className="text-red-500">{errors.email.message}</p>}
      </div>

      <div>
        <Label htmlFor="senha">Senha</Label>
        <Controller
          name="senha"
          control={control}
          rules={{ required: 'Senha é obrigatória' }}
          render={({ field }) => (
            <Input {...field} id="senha" type="password" className="mt-1" />
          )}
        />
        {errors.senha && <p className="text-red-500">{errors.senha.message}</p>}
      </div>

      <div>
        <Label htmlFor="cpf">CPF</Label>
        <Controller
          name="cpf"
          control={control}
          rules={{ required: 'CPF é obrigatório' }}
          render={({ field }) => (
            <Input {...field} id="cpf" className="mt-1" />
          )}
        />
        {errors.cpf && <p className="text-red-500">{errors.cpf.message}</p>}
      </div>

      <div>
        <Label htmlFor="rg">RG</Label>
        <Controller
          name="rg"
          control={control}
          rules={{ required: 'RG é obrigatório' }}
          render={({ field }) => (
            <Input {...field} id="rg" className="mt-1" />
          )}
        />
        {errors.rg && <p className="text-red-500">{errors.rg.message}</p>}
      </div>

      <div>
        <Label htmlFor="endereco">Endereço</Label>
        <Controller
          name="endereco"
          control={control}
          rules={{ required: 'Endereço é obrigatório' }}
          render={({ field }) => (
            <Input {...field} id="endereco" className="mt-1" />
          )}
        />
        {errors.endereco && <p className="text-red-500">{errors.endereco.message}</p>}
      </div>

      <div>
        <Label htmlFor="curso">Curso</Label>
        <Controller
          name="curso"
          control={control}
          rules={{ required: 'Curso é obrigatório' }}
          render={({ field }) => (
            <Input {...field} id="curso" className="mt-1" />
          )}
        />
        {errors.curso && <p className="text-red-500">{errors.curso.message}</p>}
      </div>

      <div>
        <Label htmlFor="instituicaoId">Instituição de Ensino</Label>
        {isLoadingInstituicoes && <div className="flex justify-center"><Spinner /></div>}
        {isErrorInstituicoes && <div className="text-red-500">Ocorreu um erro ao carregar as instituições</div>}
        {!isLoadingInstituicoes && !isErrorInstituicoes && (
          <Controller
            name="instituicaoId"
            control={control}
            rules={{ required: 'Instituição é obrigatória' }}
            render={({ field }) => (
              <Select
                onValueChange={field.onChange}
                value={field.value.toString()}
                className="mt-1"
              >
                <SelectTrigger>
                  <SelectValue placeholder="Selecione uma instituição" />
                </SelectTrigger>
                <SelectContent>
                  {instituicoes.map(instituicao => (
                    <SelectItem key={instituicao.id} value={instituicao.id.toString()}>
                      {instituicao.nome}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            )}
          />
        )}
        {errors.instituicaoId && <p className="text-red-500">{errors.instituicaoId.message}</p>}
      </div>

      <Button
        type="submit"
        className="w-full flex justify-center items-center"
        disabled={signupMutation.isPending || isLoadingInstituicoes || isErrorInstituicoes}
      >
        {signupMutation.isPending ? <div className='mr-2'><Spinner size={6}/></div> : 'Cadastrar'}
      </Button>
    </form>
  )
}