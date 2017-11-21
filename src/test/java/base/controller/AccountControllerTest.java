package base.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import base.command.AccountMod;
import base.domain.Account;
import base.domain.Role;
import base.dto.AccountDto;
import base.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final String PATH = "/api/account";
    private static final String PATH_ADMIN = "/api/accounts";
    private static final String USERNAME1 = "user1";
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
        System.out.println("account count: " + accountRepository.findAll().size());
    }

    @Test
    @WithMockUser(username=USERNAME1, roles={"USER"})
    public void changePasswordOK() throws Exception {
        AccountMod accMod = buildAccountModCmdWithUserRole(USERNAME1, PASSWORD1, PASSWORD_NEW);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(accMod);
        MvcResult result = mockMvc
                .perform(
                        put(PATH)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isOk())
                .andReturn();
        AccountDto dto = mapper.readValue(result.getResponse().getContentAsString(), AccountDto.class);
        assertTrue("Username not correct", dto.getUsername().equals(USERNAME1));
    }
    
    private AccountMod buildAccountModCmdWithUserRole(String username, String oldPassword, String newPassword) {
        AccountMod accMod = new AccountMod();
        accMod.setUsername(username);
        accMod.setNewPassword(newPassword);
        accMod.setOldPassword(oldPassword);
        List<Role> roles = new ArrayList<>();
        Role userRole = new Role("USER");
        roles.add(userRole);
        accMod.setRoles(roles);
        return accMod;
    }

    @Test
    @WithMockUser(username=USERNAME1, roles={"USER"})
    public void changePasswordFailsWithWrongPassword() throws Exception {
        AccountMod accMod = buildAccountModCmdWithUserRole(USERNAME1, PASSWORD_NEW, PASSWORD_NEW);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(accMod);
        mockMvc
            .perform(
                    put(PATH)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content))
            .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(username=USERNAME1, roles={"USER"})
    public void changePasswordFailsWithEmptyUsername() throws Exception {
        AccountMod accMod = buildAccountModCmdWithUserRole(EMPTY_STRING, PASSWORD1, PASSWORD_NEW);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(accMod);
        mockMvc
            .perform(
                    put(PATH)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username=USERNAME1, roles={"USER"})
    public void changePasswordFailsWithEmptyNewPassword() throws Exception {
        AccountMod accMod = buildAccountModCmdWithUserRole(USERNAME1, PASSWORD1, EMPTY_STRING);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(accMod);
        mockMvc
            .perform(
                    put(PATH)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username=USERNAME1, authorities={"USER", "ADMIN"})
    public void listAllAccounts() throws Exception {
        mockMvc
            .perform(get(PATH_ADMIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    
    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username=USERNAME1, authorities={"USER"})
    public void listAllAccountsNotAdminFails() throws Exception {
        mockMvc
            .perform(get(PATH_ADMIN))
                .andExpect(status().isForbidden()); 
    }
    
    
}
