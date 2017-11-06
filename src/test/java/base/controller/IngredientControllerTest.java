package base.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import base.domain.Ingredient;
import base.repository.IngredientRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class IngredientControllerTest {

    private static final String PATH = "/api/ingredients";
    private static final String INGREDIENT1 = "Tonic vesi";
    private static final String INGREDIENT2 = "Vodka";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IngredientRepository ingredientRepository;
    
    private MockMvc mockMvc;
    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Ingredient i1 = new Ingredient(INGREDIENT1);
        i1 = ingredientRepository.saveAndFlush(i1);
        Ingredient i2 = new Ingredient(INGREDIENT2);
        i2 = ingredientRepository.saveAndFlush(i2);
    }
    
    @Test
    public void listProjectsResponseStatusOKandContentTypeJsonUtf8() throws Exception {
        mockMvc
            .perform(get(PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void listMustNotBeEmpty() throws Exception {
        MvcResult res = mockMvc
                .perform(get(PATH))
                .andReturn();
        String content = res.getResponse().getContentAsString();
        assertFalse("Project list must not be empty.", content.equals("[]"));
    }
}
