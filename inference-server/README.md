# Inference Server

A `Spring boot` application serves REST APIs for Machine Learning Model Inference.

Two main tasks:
- Handle image file uploaded by users and deliver inference request to Kafka brokers.
- Listen Kafka brokers to receive inference result messages. Store the inference result into Database, and serve the associated queries from users.

The configuration, including kafka connection parameters, can be found in [application.properties](src/main/resources/application.properties).

## HTTP APIs

Successful response is returned with status code 200.
Any application failure will result in HTTP response with status code 400, and response body:

```json
// JSON:
{
    "error": String
}
```


### POST /register

Register the inference service. 

Request body:
```json
// JSON (Empty Body):
{
}
```

A `UUID` identifier is returned in json format

```json
/// JSON:
{
    "uuid": String
}
``` 

The identifier will be used in the rest of the APIs.

### POST /image

Upload image for inference. 

Request body is `FormData` with params:
- `uuid`, String. The service registration identifier.
- `seq`, Int. The sequence number defined by user, which will be included in inference result.
- `topk`, int. The number of top 'K' predictions in inference result.
- `payload`, MultipartFile. The bytes of the image file.

No response data.

### GET /result

Get the inference results corresponding to the requests previously made.

Request body:
```json
// JSON:
{
    "uuid": String,
    "seqStart": int,
    "seqEnd": int
}
```
- `uuid`. The service registration identifier.
- `seqStart` and `seqEnd` defines the range of the sequence number specified in the request previously made.

Response body:
```json
// JSON:
{
    "items": [
        {
            "seq": int,
            "predictions": String[]
        },
        ...
    ]
}
```

*The `items` might not include the inference result consecutively because the inference could still be under progress. It also implies the possibility that `items` could empty array.*

## Build

```sh
mvn package
```

The built jar file can be found in `target` folder. E.g. `target/inference-server-0.0.1-SNAPSHOT.jar`.

## Run

```sh
java -jar <path to .jar file>
```
