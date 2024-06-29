# Simple Java Test for Pub/Sub

## Test with `DirectRunner`
```bash
mvn compile exec:java -Dexec.args=--runner='DirectRunner'
```

## Test with the classic `FlinkRunner`

```bash
# build the jar
mvn package -Pflink-runner
# run the job with an embedded Flink cluster
mvn compile exec:java -Dexec.args="--runner='FlinkRunner'  --filesToStage=target/beam-flink-pubsub-1-jar-with-dependencies.jar" 
# 
```

## Test with a testing topic

```bash

# create a topic
gcloud pubsub topics create my-test-topic

# publish a message
gcloud pubsub topics publish my-test-topic --message="hello"

# test it with FlinkRunner
mvn compile exec:java -Dexec.args="--runner='FlinkRunner'  --filesToStage=target/beam-flink-pubsub-1-jar-with-dependencies.jar --topic=projects/manav-jit-test/topics/my-test-topic" 

# test it with DirectRunner
mvn compile exec:java -Dexec.args="--runner='DirectRunner' --topic=projects/manav-jit-test/topics/my-test-topic"

# delete topic
gcloud pubsub topics delete my-test-topic
```

# To-Do

## Test with `PortableRunner` (does not work yet)
```bash
docker run --net=host apache/beam_flink1.17_job_server:latest
mvn compile exec:java -Dexec.args="--runner='PortableRunner' --jobEndpoint=localhost:8099 --defaultEnvironmentType='LOOPBACK'  --streaming"
```

To use the flink config dir, you should use docker mount with `flink-conf.yaml`:
```bash
docker run --net=host -v /usr/local/google/home/xqhu/Dev/beam-flink-pubsub:/var/tmp apache/beam_flink1.16_job_server:latest --flink-conf-dir=/var/tmp
```

# Links

* https://cloud.google.com/pubsub/docs/publish-receive-messages-client-library
* https://cloud.google.com/pubsub/docs/publish-receive-messages-gcloud