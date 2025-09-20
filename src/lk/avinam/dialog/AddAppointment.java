/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package lk.avinam.dialog;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.time.LocalDate;
import lk.avinam.connection.MySQL;
import lk.avinam.util.AppIconUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author pasin
 */
public class AddAppointment extends javax.swing.JDialog {

    private HashMap<String, Integer> patientMap;
    private HashMap<String, Integer> doctorMap;
    private HashMap<String, Integer> DoctorAvailabilDate;

    public AddAppointment(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
        generateAppointmentNumber();
        this.patientMap = new HashMap<>();
        this.doctorMap = new HashMap<>();
        this.DoctorAvailabilDate = new HashMap<>();
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

        doctorInput.addActionListener(evt -> {
            String selectedDoctor = (String) doctorInput.getSelectedItem();
            if (selectedDoctor != null && doctorMap.containsKey(selectedDoctor)) {
                int doctorId = doctorMap.get(selectedDoctor);
                if (doctorId != 0) {
                    loadDoctorAvailabilDate(doctorId);
                } else {
                    DAvailableDateCombo.setModel(
                            new DefaultComboBoxModel<>(new String[]{"Select Availability Date"})
                    );
                }
            }
        });

    }

    private void generateAppointmentNumber() {
        try {

            LocalDate today = LocalDate.now();
            int year = today.getYear();
            int month = today.getMonthValue();

            String sql = "SELECT appointment_no FROM appointment WHERE appointment_no LIKE 'APT-" + year + "-" + String.format("%02d", month) + "-%' ORDER BY appointment_no DESC LIMIT 1";

            java.sql.Connection conn = MySQL.getConnection();
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            String appointmentNo;
            if (rs.next()) {
                String lastNo = rs.getString("appointment_no");
                int num = Integer.parseInt(lastNo.substring(lastNo.lastIndexOf("-") + 1)) + 1;
                appointmentNo = String.format("APT-%d-%02d-%04d", year, month, num);
            } else {
                appointmentNo = String.format("APT-%d-%02d-0001", year, month);
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
            ResultSet rs = MySQL.executeSearch("SELECT doctor_id, CONCAT(f_name,' ', l_name,' - ', specialization,' - ', slmc_id) AS doctor_info FROM doctor;");
            Vector<String> doctors = new Vector<>();
            doctors.add("Select Doctor");
            doctorMap.put("Select Doctor", 0);
            while (rs.next()) {
                String patientName = rs.getString("doctor_info");
                doctorMap.put(patientName, rs.getInt("doctor_id"));
                doctors.add(patientName);
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(doctors);
            doctorInput.setModel(dcm);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDoctorAvailabilDate(int doctorId) {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT availability_schedule_date.availability_date_id, availability_schedule_date.availability_date FROM availability_schedule_date JOIN schedule_date_has_doctor  ON availability_schedule_date.availability_date_id = schedule_date_has_doctor.schedule_date_id WHERE schedule_date_has_doctor.doctor_id = '" + doctorId + "' ORDER BY availability_schedule_date.availability_date;");
            Vector<String> DoctorAvailabilDates = new Vector<>();
            DoctorAvailabilDates.add("Select Doctors Availability Date ");
            DoctorAvailabilDate.put("Select Doctors Availability Date ", 0);
            while (rs.next()) {
                String patientName = rs.getString("availability_date");
                DoctorAvailabilDate.put(patientName, rs.getInt("availability_date_id"));
                DoctorAvailabilDates.add(patientName);
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(DoctorAvailabilDates);
            DAvailableDateCombo.setModel(dcm);

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
        jComboBox2 = new javax.swing.JComboBox<>();
        DAvailableDateCombo = new javax.swing.JComboBox<>();
        addBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        appointment_NO = new javax.swing.JTextField();
        PatientInput = new javax.swing.JComboBox<>();
        doctorInput = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(3, 4, 94));
        jSeparator1.setForeground(new java.awt.Color(3, 4, 94));
        jSeparator1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Nunito ExtraBold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 4, 94));
        jLabel1.setText("New Appointments");

        jComboBox2.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jComboBox2.setForeground(new java.awt.Color(3, 4, 94));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Doctors Availability Slot" }));
        jComboBox2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Doctor Available Slot", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

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

        cancelBtn.setBackground(new java.awt.Color(202, 240, 248));
        cancelBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(3, 4, 94));
        cancelBtn.setText("Cancel");

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
                                .addComponent(DAvailableDateCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
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
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void DAvailableDateComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DAvailableDateComboActionPerformed

    }//GEN-LAST:event_DAvailableDateComboActionPerformed

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
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
