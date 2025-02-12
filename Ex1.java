/**
 * NAME : Ron Amsalem
 * ID = :326029600
 */
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
/**
 * Introduction to Computer Science, Ariel University, Ex1 (manual Example + a Template for your solution)
 * See: https://docs.google.com/document/d/1C1BZmi_Qv6oRrL4T5oN9N2bBMFOHPzSI/edit?usp=sharing&ouid=113711744349547563645&rtpof=true&sd=true
 * Ex1 Bulls & Cows - Automatic solution.
 * ** Add a general readme text here **
 * Add your explanation here:
 *
 *We were asked to implement an efficient solution for the Bulls & Cows game.
 In this game,we get a "server side" program that generates a "code" (the code can be between 2 and 6 digits).
 The main goal of a task is to write a program that will connect to the game server and allow solving the server's numeric
 code - each time automatically so that this code needs to be found with a minimal number of guesses.

 * ** General Solution (algorithm) **
 * Add your explanation here:

 * The algorithm systematically examines potential guesses, so that you can see wrong guesses and as a result you can gradually get closer to the correct answer.
 * The algorithm starts by initializing the necessary variables such as the number of digits and the maximum possible value for guesses.
 * I created a boolean array to keep track of possible guesses.
 *All elements are initially set to 'true', indicating that all values are relevant.
 As long as the game is running in each iteration, the 'guess' function is used to select a guess
 Valid from other options.
 * The current boolean guess should be set to 'false'.
 * I used the `remove` function to update the guesses as much as possible, we will discard options that do not match the received information. The function plays a role in refining the set of possible guesses and it eliminates options that do not match the received feedback (B and C). This helps focus on relevant options for subsequent iterations.
 Once the game is no longer running (that is, all digits are guessed correctly), the algorithm
 Prints the final state of the game.
 In conclusion
 - The game starts with a 2-6 digit code
 - The initial Boolean array (`array') contains all possible guesses,
 (for example in 2 digits from "00" to "99").
 - We will enter the main loop and start generating guesses.
 - The first guess is 0 as the amount of digits we want to guess (for example two digits 00), with the help of the auxiliary functions you get feedback that contains the amount of B and C
 - We will update the boolean array (`array') based on this feedback.
 And so on there will be 01, 02, 03, 04 and so on, given the possibility that the process repeats itself, and with it the options must be reduced (with the help of the  'remove' function).
 The breakthrough occurs with guesses where the bull count increases to the number of digits, indicating that the digits are placed in the right place
 - The game ends, and the final status is printed.

 * ** Results **
 * Make sure to state the average required guesses
 * for 2,3,4,5,6 digits code:
 * Average required guesses 2: 7.15
 * Average required guesses 3: 8.12
 * Average required guesses 4: 8.88
 * Average required guesses 5: 8.92
 * Average required guesses 6: 9.18
 * The overall average: 8.45
 */
public class Ex1 {
    public static final String Title = "Ex1 demo: manual Bulls & Cows game";
    public static double count = 0; //Creating a counter that will count the number of guesses
    public static int numOfDigit= 2; // Creating a static variable that I can change at will

    /**
     * Sets the number of digits for the Bulls & Cows game.
     * @param numOfDigits The desired number of digits for the code in the game.
     * This method updates the internal variable `numOfDigit` accordingly.
     */
    public static void setNumOfDigit(int numOfDigits) {
      numOfDigit = numOfDigits;
    }

    public static void main(String[] args) {
        BP_Server game = new BP_Server();   // Starting the "game-server"
        long myID = 326029600;             // Your ID should be written here
        int numOfDigits =2;                // Number of digits [2,6]
        game.startGame(myID, numOfDigit);  // Starting a game
        System.out.println(Title + " with code of " + numOfDigits + " digits");
        //manualEx1Game(game);
        autoEx1Game(game); // you should implement this function )and any additional required functions).
    }

    public static void manualEx1Game(BP_Server game) {
        Scanner sc = new Scanner(System.in);
        int ind = 1;      // Index of the guess
        int numOfDigits = game.getNumOfDigits();
        double max = Math.pow(10, numOfDigits);
        while (game.isRunning()) {           // While the game is running (the code has not been found yet
            System.out.println(ind + ") enter a guess: ");
            int g = sc.nextInt();
            if (g >= 0 && g < max) {
                int[] guess = toArray(g, numOfDigits); // int to digit array
                int[] res = game.play(guess); // Playing a round and getting the B,C
                if (game.isRunning()) {     // While the game is running
                    System.out.println(ind + ") B: " + res[0] + ",  C: " + res[1]); // Prints the Bulls [0], and the Cows [1]
                    ind += 1;               // Increasing the index
                }
            } else {
                System.out.println("ERR: wrong input, try again");
            }
        }
        System.out.println(game.getStatus());
    }

    /**
     * Simple parsing function that gets an int and returns an array of digits
     * @param a    - a natural number (as a guess)
     * @param size - number of digits (to handle the 00 case).
     * @return an array of digits
     */
    private static int[] toArray(int a, int size) {
        int[] c = new int[size];
        for (int j = 0; j < c.length; j += 1) {
            c[j] = a % 10;
            a = a / 10;
        }
        return c;
    }

    /**
     *   This function solves the Bulls & Cows game automatically.
     *      * You should implement
     *      * ** Add a specific explanation for each function **

     *     Initialize all elements in the boolean array to true, indicating that all possible guesses are initially valid.
     *      As long as the game is valid.
     *      At each iteration, generate a guess (g) using the Guess function, convert it to an array of digits, and play it in the game.
     *      Extract the number of bulls (b) and cows (c) from the game result.
     *      Mark the current guess as spoken by setting its corresponding element in the array to false.
     *      Remove irrelevant options by result using the removeOptions function.
     *      game.getNumOfDigits = The number of digits I want to guess
     *      size = Maximum possible guesses - The maximum that can be, what is the possibility that I have in the game 10 to the power of the number
     *      of digits -1 because it will be for example 10 to the power of 2 which is 100 we will have to subtract 1 which will be 99
     *      arr = Boolean array containing all possible options
     *      g=  current guess
     */
    public static void autoEx1Game(BP_Server game) {

        int size = (int) Math.pow(10, game.getNumOfDigits()) -1;
        boolean[] arr = new boolean[size];
        for (int i = 0; i < arr.length; i++) {//Go through all the members of the array and insert a true value in them
            arr[i] = true;
        }
        while (game.isRunning()) {
            int g = guess(arr);
            int[] guess = IntToArray(g, game.getNumOfDigits());
            int[] result = game.play(guess);
            count++;
            int bool = result[0];
            int cow = result[1];
            arr[g]=false;
            remove(g, bool, cow, arr, game.getNumOfDigits());
        }
        System.out.println(game.getStatus());
    }

    /**
     *This function is designed to compare a current guess (`CurrentGuess`) with the correct answer (`result`) and determine the count of Bulls and Cows.
     * The function provides information about the accuracy of a guess and helps in evaluating the guess
     * @param CurrentGuess -A current guess
     * @param result -The number to be guessed
     * @param numOfDigit- The number of digits I want to guess
     * @return - An array of size 2, the array at the zero position belongs to B and the 1st position belongs to C
     */
    public static int[] getInfo(int[] CurrentGuess, int[] result, int numOfDigit) { //get two arrays, one is the current guess, and the other is the correct number
        boolean[] guess = new boolean[numOfDigit]; //Array for testing the guess
        boolean[] correctGuess = new boolean[numOfDigit]; //The correct guess
        int[] info = new int[2]; //The array in position 0 is B, the array in position 1 is C


        // Check for Bulls (correct digit in correct position
        for (int i = 0; i < numOfDigit; i++) {
            if (CurrentGuess[i] == result[i] && !guess[i] && !correctGuess[i]) {
                guess[i] = true;
                correctGuess[i] = true;
                info[0]++;
            }
        }
        // Check for Cows (correct digit in incorrect position)
        for (int i = 0; i < numOfDigit; i++) {
            for (int j = 0; j < numOfDigit; j++) {
                if (CurrentGuess[i] == result[j] && !guess[i] && !correctGuess[j]) {
                    guess[i] = true;
                    correctGuess[j] = true;
                    info[1]++;
                }
            }
        }
        return info;
    }


    /**
     *The function takes a provider and converts it to an array.
     * @param num = The number that I want to swap to array
     * @param numOfDigit = The number of digits
     * @return = An array that shows the number that the function took as an array
     */
    public static int[] IntToArray(int num, int numOfDigit){
        int[] array  = new int[numOfDigit]; // Opening an array
        for(int i = numOfDigit - 1;i >= 0;i--){
            if(num > 0){
                array[i] = num % 10;
                num =num / 10;
            }
            else{
                array[i] = 0;
            }
        }
        return array;
    }
    /**
     *This function uses the IntToArray function to convert numbers to an array of digits
     and then uses the GetInfo function, which returns an array with information containing the amount of B and C.
     * Checks if a given guess is correct based on the provided information.
     * @param current = The current guess
     * @param numOfDigits = The number of digits to be guessed [2-6]
     * @param b =Amount of the bool
     * @param c =Amount of the cows
     * @param cGuess = the correct guess
     * @return The function returns true or false
     * @return false :if the conditions above are not met, it returns `false`, indicating that the guess is not correct.
     * @return true : the function returns `true` if the current guess has the specified number of Bulls and Cows as expected
     */
    public static boolean guessIsCorect(int current , int numOfDigits , int b , int c,int cGuess ) {
        int [] CurrentGuess = IntToArray(current,numOfDigits);
        int [] CorectGuess=IntToArray(cGuess,numOfDigits);
        int [] info2 = getInfo(CurrentGuess,CorectGuess,numOfDigits);
        if (info2 [0] == b && info2 [1]==c){
            return true;
        }
        return false;
    }
    /**
     *This function is to reduce the options by eliminating guesses that do not match
      the given information, thus helping to focus on relevant options
     *This function iterates over the possible guesses represented by the boolean array `arr` and eliminates those that do not match
      the specified Bulls and Cows for the given current guess (`g`). The condition ensures that only guesses not satisfying the specified Bulls and Cows are removed.
     *The call to the guessIsCorrect method contributes to Comparing the current guess (g)
      with the correct guess, which is represented by the index i in the boolean array arr.
     The method checks whether the number of bulls (b) and cows (c) matches the information derived from comparing the two guesses.
     * @param g = The current guess
     * @param b =Amount of the bool
     * @param c =Amount of the cows
     * @param arr =boolean array representing guesses possible,
     * @param numOfDigits = The number of digits to be guessed [2-6]
     */

    private static void remove(int g, int b, int  c, boolean[] arr, int numOfDigits) {
        for (int i = 0; i < arr.length ; i++) {
            if (!guessIsCorect(g,numOfDigits,b,c,i)){
                arr[i]=false;
            }
        }

        }


    /**
     *The function returns the index of the first `true` element in the boolean array. If no valid guess is found, it returns 0.
     * @param arr = Boolean array
     * index = Index of array members
     * @return =- If guess is `true`, it means a valid guess is found, and the function returns the current `index`.
     */

    public static int guess(boolean[] arr) {
        int index = 0;
        for (boolean guess : arr) {
            if (guess==true) {
                return index;
            }
            index++; // If no valid guess is found at the beginning of the loop, the function increments the `index` by 1 and continues iterating through the array.
        }
        return 0; // If the loop completes without finding a valid guess, the function return 0 as an indication that there is no valid guess in the array.

    }

    }