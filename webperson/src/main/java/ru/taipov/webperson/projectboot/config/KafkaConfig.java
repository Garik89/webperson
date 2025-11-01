package ru.taipov.webperson.projectboot.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic personCreatedTopic() {
        return TopicBuilder.name("person-created")
                .partitions(3)
                .replicas(3)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000")
                .config(TopicConfig.RETENTION_BYTES_CONFIG, "524288000")
                .build();
    }

    @Bean
    public NewTopic personDeletedTopic() {
        return TopicBuilder.name("person-deleted")
                .partitions(3)
                .replicas(3)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000")
                .config(TopicConfig.RETENTION_BYTES_CONFIG, "524288000")
                .build();
    }
}
