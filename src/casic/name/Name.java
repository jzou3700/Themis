package casic.name;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner; 
public class Name 
{ 


	public static void main(String[] args) throws Exception {
		//		sortChineseNames();
		//		loadChineseNames();
		Fabnacci fabnacci = new Fabnacci(10);
	}

	public static void sortChineseNames() throws FileNotFoundException  
	{ 
		File file = new File("C:\\Users\\jzoud\\git\\Themis\\chinese_surnames.txt"); 
		Scanner sc = new Scanner(file); 
		PrintWriter writer = new PrintWriter("sorted_chinese_surnames.txt");

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] words = line.split("\t");
			for(int i=0; i<words.length; i++) {
				if( i>0 ) {
					System.out.println(words[i]);
					writer.println(words[i]);
				}
			}
		}

		writer.close();
		sc.close();	
	} 

	public static void loadChineseNames() throws FileNotFoundException {
		File file = new File("sorted_chinese_surnames.txt"); 
		Scanner sc = new Scanner(file); 

		int i=0;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			System.out.println( i++ + "\t" + line );
		}

		sc.close();
	}

	static class Fabnacci {
		int maxLevel;
		
		public Fabnacci(int maxLevel) {
			this.maxLevel = maxLevel;
			_fabnacci(null, 0);
		}

		private int[] _fabnacci(int[] array, int level) {
			if( level < maxLevel ) {
				if( array == null ) {
					array = new int[level];
				}
				int[] targetArray = new int[level+1]; 

				targetArray[0] = 1;
				for(int i=0; i<array.length-1; i++) {
					targetArray[i+1] = array[i] + array[i+1];
				}
				targetArray[targetArray.length-1] = 1;
				
				print(targetArray);
				
				int[] targetArray2 = _fabnacci(targetArray, level + 1 );
				
				if( targetArray2 != null ) {
					for(int i=0; i<targetArray2.length-1; i++) {
						targetArray[i] = targetArray2[i] + targetArray2[i+1];
					}
					
					print(targetArray);
				} else 
					return targetArray;
				
				return targetArray;
			}
			return null;
		}
		
		public void print(int[] data) {
			for(int i=0; i<data.length; i++) {
				System.out.print(data[i] + "\t");
			}
			System.out.println();
		}
	}
} 
