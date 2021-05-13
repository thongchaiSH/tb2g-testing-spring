package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<String> argumentCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @AfterEach
    void tearDown() {
        reset(clinicService);
    }


    @Test
    void showOwnerTest() throws Exception {
        given(clinicService.findOwnerById(anyInt())).willReturn(new Owner());

        mockMvc.perform(get("/owners/{ownerId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"));

        then(clinicService).should().findOwnerById(anyInt());

    }

    @Test
    void initFindFormTest() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testEditOwnerPostValid() throws Exception {
        given(clinicService.findOwnerById(anyInt())).willReturn(new Owner());

        mockMvc.perform(get("/owners/{ownerId}/edit", 1))
                .andExpect(model().attributeExists())
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM));

        then(clinicService).should().findOwnerById(anyInt());
    }


    @Test
    void testUpdateOwnerPostNoValid() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", 1)
                .param("firstName", "Jane")
                .param("lastName", "Bane")
                .param("city", "BKK"))
                .andExpect(status().isOk())
                .andExpect(view().name(OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
    }

    @Test
    void testUpdateOwnerPostValid() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", 1)
                .param("firstName", "Jane")
                .param("lastName", "Bane")
                .param("address", "1234 Rose")
                .param("city", "BKK")
                .param("telephone", "1234123123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownerId}"));

        then(clinicService).should().saveOwner(any(Owner.class));
    }

    @Test
    void testNewOwnerPostValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                .param("firstName", "Jane")
                .param("lastName", "Bane")
                .param("address", "1234 Rose")
                .param("city", "BKK")
                .param("telephone", "1234123123"))
                .andExpect(status().is3xxRedirection());

        then(clinicService).should().saveOwner(any(Owner.class));
    }

    @Test
    void testNewOwnerPostNotValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                .param("firstName", "Jane")
                .param("lastName", "Bane")
                .param("city", "BKK"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "address"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(view().name(OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM));

    }

    @Test
    void testFindOwnerOneResult() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        String findOne = "FindOne";

        given(clinicService.findOwnerByLastName(findOne)).willReturn(Lists.newArrayList(owner));

        mockMvc.perform(get("/owners")
                .param("lastName", findOne))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + owner.getId()));

        then(clinicService).should().findOwnerByLastName(anyString());
    }

    @Test
    void testReturnListOfOwners() throws Exception {
        given(clinicService.findOwnerByLastName("")).willReturn(Lists.newArrayList(new Owner(), new Owner()));

        mockMvc.perform(get("/owners"))
//                .param("lastName", "Find Me!!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attributeExists("selections"));

        then(clinicService).should().findOwnerByLastName(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualToIgnoringCase("");
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                .param("lastName", "Dont find ME!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name(OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
    }
}