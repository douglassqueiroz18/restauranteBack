package com.restaurante.demo.service;

import java.net.URI;
import java.time.Duration;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class StorageService {
    private final String bucketName = "restauranteTeste";
    private final S3Presigner presigner;

    public StorageService() {
        this.presigner = S3Presigner.builder()
            .endpointOverride(URI.create("https://sfo3.digitaloceanspaces.com"))            
            .region(Region.of("sfo3"))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create("DO00MVPPJNJ8MEQH4CN9", "k/YrrPeiTtIlsVNX7JslzM51j1oRD3JHAnDTJuLWIMc")))
            .build();
    }

    public String gerarPresignedUrl(String nomeArquivo, String contentType) {
        String caminhoCompleto = "restauranteTeste/" + nomeArquivo;
        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket("virtualnfcbucket")
            .key(caminhoCompleto)
            .contentType(contentType)
            .acl(ObjectCannedACL.PUBLIC_READ) // Para que a imagem seja acessível via link depois
            .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .putObjectRequest(objectRequest)
            .build();

        return presigner.presignPutObject(presignRequest).url().toString();
    }
}