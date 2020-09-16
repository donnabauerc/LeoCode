FROM frolvlad/alpine-java:jdk8-slim AS builder
COPY compile-java.sh .
COPY code-examples code-examples
RUN ./compile-java.sh

FROM frolvlad/alpine-java:jre8-slim
COPY --from=builder code-examples/src/main/java/at/htl/examples .
RUN echo $(ls)
CMD ["java", "HelloWorld"]
