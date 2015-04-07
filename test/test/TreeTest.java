package test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
	
	@BeforeClass
	public static void classInitialize() {
		try {
			LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			fail("Unable to create tree (RemoteException)");
		}
	}
	
	@Before
	public void initialize() {
		//Create Tree
		
		//      node1
		// 	      |
		//      node2
		//	    |   |
		// node3_1 node3_1
		
		try {
			
			node1Name = "node1";
			node1 = new RMITreeNodeImpl();
			node1.setName(node1Name);
			Naming.rebind(node1Name, node1);
			
			
			node2Name = "node2";
			node2 = new RMITreeNodeImpl();
			node2.setName(node2Name);
			Naming.rebind(node2Name, node2);
			((RMINode)Naming.lookup(node1Name)).addSuccessor(node2);
			
			node3_1Name = "node3_1";
			node3_1 = new RMITreeNodeImpl();
			node3_1.setName(node3_1Name);
			Naming.rebind(node3_1Name, node3_1);
			((RMINode)Naming.lookup(node2Name)).addSuccessor(node3_1);
			
			node3_2Name = "node3_2";
			node3_2 = new RMITreeNodeImpl();
			node3_2.setName(node3_2Name);
			Naming.rebind(node3_2Name, node3_2);
			((RMINode)Naming.lookup(node2Name)).addSuccessor(node3_2);
			
			Thread.sleep(100);
		} catch (RemoteException e) {
			fail("Unable to create tree (RemoteException)");
		} catch (MalformedURLException e) {
			fail("Unable to create tree (MalformedURLException)");
		} catch (InterruptedException e) {
			fail("Unable to create tree (InterruptedException)");
		} catch (NotBoundException e) {
			fail("Unable to create tree (NotBoundException)");
		}
	}
	
		@Test
	public void correctPropagate() throws RemoteException, InterruptedException {
		byte[] data = {'t', 'e', 's', 't'};
		
		node1.propagate(data, 1, new Trace());
		
		//wait send
		Thread.sleep(100);
		
		assertArrayEquals(data, node1.getLastData());
		Iterator<RMINode> it1 = node1.getLastTrace().getIterator();
		
		assertEquals(node1.getName(), it1.next().getName());
		assertFalse(it1.hasNext());
		
		assertArrayEquals(data, node2.getLastData());
		Iterator<RMINode> it2 = node2.getLastTrace().getIterator();
		assertEquals(node1.getName(), it2.next().getName());
		assertEquals(node2.getName(), it2.next().getName());
		assertFalse(it2.hasNext());
		
		assertArrayEquals(data, node3_1.getLastData());
		Iterator<RMINode> it3_1 = node3_1.getLastTrace().getIterator();
		assertEquals(node1.getName(), it3_1.next().getName());
		assertEquals(node2.getName(), it3_1.next().getName());
		assertEquals(node3_1.getName(), it3_1.next().getName());
		assertFalse(it3_1.hasNext());
		
		assertArrayEquals(data, node3_2.getLastData());
		Iterator<RMINode> it3_2 = node3_2.getLastTrace().getIterator();
		assertEquals(node1.getName(), it3_2.next().getName());
		assertEquals(node2.getName(), it3_2.next().getName());
		assertEquals(node3_2.getName(), it3_2.next().getName());
		assertFalse(it3_2.hasNext());
	}
	
	@Test
	public void correctName() throws RemoteException, MalformedURLException, NotBoundException {
		assertEquals(node1Name, ((RMINode)Naming.lookup(node1Name)).getName());
		assertEquals(node2Name, ((RMINode)Naming.lookup(node2Name)).getName());
		assertEquals(node3_1Name, ((RMINode)Naming.lookup(node3_1Name)).getName());
		assertEquals(node3_2Name, ((RMINode)Naming.lookup(node3_2Name)).getName());
	}

}
