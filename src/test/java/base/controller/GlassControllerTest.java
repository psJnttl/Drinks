package base.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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

import base.command.GlassAdd;
import base.domain.Glass;
import base.dto.GlassDto;
import base.repository.GlassRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GlassControllerTest {

    private static final String PATH = "/api/glasses";
    private static final String GLASS1 = "Tonic vesi";
    private static final String GLASS2 = "Vodka";
    private static final String GLASS3 = "Bourbon";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GlassRepository glassRepository;

    private MockMvc mockMvc;
    private long id1=0, id2=0;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Glass g1 = new Glass("Wheat beer glass");
        g1 = glassRepository.save(g1);
        this.id1 = g1.getId();
        Glass g2 = new Glass("Stout glass");
        g2 = glassRepository.save(g2);
        this.id2 = g2.getId();
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
    
    @Test
    public void addingGlassReturnsLocationHeaderAndDto() throws Exception {
        GlassAdd glass = new GlassAdd(GLASS3);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(glass);
        MvcResult result = mockMvc
                .perform(
                        post(PATH)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(PATH + "/")))
                .andReturn();
        GlassDto dto = mapper.readValue(result.getResponse().getContentAsString(), GlassDto.class);
        assertTrue("glass name not correct", dto.getName().equals(GLASS3));
    }

}
