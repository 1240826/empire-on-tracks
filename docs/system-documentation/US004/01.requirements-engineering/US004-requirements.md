# US004 - Create a Scenario

## 1. Requirements Engineering

### 1.1. User Story Description

As an Editor, I want to create a scenario for a selected map.

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

> The Scenario Editor must allow the user to define the restrictions and
conditions for using a given map, namely:
• Time Restrictions - the period in which the simulation will take place;
• Technological Restrictions - for example, which locomotives or industries
are available in the scenario;
• Historical Restrictions - historical events that make sense in the scenario
and change the operating conditions of the simulator. For instance,
a war increases the steel demand, and a vaccination campaign
or a schooling program encourages mail and/or passenger traffic. In
this context, the goods that the Ports import and export are also defined.

**From the client clarifications:**

> **Question:** How does the Editor define the restrictions for the scenario?
>
> **Answer:** answer.

>**Question:** Should the system request the scenario name and time period in a single interaction, or should it ask for each detail separately?  
>
> **Answer:** answer.

> **Question:** Is it possible to edit a scenario after it has been created?  
> **Answer:** answer.


### 1.3. Acceptance Criteria

* **AC1:** Time must be positive.
* **AC2:** Time must be valid.

### 1.4. Found out Dependencies

* There is a dependency on **US001 - Create a Map**, as a scenario can only be created for an existing map previously selected.
### 1.5 Input and Output Data

**Input Data:**

* Typed data:
  * Scenario name
  * Time restrictions (start and end period)
  * //Technological restrictions (available locomotives, industries)
  * //Historical restrictions (events affecting the scenario)
  
* Selected data:
    * A previously created map

**Output Data:**
* (In)Success of the operation


### 1.6. System Sequence Diagram (SSD)

![US004-SSD.svg](svg/US004-SSD.svg)

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks

* 
* For this sprint, the scenario only acts as a **time restriction** for the simulation.  
* Future sprints will introduce additional features (train types, resource generation).  