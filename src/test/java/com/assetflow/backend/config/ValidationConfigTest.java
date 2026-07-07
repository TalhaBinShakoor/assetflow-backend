package com.assetflow.backend.config;

import jakarta.validation.ClockProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(ValidationConfig.class);

    @Test
    void businessClockUsesConfiguredBusinessZone() {
        contextRunner
                .withPropertyValues("app.business-zone=Europe/Stockholm")
                .run(context -> {
                    ClockProvider clockProvider = context.getBean(ClockProvider.class);

                    assertEquals(
                            ZoneId.of("Europe/Stockholm"),
                            clockProvider.getClock().getZone()
                    );
                });
    }
}
