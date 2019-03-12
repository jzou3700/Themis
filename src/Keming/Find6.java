package Keming;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.UUID;

public class Find6 {

	static FileOutputStream fos;

	public static void disassemble(File directory) throws IOException {
		_disassemble(directory, null);
	}

	public static void _disassemble(File directory, UUID parentUUID) throws IOException {
		UUID directoryUUID = UUID.randomUUID();
		if( parentUUID == null ) {
			String output = "\t" + directoryUUID + "\n";
			System.out.print( output);
			fos.write(output.getBytes());
		} else { 
			String output = parentUUID+ "\t" + directoryUUID
					+ "\t" + directory.getName() 
					+ "\t" + directory.length() + "";
			System.out.print( output );
			fos.write(output.getBytes());
		}

		if(directory.isDirectory()) {
			for(File subfile : directory.listFiles()) {
				_disassemble(subfile, directoryUUID);
			}
		}
	}

	static HashMap<String, String> dirmap;
	public static void load(File diskinfo) throws IOException {
		dirmap = new HashMap<String, String>();
		
		InputStream is = new FileInputStream(diskinfo); 
		BufferedReader buf = new BufferedReader(new InputStreamReader(is)); 
		
		String line = buf.readLine(); 
		while(line != null)
		{ 
			String[] columns = line.split("\t");
//			System.out.print(columns.length+ "\t");
//			System.out.println(line);
			if( columns.length == 2 ) {
				dirmap.put("root", columns[1]);
			} else {
				dirmap.put(columns[0], columns[1] + columns[2] + columns[3] );
			}
			
			line = buf.readLine(); 
		} 
		is.close();
		
		System.out.println(dirmap);
	}

	public static void main(String[] argv) throws IOException {
		//		Find6.fos = new FileOutputStream("diskinfo.dat");
		//		disassemble(new File("."));
		//		fos.close();

		load(new File("diskinfo.dat"));
	}

}
