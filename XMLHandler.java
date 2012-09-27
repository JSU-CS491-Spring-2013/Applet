
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Tyler Pentecost
 */
public class XMLHandler extends DefaultHandler {

    private enum types {

        BLANK, CLAUSE, CONJ, TEXT
    };

    @Override
    public void startDocument() {
        // Create a Stack that will be used to handle nested Clauses.
        stack = new Stack<XMLTreeNode>();
        text = "";

        // Make the root node, and add it to the stack.
        Clause temp = new Clause("root", "Luke", "", "");
        root = new XMLTreeNode(temp);
        stack.add(root);
    }

    @Override
    public void endDocument() {
        // After it's all over, give the root to DAA. I would love to do this another way. A public static field isn't the best way to do this.
        DiscourseAnalysisApplet.root = root;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) {
        // If a Clause is found, make a new Clause and add it to the stack.
        if (localName.equals("clause")) {
            stack.add(new XMLTreeNode(new Clause()));
            type = types.CLAUSE;
        } else if (localName.equals("conj")) { // If a Conj is found, make sure characters() knows to listen for it.
            type = types.CONJ;
        } else if (localName.contains("text")) { // If Text is found, get the chapter and verse.
            stack.peek().setChap(attributes.getValue("chapter").trim());
            stack.peek().setVrse(attributes.getValue("verse").trim());
            type = types.TEXT;
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
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (type == types.TEXT) {
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
