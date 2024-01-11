package com.cars.api.service;

import com.cars.api.repository.CarRepository;
import com.cars.api.model.Car;
import com.cars.api.dto.CarDTO;
import com.cars.api.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private CarRepository rep;

    public List<CarDTO> getCars(){
        return rep.findAll().stream().map(CarDTO::create).collect(Collectors.toList());
    }

    public List<CarDTO> getCarsWithPagination(Pageable pageable){
        return rep.findAll(pageable).stream().map(CarDTO::create).collect(Collectors.toList());
    }

    public CarDTO getCarById(Long id){
        return rep.findById(id).map(CarDTO::create).orElseThrow(() -> new ObjectNotFoundException("Car not found!"));
    }

    public List<CarDTO> getCarByType(String type) {
        return rep.findByType(type).stream().map(CarDTO::create).collect(Collectors.toList());
    }

    public CarDTO insert(Car car) {
        Assert.isNull(car.getId(), "Insert was not done!");

        return CarDTO.create(rep.save(car));
    }

    public CarDTO updateCarById(Long id, Car car) {
        Assert.notNull(id, "Not possible to complete the update!");

        Optional<Car> optional = rep.findById(id);

        if(optional.isPresent()){
            Car db = optional.get();

            db.setName(car.getName());
            db.setType(car.getType());

            rep.save(db);

            return CarDTO.create(db);
        } else {
            return null;
        }
    }

    public boolean deleteCarById(Long id) {
        if(getCarById(id) != null) {
            rep.deleteById(id);
            return true;
        }
        return false;
    }
}
