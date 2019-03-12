package Keming;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Node4 {
	static Node4[] nodes = new Node4[100];

	String id;
	ArrayList<Node4> children;
	
	public Node4(String string) {
		id = string;
	}

	public static void graphInit() {
		for(int i=0; i<99; i++) {
			Node4 node = new Node4(new Integer(i).toString());
			nodes[i]= node;
			Node4 parentNode = nodes[ (int) (Math.random() * i) ];
			
			if( parentNode.children == null ) {
				parentNode.children = new ArrayList<Node4>();
			}
			parentNode.children.add(node);
			
			System.out.printf("%s\t%d\n", parentNode.id, i);
		}
	}
	
	public static void load(String filename) throws FileNotFoundException {
		String content = new Scanner(new File(filename)).useDelimiter("\\Z").next();
		for(String pair : content.split(",")) {
			String[] pands = pair.split(" ");
			int parent = Integer.parseInt(pands[0]);
			int thisNode = Integer.parseInt(pands[1]);
			Node4 node = new Node4(pands[1]);
			nodes[thisNode] =  node;
			if( nodes[parent].children == null ) {
				nodes[parent].children = new ArrayList<Node4>();
			}
			nodes[parent].children.add(node);
		}
	}
	
	public static void printr(Node4 node, String indent) {
		System.out.println(indent + node.id);
		
		ArrayList<Node4> children = node.children;
		if(children!=null) {
			for(int i=0; i<children.size(); i++) {
				Node4 child = children.get(i);
				if( i < children.size() - 1 ) {
					printr(child, indent + "\t");
				} else {
					printr(child, indent + "\t");
				}
			}
		}
	}
	
	public static void printr2(Node4 node, String indent) {
		System.out.println(indent + node.id);
		
		ArrayList<Node4> children = node.children;
		if(children!=null) {
			for(int i=0; i<children.size(); i++) {
				Node4 child = children.get(i);
				if( i < children.size() - 1 ) {
					printr2(child, indent + node.id + "\\");
				} else {
					printr2(child, indent + node.id + "\\");
				}
			}
		}
	}
	
	

	public static void main(String[] argv) throws FileNotFoundException {
//		graphInit();
		load("GraphSchema");
		
		for(int i=1; i<9; i++)  {
			printr( nodes[i], "");
		}
	}

}
