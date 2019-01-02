package com.zime;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.zime.util.SerialCommExample;

public class MainFrame extends JFrame {
	private CurveComponent tempCurve = new CurveComponent();
	
	private CurveComponent humdCurve = new CurveComponent();
	
	private int x = 0;
	
	private char cmd = 'S';
	
	public MainFrame() {
		setTitle("主窗口");
		setSize(600,400);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initComponent();
	}
	
	public char getCmd() {
		return cmd;
	}
	
	private void initComponent() {
		JPanel content = new JPanel();
		content.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridLayout layout = new GridLayout(1, 2, 20, 10);
		content.setLayout(layout);
		content.add(tempCurve);
		content.add(humdCurve);
		
		JButton btnStart = new JButton("开始");
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if( cmd == 'S' ) {
					cmd = 'C';
					btnStart.setText("停止");
				}
				else {
					cmd = 'S';
					btnStart.setText("开始");
				}
			}
		});
		this.add(btnStart, BorderLayout.NORTH);
		this.add(content, BorderLayout.CENTER);
		setLocationRelativeTo(null);
	}
	
	public void open() {
		SerialCommExample comm = new SerialCommExample();
		comm.setMainFrame(this);
	}
	
	public void addTempPoint(int y) {
		tempCurve.addPoint(x, y);
		x += 10;
	}
	
	public void addHumdPoint(int y) {
		humdCurve.addPoint(x, y);
	}
}
