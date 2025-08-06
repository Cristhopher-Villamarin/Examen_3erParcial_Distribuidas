# Usa una imagen base de Java
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar generado desde el directorio target al contenedor
COPY target/publicaciones-*.jar /app/app.jar


EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]