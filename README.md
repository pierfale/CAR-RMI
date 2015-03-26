TP3: RMI Data tranfer
=====================
Pierre FALEZ  
Gaëtan DEFLANDRE  
02/04/2015  
- - - - - - - - - - - 

mkdir bin
javac -d bin -sourcepath src src/car/rmi/main/Main*.java
...
cd bin
// sur les impl
rmic car.rmi.node.impl.RMIGraphNodeImpl
...
rmiregistry &
java car.rmi.main.MainGraph nodep18

- - - - - - - -
http://www.jmdoudoux.fr/java/dej/chap-rmi.htm

- - - - - - - -
Voir pour security poticy
java -classpath classDir -Djava.rmi.server.codebase=file:classDir/ -Djava.security.policy=Server.policy Server

## Introduction

## Architecture

## Code samples
