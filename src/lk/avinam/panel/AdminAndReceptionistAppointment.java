/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package lk.avinam.panel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import lk.avinam.dialog.AddAppointment;
import lk.avinam.dialog.UpdateAppointment;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import lk.avinam.connection.MySQL;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.propertyeditors.InputSourceEditor;
import raven.toast.Notifications;

/**
 *
 * @author pasin
 */
public class AdminAndReceptionistAppointment extends javax.swing.JPanel {

    public AdminAndReceptionistAppointment() {
        initComponents();
        init();
        loadAppointmentDetails();
        updateBtn.setVisible(false);
        cancelBtn.setVisible(false);
        completedBtn.setVisible(false);
        radioButtonListener();
        addAppointmentNoListener();
        addDoctorIdListener();
        addDateChooserListener();

    }

    private void init() {
        arAppointmentTable.getTableHeader().setFont(new Font("", Font.BOLD, 16));
        arAppointmentTable.getTableHeader().setOpaque(false);
        arAppointmentTable.getTableHeader().setBackground(Color.decode("#00B4D8"));
        arAppointmentTable.getTableHeader().setForeground(Color.decode("#CAF0F8"));
        arAppointmentTable.getTableHeader().setPreferredSize(new Dimension(0, 47));

        FlatSVGIcon plusIcon = new FlatSVGIcon("lk/avinam/icon/plus.svg", 15, 15);
        plusIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#90E0EF")));
        addBtn.setIcon(plusIcon);

        FlatSVGIcon searchIcon = new FlatSVGIcon("lk/avinam/icon/search.svg", 15, 15);
        searchIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#FFFFFF")));
        searchBtn.setIcon(searchIcon);

        FlatSVGIcon updateIcon = new FlatSVGIcon("lk/avinam/icon/update.svg", 20, 20);
        updateIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#CAF0F8")));
        updateBtn.setIcon(updateIcon);

        FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/cancel.svg", 15, 15);
        cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#FF0000")));
        cancelBtn.setIcon(cancelIcon);

        FlatSVGIcon reportIcon = new FlatSVGIcon("lk/avinam/icon/report.svg", 20, 20);
        reportIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#90E0EF")));
        reportBtn.setIcon(reportIcon);

        FlatSVGIcon CompletedIcon = new FlatSVGIcon("lk/avinam/icon/correct.svg", 20, 20);
        CompletedIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#90E0EF")));
        completedBtn.setIcon(CompletedIcon);

        arAppointmentTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = arAppointmentTable.getSelectedRow();
                if (selectedRow == -1) {
                    updateBtn.setVisible(false);
                    cancelBtn.setVisible(false);
                    completedBtn.setVisible(false);
                }
            }
        });

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                arAppointmentTable.clearSelection();
            }
        });

    }

    private void addAppointmentNoListener() {
        jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void search() {
                SearchFilters();
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }
        });
    }

    private void addDoctorIdListener() {
        jTextField2.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void search() {
                SearchFilters();
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }
        });
    }

    private void addDateChooserListener() {
    jDateChooser.getDateEditor().getUiComponent().addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            SearchFilters();
        }
    });

    jDateChooser.getDateEditor().addPropertyChangeListener(evt -> {
        if ("date".equals(evt.getPropertyName())) {
            SearchFilters();
        }
    });
}


    private void SearchFilters() {
        String sAppointmentNo = jTextField1.getText().trim();
        String sslmcId = jTextField2.getText().trim();
        Date selectedDate = jDateChooser.getDate();

        String status = "all";

        if (jRadioButtonPending.isSelected()) {
            status = "Pending";
        } else if (jRadioButtonCompleted.isSelected()) {
            status = "Completed";
        } else if (jRadioButtonCancelled.isSelected()) {
            status = "Cancelled";
        }

        loadAppointmentDetails(sAppointmentNo, sslmcId, selectedDate, status);
    }

    private void loadAppointmentDetails() {
        loadAppointmentDetails("", "", null, "all");
    }

    private void loadAppointmentDetails(String sAppointmentNo, String sslmcId, Date selectedDate, String status) {
        try {
            String query = "SELECT appointment_no,patient_name,slmc_id,doctor_name,appointment_room_no,availability_date,time_slot,appointment_status,price FROM appointment_view WHERE 1=1 ";

            if (sAppointmentNo != null && !sAppointmentNo.trim().isEmpty() && !sAppointmentNo.equals("Search By Appointment No")) {
                query += " AND appointment_no LIKE '%" + sAppointmentNo + "%'";
            }
            if (sslmcId != null && !sslmcId.trim().isEmpty() && !sslmcId.equals("Search By Doctor SLMC ID")) {
                query += " AND slmc_id LIKE '%" + sslmcId + "%'";
            }
            if (selectedDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateToString = dateFormat.format(selectedDate);
                query += " AND availability_date = '" + dateToString + "' ";
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
            DefaultTableModel dtm = (DefaultTableModel) arAppointmentTable.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("appointment_no"));
                v.add(rs.getString("patient_name"));
                v.add(rs.getString("slmc_id"));
                v.add(rs.getString("doctor_name"));
                v.add(rs.getString("appointment_room_no"));
                v.add(rs.getString("availability_date"));
                v.add(rs.getString("time_slot"));
                v.add(rs.getString("price"));
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

            arAppointmentTable.setDefaultRenderer(Object.class, renderer);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void radioButtonListener() {
        jRadioButtonPending.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (jRadioButtonPending.isSelected()) {
                    buttonGroup1.clearSelection();
                    loadAppointmentDetails();
                    evt.consume();
                }
            }
        });

        jRadioButtonCompleted.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (jRadioButtonCompleted.isSelected()) {
                    buttonGroup1.clearSelection();
                    loadAppointmentDetails();
                    evt.consume();
                }
            }
        });

        jRadioButtonCancelled.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (jRadioButtonCancelled.isSelected()) {
                    buttonGroup1.clearSelection();
                    loadAppointmentDetails();
                    evt.consume();
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        searchBtn = new javax.swing.JButton();
        addBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        arAppointmentTable = new javax.swing.JTable();
        updateBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        jRadioButtonCancelled = new javax.swing.JRadioButton();
        reportBtn = new javax.swing.JButton();
        jRadioButtonCompleted = new javax.swing.JRadioButton();
        completedBtn = new javax.swing.JButton();
        jRadioButtonPending = new javax.swing.JRadioButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(3, 4, 94));

        jLabel1.setFont(new java.awt.Font("Nunito ExtraBold", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 4, 94));
        jLabel1.setText("Appointment Management");

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

        addBtn.setBackground(new java.awt.Color(3, 4, 94));
        addBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        addBtn.setForeground(new java.awt.Color(144, 224, 239));
        addBtn.setText("New Appointment");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        arAppointmentTable.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        arAppointmentTable.setForeground(new java.awt.Color(3, 4, 94));
        arAppointmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Appointment No", "Patient", "Doctor SLMC ID", "Doctor", "Room", "Date", "Time Solt", "Price", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        arAppointmentTable.setRowHeight(47);
        arAppointmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                arAppointmentTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(arAppointmentTable);
        if (arAppointmentTable.getColumnModel().getColumnCount() > 0) {
            arAppointmentTable.getColumnModel().getColumn(6).setMinWidth(120);
            arAppointmentTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        }

        updateBtn.setBackground(new java.awt.Color(0, 119, 182));
        updateBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        updateBtn.setForeground(new java.awt.Color(202, 240, 248));
        updateBtn.setText("Update");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        cancelBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(255, 0, 0));
        cancelBtn.setText("Cancel");
        cancelBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
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
        jTextField1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextField1PropertyChange(evt);
            }
        });

        jTextField2.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        jTextField2.setText("Search By Doctor SLMC ID");
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

        jDateChooser.setForeground(new java.awt.Color(3, 4, 94));
        jDateChooser.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        jDateChooser.setOpaque(false);
        jDateChooser.setPreferredSize(new java.awt.Dimension(106, 53));
        jDateChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jDateChooserFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jDateChooserFocusLost(evt);
            }
        });
        jDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooserPropertyChange(evt);
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

        reportBtn.setBackground(new java.awt.Color(3, 4, 94));
        reportBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        reportBtn.setForeground(new java.awt.Color(144, 224, 239));
        reportBtn.setText("Generat Report");
        reportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportBtnActionPerformed(evt);
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

        completedBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        completedBtn.setForeground(new java.awt.Color(0, 204, 0));
        completedBtn.setText("Completed");
        completedBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 0)));
        completedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completedBtnActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonPending);
        jRadioButtonPending.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        jRadioButtonPending.setForeground(new java.awt.Color(3, 4, 94));
        jRadioButtonPending.setText("Pending");
        jRadioButtonPending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPendingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonPending)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonCompleted)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonCancelled)
                                .addGap(72, 72, 72)
                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(completedBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))))
                .addGap(14, 14, 14))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(completedBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jRadioButtonCancelled)
                        .addComponent(jRadioButtonCompleted)
                        .addComponent(jRadioButtonPending)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        SearchFilters();
    }//GEN-LAST:event_searchBtnActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        AddAppointment addAppointment = new AddAppointment(null, true);
        addAppointment.setLocationRelativeTo(null);
        addAppointment.setVisible(true);
        SearchFilters();
    }//GEN-LAST:event_addBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        String AppointmentNo = getselectedAppointmentNo();
        UpdateAppointment updateAppointment = new UpdateAppointment(null, true, AppointmentNo);
        updateAppointment.setLocationRelativeTo(null);
        updateAppointment.setVisible(true);
        SearchFilters();
        updateBtn.setVisible(false);
    }//GEN-LAST:event_updateBtnActionPerformed

    private void jRadioButtonCancelledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonCancelledActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jRadioButtonCancelledActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void reportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportBtnActionPerformed
        try {
            InputStream filePath = getClass().getResourceAsStream("/lk/avinam/report/LoadAppointmentDetailsTable1.jasper");

            HashMap<String, Object> parameters = new HashMap<>();

            JRTableModelDataSource jrTMDataSourse = new JRTableModelDataSource(arAppointmentTable.getModel());
            JasperPrint fillReport = JasperFillManager.fillReport(filePath, parameters, jrTMDataSourse);
            JasperViewer.viewReport(fillReport, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_reportBtnActionPerformed

    private void jRadioButtonCompletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonCompletedActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jRadioButtonCompletedActionPerformed

    private String selectedAppointmentNo;

    public String getselectedAppointmentNo() {
        return selectedAppointmentNo;
    }

    private String selectedAppoinmentStatus;
    private String databaseStatus;

    private void arAppointmentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_arAppointmentTableMouseClicked
        if (evt.getClickCount() == 1) {
            int row = arAppointmentTable.getSelectedRow();
            selectedAppointmentNo = (String) arAppointmentTable.getValueAt(row, 0);
            selectedAppoinmentStatus = (String) arAppointmentTable.getValueAt(row, 8);
            updateBtn.setVisible(true);

            try {
                ResultSet rs = MySQL.executeSearch("SELECT appointment_status.appointment_status  FROM appointment \n"
                        + "JOIN appointment_status ON appointment_status.appointment_status_id = appointment.appointment_status_id\n"
                        + "WHERE appointment.appointment_no = '" + selectedAppointmentNo + "';");
                if (rs.next()) {
                    databaseStatus = rs.getString("appointment_status");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if ("Pending".equals(databaseStatus)) {
                cancelBtn.setVisible(true);
                completedBtn.setVisible(true);
                updateBtn.setVisible(true);
            } else {
                cancelBtn.setVisible(false);
                completedBtn.setVisible(false);
                updateBtn.setVisible(false);
            }
        }

    }//GEN-LAST:event_arAppointmentTableMouseClicked

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        cancelAppointment();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void completedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completedBtnActionPerformed
        completedAppointment();
    }//GEN-LAST:event_completedBtnActionPerformed

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

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        if (jTextField2.getText().equals("Search By Doctor SLMC ID")) {
            jTextField2.setText("");
        }
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        if (jTextField2.getText().trim().isEmpty()) {
            jTextField2.setText("Search By Doctor SLMC ID");
        }
    }//GEN-LAST:event_jTextField2FocusLost

    private void jRadioButtonPendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonPendingActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jRadioButtonPendingActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooserPropertyChange


    }//GEN-LAST:event_jDateChooserPropertyChange

    private void jTextField1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextField1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1PropertyChange

    private void jDateChooserFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jDateChooserFocusGained

    }//GEN-LAST:event_jDateChooserFocusGained

    private void jDateChooserFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jDateChooserFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooserFocusLost

    private synchronized void cancelAppointment() {
        String appointmentNo = getselectedAppointmentNo();
        MySQL.executeIUD("UPDATE appointment SET appointment.appointment_status_id = (SELECT appointment_status.appointment_status_id FROM appointment_status WHERE appointment_status = 'Cancelled') WHERE appointment.appointment_no = '" + appointmentNo + "'; ");
        Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, "Appointment status updated to Cancelled.");
        loadAppointmentDetails();
        updateBtn.setVisible(false);
        cancelBtn.setVisible(false);
        completedBtn.setVisible(false);
    }

    private synchronized void completedAppointment() {
        String appointmentNo = getselectedAppointmentNo();
        MySQL.executeIUD("UPDATE appointment SET appointment.appointment_status_id = (SELECT appointment_status.appointment_status_id FROM appointment_status WHERE appointment_status = 'Completed') WHERE appointment.appointment_no = '" + appointmentNo + "'; ");
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, "Appointment status updated to Completed.");
        loadAppointmentDetails();
        updateBtn.setVisible(false);
        cancelBtn.setVisible(false);
        completedBtn.setVisible(false);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JTable arAppointmentTable;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton completedBtn;
    private com.toedter.calendar.JDateChooser jDateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButtonCancelled;
    private javax.swing.JRadioButton jRadioButtonCompleted;
    private javax.swing.JRadioButton jRadioButtonPending;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton reportBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
