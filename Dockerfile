#
# Build stage
#
#FROM maven:3.6.0-jdk-11-slim AS build
#COPY . /home/app/
#COPY pom.xml /home/app/
#COPY decrypted-files /home/app/
#COPY encrypted-files /home/app/
#RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:18-ea-8-jdk-slim
#COPY --from=build /home/app/target/Encrypted_storage_with_face_recognition-0.0.1-SNAPSHOT.jar /usr/local/lib/Encrypted_storage_with_face_recognition.jar
COPY . .
EXPOSE 8080
ENTRYPOINT ["java","-jar","/target/Encrypted_storage_with_face_recognition-0.0.1-SNAPSHOT.jar"]