#!/bin/sh

TERMINAL=xterm
DELAY=0.1

#Launch registry
cd bin
rmiregistry&
rmigesistry_pid="$!"
cd ..
sleep $DELAY

#Launch tree node
terminals_pid=""


$TERMINAL -e "java -classpath bin/ car.rmi.main.MainTree node1"&
terminals_pid="$terminals_pid $!"

$TERMINAL -e "java -classpath bin/ car.rmi.main.MainTree node2 node1"&
terminals_pid="$terminals_pid $!"

$TERMINAL -e "java -classpath bin/ car.rmi.main.MainTree node3 node1"&
terminals_pid="$terminals_pid $!"

$TERMINAL -e "java -classpath bin/ car.rmi.main.MainTree node4 node2"&
terminals_pid="$terminals_pid $!"

$TERMINAL -e "java -classpath bin/ car.rmi.main.MainTree node5 node2"&
terminals_pid="$terminals_pid $!"

#Wait end
echo -n "Press any key to continue... "
read var_end

# Clean
kill $rmigesistry_pid
for term_pid in $terminals_pid
do
	kill $term_pid
done
