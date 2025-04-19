FROM azul/zulu-openjdk-alpine:17-latest

ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar

ENV DB_MAXIMUM_POOL_SIZE=100
ENV DB_MINIMUM_IDLE=10

ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx768m -XX:MaxMetaspaceSize=128m -XX:+UseContainerSupport -XX:+UseG1GC"

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar","-Ddb_maximum_pool_size=${DB_MAXIMUM_POOL_SIZE}", "-Ddb_minimum_idle=${DB_MINIMUM_IDLE}"]
