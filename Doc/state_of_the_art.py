import os
import tempfile
import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
from fpdf import FPDF

FONT = "/System/Library/Fonts/Supplemental/Arial Unicode.ttf"
FONT_BOLD = "/System/Library/Fonts/Supplemental/Arial Bold.ttf"
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
                0,
                8,
                "State of the Art - Pedestrians Movement on Crossings",
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
        self.cell(8, 5.5, "\u2022")
        self.multi_cell(0, 5.5, text, align="J")
        self.ln(1)

    def formula(self, latex_str):
        img_path = render_latex(latex_str)
        self.ln(2)
        img_w = 70
        x = (210 - img_w) / 2
        self.image(img_path, x=x, w=img_w)
        self.ln(3)


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
pdf.cell(0, 10, "State of the Art", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.ln(10)
pdf.set_font("ArialU", "", 11)
pdf.set_text_color(60, 60, 60)
pdf.cell(
    0,
    7,
    "Overview of the most important models and methods for pedestrian simulation",
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
    "Kamil Barszczak, Rados\u0142aw Barszczak, Szymon Klempert",
    align="C",
    new_x="LMARGIN",
    new_y="NEXT",
)
pdf.ln(5)
pdf.set_font("ArialU", "", 10)
pdf.set_text_color(100, 100, 100)
pdf.cell(0, 6, "April 2026", align="C", new_x="LMARGIN", new_y="NEXT")

# --- Page 2: Introduction + SFM ---
pdf.add_page()

pdf.chapter_title("1. Introduction")
pdf.body_text(
    "With the growing number of mass events and increasing urban density, "
    "modeling pedestrian movement has become an increasingly important research "
    "area. It is useful in many fields such as building design, computer graphics, "
    "robot motion planning, and road traffic organization. Considering that each "
    "pedestrian has their own travel destination, character, and cultural background, "
    "predicting crowd movement may seem overly difficult. In practice, however, "
    "it has proven feasible through simulation models."
)
pdf.body_text(
    "Initially, researchers attempted to study pedestrian behavior directly by "
    "observing people, analyzing photographs, and reviewing street camera footage. "
    "Over time, the focus shifted to simulation models capable of computationally "
    "reproducing complex crowd dynamics. The following sections present an overview "
    "of the most important models and methods developed for simulating pedestrian "
    "movement, with particular emphasis on crossing scenarios and bottleneck situations."
)

pdf.chapter_title("2. Social Force Model (SFM)")

pdf.section_title("2.1 Origins and core concept")
pdf.body_text(
    "The Social Force Model (SFM), proposed by Dirk Helbing and Peter Molnar "
    "in 1998, is one of the most widely recognized and extensively studied models "
    "of pedestrian dynamics. It has been thoroughly investigated by researchers "
    "worldwide and has found commercial applications. SFM is a continuous model "
    "which suggests that pedestrians are subject to social forces - these forces "
    "represent a measure of an individual's internal motivations to perform "
    "certain actions."
)

pdf.section_title("2.2 Forces acting on pedestrians")
pdf.body_text(
    "In the Social Force Model, each pedestrian is influenced by three "
    "categories of forces:"
)
pdf.bullet(
    "Driving force in the desired direction of motion - acceleration towards "
    "the pedestrian's goal at their preferred speed."
)
pdf.bullet(
    "Forces from other pedestrians - repulsive interactions maintaining personal "
    "space, as well as an attraction effect occurring among acquaintances."
)
pdf.bullet(
    "Forces from boundaries and obstacles - repulsive forces from walls, barriers, "
    "and other physical boundaries in the environment."
)
pdf.body_text(
    "The model can be extended with additional behaviors such as overtaking, "
    "waiting, decelerating, collision avoidance, following others, individual "
    "pedestrian preferences (e.g., the desire to move in a group), and dynamic "
    "route choice in open spaces. However, such detailed representations cannot "
    "be used in larger computer simulations due to computational complexity, "
    "which grows quadratically with the number of agents."
)

pdf.section_title("2.3 Strengths and limitations")
pdf.body_text(
    "The main advantage of this methodology is the high simulation accuracy and "
    "the precision of reproducing microscopic interactions between individual "
    "objects and their environment. Computer crowd simulations show that the model "
    "reproduces certain pedestrian behaviors very realistically. Among the drawbacks "
    "are the relatively low computational efficiency and difficulties in reproducing "
    "certain events. Due to high computational costs, this method has limited "
    "scalability."
)

pdf.section_title('2.4 Interpersonal distance and the "faster-is-slower" effect')
pdf.body_text(
    "The Social Force Model enables the definition of minimal interpersonal "
    'distance, which allows observing the "faster-is-slower" effect. In normal '
    "situations, flow through a narrow passage is reduced but the chance of "
    "physical interactions remains low. In stressful situations, the interpersonal "
    "distance decreases, resulting in increased flow but also an increased chance "
    "of physical contact. In extreme cases, the interpersonal distance becomes so "
    "small that physical interactions between agents cause mutual blocking, "
    'effectively preventing passage. This phenomenon is known as "freezing by '
    'heating" - when the crowd pushes too hard, pedestrians slow down significantly '
    "upon encountering congestion."
)

# --- Page 3: Behavioral heuristics model ---
pdf.chapter_title("3. Behavioral heuristics model")

pdf.section_title("3.1 Overview")
pdf.body_text(
    "An important extension of the original SFM is the model proposed by "
    'Moussa\u00efda, Helbing, and Theraulaz in the article "How simple rules '
    'determine pedestrian behavior and crowd disasters". This model solves some '
    "of the problems associated with the original Social Force Model by relying "
    "on behavioral heuristics instead of purely physics-based force calculations."
)

pdf.section_title("3.2 Key heuristics")
pdf.body_text("The model employs two main heuristics governing pedestrian behavior:")
pdf.body_text(
    "Direction heuristic: The pedestrian selects a desired movement direction "
    "\u03b1_des, associated with the most direct path to their goal while taking "
    "into account possible collisions. This direction is computed by minimizing "
    "a distance function d(\u03b1), which considers the distance to the first obstacle "
    "in each candidate direction combined with the deviation from the direct "
    "path to the goal:"
)
pdf.formula(
    r"d(\alpha) = d_{max}^2 + f(\alpha)^2 - 2 \cdot d_{max} \cdot f(\alpha) \cdot \cos(\alpha_0 - \alpha)"
)
pdf.body_text(
    "Distance heuristic: The pedestrian maintains a distance to the first obstacle "
    "on their path that ensures a minimum time to collision of at least \u03c4 "
    "(the relaxation time). The desired speed is calculated as:"
)
pdf.formula(r"v_{des}(t) = \min(v^0_i,\; d_h \,/\, \tau)")
pdf.body_text(
    "where v0i is the comfortable walking speed and dh is the distance to the "
    "nearest obstacle. The pedestrian's acceleration resulting from this "
    "heuristic is:"
)
pdf.formula(r"a_i = \frac{v_{des} - v_i}{\tau}")

pdf.section_title("3.3 Collision effects")
pdf.body_text(
    "In high-density situations, the model also accounts for physical contact "
    "forces between pedestrians. The collision force between two pedestrians "
    "i and j is calculated as:"
)
pdf.formula(r"f_{ij} = k \cdot g(r_i + r_j - d_{ij}) \cdot n_{ij}")
pdf.body_text(
    "where k is a scaling coefficient, g(x) is a function returning 0 when "
    "pedestrians are not in contact (otherwise x), r_i is the body radius "
    "(proportional to mass: m_i / 320), d_ij is the distance between centers "
    "of mass, and n_ij is the unit vector between them. Similarly, the wall "
    "collision force is:"
)
pdf.formula(r"f_{iW} = k \cdot g(r_i - d_{iW}) \cdot n_{iW}")

pdf.section_title("3.4 Final acceleration equation")
pdf.body_text(
    "The final acceleration vector of a pedestrian combines all components - "
    "acceleration towards the desired velocity and physical collision forces "
    "from other pedestrians and walls:"
)
pdf.formula(
    r"a_i = \frac{v_{des} - v_i}{\tau} + \sum_j \frac{f_{ij}}{m_i} + \sum_W \frac{f_{iW}}{m_i}"
)

# --- Page 4: Agent-based approaches ---
pdf.chapter_title("4. Agent-based and cellular automata approaches")

pdf.section_title("4.1 Combining SFM with agent-based programming")
pdf.body_text(
    "The Social Force Model lends itself well to integration with agent-based "
    "programming and cellular automata. While combining it with a classical "
    "synchronous and homogeneous cellular automaton does not yield good results, "
    "using so-called extended automata produces much better outcomes. Even more "
    "effective is the use of agent-based systems, as they allow the reproduction "
    "of complex interactions."
)

pdf.section_title("4.2 Macroscopic character of agent units")
pdf.body_text(
    "Within the Social Force Model framework, each unit has a macroscopic "
    "character - every person is represented by a virtual agent possessing an "
    "individual set of variables. The open nature of the model allows defining "
    "dedicated behavior sets for specific scenarios: appropriate obstacle avoidance, "
    "selection of suitable movement speed, finding the best path to the goal, "
    "and certain characteristic patterns. This enables simulating the movement "
    "of people with different roles in a crowd (e.g., police officer, mother "
    "with child, child, individual person, group of friends)."
)

# --- Section 5: AI approaches ---
pdf.chapter_title("5. AI-based approaches in pedestrian modeling")

pdf.body_text(
    "In recent years, artificial intelligence techniques have been increasingly "
    "applied to pedestrian movement modeling, complementing traditional "
    "physics-based approaches."
)
pdf.body_text(
    "Gupta et al. [8] proposed Social GAN - a model that combines recurrent "
    "sequence-to-sequence architecture with generative adversarial networks "
    "to predict pedestrian trajectories. The key insight is that human motion "
    "is inherently multimodal: given past movement, numerous socially acceptable "
    "future trajectories exist. Social GAN introduces a novel pooling mechanism "
    "to aggregate information across multiple pedestrians simultaneously, and "
    "uses a variety loss function to encourage diverse predictions. The model "
    "outperforms prior work in terms of accuracy, variety, collision avoidance, "
    "and computational complexity, making it applicable to autonomous vehicles "
    "and social robots navigating crowded environments."
)
pdf.body_text(
    "Kreiss [9] proposed Deep Social Force - a differentiable implementation "
    "of the classical Social Force Model that replaces fixed interaction "
    "potentials with neural network function approximators. This hybrid approach "
    "addresses two key limitations of traditional SFM: unnatural locking behavior "
    "on head-on collision paths, and the inability to capture region-specific "
    "avoidance biases (e.g., pedestrians preferring to pass on the right or left "
    "depending on geographic conventions). The work demonstrates that sharper "
    "potential functions eliminate locking issues, while asymmetric learned "
    "potentials naturally produce directional avoidance patterns. By retaining "
    "the interpretable structure of SFM while leveraging data-driven learning, "
    "Deep Social Force represents a promising direction for scenarios where "
    "traditional parameter calibration is insufficient."
)

# --- Section 6: References ---
pdf.chapter_title("6. References")
pdf.set_font("ArialU", "", 9)
refs = [
    "1. Helbing, D. & Molnar, P. (1998). Social Force Model for Pedestrian Dynamics. Physical Review E.",
    '2. Moussa\u00efda, M., Helbing, D., & Theraulaz, G. "How simple rules determine pedestrian behavior and crowd disasters." PNAS. (doi: 10.1073/pnas.1016507108)',
    '3. "Understanding Social-Force Model in Psychological Principles of Collective Behavior." ResearchGate.',
    '4. "Crowd Management - Social Force Model." IJERT.',
    '5. "Social Force Model for Pedestrian Dynamics." ResearchGate.',
    '6. "Passenger and Pedestrian Modelling at Transport Facilities." ResearchGate.',
    '7. "Simulating Crowd Dynamics." ScienceDirect. (doi: 10.1016/j.compenvurbsys.2015.09.005)',
    '8. Gupta, A., Johnson, J., Fei-Fei, L., Savarese, S., & Alahi, A. (2018). "Social GAN: Socially Acceptable Trajectories with Generative Adversarial Networks." CVPR 2018. (arXiv: 1803.10892)',
    '9. Kreiss, S. (2021). "Deep Social Force." arXiv preprint. (arXiv: 2109.12081)',
]
for r in refs:
    pdf.set_x(15)
    pdf.multi_cell(175, 5, r)
    pdf.ln(1)

pdf.output("/Users/kamilbarszczak/Desktop/Crowd_pressure/doc/State_of_the_Art.pdf")
print("PDF generated successfully.")
