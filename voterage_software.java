package com.mycompany.dbms1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class athes extends JFrame {

    private JTable tableCitizens = new JTable();
    private DefaultTableModel citizenModel;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/voting";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "dbms";

    public athes() { // Constructor name matches the class name
        showDashboard();
    }

    private Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            return null;
        }
    }

    private void showDashboard() {
        setTitle("Voter Age Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JTextField txtName = new JTextField();
        JTextField txtAge = new JTextField();
        JTextField txtGender = new JTextField();
        JTextField txtContact = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(txtName);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(txtAge);
        inputPanel.add(new JLabel("Gender:"));
        inputPanel.add(txtGender);
        inputPanel.add(new JLabel("Contact:"));
        inputPanel.add(txtContact);

        JButton btnAdd = new JButton("Add Citizen");
        btnAdd.addActionListener((ActionEvent e) -> addCitizen(txtName, txtAge, txtGender, txtContact));
        JButton btnCount = new JButton("Count Voters");
        btnCount.addActionListener((ActionEvent e) -> countVoters());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCount);

        String[] columns = {"Name", "Age", "Gender", "Contact"};
        citizenModel = new DefaultTableModel(new Object[][]{}, columns);
        tableCitizens = new JTable(citizenModel);
        loadCitizens();

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(tableCitizens), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void addCitizen(JTextField txtName, JTextField txtAge, JTextField txtGender, JTextField txtContact) {
        String name = txtName.getText();
        int age = Integer.parseInt(txtAge.getText());
        String gender = txtGender.getText();
        String contact = txtContact.getText();

        try (Connection conn = connect()) {
            if (conn != null) {
                String sql = "INSERT INTO citizen (name, age, gender, contact) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setInt(2, age);
                pstmt.setString(3, gender);
                pstmt.setString(4, contact);
                pstmt.executeUpdate();
                citizenModel.addRow(new Object[]{name, age, gender, contact});
                JOptionPane.showMessageDialog(this, "Citizen added successfully.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding citizen: " + ex.getMessage());
        }
    }

    private void countVoters() {
        try (Connection conn = connect()) {
            if (conn != null) {
                String sql = "SELECT COUNT(*) FROM citizen WHERE age >= 18";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    int count = rs.getInt(1);
                    JOptionPane.showMessageDialog(this, "Eligible Voters: " + count);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error counting voters: " + ex.getMessage());
        }
    }

    private void loadCitizens() {
        try (Connection conn = connect()) {
            if (conn != null) {
                String sql = "SELECT name, age, gender, contact FROM citizen";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                citizenModel.setRowCount(0);
                while (rs.next()) {
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    String contact = rs.getString("contact");
                    citizenModel.addRow(new Object[]{name, age, gender, contact});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading citizens: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(athes::new);
    }
}
