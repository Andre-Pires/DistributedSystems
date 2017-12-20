# DistributedSystems

Project developed for IST, Sistemas Distribuídos course

Project consists in creating a Web Service in JAVA that manages the emission of invoices from a restaurant, using UDDI and JAX-WS. 
The project relies on Fénix Framework by IST, for the storage of information on a MySQL database.

Created by: Alexandre Pimentel, Filipe Bento and myself (André Pires).

Note: The project should be linked with the SoftwareEngineering project to control the invoices emmitted by restaurants registered in the management portal.

## Quick overview
This project consists of a linked list of replication managers, where the primary is the head of the list and
the children are inserted at the end of the list in the order they are started. The insertion is done through
sending a SOAP message to the endpoint of the primary (discovered with a UDDI lookup), which
asks you which endpoint you are connected to. When you get the next
list, this secondary is asked again which next, and so on, until it reaches the
end of list.
When the last child receives the SOAP message that asks for the next endpoint, it verifies that
does not point to anyone and starts to point to the starting secondary. Doing so launches a thread
which starts sending SOAP messages "I'm Alive" to it, and sends you a SOAP message to the
notify you that it is now the last element. The secondary that starts, upon receiving this message, launches
a thread that waits for "I'm Alive" SOAP messages with a defined timeout.
With this model, each replica receives "I'm Alives" from the replica immediately above and
sends "I'm Alives" to the replica below, with the exception of the Primary, which only sends, and
of the last secondary, which only receives.
The filter of these messages (of type "GetNextAdress" or type "I'm Alive") is done using SOAP
Handlers. The handler identifies the type of message to enter and specifies the correct behavior
depending on the message. For messages of type "GetNextAdress" you should return your nextEndpoint to
replica who sent the message; for messages of type "I'm Alive" should interrupt the thread
receiver to update the timeout window. These messages are empty envelopes with a
attribute in the header that identifies them correctly.
Since the protocol is implemented exclusively with SOAP Handlers, the process of replication and
Fault recovery is completely transparent to the client running the code. He can not
distinguish the machines with whom it communicates and the delay of the recovery is low (of approximately,
between 1 and 2 seconds - assuming normal network traffic). However, it is independent of the
in which it is used: it is possible to use the protocol with any service that communicates with a
WebService without it having to change its implementation.

## Fault Tolerance
The protocol defines a maximum reception timeout window of "I'm Alives" of 2000 ms, after which the
Secondary that receives them assumes that the replica that points to itself (which sends it "I'm alives") failed
silently and tries to restart the insertion process in order to update the list. In this way it is
necessary to update only the "link" that has broken, without any of the other replicas being affected
or do any kind of processing. The sending interval of "I'm alives" is 500 ms.
The values set for sending and for timeout are restrictive, assuming an actual application of the
protocol.
This mechanism allows failures to be quickly discovered and
with a minimal impact on customer service performance (according to the
interaction).

If the primary fails, the replica below itself (and only this) stops receiving "I'm Alives" and tries to
re-enter in the list. When you contact the primary registered in UDDI, you realize that it has failed and, as such,
registers itself as primary in UDDI (rebind). If the customer contacts the primary and the primary has not yet
been replaced, requests are resubmitted (within a period of time) until a new replica
(who is in the 2nd position) registers as primary. In this way, potential faults are tolerated
the primary server.
If there are multiple silent faults simultaneously, the system can always
each replica is inserted at the end of the list, which allows the communication between the replicas to be always
restored. If this did not happen, it would hinder the replication of messages and, consequently,
consistency.
This mechanism was tested by launching multiple replicas (2, 3, 4 and 5) and in each case, verifying the
recovery of all when one or more replicas, including the primary, fail silently. Was
the recovery process (exchange of messages between the replicas of the list) and the confirmation of the
that all replicas insert themselves as they should and where they should, and that the posting and termination of threads
sending and receiving I'm alives is started and ended as described above. It should be noted that the tests
were thorough and exhaustive regarding the recovery / replication process.

## Replication
When the primary receives a request from the client, and before it is executed and the response returned, the
request is propagated by the replica list (via the Handler), the request being executed in all
replicates, and thus ensuring sequential consistency in each of them, since these are
always executed in the same order as they were communicated by the client to the primary.
To ensure sequential consistency in full, the protocol also applies a
one-time order semantics.
The one-time semantics is implemented by entering a sequence number in the messages
that are sent by the client and causing each replica to discard messages with a number of
sequence of the last received message, and responding with the sent message
previously. In this way, there is no duplication of messages, ensuring that a message is
performed only once. Thus, if the client communicates a message to the primary and it dies after
have replicated the message, and before returning the response, the customer's request is again sent
and the secondary replacing the primary detects the repeated request and returns the last message of
response, ensuring the correct response to the customer.
This mechanism was tested by throwing multiple replicas and forcing the primer to fail silently after
have replicated the customer's message and before returning, in order to ensure the situation described. O
protocol performs the described behavior: the secondary server takes the place of the primary
messages with the id of the messages already passed to the primary server, returns the
reply message, without executing the code of that request. We have confirmed that the customer
receives the correct response to requests that the primary failed to respond.
Messages received by a replica that failed silently are not replicated, as this
would force you to restart the failed replica and update its status and place it consistent with the
other replicas, which would force an implementation with a fairly high level of complexity.
This is the only case where the protocol does not guarantee correct consistency or a return of
answers to the customer.
