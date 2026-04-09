package EnigmaCore;

public class EnigmaCore {
	
	// set rotor positions for the rotor cores
	private int rotor1Pos;
	private int rotor2Pos;
	private int rotor3Pos;
	
	// Fixed incremental value on the number of forward steps on the rotors and reflector cogs
	// as the message values get encoded/decoded
	private final static int STEP = 3;
	
	// Letters array from A to Z
	private final static String[] LETTERS = {
			"SPACE", "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
	};
	
	// Rotor and reflector values from 1 to 26
	private static final int[] ROTOR1 = {
			0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26
	};
	
	private static final int[] ROTOR2 = {
			0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26
	};
	
	private static final int[] ROTOR3 = {
			0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26
	};
	
	private static final int[] reflector = {
			0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26
	};

	// Plug board default configuration
	private static final String[] PLUG_BOARD_DEFAULT = {
			"SPACE", "A A","B B","C C","D D","E E","F F","G G","H H","I I","J J","K K","L L","M M","N N","O O","P P","Q Q","R R","S S","T T","U U","V V","W W","X X","Y Y","Z Z"
	};
	
	private String[] plugBoard;
	
	// Default constructor
	public EnigmaCore() {
		// set rotor counters to 1
		setRotor1Pos(1);
		setRotor2Pos(1);
		setRotor3Pos(1);
		// set plugBoard to default
		plugBoard = new String[PLUG_BOARD_DEFAULT.length];
		resetPlugBoard();
	}
	
	// Returns encrypted message
	public String encryptMessage(String input) {
		String encryptedMsg = "";
		char encode = ' ';
		for(int i = 0; i < input.length(); i++) {
			encode = encodeLetter(input.charAt(i));
			encryptedMsg = encryptedMsg + encode;
		}
		return encryptedMsg;
	}
	
	// Returns decoded message
	public String decryptMessage(String input) {
		String decryptedMsg = "";
		char decode = ' ';
		for(int i = 0; i < input.length(); i++) {
			decode = decodeLetter(input.charAt(i));
			decryptedMsg = decryptedMsg + decode;
		}
		return decryptedMsg;
	}
	
	// Changes letter as it goes through plug board, rotor 1, rotor2, rotor3, reflector, rotor 3, rotor 2, rotor 1, and back
	// to plug board to return result encoded letter
	public char encodeLetter(char letter) {
		char encoded = ' ';
		int tempCount = 0; // Used to store the location of the letter to retrieve either from rotor/receiver cogs
		
		// Rotate the rotors
		rotate();
		
		// Stage 1 - letter passes to plug board
		for(int i = 1; i < (26 + 1); i++) {
			// Goes through plug board to find the letter and get the corresponding destination value
			if(plugBoard[i].charAt(0) == letter) {
				encoded = plugBoard[i].charAt(2);
				break;
			}
		}
		
		// Stage 2 - letter passes to rotor 1
		// 1 - Get location of letter from letter array
		// 2 - Calculate the location which letter it will get based on the position of rotor 1 and letter location value +
		// the value of step
		// 3 - if the result is greater than 26 (relates to 26 characters in the alphabet), subtract 26 from that result
		// until result is less than 26 will get the location of the letter to get from letters array
		encoded = calcLetterPos(getLetterNumber(encoded), getRotor1Pos(), "E");

		// Stage 3 - letter passes to rotor 2
		// 1 - Get location of letter from letter array
		// 2 - Calculate the location which letter it will get based on the position of rotor 2 and letter location value +
		// the value of step
		// 3 - if the result is greater than 26 (relates to 26 characters in the alphabet), subtract 26 from that result
		// until result is less than 26 will get the location of the letter to get from letters array
		encoded = calcLetterPos(getLetterNumber(encoded), getRotor2Pos(), "E");

		// Stage 4 - letter passes to rotor 3
		// 1 - Get location of letter from letter array
		// 2 - Calculate the location which letter it will get based on the position of rotor 3 and letter location value +
		// the value of step
		// 3 - if the result is greater than 26 (relates to 26 characters in the alphabet), subtract 26 from that result
		// until result is less than 26 will get the location of the letter to get from letters array
		encoded = calcLetterPos(getLetterNumber(encoded), getRotor3Pos(), "E");

		// Stage 5 - letter passes to reflector cog
		// 1 - Get location of letter from letter array
		// 2 - Calculate the location which letter it will get based on the position of reflector and letter location value +
		// the value of step
		// 3 - if the result is greater than 26 (relates to 26 characters in the alphabet), subtract 26 from that result
		// until result is less than 26 will get the location of the letter to get from letters array
		tempCount = getLetterNumber(encoded);

		tempCount = tempCount + STEP;

		if(tempCount > 26) {
			tempCount = tempCount - 26;
		}
		encoded = LETTERS[reflector[tempCount]].charAt(0);
		
		// Stage 6 - letter goes back to rotor 3
		encoded = calcLetterPos(getLetterNumber(encoded), getRotor3Pos(), "E");

		// Stage 7 - letter goes back to rotor 2
		encoded = calcLetterPos(getLetterNumber(encoded), getRotor2Pos(), "E");

		// Stage 8 - letter goes back to rotor 1
		encoded = calcLetterPos(getLetterNumber(encoded), getRotor1Pos(), "E");

		// Stage 9 - letter goes back to plug board
		for(int i = 1; i < (26 + 1); i++) {
			// Goes through plug board to find the letter and get the corresponding destination value
			if(plugBoard[i].charAt(0) == encoded) {
				encoded = plugBoard[i].charAt(2);
				break;
			}
		}
		return encoded;
	}
	
	public char decodeLetter(char letter) {
		char decoded = ' ';
		int tempCount = 0; // Used to store the location of the letter to retrieve either from rotor/receiver cogs
		
		// Rotate the rotors
		rotate();
		
		// Stage 1 - letter passes to plug board
		for(int i = 1; i < (26 + 1); i++) {
			// Goes through plug board to find the letter and get the corresponding destination value
			if(plugBoard[i].charAt(0) == letter) {
				decoded = plugBoard[i].charAt(2);
				break;
			}
		}
		
		// Stage 2 - letter passes to rotor 1
		// 1 - Get location of letter from letter array
		// 2 - Calculate the location which letter it will get based on the position of rotor 1 and letter location value +
		// the value of step
		// 3 - if the result is greater than 26 (relates to 26 characters in the alphabet), subtract 26 from that result
		// until result is less than 26 will get the location of the letter to get from letters array
		decoded = calcLetterPos(getLetterNumber(decoded), getRotor1Pos(), "D");

		// Stage 3 - letter passes to rotor 2
		// 1 - Get location of letter from letter array
		// 2 - Calculate the location which letter it will get based on the position of rotor 2 and letter location value +
		// the value of step
		// 3 - if the result is greater than 26 (relates to 26 characters in the alphabet), subtract 26 from that result
		// until result is less than 26 will get the location of the letter to get from letters array
		decoded = calcLetterPos(getLetterNumber(decoded), getRotor2Pos(), "D");

		// Stage 4 - letter passes to rotor 3
		// 1 - Get location of letter from letter array
		// 2 - Calculate the location which letter it will get based on the position of rotor 3 and letter location value +
		// the value of step
		// 3 - if the result is greater than 26 (relates to 26 characters in the alphabet), subtract 26 from that result
		// until result is less than 26 will get the location of the letter to get from letters array
		decoded = calcLetterPos(getLetterNumber(decoded), getRotor3Pos(), "D");
		
		// Stage 5 - letter passes to reflector cog
		// 1 - Get location of letter from letter array
		// 2 - Calculate the location which letter it will get based on the position of reflector and letter location value +
		// the value of step
		// 3 - if the result is greater than 26 (relates to 26 characters in the alphabet), subtract 26 from that result
		// until result is less than 26 will get the location of the letter to get from letters array
		tempCount = getLetterNumber(decoded);
		
		tempCount = tempCount - STEP;
		
		if(tempCount < 1) {
			tempCount = tempCount + 26;
		}
		decoded = LETTERS[reflector[tempCount]].charAt(0);
		
		// Stage 6 - letter goes back to rotor 3
		decoded = calcLetterPos(getLetterNumber(decoded), getRotor3Pos(), "D");
		
		// Stage 7 - letter goes back to rotor 2
		decoded = calcLetterPos(getLetterNumber(decoded), getRotor2Pos(), "D");
		
		// Stage 8 - letter goes back to rotor 1
		decoded = calcLetterPos(getLetterNumber(decoded), getRotor1Pos(), "D");

		// Stage 9 - letter goes back to plug board
		for(int i = 1; i < (26 + 1); i++) {
			// Goes through plug board to find the letter and get the corresponding destination value
			if(plugBoard[i].charAt(0) == decoded) {
				decoded = plugBoard[i].charAt(2);
				break;
			}
		}
		return decoded;
	}

	// Calculate the letter position based on the current letter position, rotor position, and whether it is encoding or decoding
	public char calcLetterPos(int curPos, int curRotorPos, String operation) {
		switch(operation) {
			case "E":
				curPos = (curPos + curRotorPos) + STEP;
				while(curPos > 26) {
					curPos = curPos - 26;
				}
				break;
			case "D":
				curPos = (curPos - curRotorPos) - STEP;
				while(curPos < 1) {
					curPos = curPos + 26;
				}
				break;
		}
		return LETTERS[curPos].charAt(0);
	}
	
	// rotate appropriate rotor(s) by 1 
	public void rotate() {
		// temporary stores counter value
		int temp;
		// increment rotor 1 if its is less than 26, rotor 2 and rotor 3 less or equal than 26
		if(getRotor1Pos() < 26 && getRotor2Pos() <= 26 && getRotor3Pos() <= 26) {
			temp = getRotor1Pos();
			setRotor1Pos(temp+=1);
		}
		// increment rotor 2 counter if rotor 1 counter reaches 26 and rotor 3 is less or equal to 26 - also reset rotor 1 back to 1
		else if(getRotor1Pos() >= 26 && getRotor2Pos() < 26 && getRotor3Pos() <= 26) {
			temp = getRotor2Pos();
			setRotor2Pos(temp+=1);
			setRotor1Pos(1);
		}
		// increment rotor 3 counter if rotor 1 & rotor 2 reaches 26 and rotor 3 is less than 26 - also reset rotor 1 and 2 back to 1
		else if(getRotor1Pos() >= 26 && getRotor2Pos() >= 26 && getRotor3Pos() < 26) {
			temp = getRotor3Pos();
			setRotor3Pos(temp+=1);
			setRotor2Pos(1);
			setRotor1Pos(1);
		}
		// if all rotor counters reach 26, reset them back to 1
		else if(getRotor1Pos() >= 26 && getRotor2Pos() >= 26 && getRotor3Pos() >= 26) {
			setRotor3Pos(1);
			setRotor2Pos(1);
			setRotor1Pos(1);
		}
	}
	
	// Set up plug board
	public void setPlugBoard(String origin, String destination) {
		switch(origin)
		{
			case "A":
				plugBoard[1] = origin + " " + destination;
				break;
			case "B":
				plugBoard[2] = origin + " " + destination;
				break;
			case "C":
				plugBoard[3] = origin + " " + destination;
				break;
			case "D":
				plugBoard[4] = origin + " " + destination;
				break;
			case "E":
				plugBoard[5] = origin + " " + destination;
				break;
			case "F":
				plugBoard[6] = origin + " " + destination;
				break;
			case "G":
				plugBoard[7] = origin + " " + destination;
				break;
			case "H":
				plugBoard[8] = origin + " " + destination;
				break;
			case "I":
				plugBoard[9] = origin + " " + destination;
				break;
			case "J":
				plugBoard[10] = origin + " " + destination;
				break;
			case "K":
				plugBoard[11] = origin + " " + destination;
				break;
			case "L":
				plugBoard[12] = origin + " " + destination;
				break;
			case "M":
				plugBoard[13] = origin + " " + destination;
				break;
			case "N":
				plugBoard[14] = origin + " " + destination;
				break;
			case "O":
				plugBoard[15] = origin + " " + destination;
				break;
			case "P":
				plugBoard[16] = origin + " " + destination;
				break;
			case "Q":
				plugBoard[17] = origin + " " + destination;
				break;
			case "R":
				plugBoard[18] = origin + " " + destination;
				break;
			case "S":
				plugBoard[19] = origin + " " + destination;
				break;
			case "T":
				plugBoard[20] = origin + " " + destination;
				break;
			case "U":
				plugBoard[21] = origin + " " + destination;
				break;
			case "V":
				plugBoard[22] = origin + " " + destination;
				break;
			case "W":
				plugBoard[23] = origin + " " + destination;
				break;
			case "X":
				plugBoard[24] = origin + " " + destination;
				break;
			case "Y":
				plugBoard[25] = origin + " " + destination;
				break;
			case "Z":
				plugBoard[26] = origin + " " + destination;
				break;
			default:
		}
	}
	
	// Reset plugboard values to default
	public void resetPlugBoard() { 
		for(int count = 0; count < plugBoard.length;count++)
			plugBoard[count] = PLUG_BOARD_DEFAULT[count];
	}

	// Returns current plug board configuration
	public String[] getPlugBoard() {
		return plugBoard;
	}

	 // Output plug board and rotor settings
	public String toString() {
		
		String plugBoardConfig =
			"Plug Board configuration" + "\n" +
			"------------------------" + "\n";
		
		for(int i = 1; i < (26+1); i++) {
			plugBoardConfig = plugBoardConfig + plugBoard[i] + "\n";	
		}
		
		plugBoardConfig = plugBoardConfig + "------------------------" + "\n";
		
		return(
			plugBoardConfig +
			"Rotor settings" + "\n" +
			"--------------" + "\n" +
			"Rotor 1: " + getRotor1Pos() + "\n" +
			"Rotor 2: " + getRotor2Pos() + "\n" +
			"Rotor 3: " + getRotor3Pos() + "\n" +
			"--------------" + "\n"
		);
	}
	
	// Returns the location of a letter in letters array
	private int getLetterNumber(char letter) {
		int value = 0;
		for(int i = 1; i < (26 + 1); i++) {
			if(LETTERS[i].charAt(0) == letter) {
				value = i;
				break;
			}
		}
		return value; // if no match, returns default value
	}

	// Get and set methods start here
	public int getRotor1Pos() { return rotor1Pos; }

	public void setRotor1Pos(int rotor1Pos) { this.rotor1Pos = rotor1Pos; }

	public int getRotor2Pos() { return rotor2Pos; }

	public void setRotor2Pos(int rotor2Pos) { this.rotor2Pos = rotor2Pos; }

	public int getRotor3Pos() { return rotor3Pos; }

	public void setRotor3Pos(int rotor3Pos) { this.rotor3Pos = rotor3Pos; }
}
