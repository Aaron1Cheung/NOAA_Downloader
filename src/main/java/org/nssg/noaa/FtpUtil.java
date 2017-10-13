package org.nssg.noaa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	
	
	public static FTPClient getFTPClient(String ftpHost, String ftpUserName,  
            String ftpPassword) {
		FTPClient ftpClient = new FTPClient();  
        try {  
            ftpClient = new FTPClient(); 
            ftpClient.connect(ftpHost);// 连接FTP服务器  
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器  
            //ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器   
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {  
                System.out.println("未连接到FTP，用户名或密码错误。");  
                ftpClient.disconnect();  
            } else {  
            	System.out.println("FTP连接成功。");  
            }  
        } catch (SocketException e) {  
            e.printStackTrace();  
            System.out.println("FTP的IP地址可能错误，请正确配置。");  
        } catch (IOException e) {  
            e.printStackTrace();  
            System.out.println("FTP的端口错误,请正确配置。");  
        }  
        return ftpClient;
		
	}

	public static Boolean downloadFtpFile(String ftpHost, String ftpUserName, String ftpPassword, String ftpPath,
			String localPath, ArrayList<String> fileName) throws IOException, InterruptedException {

		FTPClient ftpClient = null;

		ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword);
		ftpClient.setControlEncoding("UTF-8"); // 中文支持
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.enterLocalPassiveMode();
		// ftpClient.changeWorkingDirectory(ftpPath);
		for (String name : fileName) {
			String d = name.split(".gz")[0].split("-")[2];
			File dd = new File(localPath + File.separatorChar + d);
			if (!dd.exists()) {
				dd.mkdirs();
			}
			File localFile = new File(localPath + File.separatorChar + d + File.separatorChar + name);
			OutputStream os = new FileOutputStream(localFile);
			
				if(ftpClient.retrieveFile(ftpPath + "/" + d + "/" + name, os)) {
					os.close();
					CompressUtil.unZip(localPath + File.separatorChar + d,name);
					IshUtil.dotOut(localPath + File.separatorChar + d, name.split(".gz")[0]);
				}else {
					os.close();
					localFile.delete();
				}			
		}
		ftpClient.logout();
		return true;
	}

}
