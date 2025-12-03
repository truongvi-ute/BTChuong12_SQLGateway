/*
 * File: utils/DBConnection.java
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // Phương thức static để lấy kết nối, có thể gọi ở bất kỳ đâu
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Nạp Driver (chỉ cần làm 1 lần, nhưng để ở đây cho an toàn trong ngữ cảnh này)
            Class.forName("org.postgresql.Driver");

            // 1. Lấy URL từ Biến môi trường
            String dbURL = "jdbc:postgresql://dpg-d4nq9h15pdvs73ac3hb0-a.singapore-postgres.render.com:5432/render_db_fagx?characterEncoding=UTF-8";
            String username = "render_db_fagx_user";
            String password = "hVrfapv3nbQ2UUTecQDAXpoxDgpr8Mef";


            // 3. Tạo kết nối
            connection = DriverManager.getConnection(dbURL, username, password);
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Tùy chọn: Ném ngoại lệ ra ngoài hoặc trả về null để Servlet xử lý
        }
        return connection;
    }
}