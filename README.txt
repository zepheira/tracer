

                        +------------------------------------+
                        |             Tracer                 |
                        +------------------------------------+



  What is this?
  -------------

This is a Log4J appender that is capable of reacting to particular
formatting of the log messages and generate nice indented debug logs
that help very much during debugging.

  How do I use it?
  ----------------
  
Add Tracer as a dependency in your pom and then use the following lines 
as a template for your log4j.properties file

  log4j.rootLogger=error, tracer

  log4j.logger.[logger.name.here]=error

  log4j.appender.tracer=org.apache.log4j.ConsoleAppender
  log4j.appender.tracer.layout=edu.mit.simile.tracer.Tracer

  

                                  - o -
                                  

  Thanks for your interest in Tracer
  
  
                                                  Stefano Mazzocchi
                                                <stefanom at mit.edu>

