package com.jatin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;

@Configuration
public class DebeziumConnectorConfig {
    @Bean
    public io.debezium.config.Configuration customerConnector(Environment env) throws IOException {
        var offsetStorageTempFile = File.createTempFile("offsets_", ".dat");
        return io.debezium.config.Configuration.create()
                .with("name", "customer_postgres_connector")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", offsetStorageTempFile.getAbsolutePath())
                .with("offset.flush.interval.ms", "60000")
                .with("database.hostname", "localhost")
                .with("database.port", 5432) // defaults to 5432
                .with("database.user", "docker")
                .with("database.password", "docker")
                .with("database.dbname", "debezium_master")
                .with("database.server.id", "10181")
                .with("database.server.name", "customer-postgres-db-server")
                .with("topic.prefix", "slave-postgresql")
                .with("database.history", "io.debezium.relational.history.MemoryDatabaseHistory")
//            .with("table.include.list", "public.claims")
//            .with("column.include.list", "public.customer.email,public.customer.fullname")
                .with("publication.autocreate.mode", "all_tables")
                .with("plugin.name", "pgoutput")
                .with("slot.name", "dbz_customerdb_listener")
                .build();
    }
}