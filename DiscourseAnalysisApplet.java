
import java.applet.Applet;
import java.util.Calendar;
import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.*;
import org.xml.sax.XMLReader;

/**
 * DiscourseAnalysisApplet reads an XML file and builds an XMLTreeModel. The XMLTreeModel is
 * used to build both a JTreePanel and a NodePanel. DiscourseAnalysisApplet also builds a,
 * now exterior, ButtonPanel.
 */
public class DiscourseAnalysisApplet extends Applet {

    private JTreePanel jTreePanel;      // the panel on the left that does....what does that thing do?
    private ButtonPanel buttonPanel;    // the panel on the right that contains the buttons
    private XMLTreeModel treeModel;     // the tree that contains all data?
    private NodePanel nodePanel;        // the main panel (in the middle) that contains the tree

    @Override
    public void init() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }

        this.setLayout(null); // This allows the setBounds method to work.
        treeModel = makeTreeModel();

        // Make the panels.
        buttonPanel = new ButtonPanel(this);
        buttonPanel.setBounds(1050, 0, 256, 700);
        nodePanel = new NodePanel((XMLTreeNode) treeModel.getRoot(), treeModel.getXMax(), treeModel.getYMax());
        JScrollPane s = new JScrollPane(nodePanel);
        s.setBounds(251, 0, 798, 700);
        
        // Make scrolling faster
        s.getVerticalScrollBar().setUnitIncrement(64);
        s.getHorizontalScrollBar().setUnitIncrement(32);
        jTreePanel = new JTreePanel(treeModel, nodePanel);
        jTreePanel.setBounds(0, 0, 250, 700); // Redundant? Why is this here, and which is correct?

        // Add the components to the window.
        add(jTreePanel);
        add(buttonPanel);
        add(s);
    }
    
    public static XMLTreeNode root;

    /**
     * Returns the panel that contains the tree.
     *
     * @return the NodePanel used
     */
    public NodePanel getNodePanel() {
        return nodePanel;
    }

    /**
     * Sets up the parser, gets the root and builds the XMLTreeModel
     *
     * @return XMLTreeModel
     */
    public XMLTreeModel makeTreeModel() {
        try {
            /*
            // These are the tools we need to build a Document.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();*/

            // Create a Document based on the XML file the User provides and removes "extraneous" information.
            // For example:  <book bookName="Luke 1"> is converted to null, null, "Luke 1"
            // Document doc = db.parse("/home/tyler/NetBeansProjects/CS 491 Applet/Luke 1.xml");	//Put the URL in the parse method call
            javax.swing.JFileChooser chooseFile = new javax.swing.JFileChooser();
            chooseFile.showOpenDialog(null);
            
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new XMLHandler());
            xmlReader.parse("/home/tyler/NetBeansProjects/CS 491 Applet/Luke 1.xml");
            
            // Get the time after the User selects the XML file, but before any calculations start.
            Calendar before = Calendar.getInstance();
            
            /*Document doc = db.parse(chooseFile.getSelectedFile());
            doc.getDocumentElement().normalize();

            // Point to the root element of the document portion of the Document.
            Node root = doc.getDocumentElement();

            // Get a list of every <clause> in the Document. These are never used, though.
            NodeList clause = doc.getElementsByTagName("clause");
            Node temp = clause.item(0);

            // Create the root node and set it to the bookName tag.
            XMLTreeNode rootX = new XMLTreeNode(new Clause("root", ((Element) root).getAttribute("bookName").toString(), "", ""));

            // Add child nodes to the root node.
            makeNodes(rootX, root);*/

            // Make the tree, and send it back.
            XMLTreeModel tree = new XMLTreeModel(root);
            
            // Get the time after the calculations are done, and display how many milliseconds the calculations took.
            Calendar after = Calendar.getInstance();
            System.out.println("Milliseconds to make tree:  " + (after.getTimeInMillis() - before.getTimeInMillis()));
            
            return tree;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extracts the data from the Document Node to make a new Clause object
     *
     * @return Clause
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
                } // If the text tag is found, pull out the chapter, verse, and the actual text.
                else if (clauses.item(i).getNodeName().equals("text")) {
                    d = (Element) clauses.item(i);
                    NamedNodeMap attr = d.getAttributes();
                    Text da = (Text) d.getFirstChild();
                    data = da.getNodeValue();
                    verse = attr.getNamedItem("verse").getNodeValue();
                    chapter = attr.getNamedItem("chapter").getNodeValue();
                } // Unused else statement.
                else {
                }
            }
        }

        return new Clause(data, conj, chapter, verse);
    }

    /**
     * Navigates the Document and adds children to the
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
     * This method performs one of 5 functions. This method is a nightmare, and
     * it should be split into the five separate functions if the rest of their
     * code makes that possible.
     *
     * 0: remove selected node 1: merge the selected node with the node right
     * below it 2: group starting at the selected node 3: split the selected
     * node 4: set the data of the ClausePanel
     *
     * @param co
     */
    public void performFunction(int co) {
        /*XMLTreeNode xtn = nodePanel.getSelected();
        switch (co) {
        case 0: // remove selected node
        treeModel.remove(xtn);
        break;
        case 1: // merge the selected node with the node right below it
        treeModel.merge(xtn, buttonPanel.n.getConj(), buttonPanel.n.getData(), true);
        break;
        case 2: // group starting at the selected node
        XMLTreeNode parent = (XMLTreeNode) xtn.getParent();
        int value = buttonPanel.getComboBoxSelectedIndex() + 1;
        XMLTreeNode[] nArray = new XMLTreeNode[value + 1];
        nArray[0] = xtn;
        int startIndex = parent.getIndex(xtn);
        for (int i = 1; i < nArray.length; i++) {
        nArray[i] = (XMLTreeNode) treeModel.getChild(parent, startIndex + (i));
        }
        XMLTreeNode data = new XMLTreeNode(new Clause(buttonPanel.n.getData(), buttonPanel.n.getConj(), buttonPanel.n.getChap(), buttonPanel.n.getVrse()));
        treeModel.groupNodes(data, nArray);
        
        break;
        case 3: // split the selected node
        treeModel.split(xtn, buttonPanel.sel.getData(), buttonPanel.n.getData(), buttonPanel.n.getConj());
        break;
        case 4: // set the data of the ClausePanel
        xtn.setChap(buttonPanel.sel.getChap());
        xtn.setVrse(buttonPanel.sel.getVrse());
        xtn.setConj(buttonPanel.sel.getConj());
        xtn.setData(buttonPanel.sel.getData());
        break;
        }
        
        // Refreshes the treeModel
        treeModel.reload();
        
        // Reset the x and y values for the XMLTreeNodes
        treeModel.resetXY();
        
        // Update the nodePanel
        nodePanel.setRoot((XMLTreeNode) treeModel.getRoot(), treeModel.getXMax(), treeModel.getYMax());
        
        // Update the jTreePanel
        jTreePanel.setTreeModel(treeModel);
        
        // Validate the GUI components in the JTreePanel
        jTreePanel.validate();*/
    }
}