package base.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import base.domain.Role;
import base.dto.RoleDto;
import base.repository.RoleRepository;

@RestController
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/api/admin/roles", method = RequestMethod.GET)
    public List<RoleDto> listRoles() {
        List<Role> list = roleRepository.findAll();
        return list.stream()
                   .map(r -> new RoleDto(r.getId(), r.getName()))
                   .collect(Collectors.toList());
    }
}
