/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package lk.avinam.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import lk.avinam.panel.DoctorDashboardPanel;

/**
 *
 * @author moham
 */
public class DoctorDetailsDialog extends javax.swing.JDialog {

    /**
     * Creates new form DoctorDetailsDialog
     */
    public DoctorDetailsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

private final JPanel contentPanel = new JPanel();

    public DoctorDetailsDialog(java.awt.Frame parent, boolean modal, 
                                     Date selectedDate, 
                                     List<DoctorDashboardPanel.ScheduleDetail> schedules,
                                     List<DoctorDashboardPanel.ShiftDetail> shifts) {
        super(parent, modal);
        setTitle("Schedule Details");
        setBounds(100, 100, 500, 400);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        
        sb.append("Date: ").append(dateFormat.format(selectedDate)).append("\n\n");
        
        // Add schedule details (Outpatient)
        if (schedules != null && !schedules.isEmpty()) {
            sb.append("=== OUTPATIENT SCHEDULE ===\n");
            for (DoctorDashboardPanel.ScheduleDetail schedule : schedules) {
                sb.append("Time: ").append(timeFormat.format(schedule.getTimeFrom()))
                  .append(" - ").append(timeFormat.format(schedule.getTimeTo())).append("\n")
                  .append("Room: ").append(schedule.getRoomNo()).append("\n")
                  .append("Price: LKR ").append(schedule.getPrice()).append("\n")
                  .append("Status: ").append(schedule.getStatus()).append("\n")
                  .append("------------------------\n");
            }
        }
        
        // Add shift details (Inpatient)
        if (shifts != null && !shifts.isEmpty()) {
            sb.append("\n=== INPATIENT SHIFTS ===\n");
            for (DoctorDashboardPanel.ShiftDetail shift : shifts) {
                sb.append("Ward: ").append(shift.getWardType()).append("\n")
                  .append("Shift: ").append(shift.getShiftType()).append("\n")
                  .append("------------------------\n");
            }
        }
        
        if ((schedules == null || schedules.isEmpty()) && 
            (shifts == null || shifts.isEmpty())) {
            sb.append("No schedules or shifts for this date.");
        }
        
        textArea.setText(sb.toString());
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DoctorDetailsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DoctorDetailsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DoctorDetailsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DoctorDetailsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DoctorDetailsDialog dialog = new DoctorDetailsDialog(new javax.swing.JFrame(), true);
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
    // End of variables declaration//GEN-END:variables
}
