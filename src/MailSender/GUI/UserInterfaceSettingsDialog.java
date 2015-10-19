package MailSender.GUI;

import MailSender.CoreClasses.ExceptionSender;
import MailSender.CoreClasses.SimpleSettings;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This Class provides a JDialog extended components and handle generation, 
 * edit of simpleSettings instances. * 
 */
public class UserInterfaceSettingsDialog extends JDialog{
    
    /**
     * HINT : FULL, if passed in parameter, will generate a full interface with 
     * every options
     */
    static final int FULL = 1; 
    /**
     * HINT : SIMPLE, if passed in parameter, will generate a simple interface,
     * with only-send about settings.
     */
    static final int SIMPLE = 2;
    
    static final int delay_MIN = 0;
    static final int delay_MAX = 500;
    static final int delay_INIT = 250;    //initial frames per second
    
    Frame frame;
    int type;
        
    SimpleSettings result;
    SimpleSettings user;
    
    JPanel main_panel;
    JPanel delay_panel;
    JPanel bd_path_panel;
    JPanel bottom_panel;
    JPanel smtp_sets;
    
    JLabel smtp_from;
    JLabel smtp_host;
    JTextField smtp_from_field;
    JTextField smtp_host_field;

    JLabel smtp_label;
    public JTextField smtp_field;
    JLabel smtp_port_label;
    public JTextField smtp_port_field;       
    JCheckBox smtp_auth_bool;
    
    JLabel pass_label;
    public JPasswordField pass_field;
    JLabel user_label;
    public JTextField user_field;
    
    JLabel delay_label;
    JSlider delay_slider;
    JLabel delay_label_out;
    
    JLabel path_label;
    JTextField path_field;
    JButton path_explore;
    
    JButton bottom_ok;
    JButton bottom_get_default;
    JButton bottom_cancel;
    
    JFileChooser chooser;
    
    JButton purge_history;    
    JLabel purge_warning;
    JPanel purge_panel;
    JLabel purge_warning_2;
    
    /**
     * Constructor.
     * 
     * Generate an instance of UserInterfaceSettingsDialog.
     * This dialog is modal and disable calling frame.
     * 
     * @param frame Owner of the jdialog, maybe null.
     * @param type MustBe one of HINTS <ul><li>[UserInterfaceSettingsDialog.FULL]</li><li>[UserInterfaceSettingsDialog.FULL]</li></ul>
     * @param user SimpleInstance to edit, maybe null (will create a new one)
     */
    public UserInterfaceSettingsDialog(Frame frame, int type , SimpleSettings user){
        super(frame,"Default Settings", true);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows."
                    + "WindowsLookAndFeel");
        } 
        catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        this.frame = frame;     
        this.type = type;
        this.user = user;
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        main_panel = new JPanel();
        delay_panel = new JPanel();
        bd_path_panel = new JPanel();
        bottom_panel = new JPanel();
        smtp_sets = new JPanel();

        smtp_label = new JLabel("Smtp provider");
        smtp_field = new JTextField();
        smtp_port_label = new JLabel("Port");
        smtp_port_field = new JTextField();       
        smtp_auth_bool = new JCheckBox("Authentification ?", true);

        pass_label = new JLabel("Password");
        pass_field = new JPasswordField();
        user_label = new JLabel("User");
        user_field = new JTextField();

        delay_label = new JLabel("Delay between mail");
        delay_slider = new JSlider(JSlider.HORIZONTAL,
                                      delay_MIN, delay_MAX, delay_INIT);
        delay_label_out = new JLabel(delay_INIT+"");
       
        delay_label_out.setPreferredSize(new Dimension(25, delay_label_out.getHeight()));

        path_label = new JLabel("Path bd");
        path_field = new JTextField();
        path_explore = new JButton("Explore");

        bottom_ok = new JButton("Ok");
        bottom_get_default = new JButton("Get default");
        bottom_cancel = new JButton("Cancel");
        
        smtp_host = new JLabel("Host");
        smtp_from = new JLabel("From");
        smtp_host_field = new JTextField();
        smtp_from_field = new JTextField();
        
        purge_history = new JButton("Purge");
        purge_warning = new JLabel("Warning ! this  will flush the spamming history.");
        purge_warning_2 = new JLabel("This operation can't be undone.");
        purge_warning.setForeground(Color.red);
        purge_warning_2.setForeground(Color.red);
        purge_panel = new JPanel();

        if(type == SIMPLE){
            this.setTitle("Settings");
            init_gui_simple();
            fill_gui_simple();
        }        
        else if(type == FULL){
            init_gui_full();  
            fill_gui_full();
        }
        else{
            
        }        
    }
    
    /**
     * initialize gui if FULL Hint has been pased
     */
    private void init_gui_full(){
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gdc = new GridBagConstraints();
        this.setLayout(gbl);
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.ipady = 0;
        //gdc.ipadx = 5;
        gdc.insets = new Insets(5,5,5,5);
        gdc.fill = GridBagConstraints.BOTH;
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        this.add(main_panel, gdc);
        
        main_panel.setLayout(gbl);
        gdc.insets = new Insets(5,5,5,5);
        gdc.gridx = 0;
        gdc.gridy = 0;
        main_panel.add(smtp_sets,gdc);
        gdc.gridx = 0;
        gdc.gridy = 1;
        main_panel.add(delay_panel, gdc);
        gdc.gridx = 0;
        gdc.gridy = 2;
        main_panel.add(bd_path_panel, gdc);
        gdc.gridx = 0;
        gdc.gridy = 3;
        main_panel.add(purge_panel, gdc);
        gdc.gridx = 0;
        gdc.gridy = 4;
        main_panel.add(bottom_panel,gdc);
        
        smtp_sets.setLayout(gbl);
        gdc.insets = new Insets(2,5,2,5);
        gdc.gridwidth = 0;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        smtp_sets.add(smtp_host,gdc);
        gdc.gridwidth = 2;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 1;
        gdc.gridy = 0;
        smtp_sets.add(smtp_host_field,gdc);
        gdc.gridwidth = 0;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 1;
        smtp_sets.add(smtp_from,gdc);
        gdc.gridwidth = 2;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 1;
        gdc.gridy = 1;
        smtp_sets.add(smtp_from_field,gdc);
        gdc.gridwidth = 0;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 3;
        smtp_sets.add(smtp_label,gdc);
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.gridx = 1;
        gdc.gridy = 3;
        gdc.gridwidth = 2;
        smtp_sets.add(smtp_field,gdc);
        gdc.gridwidth = 1;
        gdc.gridx = 0;
        gdc.gridy = 4;
        smtp_sets.add(smtp_port_label,gdc);
        gdc.gridwidth = 1;
        gdc.gridx = 1;
        gdc.gridy = 4;
        smtp_sets.add(smtp_port_field,gdc);
        gdc.gridwidth = 1;
        gdc.gridx = 2;
        gdc.gridy = 4;
        smtp_sets.add(smtp_auth_bool,gdc);
        gdc.gridx = 0;
        gdc.gridy = 5;
        smtp_sets.add(user_label,gdc);
        gdc.gridwidth = 2;
        gdc.gridx = 1;
        gdc.gridy = 5;
        smtp_sets.add(user_field,gdc);
        gdc.gridwidth = 1;
        gdc.gridx = 0;
        gdc.gridy = 6;
        smtp_sets.add(pass_label,gdc);
        gdc.gridwidth = 2;
        gdc.gridx = 1;
        gdc.gridy = 6;
        smtp_sets.add(pass_field,gdc);
        
        delay_panel.setLayout(gbl);
        gdc.gridwidth = 1;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        delay_panel.add(delay_label,gdc);
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 1;
        gdc.gridy = 0;
        delay_panel.add(delay_slider,gdc);
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.gridx = 2;
        gdc.gridy = 0;
        delay_panel.add(delay_label_out,gdc);
        
        
        bd_path_panel.setLayout(gbl);
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        bd_path_panel.add(path_label,gdc);
        gdc.weightx = 1.0;
        gdc.weighty = 0;        
        gdc.gridx = 1;
        gdc.gridy = 0;
        bd_path_panel.add(path_field,gdc);
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 2;
        gdc.gridy = 0;
        bd_path_panel.add(path_explore,gdc);
        
        GridBagConstraints gdc_here = new GridBagConstraints();
        purge_panel.setLayout(gbl);
        gdc_here.fill = GridBagConstraints.NONE;
        gdc_here.insets = new Insets(5,5,0,5);
        gdc_here.weightx = 0;
        gdc_here.weighty = 0;
        gdc_here.gridx = 0;
        gdc_here.gridy = 0;
        purge_panel.add(purge_warning, gdc_here);
         gdc_here.insets = new Insets(0,5,0,5);
        gdc_here.weightx = 0;
        gdc_here.weighty = 0;
        gdc_here.gridx = 0;
        gdc_here.gridy = 1;
        purge_panel.add(purge_warning_2, gdc_here);
        gdc_here.weightx = 0;
        gdc_here.weighty = 0;
        gdc_here.gridx = 0;
        gdc_here.gridy = 2;                
        gdc_here.insets = new Insets(5,5,5,5);
        purge_panel.add(purge_history, gdc_here);
        purge_history.setForeground(Color.red);        
        
        bottom_panel.setLayout(gbl);
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        bottom_panel.add(bottom_ok,gdc);
        gdc.gridx = 1;
        gdc.gridy = 0;
        bottom_panel.add(bottom_cancel,gdc);
        
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        //borders 
        smtp_sets.setBorder(BorderFactory.createTitledBorder("SMTP sets"));
        delay_panel.setBorder(BorderFactory.createTitledBorder("delay"));
        bd_path_panel.setBorder(BorderFactory.createTitledBorder("bd path"));
        purge_panel.setBorder(BorderFactory.createTitledBorder("Purge"));
        
        //resize and fixings
        this.setMinimumSize(new Dimension(400,470));
        this.setResizable(false);        
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int)(((screenSize.getWidth() / 2.0) - (this.getWidth()/2.0))),(int)(((screenSize.getHeight() / 2.0) - (this.getHeight()/2.0))));
        
        actionListeners();
        
        path_explore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                explorePath(ae);
            }
        });       
    }
    
    /**
     * initialize gui if SIMPLE Hint has been passed
     */
    private void init_gui_simple(){
                GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gdc = new GridBagConstraints();
        this.setLayout(gbl);
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.ipady = 0;
        //gdc.ipadx = 5;
        gdc.insets = new Insets(5,5,5,5);
        gdc.fill = GridBagConstraints.BOTH;
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        this.add(main_panel, gdc);
        
        main_panel.setLayout(gbl);
        gdc.insets = new Insets(5,5,5,5);
        gdc.gridx = 0;
        gdc.gridy = 0;
        main_panel.add(smtp_sets,gdc);
        gdc.gridx = 0;
        gdc.gridy = 1;
        main_panel.add(delay_panel, gdc);
        gdc.gridx = 0;
        gdc.gridy = 2;
        main_panel.add(bottom_panel,gdc);
        
        smtp_sets.setLayout(gbl);
        gdc.insets = new Insets(2,5,2,5);
        gdc.gridwidth = 0;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        smtp_sets.add(smtp_host,gdc);
        gdc.gridwidth = 2;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 1;
        gdc.gridy = 0;
        smtp_sets.add(smtp_host_field,gdc);
        gdc.gridwidth = 0;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 1;
        smtp_sets.add(smtp_from,gdc);
        gdc.gridwidth = 2;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 1;
        gdc.gridy = 1;
        smtp_sets.add(smtp_from_field,gdc);
        gdc.gridwidth = 0;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 3;
        smtp_sets.add(smtp_label,gdc);
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.gridx = 1;
        gdc.gridy = 3;
        gdc.gridwidth = 2;
        smtp_sets.add(smtp_field,gdc);
        gdc.gridwidth = 1;
        gdc.gridx = 0;
        gdc.gridy = 4;
        smtp_sets.add(smtp_port_label,gdc);
        gdc.gridwidth = 1;
        gdc.gridx = 1;
        gdc.gridy = 4;
        smtp_sets.add(smtp_port_field,gdc);
        gdc.gridwidth = 1;
        gdc.gridx = 2;
        gdc.gridy = 4;
        smtp_sets.add(smtp_auth_bool,gdc);
        gdc.gridx = 0;
        gdc.gridy = 5;
        smtp_sets.add(user_label,gdc);
        gdc.gridwidth = 2;
        gdc.gridx = 1;
        gdc.gridy = 5;
        smtp_sets.add(user_field,gdc);
        gdc.gridwidth = 1;
        gdc.gridx = 0;
        gdc.gridy = 6;
        smtp_sets.add(pass_label,gdc);
        gdc.gridwidth = 2;
        gdc.gridx = 1;
        gdc.gridy = 6;
        smtp_sets.add(pass_field,gdc);
        
        delay_panel.setLayout(gbl);
        gdc.gridwidth = 1;
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        delay_panel.add(delay_label,gdc);
        gdc.weightx = 1.0;
        gdc.weighty = 0;
        gdc.gridx = 1;
        gdc.gridy = 0;
        delay_panel.add(delay_slider,gdc);
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 2;
        gdc.gridy = 0;
        delay_panel.add(delay_label_out,gdc);
        
        bottom_panel.setLayout(gbl);
        gdc.weightx = 0;
        gdc.weighty = 0;
        gdc.gridx = 0;
        gdc.gridy = 0;
        bottom_panel.add(bottom_get_default,gdc);
        gdc.gridx = 1;
        gdc.gridy = 0;
        bottom_panel.add(bottom_ok,gdc);
        gdc.gridx = 2;
        gdc.gridy = 0;
        bottom_panel.add(bottom_cancel,gdc);
        
        //borders 
        smtp_sets.setBorder(BorderFactory.createTitledBorder("SMTP sets"));
        delay_panel.setBorder(BorderFactory.createTitledBorder("delay"));
        
        this.setMinimumSize(new Dimension(400,310));
        this.setResizable(false);      
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int)(((screenSize.getWidth() / 2.0) - (this.getWidth()/2.0))),(int)(((screenSize.getHeight() / 2.0) - (this.getHeight()/2.0))));
        
        //Action Listener
        actionListeners();
        
    }
      
    /**
     * initialize events and bindings
     */
    private void actionListeners(){
        bottom_ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        
                        apply_event(ae);
                    }
                });        
        
        bottom_cancel.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        cancel_event(ae);
                    }
                });
        
        smtp_auth_bool.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae){              
                        switchCkeck(ae);
                    }
                }); 
        
        delay_slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                //on modifie le field
                changeSlide(e);
            }
        });
    }
    
    /**
     * fill the fields for the FULL Hint interface
     */
    private void fill_gui_full(){
        if(this.user != null){
            this.smtp_auth_bool.setSelected(this.user.auth);
            this.smtp_field.setText(this.user.smtpHost);
            this.smtp_port_field.setText(this.user.smtpPort);
            this.pass_field.setText(this.user.pass);
            this.user_field.setText(this.user.user);
        }        
    }
    
    /**
     * fill the fields for the SIMPLE Hint interface
     */
    private void fill_gui_simple(){
        if(this.user != null){
            this.smtp_auth_bool.setSelected(this.user.auth);
            this.smtp_field.setText(this.user.smtpHost);
            this.smtp_port_field.setText(this.user.smtpPort);
            this.pass_field.setText(this.user.pass);
            this.user_field.setText(this.user.user);            
        }  
    }
    
    /**
     * Event processor.
     * 
     * update label from slide value.
     * 
     * @param ae processed event
     */
    private void changeSlide(ChangeEvent  ae){
        this.delay_label_out.setText(this.delay_slider.getValue() + "");
    }
    
    /**
     * Event processor.
     * 
     * process the Ok pressed event, generate the SimpleSettings instance in 
     * result field. will be returned to owner frame at the end of showDialog
     * 
     * @param ae processed event
     */
    private void apply_event(ActionEvent ae){        
        //check Try ?
        if(verify_object()){
            //End Chack
            if(type == SIMPLE){
                this.result = new SimpleSettings(
                        this.smtp_field.getText(), 
                        this.smtp_port_field.getText(), 
                        this.user_field.getText(), 
                        this.pass_field.getText(), 
                        this.delay_slider.getValue(), 
                        this.smtp_auth_bool.isSelected(),
                        this.smtp_from_field.getText(),
                        this.smtp_host_field.getText());
            }
            else if(type == FULL){
                this.result = new SimpleSettings(
                        this.smtp_field.getText(), 
                        this.smtp_port_field.getText(), 
                        this.user_field.getText(), 
                        this.pass_field.getText(), 
                        this.delay_slider.getValue(), 
                        this.smtp_auth_bool.isSelected(),
                        this.path_field.getText(),
                        this.smtp_from_field.getText(),
                        this.smtp_host_field.getText());        
            }
            this.setVisible(false);
            dispose();
        }
    }
    
    /**
     * Event processor.
     * 
     * process Cancel Button
     * 
     * @param ae processed event
     */
    private void cancel_event(ActionEvent ae){
        this.setVisible(false);
        this.result = this.user;
        dispose();
    }
    
    /**
     * Event processor.
     * 
     * process Switch CheckBox, hiding fields user and pass
     * if isSelected == false
     * 
     * @param ae processed event
     */
    private void switchCkeck(ActionEvent ae){
        System.out.println("edit : ");
        this.user_field.setEnabled(smtp_auth_bool.isSelected());
        this.pass_field.setEnabled(smtp_auth_bool.isSelected());
    }
    
    /**
     * Event processor.
     * 
     * process explore button pressed. Display file chooser dialog.
     * 
     * @param ae processed event
     */
    private void explorePath(ActionEvent ae){
        int retval =chooser.showOpenDialog(null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File myFile = chooser.getSelectedFile();
            System.out.println(myFile.getAbsolutePath());
            path_field.setText(myFile.getAbsolutePath());
        }
    }
    
    /**
     * Displays the dialog and wait for an event before closing and return 
     * the new SimpleSettings. may return null in case of bad field process
     * 
     * @return SimpleSettings, may return null
     */
    public SimpleSettings ShowDialog(){
        setVisible(true);
        return this.result;        
    }
    
    /**
     * set the fields of the dialog to the todo SimpleSettings values.
     * throws exception if null : ExceptionSender.
     * 
     * @param todo
     * @throws ExceptionSender 
     */
    public void SetOnSimpleSettings(SimpleSettings todo) throws ExceptionSender{
        if (todo == null){
            throw new ExceptionSender("Exception : un probleme lors de l'initialisation des settings");
        }
        //on met a jour tout les champs a partir d'un simple settings passé en parametre
        this.user_field.setText(todo.user);
        this.pass_field.setText(todo.pass);
        this.delay_slider.setValue(todo.delay);
        this.path_field.setText(todo.path);
        this.smtp_auth_bool.setSelected(todo.auth);
        if (!todo.auth)
        {
            //on met a jour l'affichage des champ user
            this.user_field.setText("");
            this.pass_field.setText("");
            this.user_field.setEnabled(false);
            this.pass_field.setEnabled(false);
        }
        this.smtp_field.setText(todo.smtpHost);
        this.smtp_port_field.setText(todo.smtpPort);
        this.smtp_from_field.setText(todo.from);
        this.smtp_host_field.setText(todo.host);          
    }
    
    /**
     * verify and advertise on settings validity
     * return true if valid
     * return false and spawn a message dialog whenever a 
     * field hasn't been correctly filled
     * 
     * @return boolean
     */
    private boolean verify_object(){
        //on verifie que les champs necessaires sont bien remplis
        if((this.smtp_host_field.getText() == null) || ("".equals(this.smtp_host_field.getText()))){
            JOptionPane.showMessageDialog(null, "Host field is empty.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if((this.smtp_from_field.getText() == null) || ("".equals(this.smtp_from_field.getText()))){
            JOptionPane.showMessageDialog(null, "From field is empty.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(!((this.smtp_field.getText() != null) && (!"".equals(this.smtp_field.getText())))){
            //le champ smtp n'est pas inited
            JOptionPane.showMessageDialog(null, "SmtpHost field is empty.", "Warning",
                                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if((this.smtp_port_field.getText() != null) && (!"".equals(this.smtp_field.getText()))){
            //si le champ est inited
            try {
                //on verifie que c'est bien un int
                int a = Integer.parseInt(this.smtp_port_field.getText());
                if((a < 0) || (a > 65535)){
                    JOptionPane.showMessageDialog(null, "Specifies Port does not exist.\nPlease specify a port number between 0 and 65535.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
            catch(NumberFormatException nfe){
                nfe.printStackTrace();
                JOptionPane.showMessageDialog(null, "Port field need an integer.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Port field is empty.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(this.smtp_auth_bool.isSelected()){
            //authentification activée
            if("".equals(this.user_field.getText()) ){
                //on verifie que les champs soit bien instancies
                JOptionPane.showMessageDialog(null, "User field is empty.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true;
    }
}
