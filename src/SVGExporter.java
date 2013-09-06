import java.util.Enumeration;
import java.io.*;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//batik imports
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Element;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

/**
 * The SVGExporter class takes the XML tree and converts it into a scalable
 * vector graphic, SVG.  This is accomplished by using an external java
 * library called Batik.  Batik helps to create a SVG DOM, document object
 * model, from the XML tree.  All nodes in the XML tree are represented 
 * by a rectangle element in the SVG document.  All lines in the tree are 
 * represented by a line element in the SVG document.  All text entries 
 * in the XML tree are represented by a text node element, which may be 
 * accompanied by text span elements.  The text span elements are needed 
 * to simulate word wrap within the bounds of each rectangle element in 
 * the graphic.  Batik offers no built in way to wrap text, so it is 
 * accomplished by counting characters and assuming default font size.  
 * Default font size meaning that the font size is never explicitly set 
 * within the class.  Batik offers an option to transcode the SVG DOM 
 * to a .jpeg image.  This is implemented, but it is not being used currently.
 */
public class SVGExporter{
	/**
     * The variable tree is used for traversing the XML tree.  It is a 
     * preorder traversal enumeration.  This is set in the class constructor.
     */
	private Enumeration<XMLTreeNode> tree;
	/**
     * The variable svgDoc is the SVG DOM that the SVG elements are added to.
     */
	private Document svgDoc;
	/**
     * The variable svgNS is a string representing the SVG namespace.  
     * This is set in the class constructor.
     */
	private String svgNS;
	/**
     * The following four variables are used for sizing rectangle elements in the SVG image.
     * @param  NODE_WIDTH
     * @param NODE_HEIGHT
     * @param BORDER_WIDTH
     * @param BORDER_HEIGHT
     */
	private final String NODE_WIDTH = "260";
	private final String NODE_HEIGHT = "80";
	private final String BORDER_WIDTH = "270";
	private final String BORDER_HEIGHT = "90";
	
	/**
	 * This is the constructor of the class.  The one parameter root is used to obtain the 
	 * preorder enumeration of the XML tree.  The private variables tree and svgNS are 
	 * initialized in this constructor.  This constructor also calls the generateSVG 
	 * function, which builds the SVG DOM.
	 * 
	 * @param root the tree root
	 */
	public SVGExporter(XMLTreeNode root){
		tree = root.preorderEnumeration();
		svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		svgDoc = generateSVG();
		/*        
		try{
			transcodeSVG(svgDoc);
		}
		catch(Exception e){
			System.out.println("Exception: "+ e);
		}
		*/	
	}
	
	/**
	 * This function generates the SVG DOM, which is held by the private variable svgDoc.  
	 * It uses the private variable tree to traverse the entire tree and elements 
	 * accordingly.  It returns the created document and places it into the svgDoc 
	 * private variable.
	 * 
	 * @return Returns the document that is created.
	 */
	private Document generateSVG(){
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();		
		Document svgDoc = impl.createDocument(svgNS, "svg", null);

		//Get the root element (the 'svg' element).
		Element svgRoot = svgDoc.getDocumentElement();
		//Set the width and height attributes on the root 'svg' element.
		String width = String.valueOf(DiscourseAnalysisApplet.nodePanel.getWidth());		
		String height = String.valueOf(DiscourseAnalysisApplet.nodePanel.getHeight());		
		svgRoot.setAttributeNS(null, "width", width);
		svgRoot.setAttributeNS(null, "height", height);
		svgRoot.setAttributeNS(null, "style", "background-color: #E3E4E5;");		
		
		while (tree.hasMoreElements()){
			XMLTreeNode currentNode = (XMLTreeNode) tree.nextElement();
			int nodeX = currentNode.getX() + 10;
			int nodeY = currentNode.getY();
			
			//place the border rectangle into the document
			placeRect(svgDoc, nodeX - 5, nodeY - 5, "#E3E4E5", "black", true);
			//place the text area rectangle into the document
			placeRect(svgDoc, nodeX, nodeY, "#F5F5F5", "black", false);
			
			placeText(currentNode, svgDoc);
			placeConj(currentNode, svgDoc);
			
			//if the current node has children
			if (currentNode.getDepth() > 0){
				placeLine(svgDoc, nodeX + 265, nodeX + 300, nodeY + 40, nodeY + 40, "black");
				
                //Get the first and last children.
                XMLTreeNode first = (XMLTreeNode) currentNode.getFirstChild();
                XMLTreeNode last = (XMLTreeNode) currentNode.getLastChild();

                //If they aren't the same thing, draw a line between them.
                if (first != last){
                	placeLine(svgDoc, first.getX() - 30, last.getX() - 30, first.getY() + 40, last.getY() + 40, "black");                	
                }
            }

            // If the current node is a child
            if (!currentNode.isRoot()){
            	placeLine(svgDoc, nodeX - 5, nodeX - 40, nodeY + 40, nodeY + 40, "black");            	                
            }
		}
		return svgDoc;
	}
	
	/**
	 * @return the svgDoc private variable
	 */
	public Document getDoc(){
		return svgDoc;
	}
	
	/**
	 * This function transcodes the SVG DOM into a .jpeg image.  This can only be used 
	 * after the generateSVG() function has been called.  This functionality is 
	 * not currently being used.
	 * 
	 * @param svgDoc
	 * @throws Exception
	 */
	public void transcodeSVG(Document svgDoc)  throws Exception{
		// Create a JPEGTranscoder and set its quality hint.
        JPEGTranscoder t = new JPEGTranscoder();
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));

        // Set the transcoder input and output.
        TranscoderInput input = new TranscoderInput(svgDoc);
        OutputStream ostream = new FileOutputStream("C:\\out.jpg");
        TranscoderOutput output = new TranscoderOutput(ostream);

        // Perform the transcoding.
        t.transcode(input, output);
        ostream.flush();
        ostream.close();
	}
	
	/**
	 * This function sends the information in the SVG DOM to an output stream.  
	 * The output stream is used to stream the information to a .svg file.
	 * 
	 * @param doc
	 * @param out
	 * @throws IOException
	 */
	public void write(Document doc, OutputStream out) throws IOException {
	    //this function writes the SVG document to a .svg file
	    try {
	      Transformer t = TransformerFactory.newInstance().newTransformer();
	      DocumentType dt = doc.getDoctype();
	      if (dt != null) {
	        String pub = dt.getPublicId();
	        if (pub != null) {
	          t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, pub);
	        }
	        t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dt.getSystemId());
	      }
	      t.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); // NOI18N
	      t.setOutputProperty(OutputKeys.INDENT, "yes"); // NOI18N
	      t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // NOI18N
	      Source source = new DOMSource(doc);
	      Result result = new StreamResult(out);
	      t.transform(source, result);
	    } catch (Exception e) {
	      throw (IOException) new IOException(e.toString()).initCause(e);
	    } catch (TransformerFactoryConfigurationError e) {
	      throw (IOException) new IOException(e.toString()).initCause(e);
	    }
	  }
	
	/**
	 * This function takes parameters relating to x and y coordinates, 
	 * rectangle color, rectangle border color, and if the rectangle is a border.  
	 * The function uses these parameters to set the attributes of the 
	 * rectangle element.  After creation the rectangle element is added 
	 * to the svgDoc, the private variable.
	 * 
	 * @param svgDoc
	 * @param nodeX
	 * @param nodeY
	 * @param color
	 * @param borderColor
	 * @param border
	 */
	private void placeRect(Document svgDoc, int nodeX, int nodeY, String color, String borderColor, boolean border){
		//create a rectangle element and add it to the document root
		Element rectangle = svgDoc.createElementNS(svgNS, "rect");
		rectangle.setAttributeNS(null, "x", String.valueOf(nodeX));
		rectangle.setAttributeNS(null, "y", String.valueOf(nodeY));
		//changing the width and height of the rectangle
		//depending on if it is a border rectangle or not
		if(border == true){
			rectangle.setAttributeNS(null, "width", BORDER_WIDTH);
			rectangle.setAttributeNS(null, "height", BORDER_HEIGHT);
		}
		else{
			rectangle.setAttributeNS(null, "width", NODE_WIDTH);
			rectangle.setAttributeNS(null, "height", NODE_HEIGHT);
		}
		
		//fill the rectangle with the color passed in
		rectangle.setAttributeNS(null, "fill", color);
		rectangle.setAttributeNS(null, "stroke", borderColor);
		rectangle.setAttributeNS(null, "stroke-width", "2");
		//Attach the rectangle to the root 'svg' element.
		Element svgRoot = svgDoc.getDocumentElement();
		svgRoot.appendChild(rectangle);
	}
	
	/**
	 * This function takes two pairs of x and y coordinates, which are used to 
	 * determine the path of the line.  The line color is also passed in.  
	 * The function creates a line element with the coordinates and then 
	 * adds it to the svgDoc, the private variable.
	 * 
	 * @param svgDoc
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param color
	 */
	private void placeLine(Document svgDoc, int x1, int x2, int y1, int y2, String color){
		//create a line element and add it to the document root
		Element line = svgDoc.createElementNS(svgNS, "line");
		line.setAttributeNS(null, "x1", String.valueOf(x1));
		line.setAttributeNS(null, "y1", String.valueOf(y1));
		line.setAttributeNS(null, "x2", String.valueOf(x2));
		line.setAttributeNS(null, "y2", String.valueOf(y2));
		line.setAttributeNS(null, "stroke", color);
		line.setAttributeNS(null, "stroke-width", "2");
		Element svgRoot = svgDoc.getDocumentElement();
    	svgRoot.appendChild(line);
	}
	
	/**
	 * This function adds text elements to the svgDoc, the private variable.  
	 * The function takes into account the nodes x and y coordinates to ensure 
	 * the text is within the bounds of the rectangle element it belongs to.  
	 * Text span, tspan, elements are also used to create a word wrap.  
	 * This is to keep the text within the bounds of the rectangle element 
	 * it belongs to.  The word wrap is accomplished by splitting up the text 
	 * into a word array, where every index of the array represents a 
	 * single word.  Next, character counting is used to prevent the text from 
	 * leaving the rectangle element it belongs to.  This assumes default font 
	 * size, meaning that the font size is never explicitly set within 
	 * the class.  Text span elements are added to a parent text element.  
	 * All text elements are then added to the svgDoc, the private variable.
	 * 
	 * @param node
	 * @param svgDoc
	 */
	private void placeText(XMLTreeNode node, Document svgDoc){
		//this function is needed to handle word wrap when creating the SVG document
		int width = 42;		
		String[] words = node.getClause().getData().split(" ");
		String aLine = "";
		int nodeX = node.getX() + 10;
		
        Element text = svgDoc.createElementNS(svgNS, "text");
        text.setAttributeNS(null, "x", String.valueOf(nodeX + 5));
		text.setAttributeNS(null, "y", String.valueOf(node.getY()+ 5));		

        aLine += words[0];
        if(words.length == 1){
        	Element tempElement = svgDoc.createElementNS(svgNS, "tspan");     //Create new tspan element
        	tempElement.setAttributeNS(null, "x", String.valueOf(nodeX + 5));
            tempElement.setAttributeNS(null, "dy", "15");
            Node textNode = svgDoc.createTextNode(aLine);
            tempElement.appendChild(textNode);
            text.appendChild(tempElement);
        }
        else{
	        for(int i = 1; i < words.length; i++){        	
	            int len = aLine.length();             // Find number of letters in string
	            aLine += " " + words[i];              // Add next word
	
	            if (aLine.length() >= width){            	
	            	aLine = aLine.substring(0, len); // Remove added word
	
	                Element tempElement = svgDoc.createElementNS(svgNS, "tspan");     //Create new tspan element
	                tempElement.setAttributeNS(null, "x", String.valueOf(nodeX + 5));
	                tempElement.setAttributeNS(null, "dy", "15");
	                Node textNode = svgDoc.createTextNode(aLine);
	                tempElement.appendChild(textNode);
	                text.appendChild(tempElement);
	                aLine = "";
	                aLine += words[i];	                
	            }
	            else if(i == words.length - 1){
	            	Element tempElement = svgDoc.createElementNS(svgNS, "tspan");     //Create new tspan element
	            	tempElement.setAttributeNS(null, "x", String.valueOf(nodeX + 5));
	                tempElement.setAttributeNS(null, "dy", "15");
	                Node textNode = svgDoc.createTextNode(aLine);
	                tempElement.appendChild(textNode);
	                text.appendChild(tempElement);
	            }
	        }
        }
        Element svgRoot = svgDoc.getDocumentElement();
        svgRoot.appendChild(text);
	}
	/**
	 * This function work in a similar way as the placeText function.  It concatenates 
	 * the node's chapter, verse, and conjunction together and places it in a 
	 * text node.  The x and y coordinates of the text node are based on the 
	 * node;s x and y coordinates.  The text is placed slightly above the node's y 
	 * coordinate and keeps the same x coordinate.  The text node is added to the 
	 * text elements after the text element's attributes are set.  The text 
	 * element is then added to the svgDoc, the private variable.
	 * 
	 * @param node
	 * @param svgDoc
	 */
	private void placeConj(XMLTreeNode node, Document svgDoc){
		int nodeX = node.getX() + 10;
		String chapter = node.getClause().getChap();
		String verse = node.getClause().getVrse();
		String conjunction = node.getClause().getConj();
		//concatenate the chapter, verse, and conjunction together
		String conjString = chapter + ":" + verse + " " + conjunction;
		
		Element text = svgDoc.createElementNS(svgNS, "text");
        text.setAttributeNS(null, "x", String.valueOf(nodeX));
		text.setAttributeNS(null, "y", String.valueOf(node.getY() - 8));
		text.setAttributeNS(null, "font-weight", "bold");
		Node textNode = svgDoc.createTextNode(conjString);        
        text.appendChild(textNode);
        Element svgRoot = svgDoc.getDocumentElement();
        svgRoot.appendChild(text);
	}
}
