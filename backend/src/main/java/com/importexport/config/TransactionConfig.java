package com.importexport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    // Configuration for transaction management is handled by Spring Boot auto-configuration
    // This class serves as explicit documentation that transactions are enabled
}