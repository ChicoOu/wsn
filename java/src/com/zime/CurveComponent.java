package com.zime;



import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Chico Ou on 2015/1/16.
 */

public class CurveComponent extends JPanel {
    private ArrayList<Point> pts = new ArrayList<Point>();
    
    private static final int MAX_SIZE = 2000;
    
    public CurveComponent(){

    }

    public void addPoint(int x, int y){
        Point pt = new Point(x, y);
        synchronized (this) {
            if( pts.size() >= MAX_SIZE ){
                pts.remove(0);
            }

            pts.add(pt);
        }
        this.repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(Color.lightGray);
        final int STEP_SIZE = 15;
        int xMax = this.getWidth();
        int yMax = this.getHeight();
        g2d.fillRect(0, 0, xMax, yMax);
        int tempX = 0;
        while (true){
            if( tempX >= xMax ){
                break;
            }

            g2d.setColor(Color.green);
            g2d.drawLine(tempX, 0, tempX, yMax);
            tempX += STEP_SIZE;
        }

        int tempY = 0;
        while (true){
            if( tempY >= yMax ){
                break;
            }

            g2d.setColor(Color.green);
            g2d.drawLine(0, tempY, xMax, tempY);
            tempY += STEP_SIZE;
        }
//******************数据走势*********************
        g2d.setColor(Color.red);
        if( pts.size() > 1 ){
            int offset = 0;
            synchronized (this) {
                for (int i = pts.size() - 1; i > 1; i--) {
                    
                	Point pt = pts.get(i);
                    if (pt.x < offset) {
                        break;
                    }

                    if (i == pts.size() - 1 && pt.x > xMax) {
                        offset = pt.x - xMax;
                    }

                    Point pt2 = pts.get(i - 1);
                    g2d.drawLine(pt.x - offset, yMax - pt.y, pt2.x - offset, yMax - pt2.y);
                }
            }
        }
    }

	public static void append(String string) {
		// TODO Auto-generated method stub
		
	}
}
