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
import lk.avinam.panel.AppointmentRoomManagement;
import lk.avinam.panel.DoctorManagementPanel;
import lk.avinam.panel.PatientManagementPanel;
import lk.avinam.panel.ReceptionDashboardPanel;
import lk.avinam.panel.RoomManagement;
import lk.avinam.panel.WardManagement;
import lk.avinam.util.AppIconUtil;

/**
 *
 * @author Inaamul Hasan
 */
public class ReceptionDashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    private ReceptionDashboardPanel dashboardPanel;
    private PatientManagementPanel patientManagementPanel;
    private AdminAndReceptionistAppointment appointementPanel;
    private RoomManagement wardNroomPanel;
    private AppointmentRoomManagement appointmentRoomManagementPanel;
    private DoctorManagementPanel doctorManagementPanel;
        private WardManagement wardManagementPanel;
    private CardLayout contentPanelLayout;

    public ReceptionDashboard() {
        initComponents();
        init();
        loadPanels();
    }

    private void init() {
        AppIconUtil.applyIcon(this);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                FlatSVGIcon dashboardIcon = new FlatSVGIcon("lk/avinam/icon/dashboard.svg", 20, 20);
        dashboardIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        staffDashboardBtn.setIcon(dashboardIcon);

        FlatSVGIcon doctorIcon = new FlatSVGIcon("lk/avinam/icon/doctor.svg", 20, 20);
        doctorIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        doctorBtn.setIcon(doctorIcon);
        
        
        FlatSVGIcon patientIcon = new FlatSVGIcon("lk/avinam/icon/patient-bed-hospital.svg", 20, 20);
        patientIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        staffPatientManageBtn.setIcon(patientIcon);
        
        FlatSVGIcon roomIcon = new FlatSVGIcon("lk/avinam/icon/room.svg", 20, 20);
        roomIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        roomBtn.setIcon(roomIcon);
        
        FlatSVGIcon appointmentIcon = new FlatSVGIcon("lk/avinam/icon/appointment.svg", 20, 20);
        appointmentIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        appointmentBtn.setIcon(appointmentIcon);
        
        FlatSVGIcon appointmentRoomIcon = new FlatSVGIcon("lk/avinam/icon/room-appinment.svg", 20, 20);
        appointmentRoomIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        appoinmentRoomBtn.setIcon(appointmentRoomIcon);
        
        FlatSVGIcon wardIcon = new FlatSVGIcon("lk/avinam/icon/ward.svg", 20, 20);
        wardIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        wardBtn.setIcon(wardIcon);
        
        FlatSVGIcon logOutIcon = new FlatSVGIcon("lk/avinam/icon/log-out.svg", 20, 20);
        logOutIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        logOutBtn.setIcon(logOutIcon);

    }

    private void loadPanels() {
        if (contentPanelLayout == null && contentPanel.getLayout() instanceof CardLayout) {
            this.contentPanelLayout = (CardLayout) contentPanel.getLayout();
        }

        this.dashboardPanel = new ReceptionDashboardPanel();
        this.contentPanel.add(dashboardPanel, "dashboard_panel");
        
        this.patientManagementPanel = new PatientManagementPanel(); 
        this.contentPanel.add(patientManagementPanel, "patientManagement_panel");
        
        this.appointementPanel = new AdminAndReceptionistAppointment(); 
        this.contentPanel.add(appointementPanel, "appointmentManagement_panel");
        
        this.wardNroomPanel = new RoomManagement(); 
        this.contentPanel.add(wardNroomPanel, "wardNroomManagement_panel");
        
        this.appointmentRoomManagementPanel = new AppointmentRoomManagement(); 
        this.contentPanel.add(appointmentRoomManagementPanel, "appointmentRoomManagement_panel");
        
         this.doctorManagementPanel = new DoctorManagementPanel(); 
        this.contentPanel.add(doctorManagementPanel, "doctorManagement_panel");
                this.wardManagementPanel = new WardManagement();
        this.contentPanel.add(wardManagementPanel, "ward_panel");
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
        staffDashboardBtn = new javax.swing.JButton();
        staffPatientManageBtn = new javax.swing.JButton();
        appointmentBtn = new javax.swing.JButton();
        roomBtn = new javax.swing.JButton();
        logOutBtn = new javax.swing.JButton();
        appoinmentRoomBtn = new javax.swing.JButton();
        doctorBtn = new javax.swing.JButton();
        wardBtn = new javax.swing.JButton();
        headerPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Medi Sewana | Reception Dashboard");

        menuPanel.setBackground(new java.awt.Color(3, 4, 94));

        staffDashboardBtn.setBackground(new java.awt.Color(144, 224, 239));
        staffDashboardBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        staffDashboardBtn.setForeground(new java.awt.Color(3, 4, 94));
        staffDashboardBtn.setText(" Dashboard");
        staffDashboardBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        staffDashboardBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffDashboardBtnActionPerformed(evt);
            }
        });

        staffPatientManageBtn.setBackground(new java.awt.Color(144, 224, 239));
        staffPatientManageBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        staffPatientManageBtn.setForeground(new java.awt.Color(3, 4, 94));
        staffPatientManageBtn.setText(" Patient Management");
        staffPatientManageBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        staffPatientManageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffPatientManageBtnActionPerformed(evt);
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

        logOutBtn.setBackground(new java.awt.Color(144, 224, 239));
        logOutBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        logOutBtn.setForeground(new java.awt.Color(3, 4, 94));
        logOutBtn.setText("Log Out");

        appoinmentRoomBtn.setBackground(new java.awt.Color(144, 224, 239));
        appoinmentRoomBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        appoinmentRoomBtn.setForeground(new java.awt.Color(3, 4, 94));
        appoinmentRoomBtn.setText(" Appointment Room");
        appoinmentRoomBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        appoinmentRoomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appoinmentRoomBtnActionPerformed(evt);
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

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(staffDashboardBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(staffPatientManageBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(appointmentBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roomBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logOutBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(appoinmentRoomBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(doctorBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(wardBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(staffDashboardBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(staffPatientManageBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(appointmentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(doctorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(wardBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roomBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(appoinmentRoomBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(logOutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        headerPanel.setBackground(new java.awt.Color(3, 4, 94));

        jLabel1.setFont(new java.awt.Font("Nunito ExtraBold", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(202, 240, 248));
        jLabel1.setText("Receptionist Dashboard");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)))
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

    private void staffDashboardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffDashboardBtnActionPerformed
        // TODO add your handling code here:
          this.contentPanelLayout.show(contentPanel, "dashboard_panel");
    }//GEN-LAST:event_staffDashboardBtnActionPerformed

    private void staffPatientManageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffPatientManageBtnActionPerformed
        // TODO add your handling code here:
          this.contentPanelLayout.show(contentPanel, "patientManagement_panel");
    }//GEN-LAST:event_staffPatientManageBtnActionPerformed

    private void appointmentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appointmentBtnActionPerformed
        // TODO add your handling code here:
        this.contentPanelLayout.show(contentPanel, "appointmentManagement_panel");
    }//GEN-LAST:event_appointmentBtnActionPerformed

    private void appoinmentRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appoinmentRoomBtnActionPerformed
        // TODO add your handling code here:
        this.contentPanelLayout.show(contentPanel, "appointmentRoomManagement_panel");
    }//GEN-LAST:event_appoinmentRoomBtnActionPerformed

    private void roomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomBtnActionPerformed
        // TODO add your handling code here:
        this.contentPanelLayout.show(contentPanel, "wardNroomManagement_panel");
        
    }//GEN-LAST:event_roomBtnActionPerformed

    private void doctorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doctorBtnActionPerformed
        // TODO add your handling code here:   
          this.contentPanelLayout.show(contentPanel, "doctorManagement_panel");
    }//GEN-LAST:event_doctorBtnActionPerformed

    private void wardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wardBtnActionPerformed
        // TODO add your handling code here:
                this.contentPanelLayout.show(contentPanel, "ward_panel");
    }//GEN-LAST:event_wardBtnActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReceptionDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton appoinmentRoomBtn;
    private javax.swing.JButton appointmentBtn;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton doctorBtn;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logOutBtn;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JButton roomBtn;
    private javax.swing.JButton staffDashboardBtn;
    private javax.swing.JButton staffPatientManageBtn;
    private javax.swing.JButton wardBtn;
    // End of variables declaration//GEN-END:variables
}
