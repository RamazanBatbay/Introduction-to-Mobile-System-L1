# Lab 12-13-14-15: Maze Game

* **Course name:** Introduction to Mobile System L1
* **Lab number:** Lab 12-13-14-15
* **Student name:** Ramazan Batbay
* **Student ID:** 54813

## Overview
An interactive grid-based Maze Explorer game developed as a native Android application. The game utilizes a compact bitwise-encoded matrix to represent room connections and door states, challenging the player to navigate from a dynamically detected start cell to the goal.

### Features
* **Bitmask Room Matrix:** Compactly encodes four-way door configurations and room metadata using binary flags (LEFT: 1, RIGHT: 2, UP: 4, DOWN: 8, START: 16) within a single 2D integer array.
* **Sleek UI & State Synchronizer:** Dynamically enables/disables directional navigation buttons, updates player coordinates, and translates room integers to padded binary strings in real-time.
* **Dynamic Start & Win Logic:** Automatically locates the start room by checking bit 16, and triggers a victory screen upon reaching the exit room (value 0).
* **3x3 Room Visualizer:** Renders a clean graphical representation of the current room, highlighting open doors (Emerald Green) and locked exits (Slate Gray) to guide player movement.
