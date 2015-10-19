package MailSender.GUI;

import Database.CoreClasses.*;
import Fusion.Fusion;
import MailSender.CoreClasses.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.json.JSONException;

/**
 * This class is the main Class of the MailSender part. This class codes event
 * between majority of sub components. mainFrame handle database handler,
 * sending, dialogs, pizzas and potatoes.
 */
public class UserInterfaceMainFrame extends JFrame {

    /**
     * Contains TopLevelCompenents Fusion, and this intance itself.
     */
    UserInterfaceMainFrame mainFrame;
    Fusion papa;
    /**
     * Contains Database Handlers.
     */
    DatabaseHandler dbSystem;
    DatabaseEmail dbEmail;
    /**
     * Contains memoryEnvironnement.
     */
    SimpleSettings defaultSettings;
    ArrayList<Template> templateList;
    ArrayList<String> keyWords;
    /**
     * Contains GUI
     */
    JTabbedPane panel_tab;
    JProgressBar progressBar;
    JLabel statusLabel;
    /**
     * Contains GUI--MenuBar
     */
    JMenuBar menu;
    JMenu menu_file;
    JMenuItem menu_utils_generateBase;
    JMenuItem menu_file_about;
    JMenu menu_edit;
    JMenuItem menu_edit_settings;
    JMenu menu_utils;
    JMenuItem menu_utils_spamit;
    JMenuItem menu_utils_mailPop;
    JMenuItem menu_utils_saveTemplate;
    JMenuItem menu_utils_preview;
    JButton progress_status_pause;
    JPanel stateBar;
    /**
     * Contaisn dialog and tabs components.
     */
    UserInterfaceSendStatePanel upload_tab;
    UserInterfaceMainPanel main_tab;
    UserInterfaceTemplateEditor template_tab;
    UserInterfaceSpamHard spamSys;
    UserInterfaceSettingsDialog dial_settings;
    /**
     * Refreshing and accessible thread data bridge content. Home made Observer.
     */
    UserInterfaceRefresherMainFrame refresher;
    SmtpStatus status;
    PopStatus popStatus;
    long frame;
    long wait;

    /**
     * Constructor.
     *
     * Generate an Instance of UserInterfaceMainFrame. this directly open the
     * database found at execDir/data/sendData and the mail db last passed in
     * settings.
     */
    public UserInterfaceMainFrame() {

        //pop status init.
        this.popStatus = new PopStatus();

        //fusion field.
        this.papa = null;

        //suspicious but usefull when you configure an Action performed 
        //in an actionListener, where 'this' doesn't refere anymore to the mainframe, 
        //but to the actionListener itself.

        //an another way to avoid this is to code the event out of the actionListener, 
        //in a function with an actionEvent in parameter.
        //and refere to function in the action listener.
        mainFrame = this;

        //general comportement initialization.
        this.setMinimumSize(new Dimension(900, 580));
        this.setTitle("MainFrame");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitMainframe(e);
            }
        });
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }


        //menu bar -------------------------------------------------------------
        menu = new JMenuBar();
        menu_file = new JMenu("File");

        menu_file_about = new JMenuItem("Wiki");
        menu_edit = new JMenu("Edit");
        menu_edit_settings = new JMenuItem("Settings");
        menu_utils = new JMenu("Utils");
        menu_utils_spamit = new JMenuItem("SpamPowa");
        menu_utils_mailPop = new JMenuItem("Popotor");
        menu_utils_generateBase = new JMenuItem("GenerateBase");
        menu_utils_saveTemplate = new JMenuItem("Save Template");
        menu_utils_preview = new JMenuItem("Preview");

        menu.add(menu_file);
        menu_file.add(menu_file_about);
        menu.add(menu_edit);
        menu_edit.add(menu_edit_settings);
        menu.add(menu_utils);
        menu_utils.add(menu_utils_spamit);
        menu_utils.add(menu_utils_mailPop);
        menu_utils.add(menu_utils_generateBase);
        menu_utils.add(menu_utils_saveTemplate);
        menu_utils.add(menu_utils_preview);
        menu_utils_saveTemplate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (template_tab.validTemplate != 0) {
                    JOptionPane.showMessageDialog(null, "Le Template comporte un ou plusieurs terme(s) interdit(s).\nVeuillez corriger l'erreur avant de sauvegarder.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!template_tab.template_name_field.getText().isEmpty()) { //si nom pas vide
                    int conf = template_tab.confirmTemplateOverwrite();
                    if (conf == 1) {
                        try {
                            dbSystem.deleteTemplate(template_tab.oldName);
                        } catch (ExceptionSender ex) {
                            ex.printStackTrace();
                        }
                        template_tab.saveCurrentTemplate();
                    } else if (conf == 0) {
                        template_tab.saveCurrentTemplate();
                    } else {
                    }
                }
            }
        });
        
        menu_file_about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openWiki(e);
            }
        });

        menu_utils_preview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                template_tab.actionPreview();
            }
        });
        menu_utils_saveTemplate.setAccelerator(javax.swing.KeyStroke
                .getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event
                .InputEvent.CTRL_MASK));
        menu_utils_preview.setAccelerator(javax.swing.KeyStroke
                .getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event
                .InputEvent.CTRL_MASK));
        menu_utils_saveTemplate.setVisible(false);
        menu_utils_preview.setVisible(false);


        //stateBar -------------------------------------------------------------
        stateBar = new JPanel();
        statusLabel = new JLabel("Status Bar");
        progressBar = new JProgressBar();
        progress_status_pause = new JButton("pause");

        GridBagLayout status_layout = new GridBagLayout();
        GridBagConstraints slc = new GridBagConstraints();
        stateBar.setLayout(status_layout);
        slc.fill = GridBagConstraints.BOTH;
        slc.anchor = GridBagConstraints.WEST;
        slc.insets = new Insets(5, 5, 5, 10);
        slc.weightx = 0;
        slc.gridwidth = 1;
        slc.gridx = 0;
        slc.gridy = 0;
        stateBar.add(statusLabel, slc);
        slc.fill = GridBagConstraints.BOTH;
        slc.insets = new Insets(5, 0, 5, 5);
        slc.weightx = 1.0;
        slc.weighty = 1.0;
        slc.gridwidth = 1;
        slc.gridx = 1;
        slc.gridy = 0;
        stateBar.add(progressBar, slc);
        stateBar.setPreferredSize(new Dimension(this.getWidth(), 30));

        //tabbed ---------------------------------------------------------------       
        panel_tab = new JTabbedPane();
        upload_tab = new UserInterfaceSendStatePanel();
        main_tab = new UserInterfaceMainPanel();
        template_tab = new UserInterfaceTemplateEditor();

        panel_tab.addTab("Send Creator", main_tab);
        panel_tab.addTab("Upload state", upload_tab);
        panel_tab.addTab("Template editor", template_tab);

        // INCLUSIONS SUPERIEURES         
        this.getContentPane().add(BorderLayout.NORTH, menu);
        this.getContentPane().add(BorderLayout.CENTER, panel_tab);
        this.getContentPane().add(BorderLayout.SOUTH, stateBar);

        //initialisaton environnement
        initEnv();

        //Bindings and events
        //tab changed -> if tab = status umpload, status progress bar disapear.
        initBindings();

        //starting application
        this.setVisible(true);
    }

    /**
     * Constructor.
     *
     * same thing that UserInterfaceMainFrame() but used by fusion system.
     *
     * @param parent fusion instance owner of this UserInterfaceMainFrame.
     */
    public UserInterfaceMainFrame(Fusion parent) {
        this();
        this.papa = parent;

    }

    /**
     * initialize bindings between sub components.
     */
    private void initBindings() {        
        panel_tab.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                if(panel_tab.getSelectedIndex() == 2){
                    menu_utils_saveTemplate.setVisible(true);
                    menu_utils_preview.setVisible(true);
                } else {
                    menu_utils_saveTemplate.setVisible(false);
                    menu_utils_preview.setVisible(false);
                }
                if (panel_tab.getSelectedIndex() == 1) {
                    //tab without progressbar
                    progressBar.setVisible(false);
                    statusLabel.setVisible(false);
                } else {
                    refreshTemplateList();
                    progressBar.setVisible(true);
                    statusLabel.setVisible(true);
                }
            }
        });

        menu_utils_spamit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spamSys = new UserInterfaceSpamHard(mainFrame);
                Smtp test = spamSys.ShowDialog(defaultSettings);
                if (test != null) {
                    addToQueueEffective(test);
                }
            }
        });

        menu_utils_mailPop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserInterfacePopDialog dialogPop = new UserInterfacePopDialog(null);
                dialogPop.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        try {
                            makeEmailList();
                        } catch (ExceptionSender ex) {
                            JOptionPane.showMessageDialog(null, "Database has not been initialized, please set another database in the settings.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    public void windowClosing(WindowEvent e) {
                    }
                });
                dialogPop.setPopStatus(popStatus);
                dialogPop.setDatabaseHandler(dbSystem);
                dialogPop.setDb(dbEmail);
                dialogPop.setVisible(true);
            }
        });

        menu_edit_settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event_settings_menu_bar(ae);
            }
        });

        main_tab.options_zone_main_envoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventAddToQueue(e);
            }
        });

        main_tab.dial_settings.bottom_get_default.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //getDefault get the default settings.
                eventGetDefaultSettings(e);
            }
        });

        upload_tab.play_one.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventPlay(e);
            }
        });

        upload_tab.pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventPause(e);
            }
        });

        upload_tab.stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame.upload_tab.envoi_queue.getSize() > 1) {
                    int option = JOptionPane.showConfirmDialog(null, "Will you chain the remaining Elements ?");
                    if (option == JOptionPane.YES_OPTION) {
                        upload_tab.play_all.setSelected(true);
                        refresher.refresherCanStart = true;
                    } else if (option == JOptionPane.NO_OPTION) {
                        upload_tab.play_all.setSelected(false);
                    }
                } else if(mainFrame.upload_tab.envoi_queue.getSize() == 1){
                    int option2 = JOptionPane.showConfirmDialog(null, "Will you delete the remaining Element ?");
                    if (option2 == JOptionPane.YES_OPTION) {
                        refresher.actualWorker.getStatus().setStop(true);
                        while (refresher.working) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(UserInterfaceMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        refresher.clean();
                        refresher.initInterface();
                        upload_tab.clearInfoZone();
                        return;
                    } else if (option2 == JOptionPane.NO_OPTION) {
                        return;
                    }

                }
                if (!mainFrame.upload_tab.envoi_queue.isEmpty()) {
                    eventStop(e);
                }
            }
        });

        upload_tab.play_all.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                eventChain(e);
            }
        });

        dial_settings.purge_history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!dbSystem.getMailFilter().resetFilter()) {
                    JOptionPane.showMessageDialog(null, "An error occurred while deleting history", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Purge E-Mail history has been done successfully.", "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        menu_utils_generateBase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventGenerateBase(e);
            }
        });

        refresher = new UserInterfaceRefresherMainFrame(this) {
            @Override
            public void doWorking(boolean boolTest) {
                wait = 0;
                frame++;
                frame = frame % 50;
                //progressbar update
                this.mainFrame.progressBar.setValue(this.status.getProgress());
                this.mainFrame.upload_tab.progressbar.setValue(this.status.getProgress());
                //JLabel update
                this.mainFrame.upload_tab.nb_envoi_total.setText("Mails total : " + this.status.getNbMailTotal());
                this.mainFrame.upload_tab.nb_envoi_actuel.setText("Mails envoyés : " + Math.max(0, (this.status.getNbMailAct()) + 1));
                this.mainFrame.upload_tab.nb_envoi_restant.setText("Mails restants : " + (this.status.getNbMailTotal() - (this.status.getNbMailAct() + 1)));
                if (frame == 0 || boolTest) {
                    this.mainFrame.upload_tab.temps_ecoule.setText("Temps ecoule : " + MailTime.timePrint(this.status.getTime()));
                    float temp = MailTime.getSpeedMails(Math.max(0, this.status.getCurrentTime()));
                    String maxv = temp + "";
                    if (temp >= 10) {
                        maxv = (int) temp + "";
                    } else if (maxv.length() > 4) {
                        maxv = maxv.substring(0, 4);
                    }
                    this.mainFrame.upload_tab.vitesse_actuel.setText("Vitesse : " + maxv + "  mail/s"); //speed
                    this.mainFrame.upload_tab.temps_restant.setText("Temps restant : " + MailTime.timePrint(Math.max(0, MailTime.restTime(Math.max(0, this.status.getNbMailTotal() - (this.status.getNbMailAct()) + 1),
                            MailTime.getSpeedMails(Math.max(0, (long) (this.status.getTime() / (this.status.getNbMailAct() * 1.0))))))));
                }

                if (frame % 10 == 0) {
                    this.mainFrame.upload_tab.graph.addPoint(MailTime.getSpeedMails(Math.max(0, this.status.getCurrentTime())));
                }

            }

            @Override
            public boolean doUnloaded() {
                wait++;
                if (wait < 300 && wait % 10 == 0) {
                    this.mainFrame.upload_tab.graph.addPoint(0);
                }
                
                //verify head list
                //if not void, status is set in working zone.
                if (this.mainFrame.upload_tab.list_envoi_attente.getModel().getSize() > 0) {
                    setWorker(this.mainFrame.upload_tab.list_envoi_attente.getModel().getElementAt(0));
                    this.status = this.actualWorker.getStatus();
                    return true;
                } else if (wait > 1) {
                    upload_tab.play_all.setSelected(false);
                }
                return false;
            }

            @Override
            public void clean() {
                this.status = null;
                this.mainFrame.upload_tab.envoi_queue.removeElement(actualWorker);
                this.mainFrame.upload_tab.clearInfoZone();
            }

            @Override
            public void initInterface() {
                this.mainFrame.upload_tab.trace.setText("");
                //inactive state.
                this.mainFrame.progressBar.setValue(0);
                this.mainFrame.upload_tab.progressbar.setValue(0);
                //JLabel state.
                this.mainFrame.upload_tab.nb_envoi_total.setText("mails : - ");
                this.mainFrame.upload_tab.nb_envoi_actuel.setText("mails envoyés : - ");
                this.mainFrame.upload_tab.nb_envoi_restant.setText("mails restants : - ");
                this.mainFrame.upload_tab.temps_ecoule.setText("Temps ecoule : - ");
                this.mainFrame.upload_tab.vitesse_actuel.setText("Vitesse : -"); //speed
                this.mainFrame.upload_tab.temps_restant.setText("Temps restant : - "); //resting time
            }

            @Override
            public void doEnd() {
                this.mainFrame.progressBar.setValue(this.status.getNbMailTotal());
                this.mainFrame.upload_tab.progressbar.setValue(this.status.getNbMailTotal());
            }
        };

        refresher.start();
    }

    /**
     * initialize environnement, component locations and backGround tasks
     * handlers.
     */
    private void initEnv() {
        try {
            //init gui   
            dbSystem = new DatabaseHandler();
            defaultSettings = dbSystem.loadUserSettings();
        } catch (ExceptionDataBase | ExceptionSender | JSONException ex) {
            //severe error.
            JOptionPane.showMessageDialog(null, "The program has encountered an error with the database \n Attempt to reconstruct the database.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            try {
                DatabaseHandler.cleanBD();
                dbSystem = new DatabaseHandler();
                JOptionPane.showMessageDialog(null, "Reconstruction was accomplished.", "Youpi",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (ExceptionSender | ExceptionDataBase es) {
                JOptionPane.showMessageDialog(null, "The Reconsruction has failed. \n The program will now close.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                exitMainframe();
            }
        }
        try {
            makeEmailList();
        } catch (ExceptionSender ex) {
            JOptionPane.showMessageDialog(null, "The database of email is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }


        dial_settings = new UserInterfaceSettingsDialog(null, UserInterfaceSettingsDialog.FULL, null);
        try {
            dial_settings.SetOnSimpleSettings(defaultSettings);
        } catch (ExceptionSender ex) {
        }
        template_tab.setDatabaseHandler(dbSystem);
        main_tab.setDatabaseHandler(dbSystem);
        main_tab.setDatabaseEmail(dbEmail);
        main_tab.updateRenderHtml(""
                + "<html>"
                
                + "<head>"
                + "<title>help</title>"
                + "<style type=\"text/css\">"
                + "body {"
                + "background-color:white;"
                + "}"
                + "p {"
                + "color:black;"
                + "}"
                + "</style>"
                + "</head>"
                
                + "<body>"
                + "<h2>Aide au demarrage</h2>"
                + "Ce programme permet l'envoi chainé et reparti de listes de mails.<br />"
                + "Pour prendre connaissance de son fonctionnement, veuillez vous referer "
                + "au guide ci dessous."
                + "<br/>"
                + "<br/>"
                + "La realisation d'un envoi passe par plusieurs etapes necessaires de calibrage et de creation d'Instance d'envois."
                + "<h3>Etape 1 : Tour d'horizon</h3>"
                + "Le programme s'organise autour de trois onglets et quelques boites de dialog que vous trouverez a diverses endroits.<ul>"
                + "<li><a style=\"color : blue;\">Send Creator</a> : Cet onglet genere des instances d'envoi d'email "
                + "multiples a partir des donnees receuillies en son sein.</li><br/>"
                + "<li><a style=\"color : blue;\">Upload State</a> : Cet onglet propose un systeme de gestion des envoi "
                + "chain&eacute; cr&eacute;&eacute;s depuis l'onglet Send Creator, De visualisation de la progression, et des donnees d'envoi.</li><br/>"
                + "<li><a style=\"color : blue;\">Template Editor</a> : Cette partie propose de réaliser ses propres templates et de les sauvegarder,éditer,supprimer de plus un"
                + "bouton aperçus est disponible pour voir le résultat obtenus</li><br />"
                + "</ul>"            
                + "<h3>Etape 2 : Configuration de l'environnement de base</h3>"
                + "La configuration du systeme general se fait par la barre de menu."
                + "cliquez sur Edit > Settings"
                + "<br/>"
                + "<br>Pour ce qui est de la partie connection au server Smtp par defaut :"
                + "<ul><li><a style=\"color : blue;\">Host</a> : \"localhost\" Ce champs ne doit etre changé.</li><br/>"
                + "<li><a style=\"color : blue;\">From</a> : mettez une adresse mail valide de preference, elle servira au "
                + "possibles retours du server smtp en cas d'erreur d'adressage, et servira aussi de "
                + "base des demandes de retraint de la liste d'inscription.</li><br/>"
                + "<li><a style=\"color : blue;\">smtpHost</a> : il s'agit de l'adresse du smtp utilisé pour le transfert\n"
                + "cette adresse est souvent de la forme smtp.DomainName.TopLevelDomainName\n"
                + "<br/>comme par exemple : "
                + "<br/> - smtp.unice.fr"
                + "<br/> - smtp.orange.fr"
                + "<br/> - smtp.gmail.com"
                + "</li><br/>"
                + "<li><a style=\"color : blue;\">smtpPort</a> : le port Smtp, il s'agit du port par defaut 25 surout utilisé dans le cas "
                + "d'un smtp sans authentification. Dans le cas d'un smtp avec authentication veuillez vous reporter"
                + " a la page du fournisseur.</li><br/>"
                + "<li><a style=\"color : blue;\">authentification</a> : cochez si le smtp requiert une authentification, "
                + "le type d'authentification (SSL/TLS) sera detectee automatiquement lors de l'envoi.</li><br/>"
                + "<li><a style=\"color : blue;\">User</a> : remplissez le nom d'utilisateur de votre compte "
                + "associé au fournisseur smtp spécifié plus haut</li><br/>"
                + "<li><a style=\"color : blue;\">Pass</a> : faites de meme avec votre mot de passe.</li><br/>"
                + "<li><a style=\"color : blue;\">Delay</a> : Le delay est le temps d'attente "
                + "entre deux envoi deux mails consecutifs, il s'exprime en millisecondes. "
                + "Il est necessaire de connaitre les limitations de votre smtp pour eviter que celui ne vous banisse pour "
                + "flood et de modifier cette valeur en consequences.</li>"
                + "</ul>"
                + "Pour ce qui est de la mise en place de la base de donnee de mails :"
                + "<ul>"
                + "<li><a style=\"color:blue;\">Path database</a> : Cliquez sur \"explore\" pour chercher "
                + "votre dossier contenant la base de donne de mails"
                + "ou entrez manuellement l'adresse dans le champ textuel prevu a cet effet. "
                + "Une Base donne de mails valide est necessaire au bon fonctionnement de ce programme."
                + "<br /><br /><a style=\"color : #FF5000;\"><b>AVERTISSEMENT</b></a> : Si une fenetre d'avertissement "
                + "apparais au demarrage en vous specifiant que la base de donnee de mails n'est pas valide, veillez a y remedier en en specifiant une autre " 
                + "par le biais de cet option.</li><br/>"
                + "<li><a style=\"color:blue;\">Purge</a> : Ce bouton purge l'historique anti flood de la base de donnee."
                + " cet historique est implemente automatiquement pour "
                + "prevenir l'envoi massif de mails sur une seule adresse mail. "
                + "<br/><br/><a style=\"color : #FF5000;\"><b>AVERTISSEMENT</b></a> : Cette action est irreversible.</li></ul>"
                + "Ceci termine l'initialisation de l'environnement par defaut. Cet Environnement est persistant, vous le retrouverez au prochain demarrage."
                + "<br/>Pour le modifier, retournez dans le panneau des options : Edit -> Settings. "
                + "modifiez et cliquez sur apply, vos modification seront sauvegardées.<br/><br/>"
                + "<b>Pour plus d'information sur le fonctionnement du programme, veuillez vous referer au Wiki prevu a cet effet : File -> Wiki.</b>"
                + "</body>"
                
                + "</html>");

    }

    /**
     * refresher the comboBox template of subComponents.
     */
    private void refreshTemplateList() {
        this.main_tab.refreshTemplateList();
        this.template_tab.refreshTemplateList();
    }
    
    public void openWiki(ActionEvent event){
        try {
            String url = "https://reunion.unice.fr/redmine/projects/2013-s6-e02/wiki/Guide";
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        }
        catch(java.io.IOException e){
            System.out.println(e);
        }
    }

    /**
     * Event. generate a template for a getStarted example.
     *
     * @param ae
     */
    private void eventGenerateBase(ActionEvent ae) {
        try {
            String content = "Felicitation\n$$template_EOO\n<html>\n\t<head>\n\t\t<title>Euromillion</title>\n\t</head>"
                    + "\t\n\t<body style=\"color:black; background-color:rgb(183,183,183); background-image:url"
                    + "(https://dl.dropboxusercontent.com/u/49836439/Sans%20titre4.jpg);background-position:right bottom;"
                    + "background-repeat:no-repeat;\">\n\t\t<div align=\"CENTER\">\n\t\t<br /><br />\n\n\t\t"
                    + "<img src=\"http://www.123-stickers.com/4258-4415-large/stickers-smiley-273.jpg\" alt=\"Smiley face\" "
                    + "height=\"300\" width=\"300\">\n\t\t<br /><br />\n\n\t\t<h2>Bonjour $$var_name $$var_surname,</h2>\n\t\t"
                    + "<br /><br />\n\n\t\tVotre adresse mail [$$var_mail] a été selectionnée en vue de notre grand Jeu Concours "
                    + "EUROMILLIONS.<br />\n\t\tRendez vous sur http://9gag.com/ pour y participer.\n\t\t<br /><br />\n\n\t\t"
                    + "Cordialement, LeGrandWalama.\n\t\t<br /><br />\n\t\t\n\t\tCliquez sur ce <a style=\"color : black;\"  "
                    + "href=\"http://www-mips.unice.fr/~bt005280/ProjetLicence/relay_mail.php?mail=$$var_mail&noReply=$$var_servermail\">"
                    + "lien</a> pour ne plus recevoir ce courrier.\n\t\t</div>\n\t</body>\n</html>\n$$template_EOF\n$$var_name Cher\n$$var_surname Client\n";
            Template template = new Template("Exemple Template to begin correctly", content);
            this.dbSystem.saveTemplate(template);
            this.refreshTemplateList();
        } catch (ExceptionSender ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }

    }

    /**
     * Event. play the working pause sending or start the top of the queue-stack
     *
     * @param ae
     */
    private void eventPlay(ActionEvent ae) {
        //play pressed 
        if (this.refresher.actualWorker == null) {
            return;
        }
        if (this.refresher.working) {
            this.refresher.actualWorker.getStatus().setRun(true);

        } else {
            upload_tab.play_all.setSelected(true);
            this.refresher.refresherCanStart = true;
            this.refresher.actualWorker.getStatus().setRun(true);
        }
    }

    /**
     * Event. pause the working send.
     *
     * @param ae
     */
    private void eventPause(ActionEvent ae) {
        //pause pressed
        if (this.refresher.working) {
            this.refresher.actualWorker.getStatus().setRun(false);
        }
    }

    /**
     * Event. stop all the working send.
     *
     * @param ae
     */
    private void eventStop(ActionEvent ae) {
        //stop pressed
        if (this.refresher.working) {
            this.refresher.actualWorker.getStatus().setStop(true);
            this.refresher.doUnloaded();
            while (refresher.working) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UserInterfaceMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.refresher.initInterface();
            this.upload_tab.clearInfoZone();
        }
    }

    /**
     * Event. switch the chaining state of the sending strategy
     *
     * @param ae
     */
    private void eventChain(ChangeEvent ae) {
        //check box switched
        this.refresher.chain = this.upload_tab.play_all.isSelected();
    }

    /**
     * Event. set send settings as the default settings
     *
     * @param ae
     */
    private void eventGetDefaultSettings(ActionEvent ae) {
        //set settings from default settings
        if (defaultSettings == null) {
        } else {
            try {
                main_tab.dial_settings.SetOnSimpleSettings(defaultSettings);
            } catch (ExceptionSender ex) {
                Logger.getLogger(UserInterfaceMainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Event. add to send stack from mainPanel. call addToQueueEffective on the
     * smtp.
     *
     * @param ae
     */
    private void eventAddToQueue(ActionEvent ae) {
        if (!(main_tab.keyEnd != null && main_tab.keyEnd.length > 0)) {
            return;
        }
        try {
            Smtp test = main_tab.getSmtpObject();
            addToQueueEffective(test);
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Event. displays the settings Dialog.
     *
     * @param ae
     */
    private void event_settings_menu_bar(ActionEvent ae) {
        SimpleSettings temp = dial_settings.ShowDialog();
        if (temp != null) {
            defaultSettings = temp;
            dbSystem.saveUserSettings(defaultSettings);
            try {
                makeEmailList();
            } catch (Exception me) {
                System.out.println("error : " + me);
            }
        }
    }

    /**
     * Close thread refresher in background. and close mainFrame.
     *
     * @param ae
     */
    private void exitMainframe() {
        this.refresher.quit();
        if (this.papa != null) {
            this.papa.setVisible(true);
        }
        this.dispose();
    }

    /**
     * equivalent to exitMainFrame(), used for fusion.
     *
     * @param ae
     */
    private void exitMainframe(WindowEvent e) {

        this.refresher.quit();
        if (this.papa != null) {
            this.papa.setVisible(true);
        }
        this.dispose();

    }

    /**
     * append the given smtp to send stack
     *
     * @param ae
     */
    private void addToQueueEffective(Smtp test) {
        if (test != null) {
            try {
                test.setStatus(new SmtpStatus(0, test.getNbMailTotal(), 0, false, false));
            } catch (ExceptionSender ex) {
                return;
            }
            test.getStatus().setFrameOutput(this.upload_tab.trace);
            try {
                test.setFilteredMailList(dbSystem);
            } catch (ExceptionDataBase ex) {
                JOptionPane.showMessageDialog(null, ex.toString(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (test.getNbMailTotal() == 0) {
                JOptionPane.showMessageDialog(null, "All Email List in this email is already used \n "
                        + "use the settings panel to clean the history for reuse them", "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            upload_tab.envoi_queue.addElement(test);
            System.out.println("instance envoie cree");
            if (this.upload_tab.list_envoi_attente.getModel().getSize() == 1) {
                int option = JOptionPane.showConfirmDialog(null, "Your shipment was created. Would you start sending immediately ?");
                if (option == JOptionPane.YES_OPTION) {
                    this.refresher.refresherCanStart = true;
                    this.refresher.actualWorker.getStatus().setRun(true);
                    this.upload_tab.play_all.setSelected(true);
                } else {
                    this.refresher.refresherCanStart = false;
                }
            }
        } else {
            System.out.println("echec creation envoie");
        }
    }

    /**
     * generate email list
     *
     * @param ae
     */
    private void makeEmailList() throws ExceptionSender {
        if (defaultSettings != null) {
            //emails bd initialized
            dbEmail = new DatabaseEmail(defaultSettings.path);
            if (dbEmail == null) {
                throw new ExceptionSender("makeEmailList : null dbEmail");
            }
            keyWords = dbEmail.getKeywordsList();
            this.main_tab.listKeys.clear();
            for (int i = 0; i < keyWords.size(); i++) {
                this.main_tab.listKeys.addElement(keyWords.get(i));
            }
            main_tab.setDatabaseEmail(dbEmail);
            this.main_tab.listKeyApp.clear();
        }
    }

    /**
     * Main Function.
     *
     * @param args
     */
    public static void main(String[] args) {
        UserInterfaceMainFrame user = new UserInterfaceMainFrame();
    }
}