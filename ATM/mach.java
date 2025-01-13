package ATM;

import java.util.Scanner;

public class mach {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("+-------------------WELCOME----------------------+\n");
        System.out.println("1. Login to your account");
        System.out.println("2. Create a new Bank Account");
        System.out.println("3. Exit");
        System.out.println("-------------------------------------------------\n");

        while (true) {
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: {
                    System.out.println("\n+-------------------LOGIN-------------------+");
                    System.out.print("Enter User ID: ");
                    int userID = sc.nextInt();

                    System.out.print("Enter 4-digit PIN: ");
                    int userPIN = sc.nextInt();

                    Login login = new Login(userID, userPIN);
                    login.loginUser();
                    break;
                }

                case 2: {
                    System.out.println("\n+---------------CREATE ACCOUNT---------------+");
                    // Create account object and invoke details input
                    createAccount account = new createAccount();
                    account.Details();
                    break;
                }

                case 3: {
                    System.out.println("\nThank you for using our ATM system. Goodbye!");
                    break;

                }

                default: {
                    System.out.println("Invalid choice! Please enter a valid option (1, 2, or 3).");
                }
            }

            System.out.println("\n-------------------------------------------------\n");
            System.out.println("1. Login to your account");
            System.out.println("2. Create a new Bank Account");
            System.out.println("3. Exit");
        }
    }
}
