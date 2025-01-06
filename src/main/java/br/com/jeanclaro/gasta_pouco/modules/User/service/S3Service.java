package br.com.jeanclaro.gasta_pouco.modules.User.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.bucketName}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(UUID userId, MultipartFile file) throws IOException { 
        String filename = userId + "_" + file.getOriginalFilename();

        File tempFile = convertMultiPartToFile(file);
        amazonS3.putObject(new PutObjectRequest(bucketName,filename, tempFile));

        tempFile.delete();

        return amazonS3.getUrl(bucketName, filename).toString();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException{
        File tempFile = File.createTempFile("temp", null);
        try(FileOutputStream fos = new FileOutputStream(tempFile)) { 
            fos.write(file.getBytes());
        }
        return tempFile;
    }
}
