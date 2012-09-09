
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
Provides the buttons and Text fields required to test core functions.
 * 
 */
public class ButtonPanel extends JPanel {

    public ClausePanel sel, n;
    private JButton remove, treeModel, group, split, edit, ok, cancel;
    private int command, numSuper;
    private JLabel selNode, blankNode;
    public JComboBox superSize;
    private int x;
    private TestApplet myParent;

    /**
    Constructor - builds a new ButtonPanel
    @param int x is the starting x location for the ButtonPanel
     */
    public ButtonPanel(int x, TestApplet mp) {
        this.x = x;
        this.setLayout(null);
        command = -1;
        Clause temp = null;
        sel = new ClausePanel(temp);
        n = new ClausePanel(temp);
        superSize = new JComboBox();
        remove = new JButton("remove");
        treeModel = new JButton("treeModel");
        group = new JButton("group");
        split = new JButton("split");
        edit = new JButton("edit");
        ok = new JButton("Ok");
        cancel = new JButton("cancel");

        selNode = new JLabel("Selected Clause");
        blankNode = new JLabel("New Clause");
        ok.setEnabled(false);
        cancel.setEnabled(false);
        setAct();
        setPosition();
        toggleB(false);
        
        myParent = mp;
    }

    /**
    Toggle enable for the Buttons
     */
    public void toggleB(boolean t) {
        remove.setEnabled(t);
        treeModel.setEnabled(t);
        group.setEnabled(t);
        split.setEnabled(t);
        edit.setEnabled(t);
    }

    /**
    Sets the Action Listeners for each of the Buttons
     */
    public void setAct() {
        remove.setToolTipText("Remove Clause");
        remove.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {
                //Stuff for Remove
                command = 0;
                toggleB(false);
                ok.setEnabled(true);
                cancel.setEnabled(true);
            }
        });
        treeModel.setToolTipText("Merge Two Clauses");
        treeModel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {
                //Stuff for Merge
                command = 1;
                toggleB(false);
                ok.setEnabled(true);
                cancel.setEnabled(true);
            }
        });
        group.setToolTipText("Group Adjacent Clauses");
        group.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {
                //Stuff for Group
                command = 2;
                toggleB(false);
                ok.setEnabled(true);
                cancel.setEnabled(true);
                setUpCombo();
            }
        });
        split.setToolTipText("Split Clause");
        split.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {
                //Stuff for Split
                command = 3;
                toggleB(false);
                ok.setEnabled(true);
                cancel.setEnabled(true);
            }
        });
        edit.setToolTipText("Edit Clause");
        edit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {
                //Stuff for Edit
                command = 4;
                toggleB(false);
                ok.setEnabled(true);
                cancel.setEnabled(true);
            }
        });
        ok.setToolTipText("Accept Changes");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {
                //Stuff for Ok
                //call the action method and send through as a parameter
                myParent.performFunction(command);
                ok.setEnabled(false);
                cancel.setEnabled(false);
                toggleB(false);
                myParent.getNodePanel().resetSelNode();
                superSize.removeAllItems();
            }
        });
        cancel.setToolTipText("Cancel Changes");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent a) {
                //Stuff for Cancel
                command = -1;
                ok.setEnabled(false);
                cancel.setEnabled(false);
                toggleB(true);
            }
        });
    }

    /**
    Sets the data for the Clause Panel based on the data from the selected XMLTreeNode
    @param XMLTreeNode selected
     */
    public void setSelected(XMLTreeNode selected) {

        try {
            toggleB(true);
            sel.setChap(selected.getChap());
            sel.setVrse(selected.getVrse());
            sel.setConj(selected.getConj());
            sel.setData(selected.getData());

        } catch (NullPointerException edit) {
            sel.setChap("");
            sel.setVrse("");
            sel.setConj("");
            sel.setData("");
            n.setChap("");
            n.setVrse("");
            n.setConj("");
            n.setData("");
        }
    }

    /**
    Sets the position of each of the GUI elements based on the intial 
    x value.
     */
    public void setPosition() {
        sel.setBounds(x, 0, 150, 112);
        n.setBounds(x, 114, 150, 112);
        ok.setBounds(x, 228, 50, 30);
        cancel.setBounds(x + 60, 228, 50, 30);
        treeModel.setBounds(x, 260, 50, 30);
        split.setBounds(x + 60, 260, 50, 30);
        edit.setBounds(x, 292, 50, 30);
        remove.setBounds(x + 60, 292, 50, 30);
        group.setBounds(x, 324, 50, 30);
        superSize.setBounds(x + 60, 324, 50, 30);

        this.add(sel);
        this.add(n);
        this.add(ok);
        this.add(cancel);
        this.add(superSize);
        this.add(remove);
        this.add(treeModel);
        this.add(edit);
        this.add(group);
        this.add(split);
    }

    /**
    Sets the Combo box with the correct number of Nodes to be grouped
     */
    public void setUpCombo() {
        XMLTreeNode parent = (XMLTreeNode) myParent.getNodePanel().getSelected().getParent();
        int currentI = parent.getIndex(myParent.getNodePanel().getSelected()) + 1;
        int totalC = parent.getChildCount();
        for (int i = 0; i < totalC - currentI; i++) {
            superSize.addItem(new Integer(i + 1));
        }
    }
}