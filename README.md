# Tetrisimplified V2
 Tetris, but with additional features, in Java and downloadable!

## Steps on how to build and run the game
Please ensure that Java Development Kit (or JDK) 17 or later is installed on your computer.

If you simply want to run the game as is, open TetrisGame.jar in the TetrisGame package.

Otherwise if you wish to make your own modifications and test it out, please follow these instructions on how to properly build and run the application.

1. Navigate to the project directory:
```
cd TetrisGame
```

2. Compile the Java source files:
```
javac -d out src/*.java
```

3. Create the new JAR file:
```
jar cfm TetrisGame.jar manifest.txt -C out .
```

4. Run the game:
If on Mac or Linux, make the shell script executable:
```
chmod +x launch.sh
```
Then run it:
```
./launch.sh
```
But if on Windows, simply run the batch script:
```
./launch.sh
```
Otherwise, simply open the JAR file from File Explorer.

![Screenshot 2024-07-11 094555](https://github.com/aallendarmawan/Tetrisimplified-V2/assets/110092516/6e009161-e1d5-4222-88ae-9b19924f1d77)

Have fun!
