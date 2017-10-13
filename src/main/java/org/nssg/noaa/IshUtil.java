package org.nssg.noaa;

import java.io.File;
import java.io.IOException;

public class IshUtil {
	private static String OS = System.getProperty("os.name").toLowerCase(); 
	private static String DIR = System.getProperty("user.dir");
	public static void dotOut(String path,String name) throws IOException, InterruptedException {	
		String[] cmd1 = {"java","-classpath",DIR,"ishJava",path+File.separatorChar+name,path+File.separatorChar+name+".out"};
		//String cmd2 = "cmd.exe /C start java -classpath "+DIR+" ishJava "+path+File.separatorChar+name+""+path+File.separatorChar+name+".out";
		Process p = null;
		if(isLinux()||isMacOS()||isMacOSX()) {
			p = Runtime.getRuntime().exec(cmd1);
		}else if(isWindows()) {
			p = Runtime.getRuntime().exec(cmd1);
		}
		
		p.waitFor();
		File f1 = new File(path+File.separatorChar+name);
		File f2 = new File(path+File.separatorChar+name+".gz");
		f1.delete();
		f2.delete();
	}
	public static boolean isLinux(){  
        return OS.indexOf("linux")>=0;  
    }  
      
    public static boolean isMacOS(){  
        return OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")<0;  
    }  
      
    public static boolean isMacOSX(){  
        return OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")>0;  
    }  
      
    public static boolean isWindows(){  
        return OS.indexOf("windows")>=0;  
    }  
	
}
