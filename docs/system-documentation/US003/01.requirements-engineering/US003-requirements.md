# US003 - Add a City

## 1. Requirements Engineering

### 1.1. User Story Description

As an Editor, I want to add a city in a position XY of the selected
map, with a name and a positive number of house blocks.

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>   Indicate the position XY.
>	City must have a name and a positive number of house blocks. 

**From the client clarifications:**


> **Question:** According to User Story 3, dear client, can the city name have spaces in its naming?

> **Answer:** yes you can, "Torres Novas" for example.


> **Question:** According to User Story 3, can house blocks be assigned manually (by the editor) or automatically randomly around the city position?

> **Answer:** Already clarified in the statement.

> **Question:** According to User Story 3, should the city name be unique within the same map?

> **Answer:** It can be repeated but it can become confusing for players, the editor should be alerted to the situation.

> **Question:** Dear client, should it be considered as an acceptance criterion that the map should be visually updated to show the city that was previously added to the respective map and its house blocks?
> **Answer:** There is no specification for user interfaces yet.

> **Question:** Is there a minimum or maximum number of house blocks that can be assigned?
> **Answer:** The number needs to be a positive on; there is no maximum, it's up to the editor to decide.

> **Question:** Is it possible for a city to be destroyed by a natural disaster or a war? If so, is it necessary to create a mechanism to remove the city from the map and create a new one to transfer the active elements from the first one(people, mail, ...)?
> **Answer:** A city can be removed in edit mode, but not in simulation mode.


### 1.3. Acceptance Criteria

* **AC1:** A city name cannot have special characters or digits.
* **AC2:** The house blocks can be assigned manually or automatically (randomly around the city tag position accordingly to normal distribution).
* **AC3:** Number of house blocks is positive.


### 1.4. Found out Dependencies

* **US01 - Create a Map** → A city can only be added to an existing map.

### 1.5 Input and Output Data

**Input Data:**

* Typed data:
  * City name
  * XY position on the map
  * Number of house blocks

* Selected data:
    * Manual or automatic house block assignment 

**Output Data:**

* (In)Success of the operation

### 1.6. System Sequence Diagram (SSD)

![US003-SSD.svg](svg/US003-SSD.svg)

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks

* N/A.