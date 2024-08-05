package com.ah.inference_server.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ah.inference_server.modal.InferenceRecord;
import com.ah.inference_server.modal.Registry;
import com.ah.inference_server.repository.InferenceRecordRepository;
import com.ah.message.InferenceResult;

@Service
public class InferenceResultService {

    @Autowired
    private InferenceRecordRepository inferenceRecordRepository;

    public List<InferenceRecord> getInferenceRecord(UUID registryId, int seqStart, int seqEnd) {
        return inferenceRecordRepository.findAllByRegistryIdAndSeqBetween(registryId, seqStart, seqEnd).toList();
    }

    public void newInferenceRecord(Registry registry, InferenceResult inferenceResult) {
        var inferenceRecord = new InferenceRecord();
        inferenceRecord.setInferenceResultPredictions(inferenceResult.predictions());
        inferenceRecord.setSeq(inferenceResult.seq());
        inferenceRecord.setRegistry(registry);
        inferenceRecordRepository.save(inferenceRecord);
    }
}
