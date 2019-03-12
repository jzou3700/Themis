package Keming;

import java.io.File;
import java.util.UUID;

public class Find4 {
	
	public static void disassemble(File directory) {
		_disassemble(directory, null);
	}
	
	private static String UUID(File file) {
		return java.util.UUID.fromString(file.getAbsolutePath()).toString();
	}
	
	public static void _disassemble(File directory, UUID parentUUID) {
		UUID directoryUUID = UUID.randomUUID();
		if( parentUUID == null )
			System.out.println("\t" + directoryUUID );
		else 
			System.out.println(parentUUID+ "\t" + directoryUUID
					+ "\t" + directory.getName() 
					+ "\t" + directory.length() );

		if(directory.isDirectory()) {
			File[] subfiles = directory.listFiles();
			for(int i=0; i<subfiles.length; i++) {
				File subfile = subfiles[i];
				if( i < subfiles.length - 1 )
					_disassemble(subfile, directoryUUID);
				else 
					_disassemble(subfile, directoryUUID);
			}
		}
	}
	
	public static void main(String[] argv) {
		disassemble(new File("."));
	}

}
