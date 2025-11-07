package com.spasbo.mlb.configuration;

import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.core.cql.keyspace.SpecificationBuilder;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Getter
@Configuration
@EnableCassandraRepositories(basePackages = "com.spasbo.mlb.repository.cassandra")
public class CassandraConfiguration extends AbstractCassandraConfiguration {

  @Value(value = "${spring.cassandra.port}")
  private int port;

  @Value(value = "${spring.cassandra.contact-points}")
  private String contactPoints;

  @Value(value = "${spring.cassandra.keyspace-name}")
  private String keyspaceName;

  @Value(value = "${spring.cassandra.local-datacenter}")
  private String localDataCenter;

  @Value(value = "${spring.cassandra.schema-action}")
  private SchemaAction schemaAction;

  @Override
  protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
    CreateKeyspaceSpecification specification = SpecificationBuilder.createKeyspace(keyspaceName)
        .with(KeyspaceOption.DURABLE_WRITES, true)
        .withSimpleReplication()
        .withNetworkReplication(DataCenterReplication.of(localDataCenter, 1))
        .ifNotExists();

    return List.of(specification);
  }

}
