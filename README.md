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
The main idea here is looking at all the possible pairs in the table (adjacent and across). So we use the bfs search to explore all the neighbors. The stopping condition is 60s. Then instead of looking for the pair has the best score, we are looking for the pair that has the worst score. We use the greedy method to locally 
optimal choice on the worst pair by following the heuristic function. The heuristic function is improving the score of the overall score of the table by optimizing the worst pair. We move each person in the worst pair to the person they like to sit next. We could either move to the right or below if the person is sitting on the first row. If the person sit in the second, they can be moved either right or above. If the person sits in the last seat in either row, they move across. If the moving improves the overall score, we make that move officially and change the setting of the orignal table. If not, we random the table again, in hope finding the worst pair that can improve the overall score.  
 
Since we don’t know the depth of the breadth-first search, it is in unlimited bound, which is not a complete search. Here we use the 60s as a stopping condition. The overall score is maybe not optimized as other algorithms, but it optimized locally every worst pair that the search finds. Therefore, no person has to be seat to the person she or he hated the most. In terms of complexity, we don’t need to keep track of the visited person or pair in memory, or pointers to point back to the unvisited people, so we save some memory. I used the hash table to store the pairs and their score, and its space complexity is O(n).  However, the time complexity is a trade-off. It does a lot of comparisons, and the greedy method has to look at all possible pairs to get the worst ones, so its time complexity is O(n^2). 
 
 
