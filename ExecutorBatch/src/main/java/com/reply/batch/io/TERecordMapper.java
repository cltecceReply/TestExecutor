package com.reply.batch.io;

import com.reply.io.dto.TERecord;
import org.springframework.batch.item.file.LineMapper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TERecordMapper implements LineMapper<TERecord> {

    private static final Pattern pattern = Pattern.compile("^([0-9]+);([A-Za-z0-9]+);(.*)$");

    @Override
    public TERecord mapLine(String s, int i) throws Exception {
        Matcher matcher = pattern.matcher(s);
        TERecord TERecord = new TERecord();
        if (matcher.find()) {
            TERecord.setTestId(Integer.parseInt(matcher.group(1)));
            TERecord.setServiceName(matcher.group(2));
            TERecord.setRequest(matcher.group(3));
        } else {
            throw new IOException("Line " + i + " - Invalid Sintax for record: " + s);
        }
        return TERecord;
    }
}
