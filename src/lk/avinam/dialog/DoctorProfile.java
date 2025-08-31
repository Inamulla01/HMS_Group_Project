/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package lk.avinam.dialog;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 *
 * @author moham
 */
public class DoctorProfile extends javax.swing.JDialog {

    public DoctorProfile(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();

    }

    private void init() {
        availableBtn.setIcon(new FlatSVGIcon("lk/avinam/icon/plus.svg", 20, 20));
        FlatSVGIcon addIcon = new FlatSVGIcon("lk/avinam/icon/edit.svg", 20, 20);
        addIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.decode("#CAF0F8")));
        editBtn.setIcon(addIcon);
        FlatSVGIcon addIcon1 = new FlatSVGIcon("lk/avinam/icon/delete.svg", 20, 20);
        addIcon1.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.RED));
        deleteBtn.setIcon(addIcon1);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        slmcId = new javax.swing.JTextField();
        fullName = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        qualification = new javax.swing.JTextField();
        contact = new javax.swing.JTextField();
        joinAt = new javax.swing.JTextField();
        spatialisedIn = new javax.swing.JTextField();
        password = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        clearBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        availableBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        deleteBtn = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        slmcId.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        slmcId.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SLMC ID", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        slmcId.setEnabled(false);

        fullName.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        fullName.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Full Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        fullName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullNameActionPerformed(evt);
            }
        });

        email.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        email.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Email", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        email.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });

        qualification.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        qualification.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Qualification", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        qualification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qualificationActionPerformed(evt);
            }
        });

        contact.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        contact.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Contact No", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        contact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactActionPerformed(evt);
            }
        });

        joinAt.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        joinAt.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Join At", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        joinAt.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        joinAt.setEnabled(false);
        joinAt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinAtActionPerformed(evt);
            }
        });

        spatialisedIn.setFont(new java.awt.Font("Nunito ExtraLight", 1, 14)); // NOI18N
        spatialisedIn.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Spatialised In", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        spatialisedIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spatialisedInActionPerformed(evt);
            }
        });

        password.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        password.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Password", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nunito SemiBold", 1, 14))); // NOI18N
        password.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        password.setEnabled(false);
        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });

        jLabel3.setText("Doctor Profile");
        jLabel3.setFont(new java.awt.Font("Nunito ExtraBold", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(3, 4, 94));

        clearBtn.setText("Clear");
        clearBtn.setBackground(new java.awt.Color(202, 240, 248));
        clearBtn.setFocusable(false);
        clearBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        clearBtn.setForeground(new java.awt.Color(3, 4, 94));
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        editBtn.setText("Edit");
        editBtn.setBackground(new java.awt.Color(3, 4, 94));
        editBtn.setFocusable(false);
        editBtn.setFont(new java.awt.Font("Nunito ExtraBold", 1, 14)); // NOI18N
        editBtn.setForeground(new java.awt.Color(204, 255, 255));

        availableBtn.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        availableBtn.setText("Add New Schedule");
        availableBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        availableBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        availableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                availableBtnActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Nunito SemiBold", 1, 14)); // NOI18N
        jLabel12.setText("Available Date : ");

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Nunito ExtraBold", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(3, 4, 94));
        jLabel11.setText("Doctor Schedule Details");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(joinAt, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                                    .addComponent(spatialisedIn))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(qualification)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(contact, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addComponent(email)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fullName, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(availableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(slmcId, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(22, 22, 22))))
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
                .addComponent(fullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(joinAt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contact))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spatialisedIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qualification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(availableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
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

    private void fullNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fullNameActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void qualificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qualificationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qualificationActionPerformed

    private void contactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactActionPerformed

    private void spatialisedInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spatialisedInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_spatialisedInActionPerformed

    private void joinAtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinAtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_joinAtActionPerformed

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearBtnActionPerformed

    private void availableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availableBtnActionPerformed
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AddSchedule dialog = new AddSchedule(parentFrame, true);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }//GEN-LAST:event_availableBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatIntelliJLaf.setup();

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
    private javax.swing.JButton clearBtn;
    private javax.swing.JTextField contact;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JTextField email;
    private javax.swing.JTextField fullName;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField joinAt;
    private javax.swing.JTextField password;
    private javax.swing.JTextField qualification;
    private javax.swing.JTextField slmcId;
    private javax.swing.JTextField spatialisedIn;
    // End of variables declaration//GEN-END:variables
}
