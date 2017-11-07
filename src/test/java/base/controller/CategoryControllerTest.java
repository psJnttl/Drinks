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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import base.command.CategoryAdd;
import base.domain.Category;
import base.dto.CategoryDto;
import base.repository.CategoryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CategoryControllerTest {

    private static final String PATH = "/api/categories";
    private static final String CATEGORY1 = "Classic drinks";
    private static final String CATEGORY2 = "Stiff enuff";
    private static final String CATEGORY3 = "Coming down or going up?";
    private static final String EMPTY_STRING = "";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CategoryRepository categoryRepository;

    private MockMvc mockMvc;
    private long id1 = 0, id2 = 0;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Category cat1 = new Category(CATEGORY1);
        cat1 = categoryRepository.save(cat1);
        this.id1 = cat1.getId();
        Category cat2 = new Category(CATEGORY2);
        cat2 = categoryRepository.save(cat2);
        this.id2 = cat2.getId();
    }

    @Test
    public void listResponseStatusOKandContentTypeJsonUtf8() throws Exception {
        mockMvc.perform(get(PATH)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void listMustNotBeEmpty() throws Exception {
        MvcResult res = mockMvc.perform(get(PATH)).andReturn();
        String content = res.getResponse().getContentAsString();
        assertFalse("Project list must not be empty.", content.equals("[]"));
    }

    @Test
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
    public void addingCategoryWithoutNameFails() throws Exception {
        CategoryAdd catAdd = new CategoryAdd(EMPTY_STRING);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(catAdd);
        mockMvc.perform(post(PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fetchSingleCategoryOK() throws Exception {
        Category cat = categoryRepository.findOne(this.id1);
        String name = cat.getName();
        MvcResult result = mockMvc.perform(get(PATH + "/" + this.id1)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        assertFalse("Contents cannot be empty!", result.getResponse().getContentAsString().isEmpty());
        ObjectMapper mapper = new ObjectMapper();
        CategoryDto dto = mapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertTrue("Category name not correct", dto.getName().equals(name));
    }

    @Test
    public void fetchSingleCategoryWithWrongIdFails404() throws Exception {
        mockMvc.perform(get(PATH + "/" + 751130)).andExpect(status().isNotFound());
    }

    @Test
    public void deleteCategoryOK() throws Exception {
        mockMvc.perform(delete(PATH + "/" + this.id1)).andExpect(status().isOk());
    }

    @Test
    public void deleteCategoryWithWrongIdFails404() throws Exception {
        mockMvc.perform(delete(PATH + "/" + 751130)).andExpect(status().isNotFound());
    }

    @Test
    public void modifyCategoryOKandReturnsChangedContent() throws Exception {
        CategoryAdd catAdd = new CategoryAdd(CATEGORY3);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(catAdd);
        MvcResult result = mockMvc
                .perform(put(PATH + "/" + this.id2).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isOk()).andReturn();
        CategoryDto dto = mapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertTrue(dto.getName().equals(CATEGORY3));
    }

    @Test
    public void modifyingCategoryWithWrongIdFails404() throws Exception {
        CategoryAdd catAdd = new CategoryAdd(CATEGORY3);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(catAdd);
        mockMvc.perform(put(PATH + "/" + 751130).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    public void modifyingCategoryWithEmptyContentsFails() throws Exception {
        CategoryAdd catAdd = new CategoryAdd(EMPTY_STRING);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(catAdd);
        mockMvc.perform(put(PATH + "/" + this.id2).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isBadRequest());
    }
}
