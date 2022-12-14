package com.springbatch.config;

import com.springbatch.domain.Customer;
import com.springbatch.processor.CustomerProcessor;
import com.springbatch.repositories.CustomerRepository;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilder jobBuilder;

    @Autowired
    public StepBuilder stepBuilder;

    @Autowired
    @Lazy
    public CustomerRepository customerRepository;

    @Bean
    public FlatFileItemReader<Customer> reader(){
        return new FlatFileItemReaderBuilder<Customer>()
                .resource(new ClassPathResource("data.csv"))
                .delimited()
                .names(new String[]{"firstName","lastName"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>(){{
                    setTargetType(Customer.class);
                }})
                .build();
    }

    @Bean
    public RepositoryItemWriter writer(){
        RepositoryItemWriter<Customer> itemWriter=new RepositoryItemWriter<>();
        itemWriter.setRepository(customerRepository);
        itemWriter.setMethodName("save");
        return itemWriter;
    }

    @Bean
    public CustomerProcessor processor(){
        return new CustomerProcessor();
    }
    @Bean
    public Step step1(ItemReader<Customer> itemReader, ItemWriter<Customer> itemWriter) throws Exception{
        return this.stepBuilder
                .<Customer,Customer>chunk(5)
                .reader(itemReader)
                .processor(processor())
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Job customerUpdateJob(JobCompletionNotificationListener listener, Step step1) throws  Exception{
        return this.jobBuilder
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1)
                .build();
    }
}
