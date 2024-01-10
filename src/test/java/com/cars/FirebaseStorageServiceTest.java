package com.cars;

import com.cars.api.upload.UploadInput;
import com.cars.api.upload.UploadOutput;
import com.cars.domain.service.FirebaseStorageService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FirebaseStorageServiceTest extends BaseAPITest{

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    private UploadInput getUploadInput() {
        UploadInput uploadInput = new UploadInput();

        uploadInput.setFileName("name.txt");
        uploadInput.setBase64("bWFyY2Vsbw==");
        uploadInput.setMimeType("text/plain");

        return uploadInput;
    }

    @Test
    public void testUploadFirebase() {
        String url = firebaseStorageService.upload(getUploadInput());

        ResponseEntity<String> urlResponse = get(url, String.class);
        Assertions.assertEquals(HttpStatus.OK, urlResponse.getStatusCode());
    }

    @Test
    public void testUploadAPI() {
        UploadInput uploadInput = getUploadInput();

        //Insert
        ResponseEntity<UploadOutput> response = post("/api/v1/upload", uploadInput, UploadOutput.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        UploadOutput uploadOutput = response.getBody();
        Assertions.assertNotNull(uploadOutput);

        String uploadOutputUrl = uploadOutput.getUrl();

        ResponseEntity<String> urlResponse = get(uploadOutputUrl, String.class);
        Assertions.assertEquals(HttpStatus.OK, urlResponse.getStatusCode());
    }

}
