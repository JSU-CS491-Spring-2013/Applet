
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
        scrollPane.setBounds(0, 0, 250, 720);  // Redundant? Which is correct?

        tree.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Clause temp = getSelectedNode().getClause();
                temp.enableTextArea();
                nodePanel.scrollRectToVisible(temp.getBounds());
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
        tree.validate();
    }

    /**
     * Returns the index of the selected Node.
     * @return an int identifying the selected Node
     */
    public int getSelectedIndex() {
        return tree.getLeadSelectionRow();
    }

    /*public static void main(String[] args) {
    XMLTreeNode root = new XMLTreeNode(new Clause("this is the root", "and", "1", "0"));
    for (int i = 0; i < 40; i++) {
    XMLTreeNode temp = new XMLTreeNode(new Clause("This is child asd;lfkjasdflk;jasd;flkjasf;lkjads;" + Integer.toString(i + 1), "and", "1", Integer.toString(i + 1)));
    root.add(temp);
    }
    XMLTreeModel mod = new XMLTreeModel(root);
    
    javax.swing.JFrame f = new javax.swing.JFrame("Test Clause Panel");
    f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(new JTreePanel(mod));
    f.pack();
    f.setVisible(true);
    }*/
}