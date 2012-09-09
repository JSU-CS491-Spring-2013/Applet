
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * GUI representation of a Clause object
 */
public class ClausePanel extends JPanel {

    private JLabel col;             // Used to place a colon between the Chapter and Verse text fields.
    private JTextField conjText;    // Displays the conjunction.
    private JTextField chapText;    // Displays the chapter.
    private JTextField vrseText;    // Displays the verse.
    private JTextArea dataText;     // Displays the text of the clause.
    private JScrollPane sc;         // Allows the user to scroll through the text of the clause.
    private int x;                  // Stores the x value for placement.
    private int y;                  // Stores the y value for placement.
    private XMLTreeNode w;          // The current node to be represented.

    /**
     * GUI representation of a single Clause object.
     * @param selectedNode the Node to be represented.
     */
    public ClausePanel(XMLTreeNode selectedNode) {
        w = selectedNode;
        try {
            conjText = new JTextField(w.getConj(), 12);
            chapText = new JTextField(w.getChap(), 3);
            vrseText = new JTextField(w.getVrse(), 3);
            dataText = new JTextArea(w.getData(), 4, 40);
        } catch (NullPointerException e) {
            conjText = new JTextField();
            chapText = new JTextField();
            vrseText = new JTextField();
            dataText = new JTextArea("");
            dataText.setWrapStyleWord(true);
        }

        // All redundant statements were moved here
        constructorHelper();
    }
    
    /**
     * GUI representation of a single Clause object
     * @param c the Clause to be represented
     */
    public ClausePanel(Clause c) {
        try {
            conjText = new JTextField(c.getConj(), 12);
            chapText = new JTextField(c.getChap(), 3);
            vrseText = new JTextField(c.getVrse(), 3);
            dataText = new JTextArea(c.getData(), 4, 40);
        } catch (NullPointerException e) {
            conjText = new JTextField();
            chapText = new JTextField();
            vrseText = new JTextField();
            dataText = new JTextArea("");
            dataText.setWrapStyleWord(true);
        }

        // All redundant statements were moved here
        constructorHelper();
    }

    /**
     * Contains the redundant statements that all constructors execute.
     */
    private void constructorHelper() {
        x = 0;
        y = 0;

        setLayout(null);
        this.setPreferredSize(new Dimension(150, 112));

        col = new JLabel(":");
        sc = new JScrollPane(dataText);
        dataText.setLineWrap(true);

        // All redundant statements were moved here
        setPositions();
    }

    /**
     * Sets the relative positions of the GUI components.
     */
    private void setPositions() {
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

    /**
     * Sets the chapter number of this Clause.
     * @param s a String containing the chapter number
     */
    public void setChap(String s) {
        chapText.setText(s);
    }

    /**
     * Sets the verse number of this Clause.
     * @param s a String containing the verse number
     */
    public void setVrse(String s) {
        vrseText.setText(s);
    }

    /**
     * Sets the conjunction that was used to separate this Clause.
     * @param s a String containing the conjunction
     */
    public void setConj(String s) {
        conjText.setText(s);
    }

    /**
     * Sets the text of this Clause.
     * @param s a String containing the text to be displayed
     */
    public void setData(String s) {
        dataText.setText(s);
    }

    /**
     * Sets the Node that is being used.
     * @param temp2 the XMLTreeNode to be used
     */
    public void setSelectedNode(XMLTreeNode temp2) {
        w = temp2;
    }

    /**
     * Returns the chapter number of this Clause.
     * @return a String that contains the chapter number
     */
    public String getChap() {
        return chapText.getText();
    }

    /**
     * Returns the verse number of this Clause.
     * @return a String that contains the verse number
     */
    public String getVrse() {
        return vrseText.getText();
    }

    /**
     * Returns the conjunction used in this Clause.
     * @return a String that contains the conjunction
     */
    public String getConj() {
        return conjText.getText();
    }

    /**
     * Returns the text of this Clause.
     * @return a String containing the text of this Clause
     */
    public String getData() {
        return dataText.getText();
    }

    /**
     * Returns the Node used.
     * @return the XMLTreeNode used
     */
    public XMLTreeNode getSelectedNode() {
        return w;
    }

    /**
     * This method controls the editable property of the various fields.
     * @param isE true if these controls should be editable, false otherwise
     */
    public void setEditable(boolean isE) {
        chapText.setEditable(isE);
        conjText.setEditable(isE);
        dataText.setEditable(isE);
        vrseText.setEditable(isE);
        sc.setEnabled(isE);
    }

    /*public static void main(String[] args) {
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
    }*/
}