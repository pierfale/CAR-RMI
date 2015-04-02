package car.rmi.node.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	private Map<Long, List<Integer>> receiveHistory;
	
	private byte[] lastData;
	private Trace lastTrace;
	
	public RMIGraphNodeImpl() throws RemoteException {
		neighbours = new ArrayList<RMINode>();
		receiveHistory = new HashMap<Long, List<Integer>>();
	}
	
	private boolean isNewMessage(int uid) {
		long currentTimestamp = Calendar.getInstance().getTime().getTime();
		int saveDelay = 10000; // 10 sec
		boolean received = false;
		
		// check uid history
		List<Long> objectRemove = new ArrayList<Long>();
		for(Map.Entry<Long, List<Integer>> entry : receiveHistory.entrySet()) {
			if(entry.getValue().contains(new Integer(uid))) {
				received = true;
			}
			
			if(entry.getKey().longValue()+saveDelay < currentTimestamp) {
				objectRemove.add(entry.getKey());
			}
		}
		
		// remove oldest id
		for(Long key : objectRemove) {
			receiveHistory.remove(key);
		}
		
		if(!received) {
		
			// add new uid
			List<Integer> listUid = null;
			if(receiveHistory.containsKey(new Long(currentTimestamp))) {
				listUid = receiveHistory.get(new Long(currentTimestamp));
			}
			else {
				listUid = new ArrayList<Integer>();
				receiveHistory.put(new Long(currentTimestamp), listUid);
			}
			
			listUid.add(new Integer(uid));
		}
		
		return !received;
	}

	@Override
	public void propagate(final byte[] data, final int uid, final Trace trace) throws RemoteException {
		
		if(!isNewMessage(uid)) // message already received
			return;
		
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
		
		for(final RMINode node : neighbours) {
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
	public void addSuccessor(RMINode sucessor) throws RemoteException {
		System.out.println("["+this.name+"] New child added : "+sucessor.getName());
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

	@Override
	public byte[] getLastData() throws RemoteException {
		return lastData;
	}

	@Override
	public Trace getLastTrace() throws RemoteException {
		return lastTrace;
	}
	
}