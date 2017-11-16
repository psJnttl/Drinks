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

import base.command.CategoryAdd;
import base.domain.Category;
import base.domain.Drink;
import base.dto.CategoryDto;
import base.repository.CategoryRepository;
import base.repository.DrinkRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CategoryControllerTest {

    private static final String PATH = "/api/categories";
    private static final String CATEGORY1 = "Classic drinks";
    private static final String CATEGORY2 = "Stiff enuff";
    private static final String CATEGORY3 = "Coming down or going up?";
    private static final String EMPTY_STRING = "";
    private static final String DRINK_NAME = "Random drink name, this.";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DrinkRepository drinkRepository;

    private MockMvc mockMvc;
    private Category c1, c2;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        c1 = new Category(CATEGORY1);
        c1 = categoryRepository.save(c1);
        c2 = new Category(CATEGORY2);
        c2 = categoryRepository.save(c2);
    }

    @After
    public void tearDown() throws Exception {
        Drink d1 = drinkRepository.findByName(DRINK_NAME);
        if (null != d1) {
            drinkRepository.delete(d1);
        }
        categoryRepository.delete(c1);
        categoryRepository.delete(c2);
        Category c3 = categoryRepository.findByName(CATEGORY3);
        if (null != c3) {
            categoryRepository.delete(c3);
        }
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
    public void addingCategoryReturnsLocationHeaderAndDto() throws Exception {
        CategoryAdd catAdd = new CategoryAdd(CATEGORY3);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(catAdd);
        MvcResult result = mockMvc.perform(post(PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isCreated()).andExpect(header().string("Location", containsString(PATH + "/")))
                .andReturn();
        CategoryDto dto = mapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertTrue("Category name not correct", dto.getName().equals(CATEGORY3));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void addingCategoryWithoutNameFails() throws Exception {
        CategoryAdd catAdd = new CategoryAdd(EMPTY_STRING);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(catAdd);
        mockMvc.perform(post(PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void fetchSingleCategoryOK() throws Exception {
        Category cat = categoryRepository.findOne(this.c1.getId());
        String name = cat.getName();
        MvcResult result = mockMvc.perform(get(PATH + "/" + this.c1.getId())).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        assertFalse("Contents cannot be empty!", result.getResponse().getContentAsString().isEmpty());
        ObjectMapper mapper = new ObjectMapper();
        CategoryDto dto = mapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertTrue("Category name not correct", dto.getName().equals(name));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void fetchSingleCategoryWithWrongIdFails404() throws Exception {
        mockMvc.perform(get(PATH + "/" + 751130)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void deleteCategoryOK() throws Exception {
        mockMvc.perform(delete(PATH + "/" + this.c1.getId())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void deleteCategoryWithWrongIdFails404() throws Exception {
        mockMvc.perform(delete(PATH + "/" + 751130)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyCategoryOKandReturnsChangedContent() throws Exception {
        CategoryAdd catAdd = new CategoryAdd(CATEGORY3);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(catAdd);
        MvcResult result = mockMvc
                .perform(put(PATH + "/" + this.c2.getId()).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isOk()).andReturn();
        CategoryDto dto = mapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertTrue(dto.getName().equals(CATEGORY3));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingCategoryWithWrongIdFails404() throws Exception {
        CategoryAdd catAdd = new CategoryAdd(CATEGORY3);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(catAdd);
        mockMvc.perform(put(PATH + "/" + 751130).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingCategoryWithEmptyContentsFails() throws Exception {
        CategoryAdd catAdd = new CategoryAdd(EMPTY_STRING);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(catAdd);
        mockMvc.perform(put(PATH + "/" + this.c2.getId()).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void cantDeleteCategoryIfInAdrink() throws Exception {
        Drink drink = new Drink(DRINK_NAME);
        drink.setCategory(c1);
        drinkRepository.saveAndFlush(drink);
        mockMvc.perform(
                delete(PATH + "/" + c1.getId()))
                .andExpect(status().isConflict());
    }
    
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void addingCategoryWithExistingNameFails() throws Exception {
        CategoryAdd catAdd = new CategoryAdd(CATEGORY1);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(catAdd);
        mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
        .andExpect(status().isLocked());       
        
    }
}
