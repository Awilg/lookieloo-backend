FROM openjdk:8-jre-alpine

ENV APPLICATION_USER ktor
RUN adduser -D -g '' $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app
RUN mkdir /appbuild
RUN chown -R $APPLICATION_USER /appbuild
USER $APPLICATION_USER

# App Building phase --------
COPY . /appbuild
WORKDIR /appbuild
RUN ls -lha
RUN ./gradlew clean build --debug
# End Building phase --------

COPY --from=build /appbuild/lookieloo-service-loo/build/libs/my-application.jar /app/lookieloo-service-loo/lookieloo-service-loo.jar
WORKDIR /app
CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "lookieloo-service-loo/lookieloo-service-loo.jar"]