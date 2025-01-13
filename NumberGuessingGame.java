import java.util.*;

public class NumberGuessingGame{
    public static void main(String[] args) {
        
        Random rand = new Random();
        Scanner sc = new Scanner(System.in);

        System.out.println("-----------NUMBER GUESSING GAME------------");

        System.out.println("\n Constraints :  You have only 7 attempts");

        int randomNumber = rand.nextInt(100)+1;
        int count = 0;

        

        while(count <= 7){

            System.out.print("Enter your guess (1-100) : ");
            int playerGuess = sc.nextInt();
            System.out.println();

            if(playerGuess == randomNumber){
                System.out.println("Hurrah !! Correct .. You win");
                break;
            }
            else if(playerGuess > randomNumber){
                System.out.println("Nope ! Your number is Higher");
            }
            else{
                System.out.println("Nope ! Your number is Lower");
            }

            count++;

        }

        if(count > 7){
            System.out.println("OOPS !! Game Over .. ");
            System.out.println("The answer is " + randomNumber);

        }else{
            System.out.println("You got it at " + count + " tries");
        }

        sc.close();

    }
}