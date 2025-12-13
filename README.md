# Java 2D Physics Engine 

This is a Work-In-Progress (WIP) 2D physics engine written in Java utilizing JavaFX for its graphical user interface.

## UPDATE: Discontinued, java is not really the right tool for this and developing became a hassle

## Current Version
**0.2.6**

## Features So Far
- **Core Physics Elements**:
    - **Vector2D class**: Provides fundamental vector mathematics operations.
    - **PhysicsObject class**: Represents physical objects with properties such as:
        - Mass
        - Position
        - Velocity
        - Acceleration
        - Ability to apply external forces
        - Optional trail rendering to visualize movement history.
- **World Simulation**:
    - **World class**: Manages the collection of physics objects and the simulation environment.
    - Applies global forces like gravity.
    - Steps the simulation forward in discrete time steps.
    - Handles **Window Border Collisions**.
    - Implements **Object-to-Object Collisions** with impulse-based response and basic friction.
- **Interactive Visualization (JavaFX UI)**:
    - **Click to Spawn Object**: Easily add new circular objects to the world.
    - **Drag to Apply Custom Force**: Select an object by clicking, then drag to apply an impulse force in the opposite direction of the drag.
    - **Force Visualization**: An arrow dynamically shows the direction and scaled magnitude of the applied drag force.

## 0.2.6 Changelog (Minor Update)

### New Features
- **JSON-based Simulation Persistence**:
    - Introduced a dedicated `PersistenceManager` class for handling saving and loading simulation states.
    - Allows users to **Save** the current world state (objects, gravity) to a JSON file.
    - Allows users to **Load** a previously saved simulation from a JSON file, restoring objects and world properties.
    - JSON de/serialization done via Gson

### Bug Fixes
- Resolved minor issues in the code

## Not Yet Implemented
- More advanced collision shapes.
- Complex physics mechanics (joints, rotational physics, soft bodies).
- All of Electromagnetism.

## Status
Early Work in Progress

## To Do
- Further polish of UI elements and overall visualization.
- Refinement and potential expansion of collision logic.
- Implementation of additional physics mechanics.

## Code is located in
`src/main/java/physicsengine2d`
Main Class: **HelloApplication.java**

*There is no standalone executable yet as this project is under active development.*
