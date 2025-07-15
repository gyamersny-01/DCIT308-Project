package service;

import structure.MyCategorySet;

import java.io.*;
import java.util.Scanner;

public class CategoryService {
    private static final String FILE_PATH = "data/categories.txt";
    private static MyCategorySet categorySet = new MyCategorySet();

    public static void loadCategoriesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                categorySet.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("No existing categories to load.");
        }
    }

    public static void addCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter new category name: ");
        String category = scanner.nextLine().trim();
        if (category.isEmpty()) {
            System.out.println("Category name cannot be empty.");
            return;
        }
        if (categorySet.add(category)) {
            appendToFile(category);
            System.out.println("Category added successfully.");
        } else {
            System.out.println("Category already exists.");
        }
    }

    public static void viewAllCategories() {
        System.out.println("\n--- All Categories ---");
        categorySet.displayAll();
    }

    public static void searchCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category to search: ");
        String input = scanner.nextLine().trim();
        if (categorySet.contains(input)) {
            System.out.println("Category found: " + input);
        } else {
            System.out.println("Category not found.");
        }
    }

    private static void appendToFile(String category) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(category);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing category to file: " + e.getMessage());
        }
    }
}