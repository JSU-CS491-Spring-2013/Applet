
import javax.swing.tree.DefaultMutableTreeNode;

/**
XMLTreeNode extends the DefaultMutableTreeNode, and contains an 
Object data of type Clause
 */
public class XMLTreeNode extends DefaultMutableTreeNode {

    private Clause clause;

    /**
    Constructor makes a new XMLTreeNode that does not have a parent or children.
    @param c	sets the Nodes clause object equal to c.
     */
    public XMLTreeNode(Clause c) {
        clause = c;
    }

    //Getters
    /**@return clause of this XMLTreeNode */
    public Clause getClause() {
        return clause;
    }

    /**@return String data of the Clause stored in this XMLTreeNode */
    public String getData() {
        return clause.getData();
    }

    /**@return String conjunction of the Clause stored in this XMLTreeNode */
    public String getConj() {
        return clause.getConj();
    }

    /**@return String chapter of the Clause stored in this XMLTreeNode */
    public String getChap() {
        return clause.getChap();
    }

    /**@return String verse of the Clause stored in this XMLTreeNode */
    public String getVrse() {
        return clause.getVrse();
    }

    /**@return int value of x in the Clause stored in this XMLTreeNode */
    public int getX() {
        return clause.getX();
    }

    /**@return int value of y in the Clause stored in this XMLTreeNode */
    public int getY() {
        return clause.getY();
    }

    /**@return String[] word wrap version from the Clause stored in this node(each line is an index of the array is a single line)*/
    public String[] getWordWrap() {
        return clause.getWordWrap();
    }

    //Setters
    /**@param c sets the Clause Object in this XMLTreeNode equal to c */
    public void setClause(Clause c) {
        clause = c;
    }

    /**@param d sets the data of the Clause stored in this XMLTreeNode equal to d */
    public void setData(String d) {
        clause.setData(d);
    }

    /**@param c sets the conjunction of the Clause stored in this XMLTreeNode equal to c*/
    public void setConj(String c) {
        clause.setConj(c);
    }

    /**@param c sets the chapter of the Clause stored in this XMLTreeNode equal to c*/
    public void setChap(String c) {
        clause.setChap(c);
    }

    /**@param v sets the verse of the Clause stored in this XMLTreeNode equal to v*/
    public void setVrse(String v) {
        clause.setVrse(v);
    }

    /**@param i sets the x value of the Clause stored in this XMLTreeNode equal to i*/
    public void setX(int i) {
        clause.setX(i);
    }

    /**@param i sets the y value of the Clause stored in this XMLTreeNode equal to i*/
    public void setY(int i) {
        clause.setY(i);
    }

    /**@return String the String representation of this object */
    public String toString() {
        return clause.toString();
    }

    /**@return String the XML representation of this object */
    public String toXML() {
        return clause.toXML();
    }
    //For Testing this class
	/*
    public static void main(String[] args)
    {
    Clause c = new Clause("This is the root", "and", "1", "1");
    XMLTreeNode root = new XMLTreeNode(c);
    
    //Add some children to root
    for(int i = 0; i < 10; i++)
    {
    String e = new Integer(i).toString();
    Clause temp = new Clause("This child is number "+e, "or", "1", e);
    root.add(new XMLTreeNode(temp));
    }
    
    //Print root
    System.out.println(root.getClause().toString());
    //Print Children
    for(int i = 0; i < root.getChildCount(); i++)
    {
    //root.getChildAt(i) returns an Object, so cast that Object as an XMLTreeNode
    XMLTreeNode temp = (XMLTreeNode)root.getChildAt(i);
    //Get the clause from the XMLTreeNode then call the toString() method
    System.out.println(temp.toString());
    }
    }
     */
}