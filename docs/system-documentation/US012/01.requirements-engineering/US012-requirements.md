# US012 - Create a simulator

## 1. Requirements Engineering

### 1.1. User Story Description

As a Product Owner, I want to create a simulator that generates
cargoes at current stations, automatically, considering the cities and
industries that the railway network serves.

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

> Simulation tool that will allow the user to define and interact with the operation of a railway network (including lines, stations, and trains) in the context of a map/scenario
>
>  In addition to creating the dynamic elements already described, the player/user can play or pause the simulator. During the simulator’s operation, the evolution of events should be displayed, namely the trains’ journeys (start and end).
>In the simulation the following values should be considered, further values like locomotives costs and cargos deliver values should be checked in game documentation (previously referred):

**From the client clarifications:**

> **Question:** How will the predefined list of industries be managed and updated?
>
> **Answer:** 

> **Question:** How will the predefined list of industries be managed and updated?
>
> **Answer:**

> **Question:** Will there be any restrictions on selecting multiple industries at once?
>
> **Answer:**

> **Question:** Should industries be categorized or filtered for easier selection?
>
> **Answer:**

> **Question:** Will users be able to adjust their selection before confirming?
>
> **Answer:**

> **Question:** How precise should the XY coordinates be
>
> **Answer:** 



### 1.3. Acceptance Criteria

* **AC1:** This simulator should provide options for start/pause.

### 1.4. Found out Dependencies

* There is a dependency on "US001" - "Create a map"  a map must be provided to run the simulation.
* There is a dependency on "US002" - "Add an industry"  A industy must exist to consume and/or generate resources.
* There is a dependency on "US003" - "Add a City"  a city must exist to generate people and consume resources
* There is a dependency on "US005" - "Build a station"  A station must exist to receive/send resources
* There is a dependency on "US008" - "Build a station"  A station must exist to receive/send resources


### 1.5 Input and Output Data

**Input Data:**

* Typed data:
    * a map
    * scenario
    * start/pause
	
* Selected data:
    * 

**Output Data:**

* Updated map
* (In)Success of the operation
* pause/start message


### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/US012-SSD.svg)

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks

* The created task stays in a "not published" state in order to distinguish from "published" tasks.