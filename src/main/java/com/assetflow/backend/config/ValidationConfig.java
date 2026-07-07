package com.assetflow.backend.config;

import jakarta.validation.ClockProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ValidationConfig {

    @Bean
    public ClockProvider businessClockProvider(
            @Value("${app.business-zone:Europe/Stockholm}") String businessZone) {

        ZoneId zoneId = ZoneId.of(businessZone);

        return () -> Clock.system(zoneId);
    }

    @Bean
    public ValidationConfigurationCustomizer validationConfigurationCustomizer(
            ClockProvider businessClockProvider) {

        return configuration -> configuration.clockProvider(businessClockProvider);
    }
}
