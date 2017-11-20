package base.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    @RequestMapping(value = "/api/account", method = RequestMethod.GET)
    public ResponseEntity<AccountDto> getAccount() {
        Optional<AccountDto> accountDto = accountService.getUser();
        if (!accountDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(accountDto.get(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/account/signup", method = RequestMethod.POST)
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountAdd account) {
        Optional<AccountDto> accountDto = accountService.addUser(account);
        if (!accountDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(accountDto.get(), HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/api/account", method = RequestMethod.PUT)
    public ResponseEntity<AccountDto> changePassword(@RequestBody AccountMod account) {
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
