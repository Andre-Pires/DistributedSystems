This is a simple Java Web Service

The service is defined by the WSDL file that generates Java code
(contract-first approach, also called top-down approach).

The service runs in a standalone HTTP server.


Using Ant:
---------

Build steps are specified in build.xml file

To list available targets:
    ant -p

To compile server classes:
    ant wsimport

To run:
    ant run


Testing:
-------

When running, the web service awaits connections from clients.
You can check if the service is running using your web browser:

    http://localhost:8080/hello-ws/endpoint

And see the generated WSDL file:

    http://localhost:8080/hello-ws/endpoint?WSDL

This address is defined in HelloMain when the publish() method is called.

To call the service you will need a web service client,
including code generated from the WSDL.


To configure the project in Eclipse:
-----------------------------------

If Eclipse files (.project, .classpath) exist:
    'File', 'Import...', 'General'-'Existing Projects into Workspace'
    'Select root directory' and 'Browse' to the project base folder.
    Check if everything is OK and 'Finish'.

If Eclipse files do not exist:
    Create a 'New Project', 'Java Project'.
    Uncheck 'Use default location' and 'Browse' to the project base folder.
    Fill in the 'Project name'.

    Configure source and output folders.

    Add the required libraries to the project build path.
    Project, Build Path, Libraries, Server runtime

    Click 'Next' to check if all configurations are correct and 'Finish'.

To run:
    To run the application, click 'Run' (the green play button).
    Select 'Java Application'.


--
2013-03-07
Miguel.Pardal@tecnico.ulisboa.pt
