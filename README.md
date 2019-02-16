# Interface between Erlang and Java

This project will become an interface between Erlang and Java code, in Java.

The ultimate goal is to enable easy communication between Erlang processes
and Akka actors, but that's a long way ahead.


## Why this project, why not just use JInterface?

I'm glad you asked! The JInterface seems to be mostly blocking code,
where as this code aims for being fully asynchronous and non-blocking.

Also, because it's interesting.


## Should I use this in production?

No. Not yet at least.
