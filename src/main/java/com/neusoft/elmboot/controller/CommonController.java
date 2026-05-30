package com.neusoft.elmboot.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/CommonController")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommonController {

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            System.err.println("错误: 上传的文件为空");
            return "error:文件为空";
        }

        String originalFileName = file.getOriginalFilename();
        System.out.println("====== 收到上传请求 ======");
        System.out.println("文件名: " + originalFileName);
        System.out.println("文件大小: " + file.getSize() + " bytes");
        
        try {
            String suffixName = ".png";
            if (originalFileName != null && originalFileName.contains(".")) {
                suffixName = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            String fileName = UUID.randomUUID().toString() + suffixName;
            
            String projectPath = new File("").getAbsolutePath();
            File uploadDir = new File(projectPath, "uploads");
            
            Files.createDirectories(uploadDir.toPath());
            if (!uploadDir.isDirectory() || !uploadDir.canWrite()) {
                return "error:上传目录不可写:" + uploadDir.getAbsolutePath();
            }

            File destFile = new File(uploadDir, fileName);
            
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            
            System.out.println("文件上传成功!");
            System.out.println("保存位置: " + destFile.getAbsolutePath());
            System.out.println("返回给前端的文件名: " + fileName);
            System.out.println("==========================");
            
            return fileName;
            
        } catch (Exception e) {
            System.err.println("上传过程发生严重异常: " + e.getMessage());
            e.printStackTrace();
            return "error:" + e.getMessage();
        }
    }

    @RestControllerAdvice
    public static class UploadExceptionHandler {

        @ExceptionHandler({
                MissingServletRequestPartException.class,
                MaxUploadSizeExceededException.class,
                MultipartException.class,
                IllegalStateException.class
        })
        @ResponseStatus(HttpStatus.OK)
        public String handleUploadExceptions(Exception e) {
            return "error:" + e.getClass().getSimpleName() + ":" + (e.getMessage() == null ? "" : e.getMessage());
        }
    }
}
