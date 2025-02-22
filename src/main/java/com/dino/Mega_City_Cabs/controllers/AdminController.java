package com.dino.Mega_City_Cabs.controllers;

import com.dino.Mega_City_Cabs.models.Car;
import com.dino.Mega_City_Cabs.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

//    @Autowired
//    private CarService carService;
//
//    @PostMapping("addCar")
//    @PreAuthorize("hasRole('ADMIN')")
//    public Car addCar(@RequestBody Car car, @RequestHeader("Authorization") String token){
//        return carService.addCar(car);
//    }

}
