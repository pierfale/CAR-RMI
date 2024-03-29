package car.rmi.node.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import car.rmi.node.RMINode;
import car.rmi.trace.Trace;

public class RMITreeNodeImpl extends UnicastRemoteObject implements RMINode {
	
	private static final long serialVersionUID = 6762871193609365017L;
		
	private String name;
	private List<RMINode> childs;
	
	private byte[] lastData;
	private Trace lastTrace;
	
	public RMITreeNodeImpl() throws RemoteException {
		childs = new ArrayList<RMINode>();
	}

	/**
	 * Redefine propagate method for a tree node.
	 * In this implementation (tree) the uid is ignored.
	 * 
	 * @param data is a byte array containing the content of the message
	 * @param uid is an unique identifier of this message
	 * @param trace is the history of last node visited by this message
	 */
	@Override
	public void propagate(final byte[] data, final int uid, final Trace trace) throws RemoteException {
		
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
		
		lastData = data;
		lastTrace = trace;
		
		
		for(final RMINode node : childs) {
			Thread t = new Thread() {
				public void run() {
					try {
						node.propagate(data, uid, trace);
					} catch (RemoteException e) {
						System.err.println("Error when sending message a child");
					}
				}
			};
			
			t.run();
		}
	}

	@Override
	public void addSuccessor(RMINode child) throws RemoteException {
		System.out.println("New child added : "+child.getName());
		this.childs.add(child);
	}

	@Override
	public void setName(String name) throws RemoteException {
		this.name = name;
	}

	@Override
	public String getName() throws RemoteException {
		return this.name;
	}

	@Override
	public byte[] getLastData() throws RemoteException {
		return lastData;
	}

	@Override
	public Trace getLastTrace() throws RemoteException {
		return lastTrace;
	}

}
