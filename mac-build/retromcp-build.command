#!/bin/bash

# Get the directory one level up from the current script
SCRIPT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

# Set the path to the Java executable explicitly
JAVA_EXECUTABLE="$SCRIPT_DIR/mac-build/zulu-8.jdk/Contents/Home/bin/java"

# Set the path to the JAR file one level above the script directory
JAR_FILE="$SCRIPT_DIR/../RetroMCP-Java-Maxxx-all.jar"

# Check if the Java executable exists
if [ ! -f "$JAVA_EXECUTABLE" ]; then
  echo "[ERROR]: Java executable not found at $JAVA_EXECUTABLE"
  read -p "Press Enter to exit."
  exit 1
fi

# Check if the JAR file exists
if [ ! -f "$JAR_FILE" ]; then
  echo "[ERROR]: JAR file not found at $JAR_FILE"
  read -p "Press Enter to exit."
  exit 1
fi

# Change to the directory one level above the script directory
cd "$SCRIPT_DIR/.."

# Run the Java command with the 'build' argument
echo "Running build command: $JAVA_EXECUTABLE -jar $JAR_FILE build"
"$JAVA_EXECUTABLE" -jar "$JAR_FILE" build

# Capture the exit status of the command
EXIT_STATUS=$?

# Check if the command was successful
if [ $EXIT_STATUS -ne 0 ]; then
  echo "[ERROR]: Build finished with errors! Exit status: $EXIT_STATUS"
else
  echo "Build 100% [==========] Finished!"
fi

# Keep the terminal window open after execution
read -p "Press Enter to exit."
