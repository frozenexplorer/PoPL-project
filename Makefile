# Makefile for SysOps CLI

# Variables
JAVAC = javac
JAVA = java
SRC_DIR = src/main/java
BIN_DIR = bin
MAIN_CLASS = com.sysops.SysOpsApp
SOURCES = $(shell find $(SRC_DIR) -name "*.java")

# Default target
all: compile

# Compile the Java source files
compile:
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SRC_DIR)/com/containers/*.java $(SRC_DIR)/com/sysops/*.java

# Run the application
run: compile
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

# Clean build artifacts
clean:
	rm -rf $(BIN_DIR)

# Phony targets
.PHONY: all compile run clean
