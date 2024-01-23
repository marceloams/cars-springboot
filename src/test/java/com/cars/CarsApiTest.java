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

    private final String basePathUrl = "/api/v1/cars";

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

    private Long verifyClassAttributes(String location, Car car) {
        //get object
        CarDTO carDTO = getCar(location).getBody();

        //verify
        Assertions.assertNotNull(carDTO);
        Assertions.assertEquals(car.getName(), carDTO.getName());
        Assertions.assertEquals(car.getType(), carDTO.getType());

        return carDTO.getId();
    }

    @Test
    public void insertUpdateAndDelete(){
        Car car = new Car();
        car.setName("Fusca");
        car.setType("vintage");

        //INSERT
        ResponseEntity<CarDTO> response = post(basePathUrl + "/addCar", car, CarDTO.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //get location
        String location = response.getHeaders().getLocation() + "";

        //verify
        Long id = verifyClassAttributes(location, car);

        //UPDATE
        car.setName("Fusca 1300cc");
        car.setType("classic");

        //request
        response = put(basePathUrl + "/updateCarById/" + id, car, CarDTO.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        //verify
        id = verifyClassAttributes(location, car);

        //DELETE
        delete(basePathUrl + "/deleteCarById/" + id, null);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getCar(location).getStatusCode());
    }

    @Test
    public void listCars() {
        List<CarDTO> carDTOList = getCars(basePathUrl).getBody();
        Assertions.assertNotNull(carDTOList);
        Assertions.assertEquals(30, carDTOList.size());
    }

    @Test
    public void listCarsWithPagination() {
        Assertions.assertEquals(10, getCars(basePathUrl + "/with-pagination").getBody().size());
        Assertions.assertEquals(5, getCars(basePathUrl + "/with-pagination?size=5").getBody().size());
    }

    @Test
    public void listCarsByType() {
        Assertions.assertEquals(10, getCars(basePathUrl + "/type/classicos").getBody().size());
        Assertions.assertEquals(10, getCars(basePathUrl + "/type/luxo").getBody().size());
        Assertions.assertEquals(10, getCars(basePathUrl + "/type/esportivos").getBody().size());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, getCars(basePathUrl + "/type/abc").getStatusCode());
    }

    @Test
    public void getOk() {
        ResponseEntity<CarDTO> response = getCar(basePathUrl + "/11");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        CarDTO carDTO = response.getBody();
        assert carDTO != null;
        Assertions.assertEquals("Ferrari FF", carDTO.getName());
    }

    @Test
    public void getNotFound() {
        ResponseEntity<CarDTO> response = getCar(basePathUrl + "/100");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
