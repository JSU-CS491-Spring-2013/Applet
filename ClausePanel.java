
import javax.swing.*;
import java.awt.*;

/**
GUI representation of a Clause object. 
 */
public class ClausePanel extends JPanel {

    /**
    JLabel col	Used to place a colon between the Chapter and Verse text fields
    JTextField conjText	Displays the conjunction 
    JTextField chapText	Displays the chapter
    JTextField vrseText Displays the verse
    JTextArea  dataText Displays the text of the clause
    JScrollPane sc		Allows the user to scroll through the text of the clause
    int x				Stores the x value for placement
    int y 				Stores the y value for placement
    XMLTreeNode w		The current node to be represented
     */
    private JLabel col;
    private JTextField conjText, chapText, vrseText;
    private JTextArea dataText;
    private JScrollPane sc;
    private int x, y;
    private XMLTreeNode w;

    /**
    GUI representation of a single clause object
    @param selectedNode the node to be represented
     */
    public ClausePanel(XMLTreeNode selectedNode) {
        x = 0;
        y = 0;
        setLayout(null);
        this.setPreferredSize(new Dimension(150, 112));
        col = new JLabel(":");
        w = selectedNode;
        try {
            conjText = new JTextField(w.getConj(), 12);
            chapText = new JTextField(w.getChap(), 3);
            vrseText = new JTextField(w.getVrse(), 3);
            dataText = new JTextArea(w.getData(), 4, 40);
            dataText.setLineWrap(true);
            sc = new JScrollPane(dataText);

            setPositions();
        } catch (NullPointerException e) {
            conjText = new JTextField();
            chapText = new JTextField();
            vrseText = new JTextField();
            dataText = new JTextArea("");
            dataText.setLineWrap(true);
            dataText.setWrapStyleWord(true);
            sc = new JScrollPane(dataText);
            setPositions();
        }
    }

    /**
    GUI representation of a clause
    @param c the clause to be represented 
     */
    public ClausePanel(Clause c) {
        x = 0;
        y = 0;
        setLayout(null);
        this.setPreferredSize(new Dimension(150, 112));
        col = new JLabel(":");
        try {
            conjText = new JTextField(c.getConj(), 12);
            chapText = new JTextField(c.getChap(), 3);
            vrseText = new JTextField(c.getVrse(), 3);
            dataText = new JTextArea(c.getData(), 4, 40);
            dataText.setLineWrap(true);
            sc = new JScrollPane(dataText);

            setPositions();
        } catch (NullPointerException e) {
            conjText = new JTextField();
            chapText = new JTextField();
            vrseText = new JTextField();
            dataText = new JTextArea("");
            dataText.setLineWrap(true);
            dataText.setWrapStyleWord(true);
            sc = new JScrollPane(dataText);
            setPositions();
        }
    }

    /**
    Sets the relative positions of the GUI components
     */
    public void setPositions() {
        chapText.setBounds(x, y, 20, 18);
        x = x + 20;
        col.setBounds(x + 2, y, 3, 15);
        x = x + 3;
        vrseText.setBounds(x + 4, y, 27, 18);
        x = x + 27;
        conjText.setBounds(x + 10, y, 90, 18);
        x = x + 100;
        sc.setBounds(0, 22, x, 90);

        this.add(chapText);
        this.add(col);
        this.add(vrseText);
        this.add(conjText);
        this.add(sc);
    }

    //setters
    public void setChap(String s) {
        chapText.setText(s);
    }

    public void setVrse(String s) {
        vrseText.setText(s);
    }

    public void setConj(String s) {
        conjText.setText(s);
    }

    public void setData(String s) {
        dataText.setText(s);
    }

    public void setSelectedNode(XMLTreeNode temp2) {
        w = temp2;
    }
    //getters

    public String getChap() {
        return chapText.getText();
    }

    public String getVrse() {
        return vrseText.getText();
    }

    public String getConj() {
        return conjText.getText();
    }

    public String getData() {
        return dataText.getText();
    }

    public XMLTreeNode getSelectedNode() {
        return w;
    }

    public void setEnabled(boolean isE) {
    }

    public void setEditable(boolean isE) {
        chapText.setEditable(isE);
        conjText.setEditable(isE);
        dataText.setEditable(isE);
        vrseText.setEditable(isE);
        sc.setEnabled(isE);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Test Clause Panel");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //ClausePanel c = new ClausePanel(new Clause("This is a test of the clause panel sdafasgasdfasfgsfdgsdfgasdfasdfasdfasdf alksdfjpoajdsf;alsjdf asdlkfjapsofjas;ldfk, aopfja;lskdjfoajsdf;lakjsdf;lkajsdf;lkjasdlfkj",
        //"inasmuch as", "1","100"));
        Clause cl = null;
        ClausePanel c = new ClausePanel(cl);
        c.setChap("2");
        c.setData("l;aksdjf;laskdfj;lakjdfl;kajdsf;lkjasdf This is a test of the clause panel sdafasgasdfasfgsfdgsdfgasdfasdfasdfasdf alksdfjpoajdsf;alsjdf ");
        c.setEditable(false);
        f.getContentPane().add(c);
        f.pack();
        f.setVisible(true);
    }
}