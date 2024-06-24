package com.example.MMP.wod;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUploadUtil {
    @Getter
    private final String uploadDirPath;

    public FileUploadUtil() {
        OSType osType = OSType.getInstance();
        if (osType != null) {
            this.uploadDirPath = osType.getPath();
        } else {
            throw new RuntimeException("Unsupported operating system.");
        }
    }

    public void saveFile(String fileName, MultipartFile file) throws IOException {
        File directory = new File(uploadDirPath);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }

        String filePath = directory.getAbsolutePath() + File.separator + StringUtils.cleanPath(fileName);
        File dest = new File(filePath);
        file.transferTo(dest);
    }
}

