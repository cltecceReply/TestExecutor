package com.reply.batch.io;

import com.reply.io.dto.TERecord;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class RecordFieldSetMapper implements FieldSetMapper<TERecord> {
    @Override
    public TERecord mapFieldSet(FieldSet fieldSet){
        return new TERecord(fieldSet.readLong(0), fieldSet.readString(1), fieldSet.readString(2));
    }
}
