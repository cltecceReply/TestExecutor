package com.reply.batch.io;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
public class TestResultsItemWriter implements ItemWriter<TestResultRecord> {

    FileWriter writerExp;
    FileWriter writerAct;

    protected String basePath;

    public TestResultsItemWriter(String basePath) {
        this.basePath = basePath;
    }

    @BeforeStep
    public void initWriters() throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("ddMMyy_HHmmss");
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        log.info("Inititalizing writers with {} suffix", dateStr);
        writerExp = new FileWriter(basePath+"expected_" + dateStr);
        writerAct = new FileWriter(basePath+"actual_" + dateStr);

    }

    @AfterStep
    public void closeWriters() throws IOException {
        log.info("Closing writers...");
        if(writerAct != null )
            writerAct.close();
        if(writerExp != null )
            writerExp.close();
    }

    @Override
    public void write(List<? extends TestResultRecord> list) throws Exception {
        for(TestResultRecord rec : list){
            writerAct.write(rec.getActual());
            writerExp.write(rec.getExpected());
        }
    }

}
