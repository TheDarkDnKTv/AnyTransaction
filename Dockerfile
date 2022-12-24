FROM eclipse-temurin:17

WORKDIR /build
COPY build/libs/AnyTransaction* .

STOPSIGNAL SIGTERM

CMD java ${JAVA_ARGS} -jar $(ls -1 | awk '/.*.jar/ && !/plain/') --spring.datasource.url=${DB_URL} --spring.datasource.username=${DB_USER} --spring.datasource.password=${DB_PASS} ${APPLICATION_ARGS}