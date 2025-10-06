/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package lk.avinam.dialog;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import lk.avinam.connection.MySQL;
import raven.toast.Notifications;

/**
 *
 * @author pasin
 */
public class DoctorShift extends javax.swing.JDialog {

    private int doctorId;
    private Connection conn;
    private Map<String, Integer> shiftTypeMap;
    private Map<String, Integer> wardTypeMap;

    public DoctorShift(java.awt.Frame parent, boolean modal, int doctorId) {
        super(parent, modal);
        this.doctorId = doctorId;
        this.conn = MySQL.getConnection();
        this.shiftTypeMap = new HashMap<>();
        this.wardTypeMap = new HashMap<>();
        initComponents();
        init();
        loadComboBoxData();
    }

    public DoctorShift(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.conn = MySQL.getConnection();
        this.shiftTypeMap = new HashMap<>();
        this.wardTypeMap = new HashMap<>();
        initComponents();
        init();
        loadComboBoxData();
    }

    private void init() {
        FlatSVGIcon addIcon = new FlatSVGIcon("lk/avinam/icon/plus.svg", 15, 15);
        addIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#CAF0F8")));
        addBtn.setIcon(addIcon);
        FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/cancel.svg", 15, 15);
        cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        cancelBtn.setIcon(cancelIcon);
        doctorShift.setMinSelectableDate(new java.util.Date());

    }

    private void loadComboBoxData() {
        loadShiftTypes();
        loadWardTypes();
    }

    private void loadShiftTypes() {
        try {
            String query = "SELECT shift_type_id, shift_type FROM shift_type";
            ResultSet rs = MySQL.executeSearch(query);
            
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            shiftTypeMap.clear();
            
            while (rs.next()) {
                int id = rs.getInt("shift_type_id");
                String type = rs.getString("shift_type");
                model.addElement(type);
                shiftTypeMap.put(type, id);
            }
            
            jComboBox1.setModel(model);
            rs.close();
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, 
                "Error loading shift types");
         
        }
    }

    private void loadWardTypes() {
        try {
            String query = "SELECT ward_id, ward_type FROM ward_type";
            ResultSet rs = MySQL.executeSearch(query);
            
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            wardTypeMap.clear();
            
            while (rs.next()) {
                int id = rs.getInt("ward_id");
                String type = rs.getString("ward_type");
                model.addElement(type);
                wardTypeMap.put(type, id);
            }
            
            jComboBox2.setModel(model);
            rs.close();
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, 
                "Error loading ward types");
        }
    }

    private boolean isShiftExists() {
        try {
            String query = "SELECT COUNT(*) FROM Doctor_shift_schedule WHERE doctor_id = ? AND date = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, doctorId);
            java.sql.Date sqlDate = new java.sql.Date(doctorShift.getDate().getTime());
            pstmt.setDate(2, sqlDate);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, 
                "Error checking existing shifts");
            e.printStackTrace();
        }
        return false;
    }

    private void saveDoctorShift() {
        // Validate inputs
        if (doctorShift.getDate() == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Please select a date");
            return;
        }
        
        if (jComboBox1.getSelectedItem() == null || jComboBox1.getSelectedIndex() == 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Please select a shift type");
            return;
        }
        
        if (jComboBox2.getSelectedItem() == null || jComboBox2.getSelectedIndex() == 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Please select a ward type");
            return;
        }
        
        // Check if shift already exists for this doctor on selected date
        if (isShiftExists()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "A shift already exists for this doctor on the selected date");
            return;
        }
        
        try {
            String query = "INSERT INTO Doctor_shift_schedule (date, doctor_id, ward_id, shift_type_id) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(doctorShift.getDate().getTime());
            
            pstmt.setDate(1, sqlDate);
            pstmt.setInt(2, doctorId);
            pstmt.setInt(3, wardTypeMap.get(jComboBox2.getSelectedItem().toString()));
            pstmt.setInt(4, shiftTypeMap.get(jComboBox1.getSelectedItem().toString()));
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            
            if (rowsAffected > 0) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, 
                    "Doctor shift added successfully");
                dispose();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, 
                    "Failed to add doctor shift");
            }
            
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, 
                "Error saving doctor shift: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        addBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        doctorShift = new com.toedter.calendar.JCalendar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Nunito ExtraBold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 4, 94));
        jLabel1.setText("Add Doctor Shift");

        jSeparator1.setForeground(new java.awt.Color(3, 4, 94));

        jComboBox1.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(3, 4, 94));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Shift", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(3, 4, 94));
        jLabel2.setText("Select Date");

        addBtn.setBackground(new java.awt.Color(3, 4, 94));
        addBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        addBtn.setForeground(new java.awt.Color(144, 224, 239));
        addBtn.setText("Save");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        cancelBtn.setBackground(new java.awt.Color(202, 240, 248));
        cancelBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(3, 4, 94));
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jComboBox2.setForeground(new java.awt.Color(3, 4, 94));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ward Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(doctorShift, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, 0, 574, Short.MAX_VALUE)
                            .addComponent(jSeparator1)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(doctorShift, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        saveDoctorShift();
    }//GEN-LAST:event_addBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DoctorShift dialog = new DoctorShift(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton cancelBtn;
    private com.toedter.calendar.JCalendar doctorShift;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
