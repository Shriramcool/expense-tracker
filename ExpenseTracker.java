import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseTracker {
    private static final String DATA_FILE = "expense_data.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static List<Transaction> transactions = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Expense Tracker");
        loadDataFromFile();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getValidIntInput();

            switch (choice) {
                case 1 -> addTransaction();
                case 2 -> viewMonthlySummary();
                case 3 -> loadFromFile();
                case 4 -> saveToFile();
                case 5 -> viewAllTransactions();
                case 6 -> {
                    running = false;
                    saveToFile();
                    System.out.println("Thank you for using Expense Tracker");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nEXPENSE TRACKER MENU");
        System.out.println("1. Add Income/Expense");
        System.out.println("2. View Monthly Summary");
        System.out.println("3. Load data from file");
        System.out.println("4. Save data to file");
        System.out.println("5. View all transactions");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");
    }

    private static void addTransaction() {
        System.out.println("\nADD TRANSACTION");
        System.out.println("Select transaction type:");
        System.out.println("1. Income");
        System.out.println("2. Expense");
        System.out.print("Enter choice (1 or 2): ");
        int typeChoice = getValidIntInput();

        TransactionType type = switch (typeChoice) {
            case 1 -> TransactionType.INCOME;
            case 2 -> TransactionType.EXPENSE;
            default -> null;
        };

        if (type == null) {
            System.out.println("Invalid type selected.");
            return;
        }

        String category = getCategory(type);
        if (category == null) {
            System.out.println("Invalid category.");
            return;
        }

        System.out.print("Enter amount: ");
        double amount = getValidDoubleInput();

        System.out.print("Enter description (optional): ");
        scanner.nextLine();
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description = category + " transaction";
        }

        System.out.print("Enter date (YYYY-MM-DD) or press Enter for today: ");
        String dateInput = scanner.nextLine().trim();
        LocalDate date = dateInput.isEmpty() ? LocalDate.now() : parseDate(dateInput);

        transactions.add(new Transaction(type, category, amount, description, date));
        System.out.println("Transaction added successfully.");
        saveToFile();
    }

    private static LocalDate parseDate(String dateInput) {
        try {
            return LocalDate.parse(dateInput, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid format. Using today's date.");
            return LocalDate.now();
        }
    }

    private static String getCategory(TransactionType type) {
        if (type == TransactionType.INCOME) {
            System.out.println("1. Salary  2. Business  3. Investment  4. Freelance  5. Other");
            System.out.print("Select income category: ");
            return switch (getValidIntInput()) {
                case 1 -> "Salary";
                case 2 -> "Business";
                case 3 -> "Investment";
                case 4 -> "Freelance";
                case 5 -> "Other";
                default -> null;
            };
        } else {
            System.out.println("1. Food  2. Rent  3. Travel  4. Entertainment  5. Utilities  6. Healthcare  7. Shopping  8. Other");
            System.out.print("Select expense category: ");
            return switch (getValidIntInput()) {
                case 1 -> "Food";
                case 2 -> "Rent";
                case 3 -> "Travel";
                case 4 -> "Entertainment";
                case 5 -> "Utilities";
                case 6 -> "Healthcare";
                case 7 -> "Shopping";
                case 8 -> "Other";
                default -> null;
            };
        }
    }

    private static void viewMonthlySummary() {
        System.out.print("Enter month (YYYY-MM) or press Enter for current month: ");
        scanner.nextLine();
        String input = scanner.nextLine().trim();
        String month = input.isEmpty() ? LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) : input;

        List<Transaction> monthly = transactions.stream()
                .filter(t -> t.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM")).equals(month))
                .toList();

        if (monthly.isEmpty()) {
            System.out.println("No transactions for " + month);
            return;
        }

        double income = monthly.stream().filter(t -> t.getType() == TransactionType.INCOME).mapToDouble(Transaction::getAmount).sum();
        double expense = monthly.stream().filter(t -> t.getType() == TransactionType.EXPENSE).mapToDouble(Transaction::getAmount).sum();

        System.out.printf("Total Income: %.2f%n", income);
        System.out.printf("Total Expense: %.2f%n", expense);
        System.out.printf("Net: %.2f%n", income - expense);
    }

    private static void viewAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }

        System.out.println("All Transactions:");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    private static void loadFromFile() {
        System.out.print("Enter file path: ");
        scanner.nextLine();
        String path = scanner.nextLine().trim();
        loadDataFromFile(path.isEmpty() ? DATA_FILE : path);
    }

    private static void loadDataFromFile() {
        loadDataFromFile(DATA_FILE);
    }

    private static void loadDataFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            transactions.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#") && !line.isEmpty()) {
                    Transaction t = Transaction.parse(line);
                    if (t != null) transactions.add(t);
                }
            }
            System.out.println("Loaded transactions from " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to load: " + e.getMessage());
        }
    }

    private static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            writer.println("# Type|Category|Amount|Description|Date");
            for (Transaction t : transactions) {
                writer.println(t.toFileFormat());
            }
            System.out.println("Data saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Failed to save: " + e.getMessage());
        }
    }

    private static int getValidIntInput() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.print("Enter a valid number: ");
            }
        }
    }

    private static double getValidDoubleInput() {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.print("Enter a valid amount: ");
            }
        }
    }
}
