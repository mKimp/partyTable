package com.company;
import java.io.FileReader;
import java.util.*;
import java.io.BufferedReader;
import java.util.ArrayList;

public class partyTable {

    public static void main(String[] args) throws Exception {
        // write your code here
        // read score table
        FileReader file = new FileReader (args[0]);
        BufferedReader sc = new BufferedReader(file);

        int n = Integer.parseInt(sc.readLine());
        int row = 2; int col = n/2;
        int [][] scoreTable = new int [n][n];
        int [][] table = new int[2][n];
        String line = sc.readLine();

        for (int i = 0; i < scoreTable.length;++i){
            String [] temp = line.split(" ");
            for (int j = 0; j < temp.length; ++j){
                scoreTable[i][j] = Integer.parseInt(temp[j]);
            }
            line = sc.readLine();
        }


        //create table with random people on seats
        table = table_shuffle(n,row,col);

        //max score
        int max = max_score(table, n, col, row, scoreTable);

        // get all possible pairs
        bfs (table,scoreTable,row,col,n);
    }


    public static void bfs (int[][]table, int[][]scoreTable,int row, int col, int n){
        long start = System.currentTimeMillis();
        long end = start + 60000;
        long CURRENT = start;
        Map<int[][], Integer> result = new HashMap<>();
        int final_score = 0;

        // 60s
        while (CURRENT < end) {
            Map <List<Integer>,Integer> scoreMap = new HashMap<List<Integer>, Integer>();
            //find all adjacent pairs
            for (int i = 0; i < row; ++i) {
                for (int j = 0; j < col; ++j){
                    if (j < col-1){
                        int preferScoreAB = 0; int preferScoreBA = 0;
                        int A = table[i][j];
                        int B = table[i][j+1];
                        List<Integer> pair1 = new ArrayList<Integer>();
                        List<Integer> pair2 = new ArrayList<Integer>();
                        preferScoreAB = h (A,B,scoreTable,n);
                        preferScoreBA = h (B,A,scoreTable,n);
                        pair1.add(A);
                        pair1.add(B);
                        scoreMap.put(pair1, preferScoreAB);
                        pair2.add(B);
                        pair2.add(A);
                        scoreMap.put(pair2,preferScoreBA);
                    }
                }
            }

            // find all across pairs
            for (int i = 0; i < col; ++i) {
                for (int j = 0; j < row; ++j){
                    if (j +1 < row ) {
                        int preferScoreAB = 0; int preferScoreBA = 0;
                        int A = table[j][i];
                        int B = table[j+1][i];
                        preferScoreAB = h (A,B,scoreTable,n);
                        preferScoreBA = h (B,A,scoreTable,n);
                        List<Integer> pair1 = new ArrayList<Integer>();
                        List<Integer> pair2 = new ArrayList<Integer>();
                        preferScoreAB = h (A,B,scoreTable,n);
                        preferScoreBA = h (B,A,scoreTable,n);
                        pair1.add(A);
                        pair1.add(B);
                        scoreMap.put(pair1, preferScoreAB);
                        pair2.add(B);
                        pair2.add(A);
                        scoreMap.put(pair2,preferScoreBA);
                    }
                }
            }

            // find the couple has worst score
            List<Integer> bad_couple = new ArrayList<Integer>();
            bad_couple = greedy(table,scoreTable,row,col,n,scoreMap);

            // return the pair has worst score
            int first = bad_couple.get(0); int second = bad_couple.get(1);
            int maxFirst = Integer.MIN_VALUE;
            int maxSecond = Integer.MIN_VALUE;
            int personSwitchF = 0; int personSwitchS = 0;

            // Initialize the first maxnimize point according to the row
            if (first == n){
                maxFirst = scoreTable[n-1][0];
            }
            else if (second == n){
                maxSecond = scoreTable[n-1][0];
            }
            else{
                maxFirst = scoreTable[first][0];
                maxSecond = scoreTable[second][0];
            }

            // Get the maximize point in each person in the worst pair, and return back the index based on highest score
            for (int i = 0; i < n; ++i){
                if (i < n-1){
                    // first person in the worst pair
                    if (i+1 == first) {
                        for (int j = 0; j < n; ++j){
                            if(j+1 != n){
                                int point = scoreTable[i][j];
                                if (point > maxFirst){
                                    maxFirst = point;
                                    personSwitchF = j+1;
                                }
                            }
                        }
                    }
                    //second
                    else if (i+1 == second){
                        for (int j = 0; j < n; ++j){
                            if(j+1 != n) {
                                int point = scoreTable[i][j];
                                if (point > maxSecond){
                                    maxSecond = point;
                                    personSwitchS = j+1;
                                }
                            }
                        }
                    }
                }
                else if (i == (n-1)){
                    if (i+1 == first){
                        for (int j = 0; j < n; ++ j)
                        {
                            int point = scoreTable[i][j];
                            if (point > maxFirst){
                                maxFirst = scoreTable[i][j];
                                if (j+1 != n){
                                    personSwitchF = j+1;
                                }
                                else{
                                    personSwitchF = j;
                                }
                            }
                        }
                    }
                    // second person in the worst pair
                    else if (i+1 == second){
                        for (int j = 0; j < n; ++ j)
                        {
                            int point = scoreTable[i][j];
                            if (point > maxSecond){
                                maxSecond = scoreTable[i][j];
                                if (j+1 != n){
                                    personSwitchS = j+1;
                                }
                                else{
                                    personSwitchS = j;
                                }
                            }
                        }
                    }
                }
            }
            // switching people
            int [][] tempTableA = copy (table,row,col);
            int [][] tempTableB = copy (table,row,col);
            int scoreFirstPair = 0; int scoreSecondPair = 0;
            // original score of the random table
            int original_score = max_score(table, n, col, row, scoreTable);
            int fix_original_score = original_score;
            //the person 1 in the worst pair has a highest score in the column
            if (maxFirst > maxSecond){
                int [] check1 = person_position(table,row,col,n, personSwitchF); //return back the index of person the person 1 want to sit next the most
                for (int i = 0; i < row; ++i) {
                    for (int j = 0; j < col;++j) {
                        if(table[i][j] == first && i == 0){
                            if  ((j + 1 != col) && (i +1 < row)){
                                //place the person sit ajacent to person 1
                                int tempA = tempTableA[i][j+1];
                                tempTableA[i][j+1] = tempTableA[check1[0]][check1[1]];
                                tempTableA[check1[0]][check1[1]] = tempA;
                                scoreFirstPair = max_score(tempTableA, n, col, row, scoreTable);
                                //place the person sit cross to person 1
                                int tempB = tempTableB[i+1][j];
                                tempTableB[i+1][j] = tempTableB[check1[0]][check1[1]];
                                tempTableB[check1[0]][check1[1]] = tempB;
                                scoreSecondPair = max_score(tempTableB, n, col, row, scoreTable);
                                // if overall score of adjacent is larger than the across
                                if (scoreFirstPair > scoreSecondPair){
                                    // make the person move to adjacent to person 1 if the overscore > orignal score
                                    if (scoreFirstPair > original_score){
                                        original_score = scoreFirstPair;
                                        //copy the temp table to orignial table
                                        for (int k = 0; k < tempTableA.length;++k) {
                                            System.arraycopy(tempTableA[k],0,table[k],0,tempTableA[k].length);
                                        }
                                        // for print out the result, keep the higest score and table in the hashmap
                                        int [][]hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                                // if overall score of across is larger than adjacent
                                else if (scoreFirstPair < scoreSecondPair){
                                    // if score of across > orgianal score
                                    if (scoreSecondPair > original_score){
                                        original_score = scoreSecondPair;
                                        //copy temp to original table
                                        for (int m = 0; m < tempTableB.length;++m) {
                                            System.arraycopy(tempTableB[m],0,table[m],0,tempTableB[m].length);
                                        }
                                        // for print out the result, keep the higest score and table in the hashmap
                                        int [][]hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                            }
                            else {
                                // in case person is sitting in the last seat of row
                                if (i+1 < row){
                                    int tempB = tempTableB[i+1][j];
                                    tempTableB[i+1][j] = tempTableB[check1[0]][check1[1]];
                                    tempTableB[check1[0]][check1[1]] = tempB;
                                    scoreSecondPair = max_score(tempTableB, n, col, row, scoreTable);
                                    if (scoreSecondPair > original_score){
                                        original_score = scoreSecondPair;
                                        //copy temp to orignal table
                                        for (int m = 0; m < tempTableB.length;++m) {
                                            System.arraycopy(tempTableB[m],0,table[m],0,tempTableB[m].length);
                                        }
                                        //for print out the result, keep the higest score and table in the hashmap
                                        int [][] hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                            }

                        }
                        // if the person sitting in the second row, move above or right
                        else if (table[i][j] == first && i == 1) {
                            // adjacent pair
                            if ((j+1!=col) && (i-1 > 0)){
                                tempTableA[i][j+1] = tempTableA[check1[0]][check1[1]];
                                tempTableA[check1[0]][check1[1]] = tempTableA[i][j+1];
                                scoreFirstPair = max_score(tempTableA, n, col, row, scoreTable);
                                // across pair
                                int tempB = tempTableB[i-1][j];
                                tempTableB[i-1][j] = tempTableB[check1[0]][check1[1]];
                                tempTableB[check1[0]][check1[1]]=tempB;
                                scoreSecondPair = max_score(tempTableB, n, col, row, scoreTable);
                                //compare
                                if (scoreFirstPair > scoreSecondPair){
                                    if (scoreFirstPair > original_score){
                                        original_score = scoreFirstPair;
                                        //copy to original table
                                        for (int k = 0; k < tempTableA.length;++k) {
                                            System.arraycopy(tempTableA[k],0,table[k],0,tempTableA[k].length);
                                        }
                                        // for print out the result, keep the higest score and table in the hashmap
                                        int [][]hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                                //compare across > adjacent
                                else if (scoreFirstPair < scoreSecondPair){
                                    if (scoreSecondPair > original_score){
                                        original_score = scoreSecondPair;
                                        //copy
                                        for (int m = 0; m < tempTableB.length;++m) {
                                            System.arraycopy(tempTableB[m],0,table[m],0,tempTableB[m].length);
                                        }
                                        // for print out the result, keep the higest score and table in the hashmap
                                        int[][] hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                            }
                            // case the person sitting in the last seat in second row
                            else {
                                if (i-1 ==0){
                                    int tempB = tempTableB[i-1][j];
                                    tempTableB[i-1][j] = tempTableB[check1[0]][check1[1]];
                                    tempTableB[check1[0]][check1[1]]=tempB;
                                    scoreSecondPair = max_score(tempTableB, n, col, row, scoreTable);
                                    if (scoreSecondPair > original_score){
                                        original_score = scoreSecondPair;
                                        for (int m = 0; m < tempTableB.length;++m) {
                                            System.arraycopy(tempTableB[m],0,table[m],0,tempTableB[m].length);
                                        }
                                        int[][] hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // The person 2 in the worst pair has the highest score in the column than person 1
            else if (maxFirst < maxSecond){
                int [] check1 = person_position(table,row,col,n, personSwitchS);
                for (int i = 0; i < row; ++i) {
                    for (int j = 0; j < col;++j) {
                        if(table[i][j] == second && i == 0){
                            if  ((j + 1 != col) && (i +1 < row)){
                                //adjacent pair
                                int tempA = tempTableA[i][j+1];
                                tempTableA[i][j+1] = tempTableA[check1[0]][check1[1]];
                                tempTableA[check1[0]][check1[1]] = tempA;
                                scoreFirstPair = max_score(tempTableA, n, col, row, scoreTable);
                                //aross pair
                                int tempB = tempTableB[i+1][j];
                                tempTableB[i+1][j] = tempTableB[check1[0]][check1[1]];
                                tempTableB[check1[0]][check1[1]] = tempB;
                                scoreSecondPair = max_score(tempTableB, n, col, row, scoreTable);
                                //adjacent > across score
                                if (scoreFirstPair > scoreSecondPair){
                                    if (scoreFirstPair > original_score){
                                        original_score = scoreFirstPair;
                                        //copy
                                        for (int k = 0; k < tempTableA.length;++k) {
                                            System.arraycopy(tempTableA[k],0,table[k],0,tempTableA[k].length);
                                        }
                                        // for print out the result, keep the higest score and table in the hashmap
                                        int [][]hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);

                                    }
                                }
                                // across > adjacent
                                else if (scoreFirstPair < scoreSecondPair){
                                    if (scoreSecondPair > original_score){
                                        original_score = scoreSecondPair;

                                        for (int m = 0; m < tempTableB.length;++m) {
                                            System.arraycopy(tempTableB[m],0,table[m],0,tempTableB[m].length);
                                        }
                                        // for print out the result, keep the higest score and table in the hashmap
                                        int [][]hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                            }
                            // in case person sitting in the last seat of the row
                            else {
                                if (i+1 < row){
                                    int tempB = tempTableB[i+1][j];
                                    tempTableB[i+1][j] = tempTableB[check1[0]][check1[1]];
                                    tempTableB[check1[0]][check1[1]] = tempB;
                                    scoreSecondPair = max_score(tempTableB, n, col, row, scoreTable);
                                    if (scoreSecondPair > original_score){
                                        original_score = scoreSecondPair;
                                        for (int m = 0; m < tempTableB.length;++m) {
                                            System.arraycopy(tempTableB[m],0,table[m],0,tempTableB[m].length);
                                        }
                                        int [][]hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                            }
                        }
                        // person 1 sitting in the second row, move right, or above
                        else if (table[i][j] == first && i == 1) {
                            // adjacent pair
                            if ((j+1!=col) && (i-1 > 0)){
                                tempTableA[i][j+1] = tempTableA[check1[0]][check1[1]];
                                tempTableA[check1[0]][check1[1]] = tempTableA[i][j+1];
                                scoreFirstPair = max_score(tempTableA, n, col, row, scoreTable);
                                // across pair
                                int tempB = tempTableB[i-1][j];
                                tempTableB[i-1][j] = tempTableB[check1[0]][check1[1]];
                                tempTableB[check1[0]][check1[1]]=tempB;
                                scoreSecondPair = max_score(tempTableB, n, col, row, scoreTable);
                                //compare
                                if (scoreFirstPair > scoreSecondPair){
                                    if (scoreFirstPair > original_score){
                                        original_score = scoreFirstPair;
                                        for (int k = 0; k < tempTableA.length;++k) {
                                            System.arraycopy(tempTableA[k],0,table[k],0,tempTableA[k].length);
                                        }
                                        int [][]hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                                else if (scoreFirstPair < scoreSecondPair){
                                    if (scoreSecondPair > original_score){
                                        original_score = scoreSecondPair;
                                        for (int m = 0; m < tempTableB.length;++m) {
                                            System.arraycopy(tempTableB[m],0,table[m],0,tempTableB[m].length);
                                        }
                                        int [][]hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                            }
                            else {
                                if (i-1 ==0){
                                    int tempB = tempTableB[i-1][j];
                                    tempTableB[i-1][j] = tempTableB[check1[0]][check1[1]];
                                    tempTableB[check1[0]][check1[1]]=tempB;
                                    scoreSecondPair = max_score(tempTableB, n, col, row, scoreTable);
                                    if (scoreSecondPair > original_score){
                                        original_score = scoreSecondPair;

                                        for (int m = 0; m < tempTableB.length;++m) {
                                            System.arraycopy(tempTableB[m],0,table[m],0,tempTableB[m].length);
                                        }
                                        int [][]hash = new int[row][col];
                                        hash = copy(table, row, col);
                                        result.put(hash,original_score);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // randome table if changing the worst pair not improving the overall score
            if (original_score <= fix_original_score){
                int [][] newTable = new int[row][col];
                newTable = table_shuffle(n,row,col);
                for (int m = 0; m < newTable.length;++m) {
                    System.arraycopy(newTable[m],0,table[m],0,newTable[m].length);
                }
            }
            // update the current time
            CURRENT = System.currentTimeMillis();
        }
        // Print our the result, get the best score in the map
        int maxx = Integer.MIN_VALUE;
        int f [] [] = new int [row][col];
        for(Map.Entry<int[][],Integer>entry : result.entrySet()){
            if (entry.getValue() > maxx){
                maxx = entry.getValue();
            }
        }
        System.out.println("Better score: " + maxx);
        for(Map.Entry<int[][],Integer>entry : result.entrySet()){
            if (entry.getValue().equals(maxx)){
                f = entry.getKey();
            }
        }
        // Final table
        System.out.println("Table");
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                System.out.print(f[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int [] person_position (int[][]table,int row, int col, int n, int person) {
        int [] position = new int [2];

        for (int i = 0; i<row; ++i){
            for (int j = 0; j < col; ++j){
                if (table[i][j] == person){
                    position[0] = i;
                    position[1] = j;
                }
            }
        }
        return position;
    }

    public static List<Integer> greedy (int[][]table, int[][]scoreTable,int row, int col, int n, Map <List<Integer>,Integer> scoreMap) {
        int min = Integer.MAX_VALUE;
        List<List<Integer>> smallest = new ArrayList<List<Integer>>();
        List <Integer> list= new ArrayList<Integer>();

        for (Map.Entry<List<Integer>,Integer> entry : scoreMap.entrySet()){
            int score = entry.getValue();
            if (score < min){
                min = score;
                list.clear();
                list = entry.getKey();

            }
        }
        return list;
    }
    public static int [][]copy(int[][]src,int row, int col){
        int des[][] = new int [row][col];
        for (int i = 0; i < src.length;++i) {
            System.arraycopy(src[i],0,des[i],0,src[i].length);
        }
        return des;
    }
    // get the score of table passed in
    public static int max_score(int [][]table, int n, int col, int row, int [] []scoreTable){

        // adjacent score
        int adjScore = adjacent_score(table, n, col, row, scoreTable);
        // across score
        int acroScore = across_score(table, n, col, row, scoreTable);
        // Maxscore of tabke
        int max = adjScore + acroScore;
        return max;

    }
    //randome table
    public static int [][] table_shuffle (int n, int row, int col) {
        int [][] table = new int [row][col];
        Set <Integer> visited = new HashSet<Integer>();
        Random rand = new Random();

        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                int guest = rand.nextInt(n)+1;
                while (visited.contains(guest)){
                    guest = rand.nextInt(n)+1;
                }
                visited.add(guest);
                table[i][j] = guest;
            }
        }
        return table;
    }
    // get the score of adjacent pairs and their preferred score
    public static int adjacent_score (int[][]table,int n, int col, int row, int[][]scoreTable) {
        int total_score = 0; int preferScoreAB = 0; int preferScoreBA = 0; int check = 0;

        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j){
                if (j != col-1){
                    int A = table[i][j];
                    int B = table[i][j+1];
                    preferScoreAB = h (A,B,scoreTable,n);
                    preferScoreBA = h (B,A,scoreTable,n);
                    check += preferScoreAB + preferScoreBA ;
                    if ((A <= n/2 && (B > n/2 && B <= n)) || ((A > n/2 && A <=n) && B <= n/2)){
                        total_score += 1;
                    }
                }
            }
        }
        return total_score + check;
    }
    // get the score of across pairs and their preferred score
    public static int across_score (int[][]table,int n, int col, int row, int[][]scoreTable) {
        int total_score = 0; int preferScoreAB = 0; int preferScoreBA = 0; int check = 0;

        for (int i = 0; i < col; ++i) {
            for (int j = 0; j < row; ++j){
                if (j +1 < row ) {
                    int A = table[j][i];
                    int B = table[j+1][i];
                    preferScoreAB = h (A,B,scoreTable,n);
                    preferScoreBA = h (B,A,scoreTable,n);
                    check += preferScoreAB + preferScoreBA ;
                    if ((A <= n/2 && (B > n/2 && B <= n)) || ((A > n/2 && A <=n) && B <= n/2)){
                        total_score += 2;
                    }
                }
            }
        }
        return total_score + check;
    }
    // prefer score function
    public static int h (int p1, int p2, int [][] scoreTable, int n) {
        int score = 0;

        for (int i = 0; i < n; ++i){
            if (i+1 == p1) {
                for (int j = 0; j < n; ++j){
                    if (j+1 == p2) {
                        score = scoreTable[i][j];
                    }
                }

            }
        }
        return score;
    }
}

