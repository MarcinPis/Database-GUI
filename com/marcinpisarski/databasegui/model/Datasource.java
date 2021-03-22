package com.marcinpisarski.databasegui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    // Azure instance of SQL Server Connection string
    public static final String AZURE_DB_ADDRESS = "jdbc:sqlserver://.database.windows.net:1433;";
    public static final String AZURE_DB_NAME = "database=;";
    public static final String AZURE_USERNAME = "user=;";
    public static final String AZURE_USERNAME_PASSWORD = "password=;";
    public static final String AZURE_DB_SECURITY = "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    public static final String AZURE_CONNECTION_STRING = AZURE_DB_ADDRESS + AZURE_DB_NAME + AZURE_USERNAME + AZURE_USERNAME_PASSWORD + AZURE_DB_SECURITY;

    // Local instance of SQL Server Connection string
    public static final String LOCAL_DB_ADDRESS = "jdbc:sqlserver://localhost;";
    public static final String LOCAL_DB_NAME = "databaseName=HR;";
    public static final String LOCAL_DB_SECURITY = "integratedSecurity=true;";
    public static final String LOCAL_CONNECTION_STRING = LOCAL_DB_ADDRESS + LOCAL_DB_NAME + LOCAL_DB_SECURITY;

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
    public static final String SELECT_EMPLOYEES = "SELECT e." + EMPLOYEES_COLUMN_ID + " As empID, e." +
            EMPLOYEES_COLUMN_FIRST_NAME + ", e." + EMPLOYEES_COLUMN_LAST_NAME + ", e." + EMPLOYEES_COLUMN_DEPARTMENT_ID +
            ", d." + DEPARTMENTS_COLUMN_NAME + " as DepartmentName FROM " + TABLE_EMPLOYEES + " e LEFT JOIN " +
            TABLE_DEPARTMENTS + " d ON e." + EMPLOYEES_COLUMN_DEPARTMENT_ID + " = d." + DEPARTMENTS_COLUMN_ID;

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

    private static final Datasource instance = new Datasource();

    public static Datasource getInstance() {
        return instance; // Datasource.getInstance().methodName();
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(LOCAL_CONNECTION_STRING);
            //conn = DriverManager.getConnection(AZURE_CONNECTION_STRING);

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

    public ObservableList<EmployeeTableRow> queryEmployees() {

        try {
            ResultSet results = selectEmployees.executeQuery();
            ObservableList<EmployeeTableRow> employees = FXCollections.observableArrayList();
            while(results.next()) {
                EmployeeTableRow employee = new EmployeeTableRow();
                employee.setEmployeeId(results.getInt("empID"));
                employee.setFirstName(results.getString("FIRSTNAME"));
                employee.setLastName(results.getString("LASTNAME"));

                employee.setDepartmentId(results.getInt("DEPARTMENT_ID"));
                employee.setDepartmentName(results.getString("DepartmentName"));

                employee.setGroups(queryEmployeesMailing(employee.getEmployeeId()));
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

    public List<Department> queryDepartments() {
        try {
            ResultSet results = selectDepartments.executeQuery();
            List<Department> departments = new ArrayList<>();
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

    public ObservableList<Group> queryEmployeesMailing(int employeeId) {

        try {
            selectEmployeeMailing.setInt(1, employeeId);
            ResultSet results = selectEmployeeMailing.executeQuery();

            ObservableList<Group> groups = FXCollections.observableArrayList();
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

            for (Group group : groups) {
                insertIntoEmployeeMailing.setInt(1, employeeInsertedId);
                insertIntoEmployeeMailing.setInt(2, group.getId());

                // Add to batch
                insertIntoEmployeeMailing.addBatch();
            }

            // Execute the batch
            insertIntoEmployeeMailing.executeBatch();

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
            for (Group group : groups) {
                insertIntoEmployeeMailing.setInt(1, employeeId);
                insertIntoEmployeeMailing.setInt(2, group.getId());

                // Add to batch
                insertIntoEmployeeMailing.addBatch();
            }

            // Execute the batch
            insertIntoEmployeeMailing.executeBatch();

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
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit" + e.getMessage());
            }
        }
    }

    public void updateEmployee(String firstName, String lastName, int employeeId, int departmentId) {
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
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit" + e.getMessage());
            }
        }
    }

    public void updateEmployee(int employeeId, List<Group> groups) {
        try {
            conn.setAutoCommit(false);

            // Delete employees mailing groups
            deleteEmployeesMailing(employeeId);

            // Insert employees new mailing groups
            for (Group group : groups) {
                insertIntoEmployeeMailing.setInt(1, employeeId);
                insertIntoEmployeeMailing.setInt(2, group.getId());

                // Add to batch
                insertIntoEmployeeMailing.addBatch();
            }

            // Execute the batch
            //            int [] insertCounts = insertIntoEmployeeMailing.executeBatch();
            insertIntoEmployeeMailing.executeBatch();

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
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit" + e.getMessage());
            }
        }
    }

    public void deleteEmployee(int employeeId) {
        try {
            conn.setAutoCommit(false);

            deleteFromEmployeeMailing.setInt(1, employeeId);
            deleteFromEmployeeMailing.execute();

            // Try delete Employee
            deleteFromEmployees.setInt(1, employeeId);

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
}