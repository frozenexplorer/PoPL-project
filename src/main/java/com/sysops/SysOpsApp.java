package com.sysops;

import com.containers.*;
import java.util.Scanner;
import java.util.Date;
import java.util.Comparator;

public class SysOpsApp {
    // 1. Heterogeneous List: Stores both Files and Directories
    private static GenericList<FileSystemItem> currentDirectoryItems = new GenericList<>();

    // 2. Stack: Navigation History (Breadcrumbs)
    private static Stack<DirectoryItem> navigationStack = new Stack<>();

    // 3. Queue: Job Processing Queue
    private static Queue<String> jobQueue = new Queue<>();

    // 4. Deque: Command History
    private static Deque<String> commandHistory = new Deque<>();

    // 5. PriorityQueue: Top Largest Files
    // Custom Comparator for PriorityQueue (Max Heap based on size)
    private static PriorityQueueCustom<FileItem> largestFiles = new PriorityQueueCustom<>(
            (f1, f2) -> Long.compare(f2.getSize(), f1.getSize()));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to SysOps CLI v1.0");
        System.out.println("Type 'help' for commands.");

        // Initialize with some dummy data
        loadDummyData();

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                continue;

            // Add to history (Deque)
            commandHistory.addLast(input);
            if (commandHistory.size() > 10)
                commandHistory.removeFirst();

            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();
            // DEBUG: Print received command
            // System.out.println("DEBUG: Received command: '" + command + "'");

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
                case "queue":
                    if (parts.length < 2)
                        System.out.println("Usage: queue <filename>");
                    else
                        queueJob(parts[1]);
                    break;
                case "process":
                    processJob();
                    break;
                case "process_all":
                    processAllJobs();
                    break;
                case "avg_size":
                    showAverageSize();
                    break;
                case "counts":
                    showItemCounts();
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
                case "max_size":
                    showMaxSize();
                    break;
                case "stats":
                    showStats();
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

    private static void loadDummyData() {
        currentDirectoryItems = new GenericList<>();
        largestFiles = new PriorityQueueCustom<>((f1, f2) -> Long.compare(f2.getSize(), f1.getSize()));

        // Add heterogeneous items
        FileItem f1 = new FileItem("report.pdf", 10240, new Date(), "pdf");
        FileItem f2 = new FileItem("image.png", 5120, new Date(), "png");
        FileItem f3 = new FileItem("notes.txt", 120, new Date(), "txt");
        FileItem f4 = new FileItem("video.mp4", 500000, new Date(), "mp4");
        DirectoryItem d1 = new DirectoryItem("documents", 0, new Date(), 5);
        DirectoryItem d2 = new DirectoryItem("photos", 0, new Date(), 10);

        currentDirectoryItems.add(f1);
        currentDirectoryItems.add(f2);
        currentDirectoryItems.add(f3);
        currentDirectoryItems.add(f4);
        currentDirectoryItems.add(d1);
        currentDirectoryItems.add(d2);

        // Populate PriorityQueue with files only
        largestFiles.add(f1);
        largestFiles.add(f2);
        largestFiles.add(f3);
        largestFiles.add(f4);
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

    private static void showMaxSize() {
        long max = currentDirectoryItems.stream()
                .mapToLong(FileSystemItem::getSize)
                .reduce(0, Long::max);
        System.out.println("Max File Size: " + max + " bytes");
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
            navigationStack.push((DirectoryItem) found);
            System.out.println("Entered directory: " + dirName);
            // In a real app, we would load new items here.
            // For demo, we just clear and reload dummy data to simulate change.
            loadDummyData();
        } else {
            System.out.println("Directory not found: " + dirName);
        }
    }

    private static void goBack() {
        if (navigationStack.isEmpty()) {
            System.out.println("Already at root.");
            return;
        }
        DirectoryItem prev = navigationStack.pop();
        System.out.println("Returned from " + prev.getName());
    }

    private static void showTopFiles() {
        System.out.println("Top Largest Files (PriorityQueue):");
        // Peek doesn't remove, but to show all we might need to iterate or just show
        // top 1
        FileItem top = largestFiles.peek();
        if (top != null) {
            System.out.println("Largest: " + top);
        } else {
            System.out.println("No files.");
        }
    }

    private static void queueJob(String filename) {
        jobQueue.enqueue(filename);
        System.out.println("Added " + filename + " to processing queue.");
    }

    private static void processJob() {
        String job = jobQueue.dequeue();
        if (job != null) {
            System.out.println("Processing " + job + "... Done.");
        } else {
            System.out.println("Queue is empty.");
        }
    }

    private static void processAllJobs() {
        System.out.println("Processing all jobs...");
        while (!jobQueue.isEmpty()) {
            processJob();
        }
    }

    private static void showHistory() {
        System.out.println("Command History (Last 10):");
        commandHistory.stream().forEach(System.out::println);
    }

    // Functional-OO: Aggregation using reduce
    private static void showStats() {
        long totalSize = currentDirectoryItems.stream()
                .mapToLong(FileSystemItem::getSize)
                .reduce(0, Long::sum);

        System.out.println("Total Size in current view: " + totalSize + " bytes");
    }

    private static void showAverageSize() {
        long totalSize = currentDirectoryItems.stream()
                .mapToLong(FileSystemItem::getSize)
                .reduce(0, Long::sum);
        long count = currentDirectoryItems.stream().count();
        if (count > 0) {
            System.out.println("Average Size: " + (totalSize / count) + " bytes");
        } else {
            System.out.println("Average Size: 0 bytes");
        }
    }

    private static void showItemCounts() {
        long fileCount = currentDirectoryItems.stream()
                .filter(item -> item instanceof FileItem)
                .count();
        long dirCount = currentDirectoryItems.stream()
                .filter(item -> item instanceof DirectoryItem)
                .count();
        System.out.println("Files: " + fileCount + ", Directories: " + dirCount);
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
        System.out.println("  queue <file>    - Add file to job queue");
        System.out.println("  process         - Process next job");
        System.out.println("  process_all     - Process all jobs");
        System.out.println("  avg_size        - Show average file size");
        System.out.println("  max_size        - Show maximum file size");
        System.out.println("  counts          - Show count of files and dirs");
        System.out.println("  history         - Show command history");
        System.out.println("  stats           - Show total size");
        System.out.println("  exit            - Quit");
    }
}
