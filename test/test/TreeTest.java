package test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import car.rmi.node.RMINode;
import car.rmi.node.impl.RMITreeNodeImpl;
import car.rmi.trace.Trace;
import static org.junit.Assert.*;

public class TreeTest {
	
	private String node1Name;
	private RMINode node1;
	private String node2Name;
	private RMINode node2;
	private String node3_1Name;
	private RMINode node3_1;
	private String node3_2Name;
	private RMINode node3_2;
	
	@Before
	public void initialize() {
		//Create Tree
		
		
		
		try {
			LocateRegistry.createRegistry(1099);
			
			node1Name = "node1";
			node1 = new RMITreeNodeImpl();
			node1.setName(node1Name);
			Naming.rebind(node1Name, node1);
			
			
			node2Name = "node2";
			node2 = new RMITreeNodeImpl();
			node2.setName(node2Name);
			Naming.rebind(node2Name, node2);
			node2.addSuccessor(node1);
			
			node3_1Name = "node3_1";
			node3_1 = new RMITreeNodeImpl();
			node3_1.setName(node3_1Name);
			Naming.rebind(node3_1Name, node3_1);
			node3_1.addSuccessor(node2);
			
			node3_2Name = "node3_2";
			node3_2 = new RMITreeNodeImpl();
			node3_2.setName(node3_2Name);
			Naming.rebind(node3_2Name, node3_2);
			node3_2.addSuccessor(node2);
		} catch (RemoteException e) {
			fail("Unable to create tree (RemoteException)");
		} catch (MalformedURLException e) {
			fail("Unable to create tree (MalformedURLException)");
		}
	}
	
	@Test
	public void correctPropagate() throws RemoteException, InterruptedException {
		byte[] data = {0, 1, 2, 3, 4, 5};
		
		node1.propagate(data, 1, new Trace());
		
		//wait send
		Thread.sleep(100);
		
		assertArrayEquals(data, node1.getLastData());
		Iterator<RMINode> it1 = node1.getLastTrace().getIterator();
		
		assertEquals(node1, it1.next());
		assertFalse(it1.hasNext());
		
		assertArrayEquals(data, node2.getLastData());
		Iterator<RMINode> it2 = node2.getLastTrace().getIterator();
		assertEquals(node1, it2.next());
		assertEquals(node2, it2.next());
		assertFalse(it2.hasNext());
		
		assertArrayEquals(data, node3_1.getLastData());
		Iterator<RMINode> it3_1 = node3_1.getLastTrace().getIterator();
		assertEquals(node1, it3_1.next());
		assertEquals(node2, it3_1.next());
		
		assertArrayEquals(data, node3_2.getLastData());
		assertEquals(node1, node3_2.getLastTrace().getIterator().next());
		assertEquals(node2, node3_2.getLastTrace().getIterator().next());
	}

}
