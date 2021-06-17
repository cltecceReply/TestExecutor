package com.reply.repo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="SERVICE_DESCRIPTION")
public class ServiceDescription {
    @Id
    @Column(name="SERVICE_NAME")
    private String serviceName;
    @Column(name="ENDPOINT_NEW")
    private String endpointOpen;
    @Column(name="ENDPOINT_OLD")
    private String endpointlegacy;

}
