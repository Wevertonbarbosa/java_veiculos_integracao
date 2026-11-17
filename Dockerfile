# Estágio 1: prepara o ambiente para compilar a aplicação.
FROM gradle:8.14-jdk21 AS build

# Define o diretório de trabalho dentro do container.
WORKDIR /app

# Copia os arquivos do projeto para o ambiente de build.
COPY . .

# Compila a aplicação e gera o JAR executável.
RUN gradle test bootJar --no-daemon

# Estágio 2: prepara o ambiente para executar a aplicação.
FROM eclipse-temurin:21-jre AS runtime

# Copia o JAR gerado no estágio de build para o ambiente de execução.
COPY --from=build /app/build/libs/*.jar app.jar

# Documenta a porta utilizada pela aplicação Spring Boot.
EXPOSE 8080

# Inicia a aplicação Spring Boot.
ENTRYPOINT ["java", "-jar", "app.jar"]