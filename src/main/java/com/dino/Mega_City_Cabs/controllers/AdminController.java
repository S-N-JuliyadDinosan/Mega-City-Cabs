package com.dino.Mega_City_Cabs.controllers;

import com.dino.Mega_City_Cabs.dtos.AdminDto;
import com.dino.Mega_City_Cabs.entities.Admin;
import com.dino.Mega_City_Cabs.enums.RestApiResponseStatusCodes;
import com.dino.Mega_City_Cabs.services.AdminService;
import com.dino.Mega_City_Cabs.utils.EndpointBundle;
import com.dino.Mega_City_Cabs.utils.ResponseWrapper;
import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointBundle.ADMIN) // "/api/v1/admin"
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Admin>> createAdmin(@Valid @RequestBody AdminDto adminDto) {
        try {
            Admin admin = adminService.createAdmin(adminDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CREATED.getCode(),
                    ValidationMessages.SAVED_SUCCESSFULL,
                    admin
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CONFLICT.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.SAVE_FAILED,
                    null
            ));
        }
    }

    @PutMapping(EndpointBundle.ID)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Admin>> updateAdmin(
            @PathVariable("id") Long id, @Valid @RequestBody AdminDto adminDto) {
        try {
            adminDto.setId(id);
            Admin updatedAdmin = adminService.updateAdmin(adminDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.UPDATE_SUCCESSFULL,
                    updatedAdmin
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CONFLICT.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.UPDATE_FAILED,
                    null
            ));
        }
    }

    @DeleteMapping(EndpointBundle.ID)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<String>> deleteAdmin(@PathVariable("id") Long id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.DELETE_SUCCESS,
                    "Admin with ID " + id + " deleted successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CONFLICT.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.DELETE_FAILED,
                    null
            ));
        }
    }

    @GetMapping(EndpointBundle.ID)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Admin>> getAdminById(@PathVariable("id") Long id) {
        try {
            Admin admin = adminService.getAdminById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    admin
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CONFLICT.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Page<Admin>>> getAllAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Admin> admins = adminService.getAllAdmins(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    admins
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CONFLICT.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }
}