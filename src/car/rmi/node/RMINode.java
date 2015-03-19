package car.rmi.node;

import java.rmi.*;

import car.rmi.trace.Trace;

/**
 * RMI interface for a node
 * A node own an unique identifier name and can propagate data
 * 
 */
public interface RMINode extends Remote {
	
	/**
	 * this methods propagate data to its successors. If trace parameter is not null, he add itself into it before transmit it to its sucessors.
	 * @param data is an array of byte which contains the data to transfer
	 * @param trace is the node history in which the propagation pass through
	 */
	public void propagate(byte[] data, Trace trace) throws RemoteException;

	public void addSuccessor(RMINode sucessor) throws RemoteException;
	
	/**
	 *	Identification methods
	 */
	public void setName(String name) throws RemoteException;
	
	public String getName() throws RemoteException;
}
