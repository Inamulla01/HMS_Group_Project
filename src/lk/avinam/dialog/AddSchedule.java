package lk.avinam.dialog;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
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
        init();
    }

    public AddSchedule(java.awt.Frame parent, boolean modal, int doctorId) {
        super(parent, modal);
        this.doctorId = doctorId;
        initComponents();
        init();
        populateRoomComboBox();
    }

    private void init() {
        FlatSVGIcon addIcon = new FlatSVGIcon("lk/avinam/icon/plus.svg", 15, 15);
        addIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#CAF0F8")));
        addBtn.setIcon(addIcon);

        FlatSVGIcon cancelIcon = new FlatSVGIcon("lk/avinam/icon/cancel.svg", 15, 15);
        cancelIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#03045E")));
        cancelBtn.setIcon(cancelIcon);

        availabilityDay.setMinSelectableDate(new java.util.Date());

        // Add listener to refresh rooms when date changes
        availabilityDay.addPropertyChangeListener("date", evt -> {
            populateRoomComboBox();
        });

        // Add listener to refresh rooms when time changes
        availabilityFrom.addTimeChangeListener(e -> {
            populateRoomComboBox();
        });

        availabilityTo.addTimeChangeListener(e -> {
            populateRoomComboBox();
        });
    }

    private void populateRoomComboBox() {
        try {
            java.util.Date selectedDate = availabilityDay.getDate();
            LocalTime fromTime = availabilityFrom.getTime();
            LocalTime toTime = availabilityTo.getTime();

            Vector<String> rooms = new Vector<>();
            rooms.add("Select Room");

            if (selectedDate == null || fromTime == null || toTime == null) {
                // If no date or time selected, show all active rooms
                ResultSet rs = MySQL.executeSearch("SELECT appointment_room_no FROM appointment_room WHERE status_s_id = 1");
                while (rs.next()) {
                    String roomNo = rs.getString("appointment_room_no");
                    rooms.add(roomNo);
                }
            } else {
                // If date and time selected, show only available rooms for that date and time
                java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

                // Get all active rooms
                ResultSet allRooms = MySQL.executeSearch("SELECT ar.appointment_room_id, ar.appointment_room_no FROM appointment_room ar WHERE ar.status_s_id = 1");

                while (allRooms.next()) {
                    int roomId = allRooms.getInt("appointment_room_id");
                    String roomNo = allRooms.getString("appointment_room_no");

                    // Check if room is available for the selected date and time
                    if (isRoomAvailableForDateTime(roomId, sqlDate, fromTime, toTime)) {
                        rooms.add(roomNo);
                    }
                }
            }

            DefaultComboBoxModel<String> dcm = new DefaultComboBoxModel<>(rooms);
            jComboBox1.setModel(dcm);

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Error loading rooms: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isRoomAvailableForDateTime(int roomId, java.sql.Date date, LocalTime fromTime, LocalTime toTime) throws SQLException {
        // Check if the room is already booked and not cancelled for the same date and time
        String query = "SELECT dht.date_has_time_id "
                + "FROM date_has_time dht "
                + "JOIN availability_schedule_date asd ON dht.availability_date_id = asd.availability_date_id "
                + "JOIN availability_schedule_time ast ON dht.availability_time_id = ast.availability_time_id "
                + "LEFT JOIN room_reservations rr ON dht.room_reservations_id = rr.room_reservations_id "
                + "WHERE dht.appointment_room_id = " + roomId + " "
                + "AND asd.availability_date = '" + date + "' "
                + "AND ast.availability_time_from = '" + fromTime + "' "
                + "AND ast.availability_time_to = '" + toTime + "' "
                + "AND (rr.status IS NULL OR rr.status != 'Booked')";

        ResultSet rs = MySQL.executeSearch(query);
        return !rs.next(); // Room is available if no active booking found
    }

    private int getRoomId(String roomNo) throws SQLException {
        ResultSet rs = MySQL.executeSearch("SELECT appointment_room_id FROM appointment_room WHERE appointment_room_no = '" + roomNo + "'");
        if (rs.next()) {
            return rs.getInt("appointment_room_id");
        }
        return -1;
    }

    private int getOrCreateDateId(java.sql.Date date) throws SQLException {
        ResultSet rs = MySQL.executeSearch("SELECT availability_date_id FROM availability_schedule_date WHERE availability_date = '" + date + "'");
        if (rs.next()) {
            return rs.getInt("availability_date_id");
        }

        String insertQuery = "INSERT INTO availability_schedule_date (availability_date) VALUES ('" + date + "')";
        MySQL.executeIUD(insertQuery);

        ResultSet newRs = MySQL.executeSearch("SELECT LAST_INSERT_ID() as date_id");
        if (newRs.next()) {
            return newRs.getInt("date_id");
        }
        return -1;
    }

    private int getOrCreateTimeId(LocalTime fromTime, LocalTime toTime) throws SQLException {
        ResultSet rs = MySQL.executeSearch("SELECT availability_time_id FROM availability_schedule_time WHERE availability_time_from = '" + fromTime + "' AND availability_time_to = '" + toTime + "'");
        if (rs.next()) {
            return rs.getInt("availability_time_id");
        }

        String insertQuery = "INSERT INTO availability_schedule_time (availability_time_from, availability_time_to) VALUES ('" + fromTime + "', '" + toTime + "')";
        MySQL.executeIUD(insertQuery);

        ResultSet newRs = MySQL.executeSearch("SELECT LAST_INSERT_ID() as time_id");
        if (newRs.next()) {
            return newRs.getInt("time_id");
        }
        return -1;
    }

    private boolean isScheduleExists(int dateId, int timeId, int doctorId) throws SQLException {
        ResultSet rs = MySQL.executeSearch("SELECT * FROM date_has_time WHERE availability_date_id = " + dateId + " AND availability_time_id = " + timeId + " AND doctor_id = " + doctorId);
        return rs.next();
    }

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
        price = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        availabilityDay = new com.toedter.calendar.JCalendar();

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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito ExtraLight", 1, 14), new java.awt.Color(3, 4, 94)), "Room No", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito ExtraLight", 1, 14), new java.awt.Color(3, 4, 94))); // NOI18N
        jComboBox1.setFont(new java.awt.Font("Nunito ExtraLight", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(availabilityDay, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jComboBox1, 0, 479, Short.MAX_VALUE)
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
                .addComponent(availabilityDay, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        try {
            java.util.Date selectedDate = availabilityDay.getDate();
            if (selectedDate == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Please select a date");
                return;
            }

            if (availabilityFrom.getTime() == null || availabilityTo.getTime() == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Please select both start and end time");
                return;
            }

            if (price.getText().trim().isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Please enter a price");
                return;
            }

            if (jComboBox1.getSelectedItem() == null || "Select Room".equals(jComboBox1.getSelectedItem())) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Please select a room");
                return;
            }

            LocalTime fromTime = availabilityFrom.getTime();
            LocalTime toTime = availabilityTo.getTime();

            if (fromTime.isAfter(toTime) || fromTime.equals(toTime)) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "End time must be after start time");
                return;
            }

            double priceValue;
            try {
                priceValue = Double.parseDouble(price.getText().trim());
                if (priceValue <= 0) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Price must be greater than 0");
                    return;
                }
            } catch (NumberFormatException e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Please enter a valid price");
                e.printStackTrace();
                return;
            }

            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
            String roomNo = jComboBox1.getSelectedItem().toString();

            int roomId = getRoomId(roomNo);
            if (roomId == -1) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Invalid room selected");
                return;
            }

            int dateId = getOrCreateDateId(sqlDate);
            if (dateId == -1) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Failed to process date");
                return;
            }

            int timeId = getOrCreateTimeId(fromTime, toTime);
            if (timeId == -1) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Failed to process time");
                return;
            }

            // Check if schedule already exists for this doctor
            if (isScheduleExists(dateId, timeId, doctorId)) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "This schedule already exists for this doctor");
                return;
            }

            // Final check if room is available for the specific date and time
            if (!isRoomAvailableForDateTime(roomId, sqlDate, fromTime, toTime)) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Room is already booked for the selected date and time");
                return;
            }

            // Insert the schedule with room_reservations_id as 1
            MySQL.executeIUD("INSERT INTO date_has_time (availability_date_id, availability_time_id, price, doctor_id, appointment_room_id, room_reservations_id) VALUES ("
                    + dateId + ", " + timeId + ", " + priceValue + ", " + doctorId + ", " + roomId + ", 1)");
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Schedule added successfully!");
            dispose();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

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
    private com.toedter.calendar.JCalendar availabilityDay;
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
