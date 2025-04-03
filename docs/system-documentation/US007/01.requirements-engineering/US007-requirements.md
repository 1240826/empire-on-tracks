# US007 - List all Stations

## 1. Requirements Engineering

### 1.1. User Story Description

As a Player, I want to list all the stations to select one to see its details, including the existing building and the demand/supply cargoes.

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>	When viewing a station, in addition to the buildings present, the cargo that is ready for collection and the cargo that needs to be delivered should also be displayed. 

>  

**From the client clarifications:**

> **Question:** 
>
> **Answer:** 

> **Question:** 
>
> **Answer:**

### 1.3. Acceptance Criteria

* **AC1:** At least one station must exist, and an error message should be shown in case it doesn't.
* **AC2:** 
* **AC3:** 

### 1.4. Found out Dependencies

* There is a dependency on "US005 - Build a station" as there must be at least one station already built to be able to list it.

### 1.5 Input and Output Data

**Input Data:**
selected station
* Typed data:
    * a reference
    * a designation 
    * an informal description
    * a technical description
    * an estimated duration
    * an estimated cost
	
* Selected data:
    * selected station and detail type

**Output Data:**

* station details
* (In)Success of the operation

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/US007-SSD.svg)

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks

* The created task stays in a "not published" state in order to distinguish from "published" tasks.