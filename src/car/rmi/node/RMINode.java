package car.rmi.node;

import java.rmi.*;

import car.rmi.trace.Trace;

/**
 * @author pierre falez, gaetan deflandre
 * RMI interface for a node
 * A node own an unique identifier name and can propagate data
 * 
 */
public interface RMINode extends Remote {
	
	/**
	 * this methods propagate data to its successors. If trace parameter is not null, he add itself into it before transmit it to its sucessors.
	 * @param data is an array of byte which contains the data to transfer
	 * @param trace is the node history in which the propagation pass through
	 * @throws RemoteException 
	 */
	public void propagate(byte[] data, int uid, Trace trace) throws RemoteException;

	/**
	 * add a successor node to the current node
	 * @param sucessor is a rmi node
	 * @throws RemoteException 
	 */
	public void addSuccessor(RMINode sucessor) throws RemoteException;
	
	/**
	 * @param name is an unique identifier as a String
	 * @throws RemoteException 
	 */
	public void setName(String name) throws RemoteException;
	
	/**
	 * @return name of the node
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;
	
	/**
	 * Return the last received buffer with method propagate or null otherwise
	 * @return data buffer
	 * @throws RemoteException
	 */
	public byte[] getLastData() throws RemoteException;
	
	/**
	 * Return the last received trace with method propagate or null otherwise
	 * @return trace
	 * @throws RemoteException
	 */
	public Trace getLastTrace() throws RemoteException;
}
