package Keming;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Node3 {
	static Node3[] nodes = new Node3[100];

	String id;
	ArrayList<Node3> children;
	
	public Node3(String string) {
		id = string;
	}

	public static void graphInit() {
		for(int i=0; i<99; i++) {
			Node3 node = new Node3(new Integer(i).toString());
			nodes[i]= node;
			Node3 parentNode = nodes[ (int) (Math.random() * i) ];
			
			if( parentNode.children == null ) {
				parentNode.children = new ArrayList<Node3>();
			}
			parentNode.children.add(node);
			
			System.out.printf("%s %d", parentNode.id, i);
		}
	}
	
	public static void load(String filename) throws FileNotFoundException {
		String content = new Scanner(new File(filename)).useDelimiter("\\Z").next();
		for(String pair : content.split(",")) {
			String[] pands = pair.split(" ");
			int parent = Integer.parseInt(pands[0]);
			int thisNode = Integer.parseInt(pands[1]);
			Node3 node = new Node3(pands[1]);
			nodes[thisNode] =  node;
			if( nodes[parent].children == null ) {
				nodes[parent].children = new ArrayList<Node3>();
			}
			nodes[parent].children.add(node);
		}
	}
	
	public static void printr(Node3 node, String indent) {
		System.out.println(indent + "\\--" + node.id);
		
		ArrayList<Node3> children = node.children;
		if(children!=null) {
			for(int i=0; i<children.size(); i++) {
				Node3 child = children.get(i);
					if( i < children.size() - 1 ) {
						printr(child, indent + "   |");
					} else {
						printr(child, indent + "    ");
					}
			}
		}
	}
	
	public static void printr(Node3 node, String indent, Stack<Node3> path) {
		path.push(node);
		String indent2=indent;
		if ( indent.length() > 2 ) {
			indent2 = indent.substring(1, indent.length()-1) + "+";
		}
		System.out.println(indent2 + "---" + node.id);
//		System.out.println(indent + "\\--" + node.id);
		
		ArrayList<Node3> children = node.children;
		if(children!=null) {
			for(int i=0; i<children.size(); i++) {
				Node3 child = children.get(i);
				if( !path.contains(child) ) {
					if( i < children.size() - 1 ) {
						printr(child, indent + "   |", path);
					} else {
						printr(child, indent + "    ", path);
					}
				}
			}
		}
		path.pop();
	}
	
	public static void main(String[] argv) throws FileNotFoundException {
//		graphInit();
		load("GraphSchema");
		
		for(int i=1; i<9; i++)  {
			printr( nodes[i], "", new Stack<Node3>());
		}
	}

}
