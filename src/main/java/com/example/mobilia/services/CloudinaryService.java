package com.example.mobilia.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
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

    public void deleteFileByUrl(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isEmpty()) {
            System.out.println("URL vazia, nada para deletar");
            return;
        }

        String publicId = extractPublicId(fileUrl);
        if (publicId != null && !publicId.isEmpty()) {

            String publicIdComExtensao = publicId + ".pdf";

            try {
                // Para PDFs
                Map<String, Object> options = new HashMap<>();
                options.put("resource_type", "raw");
                
                @SuppressWarnings("unchecked")
                Map<String, Object> result = cloudinary.uploader().destroy(publicIdComExtensao, options);

                String resultStatus = (String) result.get("result");
                if (!"ok".equals(resultStatus)) {
                    System.err.println("Falha ao excluir arquivo. Status: " + resultStatus);
                }
            } catch (Exception e) {
                System.err.println("Erro ao excluir arquivo do Cloudinary: " + e.getMessage());
                throw new IOException("Erro ao excluir arquivo: " + e.getMessage(), e);
            }
        } else {
            System.err.println("Não foi possível extrair public_id da URL: " + fileUrl);
        }
    }

    private String extractPublicId(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }

        try {
            if (!fileUrl.contains("/upload/")) {
                System.err.println("URL não contém '/upload/'");
                return null;
            }

            // Dividir a URL no ponto "/upload/"
            String[] parts = fileUrl.split("/upload/");
            if (parts.length < 2) {
                System.err.println("URL não tem formato esperado após '/upload/'");
                return null;
            }

            String afterUpload = parts[1];

            // Remover versão se existir (v1234567890/)
            if (afterUpload.matches("^v\\d+/.*")) {
                afterUpload = afterUpload.replaceFirst("^v\\d+/", "");
                System.out.println("Após remover versão: " + afterUpload);
            }

            // Remover a extensão do arquivo
            String publicId = afterUpload;
            int lastDotIndex = publicId.lastIndexOf('.');
            if (lastDotIndex > 0) {
                publicId = publicId.substring(0, lastDotIndex);
            }

            return publicId;

        } catch (Exception e) {
            System.err.println("Erro ao extrair public_id: " + e.getMessage());
            return null;
        }
    }

    
    public String uploadPdfFile(byte[] pdfBytes, String fileName) {
        try {
            // Configurações específicas para PDFs
            Map<String, Object> params = new HashMap<>();
            params.put("public_id", "contratos/" + fileName);
            params.put("resource_type", "raw");
            params.put("type", "upload");
            params.put("format", "pdf");
            params.put("use_filename", true);
            params.put("unique_filename", false);
            
            // Upload direto com byte array
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(pdfBytes, params);
            
            return (String) uploadResult.get("secure_url");
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do PDF para Cloudinary: " + e.getMessage(), e);
        }
    }

    public byte[] downloadPdfFile(String url) {
        try {
            if (url.contains("/image/upload/")) {
                url = url.replace("/image/upload/", "/raw/upload/");
            }
            // Usar uma biblioteca HTTP como RestTemplate ou WebClient
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, byte[].class);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar arquivo do Cloudinary: " + e.getMessage(), e);
        }
    }
}

