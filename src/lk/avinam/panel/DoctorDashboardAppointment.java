/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package lk.avinam.panel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import lk.avinam.connection.MySQL;
import lk.avinam.dialog.ViewMorePatient;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author pasin
 */
public class DoctorDashboardAppointment extends javax.swing.JPanel {

    public DoctorDashboardAppointment() {
        initComponents();
        init();
        loadDoctorAppointmentDetails();
        radioButtonListener();
        viewBtn.setVisible(false);
    }

    private void init() {
        dAppointmentTable.getTableHeader().setFont(new Font("", Font.BOLD, 16));
        dAppointmentTable.getTableHeader().setOpaque(false);
        dAppointmentTable.getTableHeader().setBackground(Color.decode("#00B4D8"));
        dAppointmentTable.getTableHeader().setForeground(Color.decode("#CAF0F8"));
        dAppointmentTable.getTableHeader().setPreferredSize(new Dimension(0, 47));

        FlatSVGIcon searchIcon = new FlatSVGIcon("lk/avinam/icon/search.svg", 15, 15);
        searchIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#FFFFFF")));
        searchBtn.setIcon(searchIcon);

        FlatSVGIcon eyeIcon = new FlatSVGIcon("lk/avinam/icon/eye.svg", 20, 20);
        eyeIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#90E0EF")));
        viewBtn.setIcon(eyeIcon);

        FlatSVGIcon reportIcon = new FlatSVGIcon("lk/avinam/icon/report.svg", 20, 20);
        reportIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#90E0EF")));
        reportBtn.setIcon(reportIcon);
        
        dAppointmentTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = dAppointmentTable.getSelectedRow();
                if (selectedRow == -1) { 
                    viewBtn.setVisible(false);
                }
            }
        });

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dAppointmentTable.clearSelection();
            }
        });
    }
    
    private void SearchFilters() {
        String sAppointmentNo = jTextField1.getText().trim();
        Date selectedDate = jDateChooser.getDate();
        
        String status = "all";

        if (jRadioButtonPending.isSelected()) {
            status = "Pending";
        } else if (jRadioButtonCompleted.isSelected()) {
            status = "Completed";
        } else if (jRadioButtonCancelled.isSelected()) {
            status = "Cancelled";
        }

        loadDoctorAppointmentDetails(sAppointmentNo,selectedDate, status);
    }

    
    private void loadDoctorAppointmentDetails() {
        loadDoctorAppointmentDetails("",null, "all");
    }
    private void loadDoctorAppointmentDetails(String sAppointmentNo, Date selectedDate, String status) {
        try {
            String query = "SELECT appointment_no,patient_nic,patient_name,appointment_room_no,availability_date,time_slot,appointment_status FROM appointment_view WHERE 1=1 ";
            if (sAppointmentNo != null && !sAppointmentNo.trim().isEmpty() && !sAppointmentNo.equals("Search By Appointment No")) {
                query += " AND appointment_no LIKE '%" + sAppointmentNo + "%'";
            }
            if (selectedDate != null) {
                 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                 String dateToString = dateFormat.format(selectedDate);
                 query += " AND availability_date = '"+dateToString+"' ";
                 
            }

            if (!status.equals("all")) {
                if (status.equals("Pending")) {
                    query += " AND appointment_status = 'Pending'";
                } else if (status.equals("Completed")) {
                    query += " AND appointment_status = 'Completed'";
                } else if (status.equals("Cancelled")) {
                    query += " AND appointment_status = 'Cancelled'";
                }
            }
            
            ResultSet rs = MySQL.executeSearch(query);
            DefaultTableModel dtm = (DefaultTableModel) dAppointmentTable.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("appointment_no"));
                v.add(rs.getString("patient_nic"));
                v.add(rs.getString("patient_name"));
                v.add(rs.getString("appointment_room_no"));
                v.add(rs.getString("availability_date"));
                v.add(rs.getString("time_slot"));
                v.add(rs.getString("appointment_status"));
                dtm.addRow(v);
            }

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,
                        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setBorder(noFocusBorder);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    return this;
                }
            };
            dAppointmentTable.setDefaultRenderer(Object.class, renderer);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void radioButtonListener() {
        jRadioButtonPending.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (jRadioButtonPending.isSelected()) {
                    buttonGroup1.clearSelection();
                    loadDoctorAppointmentDetails();
                    evt.consume();
                }
            }
        });

        jRadioButtonCompleted.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (jRadioButtonCompleted.isSelected()) {
                    buttonGroup1.clearSelection();
                    loadDoctorAppointmentDetails();
                    evt.consume();
                }
            }
        });

        jRadioButtonCancelled.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (jRadioButtonCancelled.isSelected()) {
                    buttonGroup1.clearSelection();
                    loadDoctorAppointmentDetails();
                    evt.consume();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        searchBtn = new javax.swing.JButton();
        viewBtn = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        reportBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dAppointmentTable = new javax.swing.JTable();
        jRadioButtonPending = new javax.swing.JRadioButton();
        jRadioButtonCompleted = new javax.swing.JRadioButton();
        jRadioButtonCancelled = new javax.swing.JRadioButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Nunito ExtraBold", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 4, 94));
        jLabel1.setText("Appointments");

        jSeparator1.setBackground(new java.awt.Color(3, 4, 94));
        jSeparator1.setForeground(new java.awt.Color(3, 4, 94));
        jSeparator1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        searchBtn.setBackground(new java.awt.Color(0, 180, 216));
        searchBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        searchBtn.setForeground(new java.awt.Color(255, 255, 255));
        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        viewBtn.setBackground(new java.awt.Color(3, 4, 94));
        viewBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        viewBtn.setForeground(new java.awt.Color(144, 224, 239));
        viewBtn.setText("View");
        viewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewBtnActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        jTextField1.setText("Search By Appointment No");
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

        jDateChooser.setOpaque(false);
        jDateChooser.setPreferredSize(new java.awt.Dimension(106, 53));

        reportBtn.setBackground(new java.awt.Color(3, 4, 94));
        reportBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        reportBtn.setForeground(new java.awt.Color(144, 224, 239));
        reportBtn.setText("Generat Report");
        reportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportBtnActionPerformed(evt);
            }
        });

        dAppointmentTable.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        dAppointmentTable.setForeground(new java.awt.Color(3, 4, 94));
        dAppointmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Appointment No", "Patient NIC", "Patient", "Room", "Date", "Time Solt", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dAppointmentTable.setRowHeight(47);
        dAppointmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dAppointmentTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(dAppointmentTable);
        if (dAppointmentTable.getColumnModel().getColumnCount() > 0) {
            dAppointmentTable.getColumnModel().getColumn(6).setResizable(false);
        }

        buttonGroup1.add(jRadioButtonPending);
        jRadioButtonPending.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        jRadioButtonPending.setForeground(new java.awt.Color(3, 4, 94));
        jRadioButtonPending.setText("Pending");
        jRadioButtonPending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPendingActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonCompleted);
        jRadioButtonCompleted.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        jRadioButtonCompleted.setForeground(new java.awt.Color(0, 204, 0));
        jRadioButtonCompleted.setText("Completed");
        jRadioButtonCompleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonCompletedActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonCancelled);
        jRadioButtonCancelled.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        jRadioButtonCancelled.setForeground(new java.awt.Color(255, 0, 0));
        jRadioButtonCancelled.setText("Cancelled");
        jRadioButtonCancelled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonCancelledActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonPending)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonCompleted)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonCancelled))
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(viewBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButtonCancelled)
                        .addComponent(jRadioButtonCompleted)
                        .addComponent(jRadioButtonPending))
                    .addComponent(viewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        SearchFilters();
    }//GEN-LAST:event_searchBtnActionPerformed

    private void viewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewBtnActionPerformed
        ViewMorePatient viewMorePatient = new ViewMorePatient(null, true);
        viewMorePatient.setLocationRelativeTo(null);
        viewMorePatient.setVisible(true);
    }//GEN-LAST:event_viewBtnActionPerformed

    private void reportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportBtnActionPerformed
        try{
            InputStream filePath = getClass().getResourceAsStream("/lk/avinam/report/LoadAppointmentDetailsDoctorTable.jasper");

            HashMap<String, Object> parameters = new HashMap<>();
            
            JRTableModelDataSource jrTMDataSourse = new JRTableModelDataSource(dAppointmentTable.getModel());
            JasperPrint fillReport = JasperFillManager.fillReport(filePath, parameters,jrTMDataSourse);
            JasperViewer.viewReport(fillReport,false);
        }catch(JRException e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_reportBtnActionPerformed

    private void jRadioButtonPendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonPendingActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jRadioButtonPendingActionPerformed

    private void jRadioButtonCompletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonCompletedActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jRadioButtonCompletedActionPerformed

    private void jRadioButtonCancelledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonCancelledActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jRadioButtonCancelledActionPerformed

    private String selectedNICNo;

    public String getselectedAppointmentNo() {
        return selectedNICNo;
    }
    
    private void dAppointmentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dAppointmentTableMouseClicked
        if (evt.getClickCount() == 1) {
            int row = dAppointmentTable.getSelectedRow();
            selectedNICNo = (String) dAppointmentTable.getValueAt(row, 1);
            viewBtn.setVisible(true);
        }
            
    }//GEN-LAST:event_dAppointmentTableMouseClicked

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        if (jTextField1.getText().equals("Search By Appointment No")) {
            jTextField1.setText("");
        }
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        if (jTextField1.getText().trim().isEmpty()) {
            jTextField1.setText("Search By Appointment No");
        }
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
            SearchFilters();
    }//GEN-LAST:event_jTextField1ActionPerformed
    
    
    
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTable dAppointmentTable;
    private com.toedter.calendar.JDateChooser jDateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButtonCancelled;
    private javax.swing.JRadioButton jRadioButtonCompleted;
    private javax.swing.JRadioButton jRadioButtonPending;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton reportBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JButton viewBtn;
    // End of variables declaration//GEN-END:variables
}
