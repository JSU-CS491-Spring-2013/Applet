import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import javax.swing.JLayeredPane;

/**
 * The NodePanel is used to visually represent an XMLTreeModel.
 * This class also provides a MouseListener to keep track of which node 
 * has been selected.
 */
public class NodePanel extends JLayeredPane {
    XMLTreeNode rootNode; 
    static XMLTreeNode selectedNode;
    XMLTreeNode otherSelectedNode;
    Rectangle tempRectangle;
    private final int stubLength = 10;  //Stub
    private final int nodeWidth = 200;  //Node width
    static private boolean hasChanged;         // I'm not sure why this is here.
    private boolean buttonPanelShown;   // hold whether a ButtonPanel is active on the panel
    private boolean collisionButtonPanelShown;   // hold whether a collisionButtonPanel is active on the panel
    private XMLTreeModel tree;
    
    /**
     * Creates the NodePanel.
     * @param ro sets the root node
     * @param x sets the x dimension for this panel
     * @param y sets the y dimension for this panel
     */
    public NodePanel(XMLTreeNode ro, int x, int y) {
        rootNode = ro;
        buttonPanelShown = false;
        collisionButtonPanelShown = false;
        setPreferredSize(new Dimension(x, y));
        tree = DiscourseAnalysisApplet.tree;
        
        hasChanged = false;
        setLayout(null);
        updateComponents(); // Reposition the Clauses.

        MouseListener ml = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus(); // This should steal focus away from text areas.
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
        };

        addMouseListener(ml);
    }

    /**
     * This returns the status of whether a ButtonPanel is being shown on the NodePanel.
     * @return true if there is an active ButtonPanel, false otherwise
     */
    public boolean isButtonPanelShown() {
        return buttonPanelShown;
    }
    public boolean isCollisionButtonPanelShown() {
        return collisionButtonPanelShown;
    }

    /**
     * Displays the ButtonPanel next to the Clause.
     * @param x the x-coordinate of where to display the ButtonPanel
     * @param y the y-coordinate of where to display the ButtonPanel
     */
    public void showButtonPanel(int x, int y) {
        if (!isButtonPanelShown()) { // Only display it if it is currently hidden.
            if (x + 260 + 249 <= 1000) { // If there is enough room on the right, display the ButtonPanel to the right of the Clause.
                DiscourseAnalysisApplet.buttonPanel.setBounds(x + 260, y, 249, 150);
            } else { // If there isn't enough room on the right, display the ButtonPanel to the left of the Clause.
                DiscourseAnalysisApplet.buttonPanel.setBounds(x - 249, y, 249, 150);
            }

            DiscourseAnalysisApplet.buttonPanel.setEnabled(true);
            DiscourseAnalysisApplet.buttonPanel.setVisible(true);

            buttonPanelShown = true;
        }
    }

    /**
     * Hides the ButtonPanel on the NodePanel.
     */
    public void hideButtonPanel() {
        DiscourseAnalysisApplet.buttonPanel.setEnabled(false);
        DiscourseAnalysisApplet.buttonPanel.setVisible(false);
        buttonPanelShown = false;
        requestFocus();
    }
    
    /**
     * Displays the collisionButtonPanel next to the Clause.
     * @param x the x-coordinate of where to display the collisionButtonPanel
     * @param y the y-coordinate of where to display the collisionButtonPanel
     */
    public void showCollisionButtonPanel(int x, int y) {
    	//System.out.println("In the showCollisionButtonPanel method.");
        if (!isCollisionButtonPanelShown()) { // Only display it if it is currently hidden.
            if (x + 260 + 249 <= 1000) { // If there is enough room on the right, display the ButtonPanel to the right of the Clause.
                DiscourseAnalysisApplet.collisionButtonPanel.setBounds(x + 260, y, 249, 150);
            } else { // If there isn't enough room on the right, display the ButtonPanel to the left of the Clause.
                DiscourseAnalysisApplet.collisionButtonPanel.setBounds(x - 249, y, 249, 150);
            }

            DiscourseAnalysisApplet.collisionButtonPanel.setEnabled(true);
            DiscourseAnalysisApplet.collisionButtonPanel.setVisible(true);

            collisionButtonPanelShown = true;
        }
    }

    /**
     * Hides the collisionButtonPanel on the NodePanel.
     */
    public void hideCollisionButtonPanel() {
        DiscourseAnalysisApplet.collisionButtonPanel.setEnabled(false);
        DiscourseAnalysisApplet.collisionButtonPanel.setVisible(false);
        collisionButtonPanelShown = false;
        requestFocus();
    }

    /**
    sets the rootNode for this panel
    @param XMLTreeNode rootNode
    @param int x
    @param int y
     */
    
    /**
     * Sets the root node for this Panel.
     * @param rootNode the node to be set as root
     * @param x the x-coordinate of this Clause
     * @param y the y-coordinate of this Clause
     */
    public void setRoot(XMLTreeNode rootNode, int x, int y) {
        this.rootNode = rootNode;        
        selectedNode = null;
        setPreferredSize(new Dimension(x, y));
        this.repaint();
        DiscourseAnalysisApplet.nodePanel.hideButtonPanel();
        DiscourseAnalysisApplet.nodePanel.hideCollisionButtonPanel();
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

            // If the node has children
            if (curr.getDepth() > 0){
            	if(curr == rootNode){
            		g.drawLine(curr.getX() + 260, curr.getY() + 48, curr.getX() + 300, curr.getY() + 48);
            	}
            	else if(curr.getChildCount() == 1){
            		XMLTreeNode child = (XMLTreeNode) curr.getFirstChild();
            		if(child.getBeingDragged() == false && child.getClause().isVisible() == true){
            			g.drawLine(curr.getX() + 260, curr.getY() + 48, curr.getX() + 300, curr.getY() + 48);
            		}
            	}
                
                try{
	                // Get the first and last children.
	                XMLTreeNode first = (XMLTreeNode) curr.getFirstChild();	                
	                XMLTreeNode last = (XMLTreeNode) curr.getLastChild();               

	                // If they aren't the same thing, draw a line between them.
	                if(first != last){
	                	g.drawLine(curr.getX() + 260, curr.getY() + 48, curr.getX() + 300, curr.getY() + 48);
		                if (first.getBeingDragged() == true || first.getClause().isVisible() == false) {
		                	if(curr.getChildCount() > 2){
			                	XMLTreeNode second = (XMLTreeNode)curr.getChildAt(1);			                
				                g.drawLine(second.getX() - 40, second.getY() + 48, last.getX() - 40, last.getY() + 48);
			                }
		                	else{
		                		g.drawLine(last.getX() - 40, curr.getY() + 48, last.getX() - 40, last.getY() + 48);
		                	}
		                }
		                else if(last.getBeingDragged() == true || last.getClause().isVisible() == false){
		                	if(curr.getChildCount() > 2){		                	
				                XMLTreeNode nextToLast = (XMLTreeNode)curr.getChildAt(curr.getChildCount() - 2);
				                g.drawLine(first.getX() - 40, first.getY() + 48, nextToLast.getX() - 40, nextToLast.getY() + 48);
			                }
		                	else{
		                		g.drawLine(first.getX() - 40, first.getY() + 48, first.getX() - 40, curr.getY() + 48);
		                	}
		                }
		                else{
		                	//neither are being dragged, draw the line
		                	g.drawLine(first.getX() - 40, first.getY() + 48, last.getX() - 40, last.getY() + 48);		                	
		                }
	                }	                
                }
                catch(Exception ex){
                	//NoSuchElementException
                	//ArrayIndexOutOfBoundsException
                	System.out.println(ex);
                }
            }

            // If the node is a child
            if (!curr.isRoot() && curr.getBeingDragged() == false && curr.getClause().isVisible() == true) {
                g.drawLine(curr.getX(), curr.getY() + 48, curr.getX() - 40, curr.getY() + 48);
            }
            else{
                repaint();
            }
        }
    }

    /**
     * This adds the Clauses to the NodePanel.
     */
    public void updateComponents() {
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
    
    static public XMLTreeNode getSelected() {
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
}