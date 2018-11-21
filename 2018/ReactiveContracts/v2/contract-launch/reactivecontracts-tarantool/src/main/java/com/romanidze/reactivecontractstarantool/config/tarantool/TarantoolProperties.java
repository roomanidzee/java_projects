package com.romanidze.reactivecontractstarantool.config.tarantool;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 17.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Component
@ConfigurationProperties(prefix = "tarantool")
public class TarantoolProperties {

    private String host;
    private Integer port;

    private Integer spaceID;
    private String username;
    private String password;

    @Builder.Default
    private Integer defaultRequestSize = 4096;

    @Builder.Default
    private Integer predictedFutures = (int) ((1024 * 1024) / 0.75) + 1;

    @Builder.Default
    private Integer writerThreadPriority = Thread.NORM_PRIORITY;

    @Builder.Default
    private Integer readerThreadPriority = Thread.NORM_PRIORITY;

    @Builder.Default
    private Integer sharedBufferSize = 8 * 1024 * 1024;

    @Builder.Default
    private Double directWriteFactor = 0.5d;

    @Builder.Default
    private Boolean useNewCall = false;

    @Builder.Default
    private Long initTimeoutMillis = 60*1000L;

    @Builder.Default
    private Long writeTimeoutMillis = 60*1000L;

}
