#Typesafe Activator template for Akka Cluster.

This project demos how to use AKKA cluster in client/server mode, very simple.

- To run the cluster of servers, in each terminal, type:

  ./activator "run 2551"
  ./activator "run 2552"

then select "sample.cluster.simple.DemoServerApp"

- To run any number of clients, in each terminal, type:

  ./activator run

then select "sample.cluster.simple.DemoClientApp"
