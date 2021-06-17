package com.reply.services.writes;

import com.reply.io.model.DbOperation;

import java.util.List;

public interface IIOProviderService {
    List<DbOperation> getAndFlush();
}
