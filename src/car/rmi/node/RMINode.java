package car.rmi.node;

import java.rmi.*;

import car.rmi.trace.Trace;

/**
 * RMI interface for a node
 * A node own an unique identifier name and can propagate data to its successor
 * 
 * @author pierre falez, gaetan deflandre
 */
public interface RMINode extends Remote {
	
	/**
	 * this methods propagate data to its successors. If trace parameter is not null, he add itself and transmit it to its successors.
	 * the uid parameter is used to recognize the message by an identifier. this can be useful if cycles are present in the network
	 * @param data is an array of byte which contains the data to transfer
	 * @param uid is an unique identifier for the message
	 * @param trace is the node history in which the propagated message pass through
	 * @throws RemoteException is throw when issue occurs with RMI
	 */
	public void propagate(byte[] data, int uid, Trace trace) throws RemoteException;

	/**
	 * add a successor node to the current node
	 * @param sucessor is a rmi node
	 * @throws RemoteException is throw when issue occurs with RMI
	 */
	public void addSuccessor(RMINode sucessor) throws RemoteException;
	
	/**
	 * @param name is an unique identifier as a String used by RMI registry
	 * @throws RemoteException is throw when issue occurs with RMI
	 */
	public void setName(String name) throws RemoteException;
	
	/**
	 * @return name of the node
	 * @throws RemoteException is throw when issue occurs with RMI
	 */
	public String getName() throws RemoteException;
	
	/**
	 * Return the last received buffer with method propagate or null otherwise
	 * @return data buffer
	 * @throws RemoteException is throw when issue occurs with RMI
	 */
	public byte[] getLastData() throws RemoteException;
	
	/**
	 * Return the last received trace with method propagate or null otherwise
	 * @return trace
	 * @throws RemoteException is throw when issue occurs with RMI
	 */
	public Trace getLastTrace() throws RemoteException;
}
