package base.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import base.command.GlassAdd;
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
    
    @RequestMapping(value = "/api/glasses", method = RequestMethod.POST)
    public ResponseEntity<GlassDto> addGlass(@RequestBody GlassAdd glass) throws URISyntaxException {
        Optional<GlassDto> gDto = glassService.addGlass(glass);
        if (! gDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        GlassDto dto = gDto.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/glasses/" + dto.getId()));
        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }
}
