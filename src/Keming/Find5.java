package Keming;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Find5 {
	
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
					+ "\t" + directory.length() + "\n";
			System.out.print( output );
			fos.write(output.getBytes());
		}
		
		if(directory.isDirectory()) {
			for(File subfile : directory.listFiles()) {
				_disassemble(subfile, directoryUUID);
			}
		}
	}
	
	public static void main(String[] argv) throws IOException {
		Find5.fos = new FileOutputStream("diskinfo.dat");
		disassemble(new File("."));
		fos.close();
	}

}
