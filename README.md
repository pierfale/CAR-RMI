TP3: RMI Data tranfer
=====================
Pierre FALEZ  
Gaëtan DEFLANDRE  
02/04/2015  
- - - - - - - - - - - 


## Introduction

Transfert de données décentralisé utilisant l'API RMI en JAVA. Les
données transitent entre différents noeuds possiblement sur des
machines distinctes.


## Architecture

L'architecture est composée d'une seule interface RMI, *RMINode*, pour
représenter tous les types de noeuds (Arbre ou Graph).

Deux implémentations existent :
* Les noeuds d'arbre (*RMINodeTreeImpl*) qui peuvent posséder soit 0
  soit 1 noeud parent et N fils.
* Les noeuds de graph (*RMINodeGraphImpl*) qui peuvent posséder N
  voisins.

Chaque noeud est identifié par sa propriété *name*. Celui-ci nécessite
d'être unique pour un bon fonctionnement du système.

Les noeuds possèdent une méthode *propagate* qui permet d'envoyer un
buffer à l'ensemble de ses successeurs. En plus de ce buffer deux
paramètres sont propagés :
* Une trace contenant la liste des noeuds par lesquels le message est
  passé.
* Un identifiant unique qui permet de vérifier qu'un même message soit
  reçu par le même noeud.

#### Try/catch :

* RMITreeNodeImpl.propagate (RemoteException) : permet d'afficher les
  erreurs de transmission du buffer aux enfants.
* RMIGraphNodeImpl.propagate (RemoteException) : permet d'afficher les
  erreurs de transmission du buffer aux successeurs.


## Code samples

Création d'un noeud de graph
```
String name = args[0];

RMINode node = new RMIGraphNodeImpl();
node.setName(name);
System.out.println("Start "+name+" on address "+InetAddress.getLocalHost().getHostAddress());
		
Naming.rebind("rmi://" + InetAddress.getLocalHost().getHostAddress()  + "/"+name, node); // rmi bind with name 
		
for(int i=1; i<args.length; i++) {
	RMINode neighbourNode = (RMINode)Naming.lookup(args[i]); // get successor object
	neighbourNode.addSuccessor(node);
	node.addSuccessor(neighbourNode);
}
```

Propagation d'un buffer à tous ses successeurs de façon asynchrone
```
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
```

Gestion de la réception redondante des messages via un identifiant
```
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
```

Affichage du message et de la trace lors de l'appel à la méthode propagate
```
if(trace != null && trace.getLength() > 0) {
	System.out.println("Message : "+new String(data));
			
	System.out.println("Trace : ");
			
	int cpt = 1;
	for(Iterator<RMINode> it = trace.getIterator(); it.hasNext();cpt++) {
		RMINode current = it.next();
		System.out.println("#"+cpt+" : "+current.getName());		
	}
}
```

Gestion de l'envoie de nouveau buffer et de la génération de leurs identifiants
```
Random rand = new Random();
	
while (true) {
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	node.propagate(input.readLine().getBytes(), rand.nextInt(), new Trace());
}
```
