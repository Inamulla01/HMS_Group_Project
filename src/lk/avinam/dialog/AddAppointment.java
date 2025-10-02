/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package lk.avinam.dialog;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.io.InputStream;
import lk.avinam.connection.MySQL;
import lk.avinam.util.AppIconUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import lk.avinam.validation.Validater;
import lk.avinam.validation.Validation;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import raven.toast.Notifications;

/**
 *
 * @author pasin
 */
public class AddAppointment extends javax.swing.JDialog {

    private HashMap<String, Integer> patientMap;
    private HashMap<String, Integer> doctorMap;
    private HashMap<String, Integer> doctorAvailabilDate;
    private HashMap<String, Integer> doctorAvailabilSlot;
    private int selectedDateHasTimeId = 0;

    public AddAppointment(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
        generateAppointmentNumber();
        this.patientMap = new HashMap<>();
        this.doctorMap = new HashMap<>();
        this.doctorAvailabilDate = new HashMap<>();
        this.doctorAvailabilSlot = new HashMap<>();
        loadPatient();
        loadDoctor();

    }

    private void init() {
        FlatSVGIcon addIcon = new FlatSVGIcon("lk/avinam/icon/plus.svg", 15, 15);
        addIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#CAF0F8")));
        addBtn.setIcon(addIcon);
        FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/cancel.svg", 15, 15);
        cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        cancelBtn.setIcon(cancelIcon);
        AutoCompleteDecorator.decorate(PatientInput);
        AutoCompleteDecorator.decorate(doctorInput);
        AutoCompleteDecorator.decorate(DAvailableDateCombo);
        AutoCompleteDecorator.decorate(doctorSlotCombo);

        doctorInput.addActionListener(evt -> {
            String selectedDoctor = (String) doctorInput.getSelectedItem();
            int doctorId = doctorMap.getOrDefault(selectedDoctor, 0);

            DAvailableDateCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Select Doctors Availability Date"}));
            doctorSlotCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Select Doctors Availability Slot"}));
            getAppointmentRoomNo.setText("");
            getappointmentFree.setText("");
            selectedDateHasTimeId = 0;

            if (doctorId != 0) {
                loadDoctorAvailabilDate(doctorId);

                for (java.awt.event.ActionListener al : DAvailableDateCombo.getActionListeners()) {
                    DAvailableDateCombo.removeActionListener(al);
                }

                DAvailableDateCombo.addActionListener(dateEvt -> {
                    String selectedDate = (String) DAvailableDateCombo.getSelectedItem();
                    int dateId = doctorAvailabilDate.getOrDefault(selectedDate, 0);

                    doctorSlotCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Select Doctors Availability Slot"}));
                    getAppointmentRoomNo.setText("");
                    getappointmentFree.setText("");

                    if (dateId != 0) {
                        loadDoctorAvailabilSlot(doctorId, dateId);

                        for (java.awt.event.ActionListener al : doctorSlotCombo.getActionListeners()) {
                            doctorSlotCombo.removeActionListener(al);
                        }

                        doctorSlotCombo.addActionListener(slotEvt -> {
                            String selectedSlot = (String) doctorSlotCombo.getSelectedItem();
                            int slotId = doctorAvailabilSlot.getOrDefault(selectedSlot, 0);

                            getAppointmentRoomNo.setText("");
                            getappointmentFree.setText("");

                            if (slotId != 0) {
                                loadAppointmentRoomAndPrice(doctorId, dateId, slotId);
                            }
                        });
                    }
                });
            }
        });

        doctorSlotCombo.addActionListener(slotEvt -> {
            String selectedSlot = (String) doctorSlotCombo.getSelectedItem();
            if (selectedSlot != null && doctorAvailabilSlot.containsKey(selectedSlot)) {
                int timeId = doctorAvailabilSlot.get(selectedSlot);
                if (timeId != 0) {
                    int doctorId = doctorMap.getOrDefault((String) doctorInput.getSelectedItem(), 0);
                    int dateId = doctorAvailabilDate.getOrDefault((String) DAvailableDateCombo.getSelectedItem(), 0);

                    loadAppointmentRoomAndPrice(doctorId, dateId, timeId);
                } else {
                    getAppointmentRoomNo.setText("No Room Available");
                    getappointmentFree.setText("");
                    selectedDateHasTimeId = 0;
                }
            }
        });

    }

    private void generateAppointmentNumber() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT appointment_no FROM appointment ORDER BY appointment_no DESC LIMIT 1");

            String appointmentNo;
            if (rs.next()) {
                String lastNo = rs.getString("appointment_no");
                int num = Integer.parseInt(lastNo.substring(3)) + 1;
                appointmentNo = String.format("APT%04d", num);
            } else {
                appointmentNo = "APT0001";
            }

            appointment_NO.setText(appointmentNo);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPatient() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT patient_id, CONCAT(nic_no, ' - ', patient_fname, ' ', patient_lname) AS patient_info FROM patient;");
            Vector<String> patients = new Vector<>();
            patients.add("Select Patient");
            patientMap.put("Select Patient", 0);
            while (rs.next()) {
                String patientName = rs.getString("patient_info");
                patientMap.put(patientName, rs.getInt("patient_id"));
                patients.add(patientName);
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(patients);
            PatientInput.setModel(dcm);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDoctor() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT doctor_id, CONCAT(f_name,' ', l_name,' - ', specialization,' - ', slmc_id) AS doctor_info FROM doctor JOIN doctor_type ON doctor_type.doctor_type_id = doctor.doctor_type_id WHERE doctor_type.doctor_type = 'Out Patient Doctor';");
            Vector<String> doctors = new Vector<>();
            doctors.add("Select Doctor");
            doctorMap.put("Select Doctor", 0);
            while (rs.next()) {
                String DoctorName = rs.getString("doctor_info");
                doctorMap.put(DoctorName, rs.getInt("doctor_id"));
                doctors.add(DoctorName);
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(doctors);
            doctorInput.setModel(dcm);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, "Error loading doctor's availability slots.");

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        doctorSlotCombo = new javax.swing.JComboBox<>();
        DAvailableDateCombo = new javax.swing.JComboBox<>();
        addBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        appointment_NO = new javax.swing.JTextField();
        PatientInput = new javax.swing.JComboBox<>();
        doctorInput = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        getAppointmentRoomNo = new javax.swing.JTextField();
        getappointmentFree = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(3, 4, 94));
        jSeparator1.setForeground(new java.awt.Color(3, 4, 94));
        jSeparator1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Nunito ExtraBold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 4, 94));
        jLabel1.setText("New Appointments");

        doctorSlotCombo.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        doctorSlotCombo.setForeground(new java.awt.Color(3, 4, 94));
        doctorSlotCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Doctors Availability Slot" }));
        doctorSlotCombo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Doctor Available Slot", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        DAvailableDateCombo.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        DAvailableDateCombo.setForeground(new java.awt.Color(3, 4, 94));
        DAvailableDateCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Doctors Availability Date" }));
        DAvailableDateCombo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Doctor Available Date", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        DAvailableDateCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DAvailableDateComboActionPerformed(evt);
            }
        });

        addBtn.setBackground(new java.awt.Color(3, 4, 94));
        addBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        addBtn.setForeground(new java.awt.Color(202, 240, 248));
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

        appointment_NO.setEditable(false);
        appointment_NO.setBackground(new java.awt.Color(255, 255, 255));
        appointment_NO.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        appointment_NO.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Appointment No", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        PatientInput.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        PatientInput.setForeground(new java.awt.Color(3, 4, 94));
        PatientInput.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PatientInput.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Patient", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        doctorInput.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        doctorInput.setForeground(new java.awt.Color(3, 4, 94));
        doctorInput.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        doctorInput.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Doctor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        jButton5.setBackground(new java.awt.Color(0, 180, 216));
        jButton5.setFont(new java.awt.Font("Nunito ExtraBold", 1, 24)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("+");
        jButton5.setPreferredSize(new java.awt.Dimension(41, 43));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        getAppointmentRoomNo.setEditable(false);
        getAppointmentRoomNo.setBackground(new java.awt.Color(255, 255, 255));
        getAppointmentRoomNo.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        getAppointmentRoomNo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Appointment Room No", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        getappointmentFree.setEditable(false);
        getappointmentFree.setBackground(new java.awt.Color(255, 255, 255));
        getappointmentFree.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        getappointmentFree.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Appointment Fee", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(appointment_NO, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(PatientInput, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(doctorInput, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(DAvailableDateCombo, 0, 281, Short.MAX_VALUE)
                                    .addComponent(getAppointmentRoomNo))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(getappointmentFree, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(doctorSlotCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addGap(14, 14, 14))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(appointment_NO, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PatientInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(doctorInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DAvailableDateCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(doctorSlotCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getAppointmentRoomNo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(getappointmentFree, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        AddPatient addPatient = new AddPatient(null, true);
        addPatient.setLocationRelativeTo(null);
        addPatient.setVisible(true);
        loadPatient();
    }//GEN-LAST:event_jButton5ActionPerformed

    private synchronized void insertAppointmentData() {

        String appointmentNo = appointment_NO.getText();

        String selectedPatient = (String) PatientInput.getSelectedItem();
        int patientId = patientMap.getOrDefault(selectedPatient, 0);

        String selectedDoctor = (String) doctorInput.getSelectedItem();
        int doctorId = doctorMap.getOrDefault(selectedDoctor, 0);

        String selectedDate = (String) DAvailableDateCombo.getSelectedItem();
        int dAvailableDateId = doctorAvailabilDate.getOrDefault(selectedDate, 0);

        String selectedSlot = (String) doctorSlotCombo.getSelectedItem();
        int doctorSlotId = doctorAvailabilSlot.getOrDefault(selectedSlot, 0);
        if (!Validater.isInputFieldValid(appointmentNo)) {
            return;
        } else if (!Validater.isSelectedItemValid(patientId)) {
            return;
        } else if (!Validater.isSelectedItemValid(doctorId)) {
            return;
        } else if (!Validater.isSelectedItemValid(dAvailableDateId)) {
            return;
        } else if (!Validater.isSelectedItemValid(doctorSlotId)) {
            return;
        }

        try {
            ResultSet rs = MySQL.executeSearch("SELECT appointment_no FROM appointment WHERE appointment_no = '" + appointmentNo + "';");
            if (rs.next()) {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "This appointment is already exist");
            } else {
                MySQL.executeIUD("INSERT INTO appointment (appointment_no, patient_id, doctor_id, availability_date_id,availability_time_id,appointment_room_id, appointment_status_id) VALUES ('" + appointmentNo + "', '" + patientId + "', '" + doctorId + "', '" + dAvailableDateId + "','" + doctorSlotId + "','" + selectedRoomId + "', (SELECT appointment_status_id FROM appointment_status WHERE appointment_status = 'Pending'));");
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, "New Appointment added successfully!");
                this.dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void appointmentReport() {
        try {
            HashMap<String, Object> parameters = new HashMap<>();

            parameters.put("appointmentNo", appointment_NO.getText());
            parameters.put("Patient", PatientInput.getSelectedItem());
            parameters.put("Doctor", doctorInput.getSelectedItem());
            parameters.put("DADate", DAvailableDateCombo.getSelectedItem());
            parameters.put("DASlot", doctorSlotCombo.getSelectedItem());
            parameters.put("ARoomNo", getAppointmentRoomNo.getText());
            parameters.put("AFee", getappointmentFree.getText());
            String rawBarcode = appointment_NO.getText();
            String numericBarcode = rawBarcode.replaceAll("\\D", "");
            parameters.put("BARCODE", numericBarcode);

            InputStream jrxmlStream = getClass().getResourceAsStream("/lk/avinam/report/AppointmentReport.jrxml");

            JasperReport jasperreport = JasperCompileManager.compileReport(jrxmlStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperreport, parameters, new JREmptyDataSource());
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);
            viewer.toFront();
            viewer.requestFocus();

        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private void DAvailableDateComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DAvailableDateComboActionPerformed

    }//GEN-LAST:event_DAvailableDateComboActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        insertAppointmentData();
        appointmentReport();
    }//GEN-LAST:event_addBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddAppointment dialog = new AddAppointment(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> DAvailableDateCombo;
    private javax.swing.JComboBox<String> PatientInput;
    private javax.swing.JButton addBtn;
    private javax.swing.JTextField appointment_NO;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> doctorInput;
    private javax.swing.JComboBox<String> doctorSlotCombo;
    private javax.swing.JTextField getAppointmentRoomNo;
    private javax.swing.JTextField getappointmentFree;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
