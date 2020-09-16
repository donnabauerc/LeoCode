FROM frolvlad/alpine-java:jdk8-slim AS builder
WORKDIR /myvolume
VOLUME /myvolume
RUN javac ./code-examples/src/main/java/at/htl/examples/*.java

FROM frolvlad/alpine-java:jre8-slim
COPY --from=builder /*.class /

CMD ["java", "HelloWorld"]
