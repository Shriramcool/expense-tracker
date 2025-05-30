#  Expense Tracker – Java Console Application

A simple, interactive Java console application to track personal finances. This program allows users to add and categorize income and expenses, view monthly summaries, and persist data through file storage.

---

##  Features

- Add income and expenses
- Categorize transactions (e.g., Food, Rent, Travel, Salary, Business, etc.)
- View detailed monthly summaries (income, expenses, savings)
- View complete transaction history
- Save and load data to/from a `.txt` file
- Load transactions in bulk from a formatted input file

---

##  Getting Started

### Prerequisites

- Java JDK 8 or higher
- A terminal/command prompt

### Compilation

```bash
javac ExpenseTracker.java
```

Make sure Transaction.java and TransactionType.java are in the same folder.

### Run the Application

```bash
java ExpenseTracker
```

---

##  Application Menu

```
1. Add Income/Expense
2. View Monthly Summary
3. Load data from file
4. Save data to file
5. View all transactions
6. Exit
```

---

##  Data File Format

The app uses a file named expense_data.txt to store your transactions. Each entry is saved in this format:

```
TYPE|CATEGORY|AMOUNT|DESCRIPTION|DATE
```

**Example:**

```
INCOME|Salary|5000|Monthly salary|2024-05-01
EXPENSE|Rent|1500|May rent|2024-05-02
EXPENSE|Food|200|Groceries|2024-05-03
```

---

##  How to Use

###  Add Transactions

1. Select `1` from the menu.
2. Choose whether it’s an `Income` or `Expense`.
3. Select a sub-category.
4. Enter the amount, a short description (optional), and the date (or press Enter for today).

###  View Monthly Summary

1. Select `2`.
2. Enter a month in `YYYY-MM` format or press Enter to use the current month.
3. Displays income, expenses, net amount, and category-wise breakdown.

###  Save or Load Data

- **Save**: Select `4` to save data to `expense_data.txt`.
- **Load**: Choose `3` to load existing data from expense_data.txt or another file.

###  View All Transactions

- Select `5` to see a full list of all recorded transactions, sorted by date.
