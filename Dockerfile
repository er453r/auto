FROM gradle:8.10.0-jdk-21-and-22 as build

WORKDIR /workdir

# prepare runtime
RUN jlink --add-modules ALL-MODULE-PATH --output runtime --no-header-files --no-man-pages --compress=2 --strip-debug

# build all dependencies for offline use and cache them
COPY build.gradle.kts .
RUN gradle dependencies

# copy all other files
COPY src ./src

# build
RUN gradle build

FROM ubuntu:24.04

RUN useradd -m auto
USER auto
WORKDIR /home/auto

COPY --from=build --chown=auto:auto /workdir/runtime runtime
COPY --from=build --chown=auto:auto /workdir/build/libs/*SNAPSHOT.jar auto.jar

ENTRYPOINT runtime/bin/java -jar auto.jar
