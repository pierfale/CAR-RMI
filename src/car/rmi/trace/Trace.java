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
	
	public void addTrace(RMINode node) throws RemoteException {
		traceList.add(node);
	}
	
	public Iterator<RMINode> getIterator() throws RemoteException {
		return traceList.iterator();
	}

	public int getLength()  throws RemoteException {
		return traceList.size();
	}

	public boolean contains(RMINode node) throws RemoteException {
		for(RMINode n : traceList) {
			if(node.getName().equals(n.getName())) 
				return true;
		}
		return false;
	}

}
