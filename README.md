# Programming Assignment: Maxwell's Demon

- [Goals](#goals)
- [Description](#description)
- [Specification and requirements](#specification-and-requirements)
   - [Specification](#specification)
   - [Requirements](#requirements)

## Goals

To complete this assignment, you will
- Implement a graphical user interface (GUI)
   - Design the appearance of the GUI
   - Implement the design using Swing containers and components
- Implement an event-driven program
   - Choose apropriate Swing event generators
   - Implement controllers using Swing interfaces
- Model and display a simple physical system
   - Provide a data model for simple particle dynamics
   - Display the state of the model in a Swing view component

## Description

Graphical user interfaces (GUIs) are present in many computing settings, such as the mouse-based
desktop of a personal computer, the swipe-based interface of a smartphone, and the touch-based
interfaces on ATMs or grocery store checkout scanners. These GUIs are seen as simpler (and
less intimidating) to use than their older command line interface (CLI) counterparts. 

Java was designed with graphical interfaces in mind, and provides tools for GUI construction and
associated event-driven programming through the Swing (and AWT) library. You will use these
libraries to create a simple computer game (complete with animation).

## Specification and requirements

The 19th century mathematical physicist, James Maxwell, is best known for developing a set of
four equations (known as Maxwell's equations) that describe classical electromagnetism. He also
made contributions to statistical thermodynamics, producing the Maxwell-Boltzman distribution for
ideal gas behavior. It was during this work that he conceived of a thought experiment in which
the second law of thermodynamics (entropy) might be violated.

> ... if we conceive of a being whose faculties are so sharpened that he can follow every molecure
> in its course, such a being, whose attributes are as essentially finite as our own, would be able
> to do what is impossible to us. For we have seen that molecules in a vessel full of air at
> uniform temperature are moving with velocities by no means uniform. Now let us suppose that such
> a vessel is divided into two portions, A and B, by a division in which there is a small hole,
> and that a being, who can see the individual molecules, opens and closes this hole, so as took
> allow only the swifter molecules to pass from A to B, and only the slower molecules to pass from
> B to A, in contradiction to the second law of thermodynamics.
- Bennet, Charles H. (November 1987). "Demons, Engines, and the Second Law" (PDF). Scientific
  American. 257(5): 108-116.
  
In other words, Maxwell envisioned a box split into two halves, with a door between the parts that
could be opened or closed by a "demon" who observed every particle in the box. By carefully opening
and closing the door at the correct moments, the demon could place all the fast-moving (i.e., hot)
particles on one side of the door and all the slow-moving (i.e., cold) particles on the other side,
reducing the entropy of the system.

### Specification

In this project you'll create a computer game in Java that lets the user play as Maxwell's demon.
You'll need to create a suitable data model (to store the state of the game); a GUI view to
display this state to the user; and controllers that respond to player inputs and requests as
they try to win.

The basic requirements of Maxwell's Demon are presented below; you are free to add additional
features beyond these (e.g., high score, play timer, awesome graphics) so long as they do not
break the core functionality required.

#### The Maxwell's Demon GUI

When a player begins a game of Maxwell's Demon, they should be presented with a graphical display
that includes all of the following components:
- A rectangular "play area" which is divided into two halves by a wall through the middle.
   - The wall must contain a "door" which spans (roughly) the middle third of the wall; the door
     should *not* take up the entire height/length of the wall.
   - The wall (and door) may be vertical (producing a left chamber and right chamber) or horizontal
     (producing a top chamber and bottom chamber) at your preference.
- Two "temperature" displays; one for each chamber of the play area
   - It should be clear from your layout which temperature is associated with which chamber
- Two buttons to control gameplay
   - A "reset" button which, when pressed, returns the game to its initial state (that is, as if
     the game was being opened and started for the first time)
   - An "add particles" button which, when pressed, introduces new particles into the game

The player controls the door (in the wall separating the chambers) using the mouse. When the mouse
button is *pressed* (that is, pushed and held), the door should be open; when the button is
*released* (that is, not pushed), the door should be closed. The player should be able to operate
the door by pressing or releasing anywhere in the play area.

#### Hot and cold particles

During a game of Maxwell's Demon, the player will see particles which animate within the play area.
These particles will be visually separated into hot (red) and cold (blue) particles, based on
their speed.
- A hot (red) particle should have an on-screen speed of between 4 and 6 cm/s
- A cold (blue) particle should have an on-screen speed of between 2 and 4 cm/s
(These values are more manageable than the realistic 200 m/s that the median particle is moving 
in your room right now).

The speed of a newly created particle should be chosen randomly from within the specified range;
e.g., not all hot particles will have exactly the same speed. Under the simplified kinematics of
this project, a particle will not change it speed (or color) after creation.

Particles should remain within the play area by reflecting (elastically) off of the boundary walls, 
the center wall, and the door (while closed). Particles do *not* need to reflect off of each other.

#### Starting particles and adding particles

At the start of a game (or after the reset button is pressed), there should be exactly four
particles in the game. Specifically, each chamber should contain both a hot particle and a cold
particle.

Whenever the add button is pressed, four new particles should be added to the game. Just as at the
start of the game, this should add a new hot particle and a new cold particle to both chambers.
Adding particles in this way should not affect any existing particles or their location (if the
game has been running for some time).

#### Computing the temperature of a chamber

You will need to compute and display the "temperature" of both chambers of the play area. In the
simplified kinetic model for this game, temperature should be calculated as the
*average of the squared speeds* of the particles. In other words, sum up the squared speed of all
particles in one chamber, then divide by the number of particles in the chamber.

(We are essentially ignoring physical units in this calculation, but otherwise adhering to real
physics; the temperature of an ideal gas is proportional to the average kinetic energy of the
particles. For those with an interest in numerical simulation of physical systems, see
[this Wikipedia article](http://en.wikipedia.org/wiki/Kinetic_theory_of_gases#Temperature_and_kinetic_energy).

#### A helpful Java method for determining screen resolution

As the Java2D Graphics object works in pixel-based coordinates and lengths, distances measured in
centimeters (and speeds in cm/s) need to be converted to pixels (and px/s). The number of pixels
in a centimeter varies from computer to computer, depending on your screen resolution!

The Java AWT class `java.awt.Toolkit` provides the following static method which returns the
resolution of the current device's screen in pixels per inch:
```
int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
```
(Naturally, you need to `import java.awt.Toolkit` to use the unqualified name above).

#### Source code files

You are required to provide at least one source code file, named `Maxwell.java`, which implements
the class `Maxwell`. This class must provide a `main` method which, when run, begins your game.

You are free to implement any additional classes you like to complete this project. These may be
(sensibly) named and organized in any way you see fit. In particular, `Maxwell` itself may either
serve as a wrapper around `main` (with no additional members) or may fill other roles as you see fit.                                            | **150**

