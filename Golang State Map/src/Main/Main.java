package Main;
//imports
import Objects.Literal;

//libraries
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

	static File file = new File("Test");
	static LinkedList<String> lexemes = new LinkedList<String>(); //Linked list that stores input from file
	static LinkedList<Literal> literals = new LinkedList<Literal>(); //generic linked list

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Scanner fileInput = new Scanner(file);

		while(fileInput.hasNext()) {
			lexemes.add(fileInput.next());
		}

		printStringList(lexemes); //print out the contents of our list.


		for(int i = 0; i < lexemes.size(); i++) {
			analyzeString(lexemes.get(i));
		}


		printLiteralList(literals);
	}


	public static void analyzeString(String str) {

		String check = Character.toString(str.charAt(0));

		if(check.matches("[a-zA-Z]+")) { // if the first character is a letter, we see if the lexeme is a valid id
			if(checkID(str) == true) {
				handleID(str);
			}

		} else if(check.compareTo("'") == 0) {
			if(checkRawString(str) == true) {
				handleRawString(str);
			}
		} else if(check.compareTo("\"") == 0) {
			if(checkInterpretedString(str) == true) {
				handleInterpretedString(str);
			}
		} else if(check.compareTo("0") == 0 && str.length() == 1) {
			Literal lit = new Literal("0", "Decimal_Literal");
			literals.add(lit);
		} else if(check.compareTo("0") == 0) {
			if(checkInteger(str) == true) { //we do nothing in this block since decimals that start with zero get handled through the checkInteger method
				//handleDecimal(str);
			}else if(checkFloat(str) == true) {
				handleFloatStartsWith0x(str);
			}
		} else if(isDecimal(check.charAt(0)) == true) {
			if(checkIntegerStartsWithDecimal(str) == true) {
				handleIntegerStartWithDecimal(str);
			}


			if(checkFloatStartsWithDecimal(str) == true) {
				handleFloatStartWithDecimal(str);
			}

		}

		else { //if the first character isn't a letter
			error();
		}

	}

	public static boolean checkID(String str) {
		char ch;
		for(int i = 0; i < str.length(); i++) { // We check each char if it is a letter.
			ch = str.charAt(i);
			if(Character.isLetter(ch) == false && isDecimal(ch) == false) { //If 
				return false;
			}

		}

		return true;
	}

	public static void handleID(String str) {
		Literal lit = new Literal(str, "ID");
		literals.add(lit);

	}

	public static boolean checkRawString(String str) {
		char ch;

		for(int i = 1; i < (str.length() - 1); i++) {
			ch = str.charAt(i);

			if(Character.isLetter(ch) == false && Character.toString(ch).compareTo("\\n") != 0 ) {
				return false;
			}
		}
		ch = str.charAt(str.length() - 1);

		if(Character.toString(ch).compareTo("'") != 0) {
			return false;
		}

		return true;
	}

	public static void handleRawString(String str) {
		Literal lit = new Literal(str, "Raw_String");
		literals.add(lit);
	}

	public static boolean checkInterpretedString(String str) {
		char ch;

		ch = str.charAt(1);
		if(Character.toString(ch).compareTo("\\") == 0) {
			String sub = str.substring(1, str.length() - 1);

			if(isUValue(sub) == true) {
				handleUValue(str);
				return false; //we return false here because UValues will be handled slightly differently than typical interpretedStrings
			}

		}

		for(int i = 1; i < (str.length() - 1); i++) {
			ch = str.charAt(i);

			if(Character.isLetter(ch) == false) {
				return false;
			}
		}


		ch = str.charAt(str.length() - 1);
		if(Character.toString(ch).compareTo("\"") != 0) {
			return false;
		}

		return true;
	}

	public static void handleInterpretedString(String str) {
		Literal lit = new Literal(str, "Interpreted_String");
		literals.add(lit);
	}

	public static boolean isUValue(String str) {
		System.out.println("String from isUValue: " + str);

		if(Character.toString(str.charAt(0)).compareTo("\\") != 0) {
			return false;
		}

		if(Character.toString(str.charAt(1)).compareTo("u") != 0 && Character.toString(str.charAt(1)).compareTo("U") != 0) {
			return false;
		}

		for(int i = 2; i < str.length(); i++) {
			char ch = str.charAt(i);

			if(isHexidecimal(ch) == false) {
				return false;
			}
		}

		return true;
	}

	public static void handleUValue(String str) {
		Literal lit = new Literal(str, "Interpreted_String");
		literals.add(lit);
	}

	public static boolean checkInteger(String str) {
		char ch = str.charAt(1);

		if(ch == 'b' || ch == 'B') {
			if(checkBinary(str) == true) {
				handleBinary(str);
				return true;
			}
		}
		else if(ch == 'o' || ch == 'O') {
			if(checkOctal(str) == true) {
				handleOctal(str);
				return true;
			}
		}
		else if(ch == 'x' || ch == 'X') {
			if(checkHexidecimal(str) == true) {
				handleHexidecimal(str);
				return true;
			}
		} else {

			for(int i = 1; i < str.length(); i++) {

				ch = str.charAt(i);

				if(isDecimal(ch) == false) {
					return false;
				}

			}

			handleDecimal(str);
			return true;
		}




		return false;
	}

	public static void handleDecimal(String str) {
		Literal lit = new Literal(str, "Decimal_Literal");
		literals.add(lit);
	}

	public static boolean checkBinary(String str) {

		for(int i = 2; i < str.length(); i++) {
			char ch = str.charAt(i);

			if(isBinary(ch) == false) {
				return false;
			}
		}

		return true;
	}

	public static void handleBinary(String str) {
		Literal lit  = new Literal(str, "Binary_Literal");
		literals.add(lit);
	}

	public static boolean checkOctal(String str) {

		for(int i = 2; i < str.length(); i++) {
			char ch = str.charAt(i);

			if(isOctal(ch) == false) {
				return false;
			}
		}

		return true;
	}

	public static void handleOctal(String str) {
		Literal lit = new Literal(str, "Octal_Literal");
		literals.add(lit);

	}

	public static boolean checkHexidecimal(String str) {

		for(int i = 2; i < str.length(); i++) {
			char ch = str.charAt(i);

			if(isHexidecimal(ch) == false) {
				return false;
			}
		}

		return true;
	}

	public static void handleHexidecimal(String str) {
		Literal lit = new Literal(str, "Hexidecimal_Literal");
		literals.add(lit);
	}

	public static boolean checkFloat(String str) {
		char ch;
		int pIndex = 0;

		ch = str.charAt(1);

		if(ch != 'x' && ch != 'X') {
			return false;
		}

		for(int i = 2; i < str.length(); i++) {
			char check = str.charAt(i);

			if(check == 'p' || check == 'P') {
				pIndex = i;
				break;
			}

			if(isHexidecimal(check) != true && check != '.') {
				return false;
			}

		}

		ch = str.charAt((pIndex + 1));

		if(ch != '+' && ch != '-') {
			return false;
		}

		for(int i = (pIndex + 2); i < str.length(); i++) {
			char check = str.charAt(i);

			if(isDecimal(check) == false) {
				return false;
			}

		}

		return true;
	}

	public static void handleFloatStartsWith0x(String str) {
		Literal lit = new Literal(str, "Floating_Point_Literal");
		literals.add(lit);
	}

	public static boolean checkIntegerStartsWithDecimal(String str) {

		for(int i = 0; i < str.length(); i++) {
			char check = str.charAt(i);

			if(isDecimal(check) == false) {
				return false;
			}

		}

		return true;
	}

	public static void handleIntegerStartWithDecimal(String str) {
		Literal lit = new Literal(str, "Decimal_Literal");
		literals.add(lit);
	}

	public static boolean checkFloatStartsWithDecimal(String str) {
		char ch;
		int eIndex = 0;
		boolean isE = false;

		for(int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);

			if(ch == 'e' || ch == 'E') {
				eIndex = i;
				isE = true;
				break;
			}


			if(isDecimal(ch) != true && ch != '.' ) {
				return false;
			}
		}

		if(isE == true) {

			ch = str.charAt((eIndex + 1));

			if(ch != '+' && ch != '-') {
				return false;
			}

			for(int i = (eIndex + 2); i< str.length(); i++) {
				ch = str.charAt(i);
				
				if(isDecimal(ch) == false) {
					return false;
				}
			}
			return true;
			
		} else {

			return true;
		}
	}

	public static void handleFloatStartWithDecimal(String str) {
		Literal lit = new Literal(str, "Floating_Point_Literal");
		literals.add(lit);
	}

	public static boolean containsPeriod(String str) {

		for(int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);

			if(ch == '.') {
				return true;
			}
		}

		return false;
	}

	public static boolean isDecimal(char ch) {

		switch(ch) {
		case '0':
		case '1':
		case '2':
		case '3':	
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':	
			return true;
		default:
			return false;
		}

	}

	public static boolean isBinary(char ch) {

		switch(ch) {
		case '0':
		case '1':
			return true;
		default:
			return false;
		}
	}

	public static boolean isOctal(char ch) {
		switch(ch) {
		case '0':
		case '1':
		case '2':
		case '3':	
		case '4':
		case '5':
		case '6':
		case '7':	
			return true;
		default:
			return false;
		}
	}

	public static boolean isHexidecimal(char ch) {

		switch(ch) {
		case '0':
		case '1':
		case '2':
		case '3':	
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		case 'a':
		case 'A':
		case 'b':
		case 'B':
		case 'c':
		case 'C':
		case 'd':
		case 'D':
		case 'e':
		case 'E':
		case 'f':
		case 'F':	
			return true;
		default:
			return false;
		}
	}

	public static void error() {
		System.out.println("Error!");
		System.exit(0);
	}


	public static void printStringList(List<String> list) {
		for(int i = 0; i < list.size(); i++) {
			System.out.println("list(" + i + "): " + list.get(i));
		}
	}

	public static void printLiteralList(List<Literal> list) {
		for(int i = 0; i < list.size(); i++) {
			System.out.println("literal(" + i + "): " + list.get(i));
		}
	}

}
