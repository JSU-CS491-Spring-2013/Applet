
/**
 * This is the new ButtonPanel. It no longer contains two ClausePanels. The UI
 * looks the same, but it was built differently. I used the NetBeans IDE GUI
 * builder. The associated ButtonPanel.form is included.
 */
public class ButtonPanel extends javax.swing.JPanel {

    private DiscourseAnalysisApplet myParent;                // This is the instance of DiscourseAnalysisApplet that created this ButtonPanel. Once we split that huge function, we should make those new methods static.
    private XMLTreeNode firstNodeSelection;     // Holds the Node used in the firstNodeSelection box
    private XMLTreeNode lastNodeSelection;      // Holds the Node used in the lastNodeSelection box

    /**
     * Toggle the enabled property for the buttons.
     *
     * @param enabled the value to assign to the enabled attribute of the
     * buttons
     */
    public final void setButtonsEnabled(boolean enabled) {
        cmdEdit.setEnabled(enabled);
        cmdGroup.setEnabled(enabled);
        cmdRemove.setEnabled(enabled);
        cmdSplit.setEnabled(enabled);
        cmdTreeModel.setEnabled(enabled);
    }

    /**
     * Creates new form ButtonPanel
     */
    public ButtonPanel(DiscourseAnalysisApplet myParent) {
        initComponents();

        Clause temp = null;
        setButtonsEnabled(false);
        this.myParent = myParent;
    }

    /**
     * Sets the Combo Box with the correct number of Nodes to be grouped? That's
     * what their comment said. In reality, it lists 1 through n where n is the
     * total number of nodes minus the index of the node you select. The lower
     * down the tree you select a node, the smaller n will be.
     */
    public void populateComboBox() {
        comComboBox.removeAllItems(); // I added this. The previous would just keep adding them for forever.

        try {
            XMLTreeNode parent = (XMLTreeNode) myParent.getNodePanel().getSelected().getParent(); // Gets the parent. You read from left to right just like normal. Nodes to the right are child nodes.
            int currentI = parent.getIndex(myParent.getNodePanel().getSelected()) + 1;
            int totalC = parent.getChildCount();
            for (int i = 0; i < totalC - currentI; i++) {
                comComboBox.addItem(new Integer(i + 1));
            }
        } catch (java.lang.NullPointerException npe) {
        }
    }

    /**
     * Logically sets the first Node selected, and displays its information in
     * the top box.
     *
     * @param selected the Node to be selected
     */
    public void setFirstNodeSelection(XMLTreeNode selected) {
        // Try to set the information based on the Node.
        try {
            setButtonsEnabled(true);
            firstNodeSelection = selected;

            // Populate the top box with the Node's information.
            txtFirstChapter.setText(selected.getChap());
            txtFirstVerse.setText(selected.getVrse());
            txtFirstConjunction.setText(selected.getConj());
            txtFirstText.setText(selected.getData());
        } catch (NullPointerException edit) { // Insert blanks if it fails.
            txtFirstChapter.setText("");
            txtFirstVerse.setText("");
            txtFirstConjunction.setText("");
            txtFirstText.setText("");
            txtLastChapter.setText("");
            txtLastVerse.setText("");
            txtLastConjunction.setText("");
            txtLastText.setText("");
        }
    }

    /**
     * Logically sets the last Node selected, and displays its information in
     * the bottom box.
     *
     * @param selected the Node to be selected
     */
    public void setLastNodeSelection(XMLTreeNode selected) {
        // Try to set the information based on the Node.
        try {
            setButtonsEnabled(true); // Note that the buttons will not be turned on if this section fails.
            lastNodeSelection = selected;

            // Populate the botton box with the Node's information.
            txtLastChapter.setText(selected.getChap());
            txtLastVerse.setText(selected.getVrse());
            txtLastConjunction.setText(selected.getConj());
            txtLastText.setText(selected.getData());
        } catch (NullPointerException edit) { // Insert blanks if it fails.
            txtFirstChapter.setText("");
            txtFirstVerse.setText("");
            txtFirstConjunction.setText("");
            txtFirstText.setText("");
            txtLastChapter.setText("");
            txtLastVerse.setText("");
            txtLastConjunction.setText("");
            txtLastText.setText("");
        }
    }

    /**
     * Returns the Node represented in the top box.
     *
     * @return the Node in the top box
     */
    public XMLTreeNode getFirstNodeSelection() {
        return firstNodeSelection;
    }

    /**
     * Returns the Node represented in the bottom box.
     *
     * @return the Node in the bottom box
     */
    public XMLTreeNode getLastNodeSelection() {
        return lastNodeSelection;
    }

    /**
     * Returns the number associated with the selected value in comComboBox.
     *
     * @return an int representing the selected value
     */
    public int getComboBoxSelectedIndex() {
        return comComboBox.getSelectedIndex();
    }

    /*
     * public static void main(String[] args) { // Testing code. try { // I want
     * to add this code at some point. This will make it look much better, but
     * we'll worry about making it pretty later. for
     * (javax.swing.UIManager.LookAndFeelInfo info :
     * javax.swing.UIManager.getInstalledLookAndFeels()) { if
     * ("Nimbus".equals(info.getName())) {
     * javax.swing.UIManager.setLookAndFeel(info.getClassName()); break; } } }
     * catch (Exception e) { }
     *
     * javax.swing.JFrame myFrame = new javax.swing.JFrame("Testing");
     * myFrame.setLayout(new java.awt.GridLayout(1, 1)); myFrame.add(new
     * BetterButtonPanel());
     * myFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
     * myFrame.pack(); myFrame.setVisible(true); }
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdOkay = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdTreeModel = new javax.swing.JButton();
        cmdSplit = new javax.swing.JButton();
        cmdEdit = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        cmdGroup = new javax.swing.JButton();
        comComboBox = new javax.swing.JComboBox();

        cmdOkay.setText("Okay");
        cmdOkay.setNextFocusableComponent(cmdCancel);

        cmdCancel.setText("Cancel");
        cmdCancel.setNextFocusableComponent(cmdTreeModel);

        cmdTreeModel.setText("Tree Model");
        cmdTreeModel.setNextFocusableComponent(cmdSplit);

        cmdSplit.setText("Split");
        cmdSplit.setNextFocusableComponent(cmdEdit);

        cmdEdit.setText("Edit");
        cmdEdit.setNextFocusableComponent(cmdRemove);

        cmdRemove.setText("Remove");
        cmdRemove.setNextFocusableComponent(cmdGroup);

        cmdGroup.setText("Group");
        cmdGroup.setNextFocusableComponent(comComboBox);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cmdGroup, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmdEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmdTreeModel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmdOkay, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdSplit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdRemove, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdOkay)
                    .addComponent(cmdCancel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdTreeModel)
                    .addComponent(cmdSplit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdEdit)
                    .addComponent(cmdRemove))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdGroup)
                    .addComponent(comComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdGroup;
    private javax.swing.JButton cmdOkay;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSplit;
    private javax.swing.JButton cmdTreeModel;
    private javax.swing.JComboBox comComboBox;
    // End of variables declaration//GEN-END:variables
}
