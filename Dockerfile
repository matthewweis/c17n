# syntax=docker/dockerfile:1.2

# see:
# https://www.javacodegeeks.com/2020/03/docker-compose-for-spring-boot-application-with-postgresql.html

# fatjar method
#FROM openjdk:16-alpine
#MAINTAINER Ignice
#ARG SECRET_TOKEN
#COPY use_secret.sh ./
## Mount the secret to /run/secrets:
## RUN ls
## RUN chmod +x use_secret.sh
#RUN --mount=type=secret,id=SECRET_TOKEN cat /run/secrets/SECRET_TOKEN
## RUN --mount=type=secret,id=SECRET_TOKEN use_secret.sh cat /run/secrets/SECRET_TOKEN
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#COPY src ./src
##ENV SECRET_TOKEN

#./mvnw clean compile exec:java package -Dexec.mainClass="io.ignice.c17n.Launcher" -Dapp.token="$1"
#RUN ["./mvnw", "clean", "test"]
#RUN ["./mvnw", "clean", "package"]

#RUN ./mvnw clean test package
#RUN ./mvnw clean compile test package

#ADD target/c17n-jar-with-dependencies.jar c17n-jar-with-dependencies.jar
#EXPOSE 8080
#RUN ./mvnw clean package exec:java
RUN ./mvnw clean package exec:java
#ENTRYPOINT ["java", "-jar", "c17n-jar-with-dependencies.jar", "null"]
#ENTRYPOINT java -jar target/c17n-jar-with-dependencies.jar $SECRET_TOKEN
#ENTRYPOINT SECRET_TOKEN="$(< /run/secrets/SECRET_TOKEN)" && echo $SECRET_TOKEN && java -jar target/c17n-jar-with-dependencies.jar $SECRET_TOKEN
#ENTRYPOINT [ "sh", "-c", "java", "-jar", "target/c17n-jar-with-dependencies.jar", "$SECRET_TOKEN" ]

FROM openjdk:16-alpine
MAINTAINER Ignice
ARG SECRET_TOKEN
COPY use_secret.sh ./
# Mount the secret to /run/secrets:
# RUN ls
# RUN chmod +x use_secret.sh

# TODO NEED THIS?
#RUN --mount=type=secret,id=SECRET_TOKEN cat /run/secrets/SECRET_TOKEN

#ARG TOKEN
#./mvnw clean compile exec:java package -Dexec.mainClass="io.ignice.c17n.Launcher" -Dapp.token="$1"
#./mvnw clean compile exec:java -Dexec.mainClass="io.ignice.c17n.Launcher" -Dapp.token="$1"

CMD ["./mvnw", "clean", "package"]
ADD target/c17n-jar-with-dependencies.jar c17n-jar-with-dependencies.jar
EXPOSE 8080
#ENTRYPOINT [ "sh", "-c", "java", "-jar", "c17n-jar-with-dependencies.jar", "$SECRET_TOKEN" ]
ENTRYPOINT ["sh", "-c", "ls /run/secrets", "&&", "SECRET_TOKEN=\"$(< /run/secrets/SECRET_TOKEN)\"", "&&", "java", "-jar", "c17n-jar-with-dependencies.jar", "$SECRET_TOKEN"]
#ENTRYPOINT ["sh", "-c", "java", "-jar", "c17n-jar-with-dependencies.jar", "$SECRET_TOKEN"]
#ENTRYPOINT ["java", "-cp", "c17n-jar-with-dependencies.jar", "io.ignice.c17n.Launcher"]

#FROM openjdk:16-alpine
#MAINTAINER Ignice
## no root
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
## prefer layers
#ARG DEPENDENCY=target/dependency
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java", "-cp", "c17n.jar", "io.ignice.c17n.Launcher"]


#ADD target/c17n.jar c17n.jar
#ADD target/c17n.jar c17n.jar
# todo have fatjar way and other way
#ENTRYPOINT ["java", "-jar", "c17n.jar"]


#FROM openjdk:8-jdk-alpine
#MAINTAINER experto.com
#VOLUME /tmp
#EXPOSE 8080
#ADD build/libs/springbootpostgresqldocker-0.0.1-SNAPSHOT.jar springbootpostgresqldocker.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/springbootpostgresqldocker.jar"]
#
#
#FROM openjdk:13-alpine
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
#ARG DEPENDENCY=target/dependency
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java","-cp","app:app/lib/*","pl.codeleak.samples.springboot.tc.SpringBootTestcontainersApplication"]