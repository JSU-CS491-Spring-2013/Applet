import java.util.ArrayList;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Tyler Pentecost
 */
public class XMLHandler extends DefaultHandler {

    private enum types {

        BLANK, CLAUSE, CONJ, TEXT, PCONJ
    };

    @Override
    public void startDocument() {
        // Create a Stack that will be used to handle nested Clauses.
        stack = new Stack<XMLTreeNode>();
        text = "";
        
        DiscourseAnalysisApplet.conjunctions = new ArrayList<String>();
      
    }

    @Override
    public void endDocument() {
        // After it's all over, give the root to DAA. I would love to do this another way. A public static field isn't the best way to do this.
        DiscourseAnalysisApplet.root = root;
        
        for (int i = 0; i < DiscourseAnalysisApplet.conjunctions.size(); i++) {
            int bigPosition = -1;
            int bigCount = -1;
            for (int j = i; j < DiscourseAnalysisApplet.conjunctions.size(); j++) {
                if (DiscourseAnalysisApplet.conjunctions.get(j).length() > bigCount) {
                    bigCount = DiscourseAnalysisApplet.conjunctions.get(j).length();
                    bigPosition = j;
                }
            }
            
            String temp = DiscourseAnalysisApplet.conjunctions.get(i);
            DiscourseAnalysisApplet.conjunctions.set(i, DiscourseAnalysisApplet.conjunctions.get(bigPosition));
            DiscourseAnalysisApplet.conjunctions.set(bigPosition, temp);
        }
    }
    
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) {
        // If a Clause is found, make a new Clause and add it to the stack.
        if (localName.equals("clause")) {
            
            Clause tempClause = new Clause();
            XMLTreeNode parent = new XMLTreeNode(tempClause);
            tempClause.clickNode = parent;
            stack.add(parent);
            type = types.CLAUSE;
                        
           
        } else if (localName.equals("conj")) { // If a Conj is found, make sure characters() knows to listen for it.
            type = types.CONJ;
        } else if (localName.contains("text")) { // If Text is found, get the chapter and verse.
            stack.peek().setChap(attributes.getValue("chapter").trim());
            stack.peek().setVrse(attributes.getValue("verse").trim());
            type = types.TEXT;
        } else if (localName.equals("book")) {
            // Make the root node, and add it to the stack.
            Clause temp = new Clause("root", attributes.getValue("bookName"), "", "");
            root = new XMLTreeNode(temp);
            stack.add(root);
        } else if (localName.equals("pconj")) {
            type = types.PCONJ;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        // If a Clause is ending, take the top Clause off of the stack, and finish its start up 
        if (localName.equals("clause")) {
            XMLTreeNode temp = stack.pop();
            temp.getClause().finishStartup();
            stack.peek().add(temp); // The new top spot on the stack is this Clause's parent. Add this Clause as a child of that Clause.
        } else if (localName.equals("text")) {
            // The text tag is over now, set the text back to nothing, reset our text String, and stop listening.
            stack.peek().getClause().setData(text.trim());
            text = "";
            type = types.BLANK;
        } else if (localName.equals("pconj")) {
            DiscourseAnalysisApplet.conjunctions.add(text);
            text = "";
            type = types.BLANK;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (type == types.TEXT || type == types.PCONJ) {
            // If you find TEXT, add it to what we've found so far.
            text += new String(ch, start, length);
        } else if (type == types.CONJ) {
            // We've found the CONJ, set it.
            stack.peek().setConj(new String(ch, start, length).trim());

            // Set the type to be BLANK. There no reason to point to anything.
            type = types.BLANK;
        }
    }
    private XMLHandler.types type;      // This has the various types of tags that will be found.
    private Stack<XMLTreeNode> stack;   // The Stack that helps with nested Clauses.
    private String text;                // The text that has been found by characters().
    private XMLTreeNode root;           // The root node of the tree.
}
