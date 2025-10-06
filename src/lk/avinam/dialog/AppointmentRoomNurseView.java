/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package lk.avinam.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import lk.avinam.connection.MySQL;

/**
 *
 * @author pasin
 */
public class AppointmentRoomNurseView extends javax.swing.JDialog {

    private String appointmentRoomNo;
    
    public AppointmentRoomNurseView(java.awt.Frame parent, boolean modal, String AppointmentRoomNo) {
        super(parent, modal);
        this.appointmentRoomNo = AppointmentRoomNo;
        initComponents();
        init();
        roomNoLabel.setText("Appointment Room : " + AppointmentRoomNo);
        loadAppointmentRoomNurse();
        nurseviewBtn.setVisible(false);
        
    }
    
    private void init(){
        arNurseTable.getTableHeader().setFont(new Font("", Font.BOLD, 16));
        arNurseTable.getTableHeader().setOpaque(false);
        arNurseTable.getTableHeader().setBackground(Color.decode("#00B4D8"));
        arNurseTable.getTableHeader().setForeground(Color.decode("#CAF0F8"));
        arNurseTable.getTableHeader().setPreferredSize(new Dimension(0, 30));
        
        arNurseTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = arNurseTable.getSelectedRow();
                if (selectedRow == -1) { // nothing selected
                    nurseviewBtn.setVisible(false);
                }
            }
        });

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                arNurseTable.clearSelection();
            }
        });

    }
    

    private void loadAppointmentRoomNurse(){
        try{
            ResultSet rs = MySQL.executeSearch("SELECT nurse.nurse_no, CONCAT(nurse.f_name, ' ', nurse.l_name) AS nurse_name FROM appointment_room\n" +
"JOIN nurse_has_appointment_room ON nurse_has_appointment_room.appointment_room_id = appointment_room.appointment_room_id\n" +
"JOIN nurse ON nurse.nurse_id = nurse_has_appointment_room.nurse_id WHERE appointment_room.appointment_room_no = '"+appointmentRoomNo+"';");
            DefaultTableModel dtm = (DefaultTableModel) arNurseTable.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("nurse_no"));
                v.add(rs.getString("nurse_name"));
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

            arNurseTable.setDefaultRenderer(Object.class, renderer);
            
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        arNurseTable = new javax.swing.JTable();
        roomNoLabel = new javax.swing.JLabel();
        nurseviewBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Appointment Room – Nurse Details");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Nunito ExtraBold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 4, 94));
        jLabel1.setText("Appointment Room – Nurse Details");

        jSeparator1.setForeground(new java.awt.Color(3, 4, 94));

        arNurseTable.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        arNurseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nurse ID", "Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        arNurseTable.setRowHeight(30);
        arNurseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                arNurseTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(arNurseTable);

        roomNoLabel.setFont(new java.awt.Font("Nunito SemiBold", 1, 16)); // NOI18N
        roomNoLabel.setForeground(new java.awt.Color(3, 4, 94));
        roomNoLabel.setText("Appointment Room : ");

        nurseviewBtn.setBackground(new java.awt.Color(3, 4, 94));
        nurseviewBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        nurseviewBtn.setForeground(new java.awt.Color(144, 224, 239));
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator1)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(roomNoLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nurseviewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roomNoLabel)
                    .addComponent(nurseviewBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nurseviewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nurseviewBtnActionPerformed
        ViewNurse viewNurse = new ViewNurse(null, true);
        viewNurse.setLocationRelativeTo(null);
        viewNurse.setVisible(true);
    }//GEN-LAST:event_nurseviewBtnActionPerformed

    private void arNurseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_arNurseTableMouseClicked
        if(evt.getClickCount() == 1){
            int row = arNurseTable.getSelectedRow();
            nurseviewBtn.setVisible(true);
        }
    }//GEN-LAST:event_arNurseTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable arNurseTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton nurseviewBtn;
    private javax.swing.JLabel roomNoLabel;
    // End of variables declaration//GEN-END:variables
}
