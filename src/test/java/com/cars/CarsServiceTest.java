package com.cars;

import com.cars.api.dto.CarDTO;
import com.cars.api.exception.ObjectNotFoundException;
import com.cars.api.model.Car;
import com.cars.api.service.CarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@SpringBootTest
class CarsServiceTest {

	@Autowired
	CarService carService;

	@Test
	public void listCars(){
		List<CarDTO> carsList = carService.getCars();

		Assertions.assertEquals(30, carsList.size());
	}

	@Test
	public void listCarsWithPagination(){
		List<CarDTO> carsList = carService.getCarsWithPagination(PageRequest.of(0,5));

		Assertions.assertEquals(5, carsList.size());
	}

	@Test
	public void listCarsByType(){
		List<CarDTO> carsList = carService.getCarByType("classicos");
		Assertions.assertEquals(10, carsList.size());

		carsList = carService.getCarByType("luxo");
		Assertions.assertEquals(10, carsList.size());

		carsList = carService.getCarByType("esportivos");
		Assertions.assertEquals(10, carsList.size());

		carsList = carService.getCarByType("abc");
		Assertions.assertEquals(0, carsList.size());
	}

	private Long verifyClassAttributes(CarDTO carDTO, Car car){
		//get id
		Long id = carDTO.getId();
		Assertions.assertNotNull(id);

		//get by id
		carDTO = carService.getCarById(id);
		Assertions.assertNotNull(carDTO);

		//verify
		Assertions.assertEquals(car.getName(), carDTO.getName());
		Assertions.assertEquals(car.getType(), carDTO.getType());

		return id;
	}

	@Test
	public void insertUpdateAndDelete(){

		Car car = new Car();
		car.setName("Fusca");
		car.setType("vintage");

		//INSERT
		CarDTO carDTO = carService.insert(car);
		Assertions.assertNotNull(carDTO);

		//verify
		Long id = verifyClassAttributes(carDTO, car);

		//UPDATE
		car.setName("Fusca 1300cc");
		car.setType("classic");
		carDTO = carService.updateCarById(id, car);
		Assertions.assertNotNull(carDTO);

		//verify
		verifyClassAttributes(carDTO, car);

		//delete object
		carService.deleteCarById(id);
		Assertions.assertThrows(ObjectNotFoundException.class, () -> carService.getCarById(id));
	}
}
