/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package lk.avinam.panel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import lk.avinam.connection.MySQL;
import lk.avinam.dialog.AppointmentRoomNurseView;
import lk.avinam.dialog.NewAppointmentRoom;
import raven.toast.Notifications;

/**
 *
 * @author pasin
 */
public class AppointmentRoomManagement extends javax.swing.JPanel {

    /**
     * Creates new form AppointmentRoomManagement
     */
    public AppointmentRoomManagement() {
        initComponents();
        init();
        loadAppointmentRoomDetails();
        radioButtonListener();
        nurseviewBtn.setVisible(false);
        cancelBtn.setVisible(false);
    }
    
    private void init() {
        appointmentRoomTable.getTableHeader().setFont(new Font("", Font.BOLD, 16));
        appointmentRoomTable.getTableHeader().setOpaque(false);
        appointmentRoomTable.getTableHeader().setBackground(Color.decode("#00B4D8"));
        appointmentRoomTable.getTableHeader().setForeground(Color.decode("#CAF0F8"));
        appointmentRoomTable.getTableHeader().setPreferredSize(new Dimension(0, 47));
        
        FlatSVGIcon plusIcon = new FlatSVGIcon("lk/avinam/icon/plus.svg", 15, 15);
        plusIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#90E0EF")));
        addBtn.setIcon(plusIcon);
        
        FlatSVGIcon searchIcon = new FlatSVGIcon("lk/avinam/icon/search.svg", 15, 15);
        searchIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#FFFFFF")));
        searchBtn.setIcon(searchIcon);
        
        FlatSVGIcon eyeIcon = new FlatSVGIcon("lk/avinam/icon/eye.svg", 20, 20);
        eyeIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#90E0EF")));
        nurseviewBtn.setIcon(eyeIcon);
        
        FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/cancel.svg", 15, 15);
        cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#FF0000")));
        cancelBtn.setIcon(cancelIcon);
        
        appointmentRoomTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = appointmentRoomTable.getSelectedRow();
                if (selectedRow == -1) { // nothing selected
                    nurseviewBtn.setVisible(false);
                    cancelBtn.setVisible(false);
                }
            }
        });
        
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                appointmentRoomTable.clearSelection();
            }
        });
        
    }
    
    private void SearchFilters() {
        String AppointmentRoomNo = jTextField1.getText().trim();
        String sslmcId = jTextField2.getText().trim();
        Date selectedDate = jDateChooser.getDate();
        
        String status = "all";
        
        if (jRadioButtonBooked.isSelected()) {
            status = "Booked";
        } else if (jRadioButtonCancelled.isSelected()) {
            status = "Cancelled";
        }        
        
        loadAppointmentRoomDetails(AppointmentRoomNo, sslmcId, selectedDate, status);
    }
    
    private void loadAppointmentRoomDetails() {
        loadAppointmentRoomDetails("", "", null, "all");
    }
    
    private void loadAppointmentRoomDetails(String AppointmentRoomNo, String sslmcId, Date selectedDate, String status) {
        try {
            String query = "SELECT appointment_room_no, slmc_id, doctor_name, nurse_count, availability_date, time_slot, `status` FROM appointment_room_view WHERE 1=1 ";
            
            if (AppointmentRoomNo != null && !AppointmentRoomNo.trim().isEmpty() && !AppointmentRoomNo.equals("Search By Appointment Room")) {
                query += " AND appointment_room_no LIKE '%" + AppointmentRoomNo + "%'";
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
                if (status.equals("Booked")) {
                    query += " AND status = 'Booked'";
                } else if (status.equals("Cancelled")) {
                    query += " AND status = 'Cancelled'";
                }                
            }
            
            ResultSet rs = MySQL.executeSearch(query);
            DefaultTableModel dtm = (DefaultTableModel) appointmentRoomTable.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("appointment_room_no"));
                v.add(rs.getString("slmc_id"));
                v.add(rs.getString("doctor_name"));
                v.add(rs.getString("nurse_count"));
                v.add(rs.getString("availability_date"));
                v.add(rs.getString("time_slot"));
                v.add(rs.getString("status"));
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
            
            appointmentRoomTable.setDefaultRenderer(Object.class, renderer);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void radioButtonListener() {
        jRadioButtonBooked.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (jRadioButtonBooked.isSelected()) {
                    buttonGroup1.clearSelection();;
                    loadAppointmentRoomDetails();
                    evt.consume();
                }
            }
        });
        
        jRadioButtonCancelled.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (jRadioButtonCancelled.isSelected()) {
                    buttonGroup1.clearSelection();
                    loadAppointmentRoomDetails();
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        searchBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        addBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        appointmentRoomTable = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jRadioButtonCancelled = new javax.swing.JRadioButton();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        jRadioButtonBooked = new javax.swing.JRadioButton();
        nurseviewBtn = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Nunito ExtraBold", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 4, 94));
        jLabel1.setText("Appointment Room Management");

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

        jSeparator1.setForeground(new java.awt.Color(3, 4, 94));

        searchBtn.setBackground(new java.awt.Color(0, 180, 216));
        searchBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        searchBtn.setForeground(new java.awt.Color(255, 255, 255));
        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        cancelBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(255, 51, 51));
        cancelBtn.setText("Cancel");
        cancelBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        addBtn.setBackground(new java.awt.Color(3, 4, 94));
        addBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        addBtn.setForeground(new java.awt.Color(144, 224, 239));
        addBtn.setText("New Room");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        appointmentRoomTable.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        appointmentRoomTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Room Number", "Doctor SLMC ID", "Doctor Name", "Nurse Count", "Date", "Time Slot", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        appointmentRoomTable.setRowHeight(47);
        appointmentRoomTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                appointmentRoomTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(appointmentRoomTable);

        jTextField1.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        jTextField1.setText("Search By Appointment Room");
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

        buttonGroup1.add(jRadioButtonCancelled);
        jRadioButtonCancelled.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        jRadioButtonCancelled.setForeground(new java.awt.Color(255, 0, 0));
        jRadioButtonCancelled.setText("Cancelled");
        jRadioButtonCancelled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonCancelledActionPerformed(evt);
            }
        });

        jDateChooser.setBackground(new java.awt.Color(255, 255, 255));
        jDateChooser.setForeground(new java.awt.Color(3, 4, 94));
        jDateChooser.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jDateChooser.setOpaque(false);
        jDateChooser.setPreferredSize(new java.awt.Dimension(106, 53));

        buttonGroup1.add(jRadioButtonBooked);
        jRadioButtonBooked.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        jRadioButtonBooked.setForeground(new java.awt.Color(3, 4, 94));
        jRadioButtonBooked.setText("Booked");
        jRadioButtonBooked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonBookedActionPerformed(evt);
            }
        });

        nurseviewBtn.setBackground(new java.awt.Color(0, 119, 182));
        nurseviewBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        nurseviewBtn.setForeground(new java.awt.Color(202, 240, 248));
        nurseviewBtn.setText("Nurse View");
        nurseviewBtn.setFocusable(false);
        nurseviewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nurseviewBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonBooked)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonCancelled)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nurseviewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jRadioButtonCancelled)
                                .addComponent(jRadioButtonBooked))
                            .addComponent(jDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nurseviewBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                            .addComponent(cancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jRadioButtonCancelledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonCancelledActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jRadioButtonCancelledActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        NewAppointmentRoom newAppoinmentRoom = new NewAppointmentRoom(null, true);
        newAppoinmentRoom.setLocationRelativeTo(null);
        newAppoinmentRoom.setVisible(true);
    }//GEN-LAST:event_addBtnActionPerformed

    private void jRadioButtonBookedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonBookedActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jRadioButtonBookedActionPerformed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        if (jTextField1.getText().equals("Search By Appointment Room")) {
            jTextField1.setText("");
        }
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        if (jTextField1.getText().trim().isEmpty()) {
            jTextField1.setText("Search By Appointment Room");
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
    
    private String selectedAppointmentRoomNo;
    
    public String getselectedAppointmentRoomNo() {
        return selectedAppointmentRoomNo;
    }
    
    private String selectedAppoinmentRoomStatus;
    private String doctorslmcId;
    private String date;
    private String timeSlot;
    private String timeFrom;
    private String timeTo;
    private String databaseStatus;
    

    private void appointmentRoomTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_appointmentRoomTableMouseClicked
        if (evt.getClickCount() == 1) {
            int row = appointmentRoomTable.getSelectedRow();
            selectedAppointmentRoomNo = (String) appointmentRoomTable.getValueAt(row, 0);
            doctorslmcId = (String) appointmentRoomTable.getValueAt(row, 1);
            date = (String) appointmentRoomTable.getValueAt(row, 4);
            timeSlot = (String) appointmentRoomTable.getValueAt(row, 5);
            selectedAppoinmentRoomStatus = (String) appointmentRoomTable.getValueAt(row, 6);
            nurseviewBtn.setVisible(true);
            
            if (timeSlot != null && !timeSlot.isEmpty()) {
                String[] parts = timeSlot.split(" - ");
                
                DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
                DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
                
                timeFrom = LocalTime.parse(parts[0].trim(), inputFormat).format(outputFormat);
                timeTo = LocalTime.parse(parts[1].trim(), inputFormat).format(outputFormat);
                
            }
            
            try {
                ResultSet rs = MySQL.executeSearch("SELECT room_reservations.`status` FROM appointment_room\n"
                        + "JOIN date_has_time ON date_has_time.appointment_room_id = appointment_room.appointment_room_id\n"
                        + "JOIN doctor ON date_has_time.doctor_id = doctor.doctor_id\n"
                        + "JOIN availability_schedule_date ON date_has_time.availability_date_id = availability_schedule_date.availability_date_id\n"
                        + "JOIN availability_schedule_time ON date_has_time.availability_time_id = availability_schedule_time.availability_time_id\n"
                        + "JOIN room_reservations ON room_reservations.room_reservations_id = date_has_time.room_reservations_id WHERE appointment_room.appointment_room_no = '" + selectedAppointmentRoomNo + "' AND doctor.slmc_id = '" + doctorslmcId + "' \n"
                        + "AND availability_schedule_date.availability_date = '" + date + "' AND availability_schedule_time.availability_time_from = '" + timeFrom + "' AND availability_schedule_time.availability_time_to = '" + timeTo + "';");
                if (rs.next()) {
                    databaseStatus = rs.getString("status");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            if ("Booked".equals(databaseStatus)) {
                cancelBtn.setVisible(true);
            } else {
                cancelBtn.setVisible(false);
            }
            
    }//GEN-LAST:event_appointmentRoomTableMouseClicked
    }
    
    private synchronized void CancelbookedRoom(){
        MySQL.executeIUD("UPDATE date_has_time \n" +
"JOIN appointment_room ON appointment_room.appointment_room_id = date_has_time.appointment_room_id\n" +
"JOIN doctor ON doctor.doctor_id = date_has_time.doctor_id\n" +
"JOIN availability_schedule_date ON availability_schedule_date.availability_date_id = date_has_time.availability_date_id\n" +
"JOIN availability_schedule_time ON availability_schedule_time.availability_time_id = date_has_time.availability_time_id\n" +
"SET date_has_time.room_reservations_id = (SELECT room_reservations.room_reservations_id FROM room_reservations WHERE room_reservations.`status` = 'Cancelled') \n" +
"WHERE appointment_room.appointment_room_no = '" + selectedAppointmentRoomNo + "' AND doctor.slmc_id = '" + doctorslmcId + "' \n" +
"AND availability_schedule_date.availability_date = '" + date + "' AND availability_schedule_time.availability_time_from = '" + timeFrom + "' AND availability_schedule_time.availability_time_to = '" + timeTo + "';");
        
        Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, "Appointment Room status updated to Cancelled.");
        loadAppointmentRoomDetails();
        cancelBtn.setVisible(false);
        
    }
    
    private void nurseviewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nurseviewBtnActionPerformed
        String appointmentRoomNo = getselectedAppointmentRoomNo();
        AppointmentRoomNurseView appointmentRoomNurseView = new AppointmentRoomNurseView(null, true, appointmentRoomNo);
        appointmentRoomNurseView.setLocationRelativeTo(null);
        appointmentRoomNurseView.setVisible(true);
    }//GEN-LAST:event_nurseviewBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        SearchFilters();
    }//GEN-LAST:event_searchBtnActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        SearchFilters();
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        CancelbookedRoom();
    }//GEN-LAST:event_cancelBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JTable appointmentRoomTable;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelBtn;
    private com.toedter.calendar.JDateChooser jDateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButtonBooked;
    private javax.swing.JRadioButton jRadioButtonCancelled;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton nurseviewBtn;
    private javax.swing.JButton searchBtn;
    // End of variables declaration//GEN-END:variables
}
