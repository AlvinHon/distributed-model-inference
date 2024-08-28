package com.ah.inference_server.modal;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

/**
 * This class represents the data that will be processed in ML preprocessing
 * process. The data is stored as document in mongo db, that it assumes the size
 * of the data should be less than 16MB.
 */
@Document(collection = "preprocess_data")
public class PreprocessData {
    @Id
    private String id;

    @Indexed(name = "search_key_index")
    private String searchKey;

    @Indexed(name = "ttl_index", expireAfterSeconds = 60)
    private byte[] data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
