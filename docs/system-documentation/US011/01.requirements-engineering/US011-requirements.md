# US011 - List All Trains

## 1. Requirements Engineering

### 1.1. User Story Description

As a Player, I want to list all trains.

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>	Each task is characterized by having a unique reference per organization, a designation, an informal and a technical description, an estimated duration and cost, as well as a task category. 

>	As long as it is not published, access to the task is exclusive to the employees of the respective organization. 

**From the client clarifications:**

> **Question:** Which is the unit of measurement used to estimate duration?
>
> **Answer:** Duration is estimated in days.

> **Question:** Monetary data is expressed in any particular currency?
>
> **Answer:** Monetary data (e.g. estimated cost of a task) is indicated in POT (virtual currency internal to the platform).

### 1.3. Acceptance Criteria

* **AC1:** The display of trains must include the locomotive and the current cargoes.
* **AC2:**  The trains should be grouped by locomotive type and then listed alphabetically by name.

### 1.4. Found out Dependencies

* There is a dependency on "US09 - Buy a train" as there must be trains purchased by the player to list.
* There is a dependency on "US10 - Assign a train to a route" for trains that have cargoes assigned.

### 1.5 Input and Output Data

**Input Data:**

* Typed data:
    * none
	
* Selected data:
    * a task category 

**Output Data:**

* List of all trains grouped by locomotive type and sorted alphabetically by name
* For each train: locomotive type, train name, current cargoes
* (optional) Additional train details (route, status, etc.)

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/US011-SSD.svg)

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks

* The created task stays in a "not published" state in order to distinguish from "published" tasks.