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


public class XMLConverter {
    
    public XMLConverter (){}
    
    public XMLConverter (String fp, XMLTreeModel temp){
        File xmlfile= new File(fp);
        XMLTreeNode root=(XMLTreeNode) temp.getRoot();
        BufferedWriter writer = null;
        String xmlcode = "<?xml version=\"1.0\" ?>";
        try{
            writer = new BufferedWriter(new FileWriter(xmlfile));
            //consider writing recursive function                      
        }
        catch (IOException e) {
            e.printStackTrace();
        } 

        //this is the bit where you write your XML to the file
        try {
            writer.write(xmlcode);
            nodeCycle(writer, root);
            writer.write("</book>");
            System.out.println("I fired.");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        
        
        try {
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
      
    
    }
    
    public void nodeCycle(BufferedWriter w, XMLTreeNode temp){
        
        try {
            if(temp.getChildCount() > 0){ //if the passed node has children 
                Clause c = new Clause(); //temporary clause to hold data from the node passed in.
                c = temp.getClause(); //assigning the values of the passed node to c
                System.out.print("DEPTH OF " + c.getChap().toString() + " IS : " + temp.getChildCount());//testing to see how many children if more then 0
                if(c.getData().toString().equals("root")){//this will only ever happen when the root node is passed in to set the book in XML
                    w.write("<book bookName=\"" + c.getConj().toString() + "\">\n");//initial book XML tag
                    //System.out.print("<book bookName=\"" + c.getConj().toString() + "\">");
                }
                else{
                    //System prints are for debugging
                    /*System.out.print("<clause>\n");
                    System.out.print("<conj>" + c.getConj().toString() + "</conj>\n");
                    System.out.print("<text verse=\"" + c.getVrse() + "\" chapter=\"" + c.getChap().toString() + "\">\n");
                    System.out.print(c.getData().toString() + "\n");
                    System.out.print("</text>\n");*/
                    
                    //Writing XML tags and the data for them to the file.
                    w.write("<clause>\n");
                    w.write("<conj>" + c.getConj().toString() + "</conj>\n");
                    w.write("<text verse=\"" + c.getVrse() + "\" chapter=\"" + c.getChap().toString() + "\">\n");
                    w.write(c.getData().toString() + "\n");
                    w.write("</text>\n");
                }
                   
                 
                Enumeration e = temp.breadthFirstEnumeration(); // Get a list of Nodes.
                try {//catching element exceptions
                    //While having children begin recursive function call.
                    while(e.nextElement() != null){//so it has children, go through each of them
                        XMLTreeNode curr = (XMLTreeNode) e.nextElement(); //getting the next node
                        nodeCycle(w, curr);
                    }
                } catch (Exception ex) { }
                
                //If this is the not the root node will end the clause (this is here due to possible nested nodes)
                if(!c.getData().toString().equals("root")){
                    w.write("</clause>");
                    //System for debugging
                    //System.out.print("</clause>");
                }
                          
            }
            
            else{
                //Temp Clause to temporarily store information
                Clause c = new Clause();
                c = temp.getClause();
                
                //If the node passed in is the root of the tree passed in
                if(c.getData().toString().equals("root")){
                    w.write("<book bookName=\"" + c.getData().toString() + "\">");
                    //System print outs for debugging.
                    //System.out.print("<book bookName=\"" + c.getConj().toString() + "\">");
                }
                //System print for debugging.
                /*System.out.print("<clause>\n");
                System.out.print("<conj>" + c.getConj().toString() + "</conj>\n");
                System.out.print("<text verse=\"" + c.getVrse() + "\" chapter=\"" + c.getChap().toString() + "\">\n");
                System.out.print(c.getData().toString() + "\n");
                System.out.print("</text>\n");
                System.out.print("</clause>\n");*/
                
                
                //Printing to the XML file
                w.write("<clause>\n");
                w.write("<conj>" + c.getConj().toString() + "</conj>\n");
                w.write("<text verse=\"" + c.getVrse() + "\" chapter=\"" + c.getChap().toString() + "\">\n");
                w.write(c.getData().toString() + "\n");
                w.write("</text>\n");
                w.write("</clause>\n");
            }           
        }
        catch (IOException e) {
            e.printStackTrace();
        } 
       
    }
    /*
    //Testing purposes, uncomment to self test as a file on its own.
    public static void main(String[] args){
        XMLConverter m = new XMLConverter();
        String tempd = "Blah";
        String tempc = "and";
        String tempch = "3";
        String tempv = "2.2";
        Clause c = new Clause(tempd, tempc, tempch, tempv);
        XMLTreeNode temp = new XMLTreeNode(c);
       // XMLTreeModel b = new XMLTreeModel(temp);
        //XMLTreeNode treeNode = new XMLTreeNode();
        //m = new XMLConverter("C:\\Users\\DerekZul\\Documents\\Awesome.xml", temp);
    }*/

}
