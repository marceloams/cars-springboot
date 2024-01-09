package com.cars.domain.service;

import com.cars.api.upload.UploadInput;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
public class FirebaseStorageService {

    @PostConstruct
    private void init() throws IOException {
        if(FirebaseApp.getApps().isEmpty()){
            InputStream serviceAccount =
                    FirebaseStorageService.class.getResourceAsStream("/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("cars-springboot-a3eea.appspot.com")
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }

    public String upload(UploadInput uploadInput) {

        Bucket bucket = StorageClient.getInstance().bucket();

        byte[] FileBytes = Base64.getDecoder().decode(uploadInput.getBase64());

        String fileName = uploadInput.getFileName();
        Blob blob = bucket.create(fileName, FileBytes, uploadInput.getMimeType());

        //turn url public
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        return String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), fileName);
    }

}
