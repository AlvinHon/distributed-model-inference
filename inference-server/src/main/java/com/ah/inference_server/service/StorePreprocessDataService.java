package com.ah.inference_server.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ah.inference_server.modal.PreprocessData;
import com.ah.inference_server.repository.PreprocessDataRepository;

@Service
public class StorePreprocessDataService {

    @Autowired
    private PreprocessDataRepository preprocessDataRepository;

    /**
     * Store the uploaded image data to object storage. It requires the UUID and the
     * sequence number in the request, which will be used to form a search key.
     * 
     * @param uuid The UUID of the request
     * @param seq  The sequence number of the request
     * @param data The image data
     */
    public void store(UUID uuid, int seq, byte[] data) {
        var preprocessData = new PreprocessData();
        preprocessData.setSearchKey(uuid.toString() + String.valueOf(seq));
        preprocessData.setData(data);
        preprocessDataRepository.save(preprocessData);
    }
}
