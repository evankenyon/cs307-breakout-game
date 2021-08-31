game
====

This project implements the game of Breakout.

Name: Evan Kenyon

### Timeline

Start Date: 8/26/21

Finish Date: 8/31/21

Hours Spent: 15

### Tutorial and other Resources Used
* example_animation course gitlab repo for basic Breakout class setup
* All other resources derived from stackoverflow or official documentation, attributed as necessary in inline comments

### Resource Attributions
* All resource attributions are in inline comments

### Running the Program

Main class: Breakout

Data files needed: None

Key/Mouse inputs:
* Right arrow: move the paddle to the right
* Left arrow: move the paddle to the left
* Space bar: start the ball's movement at the beginning of the game or after losing a life

Known Bugs: 
* Ball can get glitched inside of paddle when the player moves the paddle into the inside of the ball (if this happens, move the 
paddle around to get it unstuck)
* Ball can reverse y direction instead of x direction as desired when it hits the corner of a brick


### Notes/Assumptions
* I expect the program to handle this error without crashing: TSM AdjustCapsLockLEDForKeyTransitionHandling - _ISSetPhysicalKeyboardCapsLockLED Inhibit
* The only decision I made that I feel the need to explain was my algorithm for generating bricks. Basically, I vaguely remembered
playing breakout type games where there were some amount of empty rows and/or columns so that the ball would bounce around within the
bricks instead of constantly going back and forth from the paddle. So, my algorithm randomly generates x and y coordinates (separately)
to block out, with a given frequency (that's input as an argument to the Bricks class) since that's how I wanted to set up my own game.
* Any small nontrivial decision/assumption/simplification I made was explained in inline comments

### Impressions
I found this assignment to be a great one to set course expectations and to introduce us to JavaFX/reintroeduce us to Java
(I personally have barely coded in Java since CS 201). I think that giving us an individual assignment that's submitted solely for
feedback to start the course will help greatly with future assignments since we will know what to improve upon. Also, I did not
feel pressure to do things "the right way" (although of course I tried my best), so I am more so looking forward to learning 
how to do better in future assignments than concerned with what grade I'd get, as I would be if this was for a grade. Overall,
I wouldn't do anything to change this assignment in the future except open up the gitlab repos earlier.
