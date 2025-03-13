package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.HelpDto;
import java.util.List;

public interface HelpService {
    HelpDto createHelp(HelpDto helpDto);
    HelpDto updateHelp(Long id, HelpDto helpDto);
    void deleteHelp(Long id);
    HelpDto getHelpById(Long id);
    List<HelpDto> getAllHelp();
    List<HelpDto> getHelpByCategory(String category);
}