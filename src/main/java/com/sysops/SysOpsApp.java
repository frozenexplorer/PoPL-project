package com.sysops;

import com.containers.*;
import java.util.Scanner;
import java.util.Date;
import java.util.Comparator;

public class SysOpsApp {
    // 1. Heterogeneous List: Stores current view
    private static GenericList<FileSystemItem> currentDirectoryItems = new GenericList<>();

    // 2. Stack: Navigation History (Stores the LIST of items of the parent
    // directory)
    // Actually, to support "real" navigation, we should store the DirectoryItem
    // itself
    // so we can go back to its parent? Or just store the previous list?
    // Let's store the previous list for simplicity in restoring the view.
    private static Stack<GenericList<FileSystemItem>> navigationStack = new Stack<>();

    // Track current path name for display
    private static Stack<String> pathStack = new Stack<>();

    // 3. Deque: Command History
    private static Deque<String> commandHistory = new Deque<>();

    // 4. PriorityQueue: Top Largest Files (Maintained for current view)
    private static PriorityQueueCustom<FileItem> largestFiles = new PriorityQueueCustom<>(
            (f1, f2) -> Long.compare(f2.getSize(), f1.getSize()));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to SysOps CLI v2.0");
        System.out.println("Type 'help' for commands.");

        // Initialize with nested data
        loadDummyData();
        pathStack.push("/");

        while (true) {
            System.out.print("\n" + getPathString() + "> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                continue;

            // Add to history (Deque)
            commandHistory.addLast(input);
            if (commandHistory.size() > 10)
                commandHistory.removeFirst();

            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();

            switch (command) {
                case "ls":
                case "list":
                    listItems();
                    break;
                case "cd":
                    if (parts.length < 2)
                        System.out.println("Usage: cd <dirname>");
                    else
                        changeDirectory(parts[1]);
                    break;
                case "back":
                    goBack();
                    break;
                case "top":
                    showTopFiles();
                    break;
                case "find":
                    if (parts.length < 2)
                        System.out.println("Usage: find <name>");
                    else
                        findItems(parts[1]);
                    break;
                case "size_gt":
                    if (parts.length < 2)
                        System.out.println("Usage: size_gt <bytes>");
                    else
                        filterBySize(parts[1], true);
                    break;
                case "size_lt":
                    if (parts.length < 2)
                        System.out.println("Usage: size_lt <bytes>");
                    else
                        filterBySize(parts[1], false);
                    break;
                case "sort":
                    if (parts.length < 2)
                        System.out.println("Usage: sort <name|size>");
                    else
                        sortItems(parts[1]);
                    break;
                case "analyse":
                case "analyze":
                    if (parts.length < 2)
                        analyzePath(null); // Analyze current
                    else
                        analyzePath(parts[1]); // Analyze target
                    break;
                case "history":
                    showHistory();
                    break;
                case "help":
                    printHelp();
                    break;
                case "exit":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Unknown command.");
            }
        }
    }

    private static String getPathString() {
        // Reconstruct path from stack (this is a bit hacky with Stack but works for
        // display)
        // Since our Stack is LIFO, we can't easily iterate bottom-up without popping.
        // For simplicity, we'll just show the top.
        if (pathStack.isEmpty())
            return "/";
        return pathStack.peek();
    }

    private static void loadDummyData() {
        currentDirectoryItems = new GenericList<>();
        largestFiles = new PriorityQueueCustom<>((f1, f2) -> Long.compare(f2.getSize(), f1.getSize()));

        // Root items
        FileItem f1 = new FileItem("report.pdf", 10240, new Date(), "pdf");
        FileItem f2 = new FileItem("image.png", 5120, new Date(), "png");

        DirectoryItem d1 = new DirectoryItem("documents", 0, new Date());
        DirectoryItem d2 = new DirectoryItem("photos", 0, new Date());

        // Populate documents
        d1.addChild(new FileItem("resume.docx", 2048, new Date(), "docx"));
        d1.addChild(new FileItem("budget.xlsx", 4096, new Date(), "xlsx"));

        // Populate photos
        d2.addChild(new FileItem("vacation.jpg", 200000, new Date(), "jpg"));
        d2.addChild(new FileItem("profile.jpg", 50000, new Date(), "jpg"));

        currentDirectoryItems.add(f1);
        currentDirectoryItems.add(f2);
        currentDirectoryItems.add(d1);
        currentDirectoryItems.add(d2);

        refreshLargestFiles();
    }

    private static void refreshLargestFiles() {
        largestFiles = new PriorityQueueCustom<>((f1, f2) -> Long.compare(f2.getSize(), f1.getSize()));
        currentDirectoryItems.stream()
                .filter(item -> item instanceof FileItem)
                .map(item -> (FileItem) item)
                .forEach(largestFiles::add);
    }

    // Functional-OO: Using Streams (lambdas) for display
    private static void listItems() {
        System.out.println("Listing contents:");
        currentDirectoryItems.stream()
                .forEach(System.out::println);
    }

    private static void findItems(String query) {
        System.out.println("Searching for '" + query + "':");
        currentDirectoryItems.stream()
                .filter(item -> item.getName().contains(query))
                .forEach(System.out::println);
    }

    private static void filterBySize(String sizeStr, boolean greaterThan) {
        try {
            long limit = Long.parseLong(sizeStr);
            String op = greaterThan ? "larger" : "smaller";
            System.out.println("Files " + op + " than " + limit + " bytes:");
            currentDirectoryItems.stream()
                    .filter(item -> greaterThan ? item.getSize() > limit : item.getSize() < limit)
                    .forEach(System.out::println);
        } catch (NumberFormatException e) {
            System.out.println("Invalid size format.");
        }
    }

    private static void sortItems(String criterion) {
        if (criterion.equals("name")) {
            currentDirectoryItems.sort(Comparator.comparing(FileSystemItem::getName));
            System.out.println("Sorted by name.");
        } else if (criterion.equals("size")) {
            currentDirectoryItems.sort(Comparator.comparingLong(FileSystemItem::getSize));
            System.out.println("Sorted by size.");
        } else {
            System.out.println("Invalid sort criterion. Use 'name' or 'size'.");
        }
        listItems();
    }

    private static void changeDirectory(String dirName) {
        // Find directory using functional stream filter
        FileSystemItem found = currentDirectoryItems.stream()
                .filter(item -> item instanceof DirectoryItem && item.getName().equals(dirName))
                .findFirst()
                .orElse(null);

        if (found != null) {
            DirectoryItem dir = (DirectoryItem) found;

            // Save current state
            navigationStack.push(currentDirectoryItems);
            pathStack.push(dirName);

            // Switch to new state
            currentDirectoryItems = dir.getChildren();
            refreshLargestFiles();

            System.out.println("Entered directory: " + dirName);
        } else {
            System.out.println("Directory not found: " + dirName);
        }
    }

    private static void goBack() {
        if (navigationStack.isEmpty()) {
            System.out.println("Already at root.");
            return;
        }
        // Restore previous state
        currentDirectoryItems = navigationStack.pop();
        pathStack.pop();
        refreshLargestFiles();

        System.out.println("Returned to parent directory.");
    }

    private static void showTopFiles() {
        System.out.println("Top Largest Files (PriorityQueue):");
        FileItem top = largestFiles.peek();
        if (top != null) {
            System.out.println("Largest: " + top);
        } else {
            System.out.println("No files.");
        }
    }

    private static void showHistory() {
        System.out.println("Command History (Last 10):");
        commandHistory.stream().forEach(System.out::println);
    }

    // Consolidated Analysis Command
    private static void analyzePath(String path) {
        GenericList<FileSystemItem> targetList;

        if (path == null) {
            targetList = currentDirectoryItems;
            System.out.println("Analyzing current directory...");
        } else {
            // Find the target directory in current view
            FileSystemItem found = currentDirectoryItems.stream()
                    .filter(item -> item instanceof DirectoryItem && item.getName().equals(path))
                    .findFirst()
                    .orElse(null);

            if (found == null) {
                System.out.println("Directory not found: " + path);
                return;
            }
            targetList = ((DirectoryItem) found).getChildren();
            System.out.println("Analyzing " + path + "...");
        }

        // 1. Counts
        long fileCount = targetList.stream().filter(item -> item instanceof FileItem).count();
        long dirCount = targetList.stream().filter(item -> item instanceof DirectoryItem).count();

        // 2. Avg Size
        long totalSize = targetList.stream().mapToLong(FileSystemItem::getSize).reduce(0, Long::sum);
        long totalCount = targetList.stream().count();
        double avgSize = totalCount > 0 ? (double) totalSize / totalCount : 0.0;

        // 3. Max Size
        long maxSize = targetList.stream().mapToLong(FileSystemItem::getSize).reduce(0, Long::max);

        System.out.println("--- Analysis Report ---");
        System.out.println("Files: " + fileCount);
        System.out.println("Directories: " + dirCount);
        System.out.println("Total Size: " + totalSize + " bytes");
        System.out.printf("Average Size: %.2f bytes%n", avgSize);
        System.out.println("Max File Size: " + maxSize + " bytes");
        System.out.println("-----------------------");
    }

    private static void printHelp() {
        System.out.println("Available Commands:");
        System.out.println("  ls / list       - List files");
        System.out.println("  cd <dir>        - Enter directory");
        System.out.println("  back            - Go to previous directory");
        System.out.println("  top             - Show largest file");
        System.out.println("  find <name>     - Search by name");
        System.out.println("  size_gt <bytes> - Filter by size (greater than)");
        System.out.println("  size_lt <bytes> - Filter by size (less than)");
        System.out.println("  sort <crit>     - Sort by 'name' or 'size'");
        System.out.println("  analyse [path]  - Show statistics (Counts, Avg, Max)");
        System.out.println("  history         - Show command history");
        System.out.println("  exit            - Quit");
    }
}
