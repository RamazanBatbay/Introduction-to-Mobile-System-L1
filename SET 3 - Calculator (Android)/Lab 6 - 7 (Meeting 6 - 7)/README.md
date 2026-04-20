# Lab 6: Basic Calculator Application

* **Course name:** Introduction to Mobile System L1
* **Lab number:** Lab6-7
* **Student name:** Ramazan Batbay
* **Student ID:** 54813

## Overview
Part 1 and Part 2 are combined in this project.
A fully-featured Android Calculator application implementing strict sequential (left-to-right) evaluation with chaining operations. This project covers both the core mandatory requirements and the extended bonus features.

### Features
* **Immediate (Sequential) Evaluation:** Expressions evaluate sequentially in input order (e.g., `2 + 2 * 2 = 8`).
* **Basic & Advanced Operators:** Support for `+`, `-`, `*`, `/`, and Power (`num`).
* **Robust Error Handling:** Division by zero safely outputs `Error` rather than crashing the app.
* **Input Management:** Includes `AC` (All Clear) for full reset and `CE` (Clear Entry) to erase current input without breaking the operational chain.
* **Extended Bonus (Part 2):**
  * **Graphical UI:** Custom-styled color-changing interactive buttons via XML state selectors.
  * **Number Bases:** Ability to display results in `BIN`, `OCT`, and `HEX`. Safely throws `Error` if fractions/decimals are pushed.
  * **Operation History:** Includes a dedicated History activity screen containing a timeline log of intermediate and final evaluated calculations.