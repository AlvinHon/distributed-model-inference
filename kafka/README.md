# Run Kafka in Docker 

This folder contains a docker compose file which is taken from the official repository: https://github.com/apache/kafka/blob/trunk/docker/examples.

Here is to describe the setup of the kafka servers and how to create topics for our use case.

## Kafka

Kafka servers will be running in 6 docker containers. Three of them are controller which coordinates nodes and manages the cluster. The other three are brokers for messaging with producers and consumers.

The brokers' exposed server ports are:
- localhost:29092
- localhost:39092
- localhost:49092

## Run Kafka

Docker installation is required. 

To setup the containers, run

```sh
IMAGE=apache/kafka:latest docker compose -f docker-compose.yml up -d
```

Or on Windows,
```cmd
set IMAGE=apache/kafka:latest
docker compose -f docker-compose.yml up -d
```

## Topics

In the system, there are two kinds of message, `inference-request` and `inference-result`. As the names suggested, clients send Requests to brokers. Workers will pull the requests and create results after some processing. Then the result will be sent back to brokers.

Here we use the client scripts `kafka-topics.sh` (or on Windows, `kafka-topics.bat`) to create the topics. The client scripts are bundled with the Kafka releases [here](https://kafka.apache.org/downloads). 

### inference-request

```sh
bin/kafka-topics.sh --create --bootstrap-server localhost:29092 --replication-factor 3 --partitions 3 --topic inference-request
```

### inference-result

```sh
bin/kafka-topics.sh --create --bootstrap-server localhost:29092 --replication-factor 3 --partitions 3 --topic inference-result
```