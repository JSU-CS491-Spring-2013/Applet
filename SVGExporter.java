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

public class SVGExporter{
	private Enumeration<XMLTreeNode> tree;
	private Document svgDoc;
	private String svgNS;
	private final String NODE_WIDTH = "260";
	private final String NODE_HEIGHT = "80";
	private final String BORDER_WIDTH = "270";
	private final String BORDER_HEIGHT = "90";

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
	
	public Document getDoc(){
		return svgDoc;
	}
	
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
