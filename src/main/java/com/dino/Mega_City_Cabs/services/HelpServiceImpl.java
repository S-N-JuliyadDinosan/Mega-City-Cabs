package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.HelpDto;
import com.dino.Mega_City_Cabs.entities.Help;
import com.dino.Mega_City_Cabs.repositories.HelpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HelpServiceImpl implements HelpService {

    @Autowired
    private HelpRepository helpRepository;

    @Override
    @Transactional
    public HelpDto createHelp(HelpDto helpDto) {
        Help help = new Help();
        help.setTopic(helpDto.getTopic());
        help.setDescription(helpDto.getDescription());
        help.setCategory(helpDto.getCategory());
        Help savedHelp = helpRepository.save(help);
        return convertToDto(savedHelp);
    }

    @Override
    @Transactional
    public HelpDto updateHelp(Long id, HelpDto helpDto) {
        Help help = helpRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Help article not found: " + id));
        help.setTopic(helpDto.getTopic());
        help.setDescription(helpDto.getDescription());
        help.setCategory(helpDto.getCategory());
        Help updatedHelp = helpRepository.save(help);
        return convertToDto(updatedHelp);
    }

    @Override
    @Transactional
    public void deleteHelp(Long id) {
        Help help = helpRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Help article not found: " + id));
        helpRepository.delete(help);
    }

    @Override
    public HelpDto getHelpById(Long id) {
        Help help = helpRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Help article not found: " + id));
        return convertToDto(help);
    }

    @Override
    public List<HelpDto> getAllHelp() {
        return helpRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HelpDto> getHelpByCategory(String category) {
        return helpRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private HelpDto convertToDto(Help help) {
        HelpDto dto = new HelpDto();
        dto.setId(help.getId());
        dto.setTopic(help.getTopic());
        dto.setDescription(help.getDescription());
        dto.setCategory(help.getCategory());
        return dto;
    }
}