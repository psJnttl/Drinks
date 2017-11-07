package base.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import base.command.GlassAdd;
import base.domain.Glass;
import base.dto.GlassDto;
import base.repository.GlassRepository;

@Service
public class GlassService {

    @Autowired
    private GlassRepository glassRepository;

    public List<GlassDto> listAll() {
        List<Glass> list = glassRepository.findAll();
        return list.stream().map(i -> new GlassDto(i.getId(), i.getName())).collect(Collectors.toList());
    }

    @Transactional
    public Optional<GlassDto> addGlass(GlassAdd glass) {
        if (null == glass || null == glass.getName() || glass.getName().isEmpty()) {
            return Optional.empty();
        }
        Glass g = new Glass(glass.getName());
        g = glassRepository.save(g);
        GlassDto dto = new GlassDto(g.getId(), g.getName());
        return Optional.of(dto);
    }

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
}
