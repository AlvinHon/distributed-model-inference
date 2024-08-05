package com.ah.inference_server.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.ah.inference_server.modal.InferenceRecord;

@Repository
public interface InferenceRecordRepository extends CrudRepository<InferenceRecord, Long> {
    public Streamable<InferenceRecord> findAllByRegistryIdAndSeqBetween(UUID uuid, int seqStart, int seqEnd);
}