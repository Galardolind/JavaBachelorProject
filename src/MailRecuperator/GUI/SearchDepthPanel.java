package MailRecuperator.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * The class {@code SearchDepthPanel} is an extention of the Jpanel and deals 
 * with the selection of the depth of the search.
 */
public class SearchDepthPanel extends JPanel {
    
    /**
     * Simple info text.
     */
    JLabel infoSliderLabel1;
    JLabel infoSliderLabel2;
    /**
     * Slider that will set the depth.
     */
    JSlider depthSlider;
    
    /**
     * Class constructor.
     */
    public SearchDepthPanel() {
        
        /**
         * Initializes the text.
         */
        this.infoSliderLabel1 = new JLabel("Depth of the Search in Result/Keyword");
        this.infoSliderLabel2 = new JLabel("(0 means the search will go on until you stop it manually or there are no more url to search)");
        
        /**
         * Initializes the slider.
         */
        this.depthSlider = new JSlider(0, 400);
        this.depthSlider.setMajorTickSpacing(40);
        this.depthSlider.setPaintTicks(true);
        this.depthSlider.setPaintLabels(true);
        this.depthSlider.setSnapToTicks(true);
        
        /**
         * Placing all the components in their place.
         */
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(5,5,0,5);
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        this.add(this.infoSliderLabel1, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0,5,5,5);
        this.add(this.infoSliderLabel2, gbc);
        gbc.gridy = 2;
        gbc.weighty = 1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(this.depthSlider, gbc);
        
    }
    
}
