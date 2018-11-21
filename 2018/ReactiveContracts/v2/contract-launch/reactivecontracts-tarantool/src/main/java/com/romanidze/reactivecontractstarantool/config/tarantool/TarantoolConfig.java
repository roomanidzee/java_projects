package com.romanidze.reactivecontractstarantool.config.tarantool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.tarantool.SocketChannelProvider;
import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClientConfig;
import org.tarantool.TarantoolClientImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * 17.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Configuration
public class TarantoolConfig {

    private static final Logger logger = LogManager.getLogger(TarantoolConfig.class);

    private final TarantoolProperties tarantoolProperties;

    @Autowired
    public TarantoolConfig(TarantoolProperties tarantoolProperties) {
        this.tarantoolProperties = tarantoolProperties;
    }

    @Bean
    public TarantoolClientConfig tarantoolClientConfig(){

        TarantoolClientConfig config = new TarantoolClientConfig();

        config.username = this.tarantoolProperties.getUsername();
        config.password = this.tarantoolProperties.getPassword();
        config.defaultRequestSize = this.tarantoolProperties.getDefaultRequestSize();
        config.predictedFutures = this.tarantoolProperties.getPredictedFutures();
        config.writerThreadPriority = this.tarantoolProperties.getWriterThreadPriority();
        config.readerThreadPriority = this.tarantoolProperties.getReaderThreadPriority();
        config.sharedBufferSize = this.tarantoolProperties.getSharedBufferSize();
        config.directWriteFactor = this.tarantoolProperties.getDirectWriteFactor();
        config.useNewCall = this.tarantoolProperties.getUseNewCall();
        config.initTimeoutMillis = this.tarantoolProperties.getInitTimeoutMillis();
        config.writeTimeoutMillis = this.tarantoolProperties.getWriteTimeoutMillis();

        return config;

    }

    @Bean
    public SocketChannelProvider socketChannelProvider(){

        logger.info("HOST: {}", this.tarantoolProperties.getHost());

        return (retryNumber, lastError) -> {
            if (lastError != null) {

                logger.error(lastError);
                lastError.printStackTrace(System.out);

            }
            try {
                return SocketChannel.open(new InetSocketAddress(this.tarantoolProperties.getHost(), this.tarantoolProperties.getPort()));
            } catch (IOException e) {
                logger.error("error happened: {}", e);
                throw new IllegalStateException(e);
            }
        };

    }

    @Bean
    public TarantoolClient tarantoolClient(){
        return new TarantoolClientImpl(socketChannelProvider(), tarantoolClientConfig());
    }

}
