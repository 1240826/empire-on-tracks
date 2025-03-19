# Supplementary Specification (FURPS+)

## Functionality

_Specifies functionalities that:  
&nbsp; &nbsp; (i) are common across several US/UC;  
&nbsp; &nbsp; (ii) are not related to US/UC, namely: Audit, Reporting and Security._

(The system must allow authentication of users with a password containing seven alphanumeric characters, including three capital letters and two digits.
The system must support object serialization for data persistence between successive runs.
Business rules must be validated when recording and updating data.
The simulator must automatically generate cargo at stations based on cities and industries present in the network.
The system must allow statistical analysis of the railway network and its connectivity.)

## Usability 

_Evaluates the user interface. It has several subcategories,
among them: error prevention; interface aesthetics and design; help and
documentation; consistency and standards._

(The user interface should follow consistent UI/UX design principles for clarity and ease of use.
Error messages must be informative and guide the user toward resolving issues.
The interface should provide tooltips and basic help documentation for users.
Standard UI elements should be used to ensure familiarity (buttons, lists, forms).
Consistent color schemes and typography should be applied across all screens. )

## Reliability

_Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures._

(The system must prevent unauthorized access through proper authentication mechanisms.
In case of failure, data integrity must be maintained through serialized object storage.
The application must be able to recover from unexpected shutdowns without losing critical data.
The system should ensure a high level of accuracy when simulating train routes and cargo management.
Unit tests must cover all critical functionalities to reduce the frequency of failures. )

## Performance

_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._

(The system should provide a fast response time when editing maps, creating scenarios, and managing railway networks.
The startup time should not exceed a few seconds under normal conditions.
The simulator must run efficiently even with multiple trains and industries active.
Memory consumption should be optimized to avoid excessive use of resources.
CPU usage should be managed to ensure smooth simulation without performance bottlenecks.)

## Supportability

_The supportability requirements gathers several characteristics, such as:
testability, adaptability, maintainability, compatibility,
configurability, installability, scalability and more._

(The system must be developed using Java to ensure compatibility with different operating systems.
The codebase should follow Object-Oriented best practices to facilitate maintenance.
Javadoc documentation must be provided for all major classes and methods.
The application should be scalable, allowing new industries, train types, and features to be added with minimal changes.
Unit tests using JUnit 5 must be implemented, and JaCoCo must be used for test coverage analysis.)

## +

### Design Constraints

_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._

(The project must be developed using an iterative and incremental approach, following the SCRUM methodology.
The software must be implemented using Java, following best Object-Oriented programming practices.
All images and figures must be stored in SVG format.
The development must follow Test-Driven Development (TDD) principles. )

### Implementation Constraints

_Specifies or constraints the code or construction of a system such
such as: mandatory standards/patterns, implementation languages,
database integrity, resource limits, operating system._

(The application must be implemented in Java and use JUnit 5 for testing.
Coding standards such as CamelCase naming conventions must be followed.
The system must use object serialization for data persistence.
Validation rules must be strictly applied to ensure data consistency.
)

### Interface Constraints

_Specifies or constraints the features inherent to the interaction of the
system being developed with other external systems._

(The system must provide a list-based selection method for choosing stations and locomotives.
The simulator must allow interaction through a graphical map-based interface.
The system should include options for starting, pausing, and resuming simulations.
The interface must display train movements and cargo transactions in real time. )

### Physical Constraints

_Specifies a limitation or physical requirement regarding the hardware used to house the system, as for example: material, shape, size or weight._

(The application must be executable on standard desktop/laptop machines with Java installed.
No specialized hardware should be required to run the software.
The graphical interface must be optimized to function well on common screen resolutions. )