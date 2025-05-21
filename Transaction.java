import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final TransactionType type;
    private final String category;
    private final double amount;
    private final String description;
    private final LocalDate date;

    public Transaction(TransactionType type, String category, double amount, String description, LocalDate date) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public TransactionType getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }

    public String toFileFormat() {
        return String.format("%s|%s|%.2f|%s|%s", type, category, amount, description, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public static Transaction parse(String line) {
        try {
            String[] parts = line.split("\\|");
            TransactionType type = TransactionType.valueOf(parts[0]);
            String category = parts[1];
            double amount = Double.parseDouble(parts[2]);
            String description = parts[3];
            LocalDate date = LocalDate.parse(parts[4]);
            return new Transaction(type, category, amount, description, date);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %.2f (%s)", date, category, amount, type);
    }
}
