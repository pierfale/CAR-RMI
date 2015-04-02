package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Iterator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import car.rmi.node.RMINode;
import car.rmi.node.impl.RMIGraphNodeImpl;
import car.rmi.node.impl.RMITreeNodeImpl;
import car.rmi.trace.Trace;

public class testGraph {
	private String node1Name;
	private RMINode node1;
	private String node2Name;
	private RMINode node2;
	private String node3Name;
	private RMINode node3;
	private String node4Name;
	private RMINode node4;
	
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
		//Create  graph
		
		//     node1
		// 	   |   |
		// node2   node3
		//	   |   |
		//     node4
		try {
			
			node1Name = "node1";
			node1 = new RMIGraphNodeImpl();
			node1.setName(node1Name);
			Naming.rebind(node1Name, node1);
			
			
			node2Name = "node2";
			node2 = new RMIGraphNodeImpl();
			node2.setName(node2Name);
			Naming.rebind(node2Name, node2);
			((RMINode)Naming.lookup(node1Name)).addSuccessor(node2);
			
			node3Name = "node3";
			node3 = new RMIGraphNodeImpl();
			node3.setName(node3Name);
			Naming.rebind(node3Name, node3);
			((RMINode)Naming.lookup(node1Name)).addSuccessor(node3);
			
			node4Name = "node4";
			node4 = new RMIGraphNodeImpl();
			node4.setName(node4Name);
			Naming.rebind(node4Name, node4);
			((RMINode)Naming.lookup(node2Name)).addSuccessor(node4);
			((RMINode)Naming.lookup(node3Name)).addSuccessor(node4);
			
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
		
		assertArrayEquals(data, node2.getLastData());
		
		assertArrayEquals(data, node3.getLastData());
		
		assertArrayEquals(data, node4.getLastData());
	}
	
	@Test
	public void uniquePropagate() throws RemoteException, InterruptedException {
		byte[] data1 = {'t', 'e', 's', 't', '1'};
		byte[] data2 = {'t', 'e', 's', 't', '2'};
		
		int uid = 42;
		
		node1.propagate(data1, uid, new Trace());
		
		Thread.sleep(100);
		
		node1.propagate(data2, uid, new Trace());
		
		Thread.sleep(100);
		
		assertArrayEquals(data1, node1.getLastData());
		
		assertArrayEquals(data1, node2.getLastData());
		
		assertArrayEquals(data1, node3.getLastData());
		
		assertArrayEquals(data1, node4.getLastData());
	}
	
	@Test
	public void correctName() throws RemoteException, MalformedURLException, NotBoundException {
		assertEquals(node1Name, ((RMINode)Naming.lookup(node1Name)).getName());
		assertEquals(node2Name, ((RMINode)Naming.lookup(node2Name)).getName());
		assertEquals(node3Name, ((RMINode)Naming.lookup(node3Name)).getName());
		assertEquals(node4Name, ((RMINode)Naming.lookup(node4Name)).getName());
	}
}
