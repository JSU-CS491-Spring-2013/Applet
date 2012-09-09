
import java.applet.Applet;
import java.awt.*; // Not used
import java.awt.event.*;
import javax.swing.*;
import java.lang.*; // Not used
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
The TestApplet class reads an XML file and 
builds an XMLTreeModel. The XMLTreeModel
is used to build a JTreePanel and NodePanel.
The TestApplet class also builds an interior 
ButtonPanel class.
 */
public class TestApplet extends Applet {

    private JTreePanel jTreePanel;
    private ButtonPanel buttonPanel;
    private XMLTreeModel treeModel;
    private NodePanel nodePanel;
    private Timer time;

    public void init() {
        this.setLayout(null);
        treeModel = makeTreeModel();
        jTreePanel = new JTreePanel(treeModel);
        jTreePanel.setBounds(0, 0, 250, 700); // Redundant? Which is correct?
        buttonPanel = new ButtonPanel(0, this);
        buttonPanel.setBounds(1050, 0, 150, 700);
        nodePanel = new NodePanel((XMLTreeNode) treeModel.getRoot(), treeModel.getXMax(), treeModel.getYMax());
        JScrollPane s = new JScrollPane(nodePanel);
        s.setBounds(251, 0, 798, 700);
        int delay = 50;
        time = new Timer(delay, new ActionListener() {

            public void actionPerformed(ActionEvent timer) {
                buttonPanel.setSelected(nodePanel.getSelected());
            }
        });
        time.start();
        add(jTreePanel);
        add(buttonPanel);
        add(s);
    }

    public NodePanel getNodePanel() {
        return nodePanel;
    }

    /**
    Sets up the parser, gets the root and builds the XMLTreeModel
    @return XMLTreeModel
     */
    public XMLTreeModel makeTreeModel() {
        try {
            // These are the tools we need to build a Document.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Create a Document based on the XML file the User provides and removes "extraneous" information.
            // For example:  <book bookName="Luke 1"> is converted to null, null, "Luke 1"
            Document doc = db.parse("C:/Users/Tyler/Documents/NetBeansProjects/CS 491 Applet/Luke 1.xml");	//Put the URL in the parse method call
            doc.getDocumentElement().normalize();

            // Point to the root element of the document portion of the Document.
            Node root = doc.getDocumentElement();

            // Get a list of every <clause> in the Document.
            NodeList clause = doc.getElementsByTagName("clause");

            // Unused variable.
            Node temp = clause.item(0);

            // Create the root node and set it to the bookName tag.
            XMLTreeNode rootX = new XMLTreeNode(new Clause("root", ((Element) root).getAttribute("bookName").toString(), "", ""));
            
            // Add child nodes to the root node.
            makeNodes(rootX, root);
            
            /*System.out.println(((XMLTreeNode) rootX).toString());
            System.out.println(((XMLTreeNode) rootX).getChildCount());
            for (int i = 0; i < rootX.getChildCount(); i++) {
                System.out.println("\timer" + ((XMLTreeNode) rootX).getChildAt(i).toString());
            }
            printNodes(rootX);*/
            
            // Make the tree, and send it back.
            XMLTreeModel tree = new XMLTreeModel(rootX);
            return tree;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
    Extracts the data from the Document Node to make a new Clause object
    @return Clause
     */
    public Clause makeClause(Node childElement) {
        //get data, attributes, conjuntion
        String data, chapter, verse, conj;
        Element d, c;
        data = "";
        chapter = "";
        verse = "";
        conj = "";

        // Because a clause tag was given to this method, get the information it contains (conj and text)
        NodeList clauses = childElement.getChildNodes();

        //System.out.print(childElement.getFirstChild().getNodeName());
        for (int i = 0; i < clauses.getLength(); i++) {
            
            // Check if the text is empty. Why do they put a "1" in everything to check if it's empty?
            // Wouldn't the String.isEmpty() method make more sense?
            String str = clauses.item(i).getTextContent().trim().concat("1");
            if (!str.equals("1")) {
                
                // If the conjunction tag is found, set conj to the conj name.
                if (clauses.item(i).getNodeName().equals("conj") && clauses.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    c = (Element) clauses.item(i);
                    Text x = (Text) c.getFirstChild();
                    conj = x.getNodeValue();
                }
                
                // If the text tag is found, pull out the chapter, verse, and the actual text.
                else if (clauses.item(i).getNodeName().equals("text")) {
                    d = (Element) clauses.item(i);
                    NamedNodeMap attr = d.getAttributes();
                    Text da = (Text) d.getFirstChild();
                    data = da.getNodeValue();
                    verse = attr.getNamedItem("verse").getNodeValue();
                    chapter = attr.getNamedItem("chapter").getNodeValue();
                }
                
                // Unused else statement.
                else {
                }
            }
        }

        return new Clause(data, conj, chapter, verse);
    }

    /**
    Navigates the Document and adds children to the
     */
    public void makeNodes(XMLTreeNode r, Node parent) {
        // Get a list of all the children of the current Document Node
        NodeList childElements = parent.getChildNodes();
        
        // Iterate through the children of the Document Node
        for (int i = 0; i < childElements.getLength(); i++) {
            Node child = childElements.item(i);
            
            // [Unused] Get the value of the current child Node
            String s = child.getNodeValue();
            
            // Get the name and content of the current child Node
            String cCheck = child.getNodeName();
            String str = child.getTextContent().trim().concat("1");

            // If the current Node is not empty and is a clause, add its information
            // Again with the "1" thing...
            if (!str.equals("1") && cCheck.equals("clause")) {
                // Make a new XMLTreeNode
                XMLTreeNode childTreeNode = new XMLTreeNode(makeClause(child));
                
                // Add this new XMLTreeNode to the current XMLTreeNode parent.
                r.add(childTreeNode);
                
                // Check to see if the current clause tag has any nested clause tags.
                makeNodes(childTreeNode, child);
            }
        }
    }

    /**
    Performs one of the following functions:
    0: remove selected node
    1: merge the selected node with the node right below it
    2: group starting at the selected node
    3: split the selected node 
    4: set the data of the ClausePanel
     */
    public void performFunction(int co) {
        XMLTreeNode xtn = nodePanel.getSelected();
        switch (co) {
            case 0:
                treeModel.remove(xtn);
                break;
            case 1:
                treeModel.merge(xtn, buttonPanel.n.getConj(), buttonPanel.n.getData(), true);
                break;
            case 2:
                XMLTreeNode parent = (XMLTreeNode) xtn.getParent();
                int value = buttonPanel.superSize.getSelectedIndex() + 1;
                XMLTreeNode[] nArray = new XMLTreeNode[value + 1];
                nArray[0] = xtn;
                int startIndex = parent.getIndex(xtn);
                for (int i = 1; i < nArray.length; i++) {
                    nArray[i] = (XMLTreeNode) treeModel.getChild(parent, startIndex + (i));
                }
                XMLTreeNode data = new XMLTreeNode(new Clause(buttonPanel.n.getData(), buttonPanel.n.getConj(), buttonPanel.n.getChap(), buttonPanel.n.getVrse()));
                treeModel.groupNodes(data, nArray);

                break;
            case 3:
                treeModel.split(xtn, buttonPanel.sel.getData(), buttonPanel.n.getData(), buttonPanel.n.getConj());
                break;
            case 4:

                xtn.setChap(buttonPanel.sel.getChap());
                xtn.setVrse(buttonPanel.sel.getVrse());
                xtn.setConj(buttonPanel.sel.getConj());
                xtn.setData(buttonPanel.sel.getData());
                break;
        }
        treeModel.reload();		//refreshes the treemodel
        treeModel.resetXY();	//reset the x and y values for the XMLTreeNodes
        //update the nodePanel
        nodePanel.setRoot((XMLTreeNode) treeModel.getRoot(), treeModel.getXMax(), treeModel.getYMax());
        jTreePanel.setTreeModel(treeModel);  //update the jTreePanel
        jTreePanel.validate();		//validate the GUI components in the JTreePanel		
    }
}