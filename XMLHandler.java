
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Tyler Pentecost
 */
public class XMLHandler extends DefaultHandler {
    
    private enum types { BLANK, CLAUSE, CONJ, TEXT };
    
    @Override
    public void startDocument() {
        stack = new Stack<XMLTreeNode>();
        text = "";
        
        Clause temp = new Clause("Luke", "", "", "");
        root = new XMLTreeNode(temp);
        stack.add(root);
    }
    
    @Override
    public void endDocument() {
        DiscourseAnalysisApplet.root = root;
    }
    
    @Override
    public void startElement(String uri, String localName, String qName,
        Attributes attributes) {
        switch (localName) {
            case "clause":
                stack.add(new XMLTreeNode(new Clause()));
                type = types.CLAUSE;
                break;
            case "conj":
                type = types.CONJ;
                break;
            case "text":
                stack.peek().setChap(attributes.getValue("chapter").trim());
                stack.peek().setVrse(attributes.getValue("verse").trim());
                type = types.TEXT;
                break;
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (localName) {
            case "clause":
                XMLTreeNode temp = stack.pop();
                temp.getClause().finishStartup();
                stack.peek().add(temp);
                break;
            case "text":
                // The text tag is over now, set the text, reset our text String, and stop listening.
                stack.peek().getClause().setData(text.trim());
                text = "";
                type = types.BLANK;
                break;
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
    
    private XMLHandler.types type;
    private Stack<XMLTreeNode> stack;
    private String text;
    private XMLTreeNode root;
}
