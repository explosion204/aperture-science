FROM openjdk:16-alpine

ENV JAR_FILE /opt/application.jar

ADD target/application.jar ${JAR_FILE}
ADD docker/entrypoint.sh /opt/entrypoint.sh

ENTRYPOINT ["/opt/entrypoint.sh"]