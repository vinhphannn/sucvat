package sucvat;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class GUI {

    // Text fields
    JTextField txtMa = new JTextField();
    JTextField txtTen = new JTextField();
    JTextField txtNoiSinh = new JTextField();
    JTextField txtTuoi = new JTextField();
    JTextField txtDiem = new JTextField();

    // Buttons
    JButton btnThem = new JButton("THÊM");
    JButton btnXoa = new JButton("XÓA");
    JButton btnSua = new JButton("SỬA");
    JButton btnThoat = new JButton("THOÁT");
    JButton btnClear = new JButton("CLEAR");

    // ComboBox
    JComboBox<String> cbID = new JComboBox<>();

    // Table columns
    public String[] col = {"Mã", "Họ tên", "Tuổi", "Nơi sinh", "Điểm", "ComboID"};
    public DefaultTableModel model = new DefaultTableModel(col, 0);
    public JTable table = new JTable(model);

    // Database credentials
    String DB_URL = "jdbc:mysql://localhost:3306/sucvat";
    String USER_NAME = "root";
    String PASSWORD = "";

    private int index = -1;

    public void initUI() {
        // Frame
        JFrame frame = new JFrame("Form Đăng Ký");
        frame.setSize(830, 420);
        frame.setLayout(null);

        // Labels and text fields
        
        JLabel lbA = new JLabel("Mã:");
        lbA.setBounds(20, 20, 80, 25);
        frame.add(lbA);
        txtMa.setBounds(100, 20, 80, 25);
        frame.add(txtMa);

        JLabel lbB = new JLabel("Tên:");
        lbB.setBounds(20, 60, 80, 25);
        frame.add(lbB);
        txtTen.setBounds(100, 60, 220, 25);
        frame.add(txtTen);

        JLabel lbC = new JLabel("Tuổi:");
        lbC.setBounds(20, 100, 80, 25);
        frame.add(lbC);
        txtTuoi.setBounds(100, 100, 80, 25);
        frame.add(txtTuoi);

        JLabel lbD = new JLabel("Nơi Sinh:");
        lbD.setBounds(20, 150, 80, 25);
        frame.add(lbD);
        txtNoiSinh.setBounds(100, 150, 220, 25);
        frame.add(txtNoiSinh);

        JLabel lbH = new JLabel("Điểm:");
        lbH.setBounds(20, 200, 80, 25);
        frame.add(lbH);
        txtDiem.setBounds(100, 200, 80, 25);
        frame.add(txtDiem);

        // ComboBox
        JLabel lbcb = new JLabel("Combo ID:");
        lbcb.setBounds(20, 230, 80, 25);
        frame.add(lbcb);
        cbID.setBounds(100, 230, 150, 25);
        frame.add(cbID);

        // Table and scroll pane
        JScrollPane pane = new JScrollPane(table);
        JPanel panel = new JPanel();
        panel.add(pane);
        panel.setBounds(350, 20, 450, 300);
        frame.add(panel);

        // Buttons
        btnClear.setBounds(350, 340, 90, 25);
        frame.add(btnClear);
        
        btnThem.setBounds(100, 300, 90, 25);
        frame.add(btnThem);

        btnSua.setBounds(230, 300, 90, 25);
        frame.add(btnSua);

        btnXoa.setBounds(100, 340, 90, 25);
        frame.add(btnXoa);

        btnThoat.setBounds(230, 340, 90, 25);
        frame.add(btnThoat);

        // Button state
        btnXoa.setEnabled(false);
        btnSua.setEnabled(false);

        // Frame visibility
        frame.setVisible(true);

        // Table event listener
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                index = table.getSelectedRow();
                txtMa.setText(table.getValueAt(index, 0).toString());
                txtTen.setText(table.getValueAt(index, 1).toString());
                txtTuoi.setText(table.getValueAt(index, 2).toString());
                txtNoiSinh.setText(table.getValueAt(index, 3).toString());
                txtDiem.setText(table.getValueAt(index, 4).toString());
                cbID.setSelectedItem(table.getValueAt(index, 5).toString());
                btnXoa.setEnabled(true);
                btnSua.setEnabled(true);
            }
        });

        // Add button event listener
        btnThem.addActionListener(e -> {
            try {
                if (validateInputs()) {
                    int Ma = parseInt(txtMa.getText().trim(), "Mã");
                    String Name = txtTen.getText().trim();
                    int Age = parseInt(txtTuoi.getText().trim(), "Tuổi");
                    String Place = txtNoiSinh.getText().trim();
                    int GPA = parseInt(txtDiem.getText().trim(), "Điểm");
                    int comboID = parseInt(cbID.getSelectedItem().toString(), "Combo ID");

                    String query = "INSERT INTO bang VALUES (?, ?, ?, ?, ?, ?)";
                    try (Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                         PreparedStatement pstm = cnn.prepareStatement(query)) {
                        pstm.setInt(1, Ma);
                        pstm.setString(2, Name);
                        pstm.setInt(3, Age);
                        pstm.setString(4, Place);
                        pstm.setInt(5, GPA);
                        pstm.setInt(6, comboID);
                        int result = pstm.executeUpdate();
                        if (result == 1) {
                            model.addRow(new Object[]{Ma, Name, Age, Place, GPA, comboID});
                            clearForm();
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        // Edit button event listener
// Edit button event listener
        btnSua.addActionListener(e -> {
            try {
                if (validateInputs()) {
                    int oldMa = Integer.parseInt(table.getValueAt(index, 0).toString());  // Mã trước khi sửa
                    int newMa = parseInt(txtMa.getText().trim(), "Mã");  // Mã sau khi sửa
                    if (oldMa != newMa) {
                        JOptionPane.showMessageDialog(null, "Không thể sửa mã!");
                        return;  // Dừng lại nếu mã đã thay đổi
                    }

                    String Name = txtTen.getText().trim();
                    int Age = parseInt(txtTuoi.getText().trim(), "Tuổi");
                    String Place = txtNoiSinh.getText().trim();
                    int GPA = parseInt(txtDiem.getText().trim(), "Điểm");
                    int comboID = parseInt(cbID.getSelectedItem().toString(), "Combo ID");

                    String query = "UPDATE bang SET name=?, age=?, address=?, gpa=?, ComboID=? WHERE id=?";
                    try (Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                         PreparedStatement pstm = cnn.prepareStatement(query)) {
                        pstm.setString(1, Name);
                        pstm.setInt(2, Age);
                        pstm.setString(3, Place);
                        pstm.setInt(4, GPA);
                        pstm.setInt(5, comboID);
                        pstm.setInt(6, newMa);  // Sử dụng mã sau khi sửa
                        int rowAffected = pstm.executeUpdate();
                        if (rowAffected == 1) {
                            JOptionPane.showMessageDialog(null, "Cập nhật thành công");
                            model.setValueAt(Name, index, 1);
                            model.setValueAt(Age, index, 2);
                            model.setValueAt(Place, index, 3);
                            model.setValueAt(GPA, index, 4);
                            model.setValueAt(comboID, index, 5);
                            clearForm();
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });


        // Delete button event listener
        btnXoa.addActionListener(e -> {
            try {
                int Ma = parseInt(txtMa.getText().trim(), "Mã");
                String query = "DELETE FROM bang WHERE id=?";
                try (Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                     PreparedStatement pstm = cnn.prepareStatement(query)) {
                    pstm.setInt(1, Ma);
                    pstm.executeUpdate();
                    model.removeRow(index);
                    clearForm();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        // Thoát button event listener
        btnThoat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Bạn có muốn thoát?", "Xác nhận thoát", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        btnClear.addActionListener(e -> {
                            clearForm();
                            
                            
        });

        loadData();
        loadComboboxData();
    }

    // Validate inputs
// Validate inputs
private boolean validateInputs() throws Exception {
    if (txtMa.getText().isEmpty()) throw new Exception("Mã không được để trống!");
    if (txtTen.getText().isEmpty()) throw new Exception("Tên không được để trống!");
    
    // Kiểm tra tên không chứa số
    if (txtTen.getText().matches(".*\\d.*")) {
        throw new Exception("Tên không được chứa số!");
    }

    if (txtTuoi.getText().isEmpty()) throw new Exception("Tuổi không được để trống!");
    if (txtNoiSinh.getText().isEmpty()) throw new Exception("Nơi sinh không được để trống!");
    if (txtDiem.getText().isEmpty()) throw new Exception("Điểm không được để trống!");

    int age = parseInt(txtTuoi.getText().trim(), "Tuổi");
    if (age <= 0 || age > 50) throw new Exception("Tuổi phải là số dương và không quá 50!");

    int score = parseInt(txtDiem.getText().trim(), "Điểm");
    if (score < 0) throw new Exception("Điểm không thể âm!");

    return true;
}


    // Parse integer with exception handling
    private int parseInt(String value, String fieldName) throws Exception {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new Exception(fieldName + " phải là một số hợp lệ!");
        }
    }

    // Clear form
    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtTuoi.setText("");
        txtNoiSinh.setText("");
        txtDiem.setText("");
        btnXoa.setEnabled(false);
        btnSua.setEnabled(false);
        cbID.setSelectedIndex(-1);
    }

    // Load data into table
    public void loadData() {
        try (Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             Statement stm = cnn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM bang")) {
            model.setRowCount(0);
            while (rs.next()) {
                int Ma = rs.getInt("id");
                String Name = rs.getString("name");
                int Age = rs.getInt("age");
                String Place = rs.getString("address");
                int GPA = rs.getInt("gpa");
                int comboID = rs.getInt("ComboID");
                model.addRow(new Object[]{Ma, Name, Age, Place, GPA, comboID});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    // Load ComboBox data
    public void loadComboboxData() {
        try (Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             Statement stm = cnn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT ComboID FROM bangid")) {
            cbID.removeAllItems();
            while (rs.next()) {
                cbID.addItem(rs.getString("ComboID"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().initUI());
    }
}