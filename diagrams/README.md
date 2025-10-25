# QwikRide Diagrams

This folder contains UML diagrams for the QwikRide bike sharing system.

## Available Diagrams

- **USE_CASE_DIAGRAM.md** - Shows all actors and use cases in the system
- **SEQUENCE_DIAGRAMS.md** - Shows the flow for registration, login, and authenticated requests

## How to View

The diagrams are written in PlantUML syntax. Here are a few ways to view them:

### Option 1: VS Code Extension
1. Install the "PlantUML" extension in VS Code
2. Open any diagram file
3. Press `Alt + D` (or `Option + D` on Mac) to preview

### Option 2: Online Viewer
1. Copy the PlantUML code (between ```plantuml``` tags)
2. Visit http://www.plantuml.com/plantuml/uml/
3. Paste the code to see the diagram

### Option 3: IntelliJ IDEA
1. Install the "PlantUML Integration" plugin
2. Open any diagram file
3. The diagram will render automatically in the editor

### Option 4: Generate Images
If you have PlantUML installed locally:
```bash
# Install PlantUML (requires Java)
brew install plantuml  # macOS
apt-get install plantuml  # Ubuntu

# Generate PNG from a diagram
plantuml USE_CASE_DIAGRAM.md
```

## Diagram Types

### Use Case Diagram
Shows the high-level view of what users can do in the system. Good for understanding features and user roles.

### Sequence Diagrams
Shows the step-by-step flow of operations between components. Good for understanding how the system works internally.

## Tips

- Start with the use case diagram to understand the big picture
- Use sequence diagrams to understand implementation details
- These diagrams follow UML 2.5 standards
- All diagrams are kept in sync with the actual codebase
