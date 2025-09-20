/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package lk.avinam.panel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import lk.avinam.connection.MySQL;
import lk.avinam.dialog.DoctorProfile;
import raven.toast.Notifications;

/**
 *
 * @author moham
 */
public class DoctorManagementPanel extends javax.swing.JPanel {

    public DoctorManagementPanel() {
        initComponents();
        init();
        loadDoctorTable();
        cancelBtn.setVisible(false);
        updateBtn.setVisible(false);
        viewBtn.setVisible(false);
    }

    public void init() {
        doctorTable.getTableHeader().setFont(new Font("", Font.BOLD, 16));
        doctorTable.getTableHeader().setOpaque(false);
        doctorTable.getTableHeader().setBackground(Color.decode("#00B4D8"));
        doctorTable.getTableHeader().setForeground(Color.decode("#CAF0F8"));
        doctorTable.getTableHeader().setPreferredSize(new Dimension(0, 47));
        FlatSVGIcon plusIcon = new FlatSVGIcon("lk/avinam/icon/plus.svg", 15, 15);
        plusIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#90E0EF")));
        addBtn.setIcon(plusIcon);
        FlatSVGIcon searchIcon = new FlatSVGIcon("lk/avinam/icon/search.svg", 15, 15);
        searchIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#FFFFFF")));
        searchBtn.setIcon(searchIcon);
        FlatSVGIcon eyeIcon = new FlatSVGIcon("lk/avinam/icon/eye.svg", 20, 20);
        eyeIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#FFFFFF")));
        viewBtn.setIcon(eyeIcon);
        FlatSVGIcon updateIcon = new FlatSVGIcon("lk/avinam/icon/update.svg", 20, 20);
        updateIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#CAF0F8")));
        updateBtn.setIcon(updateIcon);
        FlatSVGIcon reportIcon = new FlatSVGIcon("lk/avinam/icon/report.svg", 20, 20);
        reportIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#90E0EF")));
        reportBtn.setIcon(reportIcon);
    }

private void loadDoctorTable() { 
    try {
        ResultSet rs = MySQL.executeSearch("SELECT * FROM doctor_view");
        DefaultTableModel dtm = (DefaultTableModel) doctorTable.getModel();
        dtm.setRowCount(0);

        while (rs.next()) {
            Vector v = new Vector();
            v.add(rs.getString("slmc_id"));
            v.add(rs.getString("f_name") + " " + rs.getString("l_name"));
            v.add(rs.getString("email"));
            v.add(rs.getString("mobile"));
            v.add(rs.getString("join_at"));
            v.add(rs.getString("qualification"));   
            v.add(rs.getString("specialization")); 
            v.add(rs.getString("doctor_type"));
            v.add(rs.getString("doctor_status"));
            dtm.addRow(v);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        doctorTable.setDefaultRenderer(Object.class, centerRenderer);

    } catch (SQLException e) {
        Notifications.getInstance().show(
            Notifications.Type.ERROR, 
            Notifications.Location.BOTTOM_RIGHT, 
            "Database error: " + e.getMessage()
        );
    }

    

        doctorTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && doctorTable.getSelectedRow() != -1) {
                int row = doctorTable.getSelectedRow();
                selectedIdColum = doctorTable.getValueAt(row, 0).toString();
                String currentStatus = doctorTable.getValueAt(row, 8).toString();
                if ("Active".equalsIgnoreCase(currentStatus)) {
                    cancelBtn.setText("Inactive");
                    FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/cancel.svg", 15, 15);
                    cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#FF0000")));
                    cancelBtn.setIcon(cancelIcon);
                    cancelBtn.setForeground(Color.red);
                    cancelBtn.setBorder(BorderFactory.createLineBorder(Color.red, 2));
                } else {
                    cancelBtn.setText("Active");
                    FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/correct.svg", 15, 15);
                    Color darkGreen = new Color(0, 255, 51);
                    cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> darkGreen));
                    cancelBtn.setIcon(cancelIcon);
                    cancelBtn.setForeground(darkGreen);
                    cancelBtn.setBorder(BorderFactory.createLineBorder(darkGreen, 2));
                }
                cancelBtn.setVisible(true);
                updateBtn.setVisible(true);
                viewBtn.setVisible(true);
            }
        });
    }

    private String selectedIdColum;

    public String getSelectedIdColum() {
        return selectedIdColum;
    }

    public void disableUpdateButton() {
        cancelBtn.setVisible(false);
        updateBtn.setVisible(false);
        viewBtn.setVisible(false);
    }

    private void openUpdateDialog() {
        String id = getSelectedIdColum();
        if (id == null || id.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "No doctor selected.");
            return;
        }

        try {
            
            String query = "SELECT * FROM doctor_view WHERE slmc_id = '" + id + "'";
            ResultSet rs = MySQL.executeSearch(query);

            if (rs.next()) {
                
                DoctorProfile dialog = new DoctorProfile((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), true, rs, this);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

                disableUpdateButton();
                loadDoctorTable();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Doctor not found.");
            }
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Error loading doctor details: " + e.getMessage());
        }
    }

    private synchronized void toggleDoctorStatus() {
        String id = getSelectedIdColum();
        if (id == null || id.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "No doctor selected.");
            return;
        }
        try {
            java.sql.Connection conn = MySQL.getConnection();
            String checkSql = "SELECT status_id FROM doctor WHERE slmc_id = ?";
            java.sql.PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setString(1, id);
            java.sql.ResultSet rs = checkPst.executeQuery();
            if (rs.next()) {
                int currentStatus = rs.getInt("status_id");
                int newStatus = (currentStatus == 1) ? 2 : 1;
                String updateSql = "UPDATE doctor SET status_id = ? WHERE slmc_id = ?";
                java.sql.PreparedStatement updatePst = conn.prepareStatement(updateSql);
                updatePst.setInt(1, newStatus);
                updatePst.setString(2, id);
                int rows = updatePst.executeUpdate();
                if (rows > 0) {
                    loadDoctorTable();
                    disableUpdateButton();
                    cancelBtn.setText(newStatus == 1 ? "Set Inactive" : "Set Active");
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, "Doctor status updated to " + (newStatus == 1 ? "Active" : "Inactive") + ".");
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Doctor not found.");
                }
                updatePst.close();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Doctor not found.");
            }
            rs.close();
            checkPst.close();
        } catch (HeadlessException | SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Error updating doctor status: " + e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        reportBtn1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        doctorTable = new javax.swing.JTable();
        addBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        viewBtn = new javax.swing.JButton();
        reportBtn = new javax.swing.JButton();

        reportBtn1.setBackground(new java.awt.Color(3, 4, 94));
        reportBtn1.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        reportBtn1.setForeground(new java.awt.Color(144, 224, 239));
        reportBtn1.setText("Generat Report");
        reportBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportBtn1ActionPerformed(evt);
            }
        });

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Nunito ExtraBold", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(3, 4, 94));
        jLabel4.setText("Doctor Managment");

        doctorTable.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        doctorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SLMC ID", "Full Name", "Email", "Mobile", "Join At", "Qualification", "Spatialised In", "Doctor Type", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        doctorTable.setRowHeight(47);
        doctorTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doctorTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(doctorTable);

        addBtn.setBackground(new java.awt.Color(3, 4, 94));
        addBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        addBtn.setForeground(new java.awt.Color(144, 224, 239));
        addBtn.setText("Add New Docter");
        addBtn.setFocusable(false);

        cancelBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(255, 51, 0));
        cancelBtn.setText("Cancel");
        cancelBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));
        cancelBtn.setFocusable(false);
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        updateBtn.setBackground(new java.awt.Color(0, 119, 182));
        updateBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        updateBtn.setForeground(new java.awt.Color(202, 240, 248));
        updateBtn.setText("Update");
        updateBtn.setFocusable(false);
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jTextField1.setText("Search by SLMC ID");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jTextField2.setText("Search by Email");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        searchBtn.setBackground(new java.awt.Color(0, 180, 216));
        searchBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        searchBtn.setForeground(new java.awt.Color(202, 240, 248));
        searchBtn.setText("Search");
        searchBtn.setFocusable(false);
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        jRadioButton1.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jRadioButton1.setText("Inactive");

        jRadioButton2.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jRadioButton2.setText("Active");

        viewBtn.setBackground(new java.awt.Color(0, 180, 216));
        viewBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        viewBtn.setForeground(new java.awt.Color(255, 255, 255));
        viewBtn.setText("View All");
        viewBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        viewBtn.setFocusable(false);
        viewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewBtnActionPerformed(evt);
            }
        });

        reportBtn.setBackground(new java.awt.Color(3, 4, 94));
        reportBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        reportBtn.setForeground(new java.awt.Color(144, 224, 239));
        reportBtn.setText("Generat Report");
        reportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(viewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        toggleDoctorStatus();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        openUpdateDialog();
    }//GEN-LAST:event_updateBtnActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchBtnActionPerformed

    private void viewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_viewBtnActionPerformed

    private void reportBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportBtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reportBtn1ActionPerformed

    private void reportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reportBtnActionPerformed

    private void doctorTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_doctorTableMouseClicked
        if (evt.getClickCount() == 1) {
            int row = doctorTable.getSelectedRow();
            selectedIdColum = String.valueOf(doctorTable.getValueAt(row, 0));
            cancelBtn.setVisible(true);
            updateBtn.setVisible(true);
            viewBtn.setVisible(true);
        }
    }//GEN-LAST:event_doctorTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTable doctorTable;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton reportBtn;
    private javax.swing.JButton reportBtn1;
    private javax.swing.JButton searchBtn;
    private javax.swing.JButton updateBtn;
    private javax.swing.JButton viewBtn;
    // End of variables declaration//GEN-END:variables
}
