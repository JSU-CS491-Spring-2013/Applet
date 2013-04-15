import java.awt.Dimension;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel Smith
 */
public class CollisionButtonPanel extends javax.swing.JPanel {

    /**
     * Creates new form collisionButtonPanel
     */
    public CollisionButtonPanel() {
        initComponents();
        XMLTreeNode selected = null;
        XMLTreeNode target = null;
        setPreferredSize(new Dimension(80,200));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton1.setText("Group");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Merge");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Cancel");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try{
            DiscourseAnalysisApplet.getXMLTreeModel().groupNodes(target, selected);
            selected.getClause().reposition(target);
			selected.getClause().repositionChildren(selected);
            DiscourseAnalysisApplet.tree.resetXY();
            DiscourseAnalysisApplet.tree.updateNodes();
            
			selected.getClause().reset();
			selected.getClause().changeVisibility(selected, true);
        }
        catch(IllegalArgumentException ex){
            //if the dragged node is an ancestor of the destination node
            //reset the dragged node
            System.out.println("There was a problem.");
            DiscourseAnalysisApplet.tree.resetXY();
            DiscourseAnalysisApplet.tree.updateNodes();
            selected.getClause().reset();
        }
        DiscourseAnalysisApplet.nodePanel.hideCollisionButtonPanel();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try{
            DiscourseAnalysisApplet.getXMLTreeModel().mergeNodes(selected, target);
        }
        catch(IllegalArgumentException ex){
            //if merge is not possible
        }
        target.getClause().updateClauseBounds();
        selected.getClause().changeVisibility(selected, false);
        selected.getClause().setEnabled(false);
        DiscourseAnalysisApplet.nodePanel.hideCollisionButtonPanel();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DiscourseAnalysisApplet.nodePanel.hideCollisionButtonPanel();
        DiscourseAnalysisApplet.tree.resetXY();
        DiscourseAnalysisApplet.tree.updateNodes();
        selected.getClause().changeVisibility(selected, true);
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    // End of variables declaration//GEN-END:variables
    private XMLTreeNode selected;
    private XMLTreeNode target;
    
    //Methods
    public void setSelected(XMLTreeNode s){
        selected = s;
    }
    
    public void setTarget(XMLTreeNode t){
        target = t;
    }
}
