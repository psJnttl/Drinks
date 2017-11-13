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

import com.fasterxml.jackson.databind.ObjectMapper;
import base.command.GlassAdd;
import base.command.IngredientAdd;
import base.domain.Drink;
import base.domain.Glass;
import base.dto.GlassDto;
import base.repository.DrinkRepository;
import base.repository.GlassRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GlassControllerTest {

    private static final String PATH = "/api/glasses";
    private static final String GLASS1 = "Wheat beer glass";
    private static final String GLASS2 = "Stout glass";
    private static final String GLASS3 = "Spring water glass";
    private static final String EMPTY_STRING = "";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GlassRepository glassRepository;
    
    @Autowired
    private DrinkRepository drinkRepository;

    private MockMvc mockMvc;
    private long id1 = 0, id2 = 0;
    private Glass g1, g2;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        g1 = new Glass(GLASS1);
        g1 = glassRepository.save(g1);
        this.id1 = g1.getId();
        g2 = new Glass(GLASS2);
        g2 = glassRepository.save(g2);
        this.id2 = g2.getId();
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void listGlassesResponseStatusOKandContentTypeJsonUtf8() throws Exception {
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
    public void addingGlassReturnsLocationHeaderAndDto() throws Exception {
        GlassAdd glass = new GlassAdd(GLASS3);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(glass);
        MvcResult result = mockMvc.perform(post(PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isCreated()).andExpect(header().string("Location", containsString(PATH + "/")))
                .andReturn();
        GlassDto dto = mapper.readValue(result.getResponse().getContentAsString(), GlassDto.class);
        assertTrue("glass name not correct", dto.getName().equals(GLASS3));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void addingGlassWithoutNameFails() throws Exception {
        GlassAdd glass = new GlassAdd("");
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(glass);
        mockMvc.perform(post(PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void fetchSingleGlassOK() throws Exception {
        Glass glass = glassRepository.findOne(this.id2);
        String name = glass.getName();
        MvcResult result = mockMvc.perform(get(PATH + "/" + this.id2)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        assertFalse("Contents cannot be empty!", result.getResponse().getContentAsString().isEmpty());
        ObjectMapper mapper = new ObjectMapper();
        GlassDto dto = mapper.readValue(result.getResponse().getContentAsString(), GlassDto.class);
        assertTrue(dto.getName().equals(name));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void fetchSingleGlassWithWrongIdFails404() throws Exception {
        mockMvc.perform(get(PATH + "/" + 751130)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void deleteGlassOK() throws Exception {
        mockMvc.perform(delete(PATH + "/" + this.id2)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void deleteGlassWithWrongIdFails404() throws Exception {
        mockMvc.perform(delete(PATH + "/" + 751130)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyGlassOKandReturnsChangedContent() throws Exception {
        GlassAdd glassAdd = new GlassAdd(GLASS3);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(glassAdd);
        MvcResult result = mockMvc
                .perform(put(PATH + "/" + this.id1).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isOk()).andReturn();
        GlassDto dto = mapper.readValue(result.getResponse().getContentAsString(), GlassDto.class);
        assertTrue(dto.getName().equals(GLASS3));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingGlassWithWrongIdFails404() throws Exception {
        GlassAdd glassAdd = new GlassAdd(GLASS3);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(glassAdd);
        mockMvc.perform(put(PATH + "/" + 751130).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void modifyingGlassWithEmptyContentsFails() throws Exception {
        GlassAdd glassAdd = new GlassAdd(EMPTY_STRING);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(glassAdd);
        mockMvc.perform(put(PATH + "/" + 1).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void deletingGlassUsedInDrinkFails() throws Exception {
        Drink drink = new Drink("Paulaner");
        drink.setGlass(this.g1);
        drinkRepository.saveAndFlush(drink);
        mockMvc.perform(
                delete(PATH + "/" + id1))
                .andExpect(status().isConflict());
    }
}
