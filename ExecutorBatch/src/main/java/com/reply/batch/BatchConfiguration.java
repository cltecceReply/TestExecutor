package com.reply.batch;

import com.reply.batch.io.TestResultRecord;
import com.reply.batch.io.TestResultsItemWriter;
import com.reply.batch.processor.TestCasesItemProcessor;
import com.reply.io.dto.TERecord;
import com.reply.repo.entity.TestCase;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j
@EnableBatchProcessing
@EnableJpaRepositories("com.reply.repo.*")
@EnableTransactionManagement
@Configuration
@ComponentScan(basePackages = "com.reply")
@PropertySource("classpath:application.properties")
public class BatchConfiguration {

    @Value("${file.input}")
    protected String inputPath;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private TestCasesItemProcessor testCaseProcessor;

    @Autowired
    HikariDataSource dataSource;

//    @Bean
//    public ItemReader<TERecord> testCasesItemReader() {
//        log.debug("Input Path:  {}", inputPath);
//        FlatFileItemReader<TERecord> reader = new FlatFileItemReader<>();
//        reader.setResource(new FileSystemResource(inputPath));
//        reader.setLineMapper(new TERecordMapper());
//        return reader;
//    }

    @Bean
    public JdbcCursorItemReader<TERecord> testCasesItemReader(@Value("${services.name:null}") String service_name) {
        String query = "select TEST_ID, TEST_DATA, SERVICE_NAME, TEST_WRITE from DB2INST1.TEST_CASES";
        if(!"null".equals(service_name))
            query += String.format(" WHERE SERVICE_NAME= '%s'", service_name);
        query += " ORDER BY TEST_ID asc";
        return new JdbcCursorItemReaderBuilder<TERecord>()
                .dataSource(this.dataSource)
                .name("testCaseReader")
                .sql(query)
                .rowMapper((rs, i) ->
                {
                    TERecord testRecord = new TERecord();
                    testRecord.setTestId(rs.getLong("TEST_ID"));
                    testRecord.setRequest(rs.getString("TEST_DATA"));
                    testRecord.setServiceName(rs.getString("SERVICE_NAME"));
                    testRecord.setWrite("Y".equals(rs.getString("TEST_WRITE")));
                    return testRecord;
                })
                .build();

    }

    @Bean
    ItemWriter<TestResultRecord> testResultRecordItemWriter(@Value("${file.output}") String outputPath) {
        return new TestResultsItemWriter(outputPath);
    }

    @Bean
    protected Job job(Step testExecutionStep){
        return jobs.get("TestExecutionBatch")
                .incrementer(new RunIdIncrementer())
                .start(testExecutionStep)
                .build();

    }
    @Bean
//    protected Step stepDefinition(ItemReader<TERecord> testCasesItemReader,
    protected Step stepDefinition(JdbcCursorItemReader<TERecord> testCasesItemReader,
                                  ItemProcessor<TERecord, TestResultRecord> testCaseProcessor,
                                  ItemWriter<TestResultRecord> testResultRecordItemWriter) {
        return steps.get("TEST_EXECUTION")
                .<TERecord, TestResultRecord> chunk(100)
                .reader(testCasesItemReader)
                .processor(testCaseProcessor)
                .writer(testResultRecordItemWriter)
                .allowStartIfComplete(true)
                .build();
    }

}
