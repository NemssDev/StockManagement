package lk.ijse.StockManagement.modelController;

import lk.ijse.StockManagement.db.DBConnection;
import lk.ijse.StockManagement.to.Article;
import lk.ijse.StockManagement.to.Order;
import lk.ijse.StockManagement.util.CrudUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    public static String orderID;


    public static ResultSet loadAllOrderIds() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT order_id FROM internal_order UNION ALL SELECT order_id FROM external_order");
    }

    public static ResultSet loadIntOrderIds() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT order_id FROM internal_order");
    }

    public static ResultSet loadExtOrderIds() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT order_id FROM external_order");
    }


    public static boolean addInternalOrder(Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil(
                "INSERT INTO internal_order (order_id, article ,service , quantity ,from_storage , to_storage, order_date) " +
                        "VALUES (?, ?, ?, ?, ?, ? ,?)",
                order.getOrder_id(),
                order.getArticle(),
                order.getService(),
                order.getQuantity(),
                order.getFromStorage(),
                order.getToStorage(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    public static boolean addExternalOrder(Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil(
                "INSERT INTO external_order (order_id, article ,service , quantity ,to_storage , order_date) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                order.getOrder_id(),
                order.getArticle(),
                order.getService(),
                order.getQuantity(),
                order.getFromStorage(),
                new Timestamp(System.currentTimeMillis())
        );
    }


    public static ResultSet getOrderById(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM internal_order WHERE order_id = ? UNION ALL SELECT * FROM external_order WHERE order_id = ?", id, id);
    }

    public static boolean removeOrder(String reference) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil(
                "DELETE FROM internal_order WHERE order_id = ? OR order_id IN (SELECT order_id FROM external_order WHERE order_id = ?)",
                reference, reference
        );
    }

    public static ResultSet getAllOrders() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM internal_order UNION ALL SELECT * FROM external_order");
    }

    public static ResultSet getInternalOrders() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM internal_order ");
    }

    public static ResultSet getExternalOrders() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM  external_order");
    }

    /*public static ArrayList<EmployeeAttendanceProjection> findArticlesEmployeeByDate(String date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT employee.employee_id,employee.fist_name,employee.last_name,employee.roll,employee_attendance.date,employee_attendance.time FROM employee_attendance INNER JOIN Employee ON Employee_Attendance.employee_id = Employee.employee_id WHERE employee_attendance.date LIKE ?";
        return setEmployeeProjection(CrudUtil.crudUtil(sql, date + "%"));
    }*/

    public static boolean updateArticle(Article article) throws SQLException, ClassNotFoundException {
        System.out.println("article.getIdCat(): " + article.getIdCat());
        return CrudUtil.crudUtil("UPDATE article SET reference=? , name=? ,category=? ,is_consumable=?,expiration_date=?,quantity=?,min_stock=?,designation=?, prix_achat=?, is_active=? WHERE reference=?",
                article.getReference(),
                article.getName(),
                article.getIdCat(),
                article.isConsumable(),
                article.getExpirationDate(),
                article.getQuantity(),
                article.getMinStock(),
                article.getDesignation(),
                article.getPrixAchat(),
                article.getIsActive(),
                article.getReference()
        );
    }

    public static ResultSet searchArticleByName(String name) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM article WHERE designation LIKE ?", name + "%");
    }

    public static ResultSet getArticlesByCategory(String categoryId) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM article WHERE category=?", categoryId);
    }

    public static ResultSet getExpiringArticles(String date) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM article WHERE expiration_date=?", date);
    }

    public static ResultSet getActiveArticles() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM article WHERE is_active=1");
    }

    public static ResultSet getArticlesBelowMinStock() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM article WHERE quantity < min_stock");
    }

    public static boolean articleExists(String reference) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.crudUtil("SELECT reference FROM article WHERE reference=?", reference);
        return rs.next();
    }

    public static List<String> loadAllOrders() throws SQLException, ClassNotFoundException {
        List<String> references = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet rs = con.prepareStatement("SELECT order_id FROM internal_order UNION ALL SELECT order_id FROM external_order").executeQuery();
        while (rs.next()) {

            references.add(rs.getString("order_id"));
        }
        return references;
    }

    public static List<String> searchOrders(String keyword) throws SQLException, ClassNotFoundException {
        List<String> results = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement(
                "SELECT order_id, service, article, quantity, storage, order_date\n" +
                        "FROM internal_order\n" +
                        "WHERE order_id LIKE ? OR \n" +
                        "      service LIKE ? OR \n" +
                        "      article LIKE ? OR \n" +
                        "      quantity LIKE ? OR \n" +
                        "      storage LIKE ? OR \n" +
                        "      CAST(order_date AS CHAR) LIKE ?\n" +
                        "UNION\n" +
                        "SELECT order_id, service, article, quantity, storage, order_date\n" +
                        "FROM external_order\n" +
                        "WHERE order_id LIKE ? OR \n" +
                        "      service LIKE ? OR \n" +
                        "      article LIKE ? OR \n" +
                        "      quantity LIKE ? OR \n" +
                        "      storage LIKE ? OR \n" +
                        "      CAST(order_date AS CHAR) LIKE ?;\n"
        );

        for (int i = 1; i <= 12; i++) {
            ps.setString(i, "%" + keyword + "%");
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String row = rs.getString("order_id");
            results.add(row);
        }

        return results;
    }


    // 🔥 New method to find articles by month
    public static List<String> findArticlesByMonth(String monthYear) throws SQLException, ClassNotFoundException {
        List<String> articles = new ArrayList<>();
        Connection connection = DBConnection.getInstance().getConnection();

        String query = "SELECT reference FROM article WHERE DATE_FORMAT(dateCr, '%Y-%m') = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, monthYear);  // e.g., "2025-04"

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            articles.add(resultSet.getString("reference"));
        }

        resultSet.close();
        statement.close();

        return articles;
    }
}

