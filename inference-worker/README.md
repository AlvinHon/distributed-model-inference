# Model Inference Worker

It is a java program to run a Machine Learning model to predict the object from images. 

It works as follows:
1. Subscribe Kafka topic `inference-request`
2. Listen to receive messages.
1. Run ML model with the input from the message.
1. Create prediction result
1. Produce Kafka message to topic `inference-result`
1. Repeat step 2

## Usage

```sh
java -jar <Path to .jar file>
```

See Section [Build](#build) for building the jar executable.

The application is configurable by environment variables. See [Config.java](src/main/java/com/ah/inference/Config.java) for the default values.

Environment Variables:
- `BOOTSTRAP_SERVERS`: bootstrap servers for Kafka. String value contains a list of servers separated by comma.
- `POLL_DURATION`: duration in milliseconds for polling kafka consumer messages.

## Machine Learning Model 

The ML model is an Object Detection Model. It intakes image and predicts object labels defined by [Imagenet](https://www.image-net.org/).

The model file is required to be placed under folder `src/resources/model`. Please check the file [resnet101-v1-7.md](src/resources/model/resnet101-v1-7.md) for details.

*Note: the program uses `onnxruntime` to run the model. If you want to replace the model, make sure the file format is compatible (e.g. .onnx, .ort), and the input and output shape are the same.*

## Build

Before building it, please make sure the model file has been downloed to the specifief folder. See [Machine Learning Model](#machine-learning-model).

The executable jar is built by running the command below. It bundles together with its dependencies and the model file.

```sh
mvn clean compile assembly:single
```

The built .jar file will be created under `/target` folder. E.g. `inference-1.0-SNAPSHOT-jar-with-dependencies.jar`.




