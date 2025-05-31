package lk.ijse.StockManagement.modelController;

import lk.ijse.StockManagement.db.DBConnection;
import lk.ijse.StockManagement.to.Storage;
import lk.ijse.StockManagement.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StorageController {
    public static String storageID;


    public static ResultSet loadAllStorageIds() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT id FROM storage");
    }

    public static ResultSet getLastStorageId() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT id FROM storage ORDER BY  id");
    }

    public static boolean addStorage(Storage storage) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil(
                "INSERT INTO storage(id , location_name, location_type , location_description) " +
                        "VALUES (?, ?, ?, ?)",
                storage.getId(),
                storage.getName(),
                storage.getType(),
                storage.getDesciption()
        );
    }


    public static ResultSet getStorageById(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM storage WHERE id=? ", id);
    }


    public static boolean removeStorage(String reference) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("DELETE FROM storage WHERE id=?", reference);
    }

    public static ResultSet getAllStorages() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM storage order by id");
    }

    public static ResultSet getAllStorageNames() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT location_name FROM storage");
    }

    /*public static ArrayList<EmployeeAttendanceProjection> findArticlesEmployeeByDate(String date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT employee.employee_id,employee.fist_name,employee.last_name,employee.roll,employee_attendance.date,employee_attendance.time FROM employee_attendance INNER JOIN Employee ON Employee_Attendance.employee_id = Employee.employee_id WHERE employee_attendance.date LIKE ?";
        return setEmployeeProjection(CrudUtil.crudUtil(sql, date + "%"));
    }*/

    public static boolean updateStorage(Storage storage) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("UPDATE storage SET location_name=? ,location_type=? , location_description=?  WHERE id=?",
                storage.getName(),
                storage.getType(),
                storage.getDesciption(),
                storage.getId()
        );
    }

    public static ResultSet searchStorageByName(String name) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM storage WHERE name LIKE ?", name + "%");
    }


    public static List<String> loadAllStorages() throws SQLException, ClassNotFoundException {
        List<String> references = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet rs = con.prepareStatement("SELECT id FROM storage order by id").executeQuery();
        while (rs.next()) {

            references.add(rs.getString("id"));
        }
        return references;
    }

    public static List<String> searchStorages(String keyword) throws SQLException, ClassNotFoundException {
        List<String> results = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement(
                "SELECT id , location_name, location_type, location_description " +
                        "FROM storage " +
                        "Where id LIKE ? OR " +
                        "location_name LIKE ? OR " +
                        "location_type LIKE ? OR " +
                        "location_description LIKE ?"
        );

        for (int i = 1; i <= 4; i++) {
            ps.setString(i, "%" + keyword + "%");
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String row = rs.getString("id");
            System.out.println("rs:" + rs);
            results.add(row);
        }

        return results;
    }


}

