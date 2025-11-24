package view;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ContactStats extends javax.swing.JPanel {

    public ContactStats() {
        initComponents();
    }

    public JTextArea getjTextAreaLastRecord() {
        return jTextAreaLastRecord;
    }

    public JTextField getjTextFieldTotalRecords() {
        return jTextFieldTotalRecords;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane = new javax.swing.JScrollPane();
        jTextAreaLastRecord = new javax.swing.JTextArea();
        jTextFieldTotalRecords = new javax.swing.JTextField();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(800, 500));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(25, 118, 210));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resources/Bundle"); // NOI18N
        jLabel2.setText(bundle.getString("ContactStats.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText(bundle.getString("ContactStats.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText(bundle.getString("ContactStats.jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jScrollPane.setName("jScrollPane"); // NOI18N

        jTextAreaLastRecord.setEditable(false);
        jTextAreaLastRecord.setColumns(20);
        jTextAreaLastRecord.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextAreaLastRecord.setLineWrap(true);
        jTextAreaLastRecord.setRows(5);
        jTextAreaLastRecord.setName("jTextAreaLastRecord"); // NOI18N
        jScrollPane.setViewportView(jTextAreaLastRecord);

        jTextFieldTotalRecords.setEditable(false);
        jTextFieldTotalRecords.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldTotalRecords.setName("jTextFieldTotalRecords"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(345, 345, 345)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                            .addComponent(jTextFieldTotalRecords))))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel2)
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel5))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jTextFieldTotalRecords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(238, 238, 238))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextArea jTextAreaLastRecord;
    private javax.swing.JTextField jTextFieldTotalRecords;
    // End of variables declaration//GEN-END:variables
}
