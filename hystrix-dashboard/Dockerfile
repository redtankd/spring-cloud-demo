FROM openjdk:8u131-jdk-alpine

EXPOSE 7001

WORKDIR /root

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

VOLUME /tmp

ENTRYPOINT 	["java",											\
				"-Djava.security.egd=file:/dev/./urandom", 		\
				"-jar", "./app.jar" 							\
			]