package com.springbatch.processor;

import com.springbatch.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;


public class CustomerProcessor implements ItemProcessor<Customer,Customer> {

    private static final Logger logger= LoggerFactory.getLogger(CustomerProcessor.class);
    @Override
    public Customer process(Customer customer) throws Exception {
        final String firstName=customer.getFirstName().toUpperCase();
        final String lastName=customer.getLastName().toUpperCase();
        final Customer transformedCustomer=new Customer(1L,firstName,lastName);
        logger.info("Converting ( "+customer+" ) into ( "+transformedCustomer+" )");
        return transformedCustomer;
    }
}
