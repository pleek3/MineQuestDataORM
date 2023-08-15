package framework.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtils {

    public File readFileInResourceFolder(String fileName) throws IOException {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(fileName);

        if (resourceUrl == null) {
            throw new IOException("Resource not found: " + fileName);
        }

        File resourceFile = new File(resourceUrl.getFile());

        if (!resourceFile.exists()) {
            throw new IOException("Resource file does not exist: " + resourceFile.getAbsolutePath());
        }

        String tempDir = System.getProperty("java.io.tmpdir");
        Path tempFilePath = new File(tempDir, fileName).toPath();

        if (!Files.exists(tempFilePath)) {
            Files.copy(resourceFile.toPath(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("file found:");
        System.out.println(tempFilePath);
        return tempFilePath.toFile();
    }

    public File getOrCreateFile(String path, String fileName) throws IOException {
        File expected = new File(path, fileName);
        return createFileIfNotExists(expected);
    }

    public File getOrCreateFile(String... parts) throws IOException {
        String fileUrl = buildFilePath(parts);
        File expected = new File(fileUrl);
        return createFileIfNotExists(expected);
    }

    private File createFileIfNotExists(File file) throws IOException {
        if (!file.exists() && !file.createNewFile()) {
            throw new RuntimeException("Failed to create the file: " + file.getAbsolutePath());
        }
        return file;
    }

    public File getOrCreateFileInResourceFolder(String fileName) throws IOException {
        String resourceFolderPath = getResourceFolderAbsolutePath();
        File expected = new File(resourceFolderPath, fileName);
        return createFileIfNotExists(expected);
    }

    private String getResourceFolderAbsolutePath() {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        File resourceFolder;
        String resourcePath = "";

        if (classLoader.getResource("") != null) {
            resourcePath = classLoader.getResource("").getPath();
        } else {
            String absolutePath = new File("").getAbsolutePath();
            resourcePath = absolutePath + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        }

        return resourcePath;
    }

    private String buildFilePath(String... arguments) {
        String separator = File.separator;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arguments.length; i++) {
            String part = arguments[i].trim();
            if (!part.isEmpty()) {
                builder.append(part);

                if (i != arguments.length - 1) {
                    builder.append(separator);
                }
            }
        }

        return builder.toString();
    }

}
