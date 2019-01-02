package com.zime;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
	private JTextField txtUserName = new JTextField();
	
	private JPasswordField txtPassword = new JPasswordField();
	
	public LoginFrame() {
		initUI();
	}
	
	private void initUI() {
		setTitle("��¼");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 300);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 10, 10));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		panel.add(new JLabel("�û���"));
		panel.add(txtUserName);
		panel.add(new JLabel("����"));
		panel.add(txtPassword);
		
		JButton btnLogin = new JButton("��¼");
		panel.add(btnLogin);
		
		JButton btnCancel = new JButton("ȡ��");
		panel.add(btnCancel);
		
		btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = txtUserName.getText();
				String password = txtPassword.getText();
				
				String sql = "SELECT * FROM USER WHERE username='" + userName + "' and password='" + password + "'";
				ResultSet rs = DBUtil.getInstance().query(sql);
				try {
					if( rs.next() ) {
						LoginFrame.this.dispose();
						OperationFrame frame = new OperationFrame();
						frame.setVisible(true);
					}
					else {
						JOptionPane.showConfirmDialog(LoginFrame.this, "������û�����������!");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showConfirmDialog(LoginFrame.this, "��¼�쳣!");
				}
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		getContentPane().add(panel, BorderLayout.CENTER);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		LoginFrame frame = new LoginFrame();
		frame.setVisible(true);
	}

}
