package com.reply.batch;

import com.reply.batch.io.TERecordMapper;
import com.reply.batch.io.TestResultRecord;
import com.reply.batch.io.TestResultsItemWriter;
import com.reply.batch.processor.TestCasesItemProcessor;
import com.reply.io.dto.TERecord;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
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
    @Value("${file.output}")
    protected String outputPath;

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
    public JdbcCursorItemReader<TERecord> testCasesItemReader() {
        return new JdbcCursorItemReaderBuilder<TERecord>()
                .dataSource(this.dataSource)
                .name("testCaseReader")
                .sql("select TEST_ID, TEST_DATA, SERVICE_NAME from DB2INST1.TEST_CASES")
                .rowMapper((rs, i) ->
                {
                    TERecord testRecord = new TERecord();
                    testRecord.setTestId(rs.getLong("TEST_ID"));
                    testRecord.setRequest(rs.getString("TEST_DATA"));
                    testRecord.setServiceName(rs.getString("SERVICE_NAME"));
                    return testRecord;
                })
                .build();

    }

    @Bean
    ItemWriter<TestResultRecord> testResultRecordItemWriter() {
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
