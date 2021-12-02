FROM openjdk:8

ARG JAR_FILE=target/neplog*.jar
ARG PORT=16002

ENV TZ=Asia/Shanghai

WORKDIR /home/neplog
COPY ${JAR_FILE} neplog.jar

EXPOSE ${PORT}

ENTRYPOINT java -jar neplog.jar

# docker build -t neplog .
# docker run --name neplog -dp 16002:16002 -v path/to/application.yaml:/home/neplog/application.yaml neplog


