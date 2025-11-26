# Project Report: SysOps CLI Development

**Course**: Principles of Programming Languages (PoPL) 2025
**Project**: SysOps CLI - Functional-OO File System Analyzer
**Date**: November 26, 2025

## 1. Executive Summary

This report documents the design, development, and implementation of the **SysOps CLI**, a Java-based command-line tool. The primary objective was to architect a system that strictly adheres to **Functional-Object Oriented (Functional-OO)** principles while utilizing a specific set of custom data containers (`GenericList`, `Stack`, `Queue`, `Deque`, `PriorityQueueCustom`).

The resulting application successfully demonstrates the practical application of these custom structures in a real-world scenario (system operations), moving beyond theoretical exercises to a cohesive, interactive utility.

## 2. Design Philosophy & Architectural Decisions

The development process was driven by a "What-First" approach, focusing on the domain model and data flow before committing to implementation details.

### 2.1. The Functional-OO Paradigm
A key design decision was to decouple data storage from data processing.
*   **OO Component**: The `FileSystemItem` hierarchy (polymorphism) and the custom containers encapsulate the *state* and *structure*.
*   **Functional Component**: The `SysOpsApp` logic utilizes declarative pipelines (Java Streams, lambdas) for operations like `filter`, `map`, and `reduce`. This ensures that the "How" (iteration logic) is abstracted away, allowing the code to focus on the "What" (business logic).

### 2.2. Container Utilization Strategy
Instead of forcing containers into the project, the architecture was designed to make each container the *optimal* choice for its specific role:
*   **Heterogeneity (`GenericList`)**: We needed a single view for both files and folders. `GenericList<FileSystemItem>` was chosen to demonstrate type erasure and polymorphic storage capabilities.
*   **Context Management (`Stack`)**: Directory navigation is inherently a LIFO operation. A `Stack` was the natural choice to maintain the "breadcrumb" trail.
*   **Ranking (`PriorityQueue`)**: To implement the `top` feature efficiently ($O(1)$ access), a heap-based structure was selected over sorting the entire list ($O(n \log n)$) every time.
*   **Asynchronous Simulation (`Queue`)**: The job processing feature required strict FIFO ordering, necessitating a `Queue`.

## 3. Development Timeline

The project was executed over a two-day intensive sprint, following an iterative development lifecycle.

### Day 1: Architecture & Core Container Integration
*   **Morning**: Analyzed the assignment specifications and our container constraints. Mapped the required custom containers to potential system features.
*   **Afternoon**: Designed the `FileSystemItem` abstract base class and its concrete subclasses (`FileItem`, `DirectoryItem`). This established the data model.
*   **Evening**: Integrated the custom `GenericList` and `Stack` implementations. Built the initial CLI skeleton to handle basic `ls` and `cd` commands, verifying the heterogeneous list storage.

### Day 2: Advanced Features & Functional Refinement
*   **Morning**: Implemented the `PriorityQueue` logic with a custom `Comparator` for file sizes. This required fine-tuning the generic bounds to ensure type safety.
*   **Afternoon**: Added the `Queue` and `Deque` features for job scheduling and history tracking. Refactored the imperative loops in `listItems()` and `changeDirectory()` into functional streams to meet the Functional-OO requirement.
*   **Evening**: Comprehensive testing and verification. Wrote the `Makefile` and build scripts. Finalized documentation and code cleanup.

## 4. Conclusion

The SysOps CLI stands as a testament to the power of combining robust custom data structures with modern functional programming patterns. The project not only meets all functional requirements but does so with a clean, modular, and extensible architecture.
