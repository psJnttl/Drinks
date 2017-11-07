package base.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import base.domain.Glass;
import base.dto.GlassDto;
import base.repository.GlassRepository;

@Service
public class GlassService {

    @Autowired 
    private GlassRepository glassRepository;
    
    public List<GlassDto> listAll() {
        List<Glass> list = glassRepository.findAll();
        return list.stream()
                .map(i -> new GlassDto(i.getId(), i.getName()))
                .collect(Collectors.toList());
    }
}
