package github;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Tournament {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numTeams = 0;

        // Ensure the number of teams is a power of 2
        while (true) {
            System.out.print("Enter the number of teams (must be a power of 2): ");
            if (scanner.hasNextInt()) {
                numTeams = scanner.nextInt();
                if (isPowerOfTwo(numTeams)) {
                    break;
                } else {
                    System.out.println("The number of teams must be a power of 2. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // clear the invalid input
            }
        }

        scanner.nextLine(); // consume the newline

        // Get team names
        ArrayList<String> teams = new ArrayList<>();
        for (int i = 0; i < numTeams; i++) {
            System.out.print("Enter the name of team " + (i + 1) + ": ");
            teams.add(scanner.nextLine());
        }

        ArrayList<ArrayList<String>> rounds = new ArrayList<>();
        rounds.add(teams);

        // Run the tournament
        while (rounds.get(rounds.size() - 1).size() > 1) {
            ArrayList<String> currentRound = new ArrayList<>(rounds.get(rounds.size() - 1));
            ArrayList<String> nextRound = new ArrayList<>(Collections.nCopies(currentRound.size() / 2, null));
            rounds.add(nextRound);

            while (nextRound.contains(null)) {
                showMenu(scanner, rounds);

                int matchIndex = selectMatch(scanner, currentRound, nextRound);

                if (matchIndex == -1) {
                    continue; // If no match is selected, return to menu
                }

                String team1 = currentRound.get(matchIndex * 2);
                String team2 = currentRound.get(matchIndex * 2 + 1);

                // Ask the user who won
                String winner = getWinner(scanner, team1, team2);
                nextRound.set(matchIndex, winner);
            }
        }

        // Declare the winner
        System.out.println("\n=== The winner of the tournament is: " + rounds.get(rounds.size() - 1).get(0) + " ===");
        scanner.close();
    }

    // Check if a number is a power of 2
    private static boolean isPowerOfTwo(int n) {
        return (n & (n - 1)) == 0 && n > 0;
    }

    // Show the menu
    private static void showMenu(Scanner scanner, ArrayList<ArrayList<String>> rounds) {
        System.out.println("\nMenu:");
        System.out.println("1. View Tournament Bracket");
        System.out.println("2. Input Match Result");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        if (choice == 1) {
            displayBracket(rounds);
        }
    }

    // Display the tournament bracket
    private static void displayBracket(ArrayList<ArrayList<String>> rounds) {
        System.out.println("\n=== Tournament Bracket ===");
        for (int roundNum = 0; roundNum < rounds.size(); roundNum++) {
            ArrayList<String> round = rounds.get(roundNum);
            System.out.println("Round " + (roundNum + 1) + ":");
            for (int i = 0; i < round.size(); i += 2) {
                String team1 = round.get(i);
                String team2 = i + 1 < round.size() ? round.get(i + 1) : null;
                String matchResult = (team1 != null ? team1 : "TBD") + " vs " + (team2 != null ? team2 : "TBD");
                System.out.println("  " + matchResult);
            }
            System.out.println();
        }
    }

    // Select a match to input the result for
    private static int selectMatch(Scanner scanner, ArrayList<String> currentRound, ArrayList<String> nextRound) {
        ArrayList<Integer> availableMatches = new ArrayList<>();
        System.out.println("\nSelect a match to input the result for:");
        for (int i = 0; i < currentRound.size(); i += 2) {
            if (nextRound.get(i / 2) == null) { // Only list matches that haven't been decided
                availableMatches.add(i / 2);
                System.out.println((i / 2 + 1) + ". " + currentRound.get(i) + " vs " + currentRound.get(i + 1));
            }
        }

        if (availableMatches.isEmpty()) {
            System.out.println("All matches have been decided for this round.");
            return -1;
        }

        while (true) {
            System.out.print("Enter the match number: ");
            if (scanner.hasNextInt()) {
                int matchIndex = scanner.nextInt() - 1;
                scanner.nextLine(); // consume the newline
                if (availableMatches.contains(matchIndex)) {
                    return matchIndex;
                } else {
                    System.out.println("Invalid match number. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // clear the invalid input
            }
        }
    }

    // Ask the user who won between two teams
    private static String getWinner(Scanner scanner, String team1, String team2) {
        while (true) {
            System.out.print("Who won the match between " + team1 + " and " + team2 + "? ");
            String winner = scanner.nextLine().trim();

            if (winner.equalsIgnoreCase(team1)) {
                return team1;
            } else if (winner.equalsIgnoreCase(team2)) {
                return team2;
            } else {
                System.out.println("Invalid input. Please enter the exact name of the winning team.");
            }
        }
    }
}