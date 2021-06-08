package com.reply.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface IWsInvocator {

    CompletableFuture<String> invokeService(String endpoint, String xmlSoapRequest);
}
