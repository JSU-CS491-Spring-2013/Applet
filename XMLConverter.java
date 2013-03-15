/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DerekZul
 */
import java.io.*;
import java.util.Enumeration;
import javax.swing.JOptionPane;

public class XMLConverter {
    
    public XMLConverter() {
    }

    /**
     * This is the Default Constructor. It sets up everything needed to write the tree to an XML file.
     * @param fp a String containing the file path of the XML file.
     * @param temp the XMLTreeModel containing the tree
     */
    public XMLConverter(String fp, XMLTreeModel temp) {
        File xmlfile = new File(fp); // Open up a File object that will be written to later. Why is this a space in memory?
        XMLTreeNode root = (XMLTreeNode) temp.getRoot(); // Get the root node of the tree.
        BufferedWriter writer = null; // Declaration and set of writer
        String xmlcode = "<?xml version=\"1.0\" ?>"; 
        try {
            writer = new BufferedWriter(new FileWriter(xmlfile)); // Create our BufferedWriter using the File.
        } catch (IOException e) { }//used to catch error.

        // This is the bit (a 1 or 0? Did you mean block?) where you write your XML to the file.
        try {
            writer.write(xmlcode); // This should contain the string literal "<?xml version=\"1.0\" ?>".
            nodeCycle(writer, root); // Begins the initial call. This will also be initialize a recursive call if needed.
            writer.write("</book>"); // At the end, close the book tag.
            writer.close();//closing the writer after all writing is completed.
            //Shows a message to the user to signify that the file has been saved.
            JOptionPane.showMessageDialog(null, "The file has successfully been1 saved.", "Successfully saved!", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) { }

    }
    //The function that goes through every node of the tree
    final public void nodeCycle(BufferedWriter w, XMLTreeNode temp) { 
        try {
            if (temp.getChildCount() > 1) { // If the passed node has children
                //System.out.println(temp.getData() + "'s child count is: " + temp.getChildCount());
                Clause c = new Clause(); // Temporary clause to hold data from the node passed in.
                c = temp.getClause(); // Assigning the values of the passed node to c
                
                if (c.getData().equals("root")) { // This will only ever happen when the root node is passed in to set the book in XML
                    w.write("<book bookName=\"" + c.getConj() + "\">\n"); // Initial book XML tag
                    
                    // Add potential conjunctions to the beginning of the XML file.
                    for (int i = 0; i < DiscourseAnalysisApplet.conjunctions.size(); i++) {
                        w.write("<pconj>" + DiscourseAnalysisApplet.conjunctions.get(i) + "</pconj>\n");
                    }
                } else {
                    // Write XML tags and data to the file.
                    w.write("<clause>\n");
                    w.write("<conj>" + c.getConj() + "</conj>\n");
                    w.write("<text chapter=\"" + c.getChap() + "\" verse=\"" + c.getVrse() + "\">\n");
                    w.write(c.getData() + "\n");
                    w.write("</text>\n");
                }
                
                //Enumeration e = temp.breadthFirstEnumeration(); // Get a list of Nodes.
                Enumeration e = temp.children();
                try { // Catch element exceptions
                    // While having children begin recursive function call.
                    // Enumerate through the children 
                    XMLTreeNode curr = null;
                    //curr = (XMLTreeNode)e.nextElement();
                    while (e.hasMoreElements()) { // Pass each child to the "writer". This line is wrong. You are losing nodes here, and switching to e.hasMoreElements() gave a StackOverflowError.
                        curr = (XMLTreeNode) e.nextElement(); // Get the next node.
                        //System.out.println(curr.getData() + "HAS A CHILD!");
                        nodeCycle(w, curr); // Begin again.
                        
                    
                    }
                } catch (Exception ex) {System.out.println(ex.toString());
                }

                // If this is not the root node, this will end the clause (this is here due to possible nested nodes)
                if (!c.getData().equals("root")) {
                    w.write("</clause>");
                }

            } else { // Else there are no children.
                // Temp Clause to temporarily store information. Again, merge these into one statement.
                Clause c = new Clause();
                c = temp.getClause();

                // If the node passed in is the root of the tree, this will execute.
                if (c.getData().equals("root")) {
                    w.write("<book bookName=\"" + c.getData() + "\">");
                    
                    // Add potential conjunctions to the beginning of the XML file.
                    for (int i = 0; i < DiscourseAnalysisApplet.conjunctions.size(); i++) {
                        w.write("<pconj>" + DiscourseAnalysisApplet.conjunctions.get(i) + "</pconj>\n");
                    }
                }

                // Print to the XML file.
                w.write("<clause>\n");
                w.write("<conj>" + c.getConj() + "</conj>\n");
                w.write("<text chapter=\"" + c.getChap() + "\" verse=\"" + c.getVrse() + "\">\n");
                w.write(c.getData() + "\n");
                w.write("</text>\n");
                w.write("</clause>\n");
                //System.out.println("THis data: " + c.getData());
            }
        } catch (IOException e) {
            e.printStackTrace(); // You will want to remove this now.
        }

    }
}
