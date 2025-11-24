package view;

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ContactView extends javax.swing.JFrame {

    public ContactView() {
        initComponents();
        setLocationRelativeTo(null);

        // Cargar los datos del combobox
        String[] languages = {"Español", "Ingles", "Francés"};
        for (String language : languages) {
            jComboBoxLanguage.addItem(language);
        }

        jPanelContacts.setLayout(new BorderLayout());
        jPanelStats.setLayout(new BorderLayout());
    }

    public JPanel getjPanelContacts() {
        return jPanelContacts;
    }

    public JPanel getjPanelStats() {
        return jPanelStats;
    }

    public JTabbedPane getjTabbedPaneMain() {
        return jTabbedPaneMain;
    }

    public JComboBox<String> getjComboBoxLanguage() {
        return jComboBoxLanguage;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxLanguage = new javax.swing.JComboBox<>();
        jTabbedPaneMain = new javax.swing.JTabbedPane();
        jPanelStats = new javax.swing.JPanel();
        jPanelContacts = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resources/Bundle"); // NOI18N
        setTitle(bundle.getString("ContactView.title")); // NOI18N
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(840, 720));
        setResizable(false);

        jPanelTitle.setName("jPanelTitle"); // NOI18N
        jPanelTitle.setPreferredSize(new java.awt.Dimension(800, 100));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(25, 118, 210));
        jLabel1.setText(bundle.getString("ContactView.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jLabel2.setText(bundle.getString("ContactView.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jComboBoxLanguage.setName("jComboBoxLanguage"); // NOI18N

        javax.swing.GroupLayout jPanelTitleLayout = new javax.swing.GroupLayout(jPanelTitle);
        jPanelTitle.setLayout(jPanelTitleLayout);
        jPanelTitleLayout.setHorizontalGroup(
            jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTitleLayout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addGroup(jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jComboBoxLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanelTitleLayout.setVerticalGroup(
            jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTitleLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPaneMain.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTabbedPaneMain.setName("jTabbedPaneMain"); // NOI18N
        jTabbedPaneMain.setPreferredSize(new java.awt.Dimension(800, 500));

        jPanelStats.setName("jPanelStats"); // NOI18N
        jPanelStats.setPreferredSize(new java.awt.Dimension(800, 500));

        javax.swing.GroupLayout jPanelStatsLayout = new javax.swing.GroupLayout(jPanelStats);
        jPanelStats.setLayout(jPanelStatsLayout);
        jPanelStatsLayout.setHorizontalGroup(
            jPanelStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanelStatsLayout.setVerticalGroup(
            jPanelStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 571, Short.MAX_VALUE)
        );

        jTabbedPaneMain.addTab(bundle.getString("ContactView.jPanelStats.TabConstraints.tabTitle"), jPanelStats); // NOI18N

        jPanelContacts.setName("jPanelContacts"); // NOI18N
        jPanelContacts.setPreferredSize(new java.awt.Dimension(800, 500));

        javax.swing.GroupLayout jPanelContactsLayout = new javax.swing.GroupLayout(jPanelContacts);
        jPanelContacts.setLayout(jPanelContactsLayout);
        jPanelContactsLayout.setHorizontalGroup(
            jPanelContactsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanelContactsLayout.setVerticalGroup(
            jPanelContactsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 571, Short.MAX_VALUE)
        );

        jTabbedPaneMain.addTab(bundle.getString("ContactView.jPanelContacts.TabConstraints.tabTitle"), jPanelContacts); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jTabbedPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName(bundle.getString("ContactView.AccessibleContext.accessibleName")); // NOI18N
        getAccessibleContext().setAccessibleDescription(bundle.getString("ContactView.AccessibleContext.accessibleDescription")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBoxLanguage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanelContacts;
    private javax.swing.JPanel jPanelStats;
    private javax.swing.JPanel jPanelTitle;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    // End of variables declaration//GEN-END:variables
}
