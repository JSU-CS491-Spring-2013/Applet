import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class JTreePanel extends JPanel {

    JScrollPane scrollPane;
    JTree tree;
    NodePanel nodePanel;

    /**
     * Create a panel and populate it with the given tree.
     * @param model the XMLTreeModel that is used
     */
    public JTreePanel(XMLTreeModel model, final NodePanel nodePanel) {
        this.nodePanel = nodePanel;
        this.setLayout(null);
        tree = new JTree(model);
        scrollPane = new JScrollPane(tree);
        this.add(scrollPane);
        this.setMaximumSize(new Dimension(200, 500));
        scrollPane.setBounds(0, 0, 250, 700);  // Redundant? Which is correct?

        tree.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            /**
             * Pressing the mouse button displays the ButtonPanel next to the Clause.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Clause temp = getSelectedNode().getClause();

                    boolean before = DiscourseAnalysisApplet.nodePanel.isButtonPanelShown();
                    // show the buttonpanel next to it
                    DiscourseAnalysisApplet.nodePanel.showButtonPanel(temp.getX(), temp.getY());

                    if (!before && DiscourseAnalysisApplet.nodePanel.isButtonPanelShown()) {
                        temp.enableTextArea(); // enable and focus
                        nodePanel.scrollRectToVisible(temp.getBounds()); // scroll to the component
                    }
                } catch (Exception ex) { // this will throw if you click a folder, so do nothing
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /**
     * Returns the Node that is selected in this panel.
     * @return the selected Node
     */
    public XMLTreeNode getSelectedNode() {
        //Get the selected node from the JTree, return null if nothing is selected.
        return (XMLTreeNode) tree.getLeadSelectionPath().getLastPathComponent();
    }

    /**
     * Reset the tree.
     * @param mod the XMLTreeModel used to make the new tree
     */
    public void setTreeModel(XMLTreeModel mod) {
        tree.setModel(mod);
    }
    
    /**
     * Validates the GUI components in this panel.
     */
    public void update() {
        tree.revalidate();
        tree.repaint();
    }

    /**
     * Returns the index of the selected Node.
     * @return an int identifying the selected Node
     */
    public int getSelectedIndex() {
        return tree.getLeadSelectionRow();
    }
}