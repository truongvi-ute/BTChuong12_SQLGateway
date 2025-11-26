/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sql.SQLUtil;

@WebServlet(urlPatterns = {"/sqlGateway"})
public class SqlGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";

        try {
            Class.forName("org.postgresql.Driver");
            
            // 1. Lấy URL từ Biến môi trường (Environment Variable)
            String dbURL = System.getenv("DB_URL");
            String username = System.getenv("DB_USER");
            String password = System.getenv("DB_PASS");

            // 2. Nếu không tìm thấy (tức là đang chạy Local), thì dùng giá trị mặc định
            if (dbURL == null) {
                dbURL = "jdbc:postgresql://localhost:5433/murach";
                username = "murach"; 
                password = "123";
            }
            
            Connection connection = DriverManager.getConnection(dbURL, username, password);

            Statement statement = connection.createStatement();
            sqlStatement = sqlStatement.trim();
            String sqlType = sqlStatement.substring(0, 6);

            if (sqlType.equalsIgnoreCase("select")) {
                ResultSet resultSet = statement.executeQuery(sqlStatement);
                // Class SQLUtil vẫn dùng bình thường vì nó xử lý ResultSet chuẩn
                sqlResult = SQLUtil.getHtmlTable(resultSet);
                resultSet.close();
            } else {
                int i = statement.executeUpdate(sqlStatement);
                if (i == 0) {
                    sqlResult = "<p>The statement executed successfully.</p>";
                } else {
                    sqlResult = "<p>The statement executed successfully.<br>" + 
                                i + " row(s) affected.</p>";
                }
            }

            statement.close();
            connection.close();

        } catch (ClassNotFoundException e) {
            sqlResult = "<p>Error loading the PostgreSQL driver: " + e.getMessage() + "</p>";
        } catch (SQLException e) {
            sqlResult = "<p>Error executing the SQL statement: <br>" + e.getMessage() + "</p>";
        }

        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        String url = "/index.jsp";
        getServletContext().getRequestDispatcher(url).forward(request, response);
    }
}