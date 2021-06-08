package com.reply.services;


import org.apache.commons.lang3.tuple.Pair;

import javax.management.ServiceNotFoundException;
import java.util.Map;

public interface IEndpointRetrievalService {

    Map<String, Pair<String, String>> retrieveEndpointsPerService();

    /***
     * Return the endpoints of the services being compared in the form:
     *  (actual, target)
     * @param serviceName
     * @return Tuple (Actual, Target)
     */
    Pair<String, String> retrieveEndpointsPerService(String serviceName) throws ServiceNotFoundException;
}
