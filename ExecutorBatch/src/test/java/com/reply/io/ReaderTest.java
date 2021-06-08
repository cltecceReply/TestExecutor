package com.reply.io;

import com.reply.batch.io.TERecordMapper;
import com.reply.io.dto.TERecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.test.context.SpringBatchTest;

@Slf4j
@SpringBatchTest
public class ReaderTest {

    @Test
    public void testRead() throws Exception {
        String line = "0;TEST;";
        String msg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gs=\"http://spring.io/guides/gs-producing-web-service\"> <soapenv:Header/> <soapenv:Body> <gs:getCountryRequest> <gs:name>Spain</gs:name> </gs:getCountryRequest> </soapenv:Body> </soapenv:Envelope>";
        TERecord record = new TERecordMapper().mapLine(line+msg, 0);
        Assert.assertEquals(0, record.getTestId());
        Assert.assertEquals("TEST", record.getServiceName());
        Assert.assertEquals(msg, record.getRequest());

    }
}
