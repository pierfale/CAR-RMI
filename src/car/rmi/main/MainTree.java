package car.rmi.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

import car.rmi.node.RMINode;
import car.rmi.node.impl.RMITreeNodeImpl;
import car.rmi.trace.Trace;

public class MainTree {

	public static final String usage = "command <node-name> [<parent-name>]";

	/**
	 * Create a new RMI node, use for the tree, by parsing a command line. 
	 * This method binds the RMI node in relation to the argument name, and add 
	 * itself to its parent using RMI.
	 * The created links are oriented from the parent to the children.
	 * 
	 * @param args The arguments of command line
	 * @return a register RMI node
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public RMINode create(String args[]) throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException {

		String parent = "";

		if(args.length == 0) {
			System.err.println("Require node name");
			System.err.println("Usage : "+usage);
			return null;
		}
		
		if(args.length > 2) {
			System.err.println("Too many arguments");
			System.err.println("Usage : "+usage);
			return null;
		}

		String name = args[0];
		if(args.length == 2) {
			parent = args[1];
		}

		RMINode node = new RMITreeNodeImpl();
		node.setName(name);
		Naming.rebind (name, node);


		if(!parent.equals("")) {
			RMINode parentNode = (RMINode)Naming.lookup(parent);
			parentNode.addSuccessor(node);
		}

		return node;
	}

	public static void main(String[] args) throws NotBoundException, IOException {
	
		RMINode node = new MainTree().create(args);
		
		if(node == null)
			return;
		
		Random rand = new Random();
	
		while (true) {
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
			node.propagate(input.readLine().getBytes(), rand.nextInt(), new Trace());
	
		}
	}

}
