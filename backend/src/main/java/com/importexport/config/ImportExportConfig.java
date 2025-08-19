package com.importexport.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.import-export")
public class ImportExportConfig {

    private Performance performance = new Performance();
    private Security security = new Security();
    private Validation validation = new Validation();
    private Features features = new Features();

    public static class Performance {
        private int defaultBatchSize = 100;
        private int maxBatchSize = 1000;
        private int threadPoolSize = 4;
        private long maxProcessingTimeMs = 300000; // 5 minutes
        private boolean enableParallelProcessing = true;

        // Getters and Setters
        public int getDefaultBatchSize() { return defaultBatchSize; }
        public void setDefaultBatchSize(int defaultBatchSize) { this.defaultBatchSize = defaultBatchSize; }

        public int getMaxBatchSize() { return maxBatchSize; }
        public void setMaxBatchSize(int maxBatchSize) { this.maxBatchSize = maxBatchSize; }

        public int getThreadPoolSize() { return threadPoolSize; }
        public void setThreadPoolSize(int threadPoolSize) { this.threadPoolSize = threadPoolSize; }

        public long getMaxProcessingTimeMs() { return maxProcessingTimeMs; }
        public void setMaxProcessingTimeMs(long maxProcessingTimeMs) { this.maxProcessingTimeMs = maxProcessingTimeMs; }

        public boolean isEnableParallelProcessing() { return enableParallelProcessing; }
        public void setEnableParallelProcessing(boolean enableParallelProcessing) { this.enableParallelProcessing = enableParallelProcessing; }
    }

    public static class Security {
        private boolean enableInputSanitization = true;
        private boolean enableXssProtection = true;
        private boolean enableInjectionProtection = true;
        private int maxStringLength = 10000;

        // Getters and Setters
        public boolean isEnableInputSanitization() { return enableInputSanitization; }
        public void setEnableInputSanitization(boolean enableInputSanitization) { this.enableInputSanitization = enableInputSanitization; }

        public boolean isEnableXssProtection() { return enableXssProtection; }
        public void setEnableXssProtection(boolean enableXssProtection) { this.enableXssProtection = enableXssProtection; }

        public boolean isEnableInjectionProtection() { return enableInjectionProtection; }
        public void setEnableInjectionProtection(boolean enableInjectionProtection) { this.enableInjectionProtection = enableInjectionProtection; }

        public int getMaxStringLength() { return maxStringLength; }
        public void setMaxStringLength(int maxStringLength) { this.maxStringLength = maxStringLength; }
    }

    public static class Validation {
        private boolean enableStrictValidation = true;
        private boolean enableBusinessRuleValidation = true;
        private boolean enableDuplicateChecks = true;
        private boolean allowPartialImports = true;

        // Getters and Setters
        public boolean isEnableStrictValidation() { return enableStrictValidation; }
        public void setEnableStrictValidation(boolean enableStrictValidation) { this.enableStrictValidation = enableStrictValidation; }

        public boolean isEnableBusinessRuleValidation() { return enableBusinessRuleValidation; }
        public void setEnableBusinessRuleValidation(boolean enableBusinessRuleValidation) { this.enableBusinessRuleValidation = enableBusinessRuleValidation; }

        public boolean isEnableDuplicateChecks() { return enableDuplicateChecks; }
        public void setEnableDuplicateChecks(boolean enableDuplicateChecks) { this.enableDuplicateChecks = enableDuplicateChecks; }

        public boolean isAllowPartialImports() { return allowPartialImports; }
        public void setAllowPartialImports(boolean allowPartialImports) { this.allowPartialImports = allowPartialImports; }
    }

    public static class Features {
        private boolean enableProgressTracking = true;
        private boolean enableAuditLogging = true;
        private boolean enableDataTransformation = true;
        private boolean enableAsyncProcessing = true;

        // Getters and Setters
        public boolean isEnableProgressTracking() { return enableProgressTracking; }
        public void setEnableProgressTracking(boolean enableProgressTracking) { this.enableProgressTracking = enableProgressTracking; }

        public boolean isEnableAuditLogging() { return enableAuditLogging; }
        public void setEnableAuditLogging(boolean enableAuditLogging) { this.enableAuditLogging = enableAuditLogging; }

        public boolean isEnableDataTransformation() { return enableDataTransformation; }
        public void setEnableDataTransformation(boolean enableDataTransformation) { this.enableDataTransformation = enableDataTransformation; }

        public boolean isEnableAsyncProcessing() { return enableAsyncProcessing; }
        public void setEnableAsyncProcessing(boolean enableAsyncProcessing) { this.enableAsyncProcessing = enableAsyncProcessing; }
    }

    // Main getters and setters
    public Performance getPerformance() { return performance; }
    public void setPerformance(Performance performance) { this.performance = performance; }

    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }

    public Validation getValidation() { return validation; }
    public void setValidation(Validation validation) { this.validation = validation; }

    public Features getFeatures() { return features; }
    public void setFeatures(Features features) { this.features = features; }
}