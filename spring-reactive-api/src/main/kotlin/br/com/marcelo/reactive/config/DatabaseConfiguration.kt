package br.com.marcelo.reactive.config

import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.pool.PoolingConnectionFactoryProvider.INITIAL_SIZE
import io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.PROTOCOL
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class DatabaseConfiguration(
    @Value("\${spring.r2dbc.host}")
    private val host: String,
    @Value("\${spring.r2dbc.database}")
    private val database: String,
    @Value("\${spring.r2dbc.username}")
    private val username: String,
    @Value("\${spring.r2dbc.password}")
    private val password: String,
    @Value("\${spring.r2dbc.initial-pool-size}")
    private val initialPoolSize: Int,
    @Value("\${spring.r2dbc.maximum-pool-size}")
    private val maxPoolSize: Int,
) {

    companion object {
        val LOGGER = LoggerFactory.getLogger(DatabaseConfiguration::class.java)
    }

    @Bean
    @Qualifier("customConnectionFactory")
    fun connectionFactory(): ConnectionFactory {

        LOGGER.info("initial pool size: {}", initialPoolSize)
        LOGGER.info("max pool size: {}", maxPoolSize)

        return ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(DRIVER, "pool")
                .option(PROTOCOL, "postgresql")
                .option(HOST, host)
                .option(PORT, 5432)
                .option(USER, username)
                .option(PASSWORD, password)
                .option(DATABASE, database)
                .option(INITIAL_SIZE, initialPoolSize)
                .option(MAX_SIZE, maxPoolSize)
                .build()
        )
    }

    @Bean
    fun databaseClient(
        connectionFactory: ConnectionFactory
    ): DatabaseClient {
        return DatabaseClient.create(connectionFactory)
    }

}
