package base.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import base.command.AccountAdd;
import base.command.AccountMod;
import base.dto.AccountDto;
import base.service.AccountService;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    @RequestMapping(value = "/api/accounts/logged", method = RequestMethod.GET)
    public ResponseEntity<AccountDto> getAccount() {
        Optional<AccountDto> accountDto = accountService.getUser();
        if (!accountDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(accountDto.get(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/accounts/signup", method = RequestMethod.POST)
    public ResponseEntity<AccountDto> signup(@RequestBody AccountAdd account) {
        if (accountService.doesAccountExist(account)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Optional<AccountDto> accountDto = accountService.addUser(account);
        return new ResponseEntity<>(accountDto.get(), HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.PUT)
    public ResponseEntity<AccountDto> changePassword(@PathVariable long id, @RequestBody AccountMod account) {
        if (!accountService.isRequestValid(account)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<AccountDto> accountDto = accountService.changePassword(account); 
        if (!accountDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(accountDto.get(), HttpStatus.OK);
    }

}
