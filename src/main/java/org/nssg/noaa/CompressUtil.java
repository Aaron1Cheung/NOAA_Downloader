package org.nssg.noaa;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class CompressUtil {
	public static void unZip(String path,String name) throws FileNotFoundException, IOException {
		GZIPInputStream Zin = new GZIPInputStream(new FileInputStream(path+File.separatorChar+name));
		byte[] buffer = new byte[1024];
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path+File.separatorChar+name.split(".gz")[0])));
        int count = -1;
        while ((count = Zin.read(buffer)) != -1) {
          out.write(buffer, 0, count);
        }
        Zin.close();
        out.close();
	}
	
}
