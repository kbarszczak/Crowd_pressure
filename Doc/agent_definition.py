from fpdf import FPDF

FONT = "/System/Library/Fonts/Supplemental/Arial Unicode.ttf"
FONT_BOLD = "/System/Library/Fonts/Supplemental/Arial Bold.ttf"


class PDF(FPDF):
    def header(self):
        if self.page_no() > 1:
            self.set_font("ArialU", "", 9)
            self.set_text_color(128, 128, 128)
            self.cell(
                0,
                8,
                "Agent Definition - Pedestrians Movement on Crossings",
                align="C",
            )
            self.ln(10)
            self.set_draw_color(200, 200, 200)
            self.line(10, self.get_y(), 200, self.get_y())
            self.ln(5)

    def footer(self):
        self.set_y(-15)
        self.set_font("ArialU", "", 9)
        self.set_text_color(128, 128, 128)
        self.cell(0, 10, f"{self.page_no()}", align="C")

    def chapter_title(self, title):
        self.set_font("ArialB", "", 14)
        self.set_text_color(0, 51, 102)
        self.cell(0, 10, title, new_x="LMARGIN", new_y="NEXT")
        self.set_draw_color(0, 51, 102)
        self.line(10, self.get_y(), 200, self.get_y())
        self.ln(4)

    def section_title(self, title):
        self.set_font("ArialB", "", 12)
        self.set_text_color(0, 70, 130)
        self.cell(0, 8, title, new_x="LMARGIN", new_y="NEXT")
        self.ln(2)

    def body_text(self, text):
        self.set_font("ArialU", "", 10)
        self.set_text_color(30, 30, 30)
        self.multi_cell(0, 5.5, text, align="J")
        self.ln(2)

    def bullet(self, text):
        self.set_font("ArialU", "", 10)
        self.set_text_color(30, 30, 30)
        self.cell(8, 5.5, "•")
        self.multi_cell(0, 5.5, text, align="J")
        self.ln(1)


pdf = PDF()
pdf.set_auto_page_break(auto=True, margin=20)

pdf.add_font("ArialU", "", FONT)
pdf.add_font("ArialB", "", FONT_BOLD)

pdf.add_page()

# Title page
pdf.ln(25)
pdf.set_font("ArialB", "", 22)
pdf.set_text_color(0, 51, 102)
pdf.multi_cell(0, 12, "Pedestrians Movement on Crossings", align="C")
pdf.ln(5)
pdf.set_font("ArialU", "", 16)
pdf.set_text_color(80, 80, 80)
pdf.cell(0, 10, "Agent Definition", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.ln(10)
pdf.set_font("ArialU", "", 11)
pdf.set_text_color(60, 60, 60)
pdf.cell(
    0,
    7,
    "Definition, characteristics, and interactions of agents in the system",
    align="C",
    new_x="LMARGIN",
    new_y="NEXT",
)
pdf.ln(20)
pdf.set_draw_color(0, 51, 102)
pdf.line(60, pdf.get_y(), 150, pdf.get_y())
pdf.ln(10)
pdf.set_font("ArialU", "", 11)
pdf.set_text_color(40, 40, 40)
pdf.cell(0, 7, "Course: Agent Systems", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.ln(5)
pdf.set_font("ArialB", "", 11)
pdf.cell(0, 7, "Authors:", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.set_font("ArialU", "", 11)
pdf.cell(
    0,
    7,
    "Kamil Barszczak, Radosław Barszczak, Szymon Klempert",
    align="C",
    new_x="LMARGIN",
    new_y="NEXT",
)
pdf.ln(5)
pdf.set_font("ArialU", "", 10)
pdf.set_text_color(100, 100, 100)
pdf.cell(0, 6, "May 2026", align="C", new_x="LMARGIN", new_y="NEXT")

# --- Content ---
pdf.add_page()

pdf.chapter_title("1. Agent definition")

pdf.body_text(
    "In the context of this project, an agent represents a single pedestrian "
    "navigating through a shared physical environment (a crossing, corridor, "
    "or bottleneck). Each agent will be an autonomous entity that perceives its "
    "local surroundings, makes independent movement decisions based on "
    "behavioral heuristics, and interacts with other agents and obstacles "
    "through physical and social forces. There will be no central controller - "
    "collective crowd behavior is expected to emerge from individual "
    "agent-level rules."
)

pdf.chapter_title("2. Agent characteristics")

pdf.body_text("Each agent will possess the following individual attributes:")

pdf.bullet(
    "Mass (m) - the physical mass of the pedestrian (planned range: 60-90 kg), "
    "used for computing collision forces and body radius (r = m / 50)."
)
pdf.bullet(
    "Comfortable speed (v0) - the preferred walking speed (1.5-2.0 m/s) that "
    "the agent will try to maintain when unobstructed."
)
pdf.bullet(
    "Vision angle (phi) - the field of view (1.2-1.3 rad) within which the "
    "agent will scan for obstacles and other pedestrians."
)
pdf.bullet(
    "Maximum vision distance (d_max) - the farthest distance (100-105 m) at "
    "which the agent will be able to detect obstacles."
)
pdf.bullet(
    "Relaxation time (tau) - the time constant (0.45-0.55 s) governing how "
    "quickly the agent adjusts its velocity towards the desired one."
)
pdf.bullet("Desired position - the agent's goal (destination point) on the map.")
pdf.bullet(
    "Color - a visual group identifier; agents belonging to the same group "
    "(i.e., sharing the same origin and destination) will be rendered in the same color."
)

pdf.body_text(
    "Each agent will also maintain dynamic state that changes every simulation step: "
    "current position, current velocity vector, and desired velocity vector "
    "(recomputed by the heuristics at each step). We plan to use a double-buffering "
    "mechanism for position and velocity to allow concurrent updates without "
    "read-write conflicts."
)

pdf.chapter_title("3. Agent types")

pdf.body_text(
    "All agents will share the same behavioral model - there will be no explicit "
    "type hierarchy. However, agents will be differentiated by their "
    "parameterization and group assignment:"
)

pdf.bullet(
    "Group membership - agents will be spawned in groups defined by their origin "
    "area and shared destination. For example, in a planned crossing scenario, "
    "four groups could approach an intersection from four cardinal directions, "
    "each heading to the opposite side."
)
pdf.bullet(
    "Parameter variation - within a group, agents will be assigned randomized "
    "mass, comfortable speed, vision angle, and relaxation time drawn from "
    "predefined ranges. This heterogeneity should produce realistic variance "
    "in individual behavior."
)

pdf.body_text(
    "The architecture is designed to support extending agent types in the future "
    "(e.g., a police officer agent with different force parameters, or a child "
    "agent with reduced speed and vision range) by varying the initialization "
    "parameters."
)

pdf.chapter_title("4. Communication and interactions")

pdf.section_title("4.1 Interaction model")
pdf.body_text(
    "Agents will not communicate through explicit message passing. Instead, all "
    "interaction will be implicit and physics-based, mediated through the shared "
    "environment. Each agent will perceive the positions and velocities of nearby "
    "agents and react according to the Social Force Model rules. This is "
    "consistent with real pedestrian behavior, where people do not verbally "
    "coordinate but instead react to observed positions and movements of others."
)

pdf.section_title("4.2 Types of interactions")
pdf.body_text("Three categories of interactions are planned:")
pdf.bullet(
    "Agent-to-agent repulsion - when two agents' body radii overlap, a contact "
    "force proportional to the overlap distance will push them apart. This "
    "prevents physical overlap and models body-to-body collisions in dense crowds."
)
pdf.bullet(
    "Agent-to-wall repulsion - when an agent's body radius intersects with a wall "
    "segment, a repulsive force will push the agent away from the wall surface."
)
pdf.bullet(
    "Heuristic-based avoidance - before physical contact occurs, the direction "
    "and distance heuristics will cause agents to proactively steer away from "
    "obstacles and other pedestrians within their field of view."
)

pdf.section_title("4.3 Expected emergent behaviors")
pdf.body_text(
    "Although agents will follow simple local rules, several complex collective "
    "behaviors are expected to emerge from their interactions:"
)
pdf.bullet(
    "Lane formation - counter-flowing pedestrian streams should spontaneously "
    "organize into lanes, reducing collisions."
)
pdf.bullet(
    "Bottleneck congestion - agents should accumulate before narrow passages, "
    "forming arching clusters around the opening."
)
pdf.bullet(
    "Freezing by heating - under excessive crowd pressure, mutual blocking "
    "should paradoxically reduce flow to near zero."
)
pdf.bullet(
    "Group mixing and separation - at crossing intersections, groups from "
    "different directions are expected to temporarily intermix before separating "
    "and continuing to their respective destinations."
)

pdf.chapter_title("5. Technical framework")

pdf.section_title("5.1 Programming language and platform")
pdf.body_text(
    "The simulation will be implemented in Java 17+ using JavaFX for the "
    "graphical user interface. We plan to use Gradle as the build tool and "
    "structure the project as a modular Java application."
)

pdf.section_title("5.2 Planned technologies and libraries")
pdf.bullet(
    "JavaFX - GUI framework for rendering the simulation canvas, drawing agents "
    "as colored circles, walls as lines, and providing configuration controls."
)
pdf.bullet(
    "Java Concurrency (ExecutorService, thread pools) - we plan to support "
    "both single-threaded and multi-threaded computing engines for parallel "
    "agent updates."
)
pdf.bullet(
    "JUnit 5 - intended for unit testing of core model classes and utility functions."
)

pdf.section_title("5.3 Data")
pdf.body_text(
    "The simulation will not rely on external datasets. All input data will be "
    "generated procedurally through map initializers that define wall layouts "
    "and agent spawn configurations. We plan to create several predefined maps, "
    "ranging from simple single-direction flow scenarios to complex multi-way "
    "crossing intersections. Agent parameters (mass, speed, vision) will be "
    "drawn from configurable uniform distributions at initialization time."
)

pdf.output("/Users/kamilbarszczak/Desktop/Crowd_pressure/doc/Agent_Definition.pdf")
print("PDF generated successfully.")
