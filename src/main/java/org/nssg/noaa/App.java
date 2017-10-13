package org.nssg.noaa;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * NOAA DATA
 *
 */
public class App {
	static String Y1 = null;
	static String Y2 = null;
	static String P1 = null;
	static String P2 = null;

	public static void creatFace() {
		JFrame jf = new JFrame("NOAA数据下载器");
		Container con = jf.getContentPane();
		con.setLayout(new GridLayout(1, 3, 5, 5));
		JPanel p1 = new JPanel(new GridLayout(2, 2, 5, 5));
		JPanel p2 = new JPanel(new GridLayout(2, 1, 5, 5));
		JPanel p3 = new JPanel(new GridLayout(2, 1, 5, 5));

		JLabel jl1 = new JLabel("起始年份:");
		JLabel jl2 = new JLabel("结束年份:");
		JTextField jt1 = new JTextField("2016", 4);
		JTextField jt2 = new JTextField("2017", 4);

		JButton jb1 = new JButton("请选择站点列表");
		JButton jb2 = new JButton("请选择下载目录");

		JLabel jl3 = new JLabel("↓点我↓点我↓点我↓");
		JButton jb3 = new JButton("下载数据");

		p1.add(jl1);
		p1.add(jt1);
		p1.add(jl2);
		p1.add(jt2);

		p2.add(jb1);
		p2.add(jb2);

		p3.add(jl3);
		p3.add(jb3);

		con.add(p1);
		con.add(p2);
		con.add(p3);

		jf.setVisible(true);
		jf.setSize(380, 80);
		jf.setResizable(false);
		;
		jf.setLocation(500, 300);
		jf.setDefaultCloseOperation(3);

		jb1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("请选择站点列表文件……");
				fc.setApproveButtonText("确定");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				// FileFilter ff = new FileNameExtensionFilter("csv");
				// fc.setFileFilter(ff);
				int i = fc.showOpenDialog(con);
				if (i == JFileChooser.APPROVE_OPTION) {
					File sf = fc.getSelectedFile();
					P1 = sf.getAbsolutePath();
					jb1.setText(sf.getName());
				}
			}
		});
		jb2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("请选择下载路径……");
				fc.setApproveButtonText("确定");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i = fc.showOpenDialog(con);
				if (i == JFileChooser.APPROVE_OPTION) {
					File sf = fc.getSelectedFile();
					P2 = sf.getAbsolutePath();
					jb2.setText(sf.getPath());
				}
			}
		});
		jb3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ftpHost = "ftp.ncdc.noaa.gov";
				String ftpUserName = "anonymous";
				String ftpPassword = "abc@abc.com";
				String ftpPath = "pub/data/noaa";
				ArrayList<String> ids;
				Y1 = jt1.getText().replace(" ", "");
				Y2 = jt2.getText().replace(" ", "");
				int y1,y2;
				try {
					y1 = Integer.parseInt(Y1);
				    y2 = Integer.parseInt(Y2);
				}catch(NumberFormatException x) {
					jl3.setText("年份设置错误——！");
					return;
				}
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
				String y = dateFormat.format( now ); 
				if(y1<1901 || y1>Integer.parseInt(y)||y2<1901 || y2>Integer.parseInt(y)) {
					jl3.setText("年份设置错误——！");
				}else if(y1>y2) {
					jl3.setText("年份设置错误——！");
				}else if (P1 == null || P2 == null ) {
					jl3.setText("参数不全--！");
				} else {
					try {
						ids = getIds(P1);
						ArrayList<String> fn = fileList(Y1, Y2, ids);

						/*
						 * SwingUtilities.invokeLater(new Runnable() { public void run() {
						 * jl3.setText("下载进行中……"); System.out.println("hehh"); } });
						 */
						jl3.setText("下载进行中……");
						jb3.setEnabled(false);

						new Thread(new Runnable() {
							@Override
							public void run() {
								Boolean fin = null;
								try {
									fin = FtpUtil.downloadFtpFile(ftpHost, ftpUserName, ftpPassword, ftpPath, P2, fn);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (fin) {
									jl3.setText("下载成功^o^");
									jb3.setEnabled(true);
								} else {
									jl3.setText("下载失败T^T");
								}
							}
						}).start();

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						jb3.setText("下载失败T^T");
						e1.printStackTrace();
					}
				}
			}
		});
	}

	public static void main(String[] args) {
    	creatFace();
    }
    public static ArrayList<String> fileList(String year1,String year2,ArrayList<String> ids) {
    		int y1 = Integer.parseInt(year1);
    		int y2 = Integer.parseInt(year2);
    		ArrayList<String> list = new ArrayList<String>();
    		for(int y=y1;y<=y2;y++) {
    			for(String id:ids) {
    				String n = id.substring(0, 6)+"-"+id.substring(6, id.length())+"-"+y+".gz";
    				list.add(n);
    			}
    		}
		return list;
    	
    }
    public static ArrayList<String> getIds(String path) throws IOException{
    		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(P1)),"UTF-8"));
    		ArrayList<String> list = new ArrayList<String>();
    		String line = null;
		while((line = br.readLine()) != null) {
			if(!line.equals("")){
				list.add(line);
            }		
		}
		br.close();
    		return list;
    	
    }
}
