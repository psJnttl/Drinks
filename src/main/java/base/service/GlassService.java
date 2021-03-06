package base.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import base.command.GlassAdd;
import base.domain.Drink;
import base.domain.Glass;
import base.dto.GlassDto;
import base.repository.GlassRepository;

@Service
public class GlassService {

    @Autowired
    private GlassRepository glassRepository;

    @Autowired
    private DrinkService drinkService;

    @Transactional(readOnly=true)
    public List<GlassDto> listAll() {
        List<Glass> list = glassRepository.findAll();
        return list.stream().map(i -> new GlassDto(i.getId(), i.getName())).collect(Collectors.toList());
    }

    public boolean isGlassValid(GlassAdd glass) {
        if (null == glass || null == glass.getName() || glass.getName().isEmpty()) {
            return false;
        }
        return true;
    }

    @Transactional
    public GlassDto addGlass(GlassAdd glass) {
        Glass g = new Glass(glass.getName());
        g = glassRepository.save(g);
        GlassDto dto = new GlassDto(g.getId(), g.getName());
        return dto;
    }

    @Transactional(readOnly=true)
    public Optional<GlassDto> findGlass(long id) {
        Glass glass = glassRepository.findOne(id);
        if (null == glass) {
            return Optional.empty();
        }
        GlassDto dto = new GlassDto(glass.getId(), glass.getName());
        return Optional.of(dto);
    }

    @Transactional
    public boolean deleteGlass(long id) {
        Glass glass = glassRepository.findOne(id);
        if (null == glass) {
            return false;
        }
        glassRepository.delete(glass);
        return true;
    }

    @Transactional
    public GlassDto modifyGlass(long id, GlassAdd glass) {
        Glass g = glassRepository.findOne(id);
        g.setName(glass.getName());
        g = glassRepository.save(g);
        GlassDto dto = new GlassDto(g.getId(), g.getName());
        return dto;
    }

    @Transactional(readOnly=true)
    public boolean isGlassUsed(long id) {
        Glass glass = glassRepository.findOne(id);
        if (null == glass) {
            return false;
        }
        List<Drink> list = drinkService.findByGlass(glass);
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }
    
    @Transactional(readOnly=true)
    public boolean glassExistsCaseInsensitive(GlassAdd glass) {
        List<Glass> glasses = glassRepository.findByNameIgnoreCase(glass.getName());
        if (glasses.isEmpty()) {
            return false;
        }
        return true;
    }

}
