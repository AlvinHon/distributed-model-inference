# Message Types for producers and consumers

This package contains the data types used for messaging between Kafka Producers and Consumers. Serializer (and Deserializer) classes are provided which serializes (deserializes) the Java object (implementing `java.io.Serializable`) into bytes.

## Install 
By running the command below, built package will be stored locally (i.e. `.m2` folder under home directory).

```sh
mvn clean install
```

## Import to Local Projects

Add the dependency tag below where `{version}` is the version number of this package (currently `1.0`).

```xml
<dependency>
    <groupId>com.ah.message</groupId>
    <artifactId>message</artifactId>
    <version>{version}</version>
</dependency>
```