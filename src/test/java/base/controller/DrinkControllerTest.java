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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import base.command.DrinkAdd;
import base.domain.Category;
import base.domain.Drink;
import base.domain.Glass;
import base.domain.Ingredient;
import base.dto.CategoryDto;
import base.dto.DrinkComponent;
import base.dto.DrinkDto;
import base.dto.GlassDto;
import base.dto.IngredientDto;
import base.repository.CategoryRepository;
import base.repository.DrinkRepository;
import base.repository.GlassRepository;
import base.repository.IngredientRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DrinkControllerTest {

    private static final String PATH = "/api/drinks";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private GlassRepository glassRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DrinkRepository drinkRepository;

    private MockMvc mockMvc;
    private long id1 = 0, id2 = 0;
    private Drink drink1, drink2;
    private Category catClassic, catDrink;
    private Glass glassHighB, glassCocktail;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        if (ingredientRepository.findAll().isEmpty()) {
            String[] ings = { "Vodka", "Viski", "Limemehu", "Applesiinimehu", "Jaloviina" };
            insertIngredients(Arrays.asList(ings));
        }
        if (glassRepository.findAll().isEmpty()) {
            String[] glss = { "Boolimalja", "Cocktail", "Highball", "Hurricane", "Irish Coffee", "Kuohuviini",
                    "Margarita", "On the Rocks", "Shotti", "Valkoviini", "Punaviini" };
            insertGlassware(Arrays.asList(glss));
        }
        if (categoryRepository.findAll().isEmpty()) {
            String[] cats = { "Booli", "Drinkki", "Klassikko", "Kuumat", "Shot" };
            insertCategories(Arrays.asList(cats));
        }
        String[][] ing = { 
                { "Vodka", "3 cl", "Viski", "3 cl" },
                { "Vodka", "4 cl", "Limemehu", "6 cl"} };

        drink1 = insertDrink("Semi", "Drinkki", "On the Rocks", ing[0]);
        drink2 = insertDrink("Moscow mule", "Drinkki", "Highball", ing[1]);
        catClassic = categoryRepository.findByName("Klassikko");
        glassHighB = glassRepository.findByName("Highball");
        catDrink = categoryRepository.findByName("Drinkki");
        glassCocktail = glassRepository.findByName("Cocktail");
    }

    private void insertIngredients(List<String> ingredients) {
        for (String name : ingredients) {
            Ingredient ing = new Ingredient(name);
            ingredientRepository.saveAndFlush(ing);
        }
    }

    private void insertGlassware(List<String> glasses) {
        for (String name : glasses) {
            Glass glass = new Glass(name);
            glassRepository.saveAndFlush(glass);
        }
    }

    private void insertCategories(List<String> categories) {
        for (String name : categories) {
            Category cat = new Category(name);
            categoryRepository.saveAndFlush(cat);
        }
    }

    @After
    public void tearDown() throws Exception {
        drinkRepository.delete(drink1);
        drinkRepository.delete(drink2);
        List<Drink> list = drinkRepository.findAll();
        System.out.println("list size: " + list.size());
    }

    private Drink insertDrink(String name, String cat, String glss, String[] ingredients) {
        Category category = categoryRepository.findByName(cat);
        Glass glass = glassRepository.findByName(glss);
        Drink drink = new Drink(name);
        drink.setCategory(category);
        drink.setGlass(glass);
        for (int i = 0; i < ingredients.length; i += 2) {
            Ingredient ingredient = ingredientRepository.findByName(ingredients[i]);
            drink.addIngredient(ingredient, ingredients[i + 1]);
        }
        return drinkRepository.saveAndFlush(drink);
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void listResponseStatusOKandContentTypeJsonUtf8() throws Exception {
        mockMvc.perform(get(PATH)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void listMustNotBeEmpty() throws Exception {
        MvcResult res = mockMvc.perform(get(PATH)).andReturn();
        String content = res.getResponse().getContentAsString();
        assertFalse("Project list must not be empty.", content.equals("[]"));
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void addDrinkReturnsLocationHeaderAndDto() throws Exception {
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .name("drinkero")
                .category(new CategoryDto(catDrink.getId(), catDrink.getName()))
                .glass(new GlassDto(glassCocktail.getId(), glassCocktail.getName()))
                .addComponent(createDrinkComponent("Viski", "4 cl"))
                .addComponent(createDrinkComponent("Limemehu", "5 cl"))
                .addComponent(createDrinkComponent("Appelsiinimehu", "5 cl"))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        MvcResult result = mockMvc
                .perform(
                        post(PATH)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(PATH + "/")))
                .andReturn();
        DrinkDto dto = mapper.readValue(result.getResponse().getContentAsString(), DrinkDto.class);
        assertTrue("drink name not correct", dto.getName().equals("drinkero") );
     }
    
    private DrinkComponent createDrinkComponent(String name, String amount) {
        Ingredient i = ingredientRepository.findByName(name);
        IngredientDto dto = new IngredientDto(i.getId(), i.getName());
        DrinkComponent dc = new DrinkComponent(dto, amount);
        return dc;
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void addingDrinkWithoutNameFails() throws Exception {
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .category(new CategoryDto(catDrink.getId(), catDrink.getName()))
                .glass(new GlassDto(glassCocktail.getId(), glassCocktail.getName()))
                .addComponent(createDrinkComponent("Viski", "4 cl"))
                .addComponent(createDrinkComponent("Limemehu", "5 cl"))
                .addComponent(createDrinkComponent("Appelsiinimehu", "5 cl"))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        mockMvc
        .perform(
                post(PATH)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(content))
        .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void getSingleDrink() throws Exception {
        MvcResult result = mockMvc
                .perform(get(PATH + "/" + drink1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        assertFalse("Drink contents missing!", result.getResponse().getContentAsString().isEmpty());
        ObjectMapper mapper = new ObjectMapper();
        DrinkDto dto = mapper.readValue(result.getResponse().getContentAsString(), DrinkDto.class);
        assertTrue(dto.getName().equals(drink1.getName()));
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void fetchSingleDrinkWithWrongIdFails404() throws Exception {
        mockMvc
                .perform(get(PATH + "/" + 751130))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void deleteDrinkOK() throws Exception {
        mockMvc.perform(
                delete(PATH + "/" + drink2.getId()))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void deleteDrinkWithWrongIdFails404() throws Exception {
        mockMvc.perform(
                delete(PATH + "/" + 751130))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyDrinkOKandReturnsChangedContent() throws Exception {
        long id = drink1.getId();
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .name("drinkero")
                .category(new CategoryDto(catClassic.getId(), catClassic.getName()))
                .glass(new GlassDto(glassHighB.getId(), glassHighB.getName()))
                .addComponent(createDrinkComponent("Vodka", "4 cl"))
                .addComponent(createDrinkComponent("Appelsiinimehu", "10 cl"))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        MvcResult result = mockMvc
                .perform(
                        put(PATH + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isOk())
                .andReturn();
        DrinkDto dto = mapper.readValue(result.getResponse().getContentAsString(), DrinkDto.class);
        assertTrue("drink name not correct", dto.getName().equals("drinkero") );
        assertTrue("drink category not correct", dto.getCategory().getName().equals("Klassikko"));
        assertTrue("drink glass not correct", dto.getGlass().getName().equals("Highball"));
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingDrinkWithWrongIdFails404() throws Exception {
        long id = 751130;
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .name("")
                .category(new CategoryDto(catClassic.getId(), catClassic.getName()))
                .glass(new GlassDto(glassHighB.getId(), glassHighB.getName()))
                .addComponent(createDrinkComponent("Viski", "6 cl"))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        mockMvc
                .perform(
                        put(PATH + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingDrinkWithEmptyNameFails400() throws Exception {
        long id = drink1.getId();
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .name("")
                .category(new CategoryDto(catClassic.getId(), catClassic.getName()))
                .glass(new GlassDto(glassHighB.getId(), glassHighB.getName()))
                .addComponent(createDrinkComponent("Viski", "6 cl"))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        mockMvc
                .perform(
                        put(PATH + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingDrinkWithoutCategoryFails400() throws Exception {
        long id = drink1.getId();
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .name("drinkero")
                .glass(new GlassDto(glassHighB.getId(), glassHighB.getName()))
                .addComponent(createDrinkComponent("Viski", "6 cl"))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        mockMvc
                .perform(
                        put(PATH + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingDrinkWithoutGlassFails400() throws Exception {
        long id = drink1.getId();
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .name("drinkero")
                .category(new CategoryDto(catClassic.getId(), catClassic.getName()))
                .addComponent(createDrinkComponent("Viski", "6 cl"))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        mockMvc
                .perform(
                        put(PATH + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingDrinkWithoutIngredientsFails400() throws Exception {
        long id = drink1.getId();
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .name("drinkero")
                .category(new CategoryDto(catClassic.getId(), catClassic.getName()))
                .glass(new GlassDto(glassHighB.getId(), glassHighB.getName()))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        mockMvc
                .perform(
                        put(PATH + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser
    public void addingDrinkWithExistingNameFails() throws Exception {
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .name("Semi")
                .category(new CategoryDto(catClassic.getId(), catClassic.getName()))
                .glass(new GlassDto(glassCocktail.getId(), glassCocktail.getName()))
                .addComponent(createDrinkComponent("Viski", "4 cl"))
                .addComponent(createDrinkComponent("Limemehu", "5 cl"))
                .addComponent(createDrinkComponent("Appelsiinimehu", "5 cl"))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        mockMvc
            .perform(
                    post(PATH)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content))
            .andExpect(status().isLocked());
    }
    
    @Test
    @WithMockUser
    public void addingDrinkWithoutCategoryFails() throws Exception {
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .name("drinkup")
                .glass(new GlassDto(glassCocktail.getId(), glassCocktail.getName()))
                .addComponent(createDrinkComponent("Viski", "4 cl"))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        mockMvc
            .perform(
                    post(PATH)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser
    public void addingDrinkWithoutGlassFails() throws Exception {
        DrinkCmd drinkCmd = DrinkCmd.builder()
                .name("drinkup")
                .category(new CategoryDto(catClassic.getId(), catClassic.getName()))
                .addComponent(createDrinkComponent("Viski", "4 cl"))
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(drinkCmd);
        mockMvc
            .perform(
                    post(PATH)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content))
            .andExpect(status().isBadRequest());
    }
}
