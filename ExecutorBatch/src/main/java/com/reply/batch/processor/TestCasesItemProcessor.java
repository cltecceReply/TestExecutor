package com.reply.batch.processor;

import com.reply.batch.io.TestResultRecord;
import com.reply.services.TestCaseProcessor;
import com.reply.services.TestCaseProcessorOut;
import com.reply.io.dto.TERecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TestCasesItemProcessor implements ItemProcessor<TERecord, TestResultRecord> {

    @Autowired
    TestCaseProcessor processor;

    @AfterStep
    public void printReport(){
      log.info(processor.getReport());
    }

    @Override
    public TestResultRecord process(TERecord teRecord) throws Exception {
        TestResultRecord record = new TestResultRecord();
        TestCaseProcessorOut out = processor.executeTestCase(teRecord);
        BeanUtils.copyProperties(out, record);
        return record;
    }
}
