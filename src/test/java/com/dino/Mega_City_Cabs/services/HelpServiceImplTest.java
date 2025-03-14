package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.HelpDto;
import com.dino.Mega_City_Cabs.entities.Help;
import com.dino.Mega_City_Cabs.repositories.HelpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelpServiceImplTest {

    @Mock
    private HelpRepository helpRepository;

    @InjectMocks
    private HelpServiceImpl helpService;

    private Help help;
    private HelpDto helpDto;

    @BeforeEach
    void setUp() {
        help = new Help();
        help.setId(1L);
        help.setTopic("Test Topic");
        help.setDescription("Test Description");
        help.setCategory("Test Category");

        helpDto = new HelpDto();
        helpDto.setTopic("Test Topic");
        helpDto.setDescription("Test Description");
        helpDto.setCategory("Test Category");
    }

    @Test
    void createHelp_Success() {
        when(helpRepository.save(any(Help.class))).thenReturn(help);

        HelpDto result = helpService.createHelp(helpDto);

        assertNotNull(result);
        assertEquals(help.getId(), result.getId());
        assertEquals(help.getTopic(), result.getTopic());
        assertEquals(help.getDescription(), result.getDescription());
        assertEquals(help.getCategory(), result.getCategory());
        verify(helpRepository, times(1)).save(any(Help.class));
    }

    @Test
    void updateHelp_Success() {
        when(helpRepository.findById(1L)).thenReturn(Optional.of(help));
        when(helpRepository.save(help)).thenReturn(help);

        HelpDto updatedDto = new HelpDto();
        updatedDto.setTopic("Updated Topic");
        updatedDto.setDescription("Updated Description");
        updatedDto.setCategory("Updated Category");

        HelpDto result = helpService.updateHelp(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Updated Topic", result.getTopic());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("Updated Category", result.getCategory());
        verify(helpRepository, times(1)).save(help);
    }

    @Test
    void updateHelp_NotFound_ThrowsException() {
        when(helpRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            helpService.updateHelp(1L, helpDto);
        });

        assertEquals("Help article not found: 1", exception.getMessage());
        verify(helpRepository, never()).save(any(Help.class));
    }

    @Test
    void deleteHelp_Success() {
        when(helpRepository.findById(1L)).thenReturn(Optional.of(help));

        helpService.deleteHelp(1L);

        verify(helpRepository, times(1)).delete(help);
    }

    @Test
    void deleteHelp_NotFound_ThrowsException() {
        when(helpRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            helpService.deleteHelp(1L);
        });

        assertEquals("Help article not found: 1", exception.getMessage());
        verify(helpRepository, never()).delete(any(Help.class));
    }

    @Test
    void getHelpById_Success() {
        when(helpRepository.findById(1L)).thenReturn(Optional.of(help));

        HelpDto result = helpService.getHelpById(1L);

        assertNotNull(result);
        assertEquals(help.getId(), result.getId());
        assertEquals(help.getTopic(), result.getTopic());
    }

    @Test
    void getHelpById_NotFound_ThrowsException() {
        when(helpRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            helpService.getHelpById(1L);
        });

        assertEquals("Help article not found: 1", exception.getMessage());
    }

    @Test
    void getAllHelp_Success() {
        when(helpRepository.findAll()).thenReturn(Collections.singletonList(help));

        List<HelpDto> result = helpService.getAllHelp();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(help.getId(), result.get(0).getId());
    }

    @Test
    void getAllHelp_Empty_ReturnsEmptyList() {
        when(helpRepository.findAll()).thenReturn(Collections.emptyList());

        List<HelpDto> result = helpService.getAllHelp();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getHelpByCategory_Success() {
        when(helpRepository.findByCategory("Test Category")).thenReturn(Collections.singletonList(help));

        List<HelpDto> result = helpService.getHelpByCategory("Test Category");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(help.getId(), result.get(0).getId());
        assertEquals("Test Category", result.get(0).getCategory());
    }

    @Test
    void getHelpByCategory_Empty_ReturnsEmptyList() {
        when(helpRepository.findByCategory("Unknown Category")).thenReturn(Collections.emptyList());

        List<HelpDto> result = helpService.getHelpByCategory("Unknown Category");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}