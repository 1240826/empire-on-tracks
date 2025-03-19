# US001 - Create a Map

## 1. Requirements Engineering

### 1.1. User Story Description

As an Editor, I want to create a map with a size and a name. 

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>	 The Map Editor allows you to create rectangular maps and place static elements on the map

**From the client clarifications:**

> **Question:** What are the maximum and minimum possible sizes of a world map?
>
> **Answer:** We are the ones who define them.

> **Question:** Does the name of a map have any restrictions??
>
> **Answer:** The name has no restrictions.

### 1.3. Acceptance Criteria

* **N/A**

### 1.4. Found out Dependencies

* **N/A**

### 1.5 Input and Output Data

**Input Data:**

* Typed data:
    * size
    * name
	
* Selected data:
    * a task category 

**Output Data:**

* mapa 
* Success of the operation

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/US001_CreateMap.svg)

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks

* The created task stays in a "not published" state in order to distinguish from "published" tasks.