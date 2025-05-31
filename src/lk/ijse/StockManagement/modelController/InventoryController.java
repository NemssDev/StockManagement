package lk.ijse.StockManagement.modelController;

import lk.ijse.StockManagement.db.DBConnection;
import lk.ijse.StockManagement.to.Item;
import lk.ijse.StockManagement.util.CrudUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryController {
    public static String itemID;


    public static ResultSet loadAllItemIds() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT id FROM inventory");
    }

    public static ResultSet getLastItemId() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT id FROM inventory ORDER BY  id");
    }

    public static boolean addItem(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil(
                "INSERT INTO inventory(id, name, quantity ,location , item_condition ,last_updated ) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getLocation(),
                item.getCondtion(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    public static ResultSet getItem(String selectedArticle , String selectedStorage) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT quantity FROM inventory WHERE name=? and location=?", selectedArticle , selectedStorage);
    }


    public static ResultSet getItemById(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM inventory WHERE id=?", id);
    }

    public static ResultSet getStorageNames(String article) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT location FROM inventory WHERE name=?", article);
    }





    public static boolean removeItem(String reference) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("DELETE FROM inventory WHERE id=?", reference);
    }

    public static ResultSet getAllItems() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM inventory");
    }

    /*public static ArrayList<EmployeeAttendanceProjection> findArticlesEmployeeByDate(String date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT employee.employee_id,employee.fist_name,employee.last_name,employee.roll,employee_attendance.date,employee_attendance.time FROM employee_attendance INNER JOIN Employee ON Employee_Attendance.employee_id = Employee.employee_id WHERE employee_attendance.date LIKE ?";
        return setEmployeeProjection(CrudUtil.crudUtil(sql, date + "%"));
    }*/

    public static boolean updateItem(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("UPDATE inventory SET quantity=? , location=? , item_condition=? , last_updated=?  WHERE id=?",
                item.getQuantity(),
                item.getLocation(),
                item.getCondtion(),
                new Timestamp(System.currentTimeMillis()),
                item.getId()
        );
    }

    public static boolean updateOItem(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("UPDATE inventory SET quantity=quantity-? , last_updated=?  WHERE location = ? and name=?",
                item.getQuantity(),
                new Timestamp(System.currentTimeMillis()),
                item.getLocation(),
                item.getName()
        );
    }

    public static boolean updateNItem(Item item) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.crudUtil(
                "SELECT * FROM inventory WHERE location = ? AND name = ?",
                item.getLocation(),
                item.getName()
        );

        if (resultSet.next()) {
            // If exists -> UPDATE
            return CrudUtil.crudUtil(
                    "UPDATE inventory SET quantity = quantity + ?, last_updated = ? WHERE location = ? AND name = ?",
                    item.getQuantity(),
                    new Timestamp(System.currentTimeMillis()),
                    item.getLocation(),
                    item.getName()
            );
        } else {
            // If not exists -> INSERT
            return CrudUtil.crudUtil(
                    "INSERT INTO inventory (id , name, location, quantity, last_updated , item_condition) VALUES (?,? ,?, ?, ?, ?)",
                    item.getId(),
                    item.getName(),
                    item.getLocation(),
                    item.getQuantity(),
                    new Timestamp(System.currentTimeMillis()),
                    item.getCondtion()
            );
        }
    }


    public static ResultSet searchItemByName(String name) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM inventory WHERE name LIKE ?", name + "%");
    }



    public static List<String> loadAllItems() throws SQLException, ClassNotFoundException {
        List<String> references = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet rs = con.prepareStatement("SELECT id FROM inventory").executeQuery();
        while (rs.next()) {

            references.add(rs.getString("id"));
        }
        return references;
    }

    public static List<String> searchItems(String keyword) throws SQLException, ClassNotFoundException {
        List<String> results = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement(
                "SELECT id , name, quantity, location , item_condition , last_updated " +
                        "FROM inventory " +
                        "Where id LIKE ? OR " +
                        "name LIKE ? OR " +
                        "quantity LIKE ? OR " +
                        "location LIKE ? OR " +
                        "item_condition LIKE ? OR " +
                        "last_updated LIKE ?"
        );

        for (int i = 1; i <= 4; i++) {
            ps.setString(i, "%" + keyword + "%");
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String row = rs.getString("id");
            results.add(row);
        }

        return results;
    }


}

