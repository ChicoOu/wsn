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
 * 如何使用该类？
 * 1. 在主类中调用SerialCommExample serialObj = new SerialCommExample();
 * 2. 调用serialObj.connect("COM7");连接串口
 * 3. 按照serialEvent函数中的注释，去读取串口文字、解析、写入数据库和更新界面。
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
				// 串口设置参数这里写死的
				// TODO: 建议用图形化界面配置串口参数
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();
				// 串口写不做要求
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
				System.out.print(strContent);// 实现串口输入的语句
				
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						//3.1 在这里去更新界面数据
						// 1) 25,60\n\r
						String[] result = strContent.split(",");
						int temp = Integer.parseInt(result[0]);
						
						String strHumd = result[1].replaceAll("\n", "");
						strHumd = result[1].replaceAll("\r", "");
						int humd = Integer.parseInt(strHumd);
						
						// 2) 更新曲线
						mainFrame.addTempPoint(temp);
						mainFrame.addHumdPoint(humd);
						
						// 3) 更新数据库
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
					// 这里是从标准控制台读入后写入串口
					// TODO: 用文本框作为串口输入
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
