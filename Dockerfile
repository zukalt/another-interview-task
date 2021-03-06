FROM adoptopenjdk/openjdk13  AS BUILD

WORKDIR /app/
COPY ./gradlew  ./build.gradle  ./
COPY ./gradle/wrapper/* ./gradle/wrapper/
RUN ./gradlew classes testClasses

COPY . /app/
RUN ./gradlew clean build -x test


FROM adoptopenjdk/openjdk13
WORKDIR /app
COPY --from=BUILD /app/build/libs/task-*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
