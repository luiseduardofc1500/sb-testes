# Sistema Bancário

Este sistema será desenvolvido como parte da disciplina **DIM0517 - Gerência de Configuração e Mudanças**, com o objetivo de aplicar as boas práticas e convenções de controle de versão. O sistema contará com funcionalidades de: cadastrar conta, consultar saldo, crédito, débito e transferência.

# Executando a aplicação

## BACKEND

### Requisitos:
- Java JDK 21
- Maven instalado (ou usar o wrapper Maven fornecido)

### Como rodar o projeto

1. Clone o repositório

```bash
git clone https://github.com/maycon-mdrs/sistema-bancario.git
cd sistema-bancario/backend
```

2. Com o maven instalado, rode os seguintes comandos:

```bash
mvn clean install
mvn spring-boot:run

# Ou usando o wrapper Maven (Linux/Mac):
./mvnw clean install
./mvnw spring-boot:run

# Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

3. Acesse a aplicação
Após subir, a aplicação deverá estar disponivel em:
```http://localhost:8080```

## FRONTEND

### Requisitos:
- Node.js versão 18 ou superior
- npm or yarn instalado

### Como rodar o projeto

1. Ambos projetos estão no mesmo repositorio, em diferentes pastas

```bash
cd sistema-bancario/frontend
```

2. Instale as dependencias
```bash
npm install

ou

yarn install
```

3. Rode o servidor de desenvolvimento

```bash
npm run dev
```

4. Acesse a aplicação
Após subir, a aplicação deverá estar disponivel em:
```http://localhost:3000```

## Integrantes da Equipe

<table>
    <tr>
        <td align="center" width="80">
            <a href="https://github.com/luiseduardofc1500">
                <img src="https://github.com/luiseduardofc1500.png" width="50" style="border-radius: 50%;"/>
            </a>
        </td>
        <td>
            <strong>Luís Eduardo Fernandes Cândido</strong><br/>
            <a href="https://github.com/luiseduardofc1500">luiseduardofc1500</a>
        </td>
    </tr>
    <tr>
        <td align="center" width="80">
            <a href="https://github.com/maycon-mdrs">
                <img src="https://github.com/maycon-mdrs.png" width="50" style="border-radius: 50%;"/>
            </a>
        </td>
        <td>
            <strong>Maycon Douglas Rêgo Santos</strong><br/>
            <a href="https://github.com/maycon-mdrs">maycon-mdrs</a>
        </td>
    </tr>
    <tr>
        <td align="center" width="80">
            <a href="https://github.com/pedrolgois/pedrolgois">
                <img src="https://github.com/pedrolgois.png" width="50" style="border-radius: 50%;"/>
            </a>
        </td>
        <td>
            <strong>Pedro Lucas Gois Costa</strong><br/>
            <a href="https://github.com/pedrolgois">pedrolgois</a>
        </td>
    </tr>
</table>

## Linguagem de Programação e Stack de Desenvolvimento

### **Back-end:**

<img src="https://img.icons8.com/?size=100&id=90519&format=png&color=000000" alt="Spring Boot Logo" width="20"/> Spring Boot (Java)

### **Front-end:**

<img src="https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg" width="20"/> React (Typescript)