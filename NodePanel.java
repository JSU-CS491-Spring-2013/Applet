
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
The NodePanel is used to visually represent an XMLTreeModel.
This class also provides a MouseListener to keep track of which node 
has been selected.
 */
public class NodePanel extends JPanel implements MouseListener {

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
        setBackground(Color.white);
        addMouseListener(this);
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

    private void updateComponents() {
        Enumeration n = rootNode.preorderEnumeration();
        
        while (n.hasMoreElements()) {
            /*XMLTreeNode curr = (XMLTreeNode) n.nextElement();
            JTextArea tempTextArea = new JTextArea();
            JScrollPane pane = new JScrollPane();
            JPanel myPanel = new JPanel();
            myPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(curr.getChap() + ":" + curr.getVrse() + " " + curr.getConj()));
            tempTextArea.setLineWrap(true);
            tempTextArea.setWrapStyleWord(true);

            tempTextArea.setText(curr.getData());
            // tempTextArea.setBounds(curr.getX(), curr.getY(), 200, 75);
            pane.setViewportView(tempTextArea);
            //pane.setBounds(curr.getX() + 10, curr.getY() + 10, 150, 75);
            myPanel.setBounds(curr.getX(), curr.getY(), 260, 95);
            
            myPanel.setLayout(new GridLayout(1, 1));
            myPanel.add(pane);
            tempTextArea.setEnabled(false);
            add(myPanel);
            */
            
            XMLTreeNode curr = (XMLTreeNode) n.nextElement();
            Clause temp = curr.getClause();
            temp.updateClauseBounds();
            add(temp);
        }
    }
    
    /**
    Overrides the paintComponent method in the JPanel class
    @param Graphics g
     * /
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);//Always call this first

        Graphics2D f = (Graphics2D) g;

        try {
            Enumeration n = rootNode.preorderEnumeration();
            int count = 0;

            while (n.hasMoreElements()) {
                f.setColor(Color.darkGray);
                XMLTreeNode curr = (XMLTreeNode) n.nextElement();
                Rectangle bTemp = curr.getClause().getBig();
                Rectangle sTemp = curr.getClause().getSmall();
                int mid = curr.getClause().getMid();
                Point cP = curr.getClause().getP();
                f.setColor(Color.darkGray);
                if (curr.equals(selectedNode)) {
                    f.setColor(Color.lightGray);
                }
                f.fill(bTemp);
                f.setColor(Color.darkGray);
                f.draw(bTemp);
                f.setColor(Color.black);
                if (count == 0) {

                    f.drawString(curr.getConj(), bTemp.x + 2, bTemp.y + bTemp.height - 7);
                    f.drawLine(bTemp.x + bTemp.width, mid, bTemp.x + bTemp.width + stubLength, mid);

                    int xF = ((XMLTreeNode) curr.getFirstChild()).getX() - stubLength;
                    int yF = ((XMLTreeNode) curr.getFirstChild()).getClause().getMid();

                    int xL = ((XMLTreeNode) curr.getLastChild()).getX() - stubLength;
                    int yL = ((XMLTreeNode) curr.getLastChild()).getClause().getMid();

                    g.drawLine(xF, yF, xL, yL);
                    count++;
                } else {
                    f.setColor(Color.darkGray);
                    f.fill(sTemp);
                    f.setColor(Color.darkGray);
                    f.draw(sTemp);
                    f.setColor(Color.black);
                    f.drawString(curr.getChap() + ":" + curr.getVrse(), sTemp.x + 2, sTemp.y + sTemp.height - 1);
                    String[] t = curr.getWordWrap();
                    for (int i = 0; i < t.length; i++) {
                        f.drawString(t[i], curr.getX() + 3, 2 + curr.getY() + (13 * (i + 1)));
                    }

                    if (!curr.isLeaf()) {
                        f.drawLine(curr.getX() + nodeWidth, curr.getClause().getMid(), curr.getX() + (nodeWidth + stubLength), curr.getClause().getMid());
                        int xF = ((XMLTreeNode) curr.getFirstChild()).getX() - stubLength;
                        int yF = ((XMLTreeNode) curr.getFirstChild()).getClause().getMid();

                        int xL = ((XMLTreeNode) curr.getLastChild()).getX() - stubLength;
                        int yL = ((XMLTreeNode) curr.getLastChild()).getClause().getMid();

                        Rectangle fC = ((XMLTreeNode) curr.getFirstChild()).getClause().getBig();
                        Rectangle lC = ((XMLTreeNode) curr.getLastChild()).getClause().getBig();

                        f.drawLine(fC.x - stubLength, yF, lC.x - stubLength, yL);
                    }
                    f.drawLine(bTemp.x, curr.getClause().getMid(), bTemp.x - stubLength, curr.getClause().getMid());
                    f.drawString(curr.getClause().getConj(), (int) cP.getX(), (int) cP.getY());
                }
            }
        } catch (NoSuchElementException e) {
        }
    }*/

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

    /**
    MouseListener method used to listen for node selection
    @param MouseEvent e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        hasChanged = true;
        Enumeration n = rootNode.preorderEnumeration();
        int x = e.getX();
        int y = e.getY();
        while (n.hasMoreElements()) {
            XMLTreeNode temp = (XMLTreeNode) n.nextElement();
            tempRectangle = temp.getClause().getBig();
            if (tempRectangle.contains(new Point(x, y))) {
                selectedNode = temp;
                //tempRectangle.width = tempRectangle.width+2;
                //tempRectangle.height = tempRectangle.height+2;
                break;
            } else {
                selectedNode = null;
                tempRectangle = null;
            }
        }
        repaint();
    }
    //These methods are not used

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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