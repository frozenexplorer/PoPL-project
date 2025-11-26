package com.sysops;

import com.containers.*;
import java.io.File;
import java.util.Scanner;
import java.util.Date;
import java.util.Comparator;

public class SysOpsApp {
    // 1. Heterogeneous List: Stores current view
    private static GenericList<FileSystemItem> currentDirectoryItems = new GenericList<>();

    // 2. Stack: Navigation History (Stores the previous File directory)
    private static Stack<File> navigationStack = new Stack<>();

    // Track current real directory
    private static File currentDir;

    // 3. Deque: Command History
    private static Deque<String> commandHistory = new Deque<>();

    // 4. PriorityQueue: Top Largest Files (Maintained for current view)
    private static PriorityQueueCustom<FileItem> largestFiles = new PriorityQueueCustom<>(
            (f1, f2) -> Long.compare(f2.getSize(), f1.getSize()));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to SysOps CLI v2.0");
        System.out.println("Type 'help' for commands.");

        // Initialize with real current directory
        currentDir = new File(System.getProperty("user.dir"));
        loadDirectory(currentDir);

        while (true) {
            System.out.print("\n" + currentDir.getAbsolutePath() + "> ");
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

    private static void loadDirectory(File dir) {
        currentDirectoryItems = new GenericList<>();
        File[] files = dir.listFiles();

        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    // For directories, we don't know the size or children count without recursion,
                    // so we'll just store 0/empty for now to keep it fast.
                    currentDirectoryItems.add(new DirectoryItem(f.getName(), 0, new Date(f.lastModified())));
                } else {
                    currentDirectoryItems.add(new FileItem(f.getName(), f.length(), new Date(f.lastModified()),
                            getExtension(f.getName())));
                }
            }
        }
        refreshLargestFiles();
    }

    private static String getExtension(String name) {
        int i = name.lastIndexOf('.');
        if (i > 0) {
            return name.substring(i + 1);
        }
        return "";
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
        System.out.println("Listing contents of " + currentDir.getName() + ":");
        currentDirectoryItems.stream()
                .forEach(System.out::println);
    }

    private static void findItems(String query) {
        System.out.println("Searching for '" + query + "':");
        long count = currentDirectoryItems.stream()
                .filter(item -> item.getName().contains(query))
                .peek(System.out::println) // Print matches as they are found
                .count();

        if (count == 0) {
            System.out.println("Not found.");
        }
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
        File target = new File(currentDir, dirName);
        if (target.exists() && target.isDirectory()) {
            navigationStack.push(currentDir);
            currentDir = target;
            loadDirectory(currentDir);
            System.out.println("Entered directory: " + dirName);
        } else {
            System.out.println("Directory not found: " + dirName);
        }
    }

    private static void goBack() {
        if (!navigationStack.isEmpty()) {
            currentDir = navigationStack.pop();
            loadDirectory(currentDir);
            System.out.println("Returned to parent directory.");
            return;
        }

        File parent = currentDir.getParentFile();
        if (parent != null) {
            currentDir = parent;
            loadDirectory(currentDir);
            System.out.println("Returned to parent directory.");
        } else {
            System.out.println("Already at system root.");
        }
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
            File target = new File(currentDir, path);
            if (!target.exists() || !target.isDirectory()) {
                System.out.println("Directory not found: " + path);
                return;
            }

            // Temporarily load target directory
            targetList = new GenericList<>();
            File[] files = target.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        targetList.add(new DirectoryItem(f.getName(), 0, new Date(f.lastModified())));
                    } else {
                        targetList.add(new FileItem(f.getName(), f.length(), new Date(f.lastModified()),
                                getExtension(f.getName())));
                    }
                }
            }
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
