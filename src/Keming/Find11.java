package Keming;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.UUID;

//
// 问题：由于使用UUID.randomUUID(), 每次运行为目录产生的ID都不相同。如果需要比较两次运行的结果产生了困难
// 方案：将生成的UUID存放文件中，只对文件中没有的目录生成新的UUID
//
public class Find11 extends HashMap<String, String>{
	final static String FNODEDBNAME = "fnodedb.dat";
	final static String DISKINFONAME = "diskinfo.dat";
	final static int ID=0;
	final static int TYPE=1;
	final static int PATH=2;
	final static int SIZE=3;
	static String[] frecFields = new String[4];
	static FileWriter fnodeWriter;
	static FileWriter diskinfoWriter;

	public Find11(File diskinfo) throws IOException {
		super();
		load(diskinfo);
	}

	//
	// 增加FNodeDB保存每个文件的信息。从文件系统角度看，每一个目录或者文件可被称罚一个文件结点 
	// 所有文件结点的焦点，数据库。这个例子里，使用字符串来记录数据，每一行代表一条记录，记录一个
	// 文件结点的全部信息，包括唯一ID，路径，文件大小等，用制表符分开
	//
	static class FNodeDB {
		static HashMap<String, String> idMap;
		static HashMap<String, String> pathMap;
		static FileInputStream fis;
		
		public FNodeDB() {
			idMap = new HashMap<String, String>();
			pathMap = new HashMap<String, String>();
			try {
				load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void load() throws IOException {
			fis = new FileInputStream(new File(FNODEDBNAME));
			BufferedReader buf = new BufferedReader(new InputStreamReader(fis)); 

			String line = buf.readLine(); 
			while(line != null)
			{ 
//				System.out.print(columns.length+ "\t");
				System.out.println(line);
				frecFields = line.split("\t");
				idMap.put(frecFields[ID], line);
				pathMap.put(frecFields[PATH], line);
				
				line = buf.readLine(); 
			} 
			
			fis.close();
		}
		
		public String recorderSave(File directory) throws IOException {
			String uuidString;

			String fnoderec = pathMap.get(directory.getAbsolutePath());
			if ( fnoderec==null ) {
				uuidString = UUID.randomUUID().toString();
				frecFields[ID] = uuidString;
				frecFields[PATH] = directory.getAbsolutePath();
				if( directory.isDirectory() )
					frecFields[TYPE] = "d";
				else 
					frecFields[TYPE] = "f";
				frecFields[SIZE] = new Long(directory.length()).toString();
				
				fnoderec = merge(frecFields);
				idMap.put(uuidString, fnoderec);
				pathMap.put(directory.getAbsolutePath(), fnoderec);
				fnodeWriter.write( fnoderec );
			} else {
				frecFields = fnoderec.split("\t");
			}
			 return frecFields[ID];
		}

	}
	
	static public String merge(String[] frecFields) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<frecFields.length; i++) {
			sb.append( frecFields[i] );
			if(i<frecFields.length - 1 ) {
				sb.append("\t");
			} else {
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	

	static FNodeDB fnodeDB;
	public static void disassemble(File directory) throws IOException {
		fnodeDB = new FNodeDB();
		fnodeWriter = new FileWriter(FNODEDBNAME, true);
		diskinfoWriter = new FileWriter(DISKINFONAME);
		_disassemble(directory, "");
		diskinfoWriter.close();
		fnodeWriter.close();	
	}

	public static void _disassemble(File directory, String parentUUID) throws IOException {
		String uuid = fnodeDB.recorderSave(directory);
		if( parentUUID == null ) {
			String output = "\t" + uuid + "\n";
			diskinfoWriter.write(output);
		} else { 
			String output = parentUUID+ "\t" + uuid + "\n";
			diskinfoWriter.write(output);
		}

		if(directory.isDirectory()) {
			for(File subfile : directory.listFiles()) {
				_disassemble(subfile, uuid);
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
			if( columns.length == 0) {
				put("root", columns[1]);
			} else {
				String value = get(columns[0]);
				if( value == null ) {
					value = "";
				}
				value += columns[1] + "<-->";
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
	
	public void _tree(String directoryUUID, String indent, String fnodedbRec) {
		frecFields = fnodedbRec.split("\t");
		System.out.print(indent + "\\--" + merge(frecFields) );

		String subfileSet = this.get(directoryUUID);
		if(subfileSet!=null) {
			String[] subfiles = this.get(directoryUUID).split("<-->");
			for(int i=0; i<subfiles.length; i++) {
				String subfileUUID = subfiles[i];
				String rec = FNodeDB.idMap.get(subfileUUID);
				if( i < subfiles.length - 1 )
					_tree(subfileUUID, indent + "   |", FNodeDB.idMap.get(subfileUUID));
				else 
					_tree(subfileUUID, indent + "    ", FNodeDB.idMap.get(subfileUUID));
			}
		}
	}

	public static void main(String[] argv) throws IOException {
		disassemble(new File("."));

		Find11 diskinfo = new Find11(new File(DISKINFONAME));
		diskinfo.tree();
	}

}
