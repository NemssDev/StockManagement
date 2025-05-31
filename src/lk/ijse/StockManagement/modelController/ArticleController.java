package lk.ijse.StockManagement.modelController;

import lk.ijse.StockManagement.db.DBConnection;
import lk.ijse.StockManagement.to.Article;
import lk.ijse.StockManagement.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleController {
    public static String articleID;


    public static ResultSet loadAllArticleIds() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT reference FROM article");
    }

    public static ResultSet getLastArticleId() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT reference FROM article ORDER BY LENGTH(reference), reference");
    }

    public static boolean addArticle(Article article) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil(
                "INSERT INTO article(reference, name, category ,is_consumable , expiration_date ,quantity , min_stock , designation ,prix_achat , is_active) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                article.getReference(),
                article.getName(),
                article.getCategory(),
                article.isConsumable(),
                article.getExpirationDate(),
                article.getQuantity(),
                article.getMinStock(),
                article.getDesignation(),
                article.getPrixAchat(),
                article.getIsActive()  // Make sure you're passing the right number of parameters.
        );
    }


    public static ResultSet getArticleById(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM article WHERE reference=?", id);
    }

    public static ResultSet getArticleNames() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT name FROM article");
    }

    public static boolean removeArticle(String reference) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("DELETE FROM article WHERE reference=?", reference);
    }

    public static ResultSet getAllArticles() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM article");
    }

    public static ResultSet getQuantityOfArticle(String name) throws SQLException , ClassNotFoundException{
        return CrudUtil.crudUtil("SELECT quantity FROM article where name=?", name);
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

    public static boolean updateQuantity(Article article) throws SQLException ,ClassNotFoundException{
        return CrudUtil.crudUtil("UPDATE article SET quantity=quantity-? WHERE name=?",
                article.getQuantity(),
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

    public static List<String> loadAllArticles() throws SQLException, ClassNotFoundException {
        List<String> references = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet rs = con.prepareStatement("SELECT reference FROM article").executeQuery();
        while (rs.next()) {

            references.add(rs.getString("reference"));
        }
        return references;
    }

    public static List<String> searchArticles(String keyword) throws SQLException, ClassNotFoundException {
        List<String> results = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement(
                "SELECT reference, name, category, expiration_date, quantity, prix_achat " +
                        "FROM article " +
                        "WHERE reference LIKE ? OR " +
                        "name LIKE ? OR " +
                        "category LIKE ? OR " +
                        "CAST(expiration_date AS CHAR) LIKE ? OR " +
                        "CAST(quantity AS CHAR) LIKE ? OR " +
                        "CAST(prix_achat AS CHAR) LIKE ?"
        );

        for (int i = 1; i <= 6; i++) {
            ps.setString(i, "%" + keyword + "%");
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String row = rs.getString("reference");
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

