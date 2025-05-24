
# Java 2D Physics Engine (WIP)

This is a WIP 2D physics engine written in Java (planned JavaFX UI)

## Features So Far
- **Vector2D class**: Basic vector math (add, scale, etc.)
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
- **Visualization**:
  - Click to Spawn Object
  - Double Click and drag to Apply Custom Force
  - Arrow to force direction
  - Label using arbitrary units for force
  - Coordinate Axis

## Not Yet Implemented
- **COLLISIONS**
- Big Chunk of Mechanics
- All of Electromagnetism

## Status
VERY early WIP

## To Do
- Polish UI and Visualization
- Add Collision Logic
- Implement more Mechanics

## Code is in
src/main/java/pr/phys/physicsengine2d
Main Class: **HelloApplication.java**

*There is no executable yet because this is a very early version*
