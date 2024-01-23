package com.cars;

import com.cars.api.dto.CarDTO;
import com.cars.api.model.Car;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarsApiTest extends BaseAPITest{

    private ResponseEntity<CarDTO> getCar(String url){
        return get(url, CarDTO.class);
    }

    private ResponseEntity<List<CarDTO>> getCars(String url){
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<List<CarDTO>>() {
                });
    }

    @Test
    public void addCar(){
        Car car = new Car();
        car.setName("Fusca");
        car.setType("vintage");

        //add car
        ResponseEntity<CarDTO> response = post("/api/v1/cars/addCar", car, null);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //get object
        String location = response.getHeaders().getLocation() + "";
        CarDTO carDTO = getCar(location).getBody();

        Assertions.assertNotNull(carDTO);
        Assertions.assertEquals(car.getName(), carDTO.getName());
        Assertions.assertEquals(car.getType(), carDTO.getType());

        //delete object
        delete("/api/v1/cars/deleteCarById/" + carDTO.getId(), null);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getCar(location).getStatusCode());
    }

    @Test
    public void listCars() {
        List<CarDTO> carDTOList = getCars("/api/v1/cars").getBody();
        Assertions.assertNotNull(carDTOList);
        Assertions.assertEquals(30, carDTOList.size());
    }

    @Test
    public void listCarsWithPagination() {
        Assertions.assertEquals(10, getCars("/api/v1/cars/with-pagination").getBody().size());
        Assertions.assertEquals(5, getCars("/api/v1/cars/with-pagination?size=5").getBody().size());
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
