package com.ah.inference_server.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inference_record")
public class InferenceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long recordId;

    @Column(name = "seq", nullable = false)
    private int seq;

    @Column(name = "inference_result_predictions", nullable = true)
    private String[] inferenceResultPredictions;

    @ManyToOne
    @JoinColumn(name = "registry_id", referencedColumnName = "id", nullable = false)
    private Registry registry;

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String[] getInferenceResultPredictions() {
        return inferenceResultPredictions;
    }

    public void setInferenceResultPredictions(String[] inferenceResultPredictions) {
        this.inferenceResultPredictions = inferenceResultPredictions;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}
