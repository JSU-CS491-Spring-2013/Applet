
import java.awt.*;
import javax.swing.*;

public class JTreePanel extends JPanel {
    //Make JTree, add it to a scroll pane.

    JScrollPane scrollPane;
    JTree tree;

    /**
    Contructor builds a new JTreePanel
    @param XMLTreeModel model
     */
    public JTreePanel(XMLTreeModel model) {
        this.setLayout(null);
        tree = new JTree(model);
        scrollPane = new JScrollPane(tree);
        this.add(scrollPane);
        this.setMaximumSize(new Dimension(200, 500));
        scrollPane.setBounds(0, 0, 250, 720);
    }

    //method for getting the selected node
    /**
    Returns the Node that is Selected in this panel
    @return XMLTreeNode
     */
    public XMLTreeNode getSelectedNode() {
        //Get the selected node from the JTree, return null if nothing is selected.
        return (XMLTreeNode) tree.getLeadSelectionPath().getLastPathComponent();
    }

    /**
    Reset the XMLTreeModel
    @param XMLTreeModel mod
     */
    public void setTreeModel(XMLTreeModel mod) {
        tree.setModel(mod);
    }

    /**
    Validates the GUI components in this panel
     */
    public void update() {
        tree.validate();
    }

    /**
    Returns the Index of the selected Node
    @return int
     */
    public int getSelectedIndex() {
        return tree.getLeadSelectionRow();
    }

    public static void main(String[] args) {
        XMLTreeNode root = new XMLTreeNode(new Clause("this is the root", "and", "1", "0"));
        for (int i = 0; i < 40; i++) {
            XMLTreeNode temp = new XMLTreeNode(new Clause("This is child asd;lfkjasdflk;jasd;flkjasf;lkjads;" + Integer.toString(i + 1), "and", "1", Integer.toString(i + 1)));
            root.add(temp);
        }
        XMLTreeModel mod = new XMLTreeModel(root);

        JFrame f = new JFrame("Test Clause Panel");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new JTreePanel(mod));
        f.pack();
        f.setVisible(true);
    }
}