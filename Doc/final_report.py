import glob
import os
import tempfile

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
from fpdf import FPDF

FONT = "/System/Library/Fonts/Supplemental/Arial Unicode.ttf"
FONT_BOLD = "/System/Library/Fonts/Supplemental/Arial Bold.ttf"
DOC = "/Users/kamilbarszczak/Desktop/studies/Crowd_pressure/Doc"
FIG = f"{DOC}/figures"
SIMS = f"{DOC}/simulations"
FORMULA_DIR = tempfile.mkdtemp()
FORMULA_COUNTER = 0


def render_latex(latex_str):
    global FORMULA_COUNTER
    FORMULA_COUNTER += 1
    path = os.path.join(FORMULA_DIR, f"eq_{FORMULA_COUNTER}.png")
    fig, ax = plt.subplots(figsize=(4, 0.4))
    ax.axis("off")
    ax.text(
        0.5,
        0.5,
        f"${latex_str}$",
        fontsize=11,
        ha="center",
        va="center",
        transform=ax.transAxes,
    )
    fig.patch.set_facecolor("#f0f3f8")
    fig.savefig(
        path, dpi=200, bbox_inches="tight", pad_inches=0.08, facecolor="#f0f3f8"
    )
    plt.close(fig)
    return path


class PDF(FPDF):
    def header(self):
        if self.page_no() > 1:
            self.set_font("ArialU", "", 9)
            self.set_text_color(128, 128, 128)
            self.cell(
                0, 8, "Final Report - Pedestrians Movement on Crossings", align="C"
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
        if self.get_y() > 240:
            self.add_page()
        self.ln(1)
        self.set_font("ArialB", "", 14)
        self.set_text_color(0, 51, 102)
        self.cell(0, 10, title, new_x="LMARGIN", new_y="NEXT")
        self.set_draw_color(0, 51, 102)
        self.line(10, self.get_y(), 200, self.get_y())
        self.ln(3)

    def section_title(self, title):
        if self.get_y() > 255:
            self.add_page()
        self.set_font("ArialB", "", 12)
        self.set_text_color(0, 70, 130)
        self.cell(0, 8, title, new_x="LMARGIN", new_y="NEXT")
        self.ln(1)

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

    def formula(self, latex_str):
        img_path = render_latex(latex_str)
        self.ln(1)
        img_w = 75
        self.image(img_path, x=(210 - img_w) / 2, w=img_w)
        self.ln(2)

    def figure(self, path, caption, w=150):
        if not os.path.exists(path):
            return
        if self.get_y() + w * 0.62 > 270:
            self.add_page()
        self.image(path, x=(210 - w) / 2, w=w)
        self.ln(1)
        self.set_font("ArialU", "", 8)
        self.set_text_color(110, 110, 110)
        self.multi_cell(0, 4, caption, align="C")
        self.ln(3)

    def figure_row(self, paths, caption, w=92):
        from PIL import Image

        paths = [p for p in paths if os.path.exists(p)]
        if not paths:
            return
        n = len(paths)
        gap = 4
        w = min(w, (190 - (n - 1) * gap) / n)
        height = max(w * Image.open(p).height / Image.open(p).width for p in paths)
        if self.get_y() + height + 8 > 276:
            self.add_page()
        total = n * w + (n - 1) * gap
        x = (210 - total) / 2
        y = self.get_y()
        for p in paths:
            self.image(p, x=x, y=y, w=w)
            x += w + gap
        self.set_xy(10, y + height + 1)
        self.set_font("ArialU", "", 8)
        self.set_text_color(110, 110, 110)
        self.multi_cell(0, 4, caption, align="C")
        self.ln(3)


def sim_frames(folder, picks):
    files = sorted(glob.glob(f"{SIMS}/{folder}/*.png"))
    return [files[i] for i in picks if i < len(files)]


pdf = PDF()
pdf.set_auto_page_break(auto=True, margin=18)
pdf.add_font("ArialU", "", FONT)
pdf.add_font("ArialB", "", FONT_BOLD)

# ============ TITLE PAGE (a) ============
pdf.add_page()
pdf.ln(22)
pdf.set_font("ArialB", "", 24)
pdf.set_text_color(0, 51, 102)
pdf.multi_cell(0, 12, "Pedestrians Movement on Crossings", align="C")
pdf.ln(4)
pdf.set_font("ArialU", "", 16)
pdf.set_text_color(80, 80, 80)
pdf.cell(0, 10, "Final Report", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.ln(3)
pdf.set_font("ArialU", "", 11)
pdf.set_text_color(60, 60, 60)
pdf.cell(
    0,
    7,
    "Agent-based simulation of crowd pressure at bottlenecks and crossings",
    align="C",
    new_x="LMARGIN",
    new_y="NEXT",
)
pdf.ln(16)
pdf.set_draw_color(0, 51, 102)
pdf.line(60, pdf.get_y(), 150, pdf.get_y())
pdf.ln(10)
pdf.set_font("ArialU", "", 11)
pdf.set_text_color(40, 40, 40)
pdf.cell(0, 7, "Course: Agent Systems", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.ln(5)
pdf.set_font("ArialB", "", 11)
pdf.cell(0, 7, "Team members:", align="C", new_x="LMARGIN", new_y="NEXT")
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
pdf.cell(0, 6, "June 2026", align="C", new_x="LMARGIN", new_y="NEXT")

# ============ 1. PROBLEM DESCRIPTION (b) ============
pdf.add_page()
pdf.chapter_title("1. Problem description")
pdf.body_text(
    "With the growing number of mass events and increasing urban density, understanding "
    "and predicting the movement of pedestrian crowds has become a safety-critical "
    "problem. When many people try to pass through a constrained space - a doorway, a "
    "corridor, or a crossing where several streams meet - local density can rise to "
    "dangerous levels. The physical force transmitted through a tightly packed crowd, "
    "known as crowd pressure, is the direct cause of crowd disasters: people are no "
    "longer in control of their own motion and are pushed by the bodies around them."
)
pdf.body_text(
    "The goal of this project is to build an agent-based simulator that reproduces this "
    "phenomenon from the bottom up. Each pedestrian is modelled as an autonomous agent "
    "following simple local rules; no central controller coordinates the crowd. We then "
    "measure how the emergent, collective quantities - crowd pressure, walking speed, "
    "evacuation time and the fraction of agents that successfully pass - depend on the "
    "size of the crowd, the geometry of the environment and the behavioural rules used. "
    "The central question is: under which conditions does an orderly flow degrade into a "
    "jammed, high-pressure state?"
)
pdf.body_text(
    "Concretely, the simulator targets three representative scenarios: (i) two groups "
    "passing in counter-flow through a single central constriction, (ii) four groups "
    "meeting at a crossing from the four cardinal directions, and (iii) two groups "
    "crossing through a single narrow gap in a dividing wall. These scenarios let us "
    "study both bottleneck congestion and the mixing of opposing pedestrian streams."
)

# ============ 2. STATE OF THE ART (c) ============
pdf.chapter_title("2. State of the art")
pdf.section_title("2.1 The Social Force Model")
pdf.body_text(
    "The Social Force Model (SFM), proposed by Helbing and Molnar in 1998, is one of the "
    "most widely studied models of pedestrian dynamics. It is a continuous model in which "
    "pedestrians are subject to social forces representing their internal motivations. "
    "Each pedestrian is influenced by three categories of forces: a driving force toward "
    "their goal at a preferred speed, repulsive (and occasionally attractive) forces from "
    "other pedestrians, and repulsive forces from walls and obstacles. The model "
    "reproduces microscopic interactions very realistically, but its computational cost "
    "grows quadratically with the number of agents, which limits scalability."
)
pdf.body_text(
    'A key property of the SFM is that it can reproduce the "faster-is-slower" effect: '
    "in stressful situations interpersonal distance shrinks, increasing flow but also the "
    "chance of physical contact. In the extreme, mutual blocking between agents prevents "
    'passage entirely - the "freezing by heating" phenomenon, where a crowd that pushes '
    "too hard slows itself down."
)
pdf.section_title("2.2 The behavioural heuristics model")
pdf.body_text(
    "An important extension is the behavioural-heuristics model of Moussaid, Helbing and "
    'Theraulaz ("How simple rules determine pedestrian behavior and crowd disasters"). '
    "Instead of relying purely on physics-based forces, it governs navigation with two "
    "heuristics. The direction heuristic selects the desired heading by minimising a "
    "distance function that balances progress toward the goal against the distance to the "
    "first obstacle in each candidate direction:"
)
pdf.formula(
    r"d(\alpha) = d_{max}^2 + f(\alpha)^2 - 2\, d_{max}\, f(\alpha)\, \cos(\alpha_0 - \alpha)"
)
pdf.body_text(
    "The distance heuristic adapts the desired speed so that the time-to-collision stays "
    "above the relaxation time tau, giving the desired speed and the resulting "
    "acceleration toward it:"
)
pdf.formula(r"v_{des} = \min(v^0_i,\; d_h / \tau) \qquad a_i = (v_{des} - v_i)/\tau")
pdf.body_text(
    "In dense situations the model adds physical contact forces between agents and against "
    "walls, proportional to the body overlap; the full acceleration combines the driving "
    "term with these contact forces:"
)
pdf.formula(
    r"a_i = \frac{v_{des} - v_i}{\tau} + \sum_j \frac{f_{ij}}{m_i} + \sum_W \frac{f_{iW}}{m_i}"
)
pdf.section_title("2.3 AI-based approaches")
pdf.body_text(
    "Recent work applies machine learning to pedestrian modelling. Social GAN (Gupta et "
    "al., 2018) couples a sequence-to-sequence recurrent network with a generative "
    "adversarial network to predict multimodal, socially acceptable trajectories. Deep "
    "Social Force (Kreiss, 2021) makes the classical SFM differentiable, replacing fixed "
    "interaction potentials with neural function approximators; this removes the unnatural "
    "head-on locking of the original model and learns region-specific avoidance biases "
    "(e.g. passing on the right). These hybrid approaches keep the interpretable structure "
    "of the SFM while leveraging data."
)

# ============ 3. AGENT DEFINITION (d) ============
pdf.chapter_title("3. Definition of agents")
pdf.body_text(
    "In our system an agent represents a single pedestrian navigating a shared physical "
    "environment. Each agent is autonomous: it perceives its local surroundings, decides "
    "its movement using the behavioural heuristics, and interacts with others and with "
    "obstacles through physical and social forces. There is no central controller - the "
    "collective behaviour of the crowd emerges from individual rules."
)
pdf.section_title("3.1 Agent attributes")
pdf.bullet("Mass m (60-90 kg), which also sets the body radius r = m / 50.")
pdf.bullet(
    "Comfortable speed v0 (1.5-2.0), the preferred walking speed when unobstructed."
)
pdf.bullet("Vision angle phi (1.2-1.3 rad), the field of view scanned for obstacles.")
pdf.bullet("Maximum vision distance d_max (100-105), the farthest detection range.")
pdf.bullet(
    "Relaxation time tau (0.45-0.55 s), how quickly velocity adapts to the desired one."
)
pdf.bullet(
    "Desired position - the destination point, and a group colour identifying its stream."
)
pdf.body_text(
    "Each agent also keeps dynamic state updated every step: position, velocity, desired "
    "velocity, and the instantaneous crowd pressure exerted on it. Position and velocity "
    "use a double-buffering scheme so that agents can be updated concurrently without "
    "read-write conflicts."
)
pdf.section_title("3.2 Agent types and groups")
pdf.body_text(
    "All agents share the same behavioural model; they are differentiated by "
    "parameterisation and group membership. A group is defined by a common spawn area and "
    "destination and is drawn in one colour. Within a group, mass, comfortable speed, "
    "vision and relaxation time are sampled from the ranges above, producing realistic "
    "heterogeneity. The same mechanism could express richer roles (a child with reduced "
    "speed, a police officer with different force parameters) purely by changing the "
    "initialisation parameters."
)
pdf.section_title("3.3 Interactions and expected emergent behaviour")
pdf.body_text(
    "Agents do not exchange explicit messages; all interaction is implicit and mediated "
    "by the shared environment. Three interaction types are modelled: agent-agent "
    "repulsion when body radii overlap, agent-wall repulsion near walls, and "
    "heuristic-based proactive avoidance before contact occurs. From these local rules we "
    "expect several collective phenomena to emerge: lane formation in counter-flow, "
    "arching congestion before bottlenecks, group mixing and separation at crossings, and "
    "- under excessive pressure - freezing by heating."
)

# ============ 4. APPLICATION DESCRIPTION (e) ============
pdf.chapter_title("4. The simulator application")
pdf.body_text(
    "The simulator is implemented in Java 17 with a JavaFX graphical interface. The "
    "design is deliberately modular so that heuristics, the computing engine, the physical "
    "model and the map/agent layout can each be swapped without touching the application "
    "core."
)
pdf.section_title("4.1 Architecture")
pdf.bullet(
    "Physical model (SocialForcePhysicalModel): turns the social and contact forces into "
    "per-step velocity and position updates, and records the crowd pressure on each agent."
)
pdf.bullet(
    "Heuristics (Direction, Distance): set each agent's desired velocity; they can be "
    "toggled on/off at runtime."
)
pdf.bullet(
    "Computing engine (single-threaded or thread-pool based): evaluates agent updates, "
    "exploiting the double-buffered state for safe parallelism."
)
pdf.bullet(
    "Initializers: procedurally build the wall layout (board) and the agent groups; "
    "eight maps are provided, from a single bottleneck to four-way crossings."
)
pdf.section_title("4.2 Graphical interface and crowd-pressure visualisation")
pdf.body_text(
    "A configuration window lets the user set the number of agents, the force scale "
    "coefficient, the destination radius, the simulation time step, the computing engine "
    "and the map. The simulation window then animates the crowd in real time with "
    "start / stop / reset controls and a running clock. Each agent is drawn in its group "
    "colour when calm and is interpolated toward red as the crowd pressure on it rises, so "
    "high-pressure regions are immediately visible. Destination areas are drawn as rings "
    "and walls as solid lines."
)
pdf.section_title("4.3 Data collection")
pdf.body_text(
    "For quantitative analysis the simulator can record, at every step, a CSV row per "
    "agent containing its position, velocity, speed, crowd pressure and stopped flag. A "
    "headless batch runner sweeps the parameter space (map, heuristics, crowd size) over "
    "several repetitions without the GUI, writing a per-run summary plus detailed "
    "per-step traces. All figures in Section 5 are produced from these files."
)
pdf.section_title("4.4 Example execution sessions")
pdf.body_text(
    "The three figures below show representative runs. In each, time advances left to "
    "right; agents fade from their group colour to red as local pressure grows."
)
pdf.figure_row(
    sim_frames("simulation_1", [1, 4, 7]),
    "Session 1 - two groups (red, green) in counter-flow through the central constriction.",
)
pdf.figure_row(
    sim_frames("simulation_2", [1, 4, 8]),
    "Session 2 - four groups crossing; the streams intermix and build pressure at the centre.",
)
pdf.figure_row(
    sim_frames("simulation_3", [1, 4, 7]),
    "Session 3 - two groups (red, blue) through a single narrow gap in a dividing wall.",
)

# ============ 5. DATA ANALYSIS (f) ============
pdf.chapter_title("5. Analysis of results")
pdf.section_title("5.1 Experimental setup")
pdf.body_text(
    "We ran a parameter sweep with the headless batch runner. Two maps were studied: Map7, "
    "a counter-flow bottleneck where two groups cross through a central opening, and Map1, "
    "an open corridor with a single dividing wall. For each map we compared two behavioural "
    "regimes - heuristics off (raw social forces only) and heuristics on (direction + "
    "distance) - across crowd sizes of 25, 50, 75, 100 and 150 agents, with three "
    "repetitions per configuration (60 runs total). The force scale coefficient (1000), "
    "destination radius (3) and time step (50 ms) were held fixed, and each run was capped "
    "at 125 s of simulated time. For every run we recorded the mean and maximum crowd "
    "pressure, the mean walking speed, the evacuated fraction and, when all agents cleared, "
    "the full-evacuation time."
)

pdf.section_title("5.2 Crowd pressure grows super-linearly with crowd size")
pdf.figure(
    f"{FIG}/pressure_vs_n.png",
    "Figure 1. Mean crowd pressure versus number of agents (error bars: std over repetitions).",
)
pdf.body_text(
    "The mean pressure rises faster than linearly with the number of agents. On the Map7 "
    "bottleneck with heuristics it grows from about 24 at 25 agents to about 214 at 150 "
    "agents - a roughly nine-fold increase for a six-fold increase in crowd size. This is "
    "expected: pressure accumulates from pairwise body overlaps, and both the number of "
    "simultaneous contacts per agent and the depth of each overlap increase as the crowd "
    "densifies, so their product grows super-linearly. The practical implication is that "
    "doubling the crowd more than doubles the force individuals must withstand, which is "
    "precisely why bottlenecks become dangerous well before they look completely full."
)
pdf.body_text(
    "Pressure is markedly higher with heuristics enabled than without. This may seem "
    "counter-intuitive, but it is the correct behaviour: without heuristics agents do not "
    "actively steer toward their goal, so they barely enter the constriction and never "
    "build up against each other (see 5.3). With heuristics they purposefully push into "
    "the bottleneck, which is exactly the situation that generates real crowd pressure."
)

pdf.section_title("5.3 Heuristics are essential for navigation")
pdf.figure(
    f"{FIG}/speed_vs_n.png",
    "Figure 2. Mean speed of moving agents versus crowd size, with and without heuristics.",
)
pdf.body_text(
    "Without heuristics the mean speed stays near zero (about 0.01-0.05) and no agent ever "
    "reaches its destination: the raw social forces alone keep agents apart but provide no "
    "purposeful navigation around the wall toward the goal. With heuristics enabled agents "
    "move at close to their comfortable speed (about 1.65-1.73). This confirms that the "
    "behavioural heuristics - not the contact forces - are what convert the model into "
    "goal-directed pedestrian motion. It also shows a mild but consistent slowdown as the "
    "crowd grows (1.71 at 25 agents down to 1.66 at 100 on Map7), an early sign of "
    "congestion that becomes decisive in the evacuation metrics below."
)

pdf.section_title("5.4 Evacuation degrades as the crowd grows")
pdf.figure_row(
    [f"{FIG}/evac_fraction_vs_n.png", f"{FIG}/evac_time_vs_n.png"],
    "Figure 3. Left: fraction evacuated within 125 s. Right: full-evacuation time vs crowd size (Map7).",
    w=92,
)
pdf.body_text(
    "On the Map7 bottleneck every agent clears the opening at 25 and 50 agents, but the "
    "evacuated fraction then falls - to about 98% at 75 agents, 97% at 100 and only ~92% "
    "at 150. At the same time the full-evacuation time rises with crowd size. Together "
    "these are the signature of a capacity-limited bottleneck: beyond a critical crowd "
    "size the opening can no longer clear everyone within the time window, and a residual "
    'jammed cluster remains. This is the macroscopic counterpart of the "faster-is-slower" '
    'and "freezing-by-heating" effects described in the literature - more agents pressing '
    "into the same gap reduce, rather than increase, the effective throughput."
)

pdf.section_title("5.5 Pressure is localised at the bottleneck")
pdf.figure_row(
    [f"{FIG}/Map7_density_heatmap.png", f"{FIG}/Map7_pressure_heatmap.png"],
    "Figure 4. Map7, 150 agents: time-averaged occupancy (left) and mean crowd pressure (right).",
    w=92,
)
pdf.body_text(
    "The spatial maps confirm where the danger concentrates. Occupancy is spread along the "
    "approach corridors, but crowd pressure is sharply localised at the central opening, "
    "reaching values far above the surrounding area. In other words, an agent can be in a "
    "moderately dense region and feel little force, yet experience extreme pressure the "
    "moment it enters the constriction. This matches real observations that crowd "
    "disasters occur at specific pinch points rather than uniformly across a venue, and it "
    "validates our per-agent pressure metric as a meaningful indicator."
)

pdf.section_title("5.6 Lane formation and stream mixing")
pdf.figure(
    f"{FIG}/Map7_trajectories.png",
    "Figure 5. Map7, 150 agents: trajectories coloured by group (red and green counter-flows).",
)
pdf.body_text(
    "The trajectory bundle shows the two opposing groups converging on the same opening "
    "and intermixing tightly before separating on the far side. The dense overlap of red "
    "and green paths at the centre is the geometric origin of the pressure peak in Figure "
    "4: counter-flowing agents must negotiate the same few square pixels of free space, "
    "repeatedly blocking one another - the micro-mechanism behind the reduced throughput."
)

pdf.section_title("5.7 Speed-density relation (fundamental diagram)")
pdf.figure(
    f"{FIG}/Map7_fundamental_diagram.png",
    "Figure 6. Local speed-density relation and flow measured in a window around the bottleneck.",
)
pdf.body_text(
    "Measuring local density and speed inside a window around the opening reproduces the "
    "classical fundamental diagram of pedestrian traffic: as local density increases, mean "
    "speed decreases, and the flow (density x speed) saturates rather than growing without "
    "bound. The fact that an emergent, well-known traffic relationship arises from our "
    "simple per-agent rules is a strong validation that the model captures the right "
    "physics, not just plausible-looking motion."
)

pdf.section_title("5.8 Temporal dynamics of a run")
pdf.figure(
    f"{FIG}/Map7_timeseries.png",
    "Figure 7. Map7, 150 agents: active agents, mean pressure and mean speed over time.",
)
pdf.body_text(
    "The time series tells the story of a single run. The number of active agents falls "
    "steadily as agents clear the opening; mean pressure rises to a peak while the crowd is "
    "packed against the constriction and then decays as the queue drains; mean speed is "
    "depressed during the congested phase and recovers toward the end. The peak-pressure "
    "phase is exactly the interval during which a real crowd would be at greatest risk."
)

# ============ 6. CONCLUSIONS (g) ============
pdf.chapter_title("6. Conclusions and further research")
pdf.section_title("6.1 Conclusions")
pdf.body_text(
    "We built a fully agent-based pedestrian simulator combining the Social Force Model "
    "with behavioural heuristics, instrumented it to record per-agent data, and analysed a "
    "60-run parameter sweep. The main findings are: (1) crowd pressure grows "
    "super-linearly with crowd size, so risk escalates faster than density alone suggests; "
    "(2) the behavioural heuristics, not the contact forces, are what produce purposeful "
    "navigation; (3) bottleneck evacuation degrades beyond a critical crowd size, "
    "reproducing the faster-is-slower effect; (4) pressure is sharply localised at "
    "constrictions; and (5) the model reproduces the classical speed-density fundamental "
    "diagram as an emergent property. The simulator therefore captures the qualitative "
    "physics of real crowd disasters from simple local rules."
)
pdf.section_title("6.2 Further research")
pdf.bullet(
    "Machine learning: replace the hand-tuned heuristics and force potentials with "
    "learned ones, following Deep Social Force, or use Social GAN-style trajectory "
    "prediction to calibrate parameters against real pedestrian data."
)
pdf.bullet(
    "Richer agent types: children, elderly, groups travelling together, and responders, "
    "expressed through the existing parameterisation."
)
pdf.bullet(
    "Mitigation studies: test how obstacles placed before an exit (a well-known "
    "counter-intuitive remedy), staggered release, or wider openings reduce peak pressure."
)
pdf.bullet(
    "Scalability: spatial partitioning to bring the quadratic neighbour search down and "
    "allow much larger crowds."
)
pdf.section_title("6.3 Ethical aspects")
pdf.body_text(
    "Crowd simulation is a dual-use tool. Used responsibly it improves the safety of "
    "venues, transport hubs and evacuation plans and can save lives. The same models, "
    "however, could in principle inform crowd-control tactics that endanger people, and "
    "calibrating models on real pedestrian footage raises privacy questions. Results must "
    "also be communicated with their limitations clear: a simulation is an approximation, "
    "and over-confident conclusions about a specific venue could give a false sense of "
    "safety. Our model is a research prototype and is not calibrated for certifying any "
    "real-world facility."
)

# ============ REFERENCES ============
pdf.chapter_title("7. References")
pdf.set_font("ArialU", "", 9)
refs = [
    "1. Helbing, D. & Molnar, P. (1998). Social Force Model for Pedestrian Dynamics. Physical Review E.",
    "2. Moussaid, M., Helbing, D., & Theraulaz, G. How simple rules determine pedestrian behavior and crowd disasters. PNAS. (doi: 10.1073/pnas.1016507108)",
    "3. Understanding Social-Force Model in Psychological Principles of Collective Behavior. ResearchGate.",
    "4. Social Force Model for Pedestrian Dynamics. ResearchGate.",
    "5. Gupta, A., Johnson, J., Fei-Fei, L., Savarese, S., & Alahi, A. (2018). Social GAN: Socially Acceptable Trajectories with Generative Adversarial Networks. CVPR 2018. (arXiv: 1803.10892)",
    "6. Kreiss, S. (2021). Deep Social Force. arXiv preprint. (arXiv: 2109.12081)",
]
for r in refs:
    pdf.set_x(15)
    pdf.multi_cell(180, 5, r)
    pdf.ln(1)

pdf.output(f"{DOC}/Final_Report.pdf")
print("PDF generated:", f"{DOC}/Final_Report.pdf")
