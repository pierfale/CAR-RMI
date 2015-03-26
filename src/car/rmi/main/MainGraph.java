package car.rmi.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Random;

import car.rmi.node.RMINode;
import car.rmi.node.impl.RMIGraphNodeImpl;
import car.rmi.trace.Trace;

public class MainGraph {

	public static final String usage = "command <node-name> [<neighbour-name]*";

	public RMINode create(String args[]) throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException {

		if(args.length == 0) {
			System.err.println("Require node name");
			System.err.println("Usage : "+usage);
			return null;
		}

		String name = args[0];

		RMINode node = new RMIGraphNodeImpl();
		node.setName(name);
		Naming.rebind("rmi://" + InetAddress.getLocalHost().getHostAddress()  + "/"+name, node);
		
		for(int i=1; i<args.length; i++) {
			RMINode neighbourNode = (RMINode)Naming.lookup(args[i]);
			neighbourNode.addSuccessor(node);
			node.addSuccessor(neighbourNode);
		}

		return node;
	}

	public static void main(String[] args) throws NotBoundException, IOException {
			
		RMINode node = new MainGraph().create(args);
		
		if(node == null)
			return;
		
		Random rand = new Random();
	
		while (true) {
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
			node.propagate(input.readLine().getBytes(), rand.nextInt(), new Trace());
	
		}
	}
}
