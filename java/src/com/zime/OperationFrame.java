package com.zime;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.zime.util.SerialCommExample;

public class OperationFrame extends JFrame {
	public OperationFrame() {
		initUI();
	}
	
	private void initUI() {
		setTitle("操作面板");
		setSize(600, 300);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 30, 50, 50));
		panel.setLayout(new GridLayout(1, 4, 20, 20));
		JButton btnShowCurve = new JButton("实时监控");
		btnShowCurve.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final MainFrame frame = new MainFrame();
				SerialCommExample serialObj = new SerialCommExample();
				serialObj.setMainFrame(frame);
				try {
					serialObj.connect("COM3");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				frame.setVisible(true);
				
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						for( int i=0; i< 100; i++) {
//							frame.addTempPoint((int)(Math.random()*100) + 10);
//							frame.addHumdPoint((int)(Math.random()*100) + 50);
//							
//							try {
//								Thread.sleep(100);
//							}
//							catch(Exception ex) {
//								
//							}
//						}
//					}
//				}).start();
			}
		});
		
		panel.add(btnShowCurve);
		
		JButton btnHistReport = new JButton("历史查询");
		btnHistReport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HistReportFrame histReport = new HistReportFrame();
				histReport.setVisible(true);
			}
		});
		
		panel.add(btnHistReport);
		
		JButton btnAlarmReport = new JButton("报警管理");
		btnAlarmReport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//MainFrame frame = new MainFrame();
				//frame.setVisible(true);
			}
		});
		
		panel.add(btnAlarmReport);
		
		JButton btnClose = new JButton("退出系统");
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		panel.add(btnClose);
		
		getContentPane().add(panel, BorderLayout.CENTER);
		setLocationRelativeTo(null);
	}
}
