package CodeAlpha_StockTradingPlatform;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Stock Class representing market items
class Stock {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    
    public void setPrice(double price) { this.price = price; }
}

// Portfolio Class managing user shares
class Portfolio {
    private Map<String, Integer> holdings = new HashMap<>(); // Symbol -> Quantity

    public void addShares(String symbol, int qty) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + qty);
    }

    public boolean removeShares(String symbol, int qty) {
        int currentQty = holdings.getOrDefault(symbol, 0);
        if (currentQty >= qty) {
            holdings.put(symbol, currentQty - qty);
            if (holdings.get(symbol) == 0) {
                holdings.remove(symbol);
            }
            return true;
        }
        return false;
    }

    public int getSharesCount(String symbol) {
        return holdings.getOrDefault(symbol, 0);
    }

    public void displayPortfolio(Map<String, Stock> market) {
        System.out.println("\n--- YOUR PORTFOLIO ---");
        if (holdings.isEmpty()) {
            System.out.println("Your portfolio is currently empty.");
            return;
        }
        System.out.printf("%-10s %-20s %-10s %-15s\n", "Symbol", "Name", "Shares", "Current Value");
        System.out.println("-------------------------------------------------------------");
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            Stock stock = market.get(entry.getKey());
            int qty = entry.getValue();
            double totalVal = qty * stock.getPrice();
            System.out.printf("%-10s %-20s %-10d $%-14.2f\n", stock.getSymbol(), stock.getName(), qty, totalVal);
        }
    }
}

// Main Trading Platform
public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Stock> market = new HashMap<>();
        
        // Initialize Market Stocks
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 180.50));
        market.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 140.20));
        market.put("TSLA", new Stock("TSLA", "Tesla Inc.", 210.00));
        market.put("AMZN", new Stock("Amazon", "Amazon.com Inc.", 155.80));

        double cashBalance = 10000.00; // Starting virtual cash
        Portfolio userPortfolio = new Portfolio();

        while (true) {
            System.out.println("\n=== CODEALPHA STOCK TRADING PLATFORM ===");
            System.out.printf("Available Cash Balance: $%.2f\n", cashBalance);
            System.out.println("1. Display Market Data");
            System.out.println("2. View Portfolio");
            System.out.println("3. Buy Stock");
            System.out.println("4. Sell Stock");
            System.out.println("5. Exit & Submit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 5.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- LIVE MARKET DATA ---");
                    System.out.printf("%-10s %-20s %-10s\n", "Symbol", "Company Name", "Price ($)");
                    System.out.println("----------------------------------------");
                    for (Stock s : market.values()) {
                        System.out.printf("%-10s %-20s $%-10.2f\n", s.getSymbol(), s.getName(), s.getPrice());
                    }
                    break;

                case 2:
                    userPortfolio.displayPortfolio(market);
                    break;

                case 3:
                    System.out.print("Enter Stock Symbol to Buy (e.g., AAPL): ");
                    String buySymbol = scanner.nextLine().toUpperCase();
                    if (!market.containsKey(buySymbol)) {
                        System.out.println("Stock symbol not found!");
                        break;
                    }
                    System.out.print("Enter quantity to buy: ");
                    try {
                        int buyQty = Integer.parseInt(scanner.nextLine());
                        if (buyQty <= 0) {
                            System.out.println("Quantity must be greater than zero.");
                            break;
                        }
                        Stock stockToBuy = market.get(buySymbol);
                        double totalCost = buyQty * stockToBuy.getPrice();

                        if (cashBalance >= totalCost) {
                            cashBalance -= totalCost;
                            userPortfolio.addShares(buySymbol, buyQty);
                            System.out.printf("Successfully bought %d shares of %s for $%.2f\n", buyQty, buySymbol, totalCost);
                        } else {
                            System.out.println("Insufficient funds to complete this transaction!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity format.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Stock Symbol to Sell (e.g., AAPL): ");
                    String sellSymbol = scanner.nextLine().toUpperCase();
                    if (!market.containsKey(sellSymbol)) {
                        System.out.println("Stock symbol not found!");
                        break;
                    }
                    System.out.print("Enter quantity to sell: ");
                    try {
                        int sellQty = Integer.parseInt(scanner.nextLine());
                        if (sellQty <= 0) {
                            System.out.println("Quantity must be greater than zero.");
                            break;
                        }
                        if (userPortfolio.getSharesCount(sellSymbol) >= sellQty) {
                            Stock stockToSell = market.get(sellSymbol);
                            double totalRevenue = sellQty * stockToSell.getPrice();
                            userPortfolio.removeShares(sellSymbol, sellQty);
                            cashBalance += totalRevenue;
                            System.out.printf("Successfully sold %d shares of %s for $%.2f\n", sellQty, sellSymbol, totalRevenue);
                        } else {
                            System.out.println("You do not own enough shares of this stock to sell!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity format.");
                    }
                    break;

                case 5:
                    System.out.println("Exiting platform. Good luck with your internship submission!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please select between 1 and 5.");
            }
        }
    }
}
    
