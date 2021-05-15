package org.springframework.samples.petclinic.sfg.juni5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.samples.petclinic.sfg.LaurelWordProducer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("component-scan")
@SpringJUnitConfig(classes = HearingInterpreterComponentScanTest.TestConfig.class)
class HearingInterpreterComponentScanTest {

    @Profile("component-scan")
    @Configuration
    @ComponentScan("org.springframework.samples.petclinic.sfg")
    static class TestConfig{
        @Bean
        HearingInterpreter hearingInterpreter(){
            return new HearingInterpreter(new LaurelWordProducer());
        }
    }
    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void whatIIhead() {
        String word=hearingInterpreter.whatIIhead();
        assertEquals("Luarel",word);
    }
}