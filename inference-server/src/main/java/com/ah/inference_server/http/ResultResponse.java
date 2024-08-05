package com.ah.inference_server.http;

import java.util.List;

import com.ah.inference_server.modal.InferenceRecord;

public record ResultResponse(List<RecordItem> items) {

    public record RecordItem(int seq, String[] predictions) {
        public static RecordItem fromInferenceRecord(InferenceRecord inferenceRecord) {
            return new RecordItem(
                    inferenceRecord.getSeq(),
                    inferenceRecord.getInferenceResultPredictions());
        }
    }
}
