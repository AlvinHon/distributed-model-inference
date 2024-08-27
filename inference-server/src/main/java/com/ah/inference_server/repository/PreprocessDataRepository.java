package com.ah.inference_server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ah.inference_server.modal.PreprocessData;

@Repository
public interface PreprocessDataRepository extends MongoRepository<PreprocessData, String> {

}
