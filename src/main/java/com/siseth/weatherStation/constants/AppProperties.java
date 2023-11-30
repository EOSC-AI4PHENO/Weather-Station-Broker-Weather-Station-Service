package com.siseth.weatherStation.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = true)
@PropertySources({
        @PropertySource(value = "classpath:git.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:META-INF/build-info.properties", ignoreResourceNotFound = true)
})
@Getter
@Setter
public final class AppProperties {

    private boolean migration;

    private boolean reinstall;

    private boolean repair;

}
