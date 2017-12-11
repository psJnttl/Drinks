package base.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import base.command.AccountAdd;
import base.command.AccountMod;
import base.domain.Account;
import base.domain.Role;
import base.dto.AccountDto;
import base.repository.AccountRepository;
import base.repository.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
        
    private static final String PATH = "/api/accounts";
    private static final String PATH_SIGNUP = "/api/accounts/signup";
    private static final String USERNAME1 = "user1";
    private static final String USERNAME2 = "user2";
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
        Role userRole = new Role("ROLE_USER");
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
    @WithMockUser(username=USERNAME1, roles={"USER"})
    public void signupOk() throws Exception {
        AccountAdd account = new AccountAdd();
        account.setUsername(USERNAME2);
        account.setPassword(PASSWORD1);
        Role user = roleRepository.findByName("ROLE_USER");
        account.setRoles(Arrays.asList(user));
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(account);
        MvcResult result = mockMvc
                .perform(
                        post(PATH_SIGNUP)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(content))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(PATH + "/")))
                .andReturn();
        AccountDto dto = mapper.readValue(result.getResponse().getContentAsString(), AccountDto.class);
        assertTrue("Username not correct", dto.getUsername().equals(USERNAME2));
    }

    @Test
    @WithMockUser(username=USERNAME1, roles={"USER"})
    public void signupWithEmptyUsernameFails() throws Exception {
        AccountAdd account = new AccountAdd();
        account.setUsername(EMPTY_STRING);
        account.setPassword(PASSWORD1);
        Role user = roleRepository.findByName("ROLE_USER");
        account.setRoles(Arrays.asList(user));
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(account);
        mockMvc
            .perform(
                post(PATH_SIGNUP)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(content))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username=USERNAME1, roles={"USER"})
    public void signupWithEmptyPasswordFails() throws Exception {
        AccountAdd account = new AccountAdd();
        account.setUsername(USERNAME2);
        account.setPassword(EMPTY_STRING);
        Role user = roleRepository.findByName("ROLE_USER");
        account.setRoles(Arrays.asList(user));
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(account);
        mockMvc
            .perform(
                post(PATH_SIGNUP)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(content))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username=USERNAME1, roles={"USER"})
    public void changePasswordOK() throws Exception {
        AccountMod accMod = buildAccountModCmdWithUserRole(USERNAME1, PASSWORD1, PASSWORD_NEW);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(accMod);
        MvcResult result = mockMvc
                .perform(
                        put(PATH + "/" + this.user.getId())
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
        Role userRole = new Role("ROLE_USER");
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
                    put(PATH + "/" + this.user.getId())
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
                    put(PATH + "/" + this.user.getId())
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
                    put(PATH + "/" + this.user.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content))
            .andExpect(status().isBadRequest());
    }
    
    

}
