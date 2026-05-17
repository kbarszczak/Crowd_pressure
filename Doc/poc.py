from fpdf import FPDF

FONT = "/System/Library/Fonts/Supplemental/Arial Unicode.ttf"
FONT_BOLD = "/System/Library/Fonts/Supplemental/Arial Bold.ttf"
SS = "/Users/kamilbarszczak/Desktop/Crowd_pressure/doc/screenshots"
DOC = "/Users/kamilbarszczak/Desktop/Crowd_pressure/doc"


class PDF(FPDF):
    def header(self):
        if self.page_no() > 1:
            self.set_font("ArialU", "", 9)
            self.set_text_color(128, 128, 128)
            self.cell(
                0, 8, "Proof of Concept - Pedestrians Movement on Crossings", align="C"
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

    def figure(self, path, caption, w=130):
        x = (210 - w) / 2
        self.image(path, x=x, w=w)
        self.ln(2)
        self.set_font("ArialU", "", 9)
        self.set_text_color(100, 100, 100)
        self.cell(0, 5, caption, align="C", new_x="LMARGIN", new_y="NEXT")
        self.set_text_color(30, 30, 30)
        self.ln(4)

    def two_figures(self, path1, cap1, path2, cap2, w=85):
        y = self.get_y()
        self.image(path1, x=12, y=y, w=w)
        self.image(path2, x=108, y=y, w=w)
        h = w * 0.65
        self.set_y(y + h + 2)
        self.set_font("ArialU", "", 8)
        self.set_text_color(100, 100, 100)
        self.set_x(12)
        self.cell(w, 5, cap1, align="C")
        self.set_x(108)
        self.cell(w, 5, cap2, align="C")
        self.set_text_color(30, 30, 30)
        self.ln(8)


pdf = PDF()
pdf.set_auto_page_break(auto=True, margin=20)
pdf.add_font("ArialU", "", FONT)
pdf.add_font("ArialB", "", FONT_BOLD)

# --- Title page ---
pdf.add_page()
pdf.ln(25)
pdf.set_font("ArialB", "", 22)
pdf.set_text_color(0, 51, 102)
pdf.multi_cell(0, 12, "Pedestrians Movement on Crossings", align="C")
pdf.ln(5)
pdf.set_font("ArialU", "", 16)
pdf.set_text_color(80, 80, 80)
pdf.cell(0, 10, "Proof of Concept", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.ln(10)
pdf.set_font("ArialU", "", 11)
pdf.set_text_color(60, 60, 60)
pdf.cell(
    0,
    7,
    "Prototype demonstration with screenshots",
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

# --- Page 2: Introduction + Architecture ---
pdf.add_page()

pdf.chapter_title("1. Prototype overview")
pdf.body_text(
    "This document presents a working Proof of Concept (PoC) for the pedestrian "
    "movement simulation based on the Social Force Model. The prototype implements "
    "the core agent behaviors described in the Agent Definition document and "
    "demonstrates two key scenarios: a bottleneck passage and a four-way crossing "
    "intersection. The goal is to verify that the fundamental design assumptions "
    "are feasible and that the minimal version of the simulation produces "
    "realistic crowd dynamics."
)

pdf.chapter_title("2. Architecture")
pdf.body_text(
    "The prototype follows a modular architecture with clear separation of "
    "concerns. The class diagram below shows the key components:"
)
pdf.figure(
    f"{DOC}/crowd_pressure_uml.jpg",
    "Fig. 1: UML class diagram of the prototype architecture.",
    w=150,
)
pdf.body_text(
    "The architecture is built around three core interfaces that enable "
    "flexible configuration:"
)
pdf.bullet(
    "ComputingEngine - defines how agent updates are computed. Two implementations "
    "are provided: SingleThreadComputingEngine (sequential) and "
    "MultiThreadComputingEngine (parallel using thread pools)."
)
pdf.bullet(
    "Heuristics - defines behavioral rules applied to agents each step. "
    "DirectionHeuristic selects the optimal movement direction, while "
    "DistanceHeuristic adjusts the desired speed based on proximity to obstacles."
)
pdf.bullet(
    "PhysicalModel - defines the physics applied after heuristics. "
    "SocialForcePhysicalModel implements the acceleration equation combining "
    "desired velocity, agent-agent collision forces, and agent-wall collision forces."
)
pdf.body_text(
    "The Simulation class orchestrates the loop: each step applies heuristics "
    "to all agents, then applies the physical model, and finally advances agent "
    "state using a double-buffer mechanism."
)

# --- Page 3: Configuration window ---
pdf.chapter_title("3. Configuration")
pdf.body_text(
    "Before starting a simulation, the user configures its parameters through "
    "a dedicated configuration window:"
)
pdf.figure(f"{SS}/window_configuration.png", "Fig. 2: Configuration window.", w=100)
pdf.body_text("The following parameters are available:")
pdf.bullet("Agent count - the number of pedestrian agents (0-200).")
pdf.bullet("Scale coefficient - controls the magnitude of physical collision forces.")
pdf.bullet(
    "Destination radius - the radius around the goal within which an agent is considered to have arrived."
)
pdf.bullet(
    "Delay in ms - the time interval between simulation steps (controls animation speed)."
)
pdf.bullet("Computing engine - single-threaded or multi-threaded execution.")
pdf.bullet(
    "Map - selects the scenario layout (wall configuration and agent spawn positions)."
)

# --- Page 4: Scenario 1 ---
pdf.add_page()
pdf.chapter_title("4. Scenario: Bottleneck passage")
pdf.body_text(
    "The first scenario demonstrates a group of 110 agents moving through a "
    "narrow gap in a wall. This validates the core Social Force Model mechanics: "
    "goal-directed movement, obstacle avoidance, and physical collision handling."
)
pdf.two_figures(
    f"{SS}/sim_1_1.png",
    "Fig. 3a: Agents approach the wall.",
    f"{SS}/sim_1_2.png",
    "Fig. 3b: Crowd accumulates at the gap.",
)
pdf.body_text(
    "Agents begin on the left side and move towards a destination on the right. "
    "As they approach the wall, the direction heuristic steers them towards the "
    "narrow opening. Agents accumulate in front of the gap, forming a characteristic "
    "arching cluster - a well-known bottleneck congestion pattern."
)
pdf.two_figures(
    f"{SS}/sim_1_3.png",
    "Fig. 3c: Agents pass through the gap.",
    f"{SS}/sim_1_4.png",
    "Fig. 3d: Most agents have crossed.",
)
pdf.body_text(
    "Agents gradually pass through the bottleneck one by one or in small groups. "
    "The collision forces prevent agents from overlapping, while the distance "
    "heuristic causes agents to slow down as they approach the congested area. "
    "After crossing, agents spread out and continue towards the destination. "
    'This scenario successfully demonstrates the "freezing by heating" effect '
    "- when too many agents push simultaneously, the flow through the gap "
    "temporarily stalls."
)

pdf.chapter_title("5. Next steps")
pdf.body_text("Based on the PoC results, the following steps are planned:")
pdf.bullet(
    "Implement multi-group scenarios - introduce color-coded agent groups with "
    "different origins and destinations, as outlined in the Agent Definition "
    "document (e.g., four groups approaching a crossing from cardinal directions)."
)
pdf.bullet(
    "Add more map layouts - extend beyond the single bottleneck scenario to "
    "include multi-way crossing intersections and corridor configurations."
)
pdf.bullet(
    "Implement crowd pressure visualization - color-code agents based on the "
    "physical forces they experience, making it possible to visually identify "
    "high-pressure zones as described in the project scope."
)
pdf.bullet(
    "Enable runtime parameter switching - allow toggling heuristics and "
    "switching between single-threaded and multi-threaded computation "
    "during a running simulation."
)
pdf.bullet(
    "Validate emergent phenomena - verify that the expected behaviors identified "
    "in the State of the Art review (lane formation, freezing by heating, "
    "group mixing/separation) emerge in the implemented scenarios."
)

pdf.output(f"{DOC}/Proof_of_Concept.pdf")
print("PDF generated successfully.")
