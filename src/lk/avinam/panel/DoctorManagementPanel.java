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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import lk.avinam.connection.MySQL;
import lk.avinam.dialog.AddDoctor;
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
        buttonGroup1.clearSelection();
        setupRadioButtonToggle();
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
        FlatSVGIcon updateIcon = new FlatSVGIcon("lk/avinam/icon/update.svg", 20, 20);
        updateIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#CAF0F8")));
        updateBtn.setIcon(updateIcon);
        FlatSVGIcon reportIcon = new FlatSVGIcon("lk/avinam/icon/report.svg", 20, 20);
        reportIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#90E0EF")));
        reportBtn.setIcon(reportIcon);
    }

    private void loadDoctorTable() {
        loadDoctorTable("", "", "all");
    }

    private void loadDoctorTable(String slmcId, String email, String status) {
        try {

            String query = "SELECT * FROM doctor_view WHERE 1=1";

            if (slmcId != null && !slmcId.trim().isEmpty() && !slmcId.equals("Search by SLMC ID")) {
                query += " AND slmc_id LIKE '%" + slmcId + "%'";
            }

            if (email != null && !email.trim().isEmpty() && !email.equals("Search by Email")) {
                query += " AND email LIKE '%" + email + "%'";
            }

            if (!status.equals("all")) {
                if (status.equals("active")) {
                    query += " AND doctor_status = 'Active'";
                } else if (status.equals("inactive")) {
                    query += " AND doctor_status = 'Inactive'";
                }
            }

            ResultSet rs = MySQL.executeSearch(query);
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
                updateCancelButtonAppearance(currentStatus);
                cancelBtn.setVisible(true);
                updateBtn.setVisible(true);
            }
        });
    }

    private void updateCancelButtonAppearance(String currentStatus) {
        if ("Active".equalsIgnoreCase(currentStatus)) {
            cancelBtn.setText("Inactive");
            FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/cancel.svg", 15, 15);
            cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#FF0000")));
            cancelBtn.setIcon(cancelIcon);
            cancelBtn.setForeground(Color.red);
            cancelBtn.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        } else {
            cancelBtn.setText("Set Active");
            FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/correct.svg", 15, 15);
            Color darkGreen = new Color(0, 255, 51);
            cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> darkGreen));
            cancelBtn.setIcon(cancelIcon);
            cancelBtn.setForeground(darkGreen);
            cancelBtn.setBorder(BorderFactory.createLineBorder(darkGreen, 2));
        }
    }

    private String selectedIdColum;

    public String getSelectedIdColum() {
        return selectedIdColum;
    }

    public void disableUpdateButton() {
        cancelBtn.setVisible(false);
        updateBtn.setVisible(false);
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
                DoctorProfile dialog = new DoctorProfile((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), true, rs);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                disableUpdateButton();
                performSearch();
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
                    performSearch();
                    disableUpdateButton();
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

    private void performSearch() {
        String slmcId = jTextField1.getText().trim();
        String email = jTextField2.getText().trim();
        String status = "all";

        if (jRadioButton2.isSelected()) {
            status = "active";
        } else if (jRadioButton1.isSelected()) {
            status = "inactive";
        }

        loadDoctorTable(slmcId, email, status);
    }

    private void setupRadioButtonToggle() {

        jRadioButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (jRadioButton1.isSelected()) {
                    buttonGroup1.clearSelection();
                    performSearch();
                    evt.consume();
                }
            }
        });

        jRadioButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (jRadioButton2.isSelected()) {
                    buttonGroup1.clearSelection();
                    performSearch();
                    evt.consume();
                }
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
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
        reportBtn = new javax.swing.JButton();

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
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

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
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jTextField2.setText("Search by Email");
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
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

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jRadioButton1.setText("Inactive");
        jRadioButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButton1MouseClicked(evt);
            }
        });
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jRadioButton2.setText("Active");
        jRadioButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButton2MouseClicked(evt);
            }
        });
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
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
                    .addComponent(jRadioButton2))
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
        performSearch();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        performSearch();
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        performSearch();
    }//GEN-LAST:event_searchBtnActionPerformed

    private void reportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reportBtnActionPerformed

    private void doctorTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_doctorTableMouseClicked
        if (evt.getClickCount() == 1) {
            int row = doctorTable.getSelectedRow();
            selectedIdColum = String.valueOf(doctorTable.getValueAt(row, 0));
            String currentStatus = doctorTable.getValueAt(row, 8).toString();
            updateCancelButtonAppearance(currentStatus);
            cancelBtn.setVisible(true);
            updateBtn.setVisible(true);

        }
    }//GEN-LAST:event_doctorTableMouseClicked

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AddDoctor dialog = new AddDoctor(parentFrame, true);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
        performSearch();
    }//GEN-LAST:event_addBtnActionPerformed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        if (jTextField1.getText().equals("Search by SLMC ID")) {
            jTextField1.setText("");
        }
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        if (jTextField1.getText().trim().isEmpty()) {
            jTextField1.setText("Search by SLMC ID");
        }
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        if (jTextField2.getText().equals("Search by Email")) {
            jTextField2.setText("");
        }
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        if (jTextField2.getText().trim().isEmpty()) {
            jTextField2.setText("Search by Email");
        }
    }//GEN-LAST:event_jTextField2FocusLost

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        performSearch();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        performSearch();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton2MouseClicked

    }//GEN-LAST:event_jRadioButton2MouseClicked

    private void jRadioButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton1MouseClicked

    }//GEN-LAST:event_jRadioButton1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTable doctorTable;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton reportBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
