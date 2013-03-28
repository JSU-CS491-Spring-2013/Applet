import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class JTreePanel extends JPanel {

    JScrollPane scrollPane;
    JTree tree;
    NodePanel nodePanel;
    Clause temp;

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
        scrollPane.setBounds(0, 0, 250, 686);   //CD - Need these numbers for scroll bar
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        tree.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
        
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
            try 
            {
                Clause temp = getSelectedNode().getClause();
                
                if (temp.getData().equals("root"))    //CD - This disables the root folder on the left list
                {
                    DiscourseAnalysisApplet.nodePanel.hideButtonPanel();
                }
                
                else
                {
                    boolean before = DiscourseAnalysisApplet.nodePanel.isButtonPanelShown();

                    DiscourseAnalysisApplet.nodePanel.showButtonPanel(temp.getX(), temp.getY()); // show the buttonpanel next to it
                    DiscourseAnalysisApplet.buttonPanel.associateClauseAndNode(temp, getSelectedNode());

                    if (!before && DiscourseAnalysisApplet.nodePanel.isButtonPanelShown()) 
                    {
                        temp.enableTextArea();
                        //temp.updateClauseBounds();
                        DiscourseAnalysisApplet.buttonPanel.editEnable();
                        nodePanel.scrollRectToVisible(temp.getBounds()); // scroll to the component
                    }
                    temp.getJTextPane().setCaretPosition(temp.getData().length());    //CD - Sets caret at end of text
                } 
            }
            catch (Exception ex) 
            { 
                DiscourseAnalysisApplet.buttonPanel.editEnable();    //CD
                temp.enableTextArea();    //CD
                System.out.println("Error in JTreePanel");
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