# Gateway de Busca ViaCEP


## Visão Geral

Esta é uma API REST desenvolvida como solução para o gateway de busca de endereços integrado ao ViaCEP. O projeto consiste em uma fachada inteligente que realiza a consulta, validação e tratamento dos dados de CEP, retornando apenas os dados necessários de forma padronizada e resiliente.

A arquitetura da solução baseia-se no padrão **MVC**: uma estrutura limpa, organizada e de fácil manutenção, com foco em diferenciais técnicos como tratamento centralizado de erros, validações de domínio e uma cobertura completa de testes automatizados para proteção da aplicação.

## Arquitetura da Solução

### O Porquê da Escolha: MVC + Camada de Serviço

A solução foi projetada utilizando o padrão arquitetural **MVC (Model-View-Controller)** complementado por uma **Camada de Serviço**.

A escolha mantém a simplicidade e clareza da aplicação, separando as responsabilidades de forma explícita:
* **Separação de Responsabilidades:** Cada camada tem um papel definido. O `BuscarCepController` lida com as requisições HTTP e mapeamento de entrada/saída, o `BuscarCepService` orquestra o fluxo de negócio, o `CepValidator` isola as regras de domínio para normalização, e o `ViaCepClient` abstrai a comunicação de infraestrutura com a API externa.
* **Manutenibilidade e Extensibilidade:** A divisão de componentes facilita modificações isoladas (por exemplo, alterar o gateway externo de busca sem impactar a validação ou os controllers).
* **Testabilidade:** Isolar os componentes de negócio e validação permite testar cada lógica individualmente sem acoplamento.

### Diagrama de Fluxo (Mermaid)

O diagrama abaixo ilustra o fluxo de uma requisição pelas camadas da aplicação:

```mermaid
graph TD
    subgraph "Nossa Aplicação (gateway-viacep-cosignado)"
        A[Cliente HTTP] --> B(BuscarCepController);
        B --> C{BuscarCepService};

        subgraph "Validação e Consulta"
            C -- 1. Normaliza e Valida CEP --> D[CepValidator];
            C -- 2. Consulta CEP normalizado --> E[ViaCepClient];
        end

        subgraph "Tratamento de Erros Centralizado"
            G(GlobalExceptionHandler)
        end
        
        E --> F((API ViaCEP));
        E -- 3. Retorna resposta --> C;
        C -- 4. Formata logradouro e retorna --> B;

        B --> A;

        B -. lança exceções .-> G;
        C -. lança exceções .-> G;
        E -. lança exceções .-> G;
        D -. lança exceções .-> G;
        G -- retorna resposta de erro --> A;
    end
```

## Requisitos Atendidos

### Requisitos Funcionais (RF)
- **RF01:** A aplicação disponibiliza o endpoint `GET /api/v1/buscarcep/{cep}` para busca de endereços.
- **RF02:** Na resposta ao cliente, dados adicionais retornados pelo ViaCEP (como `uf`, `ibge`, `gia`, `ddd` e `siafi`) são removidos, mantendo apenas `cep`, `logradouro`, `complemento`, `bairro` e `localidade`.
- **RF03:** O campo `logradouro` retornado na resposta final é convertido para letras minúsculas (`lowercase`).
- **RF04:** A aplicação valida o formato do CEP. Se nulo, em branco ou fora do padrão numérico esperado de 8 dígitos, retorna um erro **404 Not Found**.
- **RF05:** Se o CEP não for encontrado na base do ViaCEP, a aplicação retorna um erro **404 Not Found**.
- **RF06:** Se a comunicação com o ViaCEP falhar ou estourar o timeout, a aplicação retorna o erro **503 Service Unavailable**.

### Requisitos Não Funcionais (RNF)
- **RNF01:** A solução adota a arquitetura MVC com divisão clara de responsabilidades.
- **RNF02:** O código segue boas práticas de nomenclatura e organização limpa.
- **RNF03:** A API possui timeouts configurados (5 segundos para conexão e leitura) na integração externa.
- **RNF04:** A aplicação conta com uma suíte de testes unitários e de integração para validar as regras e fluxos.
- **RNF05:** A documentação OpenAPI é fornecida automaticamente no Swagger.

## Diferenciais Técnicos

1. **Tratamento Centralizado de Erros:** Utilização de um `GlobalExceptionHandler` (`@RestControllerAdvice`) para interceptar exceções de validação de domínio (`CepInvalidoException`), recursos não localizados (`NotFoundException`, `NoResourceFoundException`) e indisponibilidades externas (`GatewayException`), garantindo respostas HTTP consistentes com payloads padronizados.
2. **Estratégia de Testes Completa:**
    * **Testes Unitários:** Focados no `CepValidator` para validar as regras puras de validação/normalização de entrada.
    * **Testes com Mocks de Serviço:** Validação das regras de negócio do `BuscarCepService` isolando o validador e o gateway com JUnit 5 e Mockito.
    * **Testes de Controller:** Testagem das rotas, retornos HTTP e JSON estruturados utilizando `@WebMvcTest` e `MockMvc`.
    * **Testes de Cliente HTTP:** O `ViaCepClient` é testado em isolamento de rede usando `MockRestServiceServer` para interceptar as requisições do `RestClient`, garantindo cobertura segura contra falhas externas.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 4.0.7** (Spring Web MVC e RestClient)
- **Apache Maven** (Gerenciamento de dependências)
- **Lombok** (Produtividade de código)
- **JUnit 5 & Mockito** (Testes automatizados)
- **Springdoc OpenAPI** (Documentação do Swagger)

## Como Executar

### Pré-requisitos
- JDK 17 instalado e configurado nas variáveis de ambiente.

### Passos para Inicialização

1. **Clone o repositório:**
   ```bash
   git clone <URL_DO_REPOSITORIO>
   ```

2. **Execute a aplicação:**
   Navegue até a raiz do projeto e inicie a aplicação pelo terminal utilizando o Maven Wrapper:
    * No Linux/macOS:
      ```bash
      ./mvnw spring-boot:run
      ```
    * No Windows:
      ```bash
      .\mvnw spring-boot:run
      ```

3. A aplicação estará ativa em `http://localhost:8080`.
4. A documentação interativa Swagger pode ser acessada em `http://localhost:8080/documentacao`.

## Documentação da API

### `GET /api/v1/buscarcep/{cep}`
Busca as informações resumidas de um endereço com base no CEP informado (com ou sem hífens).

#### Resposta de Sucesso (Status 200 OK)
* **Exemplo de Requisição:** `GET http://localhost:8080/api/v1/buscarcep/01001-000`
* **Corpo do Retorno:**
```json
{
  "cep": "01001-000",
  "logradouro": "praça da sé",
  "complemento": "lado ímpar",
  "bairro": "Sé",
  "localidade": "São Paulo"
}
```

#### Respostas de Erro

* **CEP Inválido ou Não Encontrado (Status 404 Not Found)**
    * **Cenário:** O CEP informado possui tamanho incorreto, letras ou não está registrado no ViaCEP.
    * **Corpo do Retorno:**
  ```json
  {
    "message": "O CEP deve possuir exatamente 8 números",
    "status": 404
  }
  ```

* **Serviço ViaCEP Indisponível (Status 503 Service Unavailable)**
    * **Cenário:** A API do ViaCEP está offline ou excedeu o limite de tempo configurado.
    * **Corpo do Retorno:**
  ```json
  {
    "message": "Não foi possível consultar o serviço ViaCEP",
    "status": 503
  }
  ```

## Como Rodar os Testes

Para executar toda a suíte de testes da aplicação:
```bash
./mvnw test
```
