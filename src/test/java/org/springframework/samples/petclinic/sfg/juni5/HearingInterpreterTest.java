package org.springframework.samples.petclinic.sfg.juni5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.sfg.BaseConfig;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.samples.petclinic.sfg.LaureConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {BaseConfig.class, LaureConfig.class})
class HearingInterpreterTest {

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void whatIIhead() {
        String word=hearingInterpreter.whatIIhead();
        assertEquals("Luarel",word);
    }
}