package com.ah.preprocessor;

import java.util.Optional;

import org.bson.Document;
import org.bson.types.Binary;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class DocumentStorage {
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;

    public DocumentStorage() {
        mongoClient = MongoClients.create(Config.MongoDB.connectionString());
        mongoDatabase = mongoClient.getDatabase(Config.MongoDB.DATABASE);
        mongoCollection = mongoDatabase.getCollection(Config.MongoDB.COLLECTION);
    }

    public Optional<byte[]> find(String searchKey) {
        try {
            Document document = mongoCollection.find(Filters.eq("searchKey", searchKey)).first();
            Binary data = document.get("data", Binary.class);
            return Optional.of(data.getData());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void close() {
        mongoClient.close();
    }

}
