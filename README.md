# THIS IS A PROJECT FROM ARITIFICAL INTELLIGENCE FROM PROFESSOR BART MASSEY AT PORTLAND STATE UNIVERSITY. IF YOU ARE TAKING THIS COURSE, PLEASE USE THIS CODE AS A REFERENCE. DON'T JUST COPY AND FAIL THE CLASS. 

# PartyTable
Suppose you are given a set of n people (with n even) to be seated at a dinner party. The people will be seated along the two sides of a long table
     
Half are "hosts", half are "guests". The given function r(p) identifies the role of a given person.
As the host, you also know an integer-valued "preference function" h(p1, p2) for a pair of people p1, p2. The preference function indicates how much the first person likes the second; it may be negative.

The "score" of a table is determined by the following criteria:

1 point for every adjacent pair (seated next to each other) of people with one a host and the other a guest.
2 points for every opposite pair (seated across from each other) of people with one a host and the other a guest.
h(p1, p2) + h(p2, p1) points for every adjacent or opposite pair of people p1, p2.
Your job as event organizer is to write a search that will find a "good" table score for a given set of people and preference function.

The data is given to you in the form of an ASCII text file that has the even number n of people on the first line. The first n/2 people are assumed to be hosts, the rest guests. The preference matrix follows on the remaining lines: rows with values separated by spaces. The people are assumed to be numbered 1..n. The seats are assumed to be numbered such that the top half of the table has seats 1..n/2 left-to-right, and the bottom half of the table has seats n/2+1..n left-to-right; thus seat n/2 is opposite seat n.

## Platform: Linux  
## Language: Java 
## Execute: 
  ● Compile: javac partyTable.java 
  ● Run: ​java partyTable “text file”.  For example: java​ partyTable hw1-inst1.txt 
  ● When you run the program, nothing will show up in 60s. Then table and score will pop up in the stdout after 60s. 

## Searching strategy​: Breadth-first search mix with greedy method and heuristic function
