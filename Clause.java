
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A Clause object is composed of text, conjunction, chapter, and verse. It is also a JPanel with a JTextArea in it.
 */
public class Clause extends JPanel {

    private String data;                // The text of the Clause
    private String conj;                // The conjunction of the Clause
    private String chap;                // The chapter number of the Clause
    private String vrse;                // The verse number of the Clause
    private int x;                      // The starting x-value for drawing
    private int y;                      // The starting y-value for drawing
    private JTextArea myTextArea;       // This is where I will show my data.
    private JScrollPane myScrollPane;   // This allows the User to scroll through the text area.

    /**
     * This method is called whenever this Clause's text area needs to accept User input.
     */
    public void enableTextArea() {
        myTextArea.setEnabled(true);
        myTextArea.requestFocus();
    }

    /**
     * Makes a new Clause object with x and y values set to 0.
     *
     * @param d the text data of the Clause
     * @param c the conjunction of the Clause
     * @param ch the chapter number of the Clause
     * @param v the verse number of the Clause
     */
    public Clause(String d, String c, String ch, String v) {
        super();
        data = d;
        conj = c;
        chap = ch;
        vrse = v;

        finishStartup();
    }

    /**
     * An empty constructor. This is used by XMLHandler to make a Clause even when it hasn't finished collecting all of the data.
     */
    public Clause() {
        super();
    }

    /**
     * This is the method that finishes the process of creating a Clause. This should only be called once all data has been filled in.
     */
    public final void finishStartup() {
        x = 0;
        y = 0;

        // Set the title of the JPanel
        if (chap.isEmpty() || vrse.isEmpty()) {
            setBorder(javax.swing.BorderFactory.createTitledBorder(conj));
        } else {
            setBorder(javax.swing.BorderFactory.createTitledBorder(chap + ":" + vrse + " " + conj));
        }

        // Set my text box up
        myTextArea = new JTextArea();
        myTextArea.setLineWrap(true);
        myTextArea.setWrapStyleWord(true);
        myTextArea.setText(data);

        // Add scroll abilities to the text box
        myScrollPane = new JScrollPane();
        myScrollPane.setViewportView(myTextArea);

        // Add the scroll pane (with the text area inside) to the Clause
        setLayout(new GridLayout(1, 1));
        add(myScrollPane);

        // The text area should start disabled.
        myTextArea.setEnabled(false);

        // Create the MouseListener that will be used to listen for User Input.
        MouseListener ml = new MouseListener() {

            /**
             * Not needed, but must be present.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            /**
             * When the text area is "pressed", enable it and give it focus.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                enableTextArea();
            }

            /**
             * Not needed, but must be present.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            /**
             * Not needed, but must be present.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
            }

            /**
             * Not needed, but must be present.
             */
            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        
        // Add click to enable on the text area and panel.
        addMouseListener(ml);
        myTextArea.addMouseListener(ml);

        // When focus is lost, disable the text area.
        myTextArea.addFocusListener(new FocusListener() {

            /**
             * Not needed, but must be present.
             */
            @Override
            public void focusGained(FocusEvent e) {
            }

            /**
             * When focus is lost, disable the text area.
             */
            @Override
            public void focusLost(FocusEvent e) {
                myTextArea.setEnabled(false);
            }
        });
    }

    /**
     * This repositions the Clause in the NodePanel. This is called after updating.
     */
    public void updateClauseBounds() {
        setBounds(x, y, 260, 95);
    }

    /**
     * Returns the XML of this Clause. Possible conjunctions are not yet taken
     * into account.
     *
     * @return result a String that has XML tags and clause data
     */
    public String toXML() {
        String result = "";
        result += "<clause><conj>" + conj + "</conj><text chapter=\"" + chap + "\" verse=\"" + vrse + ">" + data + "</text></clause>";
        return result;
    }

    /**
     * This returns the string representation of the Clause.
     * @return a String representing the Clause
     */
    @Override
    public String toString() {
        if (data.equals("root")) {
            return conj;
        } else {
            String result = "";
            result += chap + ":" + vrse + " Conj: " + conj + " Text: " + data;
            return result;
        }
    }

    /**
     * @return String text of the Clause
     */
    public String getData() {
        return data;
    }

    /**
     * @return String conjunction of the Clause
     */
    public String getConj() {
        return conj;
    }

    /**
     * @return String chapter of the Clause
     */
    public String getChap() {
        return chap;
    }

    /**
     * @return String verse of the Clause
     */
    public String getVrse() {
        return vrse;
    }

    /**
     * @return int x value of Clause
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * @return int y value of Clause
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * @param d	sets data equal to d
     */
    public void setData(String d) {
        data = d;
    }

    /**
     * @param c sets conj equal to c
     */
    public void setConj(String c) {
        conj = c;
    }

    /**
     * @param c sets chap equal to c
     */
    public void setChap(String c) {
        chap = c;
    }

    /**
     * @param v sets vrse equal to v
     */
    public void setVrse(String v) {
        vrse = v;
    }

    /**
     * @param i sets x equal to i
     */
    public void setX(int i) {
        x = i;
    }

    /**
     * @param i sets y equal to i
     */
    public void setY(int i) {
        y = i;
    }
}