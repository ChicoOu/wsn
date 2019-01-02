package com.zime;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class HistReportFrame extends JFrame {
	private DefaultTableModel tm = new DefaultTableModel(new String[] {
			"id", "温度", "湿度", "时间"
	}, 0);
	
	public HistReportFrame() {
		initUI();
		refresh();
	}
	
	private void initUI() {
		JTable table = new JTable(tm);
		JScrollPane pane = new JScrollPane(table);
		getContentPane().add(pane, BorderLayout.CENTER);
		setSize(800, 600);
		setTitle("历史信息查询");
		setLocationRelativeTo(null);
	}
	
	public void refresh() {
		ResultSet rs = DBUtil.getInstance().query("SELECT * FROM sensordata");
		try {
			while( rs.next() ) {
				tm.addRow(new Object[] {rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4)});
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
