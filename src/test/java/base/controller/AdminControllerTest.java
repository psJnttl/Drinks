package base.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import base.domain.Account;
import base.domain.Role;
import base.repository.AccountRepository;
import base.repository.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String PATH = "/api/admin/accounts";
    private static final String USERNAME1 = "GHFGytddrYTESrtwEFTBI";
    private static final String USERNAME2 = "KJIPimonIMhumpIUMHNNG";
    private static final String PASSWORD1 = "password";
    private static final String PASSWORD_NEW = "NewPassword";
    private static final String EMPTY_STRING = "";
    
    private MockMvc mockMvc;
    private Account user;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        user = new Account();
        user.setUsername(USERNAME1);
        user.setPassword(passwordEncoder.encode(PASSWORD1));
        List<Role> roles = new ArrayList<>();
        Role userRole = new Role("USER");
        roles.add(userRole);
        user = accountRepository.saveAndFlush(user);
    }
    
    @After
    public void tearDown() throws Exception {
        accountRepository.delete(user);
        Account account2  = accountRepository.findByUsername(USERNAME2);
        if (null != account2) {
            accountRepository.delete(account2);
        }
    }

    @Test
    @WithMockUser(username=USERNAME1, authorities={"USER", "ADMIN"})
    public void listAllAccounts() throws Exception {
        mockMvc
            .perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    

    @Test
    @WithMockUser(username=USERNAME1, authorities={"USER", "ADMIN"})
    public void listMustNotBeEmpty() throws Exception {
        MvcResult result = mockMvc
                               .perform(get(PATH))
                               .andReturn();
        String content = result.getResponse().getContentAsString();
        assertFalse("List must not be empty.", content.equals("[]"));
    }

}
