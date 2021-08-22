FROM clojure:openjdk-14-lein-buster
RUN mkdir -p /workspace
WORKDIR /workspace
CMD exec /bin/bash -c "trap : TERM INT; sleep infinity & wait"
