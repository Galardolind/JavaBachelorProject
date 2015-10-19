package MailSender.GUI;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This class provides a dialog to choose elements in one list and append 
 * to antother one. DefaultListModel
 */
public class UserInterfaceChooseOnListDialog extends JDialog {

    /**
     * Contains name of the the dialog
     */
    String name;
    /**
     * Contains the ListModel in input of the list selection (fill the left
     * part)
     */
    DefaultListModel input;
    /**
     * Contains the Swing component that handles rendering DefaultListModel
     * input
     */
    JList input_list_f;
    /**
     * Contains the ListModel of the processed list
     */
    DefaultListModel temp;
    /**
     * Contains the ListModel in output of the list selection (fill the right
     * part)
     */
    DefaultListModel output;
    /**
     * Contains the returned ListModel (processed final)
     */
    DefaultListModel result;
    /**
     * Contains the swing component handling DefaultListModel output
     */
    JList output_list_f;
    /**
     * Contains the boolean that handles process list comportement true -> cut
     * the element from the input list to the output, (xor systeme) false ->
     * copy the element from the input list to the output.
     */
    boolean popOnMove;
    /**
     * Gui Components
     */
    JPanel mainPanel;
    JPanel left_panel;
    JPanel center_panel;
    JPanel right_panel;
    JPanel bottom_panel;
    JScrollPane INList_scroll;
    JScrollPane OUTList_scroll;
    JButton append;
    JButton append_all;
    JButton pop_all;
    JButton pop;
    JButton ok_button;
    JButton cancel_button;

    /**
     * Constructor.
     *
     * Generate a JDialog like object, with two list and buttons to appen pop,
     * from one list to other.
     *
     * @param frame Owner of the frame, maybe null.
     * @param name Name of the frame, may not be null.
     * @param input_list DefaultListModel use for the left panel.
     * @param output_list DefaultListModel use for the right panel.
     * @param popOnMove Boolean that defines if append cut or copy from the
     * input list.
     */
    public UserInterfaceChooseOnListDialog(Frame frame, String name, DefaultListModel input_list, DefaultListModel output_list, boolean popOnMove) {

        //init vars.
        super(frame, name, true);
        this.name = name;
        this.input = input_list;
        this.output = output_list;
        this.temp = null;
        this.popOnMove = popOnMove;

        //init environnement comportement
        result = null;
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //try to redefine interface look (windows like)
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows."
                    + "WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        //init gui
        initGui();

        //set gui general settings.
        this.setMinimumSize(new Dimension(500, 300));
        this.setResizable(false);

        //location : always middle of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int) (((screenSize.getWidth() / 2.0) - (this.getWidth() / 2.0))), (int) (((screenSize.getHeight() / 2.0) - (this.getHeight() / 2.0))));

        //init event listener
        initEvent();

    }

    /**
     * Initialize layouts and set components localisations.
     */
    private void initGui() {
        //init Jpanels
        mainPanel = new JPanel();
        left_panel = new JPanel();
        center_panel = new JPanel();
        right_panel = new JPanel();
        bottom_panel = new JPanel();

        //init ListModels
        this.input_list_f = new JList(this.input);
        if (this.output == null) {
            this.output = new DefaultListModel();
        }
        this.output_list_f = new JList(this.output);

        //init ListScrolls
        INList_scroll = new JScrollPane(this.input_list_f);
        OUTList_scroll = new JScrollPane(this.output_list_f);

        //init Jbuttons
        append = new JButton(">");
        append_all = new JButton(">>");
        pop_all = new JButton("<<");
        pop = new JButton("<");
        ok_button = new JButton("Ok");
        cancel_button = new JButton("Cancel");

        //init Layout managing
        GridBagLayout gdl_main = new GridBagLayout();
        GridBagConstraints gdc_main = new GridBagConstraints();
        mainPanel.setLayout(gdl_main);
        gdc_main.fill = GridBagConstraints.BOTH;
        gdc_main.weightx = 1.0;
        gdc_main.weighty = 1.0;
        gdc_main.gridx = 0;
        gdc_main.gridy = 0;
        mainPanel.add(left_panel, gdc_main);
        gdc_main.weightx = 0;
        gdc_main.weighty = 1.0;
        gdc_main.gridx = 1;
        gdc_main.gridy = 0;
        mainPanel.add(center_panel, gdc_main);
        gdc_main.weightx = 1.0;
        gdc_main.weighty = 1.0;
        gdc_main.gridx = 2;
        gdc_main.gridy = 0;
        mainPanel.add(right_panel, gdc_main);
        gdc_main.anchor = GridBagConstraints.EAST;
        gdc_main.weightx = 1.0;
        gdc_main.weighty = 0;
        gdc_main.gridx = 0;
        gdc_main.gridy = 1;
        gdc_main.gridwidth = 3;
        mainPanel.add(bottom_panel, gdc_main);

        GridBagConstraints gdc_left = new GridBagConstraints();
        left_panel.setLayout(gdl_main);
        gdc_left.fill = GridBagConstraints.BOTH;
        gdc_left.gridx = 0;
        gdc_left.gridy = 0;
        gdc_left.weightx = 1.0;
        gdc_left.weighty = 1.0;
        left_panel.add(INList_scroll, gdc_left);

        GridBagConstraints gdc_right = new GridBagConstraints();
        right_panel.setLayout(gdl_main);
        gdc_right.fill = GridBagConstraints.BOTH;
        gdc_right.gridx = 0;
        gdc_right.gridy = 0;
        gdc_right.weightx = 1.0;
        gdc_right.weighty = 1.0;
        right_panel.add(OUTList_scroll, gdc_right);

        GridBagConstraints gdc_mid = new GridBagConstraints();
        center_panel.setLayout(gdl_main);
        gdc_mid.fill = GridBagConstraints.BOTH;
        gdc_mid.insets = new Insets(0, 5, 5, 5);
        gdc_main.weighty = 1.0;
        gdc_mid.gridx = 0;
        gdc_mid.gridy = 0;
        center_panel.add(append, gdc_mid);
        gdc_mid.insets = new Insets(5, 5, 10, 5);
        gdc_mid.gridx = 0;
        gdc_mid.gridy = 1;
        center_panel.add(append_all, gdc_mid);
        gdc_mid.insets = new Insets(10, 5, 5, 5);
        gdc_mid.gridx = 0;
        gdc_mid.gridy = 2;
        center_panel.add(pop_all, gdc_mid);
        gdc_mid.insets = new Insets(5, 5, 0, 5);
        gdc_mid.gridx = 0;
        gdc_mid.gridy = 3;
        center_panel.add(pop, gdc_mid);

        //ajout des boutons de validation :
        GridBagConstraints gdc_apply = new GridBagConstraints();
        bottom_panel.setLayout(gdl_main);
        gdc_apply.fill = GridBagConstraints.BOTH;
        gdc_apply.anchor = GridBagConstraints.WEST;
        gdc_apply.insets = new Insets(10, 5, 0, 5);
        gdc_apply.gridx = 0;
        gdc_apply.gridy = 0;
        bottom_panel.add(ok_button, gdc_apply);
        gdc_apply.gridx = 1;
        gdc_apply.gridy = 0;
        bottom_panel.add(cancel_button, gdc_apply);

        //ajout principal
        GridBagConstraints gdc_sup = new GridBagConstraints();
        this.setLayout(gdl_main);
        gdc_sup.fill = GridBagConstraints.BOTH;
        gdc_sup.gridx = 0;
        gdc_sup.gridy = 0;
        gdc_sup.weightx = 1.0;
        gdc_sup.weighty = 1.0;
        gdc_sup.insets = new Insets(10, 10, 10, 10);
        this.add(mainPanel, gdc_sup);

        //rigidification de la colonne centrale 
        center_panel.setPreferredSize(new Dimension(60, 40));
        left_panel.setPreferredSize(new Dimension(100, 40));
        right_panel.setPreferredSize(new Dimension(100, 40));
    }

    /**
     * Initialize Event Listeners.
     */
    private void initEvent() {
        ok_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                apply_event(ae);
            }
        });

        cancel_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                cancel_event(ae);
            }
        });

        append.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                append_event(ae);
            }
        });

        append_all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                append_all_event(ae);
            }
        });

        pop_all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pop_all_event(ae);
            }
        });

        pop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pop_event(ae);
            }
        });

    }

    /**
     * action of the OK button action performed envent.
     *
     * @param ae
     */
    private void apply_event(ActionEvent ae) {
        this.setVisible(false);
        this.result = this.output;
        dispose();
    }

    /**
     * action of the OK button action performed envent.
     *
     * @param ae
     */
    private void cancel_event(ActionEvent ae) {
        this.setVisible(false);
        this.result = null;
        dispose();
    }

    /**
     * action of the ">" button (append) action performed envent.
     *
     * @param ae
     */
    private void append_event(ActionEvent ae) {
        if (popOnMove) {
            this.temp = this.input;
            for (Iterator it = this.input_list_f.getSelectedValuesList().iterator(); it.hasNext();) {
                Object elmt = it.next();
                if (!this.output.contains(elmt)) {
                    this.output.addElement(elmt);
                    this.temp.remove(this.input.indexOf(elmt));
                }
            }
            this.input = this.temp;
            deselectList();
        } else {
            for (Iterator it = this.input_list_f.getSelectedValuesList().iterator(); it.hasNext();) {
                Object elmt = it.next();
                if (!this.output.contains(elmt)) {
                    this.output.addElement(elmt);
                }
            }
            deselectList();
        }
    }

    /**
     * action of the pop button action performed envent.
     *
     * @param ae
     */
    private void pop_event(ActionEvent ae) {
        if (popOnMove) {
            this.temp = this.output;
            for (Iterator it = this.output_list_f.getSelectedValuesList().iterator(); it.hasNext();) {
                Object elmt = it.next();
                if (!this.input.contains(elmt)) {
                    this.input.addElement(elmt);
                    this.temp.remove(this.output.indexOf(elmt));
                }
            }
            this.output = this.temp;
            deselectList();
        } else {
            for (Iterator it = this.output_list_f.getSelectedValuesList().iterator(); it.hasNext();) {
                Object elmt = it.next();
                this.output.removeElement(elmt);
            }
            deselectList();
        }
    }

    /**
     * action of the pop all button action performed envent.
     *
     * @param ae
     */
    private void pop_all_event(ActionEvent ae) {
        if (popOnMove) {
            this.temp = this.output;
            for (Iterator it = Arrays.asList(this.temp.toArray()).iterator(); it.hasNext();) {
                Object elmt = it.next();
                this.input.addElement(elmt);
                this.temp.remove(this.output.indexOf(elmt));
            }
            this.output = this.temp;
            deselectList();
        } else {
            this.output.clear();
            deselectList();
        }
    }

    /**
     * action of the append all button action performed envent.
     *
     * @param ae
     */
    private void append_all_event(ActionEvent ae) {
        if (popOnMove) {
            this.temp = this.input;
            for (Iterator it = Arrays.asList(this.temp.toArray()).iterator(); it.hasNext();) {
                Object elmt = it.next();
                this.output.addElement(elmt);
                this.temp.remove(this.input.indexOf(elmt));
            }
            this.input = this.temp;
            deselectList();
        } else {
            this.output.clear();
            for (Iterator it = Arrays.asList(this.input.toArray()).iterator(); it.hasNext();) {
                Object elmt = it.next();
                this.output.addElement(elmt);
            }
            deselectList();
        }
    }

    /**
     * Displays the dialog on screen, calling frame is disabled during the
     * operation. a defaultListModel is return whenever the dialog is closed
     * (exit, cancel, apply ...) if cancel was pushed, null is returned. else a
     * defaultListModel is returned.
     *
     * @return DefaultListModel
     */
    public DefaultListModel ShowDialog() {
        setVisible(true);
        return this.result;
    }

    /**
     * set the input list deselected
     */
    private void deselectList() {
        this.input_list_f.setSelectedIndices(new int[0]);
        this.output_list_f.setSelectedIndices(new int[0]);
    }

    /**
     * copy the l2 list in l1. (l1 is cleared)
     *
     * @param l1
     * @param l2
     */
    public static void setListModelAs(DefaultListModel l1, DefaultListModel l2) {
        l1.removeAllElements();
        for (int i = 0; i < l2.size(); i++) {
            l1.addElement(l2.elementAt(i));
        }
    }
}
