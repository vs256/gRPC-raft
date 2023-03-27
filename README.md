# gRPC Raft Consensus Algorithm

## Server Roles

- **Leader** 
  - Only the server elected as leader can interact with the client. All other servers sync up themselves with the leader. At any point of time, there can be at most one leader(possibly 0, which we shall explain later)
- **Follower** 
  - Follower servers sync up their copy of data with that of the leader’s after every regular time intervals. When the leader server goes down(due to any reason), one of the followers can contest an election and become the leader.
- **Candidate** 
  - At the time of contesting an election to choose the leader server, the servers can ask other servers for votes. Hence, they are called candidates when they have requested votes.

## Current Features

- **Leader Election**
  - **In order to maintain authority as a Leader of the cluster:** 
    - The Leader node sends heartbeat to express dominion to other Follower nodes. 
    - A leader election takes place when a Follower node times out while waiting for a heartbeat from the Leader node. 
    - At this point of time, the timed out node changes it state to Candidate state, votes for itself and issues RequestVotes RPC to establish majority and attempt to become the Leader.
  -  **The election can go the following three ways:** 
     -  The Candidate node becomes the Leader by receiving the majority of votes from the cluster nodes. 
        -  At this point of time, it updates its status to Leader and starts sending heartbeats to notify other servers of the new Leader.
     -  The Candidate node fails to receive the majority of votes in the election and hence the term ends with no Leader. 
        -  The Candidate node returns to the Follower state.
     -  If the term number of the Candidate node requesting the votes is less than other Candidate nodes in the cluster, the AppendEntries RPC is rejected and other nodes retain their Candidate status. 
        -  If the term number is greater, the Candidate node is elected as the new Leader.

## Future Work

- **Log Replication**
  - In case the leader crashes,
    - The leader handles inconsistencies by forcing the followers’ logs to duplicate its own. This means that conflicting entries in follower logs will be overwritten with entries from the leader’s log. 
- **Safety**
  - **Log Matching safety** 
    - If multiple logs have an entry with the same index and term, then those logs are guaranteed to be identical in all entries up through to the given index.
  - **Leader completeness** 
    - The log entries committed in a given term will always appear in the logs of the leaders following the said term
  - **State Machine safety** 
    - If a server has applied a particular log entry to its state machine, then no other server in the server cluster can apply a different command for the same log.
  - **Leader is Append-only** 
    - A leader node(server) can only append(no other operations like overwrite, delete, update are permitted) new commands to its log
  - **Follower node crash** 
    - When the follower node crashes, all the requests sent to the crashed node are ignored. Further, the crashed node can’t take part in the leader election for obvious reasons. When the node restarts, it syncs up its log with the leader node


## Technologies used
```
Protobuf
gRPC
Java 11
```

## Preparing

Please read/lookup information on Protobuf and gRPC. While these are 
separate technologies, they are coupled in the .proto file for service
specification.

Question: What other technologies exist that is similar to Protobuf? 
How do they differ (be careful of marketing hype)? What are their 
strengths and weaknesses?

### Apple M1 (ARM) Issues

Note the protobuf (protoc) plugin for java v20 or older and the java-plugin (v1.49) 
do not currently support the new apple M1 chipset. 

These two are used to build the java source code files from protobuf (.proto) files.

Options:
  - Native (ARM, M1+) 
    - Protoc: Upgrade to version 3.21.x (works on m1)
    - Java plugin: **protoc-gen-java-plugin from github does not compile**.
        - There's an issue (7690) talking about Apple (ARM) M1 support 
        - If **you don't want to install Rosetta**
          - Generate the source files on an intel based computer and copy 
            them to your Mac. Use generated.tar.gz to get you started 
            (created java files from an intel computer)
  - Rosetta
    - If you have installed Rosetta then you can download the x86-64 exe from
      https://repo1.maven.org/maven2/io/grpc/protoc-gen-grpc-java/1.49.0/
      
## Building

Building is more complex as we are now dependent upon a set of new libraries (jars), and build dependencies. 

  - Install gRPC, protobuf, and the java-plugin onto your computer. 
    Apple M1 users see notes above.
  - Configure lmod files (.lua) as needed
  - The project has two internal library directories (lib, lib-ref2).
  - Code generation (build_pb.sh) is required

**Note: Simply importing the project into your IDE will not work.**


```sh
./build_pb.sh
```
Please adjust the file above before building to work on your specific machine.

## Running

- The execution scripts add additional functionality.
- Take a few minutes to look over the .sh scripts
  
Running single client
```sh
./runClient.sh
```

Running single server [*using serverA.conf as example*]
```sh
./runServer.sh conf/serverA.conf
```

Running all 4 servers at once
```sh
./runAllServers.sh
```
- Need to have gnome-terminal for above to work:
```sh
sudo apt install gnome-terminal 
```

## Foundation Work (TODOs)

After exploring the new technologies used by this lab, we begin
the work to build a strong foundation for all future class work on distributed systems.

  - ~~Be sure to understand the request-response nature of the client server.~~
  - Replace the printf calls to use slf4j.

**Question(s)**
  - What are the positive and negative aspects of this code **(blocking calls)**?
  - What are your timing numbers (how long does it take to send messages?)

