package com.reply.repo.repository;

import com.reply.repo.entity.TestCase;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestCaseRepository extends CrudRepository<TestCase, Long>{
    List<TestCase> findByServiceName(String serviceName);
}
