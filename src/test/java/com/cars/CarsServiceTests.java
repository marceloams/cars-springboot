package com.cars;

import com.cars.domain.model.Car;
import com.cars.domain.service.CarService;
import com.cars.domain.dto.CarDTO;
import com.cars.domain.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CarsServiceTests {

	@Autowired
	CarService carService;

	@Test
	public void listCarsTest(){
		List<CarDTO> carsList = carService.getCars();

		Assertions.assertEquals(30, carsList.size());
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

	@Test
	public void insertCar(){
		Car car = new Car();
		car.setName("Fusca");
		car.setType("vintage");

		//adding object
		CarDTO carDTO = carService.insert(car);
		Assertions.assertNotNull(carDTO);

		Long id = carDTO.getId();
		Assertions.assertNotNull(id);

		//getting object
		carDTO = carService.getCarById(id);
		Assertions.assertNotNull(carDTO);

		Assertions.assertEquals(car.getName(), carDTO.getName());
		Assertions.assertEquals(car.getType(), carDTO.getType());

		//delete object
		carService.deleteCarById(id);
		try{
			Assertions.assertNull(carService.getCarById(id));
			Assertions.fail("Car not deleted!");
		}catch (ObjectNotFoundException e){
			//ok
		}
	}

	@Test
	public void updateCar(){
		Car car = new Car();
		car.setName("Fusca");
		car.setType("vintage");

		//adding object
		CarDTO carDTO = carService.insert(car);
		Assertions.assertNotNull(carDTO);

		Long id = carDTO.getId();
		Assertions.assertNotNull(id);

		//getting object
		carDTO = carService.getCarById(id);
		Assertions.assertNotNull(carDTO);

		Assertions.assertEquals(car.getName(), carDTO.getName());
		Assertions.assertEquals(car.getType(), carDTO.getType());

		//updating object
		car.setName("Fusca 1300cc");
		car.setType("classicos");

		carDTO = carService.updateCarById(id, car);
		Assertions.assertNotNull(carDTO);

		id = carDTO.getId();
		Assertions.assertNotNull(id);

		carDTO = carService.getCarById(id);
		Assertions.assertNotNull(carDTO);

		Assertions.assertEquals(car.getName(), carDTO.getName());
		Assertions.assertEquals(car.getType(), carDTO.getType());

		//delete object
		carService.deleteCarById(id);
		try{
			Assertions.assertNull(carService.getCarById(id));
			Assertions.fail("Car not deleted!");
		}catch (ObjectNotFoundException e){
			//ok
		}
	}
}
