package base.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import base.command.GlassAdd;
import base.dto.GlassDto;
import base.repository.GlassRepository;
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
    
    @RequestMapping(value = "/api/glasses/{id}", method = RequestMethod.GET)
    public ResponseEntity<GlassDto> findGlass(@PathVariable long id) {
        Optional<GlassDto> gDto = glassService.findGlass(id);
        if (! gDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gDto.get(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/glasses/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<GlassDto> deleteGlass(@PathVariable long id) {
        if (! glassService.findGlass(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (glassService.isGlassUsed(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        glassService.deleteGlass(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/glasses/{id}", method = RequestMethod.PUT)
    public ResponseEntity<GlassDto> modifyGlass(@PathVariable long id, @RequestBody GlassAdd glass) {
        Optional<GlassDto> gDto = glassService.findGlass(id);
        if (! gDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (null == glass || null == glass.getName() || glass.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        GlassDto dto = glassService.modifyGlass(id, glass);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
