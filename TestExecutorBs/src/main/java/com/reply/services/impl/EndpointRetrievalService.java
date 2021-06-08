package com.reply.services.impl;

import com.reply.services.IEndpointRetrievalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EndpointRetrievalService implements IEndpointRetrievalService {

    Map<String, Pair<String, String>> endpointsMapping;

    public EndpointRetrievalService(@Value("#{${services.endpoints:null}}") Map<String, List<String>> endpoints){
        endpointsMapping = new HashMap<>();
        if(endpoints != null){
            log.info("Endpoints Loaded: {}", endpoints);
            for(Map.Entry<String, List<String>> entry: endpoints.entrySet()) {
                endpointsMapping.put(entry.getKey(), new ImmutablePair<>(entry.getValue().get(0), entry.getValue().get(0)));
            }
        }
    }

    @Override
    public Map<String, Pair<String, String>> retrieveEndpointsPerService() {
        return endpointsMapping;
    }

    @Override
    public Pair<String, String> retrieveEndpointsPerService(String serviceName) throws ServiceNotFoundException {
        if(!endpointsMapping.containsKey(serviceName))
            throw new ServiceNotFoundException("Endpoint not found for service: "+serviceName);
        return endpointsMapping.get(serviceName);
    }
}
