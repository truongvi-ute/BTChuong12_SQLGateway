package sql; // Nhớ đổi thành package controller nhé

import java.sql.*;

public class SQLUtil {

    public static String getHtmlTable(ResultSet results) throws SQLException {
        StringBuilder htmlTable = new StringBuilder();
        ResultSetMetaData metaData = results.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 1. SỬA LỖI: Chỉ dùng 1 thẻ table và thêm style border-collapse
        htmlTable.append("<table style='border: 1px solid black; border-collapse: collapse; width: 100%;'>");

        // 2. Add header row
        htmlTable.append("<tr>");
        for (int i = 1; i <= columnCount; i++) {
            // SỬA LỖI: Thêm style border cho từng ô tiêu đề
            htmlTable.append("<th style='border: 1px solid black; padding: 5px; background-color: #f2f2f2;'>");
            htmlTable.append(metaData.getColumnName(i));
            htmlTable.append("</th>");
        }
        htmlTable.append("</tr>");

        // 3. Add all other rows
        while (results.next()) {
            htmlTable.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                // SỬA LỖI: Thêm style border cho từng ô dữ liệu
                htmlTable.append("<td style='border: 1px solid black; padding: 5px;'>");
                htmlTable.append(results.getString(i));
                htmlTable.append("</td>");
            }
            htmlTable.append("</tr>");
        }

        htmlTable.append("</table>");
        return htmlTable.toString();
    }
}