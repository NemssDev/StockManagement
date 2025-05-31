package lk.ijse.StockManagement.modelController;

import lk.ijse.StockManagement.db.DBConnection;
import lk.ijse.StockManagement.to.Service;
import lk.ijse.StockManagement.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceController {
    public static String serviceID;


    public static ResultSet loadAllServiceIds() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT id FROM service");
    }

    public static ResultSet getLastServiceId() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT id FROM service ORDER BY  id");
    }

    public static ResultSet getServiceNames() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT name FROM service");
    }

    public static boolean addService(Service service) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil(
                "INSERT INTO service(id , name, description , type) " +
                        "VALUES (?, ?, ?, ?)",
                service.getId(),
                service.getName(),
                service.getDesciption(),
                service.getType()
        );
    }


    public static ResultSet getServiceById(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM service WHERE id=?", id);
    }

    public static boolean removeService(String reference) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("DELETE FROM service WHERE id=?", reference);
    }

    public static ResultSet getAllServices() throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM service");
    }

    /*public static ArrayList<EmployeeAttendanceProjection> findArticlesEmployeeByDate(String date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT employee.employee_id,employee.fist_name,employee.last_name,employee.roll,employee_attendance.date,employee_attendance.time FROM employee_attendance INNER JOIN Employee ON Employee_Attendance.employee_id = Employee.employee_id WHERE employee_attendance.date LIKE ?";
        return setEmployeeProjection(CrudUtil.crudUtil(sql, date + "%"));
    }*/

    public static boolean updateService(Service service) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("UPDATE service SET name=? ,type=? , description=?  WHERE id=?",
                service.getName(),
                service.getType(),
                service.getDesciption(),
                service.getId()
        );
    }

    public static ResultSet searchServiceByName(String name) throws SQLException, ClassNotFoundException {
        return CrudUtil.crudUtil("SELECT * FROM service WHERE name LIKE ?", name + "%");
    }


    public static List<String> loadAllServices() throws SQLException, ClassNotFoundException {
        List<String> references = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        ResultSet rs = con.prepareStatement("SELECT id FROM service").executeQuery();
        while (rs.next()) {

            references.add(rs.getString("id"));
        }
        return references;
    }

    public static List<String> searchServices(String keyword) throws SQLException, ClassNotFoundException {
        List<String> results = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement(
                "SELECT id , name, type, description " +
                        "FROM service " +
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

