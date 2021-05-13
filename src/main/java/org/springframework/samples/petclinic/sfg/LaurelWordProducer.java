package org.springframework.samples.petclinic.sfg;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("luarel")
@Component
public class LaurelWordProducer implements WordProducer{
    @Override
    public String getWord() {
        return "Luarel";
    }
}
