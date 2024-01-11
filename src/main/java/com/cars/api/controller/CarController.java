package com.cars.api.controller;

import com.cars.api.model.Car;
import com.cars.api.service.CarService;
import com.cars.api.dto.CarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    @Autowired
    private CarService service;

    @GetMapping
    public ResponseEntity<List<CarDTO>> getCars(){
        return new ResponseEntity<>(service.getCars(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id){
        CarDTO carDto = service.getCarById(id);

        return ResponseEntity.ok(carDto);
    }

    @GetMapping(value = "/type/{type}")
    public ResponseEntity<List<CarDTO>> getCarsByType(@PathVariable String type){
        List<CarDTO> carsList = service.getCarByType(type);

        return (carsList.isEmpty())? ResponseEntity.noContent().build() : ResponseEntity.ok(carsList);
    }

    @PostMapping(value = "/addCar")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<CarDTO> post(@RequestBody Car car){
        CarDTO carDTO = service.insert(car);

        return ResponseEntity.created(getURI(carDTO.getId())).build();
    }

    private URI getURI(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/updateCarById/{id}")
    public ResponseEntity<CarDTO> updateCarById(@PathVariable Long id, @RequestBody Car car){

        car.setId(id);

        CarDTO carDTO = service.updateCarById(id, car);

        return (carDTO != null)? ResponseEntity.ok(carDTO) : ResponseEntity.notFound().build();
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/deleteCarById/{id}")
    public ResponseEntity deleteCarById(@PathVariable Long id){
        return (service.deleteCarById(id))? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}