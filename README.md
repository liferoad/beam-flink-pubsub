# Simple Java Test for Pub/Sub

* Test with `DirectRunner`
```bash
mvn compile exec:java -Dexec.args=--runner='DirectRunner'
```

* Test with `PortableRunner`
```bash
docker run --net=host apache/beam_flink1.17_job_server:latest
mvn compile exec:java -Dexec.args="--runner='PortableRunner' --jobEndpoint=localhost:8099 --defaultEnvironmentType='LOOPBACK'  --streaming"
```