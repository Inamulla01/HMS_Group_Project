
package lk.avinam.dialog;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.sql.SQLException;
import lk.avinam.connection.MySQL;
import raven.toast.Notifications;
/**
 *
 * @author moham
 */
public class AddSchedule extends javax.swing.JDialog {

    private int doctorId;

    public AddSchedule(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
//        init();

    }

//    public AddSchedule(java.awt.Frame parent, boolean modal, int doctorId) {
//        super(parent, modal);
//        this.doctorId = doctorId;
//        initComponents();
//        init();
//    }
//
//    private void init() {
//        FlatSVGIcon addIcon = new FlatSVGIcon("lk/avinam/icon/plus.svg", 15, 15);
//        addIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#CAF0F8")));
//        addBtn.setIcon(addIcon);
//        FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/cancel.svg", 15, 15);
//        cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
//        cancelBtn.setIcon(cancelIcon);
//        availabilityDate.setMinSelectableDate(new java.util.Date());
//    }
//
//    private void saveSchedule() {
//        try {
//            java.util.Date selectedDate = availabilityDate.getDate();
//            if (selectedDate == null) {
//                Notifications.getInstance().show(Notifications.Type.ERROR, "Please select a date");
//                return;
//            }
//
//            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
//            LocalTime fromTime = availabilityFrom.getTime();
//            LocalTime toTime = availabilityTo.getTime();
//
//            if (fromTime == null || toTime == null) {
//                Notifications.getInstance().show(Notifications.Type.ERROR, "Please select both start and end times");
//                return;
//            }
//
//            if (fromTime.isAfter(toTime)) {
//                Notifications.getInstance().show(Notifications.Type.ERROR, "End time must be after start time");
//                return;
//            }
//
//            // Validate price
//            double priceValue;
//            try {
//                priceValue = Double.parseDouble(price.getText().trim());
//                if (priceValue <= 0) {
//                    Notifications.getInstance().show(Notifications.Type.ERROR, "Price must be greater than 0");
//                    return;
//                }
//            } catch (NumberFormatException e) {
//                Notifications.getInstance().show(Notifications.Type.ERROR, "Please enter a valid price");
//                return;
//            }
//
//            // Step 1: First add to availability_schedule_time table
//            int timeId = addToAvailabilityTime(fromTime, toTime);
//            if (timeId == -1) {
//                Notifications.getInstance().show(Notifications.Type.ERROR, "Failed to add time to availability schedule");
//                return;
//            }
//
//            // Step 2: Then add to availability_schedule_date table
//            int dateId = addToAvailabilityDate(sqlDate, timeId);
//            if (dateId == -1) {
//                Notifications.getInstance().show(Notifications.Type.ERROR, "Failed to add date to availability schedule");
//                return;
//            }
//
//            // Step 3: Finally link everything in schedule_date_has_doctor table
//            boolean success = linkScheduleDateHasDoctor(dateId, priceValue);
//
//            if (success) {
//                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Schedule added successfully!");
//                this.dispose();
//            } else {
//                Notifications.getInstance().show(Notifications.Type.ERROR, "Failed to link schedule with doctor");
//            }
//
//        } catch (Exception e) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, "Error saving schedule: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private int addToAvailabilityTime(LocalTime fromTime, LocalTime toTime) {
//        try {
//            // First check if this time slot already exists
//            String checkQuery = "SELECT availability_time_id FROM availability_schedule_time "
//                    + "WHERE availability_time_from = '" + fromTime + "' "
//                    + "AND availability_time_to = '" + toTime + "'";
//            ResultSet rs = MySQL.executeSearch(checkQuery);
//
//            if (rs.next()) {
//                // Time slot already exists, return the existing ID
//                return rs.getInt("availability_time_id");
//            } else {
//                // Insert new time slot
//                String insertQuery = "INSERT INTO availability_schedule_time (availability_time_from, availability_time_to) "
//                        + "VALUES ('" + fromTime + "', '" + toTime + "')";
//                int rowsAffected = MySQL.executeIUD(insertQuery);
//
//                if (rowsAffected > 0) {
//                    ResultSet generatedKeys = MySQL.executeSearch("SELECT LAST_INSERT_ID()");
//                    if (generatedKeys.next()) {
//                        return generatedKeys.getInt(1);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    private int addToAvailabilityDate(java.sql.Date sqlDate, int timeId) {
//        try {
//            // First check if this date with the same time already exists
//            String checkQuery = "SELECT ad.availability_date_id FROM availability_schedule_date ad "
//                    + "JOIN availability_schedule_time at ON ad.availability_time_id = at.availability_time_id "
//                    + "WHERE ad.availability_date = '" + sqlDate + "' "
//                    + "AND at.availability_time_id = " + timeId;
//            ResultSet rs = MySQL.executeSearch(checkQuery);
//
//            if (rs.next()) {
//                // Date with same time already exists, return the existing ID
//                return rs.getInt("availability_date_id");
//            } else {
//                // Insert new date with time reference
//                String insertQuery = "INSERT INTO availability_schedule_date (availability_date, availability_time_id) "
//                        + "VALUES ('" + sqlDate + "', " + timeId + ")";
//                int rowsAffected = MySQL.executeIUD(insertQuery);
//
//                if (rowsAffected > 0) {
//                    ResultSet generatedKeys = MySQL.executeSearch("SELECT LAST_INSERT_ID()");
//                    if (generatedKeys.next()) {
//                        return generatedKeys.getInt(1);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    private boolean linkScheduleDateHasDoctor(int dateId, double priceValue) {
//        try {
//            // Check if this combination already exists
//            String checkQuery = "SELECT schedule_date_has_doctor_id FROM schedule_date_has_doctor "
//                    + "WHERE schedule_date_id = " + dateId
//                    + " AND doctor_id = " + doctorId;
//            ResultSet rs = MySQL.executeSearch(checkQuery);
//
//            if (rs.next()) {
//                // Combination already exists, update it
//                String updateQuery = "UPDATE schedule_date_has_doctor SET "
//                        + "price = " + priceValue + " "
//                        + "WHERE schedule_date_id = " + dateId
//                        + " AND doctor_id = " + doctorId;
//                return MySQL.executeIUD(updateQuery) > 0;
//            } else {
//                // Insert new combination
//                String insertQuery = "INSERT INTO schedule_date_has_doctor "
//                        + "(schedule_date_id, doctor_id, price) "
//                        + "VALUES (" + dateId + ", " + doctorId + ", " + priceValue + ")";
//                return MySQL.executeIUD(insertQuery) > 0;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        availabilityFrom = new com.github.lgooddatepicker.components.TimePicker();
        availabilityTo = new com.github.lgooddatepicker.components.TimePicker();
        addBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        availabilityDate = new com.toedter.calendar.JDayChooser();
        price = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator1.setForeground(new java.awt.Color(3, 4, 94));

        jLabel2.setText("Add New Schedule");
        jLabel2.setFont(new java.awt.Font("Nunito ExtraBold", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(3, 4, 94));

        jLabel1.setText("Available Day");
        jLabel1.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 4, 94));

        availabilityFrom.setBackground(new java.awt.Color(255, 255, 255));
        availabilityFrom.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Availability From", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        availabilityFrom.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N

        availabilityTo.setBackground(new java.awt.Color(255, 255, 255));
        availabilityTo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Availability To", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        availabilityTo.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N

        addBtn.setText("Add");
        addBtn.setBackground(new java.awt.Color(3, 4, 94));
        addBtn.setFocusable(false);
        addBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        addBtn.setForeground(new java.awt.Color(204, 255, 255));
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText("Cancel");
        cancelBtn.setBackground(new java.awt.Color(202, 240, 248));
        cancelBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancelBtn.setFocusable(false);
        cancelBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(3, 4, 94));
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        price.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Price", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        price.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        price.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        price.setForeground(new java.awt.Color(3, 4, 94));
        price.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Nunito ExtraLight", 1, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito ExtraLight", 1, 14), new java.awt.Color(3, 4, 94)), "Room No", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito ExtraLight", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox1, 0, 479, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(availabilityDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel2)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel1)
                        .addComponent(availabilityFrom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                        .addComponent(availabilityTo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(price)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(availabilityDate, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(availabilityFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(availabilityTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void priceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
//        saveSchedule();
    }//GEN-LAST:event_addBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatIntelliJLaf.setup();

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddSchedule dialog = new AddSchedule(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton addBtn;
    private com.toedter.calendar.JDayChooser availabilityDate;
    private com.github.lgooddatepicker.components.TimePicker availabilityFrom;
    private com.github.lgooddatepicker.components.TimePicker availabilityTo;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField price;
    // End of variables declaration//GEN-END:variables
}
