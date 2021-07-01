package com.reply.services.endpoint;


import com.reply.model.EndpointServiceOut;

import javax.management.ServiceNotFoundException;
import java.util.Map;

public interface IEndpointRetrievalService {

    Map<String, EndpointServiceOut> retrieveEndpointsPerService();

    /***
     * Return the endpoints of the services being compared in the form:
     *  (actual, target)
     * @param serviceName
     * @return Tuple (Actual, Target)
     */
    EndpointServiceOut retrieveEndpointsPerService(String serviceName) throws ServiceNotFoundException;
}
