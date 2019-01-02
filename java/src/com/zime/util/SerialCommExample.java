package com.zime.util;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingUtilities;

import com.zime.DBUtil;
import com.zime.MainFrame;

/**
 * ���ʹ�ø��ࣿ
 * 1. �������е���SerialCommExample serialObj = new SerialCommExample();
 * 2. ����serialObj.connect("COM7");���Ӵ���
 * 3. ����serialEvent�����е�ע�ͣ�ȥ��ȡ�������֡�������д�����ݿ�͸��½��档
 */
public class SerialCommExample {
	private MainFrame mainFrame;
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public SerialCommExample() {
		super();
	}

	public void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				// �������ò�������д����
				// TODO: ������ͼ�λ��������ô��ڲ���
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();
				// ����д����Ҫ��
				SerialWriter sw = new SerialWriter(out);
				Thread taskWriter = new Thread(sw);
				taskWriter.start();

				SerialReader reader = new SerialReader(in);
				serialPort.addEventListener(reader);
				serialPort.notifyOnDataAvailable(true);
			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	public class SerialReader implements SerialPortEventListener {

		private InputStream in;
		String temp;
		String humd;
		private byte[] buffer = new byte[1024];

		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void serialEvent(SerialPortEvent evt) {
			int data;

			try {
				int len = 0;
				while ((data = in.read()) > -1) {
					if (data == '\n') {
						break;
					}
					buffer[len++] = (byte) data;
				}
				final String strContent = new String(buffer, 0, len);
				System.out.print(strContent);// ʵ�ִ�����������
				
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						//3.1 ������ȥ���½�������
						// 1) 25,60\n\r
						String[] result = strContent.split(",");
						int temp = Integer.parseInt(result[0]);
						
						String strHumd = result[1].replaceAll("\n", "");
						strHumd = result[1].replaceAll("\r", "");
						int humd = Integer.parseInt(strHumd);
						
						// 2) ��������
						mainFrame.addTempPoint(temp);
						mainFrame.addHumdPoint(humd);
						
						// 3) �������ݿ�
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						String sql = "INSERT INTO sensordata(temp,humd,time) values(" + temp + "," + humd + ",'" + sdf.format(new Date()) + "')";
						DBUtil.getInstance().insert(sql);
					}
				});

			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

	}

	/** */
	public class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		public void run() {
			while(true) {
				try {
					char c = mainFrame.getCmd();
					out.write(c);
					
					Thread.sleep(5000);
					// �����Ǵӱ�׼����̨�����д�봮��
					// TODO: ���ı�����Ϊ��������
	//				while ((c = System.in.read()) > -1) {
	//					this.out.write(c);
	//				}
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		SerialCommExample serialObj = new SerialCommExample();
		serialObj.connect("COM4");
	}
}
