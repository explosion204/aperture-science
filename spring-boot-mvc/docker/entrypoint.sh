#!/bin/sh

OPTIONALS=

if [ -n "JAVA_APP_NAME" ]; then
    OPTIONALS="${OPTIONALS} --spring.application.name=${JAVA_APP_NAME}"
fi

echo "JVM Options: ${JVM_OPTIONS}"
echo "JAR file: ${JAR_FILE}"
echo "OPTIONALS: ${OPTIONALS}"
echo ${JVM_OPTIONS} -jar ${JAR_FILE} ${OPTIONALS}
java ${JVM_OPTIONS} -jar ${JAR_FILE} ${OPTIONALS}