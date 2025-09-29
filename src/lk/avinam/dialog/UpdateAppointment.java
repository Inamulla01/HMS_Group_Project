/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package lk.avinam.dialog;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import lk.avinam.connection.MySQL;
import lk.avinam.validation.Validater;

/**
 *
 * @author pasin
 */
public class UpdateAppointment extends javax.swing.JDialog {

    private String appointmentNo;
    private HashMap<String, Integer> patientMap;
    private int doctorId = 0;
    private HashMap<String, Integer> doctorAvailabilDate;
    private HashMap<String, Integer> doctorAvailabilSlot;
    private int selectedDateHasTimeId = 0;

    public UpdateAppointment(java.awt.Frame parent, boolean modal, String AppointmentNo) {
        super(parent, modal);
        this.appointmentNo = AppointmentNo;
        this.patientMap = new HashMap<>();
        this.doctorAvailabilDate = new HashMap<>();
        this.doctorAvailabilSlot = new HashMap<>();
        initComponents();
        loadAppointmentData();
        init();

    }

    private void init() {
        FlatSVGIcon addIcon = new FlatSVGIcon("lk/avinam/icon/edit.svg", 20, 20);
        addIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#CAF0F8")));
        editBtn.setIcon(addIcon);
        FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/cancel.svg", 15, 15);
        cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        cancelBtn.setIcon(cancelIcon);

        doctorSlotCombo.addActionListener(evt -> {
            String selectedDate = (String) DAvailableDateCombo.getSelectedItem();
            int dateId = doctorAvailabilDate.getOrDefault(selectedDate, 0);

            String selectedSlot = (String) doctorSlotCombo.getSelectedItem();
            int slotId = doctorAvailabilSlot.getOrDefault(selectedSlot, 0);

            if (doctorId != 0 && dateId != 0 && slotId != 0) {
                loadAppointmentRoomAndPrice(doctorId, dateId, slotId);
            }
        });

        doctorInput.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (doctorId != 0) {
                    loadDoctorAvailabilDate(doctorId);
                }
            }
        });

        doctorInput.addActionListener(evt -> {
            if (doctorId != 0) {
                loadDoctorAvailabilDate(doctorId);
            }
        });
        
        doctorSlotCombo.addActionListener(evt -> {
    String selectedDate = (String) DAvailableDateCombo.getSelectedItem();
    int dateId = doctorAvailabilDate.getOrDefault(selectedDate, 0);

    String selectedSlot = (String) doctorSlotCombo.getSelectedItem();
    int slotId = doctorAvailabilSlot.getOrDefault(selectedSlot, 0);

    if (doctorId != 0 && dateId != 0 && slotId != 0) {
        loadAppointmentRoomAndPrice(doctorId, dateId, slotId);
    }
});


    }

    private void loadDoctorAvailabilDate(int doctorId) {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT DISTINCT availability_schedule_date.availability_date_id, availability_schedule_date.availability_date FROM availability_schedule_date INNER JOIN date_has_time ON availability_schedule_date.availability_date_id = date_has_time.availability_date_id WHERE date_has_time.doctor_id = '" + doctorId + "' AND availability_schedule_date.availability_date >= CURDATE() ORDER BY availability_schedule_date.availability_date;");
            Vector<String> DoctorAvailabilDates = new Vector<>();
            DoctorAvailabilDates.add("Select Doctors Availability Date ");
            doctorAvailabilDate.put("Select Doctors Availability Date ", 0);
            while (rs.next()) {
                String DoctorAvailabilDateName = rs.getString("availability_date");
                doctorAvailabilDate.put(DoctorAvailabilDateName, rs.getInt("availability_date_id"));
                DoctorAvailabilDates.add(DoctorAvailabilDateName);
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(DoctorAvailabilDates);
            DAvailableDateCombo.setModel(dcm);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDoctorAvailabilSlot(int doctorId, int dateId) {
        try {
            doctorAvailabilSlot.clear();

            ResultSet rs = MySQL.executeSearch("SELECT availability_schedule_time.availability_time_id, CONCAT(availability_schedule_time.availability_time_from, ' - ', availability_schedule_time.availability_time_to) AS time_slot FROM availability_schedule_time JOIN date_has_time ON date_has_time.availability_time_id = availability_schedule_time.availability_time_id JOIN availability_schedule_date ON availability_schedule_date.availability_date_id = date_has_time.availability_date_id JOIN doctor ON doctor.doctor_id = date_has_time.doctor_id WHERE doctor.doctor_id = '" + doctorId + "' AND availability_schedule_date.availability_date_id = '" + dateId + "';");

            Vector<String> DoctorAvailabilTime = new Vector<>();
            DoctorAvailabilTime.add("Select Doctors Availability Slot");
            doctorAvailabilSlot.put("Select Doctors Availability Slot", 0);

            while (rs.next()) {
                String slotLabel = rs.getString("time_slot");
                int slotId = rs.getInt("availability_time_id");

                doctorAvailabilSlot.put(slotLabel, slotId);
                DoctorAvailabilTime.add(slotLabel);
            }

            if (DoctorAvailabilTime.size() == 1) {
                DoctorAvailabilTime.set(0, "No Slots Available");
                doctorAvailabilSlot.clear();
                doctorAvailabilSlot.put("No Slots Available", 0);
            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel<>(DoctorAvailabilTime);
            doctorSlotCombo.setModel(dcm);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading doctor's availability slots.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private int selectedRoomId = 0;

    private void loadAppointmentRoomAndPrice(int doctorId, int dateId, int timeId) {
        try {
            String sql = "SELECT appointment_room.appointment_room_id, appointment_room.appointment_room_no, date_has_time.price, date_has_time.date_has_time_id "
                    + "FROM date_has_time "
                    + "JOIN doctor ON doctor.doctor_id = date_has_time.doctor_id "
                    + "JOIN appointment_room ON appointment_room.appointment_room_id = date_has_time.appointment_room_id "
                    + "JOIN availability_schedule_date ON availability_schedule_date.availability_date_id = date_has_time.availability_date_id "
                    + "JOIN availability_schedule_time ON availability_schedule_time.availability_time_id = date_has_time.availability_time_id "
                    + "WHERE doctor.doctor_id = '" + doctorId + "' "
                    + "AND availability_schedule_date.availability_date_id = '" + dateId + "' "
                    + "AND availability_schedule_time.availability_time_id = '" + timeId + "' "
                    + "LIMIT 1;";

            ResultSet rs = MySQL.executeSearch(sql);

            if (rs.next()) {
                selectedRoomId = rs.getInt("appointment_room_id");
                getAppointmentRoomNo.setText(rs.getString("appointment_room_no"));
                getappointmentFree.setText(rs.getString("price"));
                selectedDateHasTimeId = rs.getInt("date_has_time_id");
            } else {
                getAppointmentRoomNo.setText("No Room Available");
                getappointmentFree.setText("");
                selectedDateHasTimeId = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAppointmentData() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT appointment.appointment_no, patient.patient_id, CONCAT(patient.nic_no, ' - ',patient.patient_fname, ' ',patient.patient_lname) AS patient_info, \n"
                    + "doctor.doctor_id, CONCAT(doctor.f_name,' ',doctor.l_name,' - ', doctor.specialization,' - ',doctor.slmc_id) AS doctor_info,\n"
                    + " availability_schedule_date.availability_date_id, availability_schedule_date.availability_date,\n"
                    + "availability_schedule_time.availability_time_id, CONCAT(availability_schedule_time.availability_time_from, ' - ', availability_schedule_time.availability_time_to) AS time_slot,\n"
                    + "appointment_room.appointment_room_id, appointment_room.appointment_room_no, date_has_time.price, date_has_time.date_has_time_id \n"
                    + "FROM appointment\n"
                    + "JOIN patient ON patient.patient_id = appointment.patient_id \n"
                    + "JOIN doctor ON doctor.doctor_id = appointment.doctor_id\n"
                    + "JOIN availability_schedule_date ON availability_schedule_date.availability_date_id = appointment.availability_date_id\n"
                    + "JOIN availability_schedule_time ON availability_schedule_time.availability_time_id = appointment.availability_time_id\n"
                    + "JOIN date_has_time ON availability_schedule_date.availability_date_id = date_has_time.availability_date_id\n"
                    + "AND availability_schedule_time.availability_time_id = date_has_time.availability_time_id\n"
                    + "JOIN appointment_room ON appointment_room.appointment_room_id = appointment.appointment_room_id WHERE appointment.appointment_no = '" + appointmentNo + "';");

            if (rs.next()) {
                doctorId = rs.getInt("doctor_id");
                loadAppointmentNo.setText(rs.getString("appointment_no"));
                PatientInput.setText(rs.getString("patient_info"));
                doctorInput.setText(rs.getString("doctor_info"));
                loadDoctorAvailabilDate(doctorId);
                DAvailableDateCombo.setSelectedItem(rs.getString("availability_date"));
                loadDoctorAvailabilSlot(doctorId, rs.getInt("availability_date_id"));
                doctorSlotCombo.setSelectedItem(rs.getString("time_slot"));
                getAppointmentRoomNo.setText(rs.getString("appointment_room_no"));
                getappointmentFree.setText(rs.getString("price"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private synchronized void updateAppointmentData() {

        String selectedDate = (String) DAvailableDateCombo.getSelectedItem();
        int dAvailableDateId = doctorAvailabilDate.getOrDefault(selectedDate, 0);

        String selectedSlot = (String) doctorSlotCombo.getSelectedItem();
        int doctorSlotId = doctorAvailabilSlot.getOrDefault(selectedSlot, 0);

        if (!Validater.isSelectedItemValid(dAvailableDateId)) {
            return;
        } else if (!Validater.isSelectedItemValid(doctorSlotId)) {
            return;
        }

        MySQL.executeIUD("UPDATE appointment SET availability_date_id = '" + dAvailableDateId + "', availability_time_id = '" + doctorSlotId + "', appointment_room_id = '"+selectedRoomId+"' WHERE appointment_no = '" + appointmentNo + "';");

        JOptionPane.showMessageDialog(null, "Appointment updated successfully!", "Appointment Information", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        loadAppointmentNo = new javax.swing.JTextField();
        DAvailableDateCombo = new javax.swing.JComboBox<>();
        doctorSlotCombo = new javax.swing.JComboBox<>();
        getAppointmentRoomNo = new javax.swing.JTextField();
        getappointmentFree = new javax.swing.JTextField();
        PatientInput = new javax.swing.JTextField();
        doctorInput = new javax.swing.JTextField();
        reportBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(3, 4, 94));
        jSeparator1.setForeground(new java.awt.Color(3, 4, 94));
        jSeparator1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Nunito ExtraBold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 4, 94));
        jLabel1.setText("Update Appointments");

        cancelBtn.setBackground(new java.awt.Color(202, 240, 248));
        cancelBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(3, 4, 94));
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        editBtn.setBackground(new java.awt.Color(3, 4, 94));
        editBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        editBtn.setForeground(new java.awt.Color(144, 224, 239));
        editBtn.setText("Update");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        loadAppointmentNo.setEditable(false);
        loadAppointmentNo.setBackground(new java.awt.Color(255, 255, 255));
        loadAppointmentNo.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        loadAppointmentNo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Appointment No", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        DAvailableDateCombo.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        DAvailableDateCombo.setForeground(new java.awt.Color(3, 4, 94));
        DAvailableDateCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Doctors Availability Date" }));
        DAvailableDateCombo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Doctor Available Date", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        DAvailableDateCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DAvailableDateComboActionPerformed(evt);
            }
        });

        doctorSlotCombo.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        doctorSlotCombo.setForeground(new java.awt.Color(3, 4, 94));
        doctorSlotCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Doctors Availability Slot" }));
        doctorSlotCombo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Doctor Available Slot", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        getAppointmentRoomNo.setEditable(false);
        getAppointmentRoomNo.setBackground(new java.awt.Color(255, 255, 255));
        getAppointmentRoomNo.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        getAppointmentRoomNo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Appointment Room No", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        getAppointmentRoomNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getAppointmentRoomNoActionPerformed(evt);
            }
        });

        getappointmentFree.setEditable(false);
        getappointmentFree.setBackground(new java.awt.Color(255, 255, 255));
        getappointmentFree.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        getappointmentFree.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Appointment Fee", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        PatientInput.setEditable(false);
        PatientInput.setBackground(new java.awt.Color(255, 255, 255));
        PatientInput.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        PatientInput.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Patient", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        doctorInput.setEditable(false);
        doctorInput.setBackground(new java.awt.Color(255, 255, 255));
        doctorInput.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        doctorInput.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Doctor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        reportBtn.setBackground(new java.awt.Color(3, 4, 94));
        reportBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        reportBtn.setForeground(new java.awt.Color(144, 224, 239));
        reportBtn.setText("Generat Report");
        reportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(loadAppointmentNo, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PatientInput, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(doctorInput, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DAvailableDateCombo, 0, 266, Short.MAX_VALUE)
                            .addComponent(getAppointmentRoomNo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(getappointmentFree, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(doctorSlotCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loadAppointmentNo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PatientInput, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(doctorInput, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DAvailableDateCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(doctorSlotCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getAppointmentRoomNo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(getappointmentFree, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(reportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void DAvailableDateComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DAvailableDateComboActionPerformed
        String selectedDate = (String) DAvailableDateCombo.getSelectedItem();
        if (selectedDate != null) {
            int dateId = doctorAvailabilDate.getOrDefault(selectedDate, 0);
            if (doctorId != 0 && dateId != 0) {
                loadDoctorAvailabilSlot(doctorId, dateId);
                getAppointmentRoomNo.setText("");
                getappointmentFree.setText("");
            }
        }


    }//GEN-LAST:event_DAvailableDateComboActionPerformed

    private void getAppointmentRoomNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getAppointmentRoomNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_getAppointmentRoomNoActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        updateAppointmentData();
    }//GEN-LAST:event_editBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void reportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reportBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> DAvailableDateCombo;
    private javax.swing.JTextField PatientInput;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTextField doctorInput;
    private javax.swing.JComboBox<String> doctorSlotCombo;
    private javax.swing.JButton editBtn;
    private javax.swing.JTextField getAppointmentRoomNo;
    private javax.swing.JTextField getappointmentFree;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField loadAppointmentNo;
    private javax.swing.JButton reportBtn;
    // End of variables declaration//GEN-END:variables
}
