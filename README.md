# Empire on Tracks — Railway Simulation Game

A Java-based railway simulation and management game inspired by **Railroad Tycoon 2**, developed as a 2nd semester integrative project at ISEP (Instituto Superior de Engenharia do Porto).

Features a comprehensive **map editor** for creating custom scenarios and a **simulation engine** for running railway operations with dynamic train movement, cargo transport, and industry economics.

---

## What It Does

The game has two modes:

### Map Editor
- Place cities, industries (factories, farms, mines), train stations, and rail infrastructure on an interactive map
- Define cargo types, production chains, and economic relationships between industries
- Design the railway network by connecting stations with tracks
- Customize terrain and landscape features

### Simulation (Player Mode)
- Deploy locomotives and manage a railway transport company
- Create routes between stations and assign trains
- Transport cargo between industries to generate revenue
- Manage resources: fuel, maintenance, employee costs
- Watch trains move dynamically along the network with real-time interactions

---

## Architecture

```
┌────────────────────────────────────────────┐
│              JavaFX GUI (View)             │
│    Map Editor │ Dashboard │ Route Manager  │
├────────────────────────────────────────────┤
│              Controller Layer              │
│       User input handling │ Game loop      │
├────────────────────────────────────────────┤
│                 Model Layer                │
│      Game logic │ Economy │ Train physics  │
├────────────────────────────────────────────┤
│           Python (Map Processing)          │
│       Map generation │ CSV data parsing    │
└────────────────────────────────────────────┘
```

- **MVC Pattern**: Strict separation between game logic (Model), JavaFX visualization (View), and user interactions (Controller)
- **Design Patterns Used**:
  - **Observer**: UI components observe game state changes (train positions, cargo updates)
  - **Strategy**: Different train behavior strategies (express, freight, local)
  - **Command**: Undo/redo support in the map editor
  - **Singleton**: Game state manager and configuration

---

## Key Features

- **Interactive Map Editor**: Place and configure map elements (cities, industries, stations, buildings, rail tracks) via drag-and-drop
- **Dual-Mode System**: Seamlessly switch between Editor (design) and Player (simulate) roles
- **Dynamic Train Simulation**: Real-time train movement with speed control, route following, and station stops
- **Cargo Economy**: Production chains — farms produce grain → mills produce flour → bakeries produce bread — each step generates revenue
- **Python Integration**: Map data processing and scenario generation via Python scripts
- **Unit Testing**: JUnit tests for core game logic, economic calculations, and pathfinding

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Core | Java (OOP, Design Patterns) |
| GUI | JavaFX (FXML, CSS) |
| Map Processing | Python |
| Testing | JUnit |
| Architecture | MVC, Observer, Strategy, Command, Singleton |

---

## Project Structure

```
sem2-pi-24.25-g031-repo/
├── src/
│   ├── main/java/          Game source code
│   │   ├── model/           Game logic, economy, entities
│   │   ├── controller/      User input, game loop
│   │   └── view/            JavaFX screens (FXML + controllers)
│   ├── test/java/           JUnit tests
│   └── resources/           FXML layouts, CSS, images, map data
├── MATCP/                   Python map processing scripts
├── docs/                    Sprint planning, domain model
├── User Manual/             Player and editor documentation
└── plantuml4idea/           UML diagrams
```

---

## My Contributions

- Map editor development: interactive placement and configuration of game entities
- Train movement system: speed control, route following, station arrival/departure logic
- MVC architecture implementation and design pattern application
- Python scripts for map data processing and CSV parsing

---

## Team

5-person Scrum team from ISEP, 2nd semester.

---

## Setup

```bash
# Prerequisites: Java 17+, Python 3

# Clone
git clone https://github.com/1240826/empire-on-tracks.git
cd empire-on-tracks

# Build
mvn clean install -DskipTests

# Run
mvn exec:java -pl app

# Run tests
mvn test
```

---

## License

Academic project developed at ISEP. Repository made public after semester completion.
