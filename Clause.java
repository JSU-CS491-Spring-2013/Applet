
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.*;

/**
 * A Clause object is composed of text, conjunction, chapter, and verse. It is
 * also a JPanel with a JTextArea in it.
 */
public class Clause extends JPanel {

    private String data;                // The text of the Clause
    private String conj;                // The conjunction of the Clause
    private String chap;                // The chapter number of the Clause
    private String vrse;                // The verse number of the Clause
    private int x;                      // The starting x-value for drawing
    private int y;                      // The starting y-value for drawing
    private JTextPane myTextArea;       // This is where I will show my data.
    private JScrollPane myScrollPane;   // This allows the User to scroll through the text area.
    private NodePanel myNodePanel;      // This is the NodePanel this Clause is on.
    public XMLTreeNode clickNode;       // Information on the node for the button panel to use.
    
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
            public void mouseClicked(MouseEvent e) 
            {    
                try 
                {
                    if(e.getClickCount() == 2)
                    {
                        boolean before = DiscourseAnalysisApplet.nodePanel.isButtonPanelShown();

                        DiscourseAnalysisApplet.nodePanel.showButtonPanel(x, y); // show the buttonpanel next to it
                        //This part will send the information on the node that was clicked.
                        DiscourseAnalysisApplet.buttonPanel.associateClauseAndNode(clickNode.getClause(), clickNode);
                        //clickNode.getClause().chooseFocus(true);
                        DiscourseAnalysisApplet.buttonPanel.editEnable();
                    }
                }

                catch (Exception r) 
                {
                    DiscourseAnalysisApplet.nodePanel.hideButtonPanel();
                }
            }

            /**
             * When the text area is "pressed", enable it and give it focus.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                /*boolean before = DiscourseAnalysisApplet.nodePanel.isButtonPanelShown();

                DiscourseAnalysisApplet.nodePanel.showButtonPanel(x, y); // show the buttonpanel next to it
                //This part will send the information on the node that was clicked.
                DiscourseAnalysisApplet.buttonPanel.1ateClauseAndNode(clickNode.getClause(), clickNode);
		*/                
            }
                  


            /**
             * Not needed, but must be present.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
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
     * This repositions the Clause in the NodePanel. This is called after
     * updating.
     */
    public void updateClauseBounds() {
        setBounds(x, y, 260, 95);
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

    /**
     * @return int y value of Clause
     */
    @Override
    public int getY() {
        return y;
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

    /**
     * @param i sets y equal to i
     */
    public void setY(int i) {
        y = i;
    }
    
    //AiDS
    /**Splits a single node into two separate nodes by creating a new XMLTreeNode with the same chapter
     * and verse as the selectedNode, and inserting the new node into the selectedNode's parent.
     * @param selectedNode		original node that will be split
     * @param dataSelected		the data for the selectedNode after the split has occurred
     * @param newData			the data for the new XMLTreeNode
     * @param newConj			the conjunction for the new XMLTreeNode
     */
    /*public void split(XMLTreeNode selectedNode, String dataSelected){
        int modifier = dataSelected.lastIndexOf(":")+2;//manditory shift to correct for verse and conjunction
        String newConj = "x";//selectedText.substring(0,selectedText.indexOf(' '));//AiDS
        int tCurser = myTextArea.getCaretPosition() + modifier;//finds the location of the Caret/cursor for split
        String firstData = dataSelected.substring(modifier,tCurser);//AiDS
        String secondData = dataSelected.substring(tCurser);//AiDS
        //make new node using the same chapter and verse as the selected node, but
        //using the newData and newConj
        XMLTreeNode secondNode = new XMLTreeNode(new Clause(secondData, newConj, selectedNode.getChap(), selectedNode.getVrse()));
        XMLTreeNode firstNode = new XMLTreeNode(new Clause(firstData, selectedNode.getConj(), selectedNode.getChap(), selectedNode.getVrse()));
        //set the data for the new node
        secondNode.setData(secondData);
        XMLTreeModel.setNodeData(secondNode, secondData);
        firstNode.setData(firstData);
        XMLTreeModel.setNodeData(firstNode, firstData);
        
        
        //get the parent of the selected node
        XMLTreeNode parent = (XMLTreeNode) selectedNode.getParent();
        //get the index for the selected node
        int x = parent.getIndex(selectedNode);

        //add the newNode right after the selectedNode
        parent.insert(firstNode, x + 1);
        NodePanel firstSplitNode = new NodePanel(firstNode, (parent.getX() + 40), parent.getY());
        //DiscourseAnalysisApplet.getXMLTreeModel().add();
        parent.insert(secondNode, x + 2);
        NodePanel secondSplitNode = new NodePanel(secondNode, (parent.getX() + 80), parent.getY());
        System.out.println("first clause:   " + firstData);//AiDS stub
        System.out.println("second clause:  " + secondData);//AiDS stub
        parent.remove(x);
    }*/
    
}