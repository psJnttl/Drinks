package base.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
public class AdminController {

    @Autowired
    private AccountService accountService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/api/admin/accounts", method = RequestMethod.GET)
    public List<AccountDto> listAccounts() {
        return accountService.listAll();
    }
    /**
     * For adding an account by ADMIN. Difference to signup is that authorization is not locked to USER.
     * @param AccountAdd
     * @return  AccountDto, Location header doesn't have ID.
     * @throws URISyntaxException 
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/api/admin/accounts", method = RequestMethod.POST)
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountAdd account) throws URISyntaxException {
        if (null == account || null == account.getUsername() || null == account.getPassword() ||
                account.getUsername().isEmpty() || account.getPassword().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (accountService.doesAccountExist(account)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        AccountDto dto = accountService.addUserWithAuthorization(account);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/admin/accounts/" + dto.getId()) );
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }

    /**
     * For changing authorization roles and reseting password. ADMIN access only.
     * @param AccountMod
     * @return AccountDto
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/api/admin/accounts/{id}", method = RequestMethod.PUT)
    public ResponseEntity<AccountDto> modifyAccount(@PathVariable long id, @RequestBody AccountMod account) {
        if (!accountService.findAccount(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AccountDto dto = accountService.modifyAccount(account);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/api/admin/accounts/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<AccountDto> deleteAccount(@PathVariable long id) {
        if (! accountService.deleteAccount(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
