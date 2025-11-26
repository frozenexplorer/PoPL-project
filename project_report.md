# SysOps CLI: Project Report

**Course**: Principles of Programming Languages (PoPL) 2025  
**Project**: SysOps CLI - Functional-OO File System Analyzer  
**Date**: November 26, 2025  

---

## Executive Summary

The **SysOps CLI** is a robust, command-line interface application designed to simulate and analyze file system operations. Built strictly adhering to the **Functional-Object Oriented (Functional-OO)** paradigm, the system leverages a suite of custom-implemented data structures (`GenericList`, `Stack`, `Queue`, `Deque`, `PriorityQueueCustom`) to manage heterogeneous data, navigation history, job scheduling, and real-time analytics. This report details the architectural decisions, implementation strategies, and the role of Generative AI in accelerating the development process.

---

## 1. Introduction

Modern system administration requires tools that are both powerful and predictable. The SysOps CLI was conceived to meet this need by combining the state management capabilities of Object-Oriented Programming (OOP) with the declarative data processing power of Functional Programming.

### Objectives
*   **Paradigm Compliance**: Strictly enforce Functional-OO principles.
*   **Custom Infrastructure**: Utilize *only* custom container implementations for all data storage.
*   **Advanced Analytics**: Provide real-time insights (search, sort, aggregation) without external database dependencies.

---

## 2. System Architecture

The architecture is built on a "Separation of Concerns" philosophy, distinguishing clearly between *Data Structure* (OO) and *Data Transformation* (Functional).

### 2.1. The Functional-OO Paradigm
*   **Object-Oriented State**: The `FileSystemItem` abstract base class provides a polymorphic root for `FileItem` and `DirectoryItem`. This allows the system to treat files and folders uniformly while maintaining their distinct behaviors.
*   **Functional Processing**: The application logic in `SysOpsApp` avoids mutable state loops. Instead, it uses Java Streams to create declarative pipelines. For example, calculating total size is a reduction operation: `items.stream().mapToLong(getSize).reduce(0, sum)`.

### 2.2. Custom Container Integration
Each custom container was selected to solve a specific architectural challenge:

| Container | Role | Justification |
| :--- | :--- | :--- |
| **`GenericList<T>`** | Main Storage | Supports heterogeneous storage of `FileSystemItem` (files + dirs) with type safety. |
| **`Stack<T>`** | Navigation | LIFO structure perfectly models directory traversal (breadcrumbs). |
| **`Queue<T>`** | Job Scheduler | FIFO structure ensures fair ordering for simulated background tasks. |
| **`Deque<T>`** | History | Double-ended queue allows efficient addition/removal for a fixed-size command history buffer. |
| **`PriorityQueueCustom<T>`** | Analytics | Min-heap implementation provides $O(1)$ access to the largest files for the `top` command. |

---

## 3. Implementation Details

### 3.1. Core Features
The CLI supports standard operations (`ls`, `cd`, `back`) but enhances them with functional implementations. Directory changes, for instance, use stream filtering to locate targets, ensuring safety and readability.

### 3.2. Advanced Analytics & Aggregation
To transform the tool from a navigator to an analyzer, we implemented:
*   **Dynamic Sorting**: `sort <name|size>` uses `Comparator` lambdas to reorder views on the fly.
*   **Search & Filter**: `find` and `size_gt` utilize stream predicates to narrow down datasets.
*   **Aggregation**: `avg_size` and `counts` provide statistical summaries using functional reduction, demonstrating the power of the "What, not How" approach.

---

## 4. Development Methodology

The project followed a rapid, iterative development lifecycle over a two-day sprint.

*   **Phase 1: Foundation**: Defined the `FileSystemItem` hierarchy and integrated the `GenericList` and `Stack` containers.
*   **Phase 2: Core Logic**: Implemented the main REPL loop and navigation commands.
*   **Phase 3: Advanced Features**: Added `PriorityQueue` for analytics and `Queue` for job scheduling.
*   **Phase 4: Refinement**: Refactored imperative loops into functional streams and added aggregation commands.
# SysOps CLI: Project Report

**Course**: Principles of Programming Languages (PoPL) 2025  
**Project**: SysOps CLI - Functional-OO File System Analyzer  
**Date**: November 26, 2025  

---

## Executive Summary

The **SysOps CLI** is a robust, command-line interface application designed to simulate and analyze file system operations. Built strictly adhering to the **Functional-Object Oriented (Functional-OO)** paradigm, the system leverages a suite of custom-implemented data structures (`GenericList`, `Stack`, `Queue`, `Deque`, `PriorityQueueCustom`) to manage heterogeneous data, navigation history, job scheduling, and real-time analytics. This report details the architectural decisions, implementation strategies, and the role of Generative AI in accelerating the development process.

---

## 1. Introduction

Modern system administration requires tools that are both powerful and predictable. The SysOps CLI was conceived to meet this need by combining the state management capabilities of Object-Oriented Programming (OOP) with the declarative data processing power of Functional Programming.

### Objectives
*   **Paradigm Compliance**: Strictly enforce Functional-OO principles.
*   **Custom Infrastructure**: Utilize *only* custom container implementations for all data storage.
*   **Advanced Analytics**: Provide real-time insights (search, sort, aggregation) without external database dependencies.

---

## 2. System Architecture

The architecture is built on a "Separation of Concerns" philosophy, distinguishing clearly between *Data Structure* (OO) and *Data Transformation* (Functional).

### 2.1. The Functional-OO Paradigm
*   **Object-Oriented State**: The `FileSystemItem` abstract base class provides a polymorphic root for `FileItem` and `DirectoryItem`. This allows the system to treat files and folders uniformly while maintaining their distinct behaviors.
*   **Functional Processing**: The application logic in `SysOpsApp` avoids mutable state loops. Instead, it uses Java Streams to create declarative pipelines. For example, calculating total size is a reduction operation: `items.stream().mapToLong(getSize).reduce(0, sum)`.

### 2.2. Custom Container Integration
Each custom container was selected to solve a specific architectural challenge:

| Container | Role | Justification |
| :--- | :--- | :--- |
| **`GenericList<T>`** | Main Storage | Supports heterogeneous storage of `FileSystemItem` (files + dirs) with type safety. |
| **`Stack<T>`** | Navigation | LIFO structure perfectly models directory traversal (breadcrumbs). |
| **`Queue<T>`** | Job Scheduler | FIFO structure ensures fair ordering for simulated background tasks. |
| **`Deque<T>`** | History | Double-ended queue allows efficient addition/removal for a fixed-size command history buffer. |
| **`PriorityQueueCustom<T>`** | Analytics | Min-heap implementation provides $O(1)$ access to the largest files for the `top` command. |

---

## 3. Implementation Details

### 3.1. Core Features
The CLI supports standard operations (`ls`, `cd`, `back`) but enhances them with functional implementations. Directory changes, for instance, use stream filtering to locate targets, ensuring safety and readability.

### 3.2. Advanced Analytics & Aggregation
To transform the tool from a navigator to an analyzer, we implemented:
*   **Dynamic Sorting**: `sort <name|size>` uses `Comparator` lambdas to reorder views on the fly.
*   **Search & Filter**: `find` and `size_gt` utilize stream predicates to narrow down datasets.
*   **Aggregation**: `avg_size` and `counts` provide statistical summaries using functional reduction, demonstrating the power of the "What, not How" approach.

---

## 4. Development Methodology

The project followed a rapid, iterative development lifecycle over a two-day sprint.

*   **Phase 1: Foundation**: Defined the `FileSystemItem` hierarchy and integrated the `GenericList` and `Stack` containers.
*   **Phase 2: Core Logic**: Implemented the main REPL loop and navigation commands.
*   **Phase 3: Advanced Features**: Added `PriorityQueue` for analytics and `Queue` for job scheduling.
*   **Phase 4: Refinement**: Refactored imperative loops into functional streams and added aggregation commands.

**Challenges**: Integrating custom containers with Java's Stream API was a hurdle, as our containers didn't natively support streams.
**Solution**: We patched the `ListContainer` base class to expose a `stream()` method, bridging the gap between our custom structures and Java's functional libraries.

---

## 5. GenAI Usage & Prompt Engineering

To accelerate development while maintaining strict architectural control, Generative AI was utilized as a **technical consultant** throughout the entire project lifecycle. The following chronological log demonstrates the high-level design discussions and "intelligent prompts" that shaped the project:

### 5.1. Phase 1: Ideation & Concept Selection
**Goal**: Identify a project idea that naturally fits the assignment's constraints (Functional-OO + specific containers).
> **Prompt**: "I need to build a Java CLI application for a PoPL assignment. It must use a custom `GenericList`, `Stack`, `Queue`, `Deque`, and `PriorityQueue`. It must also strictly follow Functional-OO principles. Suggest 4 distinct project ideas that would *require* these specific data structures naturally, rather than forcing them in."
*   **Outcome**: The AI proposed "LogInsight", "FileScout", "StockSim", and "GradeBook Pro". We selected **"SysOps CLI"** (an evolution of FileScout) because file systems inherently map to trees (Lists), navigation maps to Stacks, and job scheduling maps to Queues.

### 5.2. Phase 2: Architecture & Core Design
**Goal**: Design a polymorphic data model to handle heterogeneous lists (Files + Directories).
> **Prompt**: "I need to design a polymorphic file system model in Java that supports heterogeneous lists. Suggest an abstract base class `FileSystemItem` that can support both `File` and `Directory` types. Ensure the design adheres to the Open/Closed principle so we can add new item types (like Symlinks) later without modifying the consumer logic."
*   **Outcome**: This led to the creation of the `FileSystemItem` abstract class, allowing `GenericList<FileSystemItem>` to store mixed content safely, satisfying the "Heterogeneous List" requirement.

### 5.3. Phase 3: Functional Refactoring
**Goal**: Replace imperative logic with declarative functional pipelines.
> **Prompt**: "Refactor this imperative `while` loop for directory traversal into a functional stream pipeline. I want to use `map` to extract file sizes and `reduce` to calculate the total, avoiding mutable state and side effects. The goal is to strictly follow the Functional-OO paradigm."
*   **Outcome**: This transformed the `stats` command from a 10-line loop into a concise, declarative stream operation: `items.stream().mapToLong(FileSystemItem::getSize).reduce(0, Long::sum)`.

### 5.4. Phase 4: Advanced Features & Optimization
**Goal**: Implement efficient sorting and filtering without external databases.
> **Prompt**: "The current sorting implementation for finding the largest file is $O(n \log n)$. Propose a heap-based solution using a custom `Comparator` and `PriorityQueue` to maintain only the top $K$ largest files. I need $O(1)$ access time for the largest element."
*   **Outcome**: This drove the decision to use `PriorityQueueCustom` for the `top` command, significantly optimizing performance for large directories compared to a full sort.

### 5.5. Phase 5: Aggregation & Final Polish
**Goal**: Add statistical insights and ensure professional documentation.
> **Prompt**: "I need to add aggregation commands to the CLI. Suggest implementation logic for `avg_size` and `counts` (file vs dir) using Java Streams. Also, help me structure a formal project report that covers the architecture, implementation details, and this prompt engineering strategy."
*   **Outcome**: Implemented `avg_size` and `counts` using stream reductions and filters. The project report was structured to professionally document the entire engineering process.


---

## 6. Conclusion

The SysOps CLI successfully demonstrates that custom data structures and modern functional programming paradigms can coexist to create clean, maintainable, and powerful software. The project meets all functional requirements and stands as a strong example of Functional-OO design.
