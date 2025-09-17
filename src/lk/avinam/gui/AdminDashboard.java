/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package lk.avinam.gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import lk.avinam.panel.AdminAndReceptionistAppointment;
import lk.avinam.panel.AdminDashboardPanel;
import lk.avinam.panel.AppointmentRoomManagement;
import lk.avinam.panel.AttendancePanel;
import lk.avinam.panel.DoctorManagementPanel;
import lk.avinam.panel.NurseManagment;
import lk.avinam.panel.PatientManagementPanel;
import lk.avinam.panel.StaffManagment;
import lk.avinam.panel.RoomManagement;
import lk.avinam.panel.WardManagement;
import lk.avinam.util.AppIconUtil;

/**
 *
 * @author Inaamul Hasan
 */
public class AdminDashboard extends javax.swing.JFrame {

    private AdminDashboardPanel adminDashboardPanel;
    private StaffManagment staffManagmentPanel;
    private DoctorManagementPanel doctorManagementPanel;
    private NurseManagment nurseManagementPanel;
    private PatientManagementPanel patientManagementPanel;
    private RoomManagement roomManagementPanel;
    private AdminAndReceptionistAppointment appointmentManagementPanel;
    private AppointmentRoomManagement appointmentRoomManagementPanel;
    private WardManagement wardManagementPanel;
        private AttendancePanel attendanceManagementPanel;
    private CardLayout contentPanelLayout;

    /**
     * Creates new form Dashboard
     */
    public AdminDashboard() {
        initComponents();
        init();
        loadPanels();
    }

    private void init() {
        AppIconUtil.applyIcon(this);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        FlatSVGIcon dashboardIcon = new FlatSVGIcon("lk/avinam/icon/dashboard.svg", 20, 20);
        dashboardIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        adminDashboardBtn.setIcon(dashboardIcon);
        
        FlatSVGIcon staffIcon = new FlatSVGIcon("lk/avinam/icon/staff.svg", 20, 20);
        staffIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        staffBtn.setIcon(staffIcon);
        
        FlatSVGIcon doctorIcon = new FlatSVGIcon("lk/avinam/icon/doctor.svg", 20, 20);
        doctorIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        doctorBtn.setIcon(doctorIcon);
        
        FlatSVGIcon nurseIcon = new FlatSVGIcon("lk/avinam/icon/nurse.svg", 20, 20);
        nurseIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        nurseBtn.setIcon(nurseIcon);
        
        FlatSVGIcon patientIcon = new FlatSVGIcon("lk/avinam/icon/patient-bed-hospital.svg", 20, 20);
        patientIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        patientBtn.setIcon(patientIcon);
        
        FlatSVGIcon roomIcon = new FlatSVGIcon("lk/avinam/icon/room.svg", 20, 20);
        roomIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        roomBtn.setIcon(roomIcon);
        
        FlatSVGIcon appointmentIcon = new FlatSVGIcon("lk/avinam/icon/appointment.svg", 20, 20);
        appointmentIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        appointmentBtn.setIcon(appointmentIcon);
        
        FlatSVGIcon appointmentRoomIcon = new FlatSVGIcon("lk/avinam/icon/room-appinment.svg", 20, 20);
        appointmentRoomIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        appointmentRoomBtn.setIcon(appointmentRoomIcon);
        
        FlatSVGIcon wardIcon = new FlatSVGIcon("lk/avinam/icon/ward.svg", 20, 20);
        wardIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        wardBtn.setIcon(wardIcon);
        
                FlatSVGIcon attendanceIcon = new FlatSVGIcon("lk/avinam/icon/attendance.svg", 25, 25);
        attendanceIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        attendanceBtn.setIcon(attendanceIcon);
        
        FlatSVGIcon logOutIcon = new FlatSVGIcon("lk/avinam/icon/log-out.svg", 20, 20);
        logOutIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        logOutBtn.setIcon(logOutIcon);
        
        
    }

    private void loadPanels() {
        if (contentPanelLayout == null && contentPanel.getLayout() instanceof CardLayout) {
            this.contentPanelLayout = (CardLayout) contentPanel.getLayout();
            System.out.println(contentPanelLayout);
        }

        this.adminDashboardPanel = new AdminDashboardPanel();
        this.contentPanel.add(adminDashboardPanel, "dashboard_panel");
        
        this.staffManagmentPanel = new StaffManagment();
        this.contentPanel.add(staffManagmentPanel, "staff_panel");
        
        this.doctorManagementPanel = new DoctorManagementPanel();
        this.contentPanel.add(doctorManagementPanel, "doctor_panel");
        
        this.nurseManagementPanel = new NurseManagment();
        this.contentPanel.add(nurseManagementPanel, "nurse_panel");
        
        this.patientManagementPanel = new PatientManagementPanel();
        this.contentPanel.add(patientManagementPanel, "patient_panel");
        
        this.roomManagementPanel = new RoomManagement();
        this.contentPanel.add(roomManagementPanel, "room_panel");
        
           this.appointmentRoomManagementPanel = new AppointmentRoomManagement(); 
        this.contentPanel.add(appointmentRoomManagementPanel, "appointmentRoomManagement_panel");
        
        this.appointmentManagementPanel = new AdminAndReceptionistAppointment();
        this.contentPanel.add(appointmentManagementPanel, "appointment_panel");
        
        this.wardManagementPanel = new WardManagement();
        this.contentPanel.add(wardManagementPanel, "ward_panel");

                this.attendanceManagementPanel = new AttendancePanel();
        this.contentPanel.add(attendanceManagementPanel, "attendance_panel");
        SwingUtilities.updateComponentTreeUI(contentPanel);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuPanel = new javax.swing.JPanel();
        adminDashboardBtn = new javax.swing.JButton();
        staffBtn = new javax.swing.JButton();
        doctorBtn = new javax.swing.JButton();
        nurseBtn = new javax.swing.JButton();
        patientBtn = new javax.swing.JButton();
        roomBtn = new javax.swing.JButton();
        appointmentBtn = new javax.swing.JButton();
        logOutBtn = new javax.swing.JButton();
        appointmentRoomBtn = new javax.swing.JButton();
        wardBtn = new javax.swing.JButton();
        attendanceBtn = new javax.swing.JButton();
        headerPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Medi Sewana | Admin Dashboard");

        menuPanel.setBackground(new java.awt.Color(3, 4, 94));

        adminDashboardBtn.setBackground(new java.awt.Color(144, 224, 239));
        adminDashboardBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        adminDashboardBtn.setForeground(new java.awt.Color(3, 4, 94));
        adminDashboardBtn.setText(" Dashboard");
        adminDashboardBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        adminDashboardBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminDashboardBtnActionPerformed(evt);
            }
        });

        staffBtn.setBackground(new java.awt.Color(144, 224, 239));
        staffBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        staffBtn.setForeground(new java.awt.Color(3, 4, 94));
        staffBtn.setText(" Staff Management");
        staffBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        staffBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffBtnActionPerformed(evt);
            }
        });

        doctorBtn.setBackground(new java.awt.Color(144, 224, 239));
        doctorBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        doctorBtn.setForeground(new java.awt.Color(3, 4, 94));
        doctorBtn.setText(" Doctor Management");
        doctorBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        doctorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doctorBtnActionPerformed(evt);
            }
        });

        nurseBtn.setBackground(new java.awt.Color(144, 224, 239));
        nurseBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        nurseBtn.setForeground(new java.awt.Color(3, 4, 94));
        nurseBtn.setText(" Nurse Management");
        nurseBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nurseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nurseBtnActionPerformed(evt);
            }
        });

        patientBtn.setBackground(new java.awt.Color(144, 224, 239));
        patientBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        patientBtn.setForeground(new java.awt.Color(3, 4, 94));
        patientBtn.setText(" Patient Management");
        patientBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        patientBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientBtnActionPerformed(evt);
            }
        });

        roomBtn.setBackground(new java.awt.Color(144, 224, 239));
        roomBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        roomBtn.setForeground(new java.awt.Color(3, 4, 94));
        roomBtn.setText(" Room Management");
        roomBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        roomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomBtnActionPerformed(evt);
            }
        });

        appointmentBtn.setBackground(new java.awt.Color(144, 224, 239));
        appointmentBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        appointmentBtn.setForeground(new java.awt.Color(3, 4, 94));
        appointmentBtn.setText(" Appointment Management");
        appointmentBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        appointmentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appointmentBtnActionPerformed(evt);
            }
        });

        logOutBtn.setBackground(new java.awt.Color(144, 224, 239));
        logOutBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        logOutBtn.setForeground(new java.awt.Color(3, 4, 94));
        logOutBtn.setText("Log Out");
        logOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutBtnActionPerformed(evt);
            }
        });

        appointmentRoomBtn.setBackground(new java.awt.Color(144, 224, 239));
        appointmentRoomBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        appointmentRoomBtn.setForeground(new java.awt.Color(3, 4, 94));
        appointmentRoomBtn.setText(" Appointment Room");
        appointmentRoomBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        appointmentRoomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appointmentRoomBtnActionPerformed(evt);
            }
        });

        wardBtn.setBackground(new java.awt.Color(144, 224, 239));
        wardBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        wardBtn.setForeground(new java.awt.Color(3, 4, 94));
        wardBtn.setText(" Ward Management");
        wardBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        wardBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wardBtnActionPerformed(evt);
            }
        });

        attendanceBtn.setBackground(new java.awt.Color(144, 224, 239));
        attendanceBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        attendanceBtn.setForeground(new java.awt.Color(3, 4, 94));
        attendanceBtn.setText("Attendance Managment");
        attendanceBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        attendanceBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attendanceBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(staffBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(doctorBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(nurseBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(patientBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(roomBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(appointmentBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(logOutBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(appointmentRoomBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(wardBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(adminDashboardBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(attendanceBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(adminDashboardBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(staffBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(doctorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nurseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(patientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roomBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(wardBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(appointmentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(appointmentRoomBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(attendanceBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(logOutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        headerPanel.setBackground(new java.awt.Color(3, 4, 94));

        jLabel1.setFont(new java.awt.Font("Nunito ExtraBold", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(202, 240, 248));
        jLabel1.setText("Admin Dashboard");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(428, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        contentPanel.setBackground(new java.awt.Color(202, 240, 248));
        contentPanel.setLayout(new java.awt.CardLayout());
        jScrollPane1.setViewportView(contentPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void adminDashboardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminDashboardBtnActionPerformed
        // TODO add your handling code here:

        this.contentPanelLayout.show(contentPanel, "dashboard_panel");
    }//GEN-LAST:event_adminDashboardBtnActionPerformed

    private void staffBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffBtnActionPerformed
        // TODO add your handling code here:
           this.contentPanelLayout.show(contentPanel, "staff_panel");
    }//GEN-LAST:event_staffBtnActionPerformed

    private void doctorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doctorBtnActionPerformed
        // TODO add your handling code here:
        this.contentPanelLayout.show(contentPanel, "doctor_panel");
    }//GEN-LAST:event_doctorBtnActionPerformed

    private void nurseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nurseBtnActionPerformed
        // TODO add your handling code here:
        this.contentPanelLayout.show(contentPanel, "nurse_panel");
    }//GEN-LAST:event_nurseBtnActionPerformed

    private void patientBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientBtnActionPerformed
        // TODO add your handling code here:
        this.contentPanelLayout.show(contentPanel, "patient_panel");
    }//GEN-LAST:event_patientBtnActionPerformed

    private void roomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomBtnActionPerformed
        // TODO add your handling code here:
        this.contentPanelLayout.show(contentPanel, "room_panel");
    }//GEN-LAST:event_roomBtnActionPerformed

    private void appointmentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appointmentBtnActionPerformed
        // TODO add your handling code here:
        this.contentPanelLayout.show(contentPanel, "appointment_panel");
    }//GEN-LAST:event_appointmentBtnActionPerformed

    private void logOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_logOutBtnActionPerformed

    private void appointmentRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appointmentRoomBtnActionPerformed
        // TODO add your handling code here:
          this.contentPanelLayout.show(contentPanel, "appointmentRoomManagement_panel");
    }//GEN-LAST:event_appointmentRoomBtnActionPerformed

    private void wardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wardBtnActionPerformed
        // TODO add your handling code here:
        this.contentPanelLayout.show(contentPanel, "ward_panel");
    }//GEN-LAST:event_wardBtnActionPerformed

    private void attendanceBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attendanceBtnActionPerformed
        // TODO add your handling code here:
         this.contentPanelLayout.show(contentPanel, "attendance_panel");
    }//GEN-LAST:event_attendanceBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton adminDashboardBtn;
    private javax.swing.JButton appointmentBtn;
    private javax.swing.JButton appointmentRoomBtn;
    private javax.swing.JButton attendanceBtn;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton doctorBtn;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logOutBtn;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JButton nurseBtn;
    private javax.swing.JButton patientBtn;
    private javax.swing.JButton roomBtn;
    private javax.swing.JButton staffBtn;
    private javax.swing.JButton wardBtn;
    // End of variables declaration//GEN-END:variables
}
