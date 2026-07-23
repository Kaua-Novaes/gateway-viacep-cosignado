# Estágio 1: Compilação e Build da aplicação
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copia arquivos do Maven Wrapper e dependências
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Baixa as dependências em cache
RUN ./mvnw dependency:go-offline -B

# Copia os fontes e compila o JAR
COPY src ./src
RUN ./mvnw package -DskipTests

# Estágio 2: Imagem leve para execução em produção
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Usuário sem privilégios de root por segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copia o JAR do estágio de build
COPY --from=builder /app/target/*.jar app.jar

# Define porta alternativa (8088) em vez da 8080 padrão
# Permite mudar facilmente no container via variável de ambiente SERVER_PORT
ENV SERVER_PORT=8088
EXPOSE 8088

ENTRYPOINT ["java", "-Dserver.port=${SERVER_PORT}", "-jar", "app.jar"]
