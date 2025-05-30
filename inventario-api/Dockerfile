# Usar OpenJDK 21 como imagen base
FROM openjdk:21-jdk-slim

# Información del mantenedor
LABEL maintainer="Jesus Antonio Suarez Duarte <Jebus702@hotmail.com>"
LABEL description="Microservicio de Inventario - Sistema de Inventario"
LABEL version="1.0.0"

# Instalar herramientas necesarias
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Crear directorio de trabajo
WORKDIR /app

# Crear usuario no-root para seguridad
RUN groupadd -r spring && useradd -r -g spring spring

# Copiar el JAR compilado
COPY target/inventario-api-*.jar app.jar

# Crear directorio para logs
RUN mkdir -p /app/logs && chown -R spring:spring /app

# Cambiar al usuario no-root
USER spring

# Exponer puerto
EXPOSE 8082

# Variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV TZ=America/Bogota

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8082/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]