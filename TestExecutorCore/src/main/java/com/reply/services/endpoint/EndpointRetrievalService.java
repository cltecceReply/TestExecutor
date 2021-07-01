package com.reply.services.endpoint;

import com.reply.model.EndpointServiceOut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EndpointRetrievalService implements IEndpointRetrievalService {

    Map<String, EndpointServiceOut> endpointsMapping;

    public EndpointRetrievalService(@Value("#{${services.endpoints:null}}") Map<String, List<String>> endpoints){
        endpointsMapping = new HashMap<>();
        if(endpoints != null){
            log.info("Endpoints Loaded: {}", endpoints);
            for(Map.Entry<String, List<String>> entry: endpoints.entrySet()) {
                endpointsMapping.put(entry.getKey(), new EndpointServiceOut(entry.getValue().get(0), entry.getValue().get(1)));
            }
        }
    }

    @Override
    public Map<String, EndpointServiceOut> retrieveEndpointsPerService() {
        return endpointsMapping;
    }

    @Override
    public EndpointServiceOut retrieveEndpointsPerService(String serviceName) throws ServiceNotFoundException {
        if(!endpointsMapping.containsKey(serviceName))
            throw new ServiceNotFoundException("Endpoint not found for service: "+serviceName);
        return endpointsMapping.get(serviceName);
    }
}
