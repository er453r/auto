FROM ubuntu:24.04 as build

RUN apt-get update && apt-get -y --no-install-recommends install \
   wget unzip openjdk-17-jdk

ENV KOTLIN_VERSION 2.0.20
RUN wget --no-check-certificate "https://github.com/JetBrains/kotlin/releases/download/v${KOTLIN_VERSION}/kotlin-compiler-${KOTLIN_VERSION}.zip" &&\
    unzip kotlin-compiler-${KOTLIN_VERSION}.zip -d / && mv /kotlinc /usr/lib/

ENV PATH="/usr/lib/kotlinc/bin:${PATH}"
WORKDIR /step

COPY kotlin.main.kts .

CMD ["kotlin", "kotlin.main.kts"]
