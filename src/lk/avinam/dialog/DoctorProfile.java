package lk.avinam.dialog;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import lk.avinam.connection.MySQL;
import lk.avinam.panel.DoctorManagementPanel;
import raven.toast.Notifications;

public class DoctorProfile extends javax.swing.JDialog {

    private int doctorId;
    private String slmcIdValue;
    private DoctorManagementPanel managementPanel;
    private boolean isOutpatient;

    public DoctorProfile(java.awt.Frame parent, boolean modal, ResultSet doctorData) {
        super(parent, modal);
        initComponents();
        init();
        populateFields(doctorData);
        checkDoctorType();
        updatePanelVisibility();
    }

    public DoctorProfile(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    private void init() {
        availableBtn.setIcon(new FlatSVGIcon("lk/avinam/icon/plus.svg", 15, 15));
        FlatSVGIcon addIcon = new FlatSVGIcon("lk/avinam/icon/edit.svg", 20, 20);
        addIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#CAF0F8")));
        editBtn.setIcon(addIcon);
        FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/cancel.svg", 15, 15);
        cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        cancelBtn.setIcon(cancelIcon);
        FlatSVGIcon addIcon1 = new FlatSVGIcon("lk/avinam/icon/delete.svg", 20, 20);
        addIcon1.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.RED));
        deleteBtn.setIcon(addIcon1);
        availableBtn1.setIcon(new FlatSVGIcon("lk/avinam/icon/plus.svg", 15, 15));

        FlatSVGIcon addIcon2 = new FlatSVGIcon("lk/avinam/icon/delete.svg", 20, 20);
        addIcon2.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.RED));
        deleteBtn1.setIcon(addIcon2);
    }

    private void loadShiftSchedules() {
        try {
            jPanel3.removeAll();
            jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.Y_AXIS));

            String query = "SELECT * FROM `doctor_shift_view`" +
                         "WHERE doctor_id = " + doctorId + " ";

            ResultSet rs = MySQL.executeSearch(query);

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                int shiftId = rs.getInt("Doctor_shift_schedule_id");
                String date = rs.getString("date");
                String ward = rs.getString("ward_type");
                String shift = rs.getString("shift_type");

                // Create main card panel
                JPanel card = new JPanel();
                card.setLayout(new BorderLayout(10, 5));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                // Create content panel
                JPanel contentPanel = new JPanel();
                contentPanel.setLayout(new GridLayout(2, 1, 5, 5));
                contentPanel.setBackground(Color.WHITE);
                contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

                // Day label
                JLabel dayLabel = new JLabel("Available Day: " + date);
                dayLabel.setFont(new Font("Nunito SemiBold", Font.BOLD, 14));
                dayLabel.setForeground(new Color(3, 4, 94));

                // Shift label
                JLabel shiftLabel = new JLabel("Available Shift: " + shift + " - " + ward);
                shiftLabel.setFont(new Font("Nunito SemiBold", Font.PLAIN, 12));
                shiftLabel.setForeground(Color.DARK_GRAY);

                contentPanel.add(dayLabel);
                contentPanel.add(shiftLabel);

                // Delete button
                JButton deleteBtn = new JButton("Delete");
                deleteBtn.setFont(new Font("Nunito ExtraBold", Font.BOLD, 12));
                deleteBtn.setForeground(Color.WHITE);
                deleteBtn.setBackground(Color.RED);
                deleteBtn.setFocusPainted(false);
                deleteBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                deleteBtn.setPreferredSize(new Dimension(100, 35));

                deleteBtn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to delete this shift?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            String deleteQuery = "DELETE FROM doctor_shift_schedule WHERE Doctor_shift_schedule_id=" + shiftId;
                            MySQL.executeIUD(deleteQuery);
                            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, "Shift deleted!");
                            loadShiftSchedules();
                        } catch (Exception ex) {
                            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Error deleting shift: " + ex.getMessage());
                        }
                    }
                });

                // Add components to card
                card.add(contentPanel, BorderLayout.CENTER);
                card.add(deleteBtn, BorderLayout.EAST);

                jPanel3.add(card);
                jPanel3.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between cards
            }

            // Add empty state message if no data
            if (!hasData) {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setLayout(new BorderLayout());
                emptyPanel.setBackground(Color.WHITE);
                emptyPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

                JLabel emptyLabel = new JLabel("No shift schedules available", JLabel.CENTER);
                emptyLabel.setFont(new Font("Nunito SemiBold", Font.ITALIC, 14));
                emptyLabel.setForeground(Color.GRAY);

                emptyPanel.add(emptyLabel, BorderLayout.CENTER);
                jPanel3.add(emptyPanel);
            }

            jPanel3.revalidate();
            jPanel3.repaint();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Error loading shifts: " + e.getMessage());
        }
    }

    private void loadOutpatientSchedules() {
        try {
            schedulePanel.removeAll();
            schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));

            String query = "SELECT * FROM `doctor_schedule_view` " +
                         "WHERE `doctor_id` = " + doctorId + " ";

            ResultSet rs = MySQL.executeSearch(query);

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                int scheduleId = rs.getInt("date_has_time_id");
                String date = rs.getString("availability_date");
                String from = rs.getString("availability_time_from");
                String to = rs.getString("availability_time_to");

                // Create main card panel
                JPanel card = new JPanel();
                card.setLayout(new BorderLayout(10, 5));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                // Create content panel
                JPanel contentPanel = new JPanel();
                contentPanel.setLayout(new GridLayout(2, 1, 5, 5));
                contentPanel.setBackground(Color.WHITE);
                contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

                // Day label
                JLabel dayLabel = new JLabel("Available Day: " + date);
                dayLabel.setFont(new Font("Nunito SemiBold", Font.BOLD, 14));
                dayLabel.setForeground(new Color(3, 4, 94));

                // Time label
                JLabel timeLabel = new JLabel("Available Time From: " + from + " To: " + to);
                timeLabel.setFont(new Font("Nunito SemiBold", Font.PLAIN, 12));
                timeLabel.setForeground(Color.DARK_GRAY);

                contentPanel.add(dayLabel);
                contentPanel.add(timeLabel);

                // Delete button
                JButton deleteBtn = new JButton("Delete");
                deleteBtn.setFont(new Font("Nunito ExtraBold", Font.BOLD, 12));
                deleteBtn.setForeground(Color.WHITE);
                deleteBtn.setBackground(Color.RED);
                deleteBtn.setFocusPainted(false);
                deleteBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                deleteBtn.setPreferredSize(new Dimension(100, 35));

                deleteBtn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to delete this schedule?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            String deleteQuery = "DELETE FROM date_has_time WHERE date_has_time_id=" + scheduleId;
                            MySQL.executeIUD(deleteQuery);
                            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, "Schedule deleted!");
                            loadOutpatientSchedules();
                        } catch (Exception ex) {
                            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Error deleting schedule: " + ex.getMessage());
                        }
                    }
                });

                // Add components to card
                card.add(contentPanel, BorderLayout.CENTER);
                card.add(deleteBtn, BorderLayout.EAST);

                schedulePanel.add(card);
                schedulePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between cards
            }

            // Add empty state message if no data
            if (!hasData) {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setLayout(new BorderLayout());
                emptyPanel.setBackground(Color.WHITE);
                emptyPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

                JLabel emptyLabel = new JLabel("No outpatient schedules available", JLabel.CENTER);
                emptyLabel.setFont(new Font("Nunito SemiBold", Font.ITALIC, 14));
                emptyLabel.setForeground(Color.GRAY);

                emptyPanel.add(emptyLabel, BorderLayout.CENTER);
                schedulePanel.add(emptyPanel);
            }

            schedulePanel.revalidate();
            schedulePanel.repaint();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Error loading schedules: " + e.getMessage());
        }
    }

    public void setManagementPanel(DoctorManagementPanel managementPanel) {
        this.managementPanel = managementPanel;
    }

    private void populateFields(ResultSet doctorData) {
        try {
            if (doctorData != null) {
                slmcIdValue = doctorData.getString("slmc_id");
                doctorId = doctorData.getInt("doctor_id");
                password.setText(doctorData.getString("password"));
                slmcId.setText(slmcIdValue);
                firstName.setText(doctorData.getString("f_name"));
                lastName.setText(doctorData.getString("l_name"));
                email.setText(doctorData.getString("email"));
                contact.setText(doctorData.getString("mobile"));
                joinAt.setText(doctorData.getString("join_at"));
                qualification.setText(doctorData.getString("qualification"));
                spatialisedIn.setText(doctorData.getString("specialization"));
            }
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Error loading doctor data: " + e.getMessage());
        }
    }

    private void updateDoctor() {
        try {
            String firstName = this.firstName.getText().trim();
            String lastName = this.lastName.getText().trim();
            String email = this.email.getText().trim();
            String contact = this.contact.getText().trim();
            String qualification = this.qualification.getText().trim();
            String specialization = this.spatialisedIn.getText().trim();

            if (!lk.avinam.validation.Validater.isInputFieldValid(firstName)) {
                return;
            }
            if (!lk.avinam.validation.Validater.isInputFieldValid(lastName)) {
                return;
            }
            if (!lk.avinam.validation.Validater.isEmailValid(email)) {
                return;
            }
            if (!lk.avinam.validation.Validater.isMobileValid(contact)) {
                return;
            }
            if (!lk.avinam.validation.Validater.isInputFieldValid(qualification)) {
                return;
            }
            if (!lk.avinam.validation.Validater.isInputFieldValid(specialization)) {
                return;
            }

            String updateDoctorQuery = "UPDATE doctor SET f_name = '" + firstName
                    + "', l_name = '" + lastName
                    + "', email = '" + email
                    + "', mobile = '" + contact
                    + "', specialization = '" + specialization
                    + "', qualification = '" + qualification
                    + "' WHERE slmc_id = '" + slmcIdValue + "'";

            MySQL.executeIUD(updateDoctorQuery);

            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, "Doctor updated successfully!");

            if (managementPanel != null) {
                managementPanel.disableUpdateButton();
            }

            this.dispose();

        } catch (Exception e) {

            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Error updating doctor: " + e.getMessage());
        }
    }

    private void checkDoctorType() {
        try {
            String query = "SELECT dt.doctor_type FROM doctor d "
                    + "JOIN doctor_type dt ON d.doctor_type_id = dt.doctor_type_id "
                    + "WHERE d.doctor_id = " + doctorId;
            ResultSet rs = MySQL.executeSearch(query);
            if (rs.next()) {
                String doctorType = rs.getString("doctor_type");
                isOutpatient = "Out Patient Doctor".equalsIgnoreCase(doctorType);
            }
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT,
                    "Error checking doctor type: " + e.getMessage());
            isOutpatient = true;
        }
    }

    private void updatePanelVisibility() {
        if (isOutpatient) {

            schedulePanel.setVisible(true);
            jPanel3.setVisible(false);
            availableBtn.setVisible(true);
            availableBtn1.setVisible(false);
            scheduleText.setVisible(true);
            jLabel18.setVisible(false);
            loadOutpatientSchedules();

        } else {

            schedulePanel.setVisible(false);
            jPanel3.setVisible(true);
            availableBtn.setVisible(false);
            availableBtn1.setVisible(true);
            scheduleText.setVisible(false);
            jLabel18.setVisible(true);
            loadShiftSchedules();

        }
    }

    private void availableBtnSwitch() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (isOutpatient) {
            AddSchedule dialog = new AddSchedule(parentFrame, true, doctorId);
            dialog.setLocationRelativeTo(parentFrame);
            dialog.setVisible(true);
            dialog.dispose();
            loadOutpatientSchedules();
        } else {
            DoctorShift dialog = new DoctorShift(parentFrame, true, doctorId);
            dialog.setLocationRelativeTo(parentFrame);
            dialog.setVisible(true);
            dialog.dispose();
            loadShiftSchedules();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        availableBtn = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        slmcId = new javax.swing.JTextField();
        schedulePanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        deleteBtn = new javax.swing.JButton();
        availableBtn1 = new javax.swing.JButton();
        firstName = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        qualification = new javax.swing.JTextField();
        contact = new javax.swing.JTextField();
        joinAt = new javax.swing.JTextField();
        lastName = new javax.swing.JTextField();
        cancelBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        scheduleText = new javax.swing.JLabel();
        spatialisedIn = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        deleteBtn1 = new javax.swing.JButton();
        password = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(782, 870));
        jScrollPane1.setRequestFocusEnabled(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Nunito ExtraBold", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(3, 4, 94));
        jLabel3.setText("Doctor Profile");

        availableBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        availableBtn.setText("Add New Schedule");
        availableBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        availableBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        availableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                availableBtnActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Nunito ExtraBold", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(3, 4, 94));
        jLabel18.setText("Doctor Shift Details");

        slmcId.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        slmcId.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SLMC ID", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        slmcId.setEnabled(false);

        jLabel12.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jLabel12.setText("Available Day : ");

        jLabel13.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jLabel13.setText("Available Time From : ");

        jLabel14.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jLabel14.setText(" To  ");

        deleteBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        deleteBtn.setForeground(new java.awt.Color(255, 0, 0));
        deleteBtn.setText("Delete");
        deleteBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
        deleteBtn.setFocusable(false);
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout schedulePanelLayout = new javax.swing.GroupLayout(schedulePanel);
        schedulePanel.setLayout(schedulePanelLayout);
        schedulePanelLayout.setHorizontalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(schedulePanelLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14))
                    .addGroup(schedulePanelLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        schedulePanelLayout.setVerticalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        availableBtn1.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        availableBtn1.setText("Add New Shift");
        availableBtn1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        availableBtn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        availableBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                availableBtn1ActionPerformed(evt);
            }
        });

        firstName.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        firstName.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "First Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        firstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstNameActionPerformed(evt);
            }
        });

        email.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        email.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Email", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        email.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });

        qualification.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        qualification.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Qualification", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        qualification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qualificationActionPerformed(evt);
            }
        });

        contact.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        contact.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Contact No", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        contact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactActionPerformed(evt);
            }
        });

        joinAt.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        joinAt.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Join At", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        joinAt.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        joinAt.setEnabled(false);
        joinAt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinAtActionPerformed(evt);
            }
        });

        lastName.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        lastName.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Last Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        lastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastNameActionPerformed(evt);
            }
        });

        cancelBtn.setBackground(new java.awt.Color(202, 240, 248));
        cancelBtn.setFont(new java.awt.Font("Nunito SemiBold", 0, 16)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(3, 3, 94));
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        editBtn.setBackground(new java.awt.Color(3, 3, 94));
        editBtn.setFont(new java.awt.Font("Nunito SemiBold", 0, 16)); // NOI18N
        editBtn.setForeground(new java.awt.Color(202, 240, 248));
        editBtn.setText("Update");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        scheduleText.setFont(new java.awt.Font("Nunito ExtraBold", 1, 16)); // NOI18N
        scheduleText.setForeground(new java.awt.Color(3, 4, 94));
        scheduleText.setText("Doctor Schedule Details");

        spatialisedIn.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        spatialisedIn.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Spatialised In", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        spatialisedIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spatialisedInActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jLabel15.setText("Available Day : ");

        jLabel16.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jLabel16.setText("Available Shift : ");

        deleteBtn1.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        deleteBtn1.setForeground(new java.awt.Color(255, 0, 0));
        deleteBtn1.setText("Delete");
        deleteBtn1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
        deleteBtn1.setFocusable(false);
        deleteBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15)
                    .addComponent(deleteBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        password.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        password.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Password", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        password.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        password.setEnabled(false);
        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(3, 4, 94));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(availableBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(email, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1)
                            .addComponent(schedulePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(slmcId, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(scheduleText)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(availableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(firstName, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(joinAt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                                    .addComponent(spatialisedIn, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(qualification)
                                    .addComponent(lastName)
                                    .addComponent(contact))))))
                .addGap(34, 34, 34))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(slmcId, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(joinAt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contact))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spatialisedIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qualification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(availableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scheduleText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(schedulePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(availableBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void availableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availableBtnActionPerformed
        availableBtnSwitch();
    }//GEN-LAST:event_availableBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void availableBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availableBtn1ActionPerformed
        availableBtnSwitch();
    }//GEN-LAST:event_availableBtn1ActionPerformed

    private void firstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNameActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void qualificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qualificationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qualificationActionPerformed

    private void contactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactActionPerformed

    private void joinAtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinAtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_joinAtActionPerformed

    private void lastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastNameActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        updateDoctor();
    }//GEN-LAST:event_editBtnActionPerformed

    private void spatialisedInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spatialisedInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_spatialisedInActionPerformed

    private void deleteBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteBtn1ActionPerformed

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DoctorProfile dialog = new DoctorProfile(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton availableBtn;
    private javax.swing.JButton availableBtn1;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTextField contact;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton deleteBtn1;
    private javax.swing.JButton editBtn;
    private javax.swing.JTextField email;
    private javax.swing.JTextField firstName;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField joinAt;
    private javax.swing.JTextField lastName;
    private javax.swing.JTextField password;
    private javax.swing.JTextField qualification;
    private javax.swing.JPanel schedulePanel;
    private javax.swing.JLabel scheduleText;
    private javax.swing.JTextField slmcId;
    private javax.swing.JTextField spatialisedIn;
    // End of variables declaration//GEN-END:variables
}
