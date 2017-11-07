package base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import base.dto.GlassDto;
import base.service.GlassService;

@RestController
public class GlassController {

    @Autowired 
    GlassService glassService;
    
    @RequestMapping(value = "/api/glasses", method = RequestMethod.GET)
    public List<GlassDto> listAll() {
        return glassService.listAll();
    }
}
