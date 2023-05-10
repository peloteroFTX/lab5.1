package com.gtics.lahaces.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.common.io.ByteStreams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;

import org.springframework.core.io.WritableResource;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RestController
public class GcsController {

    public static byte[] downloadObject
            (String projectId, String bucketName, String blobName) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, blobName);

        try (ReadChannel reader = storage.reader(blobId)) {
            ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
            while (reader.read(bytes) > 0) {
                bytes.flip();
                bytes.clear();
            }
            byte[] image = bytes.array();
            return image;
        }

    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> displayItemImage() throws IOException {
        byte[] image = downloadObject("plenary-magpie-386203", "spring-bucket-jchavezs", "image.jpeg");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @GetMapping("/imagenEvento")
    public ResponseEntity<byte[]> displayItemImage(@RequestParam("id") int id) throws IOException {
//        byte[] image = downloadObject("plenary-magpie-386203", "spring-bucket-jchavezs", "image.jpeg");
        String blobName = "fotosEventos/foto-evento-" + id +".jpeg";
        byte[] image = downloadObject("plenary-magpie-386203", "spring-bucket-jchavezs", blobName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
