package com.zime;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chico Ou on 2015/1/19.
 */
public class DigitalComponent extends JPanel {
    private float digitalValue;

    private String[] imgFiles = new String[]{
            "0.jpg", "1.jpg", "2.jpg", "3.jpg", "4.jpg",
            "5.jpg", "6.jpg", "7.jpg", "8.jpg", "9.jpg",
            "dot.jpg"
    };

    private static final int DOT_INDEX = 10;

    private static final String FOLDER_PREV = "./res/";

    private List<JLabel> labels = new ArrayList<JLabel>();

    public DigitalComponent(){
        super();

        this.setPreferredSize(new Dimension(600, 100));
        for (int i = 0; i < 6; i++) {
            JLabel lblInfo = new JLabel();
            lblInfo.setIcon(new ImageIcon(FOLDER_PREV + imgFiles[0]));
            this.add(lblInfo);
            labels.add(lblInfo);
        }

        JLabel lblInfo = new JLabel();
        lblInfo.setIcon(new ImageIcon(FOLDER_PREV + imgFiles[DOT_INDEX]));
        this.add(lblInfo);
        labels.add(lblInfo);

        for (int i = 0; i < 2; i++) {
            lblInfo = new JLabel();
            lblInfo.setIcon(new ImageIcon(FOLDER_PREV + imgFiles[0]));
            this.add(lblInfo);
            labels.add(lblInfo);
        }
    }

    public float getDigitalValue() {
        return digitalValue;
    }

    public void setDigitalValue(float digitalValue) {
        this.digitalValue = digitalValue;

        String strDigital = String.valueOf(Math.abs(digitalValue));
        System.out.println("Digtal number:" + strDigital);
        if( strDigital.length() > 0 ){
            int index = labels.size() - 1;
            for (int i = strDigital.length() - 1; i >= 0; i-- ){
                char c = strDigital.charAt(i);
                if( c == '.' ){
                    JLabel lblDot = labels.get(index);
                    lblDot.setIcon(new ImageIcon(FOLDER_PREV + imgFiles[DOT_INDEX]));
                }
                else{
                    int temp = (int)(c - '0');
                    JLabel lblNum = labels.get(index);
                    lblNum.setIcon(new ImageIcon(FOLDER_PREV + imgFiles[temp]));
                }

                index -= 1;
            }
            
            for( int i = strDigital.length(); i< labels.size(); i++ ){
            	JLabel lblNum = labels.get(index);
                lblNum.setIcon(new ImageIcon(FOLDER_PREV + imgFiles[0]));
                index -= 1;
            }
        }
        else{
        	for( int i = 0; i < labels.size(); i++ ){
        		JLabel lblNum = labels.get(i);
                lblNum.setIcon(new ImageIcon(FOLDER_PREV + imgFiles[0]));
        	}
        }
        //this.invalidate();
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
