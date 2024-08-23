package dev.start.init.util;

import dev.start.init.entity.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
@Component
public class FileStore {
    private final String addressBasePath;

    public FileStore(@Value("${address.image.storage.path}") String addressBasePath) {
        this.addressBasePath = addressBasePath;
    }

    public String storeFile(MultipartFile file, Address address) {
        String uuid = UUID.randomUUID().toString().substring(0, 5);
        String fileName = "file_"+uuid + "_" + file.getOriginalFilename();

        // Manually construct the Unix-style path as a string
        String filePath = addressBasePath + "/" + fileName;

        // Create the target file
        File targetFile = new File(filePath);

        // Check if the directory exists, if not, create it
        File targetDirectory = new File(addressBasePath);
        if (!targetDirectory.exists()) {
            if (!targetDirectory.mkdirs()) {
                throw new RuntimeException("Could not create directory: " + targetDirectory.getAbsolutePath());
            }
        }
        try (FileOutputStream out = new FileOutputStream(targetFile)) {
            out.write(file.getBytes());
            // Set the docPath on the address entity without modifying the constructed path
            address.setFilePath(filePath);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
