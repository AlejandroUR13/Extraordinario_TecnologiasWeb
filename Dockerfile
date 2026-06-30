# =========================================================
# Dockerfile — Sistema de Productos (Spring Boot + Java 21)
# Despliegue en Render.com
# =========================================================

# --- ETAPA 1: Compilar el proyecto con Maven ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar el wrapper de Maven primero (aprovecha la cache de Docker)
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# Descargar dependencias antes de copiar el codigo fuente
# (si el pom.xml no cambia, esta capa se reutiliza en builds posteriores)
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

# Copiar el codigo fuente y compilar
COPY src src
RUN ./mvnw clean package -DskipTests -B

# --- ETAPA 2: Imagen final solo con el JRE (mas liviana) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar solo el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Render inyecta la variable PORT automaticamente
EXPOSE 8080
ENV PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar"]
