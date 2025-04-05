# Supplementary Specification (FURPS+)

## Functionality

_Specifies functionalities that:  
&nbsp; &nbsp; (i) are common across several US/UC;  
&nbsp; &nbsp; (ii) are not related to US/UC, namely: Audit, Reporting and Security._

(Security:
* "All those who wish to use the application must be authenticated with a password of seven alphanumeric characters, including three capital letters and two digits." 
* The application ought to employ object serialization to guarantee the persistence of the data in two successive runs.


Functionalities that are common across several US/UC:
* The simulator must automatically generate cargo at stations based on the cities and industries present in the railway network. 
Additionally, the system must provide statistical tools to analyze the railway network's performance, including its connectivity and efficiency.
* The simulator must provide tools to assess the railway network’s structure and performance.)


## Usability

_Evaluates the user interface. It has several subcategories,
among them: error prevention; interface aesthetics and design; help and
documentation; consistency and standards._

(Interface aesthetics and design:
* The user interface must follow usability principles, prioritizing user intent rather than complex UI mechanisms.

Error prevention:
* Error messages must follow a centralized error reporting approach, providing consistent notifications to users and facilitating issue resolution. 

Interface aesthetics and design:
* Interactive guidance, such as contextual hints and help features, must be available to assist users in navigating the system effectively. 

Consistency and standards:
* Input methods must be structured to minimize user errors, providing clear validation feedback when incorrect data is entered. 
* The interface must maintain uniformity across all modules to ensure a predictable and intuitive interaction experience.)

## Reliability

_Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures._

(In case of system failure, data integrity must be maintained using object serialization. 
The simulator must execute train routes, cargo movement, and station operations based on predefined operational rules.
The system must maintain data persistence between successive runs using object serialization.)

## Performance

_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._

(TBD)

## Supportability

_The supportability requirements gathers several characteristics, such as:
testability, adaptability, maintainability, compatibility,
configurability, installability, scalability and more._

(The system must be developed in Java to ensure cross-platform compatibility. 
The codebase must follow Object-Oriented programming principles to facilitate maintainability and scalability. 
Javadoc documentation must be provided for all major classes and methods. 
The architecture must be modular to facilitate the addition of new industries, train types, and functionalities with minimal modifications. 
Unit tests must be implemented using JUnit 5, and test coverage analysis must be performed using JaCoCo.)

## +

### Design Constraints

_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._

(The project must follow an iterative and incremental development approach based on SCRUM.
The architecture must incorporate UML modeling as part of the system design process .
All diagrams and graphical elements must be stored in SVG format.
The development must follow Test-Driven Development (TDD) principles to ensure software quality and maintainability.)

### Implementation Constraints

_Specifies or constraints the code or construction of a system such
such as: mandatory standards/patterns, implementation languages,
database integrity, resource limits, operating system._

(implementation languages:
* "The application must be developed in Java language."

Mandatory standards/patterns:
* Coding standards such as CamelCase naming conventions must be followed.
The system must use object serialization to guarantee data persistence across system executions.
Strict validation rules must be applied to ensure business logic consistency and data integrity.)

### Interface Constraints

_Specifies or constraints the features inherent to the interaction of the
system being developed with other external systems._

(The system must enable users to select stations and locomotives from a predefined list. 
The simulator must allow users to create and oversee the railway network. 
Users must be able to start, pause, and resume simulations dynamically. 
The interface must present train movements and cargo transactions in real time, facilitating effective network monitoring.)

### Physical Constraints

_Specifies a limitation or physical requirement regarding the hardware used to house the system, as for example: material, shape, size or weight._

(The system must run on any machine capable of executing Java applications.
No specialized hardware should be required to run the software.
The graphical interface must be adaptable to different screen configurations to maintain usability.)