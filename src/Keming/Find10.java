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

public class Find10 extends HashMap<String, String>{

	static FileOutputStream fos;
	
	

	public Find10(File diskinfo) throws IOException {
		super();
		load(diskinfo);
	}

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
				String value = get(columns[0]);
				if( value == null ) {
					value = "";
				}
				value += columns[1] + "\t" + columns[2] + "\t" + columns[3] + "<-->";
				put(columns[0], value);
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

	public void tree() {
		_tree(this.get("root").split("\t")[0], "", ".");
	}
	
	public void _tree(String directoryUUID, String indent, String filename) {
		System.out.println(indent + "\\--" + filename);

		String subfileSet = this.get(directoryUUID);
		if(subfileSet!=null) {
			String[] subfiles = this.get(directoryUUID).split("<-->");
			for(int i=0; i<subfiles.length; i++) {
				String subfile = subfiles[i];
				String[] subfileInfo = subfile.split("\t");
				String subfileUUID = subfileInfo[0];
				if( i < subfiles.length - 1 )
					_tree(subfileUUID, indent + "   |", subfileInfo[1] + "\t" + subfileInfo[2]);
				else 
					_tree(subfileUUID, indent + "    ", subfileInfo[1] + "\t" + subfileInfo[2]);
			}
		}
	}

	public static void main(String[] argv) throws IOException {
		//		Find6.fos = new FileOutputStream("diskinfo.dat");
		//		disassemble(new File("."));
		//		fos.close();

		Find10 diskinfo = new Find10(new File("diskinfo.dat"));
		diskinfo.tree();
	}

}
