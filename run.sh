#!/bin/bash

# SysOps CLI Runner Script

# Ensure bin directory exists
mkdir -p bin

echo "Compiling sources..."
javac -d bin src/main/java/com/containers/*.java src/main/java/com/sysops/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful. Starting SysOps CLI..."
    echo "------------------------------------------------"
    java -cp bin com.sysops.SysOpsApp
else
    echo "Compilation failed."
    exit 1
fi
