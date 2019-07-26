//author: vivianxwz @ github

import java.util.*;

/**
   *The following part is the main program.
   *This program is used to simulate the entire process of Solitaire Board.
   
   *There are 4 modes for user to choose. The program read from the keyboard to decide which mode the simulation
    result will be presented.
   *1.run -ea BulgarianSolitaireSimulator	
   *2.run -ea BulgarianSolitaireSimulator -u	
   *3.run -ea BulgarianSolitaireSimulator -s	
   *4.run -ea BulgarianSolitaireSimulator -u -s	
   
   *The main class will handle the mode.
   
   *The other private methods will the printed information showing to the user.
*/
public class BulgarianSolitaireSimulator {

    public static void main(String[] args) {
     
        boolean singleStep = false;
        boolean userConfig = false;
        
        //Read from the keyboard, to set the flag.
        for(int i = 0; i < args.length; i++) {
            if (args[i].equals("-u")) {
                userConfig = true;
            }
            else if (args[i].equals("-s")) {
            	singleStep = true;
            }
        }

      /*
       * When the userConfig flag is true, we need to check the input is valid or not.
       * At first we would need to print some information to remind the user to input from the keyboard.
       * Then need to call checkInputMode() method to check the input.
       * If the userConfig is false, just skip the check.
       * If the singleStep flag is true, we need to stop at each step. This will be checked in playGame method.
       */
        boolean validFlag = false;
        if(userConfig == true) {
        	userModeInfo();
        	Scanner readIn = new Scanner(System.in);
            //use an ArrayList to store the in put piles
        	ArrayList<Integer> inputPiles = new ArrayList<Integer>();
        	while(!validFlag) {
        		inputPiles = new ArrayList<Integer>();
                //check input is used to check the whether the user input is valid or not
        		validFlag = checkInput(inputPiles, validFlag,readIn);
        	}
        	SolitaireBoard board = new SolitaireBoard(inputPiles);
        	playGame(board, singleStep);
        }else {
        	SolitaireBoard board = new SolitaireBoard();
        	playGame(board, singleStep);
        }
      
    }
   
    /*
     * The following printing information will be used if the user want to input the initial configuration of cards.
     * This information is used to remind the user what they need to type.
     */
    private static void userModeInfo(){
    	System.out.println("Number of total cards is " + SolitaireBoard.CARD_TOTAL);
        System.out.println("You will be entering the initial configuration of the cards (i.e., how many in each pile).");
        System.out.println("Please enter a space-separated list of positive integers followed by newline:");
    }
    
    /*
     * This method is used to check whether the input from user is correct or not.
     * It will return an ArrayList. This ArrayList is used to store the valid input value and it will be used as the input of SolitaireBoard.
     * The return value of this method is validFlag, it's a boolean type.
     * The input value is the ArrayList of inputPiles, the flag of whether the input is valid or not, and the readIn scanner
     */
    private static boolean checkInput(ArrayList<Integer> inputPiles, boolean validFlag, Scanner readIn) {
    	int checkSum = 0;
    	String readLine = readIn.nextLine();
		Scanner readPiles = new Scanner(readLine);
    	
    	while(readPiles.hasNext()) {
    		if(readPiles.hasNextInt() == false) {
				validFlag = false;
				break;
			}
    		int input = readPiles.nextInt();
    		if(input > 0) {
    			checkSum += input;
    			inputPiles.add(input);
    			validFlag = true;
    		}else {
    			validFlag = false;
    			break;
    		}	
    	}
        //after adding all valid input, check the sum of input
    	if(checkSum != SolitaireBoard.CARD_TOTAL) {
			validFlag = false;
		}
    	if(validFlag == false) {
			System.out.println("ERROR: Each pile must have at least one card and the total number of cards must be " 
					+SolitaireBoard.CARD_TOTAL);
			System.out.println("Please enter a space-separated list of positive integers followed by newline:");
		}
		return validFlag;
    }
    
    /*
     * This method will start a game.
     * According to the flag of single step mode or user configure mode, the game will start in different ways.
     * This method will use the result of isDone method to check the game is done or not 
     * This method has no return value.
     * The input is SolitaireBoard, and the flag of singleStep.
     */
    private static void playGame(SolitaireBoard board, boolean flag) {
    	int count = 0;
    	Scanner scanEnter = new Scanner(System.in);
    	System.out.println("Initial configuration:" + board.configString());
    	
       //if this game is not finish, use a loop to continue
    	while(board.isDone() == false) {
    		count++;
    		board.playRound();
    		System.out.println("[" + count + "] Current configuration: " + board.configString());
            //check the flag of singleStep. To decide whether to stop at each step.
    		if(flag == true) {
    			System.out.print("<Type return to continue>");
    			scanEnter.nextLine();
    		}
    	}
    	System.out.println("Done!");
    }
    
}

