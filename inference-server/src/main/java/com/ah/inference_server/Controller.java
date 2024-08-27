package com.ah.inference_server;

import java.util.UUID;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ah.inference_server.http.ExceptionResponse;
import com.ah.inference_server.http.ImageRequest;
import com.ah.inference_server.http.RegisterRequest;
import com.ah.inference_server.http.RegisterResponse;
import com.ah.inference_server.http.ResultRequest;
import com.ah.inference_server.http.ResultResponse;
import com.ah.inference_server.service.InferenceRequestService;
import com.ah.inference_server.service.InferenceResultService;
import com.ah.inference_server.service.RegistrationService;
import com.ah.inference_server.service.StorePreprocessDataService;
import com.ah.message.InferenceResult;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class Controller {
    final static Logger logger = Logger.getLogger(Controller.class.getName());

    @Autowired
    private InferenceRequestService inferenceRequestService;

    @Autowired
    private InferenceResultService inferenceResultService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private StorePreprocessDataService storePreprocessDataService;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
        var uuid = registrationService.newRegistry();
        return new RegisterResponse(uuid.toString());
    }

    @PostMapping(value = "/image", consumes = "multipart/form-data")
    public void request(
            @RequestParam("uuid") String paramUuid,
            @RequestParam("seq") int paramSeq,
            @RequestParam("topk") int paramTopK,
            @RequestParam("payload") MultipartFile param_payload) throws Exception {
        var payloadBytes = param_payload.getBytes();
        var imageRequest = new ImageRequest(paramUuid, paramSeq, paramTopK, payloadBytes);
        var uuid = imageRequest.validatedUuid();
        var image = imageRequest.validatedImage();
        var seq = imageRequest.validatedSeq();
        var topk = imageRequest.validatedTopK();

        // Store the image data to object storage. Expected other microservices to
        // process this data.
        storePreprocessDataService.store(uuid, seq, payloadBytes);
        // Send the inference request. Other microservices will process this request
        // upon receiving it.
        inferenceRequestService.sendRequest(image, uuid, seq, topk);
    }

    @GetMapping("/result")
    public ResultResponse result(
            @RequestParam("uuid") String paramUuid,
            @RequestParam("seqStart") Integer paramSeqStart,
            @RequestParam("seqEnd") Integer paramSeqEnd) throws Exception {
        var resultRequest = new ResultRequest(paramUuid, paramSeqStart, paramSeqEnd);
        var uuid = resultRequest.validatedUuid();
        var seqStart = resultRequest.seqStart();
        var seqEnd = resultRequest.seqEnd();

        var recordItems = registrationService.getRegistry(uuid)
                .map((registry) -> inferenceResultService.getInferenceRecord(registry.getId(), seqStart, seqEnd))
                .orElseThrow(() -> new Exception("Cannot find registry"))
                .stream()
                .map(ResultResponse.RecordItem::fromInferenceRecord)
                .toList();
        return new ResultResponse(recordItems);
    }

    @ExceptionHandler({ Exception.class })
    public ExceptionResponse handleException(Exception ex, HttpServletResponse response) {
        response.setStatus(400);
        return new ExceptionResponse(ex.getMessage());
    }

    @KafkaListener(topics = "#{'${spring.kafka.topic-inference-result}'.split(',')}", groupId = "${spring.kafka.consumer-group-id}")
    public void listenInferenceResult(ConsumerRecord<String, InferenceResult> record) throws Exception {
        var result = record.value();
        var uuid = UUID.fromString(result.id());

        registrationService.getRegistry(uuid).ifPresent((registry) -> {
            logger.info("New inference record: " + registry.getId());
            inferenceResultService.newInferenceRecord(registry, result);
        });
    }
}
