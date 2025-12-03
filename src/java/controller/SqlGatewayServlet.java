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
import dao.DBConnection;

@WebServlet(urlPatterns = {"/sqlGateway"})
public class SqlGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";

        // Gọi hàm lấy kết nối từ class riêng biệt
        Connection connection = DBConnection.getConnection();

        if (connection == null) {
            // Xử lý trường hợp không kết nối được
            sqlResult = "<p>Error: Unable to establish database connection.</p>";
        } else {
            try {
                Statement statement = connection.createStatement();
                sqlStatement = sqlStatement.trim();
                String sqlType = sqlStatement.length() >= 6 ? sqlStatement.substring(0, 6) : "";

                if (sqlType.equalsIgnoreCase("select")) {
                    ResultSet resultSet = statement.executeQuery(sqlStatement);
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
                connection.close(); // Đóng kết nối sau khi dùng xong

            } catch (SQLException e) {
                sqlResult = "<p>Error executing the SQL statement: <br>" + e.getMessage() + "</p>";
            }
        }

        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        String url = "/index.jsp";
        getServletContext().getRequestDispatcher(url).forward(request, response);
    }
}