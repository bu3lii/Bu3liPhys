
# Java 2D Physics Engine

This is a WIP 2D physics engine written in Java (planned JavaFX UI)

## Features So Far
- **Vector2D class**: Basic vector math
- **PhysicsObject class**: Represents physical objects with:
  - Mass
  - Position
  - Velocity
  - Acceleration
  - Ability to apply external forces
- **World class**:
  - Manages a list of physics objects
  - Applies gravity
  - Steps the simulation forward in time
  - Window Border Collisions
  - Object to Object Collisions (Early Implementation)
- **Visualization**:
  - Click to Spawn Object
  - Double Click and drag to Apply Custom Force
  - Arrow to force direction
  - Label using arbitrary units for force
  - Coordinate Axis

## Early-0.2.0 Changelog
Border collisions are handled correctly with no issue
Object to Object collision now works accurately as compared to Early-0.1.0, previous version had the objects simply slide against each other than continue along their way, colliding objects now actively bounce off each other

## Not Yet Implemented
- Big Chunk of Mechanics
- All of Electromagnetism

## Status
VERY early WIP

## To Do
- Polish UI and Visualization
- Polish Collision Logic
      Currently, objects that collide simply slide off of each other rather than bouncing off 
- Implement more Mechanics

## Code is in
src/main/java/pr/phys/physicsengine2d
Main Class: **HelloApplication.java**

*There is no executable yet because this is a very early version*
