package com.marcinpisarski.databasegui.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Datasource {

    // Connection string
    public static final String DB_ADDRESS = "jdbc:sqlserver://localhost;";
    public static final String DB_NAME = "databaseName=HR;";
    public static final String DB_SECURITY = "integratedSecurity=true;";
    public static final String CONNECTION_STRING = DB_ADDRESS + DB_NAME + DB_SECURITY;

    // SQL tables and columns
    public static final String TABLE_EMPLOYEES = "Employees";
    public static final String EMPLOYEES_COLUMN_ID = "ID";
    public static final String EMPLOYEES_COLUMN_FIRST_NAME = "FIRSTNAME";
    public static final String EMPLOYEES_COLUMN_LAST_NAME = "LASTNAME";
    public static final String EMPLOYEES_COLUMN_DEPARTMENT_ID = "DEPARTMENT_ID";

    public static final String TABLE_DEPARTMENTS = "Departments";
    public static final String DEPARTMENTS_COLUMN_ID = "ID";
    public static final String DEPARTMENTS_COLUMN_NAME = "NAME";

    public static final String TABLE_MAILING_GROUPS = "MailingGroups";
    public static final String MAILING_GROUPS_COLUMN_ID = "ID";
    public static final String MAILING_GROUPS_COLUMN_NAME = "NAME";

    public static final String TABLE_EMPLOYEES_MAILING_GROUPS = "EmployeesMailing";
    public static final String EMPLOYEES_MAILING_GROUPS_COLUMN_ID = "ID";
    public static final String EMPLOYEES_MAILING_GROUPS_COLUMN_EMPLOYEE_ID = "EMPLOYEE_ID";
    public static final String EMPLOYEES_MAILING_GROUPS_COLUMN_MAILING_ID = "MAILING_ID";

    // SQL procedures
    public static final String INSERT_EMPLOYEE = "INSERT INTO " + TABLE_EMPLOYEES + "(" + EMPLOYEES_COLUMN_FIRST_NAME +
            ", " + EMPLOYEES_COLUMN_LAST_NAME + ", " + EMPLOYEES_COLUMN_DEPARTMENT_ID + ") VALUES(?, ?, ?)";
    public static final String UPDATE_EMPLOYEE = "UPDATE " + TABLE_EMPLOYEES +
            " SET " + EMPLOYEES_COLUMN_FIRST_NAME + " = ? , " + EMPLOYEES_COLUMN_LAST_NAME + " = ? ," +
            EMPLOYEES_COLUMN_DEPARTMENT_ID + " = ? " + " WHERE ID = ?";
    public static final String DELETE_EMPLOYEE = "DELETE FROM " + TABLE_EMPLOYEES + " WHERE " + EMPLOYEES_COLUMN_ID + " = ?";
    public static final String SELECT_EMPLOYEES = "SELECT e.ID As empID, e.FIRSTNAME, e.LASTNAME, e.DEPARTMENT_ID, d.NAME as DepartmentName FROM Employees e LEFT JOIN Departments d" +
            " ON e.DEPARTMENT_ID = d.ID";

    public static final String INSERT_DEPARTMENT = "INSERT INTO " + TABLE_DEPARTMENTS + "(" + DEPARTMENTS_COLUMN_NAME +
            ") VALUES(?)";
    public static final String SELECT_DEPARTMENTS = "SELECT " + DEPARTMENTS_COLUMN_ID + ", " + DEPARTMENTS_COLUMN_NAME +
            " FROM " + TABLE_DEPARTMENTS;
    public static final String SELECT_SINGLE_DEPARTMENT = "SELECT " + DEPARTMENTS_COLUMN_ID + ", " + DEPARTMENTS_COLUMN_NAME +
            " FROM " + TABLE_DEPARTMENTS + " WHERE ID = ?";

    public static final String INSERT_GROUP = "INSERT INTO " + TABLE_MAILING_GROUPS +
            "(" + MAILING_GROUPS_COLUMN_NAME + ") VALUES(?)";
    public static final String SELECT_GROUPS = "SELECT " + MAILING_GROUPS_COLUMN_ID + ", " + MAILING_GROUPS_COLUMN_NAME +
            " FROM " + TABLE_MAILING_GROUPS;

    public static final String INSERT_EMPLOYEE_MAILING = "INSERT INTO " + TABLE_EMPLOYEES_MAILING_GROUPS +
            "(" + EMPLOYEES_MAILING_GROUPS_COLUMN_EMPLOYEE_ID + ", " + EMPLOYEES_MAILING_GROUPS_COLUMN_MAILING_ID + ") VALUES(?, ?)";
    public static final String DELETE_EMPLOYEE_MAILING = "DELETE FROM " + TABLE_EMPLOYEES_MAILING_GROUPS +
            " WHERE " + EMPLOYEES_MAILING_GROUPS_COLUMN_EMPLOYEE_ID + " = ?";
    public static final String SELECT_EMPLOYEE_GROUPS = "SELECT mg." + EMPLOYEES_MAILING_GROUPS_COLUMN_ID  + ", mg." +
            MAILING_GROUPS_COLUMN_NAME + " FROM " + TABLE_EMPLOYEES_MAILING_GROUPS + " em INNER JOIN " +
            TABLE_MAILING_GROUPS + " mg ON em." + EMPLOYEES_MAILING_GROUPS_COLUMN_MAILING_ID + "= mg." +
            MAILING_GROUPS_COLUMN_ID + " WHERE em." + EMPLOYEES_MAILING_GROUPS_COLUMN_EMPLOYEE_ID + " = ?";

    private Connection conn;

    private PreparedStatement insertIntoEmployees;
    private PreparedStatement deleteFromEmployees;
    private PreparedStatement updateEmployees;
    private PreparedStatement selectEmployees;

    private PreparedStatement insertIntoDepartments;
    private PreparedStatement selectDepartments;
    private PreparedStatement selectSingleDepartment;

    private PreparedStatement insertIntoGroups;
    private PreparedStatement selectGroups;

    private PreparedStatement insertIntoEmployeeMailing;
    private PreparedStatement deleteFromEmployeeMailing;
    private PreparedStatement selectEmployeeMailing;

    private Datasource() {
    }

    private static Datasource instance = new Datasource();

    public static Datasource getInstance() {
        return instance;
        // Datasource.getInstance().methodName();
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);

            insertIntoEmployees = conn.prepareStatement(INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS);
            deleteFromEmployees = conn.prepareStatement(DELETE_EMPLOYEE);
            updateEmployees = conn.prepareStatement(UPDATE_EMPLOYEE);
            selectEmployees = conn.prepareStatement(SELECT_EMPLOYEES);

            insertIntoDepartments = conn.prepareStatement(INSERT_DEPARTMENT);
            selectDepartments = conn.prepareStatement(SELECT_DEPARTMENTS);
            selectSingleDepartment = conn.prepareStatement(SELECT_SINGLE_DEPARTMENT);

            insertIntoGroups = conn.prepareStatement(INSERT_GROUP);
            selectGroups = conn.prepareStatement(SELECT_GROUPS);

            insertIntoEmployeeMailing = conn.prepareStatement(INSERT_EMPLOYEE_MAILING);
            deleteFromEmployeeMailing = conn.prepareStatement(DELETE_EMPLOYEE_MAILING);
            selectEmployeeMailing = conn.prepareStatement(SELECT_EMPLOYEE_GROUPS);

            return true;
        } catch(SQLException e) {
            System.out.println("Couldn't open connection: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {

            // Employees
            if (insertIntoEmployees != null) {
                insertIntoEmployees.close();
            }

            if (deleteFromEmployees != null) {
                deleteFromEmployees.close();
            }

            if (updateEmployees != null) {
                updateEmployees.close();
            }

            if (selectEmployees != null) {
                selectEmployees.close();
            }

            // Departments
            if (insertIntoDepartments != null) {
                insertIntoDepartments.close();
            }

            if (selectDepartments != null) {
                selectDepartments.close();
            }

            if (selectSingleDepartment != null) {
                selectSingleDepartment.close();
            }

            // Mailing groups
            if (selectGroups != null) {
                selectGroups.close();
            }

            if (insertIntoGroups != null) {
                insertIntoGroups.close();
            }

            // Employees in mailing groups
            if (insertIntoEmployeeMailing != null) {
                insertIntoEmployeeMailing.close();
            }

            if (deleteFromEmployeeMailing != null) {
                deleteFromEmployeeMailing.close();
            }

            if (selectEmployeeMailing != null) {
                selectEmployeeMailing.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public List<Employee> queryEmployees() {
        try {
            ResultSet results = selectEmployees.executeQuery();
            List<Employee> employees = new ArrayList<>();
            while(results.next()) {
                Employee employee = new Employee();
                employee.setId(results.getInt("empID"));
                employee.setFirstName(results.getString("FIRSTNAME"));
                employee.setLastName(results.getString("LASTNAME"));

                employee.setDepartmentId(results.getInt("DEPARTMENT_ID"));
                employee.setDepartmentName(results.getString("DepartmentName"));

                employees.add(employee);
            }

            return employees;

        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Group> queryGroups() {
        try {
            ResultSet results = selectGroups.executeQuery();
            List<Group> groups = new ArrayList<Group>();
            while (results.next()) {
                Group group = new Group();
                group.setId(results.getInt(1));
                group.setName(results.getString(2));
                groups.add(group);
            }
            return groups;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Department> queryDepartments() {
        try {
            ResultSet results = selectDepartments.executeQuery();
            List<Department> departments = new ArrayList<Department>();
            while (results.next()) {
                Department department = new Department();
                department.setId(results.getInt(1));
                department.setName(results.getString(2));
                departments.add(department);
            }
            return departments;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Group> queryEmployeesMailing(int employeeId) {

        try {
            selectEmployeeMailing.setInt(1, employeeId);
            ResultSet results = selectEmployeeMailing.executeQuery();

            List<Group> groups = new ArrayList<>();
            while (results.next()) {
                Group group = new Group();
                group.setId(results.getInt(1));
                group.setName(results.getString(2));
                groups.add(group);
            }

            return groups;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public void insertEmployee(String firstName, String lastName, int departmentId, List<Group> groups) {

        try {
            conn.setAutoCommit(false);
            insertIntoEmployees.setString(1, firstName);
            insertIntoEmployees.setString(2, lastName);
            insertIntoEmployees.setInt(3, departmentId);

            int affectedRows = insertIntoEmployees.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert Employee");
            }

            ResultSet generatedKeys = insertIntoEmployees.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get id for Employee");
            }

            int employeeInsertedId = generatedKeys.getInt(1);

            //insertEmployeesMailing(employeeInsertedId, groupId);

            for (Iterator<Group> iterator = groups.iterator(); iterator.hasNext();) {
                Group group = (Group) iterator.next();
                insertIntoEmployeeMailing.setInt(1, employeeInsertedId);
                insertIntoEmployeeMailing.setInt(2, group.getId());

                // Add to batch
                insertIntoEmployeeMailing.addBatch();
            }

            // Execute the batch
            int [] insertCounts = insertIntoEmployeeMailing.executeBatch();
            System.out.println(Arrays.toString(insertCounts));

            // If all good commit the changes
            conn.commit();

        } catch (Exception e) {
            System.out.println("Insert Employee exception: " + e.getMessage());
            try {
                System.out.println("Rollback operation");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rollback error: " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit" + e.getMessage());
            }
        }
    }

    public void updateEmployee(String firstName, String lastName, int employeeId, int departmentId, List<Group> groups) {

        try {
            conn.setAutoCommit(false);
            updateEmployees.setString(1, firstName);
            updateEmployees.setString(2, lastName);
            updateEmployees.setInt(3, departmentId);
            updateEmployees.setInt(4, employeeId);

            int affectedRows = updateEmployees.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert Employee");
            }

            // Delete employees mailing groups
            deleteEmployeesMailing(employeeId);

            // Insert employees new mailing groups
            for (Iterator<Group> iterator = groups.iterator(); iterator.hasNext();) {
                Group group = (Group) iterator.next();
                insertIntoEmployeeMailing.setInt(1, employeeId);
                insertIntoEmployeeMailing.setInt(2, group.getId());

                // Add to batch
                insertIntoEmployeeMailing.addBatch();
            }

            // Execute the batch
            int [] insertCounts = insertIntoEmployeeMailing.executeBatch();
            System.out.println(Arrays.toString(insertCounts));

            // If all good commit the changes
            conn.commit();

        } catch (Exception e) {
            System.out.println("Update Employee exception: " + e.getMessage());
            try {
                System.out.println("Rollback operation");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rollback error: " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit" + e.getMessage());
            }
        }
    }

    public void deleteEmployee(Employee employee) {

        try {
            conn.setAutoCommit(false);

            deleteFromEmployeeMailing.setInt(1, employee.getId());
            deleteFromEmployeeMailing.execute();
            //deleteEmployeesMailing(employee.getId());

            // Try delete Employee
            deleteFromEmployees.setInt(1, employee.getId());

            int affectedRows = deleteFromEmployees.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't delete Employee");
            }

            // If all good commit the changes
            conn.commit();

        } catch (Exception e) {
            System.out.println("Delete Employee exception: " + e.getMessage());
            try {
                System.out.println("Rollback operation");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rollback error: " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit" + e.getMessage());
            }
        }
    }

    private void deleteEmployeesMailing(int employeeId) throws SQLException {

        try {
            deleteFromEmployeeMailing.setInt(1, employeeId);
            deleteFromEmployeeMailing.execute();
        } catch (SQLException e) {
            throw new SQLException("Couldn't delete Employee from mailing groups: " + e.getMessage());
        }
    }

    private void insertEmployeesMailing(int employeeId, int groupId) throws SQLException {

        insertIntoEmployeeMailing.setInt(1, employeeId);
        insertIntoEmployeeMailing.setInt(2, groupId);

        int affectedRows = insertIntoEmployeeMailing.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't insert Employee Mailing");
        }
    }
}