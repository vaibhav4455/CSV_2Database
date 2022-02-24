package com.moglix.csv.CSV2Database.config;

import com.moglix.csv.CSV2Database.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<User> reader(){

        FlatFileItemReader<User>  reader=new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("ABFRL_ASN_ReportV2.csv"));
        reader.setLineMapper(getLineMapper());
        reader.setLinesToSkip(1);
        return reader;
    }
    @Bean
    public LineMapper<User> getLineMapper() {
         DefaultLineMapper<User> lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenize=new DelimitedLineTokenizer();
        lineTokenize.setNames(new String[]{ "Vendor_ID","asn_Quantity"});
        lineTokenize.setIncludedFields(new int[]{ 0,11});

        BeanWrapperFieldSetMapper<User> fieldSetMapper=  new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);
         lineMapper.setLineTokenizer(lineTokenize);
         lineMapper.setFieldSetMapper(fieldSetMapper);
         return lineMapper;
    }
    @Bean
    public UserItemProcessor processor(){

        return new UserItemProcessor();
    }
    @Bean
    public JdbcBatchItemWriter<User> writer(){

        JdbcBatchItemWriter<User> writer=new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
        writer.setSql("insert into asn_table(Vendor_ID,asn_Quantity) values (:Vendor_ID,:asn_Quantity)");
        writer.setDataSource(this.dataSource);
        return writer;
    }
    @Bean
    public Job importUserJob(){
        return this.jobBuilderFactory.get("USER-IMPORT-JOB")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }
    @Bean
    public Step step1() {
       return  this.stepBuilderFactory.get("step1")
                .<User,User>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();

    }
}
