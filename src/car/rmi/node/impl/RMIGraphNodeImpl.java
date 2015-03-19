package car.rmi.node.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import car.rmi.node.RMINode;
import car.rmi.trace.Trace;

/**
 * @author pierre falez, gaetan deflandre
 * 
 *  RMI graph node implementation.
 *  the node network can have cycle.
 *  When propagate a message, the node send the data to all the successor which are not in the trace.
 *
 */
public class RMIGraphNodeImpl extends UnicastRemoteObject implements RMINode {
	
	private static final long serialVersionUID = 1966416235310050690L;
	private String name;
	private List<RMINode> neighbours;
	
	public RMIGraphNodeImpl() throws RemoteException {
		neighbours = new ArrayList<RMINode>();
	}

	@Override
	public void propagate(final byte[] data, final Trace trace) throws RemoteException {
		
		if(trace != null && trace.getLength() > 0) {
			System.out.println("Message : "+new String(data));
			
			System.out.println("Trace : ");
			
			int cpt = 1;
			for(Iterator<RMINode> it = trace.getIterator(); it.hasNext();cpt++) {
				RMINode current = it.next();
				System.out.println("#"+cpt+" : "+current.getName());
				
			}
		}
		
		if(trace != null)
			trace.addTrace(this);
		
		
		for(final RMINode node : neighbours) {
			if(trace == null || !trace.contains(node)) {
				Thread t = new Thread() {
					public void run() {
						try {
							node.propagate(data, trace);
						} catch (RemoteException e) {
							System.err.println("Error when sending message a child");
						}
					}
				};
				
				t.run();
			}
		}
	}

	@Override
	public void addSuccessor(RMINode sucessor) throws RemoteException {
		System.out.println("New child added : "+sucessor.getName());
		this.neighbours.add(sucessor);
	}

	@Override
	public void setName(String name) throws RemoteException {
		this.name = name;
	}

	@Override
	public String getName() throws RemoteException {
		return this.name;
	}
	
}