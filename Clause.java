import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.*;

/**
 * A Clause object is composed of text, conjunction, chapter, and verse. It is
 * also a JPanel with a JTextArea in it.
 */
public class Clause extends JPanel{

    private String data;                // The text of the Clause
    private String conj;                // The conjunction of the Clause
    private String chap;                // The chapter number of the Clause
    private String vrse;                // The verse number of the Clause
    private int x;                      // The starting x-value for drawing
    private int y;                      // The starting y-value for drawing
    private int lastX;				//the last known correct x-value, see the resetNode function
    private int lastY;				//the last known correct y-value, see the resetNode function
    private final int WIDTH = 260;
    private final int HEIGHT = 95;
    private final int BUFFER = 40;
    private int oldMouseX;
    private int oldMouseY;
    private int dX;
    private int dY;
    private JTextPane myTextArea;       // This is where I will show my data.
    private JScrollPane myScrollPane;   // This allows the User to scroll through the text area.
    private XMLTreeNode root;
    private NodePanel myNodePanel;      // This is the NodePanel this Clause is on.
    private XMLTreeNode myNode;         // This is the XMLTreeNode this clause belongs to
    public XMLTreeNode clickNode;       // Information on the node for the button panel to use.
    private XMLTreeModel tree;
    private boolean doneDragging;
    private boolean beingDragged;
    private boolean lastStateSet;     	//for reseting children nodes if this node is being dragged without collision
    
    public String selected;//AiDS
    //private XMLTreeNode selected;
    /**
     * This method is called whenever this Clause's text area needs to accept
     * User input.
     */
    public void enableTextArea() {
        if (DiscourseAnalysisApplet.nodePanel.isButtonPanelShown()) {
            myTextArea.setEnabled(true);
            myTextArea.requestFocus();
        }
    }

    /**
     * Makes a new Clause object with x and y values set to 0.
     *
     * @param d the text data of the Clause
     * @param c the conjunction of the Clause
     * @param ch the chapter number of the Clause
     * @param v the verse number of the Clause
     */
    public Clause(String d, String c, String ch, String v) {
        super();
        data = d;
        conj = c;
        chap = ch;
        vrse = v;
        doneDragging = false;
        beingDragged = false;
        lastStateSet = false;

        finishStartup();
    }

    /**
     * An empty constructor. This is used by XMLHandler to make a Clause even
     * when it hasn't finished collecting all of the data.
     */
    public Clause() {
        super();
    }

    private void highlightPotentialConjunctions() {
        String temporaryData = data;

        // Get a style for normal text.
        StyleContext context = new StyleContext();
        StyledDocument document = new DefaultStyledDocument(context);
        Style style = context.getStyle(StyleContext.DEFAULT_STYLE);

        // Get a style for potential conjunctions.
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setBold(attributes, true);
        StyleConstants.setBackground(attributes, Color.BLUE);
        StyleConstants.setForeground(attributes, Color.WHITE);

        // While there is still text to be added...
        while (temporaryData.length() > 0) {
            int firstConjunction = Integer.MAX_VALUE; // Set this to the max so the calculation will work.
            String firstConjunctionText = ""; // This will hold the found potential conj
            String tempConjunction = "";
            int temporaryConjunctionLocation = -1;

            // Go through the list of conjunctions and find which one comes first.
            for (int j = 0; j < DiscourseAnalysisApplet.conjunctions.size(); j++) {
                tempConjunction = DiscourseAnalysisApplet.conjunctions.get(j);
                temporaryConjunctionLocation = temporaryData.toLowerCase().indexOf(tempConjunction.toLowerCase());

                // If a conjunction is found closer to the beginning, keep up with it instead.
                if (temporaryConjunctionLocation >= 0 && temporaryConjunctionLocation < firstConjunction) {
                    boolean wholeWord = true;

                    try {
                        char tempChar = temporaryData.toLowerCase().charAt(temporaryConjunctionLocation - 1);
                        if (tempChar == 'a'
                                || tempChar == 'b'
                                || tempChar == 'c'
                                || tempChar == 'd'
                                || tempChar == 'e'
                                || tempChar == 'f'
                                || tempChar == 'g'
                                || tempChar == 'h'
                                || tempChar == 'i'
                                || tempChar == 'j'
                                || tempChar == 'k'
                                || tempChar == 'l'
                                || tempChar == 'm'
                                || tempChar == 'n'
                                || tempChar == 'o'
                                || tempChar == 'p'
                                || tempChar == 'q'
                                || tempChar == 'r'
                                || tempChar == 's'
                                || tempChar == 't'
                                || tempChar == 'u'
                                || tempChar == 'v'
                                || tempChar == 'w'
                                || tempChar == 'x'
                                || tempChar == 'y'
                                || tempChar == 'z') {
                            wholeWord = false;
                        }
                    } catch (Exception e) {
                    }

                    try {
                        char tempChar = temporaryData.toLowerCase().charAt(temporaryConjunctionLocation + tempConjunction.length());
                        if (tempChar == 'a'
                                || tempChar == 'b'
                                || tempChar == 'c'
                                || tempChar == 'd'
                                || tempChar == 'e'
                                || tempChar == 'f'
                                || tempChar == 'g'
                                || tempChar == 'h'
                                || tempChar == 'i'
                                || tempChar == 'j'
                                || tempChar == 'k'
                                || tempChar == 'l'
                                || tempChar == 'm'
                                || tempChar == 'n'
                                || tempChar == 'o'
                                || tempChar == 'p'
                                || tempChar == 'q'
                                || tempChar == 'r'
                                || tempChar == 's'
                                || tempChar == 't'
                                || tempChar == 'u'
                                || tempChar == 'v'
                                || tempChar == 'w'
                                || tempChar == 'x'
                                || tempChar == 'y'
                                || tempChar == 'z') {
                            wholeWord = false;
                        }
                    } catch (Exception e) {
                    }

                    if (wholeWord) {
                        firstConjunction = temporaryConjunctionLocation;
                        firstConjunctionText = tempConjunction;
                    }
                }
            }

            if (firstConjunction > 0 && firstConjunction < Integer.MAX_VALUE) { // If the first conjunction appears after the beginning.
                try {
                    document.insertString(document.getLength(), temporaryData.substring(0, firstConjunction), style);
                    temporaryData = temporaryData.substring(firstConjunction);

                    document.insertString(document.getLength(), temporaryData.substring(0, firstConjunctionText.length()), attributes);
                    temporaryData = temporaryData.substring(firstConjunctionText.length());
                } catch (BadLocationException badLocationException) {
                    System.err.println("Oops");
                }
            } else if (firstConjunction == 0) { // If the first conjunction appears at the beginning.
                try {
                    document.insertString(document.getLength(), temporaryData.substring(0, firstConjunctionText.length()), attributes);
                    temporaryData = temporaryData.substring(firstConjunctionText.length());
                } catch (BadLocationException badLocationException) {
                    System.err.println("Oops");
                }
            } else { // If there are no conjunctions
                try {
                    document.insertString(document.getLength(), temporaryData, style);
                    temporaryData = "";
                } catch (BadLocationException badLocationException) {
                    System.err.println("Oops");
                }
            }
        }

        myTextArea.setDocument(document);
    }

    /**
     * This is the method that finishes the process of creating a Clause. This
     * should only be called once all data has been filled in.
     */
    public final void finishStartup() {
        x = 0;
        y = 0;
        myNode = null;

        // Set the title of the JPanel
        if (chap.isEmpty() || vrse.isEmpty()) {
            setBorder(javax.swing.BorderFactory.createTitledBorder(conj));
        } else {
            setBorder(javax.swing.BorderFactory.createTitledBorder(chap + ":" + vrse + " " + conj));
        }

        // Set my text box up
        myTextArea = new JTextPane();
        myTextArea.setText(data);
        //Goes through the list of potential conjunctions and highlights them
        highlightPotentialConjunctions();

        // Add scroll abilities to the text box
        myScrollPane = new JScrollPane();
        myScrollPane.setViewportView(myTextArea);

        // Add the scroll pane (with the text area inside) to the Clause
        setLayout(new GridLayout(1, 1));
        add(myScrollPane);

        // The text area should start disabled.
        myTextArea.setEnabled(false);

        // Create the MouseListener that will be used to listen for User Input.
        MouseListener ml = new MouseListener() {

            /**
             * Not needed, but must be present.
             */
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    boolean before = DiscourseAnalysisApplet.nodePanel.isButtonPanelShown();

                DiscourseAnalysisApplet.nodePanel.showButtonPanel(x, y); // show the buttonpanel next to it
                //This part will send the information on the node that was clicked.
                DiscourseAnalysisApplet.buttonPanel.associateClauseAndNode(clickNode.getClause(), clickNode);
                //clickNode.getClause().chooseFocus(true);
                DiscourseAnalysisApplet.buttonPanel.editEnable();
                }
            }

            /**
             * When the text area is "pressed", enable it and give it focus.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                //getting the mouse coordinates when pressed
                //this is used to calculate the change in the x and y values
                //and the allow for repositioning of nodes based on the change in the values
                oldMouseX = e.getX();
                oldMouseY = e.getY();

                //setting the last known correct coordinates for the node
                //this is used to reset the node
                lastX = x;
                lastY = y;
            }

            /**
             * Not needed, but must be present.
             */
            @Override
            public void mouseReleased(MouseEvent e){
                resetDrag();
                resetNode();
                updateClauseBounds();
                resetChildren(myNode);            	
            }

            /**
             * Not needed, but must be present.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
            }

            /**
             * Not needed, but must be present.
             */
            @Override
            public void mouseExited(MouseEvent e) {
            }
        };

        // Add click to enable on the text area and panel.
        addMouseListener(ml);
        myTextArea.addMouseListener(ml);
        
        MouseMotionListener mouseMotion = new MouseMotionListener(){
        	@Override
        	public void mouseDragged(MouseEvent event) {
        		beingDragged = true;
        		setRoot(DiscourseAnalysisApplet.root);
        		tree = DiscourseAnalysisApplet.tree;
        		if(SwingUtilities.isLeftMouseButton(event)){
        			dX = event.getX() - oldMouseX;
            		dY = event.getY() - oldMouseY;
            		
            		if(doneDragging == false){
            			x = x + dX;
            			y = y + dY;            		           		
            			updateClauseBounds();            			
            		}
        			//if there is a collision, group or merge the node
        			//and pop up the button panel
        			Enumeration nodeList = root.preorderEnumeration(); // Get a list of Nodes.        			
        			while (nodeList.hasMoreElements()){        				
        	    		XMLTreeNode currentNode = (XMLTreeNode) nodeList.nextElement();
        	    		//checking to see if the currentNode is the same one we are dragging
        	    		if(sameClause(currentNode)){
        	    			if(myNode.getChildCount() > 0 && doneDragging == false){        	    				
        	    				//System.out.println("Trying to drag all my children. Not the soap opera.");
        	    				followParent(myNode);        	    				
                    			lastStateSet = true;
        	    			}
        	    		}
                		        	    		       	    		
        	    		if(intersects(currentNode.getClause())){
            				//System.out.println("There was a collision!");
        	    			//trying to drag children nodes with        	    			
        	    			
            				try{
	            				tree.groupNodes(currentNode, myNode);
	            				//changing the dragged node's x and y
	            				//if it is the only child, place it after the parent
	            				//and preventing more dragging until the mouse is released
	            				if(currentNode.getChildCount() == 1 && doneDragging == false){
	            					//preventing more dragging until the mouse is released
	            					doneDragging = true;	            					
	            					myNode.setX(currentNode.getX()+ WIDTH + BUFFER);
	            					myNode.setY(currentNode.getY());
	            					//used to reset the node
	            					lastX = x;
	            	            	lastY = y;	            	            	
	            				}
	            				//changing the dragged node's x and y
	            				//if it is not the only child, get the last child's x and y
	            				//and preventing more dragging until the mouse is released
	            				
	            				else if(currentNode.getChildCount() > 1 && doneDragging == false){	            					
	            					doneDragging = true;
	            					XMLTreeNode parentOfMyNode = (XMLTreeNode)myNode.getParent();
	            					Enumeration children =  parentOfMyNode.children();
	            					while(children.hasMoreElements()){
	            						XMLTreeNode currentChild = (XMLTreeNode) children.nextElement();	            						
	            						int distanceFromParent = currentChild.getLevel() - parentOfMyNode.getLevel();
	            						//System.out.println("Current child's distance from parent - "+ distanceFromParent);
	            						if(currentChild != myNode && distanceFromParent == 1){
	            							if(currentNode.getChildCount() <= 2){		            							
		            							currentChild.setX(currentChild.getX() + 40);
		            							currentChild.setY(currentChild.getY() - 59);
		            							currentChild.getClause().updateClauseBounds();
	            							}
	            							else if(currentNode.getChildCount() == 3){	            								
		            							currentChild.setX(currentChild.getX());
		            							currentChild.setY(currentChild.getY() - 76);
		            							currentChild.getClause().updateClauseBounds();
	            							}
	            							else{	            								
		            							currentChild.setX(currentChild.getX());
		            							currentChild.setY(currentChild.getY() - 68);
		            							currentChild.getClause().updateClauseBounds();
	            							}
	            						}
	            					}
	            					if(parentOfMyNode.getChildCount() == 2){
		            					XMLTreeNode previousChild = (XMLTreeNode)parentOfMyNode.getFirstChild();	            					
		            					myNode.setX(previousChild.getX());
		            					myNode.setY(previousChild.getY() + HEIGHT + BUFFER);
		            					//used to reset the node
		            					lastX = x;
		            	            	lastY = y;
		            					//System.out.println("Last Child X -" + previousChild.getX());
		            					//System.out.println("Last Child Y -" + previousChild.getY());
		            					//System.out.println("Last Child Y plus H and buffer -" + previousChild.getY() + HEIGHT + BUFFER);
	            					}
	            					else{
	            						XMLTreeNode previousChild = (XMLTreeNode)parentOfMyNode.getChildAt(parentOfMyNode.getChildCount() - 2);	            					
		            					myNode.setX(previousChild.getX());
		            					myNode.setY(previousChild.getY() + HEIGHT + BUFFER);
		            					//used to reset the node
		            					lastX = x;
		            	            	lastY = y;
		            					//System.out.println("Last Child X -" + previousChild.getX());
		            					//System.out.println("Last Child Y -" + previousChild.getY());
		            					//System.out.println("Last Child Y plus H and buffer -" + previousChild.getY() + HEIGHT + BUFFER);
	            					}
	            				}
	            				//updating the clause bounds after the x and y update
	            				updateClauseBounds();	            				
            				}
            				catch(IllegalArgumentException ex){
            					//if the dragged node is an ancestor of the destination node
            					//reset the dragged node
            					System.out.println("There was a problem.");
            					doneDragging = true;
            					resetNode();
            					updateClauseBounds();            					
            				}            				            				
            			}        	    		       	    		        	    		
        	    	}
        		}        		
        	}
        	/**
             * Not needed, but must be present.
             */
        	@Override
        	public void mouseMoved(MouseEvent e) {}
        };
        
        //adding a mouse motion listener to detect dragging
        addMouseMotionListener(mouseMotion);
        myTextArea.addMouseMotionListener(mouseMotion);
        
        // When focus is lost, disable the text area.
        myTextArea.addFocusListener(new FocusListener() {

            /**
             * Not needed, but must be present.
             */
            @Override
            public void focusGained(FocusEvent e) {
                myTextArea.setEnabled(true);
            }

            /**
             * When focus is lost, disable the text area.
             */
            @Override
            public void focusLost(FocusEvent e) {
                myTextArea.setEnabled(false);
                DiscourseAnalysisApplet.nodePanel.hideButtonPanel();
            }
        });
    }
    
    /**
     * This checks to see if there is a collision between this clause
     * and another node.
     */
    
    private boolean intersects(Clause currentNode){
    	//if it isn't this clause check for intersection
    	if(currentNode != this){
	        Area areaA = new Area(this.getBounds());
	        //System.out.println(this.getBounds());
	        Area areaB = new Area(currentNode.getBounds());
	
	        return areaA.intersects(areaB.getBounds2D());
    	}
    	else{
    		//if it is this clause return false
    		//System.out.println("It's the same node!");
    		return false;
    	}
    }
    private boolean sameClause(XMLTreeNode node){
    	if(node.getClause() == this){
    		myNode = node;
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    private void followParent(XMLTreeNode node){
    	//makes all children of a dragged node be dragged also
    	Enumeration children =  node.children();
		while(children.hasMoreElements()){                    				
			XMLTreeNode currentChild = (XMLTreeNode) children.nextElement();
			//setting lastX and lastY, only once per mouse drag			
			followParent(currentChild);
			if(lastStateSet == false){
				currentChild.setLastX(currentChild.getX());
				currentChild.setLastY(currentChild.getY());
			}			
			currentChild.setX(currentChild.getX() + dX);
			currentChild.setY(currentChild.getY() + dY);           		           		
			currentChild.getClause().updateClauseBounds();
		}
    }
    private void resetChildren(XMLTreeNode node){
    	//this function resets all children nodes
    	//of a dragged node
    	Enumeration children =  node.children();
		while(children.hasMoreElements()){                    				
			XMLTreeNode currentChild = (XMLTreeNode) children.nextElement();
			resetChildren(currentChild);					   				
			currentChild.setX(currentChild.getLastX());
			currentChild.setY(currentChild.getLastY());           		           		
			currentChild.getClause().updateClauseBounds();
		}
    }
    
    /**
     * This repositions the Clause in the NodePanel. This is called after
     * updating.
     */
    public void updateClauseBounds() {
        setBounds(x, y, WIDTH, HEIGHT);
    }
    
    private void setRoot(XMLTreeNode root){
    	this.root = root;    	
    }
    
    //Will return the text area (used for when the user makes edits to the text area
    public String getTextArea(){
        return myTextArea.getText();        
    }
    
    //This is only used when they hit cancel button, it will revert the changes (can be used for other 
    //things but that is the only current used so far
    public void setTextArea(String c){
        myTextArea.setText(data);
    }

    //AiDS
    public JTextPane getJTextPane(){return myTextArea;}
    
    /**
     * Returns the XML of this Clause. Possible conjunctions are not yet taken
     * into account.
     *
     * @return result a String that has XML tags and clause data
     */
    public String toXML() {
        String result = "";
        result += "<clause><conj>" + conj + "</conj><text chapter=\"" + chap + "\" verse=\"" + vrse + ">" + data + "</text></clause>";
        return result;
    }
    
    //Function to enable or disable focus since it was requested that the area not be editable 
    //until they hit the "Edit" button. So it will only be focused when edit is enabled or 
    //cancel reverts it back to dormant state
    public void chooseFocus(boolean a){
        myTextArea.setEnabled(a);
    }

    /**
     * This returns the string representation of the Clause.
     *
     * @return a String representing the Clause
     */
    @Override
    public String toString() {
        if (data.equals("root")) {
            return conj;
        } else {
            String result = "";
            result += chap + ":" + vrse + " Conj: " + conj + " Text: " + data;
            return result;
        }
    }
    
    //Function used to return the clicked node.
   public XMLTreeNode getClicked(){
        return clickNode;        
    }

    /**
     * @return String text of the Clause
     */
    public String getData() {
        return data;
    }

    /**
     * @return String conjunction of the Clause
     */
    public String getConj() {
        return conj;
    }

    /**
     * @return String chapter of the Clause
     */
    public String getChap() {
        return chap;
    }

    /**
     * @return String verse of the Clause
     */
    public String getVrse() {
        return vrse;
    }

    /**
     * @return int x value of Clause
     */
    @Override
    public int getX() {
        return x;
    }
    public int getLastX(){
    	return lastX;
    }

    /**
     * @return int y value of Clause
     */
    @Override
    public int getY() {
        return y;
    }
    public int getLastY(){
    	return lastY;
    }
    public boolean getDoneDragging(){
    	return doneDragging;
    }
    public boolean getBeingDragged(){
    	return beingDragged;
    }
    
    private void resetDrag(){
    	doneDragging = false;
    	beingDragged = false;
    	lastStateSet = false;
    }
    private void resetNode(){
    	x = lastX;
    	y = lastY;
    }

    /**
     * @param d	sets data equal to d
     */
    public void setData(String d) {
        data = d;
    }

    /**
     * @param c sets conj equal to c
     */
    public void setConj(String c) {
        conj = c;
    }

    /**
     * @param c sets chap equal to c
     */
    public void setChap(String c) {
        chap = c;
    }

    /**
     * @param v sets vrse equal to v
     */
    public void setVrse(String v) {
        vrse = v;
    }

    /**
     * @param i sets x equal to i
     */
    public void setX(int i) {
        x = i;
    }
    public void setLastX(int i){
    	lastX = i;
    }

    /**
     * @param i sets y equal to i
     */
    public void setY(int i) {
        y = i;
    }
    public void setLastY(int i){
    	lastY = i;
    }    
}