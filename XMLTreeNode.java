import javax.swing.tree.DefaultMutableTreeNode;

/**
 * XMLTreeNode extends the DefaultMutableTreeNode, and contains an 
 * Object data of type Clause
 */
public class XMLTreeNode extends DefaultMutableTreeNode {

    private Clause clause;

    /**
     * Constructor makes a new XMLTreeNode that does not have a parent or children.
     * @param c	sets the Nodes clause object equal to c.
     */
    public XMLTreeNode(Clause c) {
        clause = c;
    }

    //Getters
    /**
     * @return clause of this XMLTreeNode
     */
    public Clause getClause() {
        return clause;
    }

    /**
     * @return String data of the Clause stored in this XMLTreeNode
     */
    public String getData() {
        return clause.getData();
    }

    /**
     * @return String conjunction of the Clause stored in this XMLTreeNode
     */
    public String getConj() {
        return clause.getConj();
    }

    /**
     * @return String chapter of the Clause stored in this XMLTreeNode
     */
    public String getChap() {
        return clause.getChap();
    }

    /**
     * @return String verse of the Clause stored in this XMLTreeNode
     */
    public String getVrse() {
        return clause.getVrse();
    }

    /**
     * @return int value of x in the Clause stored in this XMLTreeNode
     */
    public int getX() {
        return clause.getX();
    }
    public int getLastX() {
        return clause.getLastX();
    }

    /**
     * @return int value of y in the Clause stored in this XMLTreeNode
     */
    public int getY() {
        return clause.getY();
    }
    public int getLastY() {
        return clause.getLastY();
    }
    public boolean getDoneDragging(){
    	return clause.getDoneDragging();
    }
    public boolean getBeingDragged(){
    	return clause.getBeingDragged();
    }

    //Setters
    /**
     * @param c sets the Clause Object in this XMLTreeNode equal to c
     */
    public void setClause(Clause c) {
        clause = c;
    }

    /**
     * @param d sets the data of the Clause stored in this XMLTreeNode equal to d
     */
    public void setData(String d) {
        clause.setData(d);
    }

    /**
     * @param c sets the conjunction of the Clause stored in this XMLTreeNode equal to c
     */
    public void setConj(String c) {
        clause.setConj(c);
    }

    /**
     * @param c sets the chapter of the Clause stored in this XMLTreeNode equal to c
     */
    public void setChap(String c) {
        clause.setChap(c);
    }

    /**
     * @param v sets the verse of the Clause stored in this XMLTreeNode equal to v
     */
    public void setVrse(String v) {
        clause.setVrse(v);
    }

    /**
     * @param i sets the x value of the Clause stored in this XMLTreeNode equal to i
     */
    public void setX(int i) {
        clause.setX(i);
    }
    public void setLastX(int i) {
        clause.setLastX(i);
    }

    /**
     * @param i sets the y value of the Clause stored in this XMLTreeNode equal to i
     */
    public void setY(int i) {
        clause.setY(i);
    }
    public void setLastY(int i) {
        clause.setLastY(i);
    }

    /**
     * @return String the String representation of this object
     */
    public String toString() {
        return clause.toString();
    }

    /**
     * @return String the XML representation of this object
     */
    public String toXML() {
        return clause.toXML();
    }
}