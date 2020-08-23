# CalendarApp

## About
CalendarApp was developed as part of the University of Toronto course:  "Software Design (CS207)". The Team consistem of myself and 6 other team members: 
- Kai Zhang
- Lotus Zie
- Raymond Liu
- Roland G
- Xiaoning Wang
- Zach Folan

The intent of the project was to design a Java application to specifications ([Phase 1](/Specifications%20Phase%201.txt), [Phase 2](/Specifications%20Phase%202.txt)) while demonstrating good software design practices such as: implementation of relevant design patterns, managing modularity/dependancy/coupling and using clean Architecture layering. 

The specifications included a list of optional and required features with a a minimum number of optional features that must be implemented ([Full Project Feature List](CalendarApp/Feature%20List.md)). In a nutshell, the application is a console calendar application which allows for schedualing events, alerts, memos, and series' as well as the sharing of that content and messaging other calendar users. 

Since this project was intended primarily as an exercies in software design practices, we kept a recored of our design decisions and alternative which which can be found [here](CalendarApp/Design%20Decisions.md)

## Documentation
Full Javadoc documentation can be found [here](CalendarApp/JavaDoc/index.html).

## My Contributions
1. A loader class that saves and loads serialized objects to specified files
2. The User Action and SubMenu super classes that prevent code redundancy for the various submenues.
3. The MessageUI, AlertUI, AccountUI, and MemoUI classes.
4. The messaging System (including all classes int the Messaging package and the messaging class in the App package) that allows users to send messages and share calendars and Events with each other. m
5. The Memo Package that allows creation of memos and association of a memo to event (one-to-many relation respectively).
6. The timing functionality for triggering alerts including the AlertTask nested class within CallendarApp. 
7. Generated [UML Diagrams](CalendarApp/UML) using Intellij.

I am particularly proud of removing cyclic dependencies from the messaging and packages using the methods described in chapter 14 of the "Clean Architecture" by Robert Cecil Martin. I am also proud of the Submenu heirarchy as I think it greatly simplifies the code as opposed to how it would be without it. Lastly I really enjoyed using the observer/observable design pattern for the timming functionality of the program.

Looking back I wish our group had implemented some kind of inheritence structure or system of interfaces for the base entities and their manager classes as I think we could have reduced the code size, redundancy, and complexity there.
