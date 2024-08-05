package com.ah.inference_server.modal;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "registry")
public class Registry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "registry", fetch = FetchType.LAZY)
    private List<InferenceRecord> inferenceRecords;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<InferenceRecord> getInferenceRecords() {
        return inferenceRecords;
    }

    public void setInferenceRecords(List<InferenceRecord> inferenceRecords) {
        this.inferenceRecords = inferenceRecords;
    }
}
