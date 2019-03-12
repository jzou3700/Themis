package Keming;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Node {
	static Node[] nodes = new Node[100];

	String id;
	ArrayList<Node> children;
	
	public Node(String string) {
		id = string;
	}

	public static void graphInit() throws IOException {
		FileOutputStream fos = new FileOutputStream(new File("GraphSchema"));
		for(int i=0; i<99; i++) {
			Node node = new Node(new Integer(i).toString());
			nodes[i]= node;
			Node parentNode = nodes[ (int) (Math.random() * i) ];
			
			if( parentNode.children == null ) {
				parentNode.children = new ArrayList<Node>();
			}
			parentNode.children.add(node);
			
			String output = String.format("%s %d,", parentNode.id, i);
			System.out.printf(output);
			fos.write(output.getBytes());
		}
		fos.close();
	}
	
	public static void printr(Node node, String indent) {
		System.out.println(indent + node.id);
		
		ArrayList<Node> children = node.children;
		if(children!=null) {
			for(int i=0; i<children.size(); i++) {
				Node child = children.get(i);
				if( i < children.size() - 1 ) {
					printr(child, indent + "\t");
				} else {
					printr(child, indent + "\t");
				}
			}
		}
	}
	
	public static void printr2(Node node, String path) {
		System.out.println(path + node.id);
		
		ArrayList<Node> children = node.children;
		if(children!=null) {
			for(int i=0; i<children.size(); i++) {
				Node child = children.get(i);
				if( i < children.size() - 1 ) {
					printr2(child, path + node.id + "\\");
				} else {
					printr2(child, path + node.id + "\\");
				}
			}
		}
	}
	
	

	public static void main(String[] argv) throws IOException {
		graphInit();
		
		for(int i=1; i<9; i++)  {
			printr2( nodes[i], "");
		}
	}

}
