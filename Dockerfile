# Imagen base de Java
FROM openjdk:21-jdk-slim

# Crear un directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo .jar del proyecto al contenedor
COPY target/restaurApp-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto donde corre Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
