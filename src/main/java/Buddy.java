import java.util.Scanner;

/**
 * Entry point for Buddy, a simple chatbot used for the iP.
 */
public class Buddy {
    public static final String NAME = "Buddy";
    private static final String LINE = "---------------------------------------------------------------";

    /**
     * Greets the user and exits.
     *
     * @param args command line arguments supplied by the runtime
     */
    public static void main(String[] args) {
        String banner = " ____            _     _       \n"
                + "| __ ) _   _  __| | __| |_   _ \n"
                + "|  _ \\| | | |/ _` |/ _` | | | |\n"
                + "| |_) | |_| | (_| | (_| | |_| |\n"
                + "|____/ \\__,_|\\__,_|\\__,_|\\__, |\n"
                + "                          |___/ \n";
        Scanner scanner = new Scanner(System.in);

        System.out.println(LINE);
        System.out.println(banner);
        System.out.println("Hello! I'm " + NAME);
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            System.out.println(LINE);
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }
            System.out.println(input);
            System.out.println(LINE);
        }
    }
}
