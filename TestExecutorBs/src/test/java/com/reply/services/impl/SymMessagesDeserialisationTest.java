package com.reply.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.reply.io.model.DbOperation;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SymMessagesDeserialisationTest extends IOProviderService{

    @Override
    protected InjectableValues injectEventType() {
        return new InjectableValues.Std()
                .addValue("topic", "T_GES_DOCU");
    }

    @Test
    public void symMessagesDeserialisationTest() throws JsonProcessingException {
        String json = "{\"T_GES_DOCU\": {\"eventType\": \"INSERT\",\"data\": { \"DOCU_NUM_DOC_SE\": \"ZX\",\"DOCU_NUM_DOC\": \"  \",\"DOCU_DES_DOC\": \"                                                  \",\"DOCU_QTA_DOC\": \"0\",\"DOCU_TIT_DOC\": \"0\",\"DOCU_FLG_STAMPA\": \"0\",\"DOCU_COD_DOCUM\": \"   \",\"DOCU_FLG_OPZ\": \"0\" } } }{\"T_GES_DOCU\": {\"eventType\": \"DELETE\",\"data\": { \"DOCU_NUM_DOC_SE\": \"ZX\",\"DOCU_NUM_DOC\": \"  \",\"DOCU_DES_DOC\": \"                                                  \",\"DOCU_QTA_DOC\": \"0\",\"DOCU_TIT_DOC\": \"0\",\"DOCU_FLG_STAMPA\": \"0\",\"DOCU_COD_DOCUM\": \"   \",\"DOCU_FLG_OPZ\": \"0\" } } }";

        List<DbOperation> ops = this.deserializeSymDsMessages(json);
        Assert.assertEquals(2, ops.size());
    }

}
