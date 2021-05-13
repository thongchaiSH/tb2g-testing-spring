package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    ClinicService clinicService;

    @InjectMocks
    VetController controller;

    List<Vet> vetListLi=new ArrayList<>();

    @Mock
    Map<String, Object> model;

    @BeforeEach
    void setUp() {
        Vet vet=new Vet();
        vetListLi.add(vet);

        given(clinicService.findVets()).willReturn(vetListLi);
    }

    @Test
    void showVetList() {
        //when
        String view=controller.showVetList(model);
        //then
        then(clinicService).should().findVets();
        then(model).should().put(anyString(),anyObject());
        assertThat(view).isEqualTo("vets/vetList");
    }

    @Test
    void showResourcesVetList() {
        //when
        Vets vets=controller.showResourcesVetList();
        //then
        then(clinicService).should().findVets();
        assertThat(vets.getVetList()).hasSize(1);
    }
}