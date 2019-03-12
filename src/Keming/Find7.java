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

public class Find7 extends HashMap<String, String>{

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

	public void load(File diskinfo) throws IOException {
		InputStream is = new FileInputStream(diskinfo); 
		BufferedReader buf = new BufferedReader(new InputStreamReader(is)); 
		
		String line = buf.readLine(); 
		while(line != null)
		{ 
			String[] columns = line.split("\t");
//			System.out.print(columns.length+ "\t");
//			System.out.println(line);
			if( columns.length == 2 ) {
				put("root", columns[1]);
			} else {
				put(columns[0], columns[1] + columns[2] + columns[3] );
			}
			
			line = buf.readLine(); 
		} 
		is.close();
		
		System.out.println(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String key : this.keySet() ) {
			String value = this.get(key);
			sb.append(key);
			sb.append("\t");
			sb.append(value);
			sb.append("\n");
		}
		return sb.toString();
	}


	public static void main(String[] argv) throws IOException {
		//		Find6.fos = new FileOutputStream("diskinfo.dat");
		//		disassemble(new File("."));
		//		fos.close();

		new Find7().load(new File("diskinfo.dat"));
	}

}
