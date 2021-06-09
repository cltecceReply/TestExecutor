package com.reply.repo.repository;

import com.reply.repo.entity.ServiceDescription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServiceDescriptionRepository extends CrudRepository<ServiceDescription, String> {
    ServiceDescription findByServiceName(String name);
    List<ServiceDescription> findAll();
}
