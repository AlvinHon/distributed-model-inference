package com.ah.inference_server.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ah.inference_server.modal.Registry;
import com.ah.inference_server.repository.RegistryRepository;

@Service
public class RegistrationService {

    @Autowired
    private RegistryRepository registryRepository;

    public UUID newRegistry() {
        var registry = new Registry();
        registryRepository.save(registry);
        return registry.getId();
    }

    public Optional<Registry> getRegistry(UUID uuid) {
        return registryRepository.findById(uuid);
    }
}
