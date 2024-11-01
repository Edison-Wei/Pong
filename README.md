# Pong
I plan to create the classic retro game Pong using Java and JavaFX to render the 
graphical components of the game.

**How To Play**

1. Click the 'Start' button to begin the game.
2. Use the mouse to control the blue paddle, every bounce from a paddle increases the balls speed.
3. Enjoy!


## Goals
- [x] Reuseable class objects
- [x] Use Threads to handle the games interactions (object movement and collision detection)
- [x] Implement Game play loop and Pause screen
- [x] Provide documentation to each function and messy calculations


### Outcomes from Pong:
- Gained knowledge and experience with Object Oriented Programming (OOP) and Data Structures
- Design a user-friendly interface and simple experience
- Debugging incorrect responses from collision and UI issues
- Game Physics: Controlling the paddles movement of both player and bot and balls movements


## Setup
1. To begin, download the 'Pong.jar' file that contains the compiled source files
or download/clone this repository on your local machine. 

2. Ensure you have Java installed on your system. Try ``` java -version ``` in the terminal to check.
If not, you can download it from the [Java Website](https://www.java.com/). Any version will work for the application to run correctly.

3. Then, Head over to the [JavaFx website](https://openjfx.io/) and download version 21.0.2 of the SDK for your OS (Windows, Mac, Linux).

4. Use the following command to compile and run the application.
``` command
java --module-path "/Path/To/javafx-sdk-21.0.2/lib" --add-modules javafx.controls,javafx.fxml -jar Pong.jar
```
Make sure to replace '/Path/To/' from your home directory to the file location.

5. You are all set. Enjoy, the game!!