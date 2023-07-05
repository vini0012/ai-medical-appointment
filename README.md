# Documentação completa da API -> [ai-medical-appointment](http://localhost:8080/swagger-ui/index.html)
 
## Requisitos para execução do projeto
- Docker;
- Java 17;
- Gradle.

### Executando o projeto 
Após clonar o projeto com o Docker rodando, acesse o terminal e execute o comando `docker-compose up` para criar as imagens dos containers da aplicação.
Feito isso, basta executar a aplicação Spring Boot.

## Sobre

Trabalho final para matéria de Arquitetura de Software com framework Java PUC MG ministrada pelo professor Samuel 
Almeida Cardoso, onde a aplicação foi criada com o intuito 
de Construir um chatbot de uma assistente pessoal responder perguntas e realizar tarefas específicas como o agendamento de consultas medicas utilizando a [API OpenAI](https://github.com/TheoKanning/openai-java) 
para cadastro dos medicos, criação de agendas, consultar agendas, marcação de horarios
para consultar com determinado medico.

A forma utilizada para destinguir qual ação tomada pelo serviço em questão e abstraindo informações impostas pelo 
usuario destingue a intenção do mesmo e assim executando uma serie de comandos e ações.
De forma assim facilitando a interação e a experiencia do usuario.

## Detalhes Arquiteturais
### Requisitos para execução do projeto

- Java 17;
- Gradle;
- Docker.

### Padrões de Projetos Implementados

- Circuit Breaker Pattern com [Resilience4J](https://resilience4j.readme.io/docs) 
- Broker Pattern com [RabbitMQ](https://www.rabbitmq.com/documentation.html)
- Observer Pattern com [Spring - ApplicationListener](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationListener.html)

### Monitoramento da Aplicação

As url's abaixo devem ser acessadas com a aplicação rodando.

- http://localhost:8080/actuator temos o [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/).
- http://localhost:8080/actuator/prometheus temos o [Prometheus](https://prometheus.io/) juntamente com o [Micrometer](https://micrometer.io/) para capturar as métricas da aplicação para observabilidade.
- http://localhost:9411/zipkin/ temos o [Zipkin](https://zipkin.io/) para trace da aplicação.
- http://localhost:5601/ temos o [Kibana](https://www.elastic.co/pt/kibana/) (ELK).
- http://localhost:3000/ temos o [Grafana](https://grafana.com/) para Observabilidade da aplicação usando as métricas geradas pelo Prometheus.
- http://localhost:9090/ temos a UI do [Prometheus](https://prometheus.io/) para consulta de métricas e gráficos das mesmas.
- http://localhost:15672/ temos o [RabbitMQ](https://www.rabbitmq.com/documentation.html) para mensageria da aplicação. Usuário/Senha de acesso: guest

### Banco de dados NoSql

- Utilizamos um banco de dados não relacional e na http://localhost:8081/ temos acesso a UI do [MongoDB](https://encr.pw/HXSPS).

## Criando o Dashboard da aplicação no Grafana
Usuário/Senha de acesso: admin

Primeiramente deve ser criado um DataSource do Prometheus no menu Connections > New DataSource, a url de acesso ao Prometheus é http://prometheus:9090.
Dentro da aplicação existe um arquivo chamado <b>grafana-dashboard.json</b>, este JSON deve ser copiado e importado dentro do Grafana através da opção Dashboard > New > Import Json, feito isso,
basta linkar o datasource que acabou de ser criado no dashboard e salvar.

## Detalhes sobre a Security

  Para gerar o token do OpenAI, para usar as APIS, siga o seguinte tutorial:
  - https://text-gen.com/get-openai-access-token

  É necessário criar os tokens das APIS de autenticação do Google e/ou github:
  
  - Google:
    - https://developers.google.com/workspace/guides/create-credentials
    - Callback URL - http://localhost:8080/login/oauth2/code/google

  - Github:
    - https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/creating-an-oauth-app
    - HomePage URL - http://localhost:8080
    - Callback URL - http://localhost:8080/login/oauth2/code/github


## Integrantes

```
Emerson de Almeida Rocha (18524)
Hygor Silva Souza (149288)
Marcos Vinícius da Silva Antonio (179750)
Matheus Peluca de Paula (180320)
Reinaldo Oliveira Machado Junior (177349)
Rodrigo da Silva Pimenta (177875)

```

