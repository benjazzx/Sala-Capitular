# Stage 1: Compilación del proyecto con Maven
FROM eclipse-temurin:21-jdk-alpine AS build
# Instalar el gestor de Maven, ya que alpine no lo trae
RUN apk add --no-cache maven
# Indicar el directorio de trabajo dentro del contenedor
WORKDIR /app
# Copiar la configuración que tiene el proyecto
COPY pom.xml .
# Descargue las dependencias de Maven pero sin compilar
RUN mvn dependency:go-offline -B -q
# Copiar código fuente
COPY src ./src
# Compilar y empaquetar --- No incluir los tests (Pruebas unitarias)
RUN mvn package -DsKipTests -B -q

# Stage 2: Ejecutar la imágen de manera liviana
FROM eclipse-temurin:21-jre-alpine 
WORKDIR /app
# Copiar el JAR de la app
COPY --from=build /app/target/*.jar app.jar
# Documente el puerto de salida pero sin abrirlo directamente
EXPOSE 8761
# Cual es la imágen empaquetada para arrancar la aplicación Spring Boot
ENTRYPOINT [ "java", "-jar", "app.jar" ]