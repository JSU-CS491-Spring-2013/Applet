
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.*;

/**
 * A Clause object is composed of text, conjunction, chapter, and verse. It is
 * also a JPanel with a JTextArea in it.
 */
public class Clause extends JPanel {

    private String data;                // The text of the Clause
    private String conj;                // The conjunction of the Clause
    private String chap;                // The chapter number of the Clause
    private String vrse;                // The verse number of the Clause
    private int x;                      // The starting x-value for drawing
    private int y;                      // The starting y-value for drawing
    private JTextPane myTextArea;       // This is where I will show my data.
    private JScrollPane myScrollPane;   // This allows the User to scroll through the text area.
    private NodePanel myNodePanel;      // This is the NodePanel this Clause is on.

    /**
     * This method is called whenever this Clause's text area needs to accept
     * User input.
     */
    public void enableTextArea() {
        if (DiscourseAnalysisApplet.nodePanel.isButtonPanelShown()) {
            myTextArea.setEnabled(true);
            myTextArea.requestFocus();
        }
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
     * An empty constructor. This is used by XMLHandler to make a Clause even
     * when it hasn't finished collecting all of the data.
     */
    public Clause() {
        super();
    }

    private void highlightPotentialConjunctions() {
        String temporaryData = data;

        // Get a style for normal text.
        StyleContext context = new StyleContext();
        StyledDocument document = new DefaultStyledDocument(context);
        Style style = context.getStyle(StyleContext.DEFAULT_STYLE);

        // Get a style for potential conjunctions.
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setBold(attributes, true);
        StyleConstants.setBackground(attributes, Color.BLUE);
        StyleConstants.setForeground(attributes, Color.WHITE);

        // While there is still text to be added...
        while (temporaryData.length() > 0) {
            int firstConjunction = Integer.MAX_VALUE; // Set this to the max so the calculation will work.
            String firstConjunctionText = ""; // This will hold the found potential conj
            String tempConjunction = "";
            int temporaryConjunctionLocation = -1;

            // Go through the list of conjunctions and find which one comes first.
            for (int j = 0; j < DiscourseAnalysisApplet.conjunctions.size(); j++) {
                tempConjunction = DiscourseAnalysisApplet.conjunctions.get(j);
                temporaryConjunctionLocation = temporaryData.toLowerCase().indexOf(tempConjunction.toLowerCase());

                // If a conjunction is found closer to the beginning, keep up with it instead.
                if (temporaryConjunctionLocation >= 0 && temporaryConjunctionLocation < firstConjunction) {
                    boolean wholeWord = true;

                    try {
                        char tempChar = temporaryData.toLowerCase().charAt(temporaryConjunctionLocation - 1);
                        if (tempChar == 'a'
                                || tempChar == 'b'
                                || tempChar == 'c'
                                || tempChar == 'd'
                                || tempChar == 'e'
                                || tempChar == 'f'
                                || tempChar == 'g'
                                || tempChar == 'h'
                                || tempChar == 'i'
                                || tempChar == 'j'
                                || tempChar == 'k'
                                || tempChar == 'l'
                                || tempChar == 'm'
                                || tempChar == 'n'
                                || tempChar == 'o'
                                || tempChar == 'p'
                                || tempChar == 'q'
                                || tempChar == 'r'
                                || tempChar == 's'
                                || tempChar == 't'
                                || tempChar == 'u'
                                || tempChar == 'v'
                                || tempChar == 'w'
                                || tempChar == 'x'
                                || tempChar == 'y'
                                || tempChar == 'z') {
                            wholeWord = false;
                        }
                    } catch (Exception e) {
                    }

                    try {
                        char tempChar = temporaryData.toLowerCase().charAt(temporaryConjunctionLocation + tempConjunction.length() + 1);
                        if (tempChar == 'a'
                                || tempChar == 'b'
                                || tempChar == 'c'
                                || tempChar == 'd'
                                || tempChar == 'e'
                                || tempChar == 'f'
                                || tempChar == 'g'
                                || tempChar == 'h'
                                || tempChar == 'i'
                                || tempChar == 'j'
                                || tempChar == 'k'
                                || tempChar == 'l'
                                || tempChar == 'm'
                                || tempChar == 'n'
                                || tempChar == 'o'
                                || tempChar == 'p'
                                || tempChar == 'q'
                                || tempChar == 'r'
                                || tempChar == 's'
                                || tempChar == 't'
                                || tempChar == 'u'
                                || tempChar == 'v'
                                || tempChar == 'w'
                                || tempChar == 'x'
                                || tempChar == 'y'
                                || tempChar == 'z') {
                            wholeWord = false;
                        }
                    } catch (Exception e) {
                    }

                    if (wholeWord) {
                        firstConjunction = temporaryConjunctionLocation;
                        firstConjunctionText = tempConjunction;
                    }
                }
            }

            if (firstConjunction > 0 && firstConjunction < Integer.MAX_VALUE) { // If the first conjunction appears after the beginning.
                try {
                    document.insertString(document.getLength(), temporaryData.substring(0, firstConjunction), style);
                    temporaryData = temporaryData.substring(firstConjunction);

                    document.insertString(document.getLength(), temporaryData.substring(0, firstConjunctionText.length()), attributes);
                    temporaryData = temporaryData.substring(firstConjunctionText.length());
                } catch (BadLocationException badLocationException) {
                    System.err.println("Oops");
                }
            } else if (firstConjunction == 0) { // If the first conjunction appears at the beginning.
                try {
                    document.insertString(document.getLength(), temporaryData.substring(0, firstConjunctionText.length()), attributes);
                    temporaryData = temporaryData.substring(firstConjunctionText.length());
                } catch (BadLocationException badLocationException) {
                    System.err.println("Oops");
                }
            } else { // If there are no conjunctions
                try {
                    document.insertString(document.getLength(), temporaryData, style);
                    temporaryData = "";
                } catch (BadLocationException badLocationException) {
                    System.err.println("Oops");
                }
            }
        }

        myTextArea.setDocument(document);
    }

    /**
     * This is the method that finishes the process of creating a Clause. This
     * should only be called once all data has been filled in.
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
        myTextArea = new JTextPane();
        // myTextArea.setLineWrap(true);
        // myTextArea.setWrapStyleWord(true);
        myTextArea.setText(data);
        highlightPotentialConjunctions();

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
                boolean before = DiscourseAnalysisApplet.nodePanel.isButtonPanelShown();

                DiscourseAnalysisApplet.nodePanel.showButtonPanel(x, y); // show the buttonpanel next to it

                if (!before && DiscourseAnalysisApplet.nodePanel.isButtonPanelShown()) {
                    enableTextArea(); // enable and focus
                }
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
                // DiscourseAnalysisApplet.nodePanel.showButtonPanel(x, y); // show the buttonpanel next to it - redundant, but safe
            }

            /**
             * When focus is lost, disable the text area.
             */
            @Override
            public void focusLost(FocusEvent e) {
                myTextArea.setEnabled(false);
                DiscourseAnalysisApplet.nodePanel.hideButtonPanel();
            }
        });
    }

    /**
     * This repositions the Clause in the NodePanel. This is called after
     * updating.
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
     *
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