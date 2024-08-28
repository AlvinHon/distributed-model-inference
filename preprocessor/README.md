# Preprocessor

It is a java program to process users' input data into Machine Learning Inference input (i.e. a tensor).

It works as follows:
1. Listen messages from Kafka streams topic `preprocess-request`.
1. Get the actual data from storage (`mongodb`) according to the information from the message.
1. Transform the data to input tensor for creating Inference Request.
1. Stream the Inference Request message to topic `inference-request`.
1. Repeat the process.

## Usage

```sh
# Build the jar file
mvn clean compile assembly:single
# Run the program
java -jar <Path to .jar file>
```




