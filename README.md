# SysOps CLI

**SysOps CLI** is a robust, terminal-based file system analysis and operations tool designed to demonstrate advanced **Functional-Object Oriented** programming paradigms in Java. It leverages a suite of custom-built data structures to provide efficient file management, real-time analytics, and job scheduling.

## Key Features

*   **Heterogeneous File Management**: Seamlessly handles both files and directories within a unified `GenericList` structure, demonstrating polymorphic data storage.
*   **Smart Navigation**: Implements a `Stack`-based navigation history, allowing users to traverse directory depths with a reliable "Back" functionality.
*   **Real-time Analytics**: Utilizes a `PriorityQueue` with a custom comparator to instantly identify and rank the largest files in any directory.
*   **Job Scheduling**: Features a `Queue`-based task processing system for simulating background file operations.
*   **Command History**: Tracks user interactions using a `Deque` to provide a history of executed commands.
*   **Functional Design**: Heavily utilizes Java Streams, lambdas, and functional interfaces for filtering, mapping, and reducing data.

## Architecture

The project is built upon a foundation of custom container implementations:

*   `GenericList<T>`: A flexible, array-based list supporting functional stream operations.
*   `Stack<T>`: LIFO structure for navigation context.
*   `Queue<T>`: FIFO structure for task scheduling.
*   `Deque<T>`: Double-ended queue for history management.
*   `PriorityQueueCustom<T>`: Heap-based priority queue for ranking.

## Installation & Usage

### Prerequisites
*   Java Development Kit (JDK) 8 or higher.
*   Make (optional, for using the Makefile).

### Building and Running

**Using Make:**
```bash
make run
```

**Using Bash Script:**
```bash
./run.sh
```

**Manual Compilation:**
```bash
javac -d bin src/main/java/com/containers/*.java src/main/java/com/sysops/*.java
java -cp bin com.sysops.SysOpsApp
```

## Commands

| Command | Description |
| :--- | :--- |
| `ls` / `list` | List contents of the current directory. |
| `cd <dir>` | Navigate into a subdirectory. |
| `back` | Return to the previous directory. |
| `top` | Show the largest file in the current view. |
| `queue <file>` | Add a file to the processing job queue. |
| `process` | Process the next job in the queue. |
| `history` | Show the last 10 commands. |
| `stats` | Display total size of files in the current view. |
| `exit` | Exit the application. |

## License
This project is part of the PoPL 2025 Coursework.
