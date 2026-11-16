package Utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

public final class DishImageStorage2 {
    public static final String URL_PREFIX = "/dish-images/";
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;
    private static final long MAX_IMAGE_PIXELS = 40_000_000L;

    private DishImageStorage2() {
    }

    public static String save(Part part) throws IOException {
        if (part == null || part.getSize() <= 0 || part.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Ảnh phải có dung lượng từ 1 byte đến 5 MB.");
        }

        byte[] content;
        try (java.io.InputStream input = part.getInputStream()) {
            content = input.readNBytes((int) MAX_IMAGE_SIZE + 1);
        }
        if (content.length == 0 || content.length > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Ảnh phải có dung lượng từ 1 byte đến 5 MB.");
        }

        String extension = imageExtension(content);
        String fileName = UUID.randomUUID().toString() + extension;
        Files.write(getDirectory().resolve(fileName), content,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        return URL_PREFIX + fileName;
    }

    public static Path resolve(String pathInfo) throws IOException {
        String fileName = pathInfo == null ? "" : pathInfo.replaceFirst("^/", "");
        if (!fileName.matches("[0-9a-fA-F-]+\\.(jpg|png|gif)")) {
            throw new IllegalArgumentException("Tên ảnh không hợp lệ.");
        }
        Path directory = getDirectory();
        Path image = directory.resolve(fileName).normalize();
        if (!directory.equals(image.getParent())) {
            throw new IllegalArgumentException("Đường dẫn ảnh không hợp lệ.");
        }
        return image;
    }

    public static void delete(String imageUrl) throws IOException {
        if (imageUrl != null && imageUrl.startsWith(URL_PREFIX)) {
            Files.deleteIfExists(resolve(imageUrl.substring(URL_PREFIX.length())));
        }
    }

    private static Path getDirectory() throws IOException {
        String catalinaBase = System.getProperty("catalina.base");
        if (catalinaBase == null || catalinaBase.trim().isEmpty()) {
            throw new IOException("Không xác định được thư mục CATALINA_BASE để lưu ảnh món ăn.");
        }
        Path directory = Paths.get(catalinaBase, "uploads", "dishes").toAbsolutePath().normalize();
        Files.createDirectories(directory);
        return directory;
    }

    private static String imageExtension(byte[] content) throws IOException {
        try (ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(content))) {
            if (input == null) {
                throw new IllegalArgumentException("Tệp tải lên không phải ảnh PNG, JPEG hoặc GIF hợp lệ.");
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw new IllegalArgumentException("Chỉ chấp nhận ảnh PNG, JPEG hoặc GIF.");
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(input, true, true);
                long pixels = (long) reader.getWidth(0) * reader.getHeight(0);
                if (pixels <= 0 || pixels > MAX_IMAGE_PIXELS) {
                    throw new IllegalArgumentException("Kích thước ảnh vượt quá giới hạn cho phép.");
                }
                String format = reader.getFormatName().toLowerCase(Locale.ROOT);
                if ("jpeg".equals(format) || "jpg".equals(format)) {
                    return ".jpg";
                }
                if ("png".equals(format) || "gif".equals(format)) {
                    return "." + format;
                }
                throw new IllegalArgumentException("Chỉ chấp nhận ảnh PNG, JPEG hoặc GIF.");
            } finally {
                reader.dispose();
            }
        }
    }
}
