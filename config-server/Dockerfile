FROM openjdk:8u131-jdk-alpine

EXPOSE 8888

WORKDIR /root

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ARG LOCAL_CONFIG_REPO_DIR
COPY ${LOCAL_CONFIG_REPO_DIR}/*.yml ./config-repo/

ENV CONFIG_REPO_DIR /root/config-repo

VOLUME /tmp

ENTRYPOINT 	["java",											\
				"-Djava.security.egd=file:/dev/./urandom", 		\
				"-jar", "./app.jar" 							\
			]