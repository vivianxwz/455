// Name:Xiaowen Zhang
// USC NetID:zhan204
// CSCI455 PA2
// Spring 2019

import java.util.*;

/*
  class SolitaireBoard
  The board for Bulgarian Solitaire.  You can change what the total number of cards is for the game
  by changing NUM_FINAL_PILES, below.  Don't change CARD_TOTAL directly, because there are only some values
  for CARD_TOTAL that result in a game that terminates.
  (See comments below next to named constant declarations for more details on this.)
*/


public class SolitaireBoard {
   
    public static final int NUM_FINAL_PILES = 9;
    // number of piles in a final configuration
    // (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)
    
    public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
    // bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
    // see: http://en.wikipedia.org/wiki/Bulgarian_solitaire for more details
    // the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES

    // Note to students: you may not use an ArrayList -- see assgt description for details.
    
    private static final int RANDOM_OFFSET = 1;
    //Since the nextInt method in Random class starts from 0, we need to use the offset to make it starts from 1.
    
    /**
        Representation invariant:
        1. the max. number of inputs must be less than or equal to CARD_TOTAL
		2. each input should be larger than 0
		3. each input should be less than or equal to CARD_TOTAL
		4. the sum of all inputs should be equal to CARD_TOTAL
		5. elements from 0 to numOfPiles-1 in pilesArray should be greater than 0
		6. elements since numOfPiles-1 in pilesArray should be zero
    */
    
    // <add instance variables here>
    //use the array to store every pile
    private int[] pilesArray;
    //used to generate the random cards of each pile
    private Random rand = new Random();
    //to store the number of piles of each array
    private int numOfPiles;
    
 
    /**
        Creates a solitaire board with the configuration specified in piles.
        piles has the number of cards in the first pile, then the number of cards in the second pile, etc.
        PRE: piles contains a sequence of positive numbers that sum to SolitaireBoard.CARD_TOTAL
    */
    public SolitaireBoard(ArrayList<Integer> piles) {
    	pilesArray = new int[CARD_TOTAL];
    	numOfPiles = piles.size();
    	for(int i = 0; i < piles.size(); i++) {
    		pilesArray[i] = piles.get(i);
    	}
    	for(int j = piles.size(); j < CARD_TOTAL; j++) {
    		pilesArray[j] = 0;
    	}
        assert isValidSolitaireBoard();   // sample assert statement (you will be adding more of these calls)
    }
 
   
    /**
        Creates a solitaire board with a random initial configuration.
    */
    public SolitaireBoard() {
    	int cardInPile;
    	int remains = CARD_TOTAL;
    	numOfPiles = 0;
    	pilesArray = new int[CARD_TOTAL];
    	
    	for(int i = 0; i < CARD_TOTAL; i++) {
    		//since nextInt has a range of [0,remains), we have to add 1 to ensure the number generated is not 0;
    		cardInPile = rand.nextInt(remains) + RANDOM_OFFSET;
    		remains = remains - cardInPile;
    		pilesArray[i] = cardInPile;
    		numOfPiles++;
    		if(remains == 0) {
    			break;
    		}
    	}
    	assert isValidSolitaireBoard();
    }
  
   
    /**
        Plays one round of Bulgarian solitaire.  Updates the configuration according to the rules
        of Bulgarian solitaire: Takes one card from each pile, and puts them all together in a new pile.
        The old piles that are left will be in the same relative order as before, 
        and the new pile will be at the end.
    */
    public void playRound() {
    	int countZeros = 0;
    	for(int i = 0; i < numOfPiles; i++) {
    		pilesArray[i] --;
    	}
    	//put the new pile to the end of array
    	pilesArray[numOfPiles] = numOfPiles;
    	numOfPiles++;
    	// use two pointers to remove zeros in the new pilesArray
    	int slow = 0;
    	int fast;
    	for(fast = 0; fast < numOfPiles; fast++) {
    		if(pilesArray[fast] != 0) {
                //the slow pointer only move one step when it is not 0
    			pilesArray[slow++] = pilesArray[fast];
    		}else {
                //countZeros is used to update numOfPiles
    			countZeros++;
    		}
    	}
    	numOfPiles -= countZeros;
    	assert isValidSolitaireBoard();
    }
   
    /**
        Returns true iff the current board is at the end of the game.  That is, there are NUM_FINAL_PILES
        piles that are of sizes 1, 2, 3, . . . , NUM_FINAL_PILES, in any order.
    */
   
    public boolean isDone() {
    	boolean doneCheck = true;
    	//if the number of piles is not equal to NUM_FINAL_PILES, then the game must not be done.
    	if(numOfPiles != NUM_FINAL_PILES) {
    		doneCheck = false;
    		return doneCheck;
    	}
    	//since we can use extra space, use a new array to mark whether the number from 1 to NUM_FINAL_PILES appeared or not in pilesArray.
    	boolean[] visited = new boolean[NUM_FINAL_PILES];
    	for(int i = 0; i < NUM_FINAL_PILES; i++) {
    		if(pilesArray[i] <= NUM_FINAL_PILES) {
    			int temp = pilesArray[i] - 1;
        		visited[temp] = true;
    		}else {
    			doneCheck = false;
    			return doneCheck;
    		}
    	}
    	//check each position of the array whether each number from  to NUM_FINAL_PILES appeared or not.
    	for(int i = 0; i < NUM_FINAL_PILES; i++) {
    		if(visited[i] == false) {
    			doneCheck = false;
    			return doneCheck;
    		}
    	}
        return doneCheck;  // dummy code to get stub to compile
    }

   
    /**
        Returns current board configuration as a string with the format of
        a space-separated list of numbers with no leading or trailing spaces.
        The numbers represent the number of cards in each non-empty pile.
    */
    public String configString() {
    	String printString = "";
    	for(int i = 0; i < numOfPiles - 1; i++) {
    		printString += pilesArray[i] + " ";
    	}
    	printString += pilesArray[numOfPiles - 1];
        return printString;   //return a string of number with space to separate.
    }
   
   
    /**
        Returns true iff the solitaire board data is in a valid state
        (See representation invariant comment for more details.)
    */
    private boolean isValidSolitaireBoard() {
    	//First, check the size of input is valid or not
    	boolean validFlag = true;
    	if( numOfPiles <= 0 || numOfPiles > CARD_TOTAL ) {
    		return false;
    	}
    	int num = 0;
    	int pilesSum = 0;
    	for (int i = 0; i < numOfPiles; i++) {
    		//Then, check each piles is positive number.
    		if(pilesArray[i] <= 0 || pilesArray[i] > CARD_TOTAL) {
    			return false;
    		}
    		num = pilesArray[i];
    		pilesSum = pilesSum + num;
    	}
    	//Finally, check the sum of input is equal to the SolitaireBoard.CARD_TOTAL
    	if(pilesSum != CARD_TOTAL) {
    		return false;
    	}
    	return validFlag;
    }
   

    // <add any additional private methods here>


}
