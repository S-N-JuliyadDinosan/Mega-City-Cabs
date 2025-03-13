package com.dino.Mega_City_Cabs.controllers;

import com.dino.Mega_City_Cabs.dtos.HelpDto;
import com.dino.Mega_City_Cabs.services.HelpService;
import com.dino.Mega_City_Cabs.utils.EndpointBundle;
import com.dino.Mega_City_Cabs.utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(EndpointBundle.HELP)
public class HelpController {

    @Autowired
    private HelpService helpService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<HelpDto>> createHelp(@Valid @RequestBody HelpDto helpDto) {
        try {
            HelpDto createdHelp = helpService.createHelp(helpDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper<>(
                    201, "Help article created successfully", createdHelp));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
                    400, e.getMessage(), null));
        }
    }

    @PutMapping(EndpointBundle.ID)
    public ResponseEntity<ResponseWrapper<HelpDto>> updateHelp(
            @PathVariable Long id, @Valid @RequestBody HelpDto helpDto) {
        try {
            HelpDto updatedHelp = helpService.updateHelp(id, helpDto);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    200, "Help article updated successfully", updatedHelp));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
                    400, e.getMessage(), null));
        }
    }

    @DeleteMapping(EndpointBundle.ID)
    public ResponseEntity<ResponseWrapper<String>> deleteHelp(@PathVariable Long id) {
        try {
            helpService.deleteHelp(id);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    200, "Help article deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
                    400, e.getMessage(), null));
        }
    }

    @GetMapping(EndpointBundle.ID)
    public ResponseEntity<ResponseWrapper<HelpDto>> getHelpById(@PathVariable Long id) {
        try {
            HelpDto help = helpService.getHelpById(id);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    200, "Help article retrieved successfully", help));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(
                    404, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<HelpDto>>> getAllHelp() {
        List<HelpDto> helpList = helpService.getAllHelp();
        return ResponseEntity.ok(new ResponseWrapper<>(
                200, "Help articles retrieved successfully", helpList));
    }

    @GetMapping(EndpointBundle.GET_HELP_BY_CATEGORY)
    public ResponseEntity<ResponseWrapper<List<HelpDto>>> getHelpByCategory(@PathVariable String category) {
        List<HelpDto> helpList = helpService.getHelpByCategory(category);
        return ResponseEntity.ok(new ResponseWrapper<>(
                200, "Help articles for category retrieved successfully", helpList));
    }
}