package com.reply.services.invoker;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface IWebServiceInvoker {

    CompletableFuture<String> invokeService(String endpoint, String xmlSoapRequest);
}
