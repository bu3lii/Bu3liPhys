# Java 2D Physics Engine

This is a Work-In-Progress (WIP) 2D physics engine written in Java utilizing JavaFX for its graphical user interface.

## Current Version
**0.2.5**

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

## 0.2.5 Changelog (Major Updates)

### New Features / Quality of Life (QoL) Enhancements
- **Pause/Resume Simulation**: Control the simulation flow with a dedicated button or the Spacebar key.
- **Reset Simulation**: A button to clear all objects and reset the world to its initial state (default gravity, no selected objects).
- **Adjustable Gravity**: Sliders for real-time modification of both X and Y components of the global gravity vector.
- **Simulation Speed Control**: A slider to increase or decrease the overall simulation speed.
- **Velocity Vectors** arrows extending from objects indicating their current velocity direction and magnitude.
- **Toggleable Visualizations**: Checkboxes to dynamically show/hide:
    - Object trails.
    - Coordinate axes
    - Velocity Vectors
- **Real-time Object Counter**: Displays the current number of objects in the simulation.

### Bug Fixes
- **GraphicsContext Scope**: Corrected the scope of the GraphicsContext to ensure persistent drawing across frames.
- **No Spawning While Paused**: Prevented new objects from being added when the simulation is in a paused state.
- **Corrected Drag Force Label Scaling**: The force magnitude displayed on the drag arrow now accurately reflects the applied force (scaled by 75 units).
- **Resolved minor issues in the code**

## Not Yet Implemented
- More advanced collision shapes.
- Complex physics mechanics (joints, rotational physics, soft bodies).
- All of Electromagnetism.

## Status
Early Work in Progress

## To Do
- Further polish of UI elements and overall visualization.
- Refinement and potential expansion of collision logic.
- Persistent Scene Setup via JSON
- Implementation of additional physics mechanics.

## Code is located in
`src/main/java/physicsengine2d`
Main Class: **HelloApplication.java**

*There is no standalone executable yet as this project is under active development.*
