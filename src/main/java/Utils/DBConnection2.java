package Utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection2 {
    private static final Properties CONFIG = loadConfig();

    private DBConnection2() {
    }

    public static Connection getConnection() throws SQLException {
        String url = setting("SUPABASE_DB_URL", "db.url");
        String user = setting("SUPABASE_DB_USER", "db.user");
        String password = setting("SUPABASE_DB_PASSWORD", "db.password");
        rejectPlaceholder(url, "db.url");
        rejectPlaceholder(user, "db.user");
        rejectPlaceholder(password, "db.password");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException exception) {
            throw new SQLException("Không tìm thấy PostgreSQL JDBC driver trong ứng dụng.", exception);
        }
        return DriverManager.getConnection(url, user, password);
    }

    private static Properties loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = DBConnection2.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Không tìm thấy db.properties trên classpath.");
            }
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new RuntimeException("Không thể đọc db.properties trên classpath.", exception);
        }
    }

    private static String setting(String environmentKey, String propertyKey) throws SQLException {
        String value = System.getenv(environmentKey);
        if (value == null || value.trim().isEmpty()) {
            value = CONFIG.getProperty(propertyKey);
        }
        if (value == null || value.trim().isEmpty()) {
            throw new SQLException("Thiếu cấu hình '" + propertyKey + "' trong db.properties hoặc biến môi trường "
                    + environmentKey + ".");
        }
        return value;
    }

    private static void rejectPlaceholder(String value, String key) throws SQLException {
        if (value.contains("<") || value.contains(">")) {
            throw new SQLException("Hãy thay placeholder của '" + key + "' trong db.properties hoặc cấu hình biến môi trường.");
        }
    }
}
