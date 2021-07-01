package com.reply.services.endpoint;

import com.reply.model.EndpointServiceOut;
import com.reply.repo.entity.ServiceDescription;
import com.reply.repo.repository.ServiceDescriptionRepository;
import com.reply.services.endpoint.IEndpointRetrievalService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier("DbEndpointRetrivalService")
public class DbEndpointRetrivalService implements IEndpointRetrievalService {

    @Autowired
    ServiceDescriptionRepository repo;


    @Override
    public Map<String, EndpointServiceOut> retrieveEndpointsPerService() {
        return repo.findAll()
                .stream()
                .collect(Collectors
                        .toMap(ServiceDescription::getServiceName, el -> new EndpointServiceOut(el.getEndpointOpen(), el.getEndpointlegacy())));
    }

    @Override
    public EndpointServiceOut retrieveEndpointsPerService(String serviceName) throws ServiceNotFoundException {
        ServiceDescription desc = repo.findByServiceName(serviceName);
        if (desc == null)
            throw new ServiceNotFoundException("Endpoint not found for service: " + serviceName);
        return new EndpointServiceOut(desc.getEndpointOpen(), desc.getEndpointlegacy());
    }
}
