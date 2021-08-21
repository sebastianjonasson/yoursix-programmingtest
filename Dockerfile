FROM clojure:openjdk-14-lein-buster
RUN mkdir -p /workspace
WORKDIR /workspace
CMD ["lein", "run"]


