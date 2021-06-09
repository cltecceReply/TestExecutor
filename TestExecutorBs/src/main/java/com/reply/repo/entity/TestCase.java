package com.reply.repo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="TEST_CASES")
public class TestCase {

    @Id
    @Column(name="TEST_ID")
    private long testId;
    @Column(name="SERVICE_NAME")
    private String serviceName;
    @Column(name="TEST_DATA")
    private String request;
}