package base.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.command.IngredientAdd;
import base.domain.Drink;
import base.domain.Ingredient;
import base.dto.IngredientDto;
import base.repository.DrinkRepository;
import base.repository.IngredientRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class IngredientControllerTest {

    private static final String PATH = "/api/ingredients";
    private static final String INGREDIENT1 = "Tonic vesi";
    private static final String INGREDIENT2 = "Vodka";
    private static final String INGREDIENT3 = "Bourbon";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IngredientRepository ingredientRepository;
    
    @Autowired
    private DrinkRepository drinkRepository;
    
    private MockMvc mockMvc;
    private long id1 = 0, id2 = 0;
    private Ingredient i1, i2;
    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        i1 = new Ingredient(INGREDIENT1);
        i1 = ingredientRepository.saveAndFlush(i1);
        id1 = i1.getId();
        i2 = new Ingredient(INGREDIENT2);
        i2 = ingredientRepository.saveAndFlush(i2);
        id2 = i2.getId();
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void listIngredientsResponseStatusOKandContentTypeJsonUtf8() throws Exception {
        mockMvc
            .perform(get(PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void listMustNotBeEmpty() throws Exception {
        MvcResult res = mockMvc
                .perform(get(PATH))
                .andReturn();
        String content = res.getResponse().getContentAsString();
        assertFalse("Project list must not be empty.", content.equals("[]"));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void addingIngredientReturnsLocationHeaderAndDto() throws Exception {
        IngredientAdd ingredient = new IngredientAdd(INGREDIENT3);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(ingredient);
        MvcResult result = mockMvc
                .perform(
                        post(PATH)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(PATH + "/")))
                .andReturn();
        IngredientDto dto = mapper.readValue(result.getResponse().getContentAsString(), IngredientDto.class);
        assertTrue("ingredient name not correct", dto.getName().equals(INGREDIENT3));
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void addingIngredientWithoutNameFails() throws Exception {
        IngredientAdd ingredient = new IngredientAdd("");
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(ingredient);
         mockMvc
                .perform(
                        post(PATH)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void fetchSingleIngredient() throws Exception {
        MvcResult result = mockMvc
                .perform(get(PATH + "/" + id1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        assertFalse("Contents cannot be empty!", result.getResponse().getContentAsString().isEmpty());
        ObjectMapper mapper = new ObjectMapper();
        IngredientDto dto = mapper.readValue(result.getResponse().getContentAsString(), IngredientDto.class);
        assertTrue(dto.getName().equals(INGREDIENT1));
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void fetchSingleIngredientWithWrongIdFails404() throws Exception {
        mockMvc
                .perform(get(PATH + "/" + 751130))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void deleteIngredientOK() throws Exception {
        mockMvc.perform(
                delete(PATH + "/" + id2))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void deleteIngredientWithWrongIdFails404() throws Exception {
        mockMvc.perform(
                delete(PATH + "/" + 751130))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyIngredientOKandReturnsChangedContent() throws Exception {
        IngredientAdd ingAdd = new IngredientAdd("Spring water");
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(ingAdd);
        MvcResult result = mockMvc
                .perform(
                        put(PATH + "/" + id1)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(content))
                .andExpect(status().isOk())
                .andReturn();
        IngredientDto dto = mapper.readValue(result.getResponse().getContentAsString(), IngredientDto.class);
        assertTrue(dto.getName().equals("Spring water"));
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingIngredientWithWrongIdFails404() throws Exception {
        IngredientAdd ingAdd = new IngredientAdd("Spring water");
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(ingAdd);
        mockMvc
            .perform(
                put(PATH + "/" + 751130)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(content))
            .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingIngredientWithEmptyContentsFails() throws Exception {
        IngredientAdd ingAdd = new IngredientAdd("");
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(ingAdd);
        mockMvc
            .perform(
                put(PATH + "/" + id1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(content))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void cantDeleteIngredientIfInAdrink() throws Exception {
        Drink drink = new Drink("Vodka Tonic");
        drink.addIngredient(i1, "6 cl");
        drink.addIngredient(i2, "6 cl");
        drinkRepository.saveAndFlush(drink);
        mockMvc.perform(
                delete(PATH + "/" + id2))
                .andExpect(status().isConflict());
    }
}
