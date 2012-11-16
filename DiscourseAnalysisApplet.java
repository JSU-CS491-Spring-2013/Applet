
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

/**
 * DiscourseAnalysisApplet reads an XML file and builds an XMLTreeModel. The
 * XMLTreeModel is used to build both a JTreePanel and a NodePanel.
 * DiscourseAnalysisApplet also builds a, now exterior, ButtonPanel.
 */
public class DiscourseAnalysisApplet extends JApplet {

    private JTreePanel jTreePanel;              // the panel on the left that does....what does that thing do?
    public static ButtonPanel buttonPanel;            // the panel on the right that contains the buttons
    private XMLTreeModel treeModel;             // the tree that contains all data?
    public static NodePanel nodePanel;          // the main panel (in the middle) that contains the tree
    private ProgressBarDialogBox myProgress;    // a dialog box that show the user that the data is loading
    public static ArrayList<String> conjunctions;
    private JMenuItem saveXML, saveImg;
    private JMenu menu;
    private JMenuBar bar;
    private XMLTreeModel tree;
    private JFileChooser chooseFile;
    

    // DEBUG INFORMATION - DELETE LATER
    Calendar before;
    
    @Override
    public void init() {
        
        //Look and feel change, we liked it but it was not able to be implemented well, and caused issues.
        /*try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }*/

        String xmlURL = getParameter("xmlURL");
        // xmlURL = "http://127.0.0.1/Luke 1 - Shorter.xml"; This has it pull from a local server. You need to have the file in the base directory of the server (LAMP, MAMP, or WAMP).
        if (xmlURL == null) {
            treeModel = makeTreeModel();
        } else {
            treeModel = makeTreeModel(xmlURL);
        }

        this.setLayout(null); // This allows the setBounds method to work.

        // Make the panels.
        buttonPanel = new ButtonPanel();
        buttonPanel.setBounds(10, 10, 249, 150);
        buttonPanel.setEnabled(false);
        buttonPanel.setVisible(false);
        nodePanel = new NodePanel(root, treeModel.getXMax(), treeModel.getYMax());
        JScrollPane s = new JScrollPane(nodePanel);
        s.setBounds(250, 0, 1000, 700);

        // Make scrolling faster
        s.getVerticalScrollBar().setUnitIncrement(64);
        s.getHorizontalScrollBar().setUnitIncrement(32);
        jTreePanel = new JTreePanel(treeModel, nodePanel);
        jTreePanel.setBounds(0, 0, 250, 700); // Redundant? Why is this here, and which is correct?

        nodePanel.add(buttonPanel, JLayeredPane.POPUP_LAYER);

        // Making the Menu items and their functionality 
        saveXML = new JMenuItem("Save XML");
        saveImg = new JMenuItem("Save as Image");
        //Adding functionality to the "Save" selection
        saveXML.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //Gets the original File name
                String newFileName;
                //This will create a new pop-up that will allow the user to specify a file name
                chooseFile = null;
                chooseFile = new JFileChooser();

                //The filechooser doesn't allow you to leave it blank and save, but I put this in just in case
                //chooseFile.showSaveDialog(null);
                int returnVal = chooseFile.showSaveDialog(null);
                if(returnVal == JFileChooser.CANCEL_OPTION){}
                else{
                    if(chooseFile.getSelectedFile().toString().equals("")){
                        newFileName = "DiscourseAnalysisTempFile.xml";
                    }
                    else
                        //This will now check to see if the file name the user chose contains .xml tag, if not then it will add it
                        newFileName = chooseFile.getSelectedFile().toString();
                    if(!newFileName.contains(".xml"))
                        newFileName += ".xml";
                    //Calls the save class passing it the new filepath and the tree as it is.
                    XMLConverter xml = new XMLConverter(newFileName, tree);
                }
            }
        });
        
        saveImg.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //This will create a new pop-up that will allow the user to specify a file name
                JOptionPane.showMessageDialog(null, "All Images are saved as .png", "Save Warning", JOptionPane.INFORMATION_MESSAGE);
                chooseFile = null;
                chooseFile = new JFileChooser();
                String newFileName = "";
                //The filechooser doesn't allow you to leave it blank and save, but I put this in just in case
                //chooseFile.showSaveDialog(null);
                
                int returnVal = chooseFile.showSaveDialog(null);
                if(returnVal == JFileChooser.CANCEL_OPTION){}
                else{
                    if(chooseFile.getSelectedFile().toString().equals("")){
                        newFileName = "DiscourseAnalysisImage.png";
                    }
                    else
                        //This will now check to see if the file name the user chose contains .xml tag, if not then it will add it
                        newFileName = chooseFile.getSelectedFile().toString();
                    if(!newFileName.contains(".png"))
                        newFileName += ".png";

                    // Make an image.
                    BufferedImage img = new BufferedImage(DiscourseAnalysisApplet.nodePanel.getWidth(), DiscourseAnalysisApplet.nodePanel.getHeight(), BufferedImage.TYPE_INT_RGB);

                    // Painstakingly put a pixel of a certain color into every position. Horribly inefficient, but working.
                    for (int i = 0; i < DiscourseAnalysisApplet.nodePanel.getWidth(); i++) {
                        for (int j = 0; j < DiscourseAnalysisApplet.nodePanel.getHeight(); j++) {
                            img.setRGB(i, j, new Color(200, 200, 200).getRGB());
                        }
                    }

                    // Paint the image into the BufferedImage
                    Graphics2D g2d = img.createGraphics();
                    DiscourseAnalysisApplet.nodePanel.paint(g2d);
                    g2d.dispose();

                    // Save it!
                    try {
                        ImageIO.write(img, "png", new File(newFileName));
                        JOptionPane.showMessageDialog(null, "The image file has successfully been saved.", "Successfully saved!", JOptionPane.PLAIN_MESSAGE);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                
            }
        });
        
        //Creates a Menu with "Menu" as a selectable item.
        menu = new JMenu("Menu");
        //Adds the "Save" button to the Menu
        menu.add(saveXML);
        menu.add(saveImg);
        
        add(jTreePanel);
        add(s);
        //Creates a new Menu Bar and adds Menu Items to the bar
        bar = new JMenuBar();
        bar.add(menu);
        //Attaches the Menu to the applet
        setJMenuBar(bar);
        
        
        // DEBUG INFORMATION - DELETE LATER
        Calendar after = Calendar.getInstance();
        System.out.println("Total number of milliseconds since XML file was given:  " + (after.getTimeInMillis() - before.getTimeInMillis()));

        // If the dialog box is still there, get rid of it.
        if (myProgress != null) {
            myProgress.dispose();
            myProgress = null;
        }
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

    public XMLTreeModel makeTreeModel() {
        // Give the User the ability to choose which XML file he/she wants to use.
        chooseFile = new javax.swing.JFileChooser();
        chooseFile.showOpenDialog(null);
        return makeTreeModel(chooseFile.getSelectedFile().toString());
    }

    /**
     * Sets up the parser, gets the root and builds the XMLTreeModel
     *
     * @return XMLTreeModel
     */
    public XMLTreeModel makeTreeModel(String path) {
        try {
            // Give the User the ability to choose which XML file he/she wants to use.
            // chooseFile = new javax.swing.JFileChooser();
            // chooseFile.showOpenDialog(null);

            // Create my JDialog that displays the progress bar.
            myProgress = new ProgressBarDialogBox(null, false);

            // Create a SAX parser, and parse the XML file the User provided.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new XMLHandler());

            // DEBUG INFORMATION - DELETE LATER
            before = Calendar.getInstance();

            // After this finishes, the static field, root, will contain the root node of the tree.
            InputSource is = new InputSource(path);
            xmlReader.parse(is);

            // DEBUG INFORMATION - DELETE LATER
            Calendar after = Calendar.getInstance();

            // Make the tree, and send it back.
            tree = new XMLTreeModel(root);

            // DEBUG INFORMATION - Uncomment if you wish
            //System.out.println("Number of milliseconds needed to make tree using SAX parser:  " + (after.getTimeInMillis() - before.getTimeInMillis()));

           
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