package MailSender.GUI;

import MailSender.CoreClasses.DatabaseHandler;
import MailSender.CoreClasses.ExceptionSender;
import MailSender.CoreClasses.Template;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * This class provides a template Editor, generate, edit, delete templates
 * extended from JPanel.
 */
public class UserInterfaceTemplateEditor extends JPanel {

    /**
     * FileChooser Dialog for the path of the database.
     */
    JFileChooser chooser;
    /**
     * Panels
     */
    JPanel panel_template = new JPanel();
    JPanel panel_template_border = new JPanel();
    JPanel panel_column_options = new JPanel();
    JPanel panel_options_zone_template = new JPanel();
    JPanel panel_options_zone_vars = new JPanel();
    JPanel panel_options_zone_main = new JPanel();
    JPanel panel_options_zone_new = new JPanel();
    JPanel variables_interdites = new JPanel();
    JPanel variables_perso = new JPanel();
    JPanel variables_reserved = new JPanel();
    /**
     * Template editor area.
     */
    StyledDocument template_text_field = new DefaultStyledDocument();
    JTextPane textPane = new JTextPane(template_text_field) {
        @Override
        public boolean getScrollableTracksViewportWidth() {
            Component parent = getParent();
            ComponentUI ui = getUI();

            return parent != null ? (ui.getPreferredSize(this).width <= parent
                    .getSize().width) : true;
        }
    };
    Style style = textPane.addStyle("var", null);
    Style styleNormal = textPane.addStyle("normal", null);
    JScrollPane template_scroll_pane = new JScrollPane(textPane);
    JButton html_viewer = new JButton("View");
    /**
     * Variables
     */
    JLabel vareoo = new JLabel("$$template_EOO");
    JLabel vareof = new JLabel("$$template_EOF");
    JLabel var_name = new JLabel("$$var_name : ");
    JLabel var_surname = new JLabel("$$var_surname : ");
    JLabel var_mail = new JLabel("$$var_mail");
    JLabel var_servermail = new JLabel("$$var_servermail");
    JTextField var_name_field = new JTextField();
    JTextField var_surname_field = new JTextField();
    JLabel template_name_label = new JLabel("Template name : ");
    JTextField template_name_field = new JTextField();
    JLabel template_subject_label = new JLabel("Subject : ");
    JTextField template_subject_field = new JTextField();
    JButton options_zone_template_new = new JButton("New");
    JButton options_zone_template_delete = new JButton("Delete");
    JButton options_zone_template_edit = new JButton("Edit");
    JButton options_zone_template_preview = new JButton("Preview");
    JButton options_zone_main_envoi = new JButton("Save");
    DefaultComboBoxModel<Template> templateList = new DefaultComboBoxModel<>();
    JComboBox test = new JComboBox(templateList);
    DatabaseHandler dbSystem;
    String oldName;
    boolean edit;
    String[][] data_ = {{"$$var_name", "green"}, {"$$var_surname", "green"}, {"$$var_mail", "blue"}, {"$$var_servermail", "blue"}, {"$$template_EOO", "red"}, {"$$template_EOF", "red"}};
    int validTemplate = 0;
    UserInterfaceHtmlPreviewDialog htmlRenderer;

    /**
     * Constructor.
     *
     * Generate an Instance of UserInterfaceTemplateEditor whose extended from
     * JPanel
     */
    public UserInterfaceTemplateEditor() {
        StyleConstants.setForeground(this.style, Color.RED);
        StyleConstants.setForeground(this.styleNormal, Color.BLACK);

        oldName = "";
        edit = false;

        initEnv();
        initBinds();
    }

    /**
     * Initialized bindings end event processing
     */
    private void initBinds() {
        textPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (true) {
                    //if (ke.getKeyCode() == 10){
                    try {
                        validTemplate = forelighter(data_);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(UserInterfaceTemplateEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if (true) {
                    //if (ke.getKeyCode() == 10){
                    try {
                        validTemplate = forelighter(data_);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(UserInterfaceTemplateEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        options_zone_template_new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                newTemplate();
            }
        });

        options_zone_template_edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (test.getSelectedItem() != null) {
                    editTemplate((Template) test.getSelectedItem());
                }
            }
        });

        options_zone_main_envoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (validTemplate != 0) {
                    JOptionPane.showMessageDialog(null, "The template has one or more term (s) banned (s). \n Please correct the error before saving.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!template_name_field.getText().isEmpty()) { //si nom pas vide
                    int conf = confirmTemplateOverwrite();
                    if (conf == 1) {
                        try {
                            dbSystem.deleteTemplate(oldName);
                        } catch (ExceptionSender ex) {
                            ex.printStackTrace();
                        }
                        saveCurrentTemplate();
                    } else if (conf == 0) {
                        saveCurrentTemplate();
                    } else {
                    }
                }
            }
        });

        options_zone_template_preview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                actionPreview();
            }
        });

        options_zone_template_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                deleteTemplate();
            }
        });
    }

    /**
     * Initialize environnement, and componenet localisation.
     */
    private void initEnv() {
        GridBagLayout alpha = new GridBagLayout();
        panel_template_border.setLayout(alpha);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.85;
        c.insets = new Insets(10, 10, 0, 10);
        c.gridx = 1;
        c.gridy = 0;
        panel_template_border.add(template_name_field, c);
        c.insets = new Insets(10, 15, 0, 10);
        c.weightx = 0.15;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 5;
        panel_template_border.add(template_name_label, c);
        c.weightx = 0.85;
        c.insets = new Insets(10, 10, 0, 10);
        c.gridx = 1;
        c.gridy = 1;
        panel_template_border.add(template_subject_field, c);
        c.insets = new Insets(10, 15, 0, 10);
        c.weightx = 0.15;
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 5;
        panel_template_border.add(template_subject_label, c);
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        panel_template_border.add(template_scroll_pane, c);
        c.insets = null;

        GridBagLayout beta = new GridBagLayout();
        GridBagConstraints mgc = new GridBagConstraints();
        panel_column_options.setLayout(beta);
        mgc.fill = GridBagConstraints.BOTH;
        mgc.anchor = GridBagConstraints.NORTHWEST;
        mgc.weightx = 1.0;
        mgc.weighty = 0;
        mgc.gridx = 0;
        mgc.gridy = 0;
        panel_column_options.add(variables_perso, mgc);
        mgc.gridx = 0;
        mgc.gridy = 1;
        panel_column_options.add(variables_reserved, mgc);
        mgc.gridx = 0;
        mgc.gridy = 2;
        panel_column_options.add(variables_interdites, mgc);
        mgc.weightx = 1.0;
        mgc.weighty = 1.0;
        mgc.gridx = 0;
        mgc.gridy = 3;
        panel_column_options.add(new JPanel(), mgc);
        mgc.weightx = 1.0;
        mgc.weighty = 0;
        mgc.gridx = 0;
        mgc.gridy = 4;
        panel_column_options.add(panel_options_zone_template, mgc);
        mgc.gridx = 0;
        mgc.gridy = 5;
        panel_column_options.add(panel_options_zone_new, mgc);
        panel_column_options.setMinimumSize(new Dimension(250, panel_column_options.getHeight()));
        panel_column_options.setPreferredSize(new Dimension(250, panel_column_options.getHeight()));
        panel_column_options.setMaximumSize(new Dimension(250, panel_column_options.getHeight()));

        variables_perso.setLayout(beta);
        GridBagConstraints kgc = new GridBagConstraints();
        kgc.insets = new Insets(10, 10, 0, 0);
        kgc.fill = GridBagConstraints.BOTH;
        kgc.anchor = GridBagConstraints.BASELINE_LEADING;
        kgc.gridx = 0;
        kgc.gridy = 0;
        kgc.weightx = 0;
        kgc.weighty = 0;
        variables_perso.add(var_name, kgc);
        kgc.insets = new Insets(5, 5, 0, 10);
        kgc.gridx = 1;
        kgc.gridy = 0;
        kgc.weightx = 1.0;
        kgc.weighty = 0;
        variables_perso.add(var_name_field, kgc);
        kgc.insets = new Insets(5, 10, 10, 0);
        kgc.gridx = 0;
        kgc.gridy = 1;
        kgc.weightx = 0;
        kgc.weighty = 0;
        variables_perso.add(var_surname, kgc);
        kgc.insets = new Insets(5, 5, 10, 10);
        kgc.gridx = 1;
        kgc.gridy = 1;
        kgc.weightx = 1.0;
        kgc.weighty = 0;
        variables_perso.add(var_surname_field, kgc);
        var_surname.setForeground(new Color(0, 153, 0));
        var_name.setForeground(new Color(0, 153, 0));

        variables_reserved.setLayout(beta);
        kgc.insets = new Insets(10, 10, 0, 10);
        kgc.anchor = GridBagConstraints.BASELINE_LEADING;
        kgc.gridx = 0;
        kgc.gridy = 0;
        kgc.weightx = 1.0;
        kgc.weighty = 0;
        variables_reserved.add(var_mail, kgc);
        kgc.insets = new Insets(0, 10, 10, 10);
        kgc.gridx = 0;
        kgc.gridy = 1;
        kgc.weightx = 1.0;
        kgc.weighty = 0;
        variables_reserved.add(var_servermail, kgc);
        var_mail.setForeground(Color.blue);
        var_servermail.setForeground(Color.blue);

        variables_interdites.setLayout(beta);
        kgc.insets = new Insets(10, 10, 0, 10);
        kgc.anchor = GridBagConstraints.BASELINE_LEADING;
        kgc.gridx = 0;
        kgc.gridy = 0;
        kgc.weightx = 1.0;
        kgc.weighty = 0;
        variables_interdites.add(vareoo, kgc);
        kgc.insets = new Insets(0, 10, 10, 10);
        kgc.gridx = 0;
        kgc.gridy = 1;
        kgc.weightx = 1.0;
        kgc.weighty = 0;
        variables_interdites.add(vareof, kgc);
        vareoo.setForeground(Color.red);
        vareof.setForeground(Color.red);


        //template grid
        panel_options_zone_template.setLayout(beta);
        GridBagConstraints tgc = new GridBagConstraints();
        tgc.fill = GridBagConstraints.BOTH;
        tgc.insets = new Insets(10, 10, 10, 10);
        tgc.weightx = 1.0;
        tgc.weighty = 0;
        tgc.gridwidth = 2;
        tgc.gridx = 0;
        tgc.gridy = 0;
        panel_options_zone_template.add(test, tgc); // ---------------------------------> change to combo Box
        tgc.weighty = 1.0;
        tgc.gridwidth = 1;
        tgc.insets = new Insets(0, 10, 10, 10);
        tgc.gridx = 0;
        tgc.gridy = 1;
        panel_options_zone_template.add(options_zone_template_edit, tgc);
        tgc.insets = new Insets(0, 10, 10, 10);
        tgc.gridx = 1;
        tgc.gridy = 1;
        panel_options_zone_template.add(options_zone_template_delete, tgc);


        //vars
        panel_options_zone_new.setLayout(beta);
        GridBagConstraints ttc = new GridBagConstraints();
        ttc.fill = GridBagConstraints.BOTH;
        ttc.insets = new Insets(10, 10, 5, 5);
        ttc.weighty = 0;
        ttc.weightx = 0.5;
        ttc.gridx = 0;
        ttc.gridy = 0;
        ttc.ipady = 20;
        panel_options_zone_new.add(options_zone_template_new, ttc);
        ttc.insets = new Insets(10, 0, 5, 10);
        ttc.gridx = 1;
        ttc.gridy = 0;
        panel_options_zone_new.add(options_zone_template_preview, ttc);
        ttc.insets = new Insets(0, 10, 10, 10);
        ttc.gridx = 0;
        ttc.gridy = 1;
        ttc.weightx = 1.0;
        ttc.gridwidth = 2;
        ttc.ipady = 20;
        panel_options_zone_new.add(options_zone_main_envoi, ttc);

        //inclusions mineures
        GridBagLayout template_layout = new GridBagLayout();
        GridBagConstraints solo = new GridBagConstraints();
        panel_template.setLayout(template_layout);
        solo.fill = GridBagConstraints.BOTH;
        solo.weightx = 1.0;
        solo.weighty = 1.0;
        solo.gridwidth = 1;
        solo.gridx = 0;
        solo.gridy = 1;
        panel_template.add(panel_template_border, solo);

        //inclusions majeures        
        GridBagLayout ender = new GridBagLayout();
        this.setLayout(ender);
        GridBagConstraints supgc = new GridBagConstraints();
        supgc.fill = GridBagConstraints.BOTH;
        supgc.insets = new Insets(5, 5, 5, 5);
        supgc.weightx = 1.0;
        supgc.weighty = 1.0;
        supgc.gridwidth = 1;
        supgc.gridx = 0;
        supgc.gridy = 0;
        this.add(panel_template, supgc);
        supgc.fill = GridBagConstraints.VERTICAL;
        supgc.anchor = GridBagConstraints.NORTHWEST;
        supgc.weighty = 1.0;
        supgc.weightx = 0;
        supgc.gridx = 1;
        supgc.gridy = 0;
        this.add(panel_column_options, supgc);
        panel_column_options.setBackground(Color.LIGHT_GRAY);

        //borders

        panel_options_zone_new.setBorder(BorderFactory.createTitledBorder(" Funct "));
        panel_template_border.setBorder(BorderFactory.createTitledBorder(" Template "));
        panel_options_zone_vars.setBorder(BorderFactory.createTitledBorder(" Variables "));
        panel_options_zone_template.setBorder(BorderFactory.createTitledBorder(" Template Set "));
        panel_options_zone_main.setBorder(BorderFactory.createTitledBorder(" Main "));
        variables_perso.setBorder(BorderFactory.createTitledBorder("Variables Personalisables"));
        variables_reserved.setBorder(BorderFactory.createTitledBorder("Variables Reservées"));
        variables_interdites.setBorder(BorderFactory.createTitledBorder("Variables Interdites"));
    }

    /**
     * Give a reference of the dbSystem. MUST BE setted. this is set here
     * beacause the constructor copy the instance instead of taking reference.
     *
     * @param dbSystem DatabaseHandler
     */
    public void setDatabaseHandler(DatabaseHandler dbSystem) {
        this.dbSystem = dbSystem;
        refreshTemplateList();
    }

    /**
     * refresh Template List
     */
    public void refreshTemplateList() {
        DefaultComboBoxModel<Template> essai = (DefaultComboBoxModel<Template>) test.getModel();
        essai.removeAllElements();
        if (this.dbSystem.getTemplateList() != null) {
            ArrayList<Template> templates = this.dbSystem.getTemplateList();
            for (Template template : templates) {
                essai.addElement(template);
            }
        }
    }

    /**
     *
     */
    public void actionPreview() {
        htmlRenderer = new UserInterfaceHtmlPreviewDialog(null);
        htmlRenderer.ShowDialog(getObjectTemplate().getCleanContent());
    }

    /**
     * @deprecated
     *
     * This function is deprecated, foreligther(String[][] data) would done it
     * in a better way, or with a better looking.
     *
     * @param data
     * @return
     * @throws BadLocationException
     */
    @Deprecated
    private int hightlighter(String[][] data) throws BadLocationException { //String word, boolean accepted
        //System.out.println("Highlighting ...");
        int except = 0;
        this.textPane.getHighlighter().removeAllHighlights();
        String text = this.textPane.getText();
        int tailleMax = text.length();
        String wordProcessing = data[0][0];
        int MarkerStartWordPos = text.indexOf(data[0][0], 0);
        int markerStart = 0;
        int markerEnd = 0;
        while (true) {
            Color colorHigh;
            for (int i = 0; i < data.length; i++) {
                //System.out.println("word sync " + data[i][0]);
                MarkerStartWordPos = text.indexOf(data[i][0], markerStart);
                if (MarkerStartWordPos > -1) {
                    MarkerStartWordPos = text.indexOf(data[i][0], markerStart);
                    wordProcessing = data[i][0];
                    //System.out.println("word process : " + wordProcessing + " @ " + MarkerStartWordPos);
                    switch (data[i][1]) {
                        case "orange":
                            colorHigh = Color.orange;
                            break;
                        case "yellow":
                            colorHigh = Color.yellow;
                            break;
                        case "red":
                            colorHigh = Color.red;
                            except++;
                            break;
                        case "blue":
                            colorHigh = Color.blue;
                            break;
                        case "green":
                            colorHigh = new Color(0, 153, 0);
                            break;
                        case "cyan":
                            colorHigh = Color.cyan;
                            break;
                        case "simpleBlue":
                            colorHigh = new Color(51, 204, 255);
                            break;
                        default:
                            colorHigh = Color.cyan;
                            break;
                    }
                    this.textPane.getHighlighter().addHighlight(MarkerStartWordPos, MarkerStartWordPos + wordProcessing.length(), new DefaultHighlighter.DefaultHighlightPainter(colorHigh));
                } else {
                    //System.out.println("fail " + wordProcessing + " @ " + MarkerStartWordPos);
                }
            }
            markerStart = markerStart + MarkerStartWordPos + wordProcessing.length();
            if (markerStart > tailleMax) {
                break;
            }
        }
        return except;
    }

    /**
     * this function provides an highlighting of strings passed in argument.
     * data has to be formatted like :
     *
     * ["wordToHightLight",color]
     *
     * color has to in ["orange", "yellow", "red", "blue", "green", "cyan",
     * "simpleBlue"]. another color will be hightlighted in "orange"
     *
     * @param data
     * @return
     * @throws BadLocationException
     */
    private int forelighter(String[][] data) throws BadLocationException { //String word, boolean accepted
        //System.out.println("Highlighting ...");
        int except = 0;
        this.textPane.getHighlighter().removeAllHighlights();
        String text = this.textPane.getText();
        int tailleMax = text.length();
        String wordProcessing = data[0][0];
        int MarkerStartWordPos = text.indexOf(data[0][0], 0);
        int markerStart = 0;
        int markerEnd = 0;
        this.template_text_field.setCharacterAttributes(0, this.textPane.getText().length(), styleNormal, true);
        while (true) {
            Color colorHigh;
            for (int i = 0; i < data.length; i++) {
                //System.out.println("word sync " + data[i][0]);
                MarkerStartWordPos = text.indexOf(data[i][0], markerStart);
                if (MarkerStartWordPos > -1) {
                    MarkerStartWordPos = text.indexOf(data[i][0], markerStart);
                    wordProcessing = data[i][0];
                    //System.out.println("word process : " + wordProcessing + " @ " + MarkerStartWordPos);
                    switch (data[i][1]) {
                        case "orange":
                            colorHigh = Color.orange;
                            break;
                        case "yellow":
                            colorHigh = Color.yellow;
                            break;
                        case "red":
                            colorHigh = Color.red;
                            except++;
                            break;
                        case "blue":
                            colorHigh = Color.blue;
                            break;
                        case "green":
                            colorHigh = new Color(0, 153, 0);
                            break;
                        case "cyan":
                            colorHigh = Color.cyan;
                            break;
                        case "simpleBlue":
                            colorHigh = new Color(51, 204, 255);
                            break;
                        default:
                            colorHigh = Color.orange;
                            break;
                    }
                    //this.textPane.getHighlighter().addHighlight(MarkerStartWordPos,MarkerStartWordPos + wordProcessing.length(),new DefaultHighlighter.DefaultHighlightPainter(colorHigh));
                    StyleConstants.setForeground(this.style, colorHigh);
                    StyleConstants.setBold(style, true);
                    //StyleConstants.setUnderline(style, true);
                    this.template_text_field.setCharacterAttributes(MarkerStartWordPos, wordProcessing.length(), style, true);

                } else {
                    //System.out.println("fail " + wordProcessing + " @ " + MarkerStartWordPos);
                }
            }
            markerStart = markerStart + MarkerStartWordPos + wordProcessing.length();
            if (markerStart > tailleMax) {
                break;
            }
        }
        return except;
    }

    /**
     * return a String Array of the variables
     *
     * @return
     */
    private String[][] getTableData() {
        String[][] result = {{"$$var_name", var_name_field.getText()}, {"$$var_surname", var_surname_field.getText()}};
        return result;
    }

    /**
     * generate String variables from data array
     * 
     * @param data
     * @return
     */
    private String formatVariables(String[][] data) {
        String result = "";
        for (int i = 0; i < data.length; i++) {
            result += data[i][0] + " " + data[i][1] + "\n";
        }
        return result;
    }

    /**
     * generate a Content Template of the 
     *
     * @return
     */
    private String makeTemplate() {
        String result;
        result = template_subject_field.getText() + "\n$$template_EOO\n" + textPane.getText() + "\n$$template_EOF\n" + formatVariables(getTableData());
        return result;
    }

    /**
     * generate a Template with the name, and the content from the frame
     *
     * @return
     */
    public Template getObjectTemplate() {
        return new Template(this.template_name_field.getText(), makeTemplate());
    }

    /**
     * fill the field with empty strings.
     */
    private void newTemplate() {
        edit = false;
        template_name_field.setText("");
        template_subject_field.setText("");
        textPane.setText("");
        var_name_field.setText("");
        var_surname_field.setText("");
    }

    /**
     * fill the fields with templates fields in arg.
     * 
     * @param t
     */
    public void editTemplate(Template t) {
        edit = true;
        template_name_field.setText(t.getName());
        oldName = t.getName();
        template_subject_field.setText(t.getObject());
        textPane.setText(t.getCleanContent());
        textPane.setCaretPosition(0);
        var_name_field.setText(t.getVarsAndValuesHash().get("$$var_name"));
        var_surname_field.setText(t.getVarsAndValuesHash().get("$$var_surname"));

        try {
            validTemplate = forelighter(data_);
        } catch (BadLocationException ex) {
        }
        refreshTemplateList();
    }

    /**
     * delete the template in bd.
     *
     */
    public void deleteTemplate() {
        if (test.getSelectedItem() != null) {

            Template temp = (Template) test.getSelectedItem();
            int option = JOptionPane.showConfirmDialog(null, "Do you want to remove " + temp.getName() + " ?");
            if (option == JOptionPane.YES_OPTION) {
                if (temp.getName().equals(template_name_field.getText())) {
                    if (temp.getCleanContent().equals(textPane.getText())) {
                        if (temp.getObject().equals(template_subject_field.getText())) {
                            newTemplate();
                        }
                    }
                }
                try {
                    this.dbSystem.deleteTemplate(temp.getName());
                    refreshTemplateList();
                } catch (ExceptionSender ex) {
                    ex.printStackTrace();
                }


            }
        }
    }

    /**
     * save template in bd.
     *
     */
    public void saveCurrentTemplate() {
        Template item = getObjectTemplate();
        try {
            this.dbSystem.saveTemplate(item);
        } catch (ExceptionSender ex) {
            ex.printStackTrace();
        }
        this.refreshTemplateList();
        edit = true;
        oldName = template_name_field.getText();
    }

    /**
     * Check if a template in the combo box has the same name.
     *
     * @return postion of item or -1 if not in combo box
     */
    public int isTemplateNameExist() {
        DefaultComboBoxModel<Template> essai = (DefaultComboBoxModel<Template>) test.getModel();
        Template t;
        for (int i = 0; i < essai.getSize(); i++) {
            t = (Template) essai.getElementAt(i);
            if (t.name.equals(this.oldName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * verify if the content template is eligible to a formal template. 
     */
    public int confirmTemplateOverwrite() {
        int option = JOptionPane.CANCEL_OPTION;
        if (!oldName.equals(template_name_field.getText()) && edit) {
            Template template = this.dbSystem.getTemplate(template_name_field.getText());
            if (template == null) {
                option = JOptionPane.showConfirmDialog(null, "Would you rename : " + oldName + " by " + template_name_field.getText() + " ?\n\n"
                        + "  -Press Yes erase the old one.\n"
                        + "  -Press No save a new one.\n"
                        + "  -Press Cancel to exit without saving.\n\n");
            } else {
                option = JOptionPane.showConfirmDialog(null, "This template already exist : " + template_name_field.getText() + " ,would you overwrite this template ?\n\n"
                        + "  -Press Yes erase the old one.\n"
                        + "  -Press No to exit without saving.\n"
                        + "  -Press Cancel to exit without saving.\n\n");
                if (option == JOptionPane.NO_OPTION) {
                    option = JOptionPane.CANCEL_OPTION;
                }
            }
        } else {
            Template template = this.dbSystem.getTemplate(template_name_field.getText());
            if (template != null) {
                option = JOptionPane.showConfirmDialog(null, "This template already exist : " + template_name_field.getText() + " ,would you overwrite this template ?\n\n"
                        + "  -Press Yes erase the old one.\n"
                        + "  -Press No to exit without saving.\n"
                        + "  -Press Cancel to exit without saving.\n\n");
            } else {
                option = JOptionPane.showConfirmDialog(null, "Would you save : " + template_name_field.getText() + " ?\n\n"
                        + "  -Press Yes to save.\n"
                        + "  -Press No to exit without saving.\n"
                        + "  -Press Cancel to exit without saving.\n\n");
            }
            if (option == JOptionPane.NO_OPTION) {
                option = JOptionPane.CANCEL_OPTION;
            } else if (option == JOptionPane.YES_OPTION) {
                option = JOptionPane.NO_OPTION;
            }
        }
        if (option == JOptionPane.YES_OPTION) {
            return 1;
        } else if (option == JOptionPane.NO_OPTION) {
            return 0;
        } else {
            return 2;
        }
    }
}
