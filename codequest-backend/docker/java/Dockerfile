FROM eclipse-temurin:17-jdk-alpine

RUN adduser -D -s /bin/sh runner

WORKDIR /sandbox

RUN chmod 755 /sandbox

USER runner

CMD ["sh", "-c", "cp solution.java Main.java && javac Main.java && java Main"]
