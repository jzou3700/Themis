package Keming;

import java.io.File;

public class Find2 {
	
	public static void find(File directory) {
		_find(directory, "");
	}
	
	public static void _find(File directory, String indent) {
		System.out.println(indent + "\\--" + directory.getName());

		if(directory.isDirectory()) {
			File[] subfiles = directory.listFiles();
			for(int i=0; i<subfiles.length; i++) {
				File subfile = subfiles[i];
				if( i < subfiles.length - 1 )
					_find(subfile, indent + "   |");
				else 
					_find(subfile, indent + "    ");
			}
		}
	}
	
	public static void main(String[] argv) {
		find(new File("."));
	}

}
