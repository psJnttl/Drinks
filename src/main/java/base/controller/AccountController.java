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
    
    @RequestMapping(value = "/api/account", method = RequestMethod.GET)
    public ResponseEntity<AccountDto> getAccount() {
        Optional<AccountDto> accountDto = accountService.getUser();
        if (!accountDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(accountDto.get(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/account/signup", method = RequestMethod.POST)
    public ResponseEntity<AccountDto> signup(@RequestBody AccountAdd account) {
        if (accountService.doesAccountExist(account)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Optional<AccountDto> accountDto = accountService.addUser(account);
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
    public List<AccountDto> listAccounts() {
        return accountService.listAll();
    }
    /**
     * For adding an account by ADMIN. Difference to signup is that authorization is not locked to USER.
     * @param AccountAdd
     * @return  AccountDto, Location header doesn't have ID.
     * @throws URISyntaxException 
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/api/accounts", method = RequestMethod.POST)
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountAdd account) throws URISyntaxException {
        if (accountService.doesAccountExist(account)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        AccountDto dto = accountService.addUserWithAuthorization(account);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/accounts/") );
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }

    /**
     * For changing authorization roles and reseting password. ADMIN access only.
     * @param AccountMod
     * @return AccountDto
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/api/accounts", method = RequestMethod.PUT)
    public ResponseEntity<AccountDto> modifyAccount(@RequestBody AccountMod account) {
        AccountDto dto = accountService.modifyAccount(account);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/api/accounts/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<AccountDto> deleteAccount(@PathVariable String username) {
        if (! accountService.deleteAccountByUsername(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
