package Keming;

import java.io.File;

public class Find {
	
	public static void find(File directory) {
		System.out.println(directory.getName());

		for(File subdir : directory.listFiles()) {
			if(subdir.isDirectory())
				find(subdir);
		}
	}
	
	public static void main(String[] argv) {
		find(new File("."));
	}

}
