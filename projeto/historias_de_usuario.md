
# Histórias de usuário

---

## Alunos

1. **Cadastro de Aluno**
   - **História de Usuário**: Como aluno, desejo ingressar no sistema de mérito, informando no meu cadastro meu nome, email, CPF, RG, endereço, curso e selecionando instituição de ensino que deve estar pré-cadstrada.
   - **Critérios de Aceitação**:
     - O formulário de cadastro deve exigir todos os campos mencionados.
     - A instituição de ensino deve ser selecionada a partir de uma lista pré-cadastrada.

2. **Notificação de Recebimento de Moedas**
   - **História de Usuário**: Como aluno, quero ser notificado por email quando receber moedas de um professor, para estar ciente dos meus reconhecimentos.
   - **Critérios de Aceitação**:
     - Receber um email após o professor enviar as moedas.
     - O email deve incluir a quantidade de moedas e o motivo do reconhecimento.

3. **Consulta de Extrato**
   - **História de Usuário**: Como aluno, quero consultar o extrato da minha conta, visualizando meu saldo total de moedas e as transações realizadas, para acompanhar meus recebimentos e trocas de moedas.
   - **Critérios de Aceitação**:
     - O extrato deve mostrar todas as moedas recebidas e trocadas.
     - Deve exibir data, quantidade e o tipo de transação.

4. **Troca de Moedas por Vantagens**
   - **História de Usuário**: Como aluno, quero trocar minhas moedas por vantagens cadastradas no sistema, como descontos ou compra de materiais, para usufruir dos benefícios oferecidos.
   - **Critérios de Aceitação**:
     - Poder visualizar uma lista de vantagens com descrições, e custos em moedas.
     - Somente conseguir trocar se tiver saldo suficiente.

5. **Desconto de Saldo ao Resgatar Vantagem**
   - **História de Usuário**: Como aluno, ao resgatar uma vantagem, quero que o valor seja descontado do meu saldo, para refletir corretamente minhas moedas restantes.
   - **Critérios de Aceitação**:
     - O sistema deve atualizar meu saldo imediatamente após a troca.
     - A transação deve aparecer no meu extrato.

6. **Recebimento de Cupom por Email**
   - **História de Usuário**: Como aluno, ao resgatar uma vantagem, quero receber um email com um cupom e código gerado pelo sistema, para utilizar na troca presencial.
   - **Critérios de Aceitação**:
     - Receber o email de cupom após a troca.
     - O email deve incluir detalhes da vantagem e o código alfanumérico único.

7. **Acesso Seguro ao Sistema**
   - **História de Usuário**: Como aluno, quero acessar o sistema usando login e senha, para garantir a segurança das minhas informações.
   - **Critérios de Aceitação**:
     - O sistema deve solicitar autenticação ao tentar fazer login.

---

## Professores

1. **Conta Pré-Cadastrada**
   - **História de Usuário**: Como professor, quero acessar minha conta pré-cadastrada com meu nome, CPF e departamento, para começar a utilizar o sistema sem necessidade de cadastro.
   - **Critérios de Aceitação**:
     - Receber informações de acesso da instituição parceira.

2. **Visualização de Vinculação à Instituição**
   - **História de Usuário**: Como professor, quero que meu perfil mostre claramente minha vinculação à instituição, para confirmar minha afiliação.
   - **Critérios de Aceitação**:
     - A instituição e departamento devem estar visíveis no perfil.

3. **Recebimento de Moedas Semestrais**
   - **História de Usuário**: Como professor, quero receber 1.000 moedas a cada semestre, acumulando as não utilizadas, para distribuir aos alunos como reconhecimento.
   - **Critérios de Aceitação**:
     - As moedas devem ser creditadas automaticamente no início de cada semestre.
     - O saldo deve refletir o acúmulo de semestres anteriores.

4. **Envio de Moedas para Alunos**
   - **História de Usuário**: Como professor, quero enviar moedas para um aluno específico, indicando o motivo por mensagem aberta, para reconhecer seu desempenho.
   - **Critérios de Aceitação**:
     - Poder selecionar o aluno a partir de uma busca.
     - Campo obrigatório para inserir mensagem com o motivo.
     - O sistema deve verificar se há saldo suficiente antes de confirmar o envio.

5. **Consulta de Extrato**
   - **História de Usuário**: Como professor, quero consultar meu extrato de moedas, para acompanhar minhas distribuições.
   - **Critérios de Aceitação**:
     - O extrato deve mostrar datas, alunos beneficiados, quantidades, motivos e tipo de transação.

6. **Acesso Seguro ao Sistema**
   - **História de Usuário**: Como professor, quero acessar o sistema usando login e senha, para garantir a segurança das minhas informações.
   - **Critérios de Aceitação**:
     - O sistema deve exigir autenticação no acesso.

---

## Para Empresas Parceiras

1. **Cadastro de Empresa Parceira**
   - **História de Usuário**: Como empresa interessada em parceria, quero me cadastrar no sistema, incluindo as vantagens que desejo oferecer, para disponibilizar benefícios aos alunos.
   - **Critérios de Aceitação**:
     - Formulário de cadastro deve solicitar informações da empresa e login/senha.
     - Após cadastro, poder acessar gerenciar vantagens.

2. **Cadastro de Vantagens**
   - **História de Usuário**: Como empresa parceira, quero cadastrar vantagens com descrição, foto e custo em moedas, para que os alunos possam trocá-las.
   - **Critérios de Aceitação**:
     - As vantagens cadastradas devem estar disponíveis para os alunos.

3. **Recebimento de Notificação de Resgate**
   - **História de Usuário**: Como empresa parceira, quero receber um email quando um aluno resgatar uma vantagem minha, incluindo um código, para confirmar a troca presencialmente.
   - **Critérios de Aceitação**:
     - Receber email após o resgate pelo aluno.
     - Email deve conter detalhes da vantagem e código alfanumérico único.

4. **Acesso Seguro ao Sistema**
   - **História de Usuário**: Como empresa parceira, quero acessar o sistema usando login e senha, para gerenciar minhas vantagens com segurança.
   - **Critérios de Aceitação**:
     - O sistema deve exigir autenticação no acesso.

---

## Requisitos Gerais

1. **Processo de Autenticação**
   - **História de Usuário**: Como usuário (aluno, professor ou empresa), quero que o sistema exija autenticação para realização de ações no sistema, a fim de garantir a segurança dos dados.
   - **Critérios de Aceitação**:
     - Todas as áreas restritas devem exigir login e senha.

2. **Pré-Cadastro de Instituições e Professores**
   - **História de Usuário**: Como administrador do sistema, quero pré-cadastrar instituições e professores, para que possam ser selecionados e acessados pelos usuários(alunos).
   - **Critérios de Aceitação**:
     - Deve ser possível inserir dados das instituições e professores.
