package io.vertigo.connectors.neo4j;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.core.node.component.Connector;
import io.vertigo.core.param.ParamValue;

public class Neo4JConnector implements Connector, Activeable {

	private final Driver neo4jDriver;
	private final String connectionName;

	@Inject
	public Neo4JConnector(
			@ParamValue("uri") final String uri,
			@ParamValue("login") final String login,
			@ParamValue("password") final String password,
			@ParamValue("name") final Optional<String> connectionNameOpt,
			@ParamValue("connectionTimeout") final Optional<Long> connectionTimeoutOpt,
			@ParamValue("connectionLivenessCheckTimeout") final Optional<Long> connectionLivenessCheckTimeoutOpt,
			@ParamValue("connectionAcquisitionTimeout") final Optional<Long> connectionAcquisitionTimeoutOpt,
			@ParamValue("connectionPoolSize") final Optional<Integer> connectionPoolSizeOpt) {
		Assertion.checkArgNotEmpty(uri);
		Assertion.checkArgNotEmpty(login);
		Assertion.checkArgNotEmpty(password);
		//---
		connectionName = connectionNameOpt.orElse("main");
		//---
		final Driver myNeo4jDriver = GraphDatabase.driver(
				uri,
				AuthTokens.basic(login, password),
				getConfig(connectionTimeoutOpt, connectionLivenessCheckTimeoutOpt, connectionAcquisitionTimeoutOpt, connectionPoolSizeOpt));
		// ---
		neo4jDriver = myNeo4jDriver;
	}

	protected Config getConfig(final Optional<Long> connectionTimeoutOpt,
			final Optional<Long> connectionLivenessCheckTimeoutOpt,
			final Optional<Long> connectionAcquisitionTimeoutOpt,
			final Optional<Integer> connectionPoolSizeOpt) {
		return Config.build()
				.withConnectionTimeout(connectionTimeoutOpt.orElse(1L), TimeUnit.MINUTES)
				.withConnectionLivenessCheckTimeout(connectionLivenessCheckTimeoutOpt.orElse(10L), TimeUnit.MINUTES)
				.withConnectionAcquisitionTimeout(connectionAcquisitionTimeoutOpt.orElse(1L), TimeUnit.MINUTES)
				.withMaxConnectionPoolSize(connectionPoolSizeOpt.orElse(100))
				.toConfig();
	}

	public Driver getNeo4jDriver() {
		return neo4jDriver;
	}

	@Override
	public void start() {
		// nothing to do

	}

	@Override
	public void stop() {
		neo4jDriver.close();

	}

	@Override
	public String getName() {
		return connectionName;
	}

}
