package com.cars.domain.dto;

import com.cars.domain.model.Car;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class CarDTO {
    private long id;
    private String name;
    private String type;

    public static CarDTO create(Car car){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(car, CarDTO.class);
    }
}
