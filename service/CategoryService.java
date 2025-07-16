package service;

import structure.MyLinkedList;
import java.io.*;

public class CategoryService {
    private static final String FILE_PATH = "data/categories.txt";
    private static MyLinkedList categories = new MyLinkedList();

    public static void loadCategoriesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    categories.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load categories: " + e.getMessage());
        }
    }

    public static MyLinkedList getCategories() {
        return categories;
    }

    public static String selectCategoryFromList() {
        System.out.println("Available Categories:");
        final String[] selected = new String[100];
        final int[] index = {0};

        categories.forEach(new MyLinkedList.MyLinkedListConsumer() {
            public void accept(String data) {
                selected[index[0]] = data;
                System.out.println((index[0] + 1) + ". " + data);
                index[0]++;
            }
        });

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int choice = -1;
        while (choice < 1 || choice > index[0]) {
            try {
                System.out.print("Select a category by number: ");
                choice = Integer.parseInt(reader.readLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return selected[choice - 1];
    }

    public static void addCategory() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter new category: ");
            String newCategory = reader.readLine().trim();
            if (categories.contains(newCategory)) {
                System.out.println("Category already exists.");
                return;
            }
            categories.add(newCategory);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                writer.write(newCategory);
                writer.newLine();
            }
            System.out.println("Category added.");
        } catch (IOException e) {
            System.out.println("Error reading input or saving category.");
        }
    }

    public static void viewAllCategories() {
        System.out.println("All Categories:");
        categories.printAll();
    }

    public static void searchCategory() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Search category: ");
            String term = reader.readLine().trim();
            System.out.println("Results:");
            categories.forEach(data -> {
                if (data.toLowerCase().contains(term.toLowerCase())) {
                    System.out.println("- " + data);
                }
            });
        } catch (IOException e) {
            System.out.println("Error reading search term.");
        }
    }
}
