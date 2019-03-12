package Keming;

import java.io.File;

public class Find3 {
	
	public static void find(File directory) {
		_find(directory, "");
	}
	
	public static void _find(File directory, String indent) {
		System.out.println(indent + "\\--" + directory.getName());

		File subdirOld = null;
		if(directory.isDirectory()) {
			for(File subdir : directory.listFiles()) {
				if(subdirOld != null ) {
					_find(subdirOld, indent + "   |");
				}
				subdirOld = subdir;
			}
			if(subdirOld != null ) {
				_find(subdirOld, indent + "    ");
			}
		}
	}
	
	public static void main(String[] argv) {
		find(new File("."));
	}

}
