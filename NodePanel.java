
import java.awt.*;
import java.util.Enumeration;
import javax.swing.JPanel;

/**
The NodePanel is used to visually represent an XMLTreeModel.
This class also provides a MouseListener to keep track of which node 
has been selected.
 */
public class NodePanel extends JPanel {

    /**
    XMLTreeNode rootNode		Is the root of the TreeModel
    XMLTreeNode selected Node	Is the selected Node
    Rectangle tempRectangle		The rectangle used to show the selectedNode	
    
     */
    XMLTreeNode rootNode;
    XMLTreeNode selectedNode;
    XMLTreeNode otherSelectedNode;
    Rectangle tempRectangle;
    private final int stubLength = 10; //Stub
    private final int nodeWidth = 200; //Node width
    private boolean hasChanged;

    /**
    Contructor Creates the NodePanel
    @param XMLTreeNode ro	sets the rootNode
    @param int x			sets the x dimension for this panel
    @param int y			sets the y dimension for this panel
     */
    public NodePanel(XMLTreeNode ro, int x, int y) {
        rootNode = ro;
        setPreferredSize(new Dimension(x, y));
        
        // Make that background easier on the eyes.
        setBackground(new java.awt.Color(240, 240, 240));
        hasChanged = false;
        setLayout(null);
        updateComponents();
    }

    /**
    sets the rootNode for this panel
    @param XMLTreeNode rootNode
    @param int x
    @param int y
     */
    public void setRoot(XMLTreeNode rootNode, int x, int y) {
        this.rootNode = rootNode;
        selectedNode = null;
        setPreferredSize(new Dimension(x, y));
        this.repaint();
    }
    
    /**
     * Draw the lines.
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Enumeration n = rootNode.preorderEnumeration(); // Get a list of Nodes.
        while (n.hasMoreElements()) {
            XMLTreeNode curr = (XMLTreeNode) n.nextElement();
            curr.getX();
            curr.getY();
            
            // If the node has children
            if (curr.getDepth() > 0) {
                g.drawLine(curr.getX() + 260, curr.getY() + 48, curr.getX() + 300, curr.getY() + 48);
                
                // Get the first and last children.
                XMLTreeNode first = (XMLTreeNode) curr.getFirstChild();
                XMLTreeNode last = (XMLTreeNode) curr.getLastChild();
                
                // If they aren't the same thing, draw a line between them.
                if (first != last) {
                    g.drawLine(first.getX() - 40, first.getY() + 48, last.getX() - 40, last.getY() + 48);
                }
            }
   
            // If the node is a child
            if (!curr.isRoot()) {
                g.drawLine(curr.getX(), curr.getY() + 48, curr.getX() - 40, curr.getY() + 48);
            }
        }
    }
    
    /**
     * This adds the Clauses to the NodePanel (the one in the middle).
     */
    private void updateComponents() {
        Enumeration n = rootNode.preorderEnumeration(); // Get a list of Nodes.

        while (n.hasMoreElements()) {
            XMLTreeNode curr = (XMLTreeNode) n.nextElement();
            Clause temp = curr.getClause();
            temp.updateClauseBounds(); // Make sure to update its position.
            add(temp);
        }
    }

    /**
    Returns the Selected Node
    @return XMLTreeNode selectedNode
     */
    public XMLTreeNode getSelected() {
        hasChanged = false;
        return selectedNode;
    }

    /**
    Resets the Selected Node and repaints the panel
    
     */
    public void resetSelNode() {
        selectedNode = null;
        tempRectangle = null;
        repaint();
    }

    /**
    If the selected node has changed, return true
    @return boolean hasChanged
     */
    public boolean isChanged() {
        return hasChanged;
    }
    /*
    public static void main(String[] args)
    {
    System.out.println("Hello");
    XMLTreeNode te = new XMLTreeNode(new Clause("root", "Luke", "",""));
    
    XMLTreeNode[] level1 = new XMLTreeNode[10];
    for(int i = 0; i < level1.length; i++)
    {
    Integer x = new Integer(i+1);
    level1[i] = new XMLTreeNode(new Clause("This is child "+x.toString(), "and", "1", x.toString()));
    te.add(level1[i]);
    }
    
    for(int i = 0; i < 5; i++)
    {
    Integer x = new Integer(i+1);
    level1[0].add(new XMLTreeNode(new Clause("This is child "+x.toString()+" more stuff to push it to the next line", "and", "1", x.toString())));
    }
    
    XMLTreeModel test = new XMLTreeModel(te);
    NodePanel n = new NodePanel(te, test.getXMax(), test.getYMax());
    JScrollPane p = new JScrollPane(n);
    p.setPreferredSize(new Dimension(450,450));
    p.setBounds(0, 0, 450, 450);
    
    JFrame f = new JFrame("Test Clause Panel");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLayout(null);
    f.getContentPane().add(p);
    f.pack();
    f.setSize(500,500);
    f.setVisible(true);
    
    Scanner scan = new Scanner(System.in);
    String stubLength = scan.next();
    test.remove(level1[0]);
    test.reload();
    test.resetXY();
    n.setRoot((XMLTreeNode)test.getRoot(), test.getXMax(), test.getYMax());
    }
     */
}