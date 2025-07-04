# new_colour_guesser_game

## Overview
This project is a Java application that utilizes JavaFX for its graphical user interface. It serves as a color guessing game where users can interact with the application to guess colors based on provided hints.

## Project Structure
```
new_colour_guesser_game
├── src
│   └── nz
│       └── ac
│           └── auckland
│               └── se281
│                   └── Main.java
├── lib
│   └── javafx-sdk-21.0.7
│       └── lib
│           └── [javafx jars]
├── .vscode
│   └── launch.json
├── build.gradle
├── settings.gradle
└── README.md
```

## Prerequisites
- Java Development Kit (JDK) 11 or higher
- Gradle (for building the project)
- JavaFX SDK (included in the `lib` directory)

## Setup Instructions
1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd new_colour_guesser_game
   ```

2. **Install dependencies**:
   Ensure that the JavaFX SDK is located in the `lib/javafx-sdk-21.0.7/lib` directory.

3. **Build the project**:
   Use Gradle to build the project:
   ```
   ./gradlew build
   ```

4. **Run the application**:
   You can run the application using the provided launch configurations in the `.vscode/launch.json` file or directly from the command line:
   ```
   java --module-path lib/javafx-sdk-21.0.7/lib --add-modules javafx.controls,javafx.fxml -cp build/libs/new_colour_guesser_game.jar nz.ac.auckland.se281.Main
   ```

## Usage
- Launch the GUI version of the game to start guessing colors.
- Follow the on-screen instructions to play the game.

## Notes
- Ensure that your environment variables are set correctly to include the Java and Gradle binaries.
- For any issues or contributions, please refer to the project's issue tracker.