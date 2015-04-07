package car.rmi.trace;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import car.rmi.node.RMINode;

public class Trace implements Serializable {
	
	private static final long serialVersionUID = -262786437431615714L;
	
	List<RMINode> traceList;
	
	public Trace()  throws RemoteException {
		traceList = new ArrayList<RMINode>();
	}
	
	/**
	 * Add a node into trace history
	 * @param node
	 * @throws RemoteException
	 */
	public void addTrace(RMINode node) throws RemoteException {
		traceList.add(node);
	}
	
	/**
	 * Get an iterator over trace, begin with the oldest node
	 * @return
	 * @throws RemoteException
	 */
	public Iterator<RMINode> getIterator() throws RemoteException {
		return traceList.iterator();
	}

	/**
	 * Get the number of node in this trace
	 * @return
	 * @throws RemoteException
	 */
	public int getLength()  throws RemoteException {
		return traceList.size();
	}
	
	
	/**
	 * Return true if the node already exist in this trace, false otherwise
	 * @param node
	 * @return
	 * @throws RemoteException
	 */
	public boolean contains(RMINode node) throws RemoteException {
		for(RMINode n : traceList) {
			if(node.getName().equals(n.getName())) 
				return true;
		}
		return false;
	}

}
