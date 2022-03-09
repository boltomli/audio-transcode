FROM maven:3-openjdk-11

WORKDIR /app
COPY pom.xml .
RUN mvn -Djavacpp.platform=linux-x86_64 dependency:resolve
COPY src src
RUN mvn -Djavacpp.platform=linux-x86_64 clean install
ENTRYPOINT [ "java", "-jar", "target/audio-transcode-0.0.1-SNAPSHOT.jar" ]
