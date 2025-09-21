package com.example.mobilia.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return result.get("secure_url").toString();
    }

    public void deleteFileByUrl(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty()) return;

        String publicId = extractPublicId(imageUrl);
        if (publicId != null) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }

    private String extractPublicId(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("/upload/")) return null;

        try {
            String[] parts = imageUrl.split("/upload/");
            String path = parts[1];
            String fileName = path.substring(path.indexOf("/") + 1);
            return fileName.replace(".jpg", "").replace(".png", "").replace(".jpeg", "");
        } catch (Exception e) {
            return null;
        }
    }
}

