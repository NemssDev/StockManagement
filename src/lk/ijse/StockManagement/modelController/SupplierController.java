package lk.ijse.StockManagement.modelController;

import lk.ijse.StockManagement.db.DBConnection;
import lk.ijse.StockManagement.to.Supplier;
import lk.ijse.StockManagement.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierController {
    public static String supplierID;


    public static ResultSet loadAllSupplierIds() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT id FROM supplier");
    }

    public static ResultSet getLastSupplierId() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT id FROM supplier ORDER BY  id");
    }

    public static boolean addSupplier(Supplier supplier) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil(
                "INSERT INTO supplier(id , name, address ,phone_number , email) " +
                        "VALUES (?, ?, ?, ?, ?)",
                supplier.getId(),
                supplier.getName(),
                supplier.getAddress(),
                supplier.getPhoneNumber(),
                supplier.getEmail()
        );
    }


    public static ResultSet getSupplierById(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM supplier WHERE id=?", id);
    }

    public static boolean removeSupplier(String reference) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("DELETE FROM supplier WHERE id=?", reference);
    }

    public static ResultSet getAllSuppliers() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM supplier");
    }

    /*public static ArrayList<EmployeeAttendanceProjection> findArticlesEmployeeByDate(String date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT employee.employee_id,employee.fist_name,employee.last_name,employee.roll,employee_attendance.date,employee_attendance.time FROM employee_attendance INNER JOIN Employee ON Employee_Attendance.employee_id = Employee.employee_id WHERE employee_attendance.date LIKE ?";
        return setEmployeeProjection(CrudUtil.crudUtil(sql, date + "%"));
    }*/

    public static boolean updateSupplier(Supplier supplier) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("UPDATE supplier SET name=? ,address=? , phone_number=? , email=?  WHERE id=?",
                supplier.getName(),
                supplier.getAddress(),
                supplier.getPhoneNumber(),
                supplier.getEmail(),
                supplier.getId()
        );
    }

    public static ResultSet searchSupplierByName(String name) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM supplier WHERE name LIKE ?", name + "%");
    }


    public static List<String> loadAllSuppliers() throws SQLException, ClassNotFoundException {
        List<String> references = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet rs = con.prepareStatement("SELECT id FROM supplier").executeQuery();
        while (rs.next()) {

            references.add(rs.getString("id"));
        }
        return references;
    }

    public static List<String> searchSuppliers(String keyword) throws SQLException, ClassNotFoundException {
        List<String> results = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement(
                "SELECT id , name, address, phone_number, email " +
                        "FROM supplier " +
                        "Where id LIKE ? OR " +
                        "name LIKE ? OR " +
                        "address LIKE ? OR " +
                        "phone_number LIKE ? OR " +
                        "email LIKE ? "
        );

        for (int i = 1; i <= 5; i++) {
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

