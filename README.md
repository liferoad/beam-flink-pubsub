# Simple Java Test for Pub/Sub

* Test with `DirectRunner`
```bash
mvn compile exec:java -Dexec.args=--runner='DirectRunner'
```

* Test with the classic `FlinkRunner`

```bash
# build the jar
mvn package -Pflink-runner
# run the job with an embedded Flink cluster
mvn compile exec:java -Dexec.args="--runner='FlinkRunner'  --filesToStage=target/beam-flink-pubsub-1-jar-with-dependencies.jar" 
# 
```

* Test with `PortableRunner` (does not work)
```bash
docker run --net=host apache/beam_flink1.17_job_server:latest
mvn compile exec:java -Dexec.args="--runner='PortableRunner' --jobEndpoint=localhost:8099 --defaultEnvironmentType='LOOPBACK'  --streaming"
```

To use the flink config dir, you should use docker mount with `flink-conf.yaml`:
```bash
docker run --net=host -v /usr/local/google/home/xqhu/Dev/beam-flink-pubsub:/var/tmp apache/beam_flink1.16_job_server:latest --flink-conf-dir=/var/tmp
```

