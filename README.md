# Distributed Model Inference

An example distributed system for Machine Learning model inference by using `Kafka`. 

The system serves user requests to predict objects from input images. The word "distributed" here does not imply techniques for dividing a model or input data for parallel execution. Instead, it refers to a loosely coupled architecture where workers can perform model inference in parallel to maximize the throughput of request completion.

![Created in https://app.diagrams.net](architecture.drawio.svg)

In general, a server handles images uploaded by users and delivers inference requests via a Kafka broker. A group of workers receives the requests and runs a Machine Learning model to predict objects. Workers will deliver the prediction results to Kafka. The results will be accumulated on the server so that users can later query the inference results.

The system is composed of
- `Spring boot` REST server which connects to `MariaDB` database by `JPA`.
- `Kafka` cluster deployable in `docker` environment.
- `Java` program to infer a `Resnet` model.

A `NodeJs` client script is included for demonstrating how to interact with the system.

## Repository Structure

|folder|description|
|---|---|
|[kafka](./kafka)|Contains a Docker Compose file to run Kafka in docker.|
|[mariadb](./mariadb)|Contains a Docker Compose file to setup database layer.|
|[inference-server](./inference-server)|A Maven project - Sprint boot REST server.|
|[inference-worker](./inference-worker)|A Maven project - Java program for ML model inference.|
|[client-demo](./client-demo)|Contains a client `nodejs` script for interaction with the system.|


## How to setup

1. Follow [kafka/README.md](/kafka/README.md), you will be able to run kafka cluster in docker containers.
1. Follow [mariadb/README.md](/kafka/README.md), you will be able to run database(s) in docker containers.
1. Follow [message/README.md](/message/README.md), you will install the common java package `com.ah.message` for the other java projects in this repo.
1. Follow [inference-workder/README.md](/inference-worker/README.md), you will be able to run multiple java programs as workers.
1. Follow [inference-server/README.md](/inference-server/README.md), you will run the REST API server to handle user requests.
1. Try out the system by using the client script [here](/client-demo/README.md)!
