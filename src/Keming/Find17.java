package Keming;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

//
//  1。 不再每次删除diskinfo.dat重建，仅在fnodedb中新增记录时，对应增加diskinfo结点对。
//  2。 使用参数类来传递参数，更好的控制变量使用范围
//	3。 在最后的树形阶段，仅显示有限深度的目录
//
public class Find17 extends HashMap<String, String>{
	final static String DISKINFONAME = "diskinfo.dat";
	final static int ID=0;
	final static int TYPE=1;
	final static int PATH=2;
	final static int SIZE=3;
	static String[] frecFields = new String[4];
	static FileWriter diskinfoWriter;

	static boolean running = true;
	class MessageBuffer implements Runnable {
		public String output ;

		public void append(String output) {
			//			if(this.output == null ) {
			//				this.output = output;
			//			} else {
			//				this.output += output;
			//			}
			this.output = output;
		}

		@Override
		public void run() {
			while(running) {
				if( output != null ) {
					System.out.print(output);
					output = null;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	} 

	static MessageBuffer messageBuffer;

	public Find17(File diskinfo) throws IOException {
		super();
		messageBuffer = new MessageBuffer();
		new Thread(messageBuffer).start();
		load(diskinfo);
		diskinfoWriter = new FileWriter(DISKINFONAME, true);
	}

	public String put(String key, String value, Parameter isNewRec) {
		// 
		// save parent-son pair into file
		//
		if( isNewRec.isNewRec ) {		// only new recorder be wrote into file
			try {
				String output = key + "\t" + value + "\n";
				diskinfoWriter.write( output );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//
		// accumulate existing value and save it
		//
		String savedValue = get(key);
		if( savedValue == null ) {
			savedValue = value;
		} else {
			savedValue += "<-->" + value ;
		}
		return put(key, savedValue);
	}


	public void load(File diskinfo) throws IOException  {
		InputStream is;
		try {
			System.out.print("Start to load map of the filesystem.\n");
			is = new FileInputStream(diskinfo);
			BufferedReader buf = new BufferedReader(new InputStreamReader(is)); 

			String line = buf.readLine(); 
			while(line != null)
			{ 
				String[] columns = line.split("\t");
				//				System.out.print(columns.length+ "\t" + line + "\n);
				if( columns.length == 2) {
					String value = get(columns[0]);
					if( value == null ) {
						value = "";
					}
					value += columns[1] + "<-->";
					put(columns[0], value);
				} else {
					is.close();
					return;
				}

				line = buf.readLine(); 
			} 
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

	static public String merge(String[] frecFields) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<frecFields.length; i++) {
			sb.append( frecFields[i] );
			if(i<frecFields.length - 1 ) {
				sb.append("\t");
			}
		}
		return sb.toString();
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

	public void close() throws IOException {
		diskinfoWriter.close();
	}

	//
	// 增加FNodeDB保存每个文件的信息。从文件系统角度看，每一个目录或者文件可被称罚一个文件结点 
	// 所有文件结点的焦点，数据库。这个例子里，使用字符串来记录数据，每一行代表一条记录，记录一个
	// 文件结点的全部信息，包括唯一ID，路径，文件大小等，用制表符分开
	//
	static class FNodeDB {
		final static String FNODEDBNAME = "fnodedb.dat";
		static FileWriter fnodeWriter;
		static HashMap<String, String> idMap;
		static HashMap<String, String> pathMap;
		static FileInputStream fis;

		public FNodeDB() throws IOException {
			idMap = new HashMap<String, String>();
			pathMap = new HashMap<String, String>();
			load();
			fnodeWriter = new FileWriter(FNODEDBNAME, true);
		}

		public void load() throws IOException {
			try {
				System.out.print("Start to load file node database.\n");
				fis = new FileInputStream(new File(FNODEDBNAME));
				BufferedReader buf = new BufferedReader(new InputStreamReader(fis)); 

				String line = buf.readLine(); 
				while(line != null)
				{ 
					//				System.out.print(columns.length+ "\t" + line + "\n");
					frecFields = line.split("\t");
					idMap.put(frecFields[ID], line);
					pathMap.put(frecFields[PATH], line);

					line = buf.readLine(); 
				} 

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if( fis !=null ) {
					fis.close();
				}
			}

		}

		public String recorderSave(File directory, Parameter isNewRec) throws IOException {
			String uuidString;

			String fnoderec = pathMap.get(directory.getAbsolutePath());
			if ( fnoderec==null ) {
				isNewRec.isNewRec = true;
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
				fnodeWriter.write( fnoderec + "\n" );
				messageBuffer.append(fnoderec + "\n");
			} else {
				isNewRec.isNewRec = false;
				frecFields = fnoderec.split("\t");
			}
			return frecFields[ID];
		}

		public void close() throws IOException {
			fnodeWriter.close();
		}
	}


	class Parameter {
		boolean isNewRec = false;
	}

	static FNodeDB fnodeDB;
	public void tree2map(File directory) throws IOException {
		_tree2map(directory, null);
	}

	public void _tree2map(File directory, String parentUUID) throws IOException {
		Parameter isNewRec = new Parameter();
		String uuid = fnodeDB.recorderSave(directory, isNewRec);
		//		if( uuid.length() != 36 )
		//			System.out.println(uuid.length());
		messageBuffer.append(directory.getAbsolutePath() + "\n");
		if( parentUUID == null ) {
			put("root", uuid, isNewRec);
		} else { 
			put(parentUUID, uuid, isNewRec); 
		}

		File[] subfiles = directory.listFiles();
		if( subfiles != null ) {
			for( File subfile : subfiles ) {
				_tree2map(subfile, uuid);
			}
		}
	}

	class NodeInfo {
		String head;
		Long size;
		String foot;

		public NodeInfo(String head, String foot) {
			this.head = head;
			this.foot = foot;
		}
	}
	
	class OutputBuffer extends Vector<NodeInfo> {

		public synchronized boolean add(NodeInfo e, int level) {
			if( level < 3) {
				return add(e);
			} else {
				return false;
			}
		}
		
	}

	public void map2tree() {
		OutputBuffer buf = new OutputBuffer();
		long size = _map2tree(buf, this.get("root").split("<-->")[0], "", ".", 0);
		for(NodeInfo nodeInfo : buf) {
			System.out.print(nodeInfo.head + "(" + nodeInfo.size + ") " + nodeInfo.foot + "\n" );
		}
		System.out.println("total size is " + size/1024 + "K");
	}

	private String fnodeInfo(String[] fields) {
		String path = fields[PATH];
		String dirs[] = path.split("\\\\");
		String filename = dirs[dirs.length-1];
		return filename + "\t" + fields[SIZE];
	}

	public long _map2tree(OutputBuffer buf, String directoryUUID, String indent, String fnodedbRec, int level) {
		long size=0;
		frecFields = fnodedbRec.split("\t");
		NodeInfo nodeInfo;
		if( frecFields.length < 4 ) {
			nodeInfo = new NodeInfo(indent + "\\--",  frecFields[0]);
			buf.add( nodeInfo, level );
		} else {
			size +=  Long.parseLong(frecFields[SIZE]);
			nodeInfo = new NodeInfo(indent + "\\-- ", fnodeInfo(frecFields));
			buf.add( nodeInfo, level );
		}

		String subfileSet = this.get(directoryUUID);
		if(subfileSet!=null) {
			String[] subfiles = this.get(directoryUUID).split("<-->");
			for(int i=0; i<subfiles.length; i++) {
				String subfileUUID = subfiles[i];
				String fnodedbRec2 = FNodeDB.idMap.get(subfileUUID);
				if( i < subfiles.length - 1 )
					size += _map2tree(buf, subfileUUID, indent + "   |", fnodedbRec2, level + 1 );
				else 
					size += _map2tree(buf, subfileUUID, indent + "    ", fnodedbRec2, level + 1 );
			}
		}

		return nodeInfo.size = size;
	}


	static public void test(String filename) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(filename));
			BufferedReader buf = new BufferedReader(new InputStreamReader(fis)); 

			String line = buf.readLine(); 
			while(line != null)
			{ 
				System.out.print( line + "\n");
				line = buf.readLine(); 
			} 

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if( fis !=null ) {
				fis.close();
			}
		}

	}

	public static void main(String[] argv) throws IOException {
		Find17 diskinfo = new Find17(new File(DISKINFONAME));
		fnodeDB = new FNodeDB();
//		diskinfo.tree2map(new File("c:\\"));
//		diskinfo.close();
		fnodeDB.close();	
		diskinfo.map2tree();

		running = false;		// shutdown the message buffer thread.
	}

}
