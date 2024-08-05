package com.ah.inference_server.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ah.inference_server.modal.Registry;

@Repository
public interface RegistryRepository extends CrudRepository<Registry, UUID> {

}