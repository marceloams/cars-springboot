package com.cars;

import com.cars.domain.model.Car;
import com.cars.domain.dto.CarDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarsApiTest {
    @Autowired
    protected TestRestTemplate rest;

    private ResponseEntity<CarDTO> getCar(String url){
        return rest.getForEntity(url, CarDTO.class);
    }

    private ResponseEntity<List<CarDTO>> getCars(String url){
        return rest.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CarDTO>>() {}
        );
    }

    @Test
    public void addCar(){
        Car car = new Car();
        car.setName("Fusca");
        car.setType("vintage");

        //add car
        ResponseEntity<CarDTO> response = rest.postForEntity("/api/v1/cars/addCar", car, null);
        System.out.println(response);

        //get object
        String location = response.getHeaders().getLocation() + "";
        String id = location.substring(location.length()-2);
        String url = location.substring(0, 35) + id;
        CarDTO carDTO = getCar(url).getBody();

        Assertions.assertNotNull(carDTO);
        Assertions.assertEquals(car.getName(), carDTO.getName());
        Assertions.assertEquals(car.getType(), carDTO.getType());

        //delete object
        rest.delete("/api/v1/cars/deleteCarById/" + carDTO.getId());
        Assertions.assertEquals(getCar(location).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void listCars() {
        List<CarDTO> carDTOList = getCars("/api/v1/cars").getBody();
        Assertions.assertNotNull(carDTOList);
        Assertions.assertEquals(30, carDTOList.size());
    }

    @Test
    public void listCarsByType() {
        Assertions.assertEquals(10, getCars("/api/v1/cars/type/classicos").getBody().size());
        Assertions.assertEquals(10, getCars("/api/v1/cars/type/luxo").getBody().size());
        Assertions.assertEquals(10, getCars("/api/v1/cars/type/esportivos").getBody().size());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, getCars("/api/v1/cars/type/abc").getStatusCode());
    }

    @Test
    public void getOk() {
        ResponseEntity<CarDTO> response = getCar("/api/v1/cars/11");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        CarDTO carDTO = response.getBody();
        assert carDTO != null;
        Assertions.assertEquals("Ferrari FF", carDTO.getName());
    }

    @Test
    public void getNotFound() {
        ResponseEntity<CarDTO> response = getCar("/api/v1/cars/100");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
