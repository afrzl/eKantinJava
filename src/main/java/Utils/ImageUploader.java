package Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageUploader {
    public static String uploadImage(File file, String uploadDir) throws IOException {
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist.");
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String extension = getFileExtension(file);
        String newFileName = timestamp + extension;
        File destinationFile = new File(uploadDir, newFileName);

        Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return uploadDir + "\\" + newFileName;
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        if (lastIndex == -1) {
            return "";
        }
        return name.substring(lastIndex);
    }
}
