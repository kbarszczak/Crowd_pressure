import glob

from fpdf import FPDF

FONT = "/System/Library/Fonts/Supplemental/Arial Unicode.ttf"
FONT_BOLD = "/System/Library/Fonts/Supplemental/Arial Bold.ttf"
DOC = "/Users/kamilbarszczak/Desktop/studies/Crowd_pressure/Doc"
SIMS = f"{DOC}/simulations"

SIMULATIONS = [
    (
        "simulation_1",
        "Simulation 1 - Two-group bottleneck",
        "Two pedestrian groups (red, green) approach a central constriction from "
        "opposite sides and must pass through the same narrow opening in counter-flow. "
        "Frames are ordered in time; agent colour shifts from the group colour towards "
        "red as the local crowd pressure increases.",
    ),
    (
        "simulation_2",
        "Simulation 2 - Four-group crossing",
        "Four groups (red, green, blue, orange) enter an intersection from the four "
        "cardinal directions, each heading to the opposite exit. The groups intermix at "
        "the centre before separating again - note the pressure build-up where the four "
        "streams overlap.",
    ),
    (
        "simulation_3",
        "Simulation 3 - Single narrow gap",
        "Two groups (red, blue) cross in opposite directions through a single narrow gap "
        "in a dividing wall. This is the most severe constriction and produces the "
        "longest clearance time and the highest sustained pressure at the opening.",
    ),
]


class PDF(FPDF):
    def header(self):
        if self.page_no() > 1:
            self.set_font("ArialU", "", 9)
            self.set_text_color(128, 128, 128)
            self.cell(
                0,
                8,
                "Simulation Sessions - Pedestrians Movement on Crossings",
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
        self.ln(3)

    def body_text(self, text):
        self.set_font("ArialU", "", 10)
        self.set_text_color(30, 30, 30)
        self.multi_cell(0, 5.5, text, align="J")
        self.ln(2)

    def frame_grid(self, frames, cols=3):
        cell_w = (210 - 20 - (cols - 1) * 3) / cols
        cell_h = cell_w * 0.6
        gap_x, gap_y, caption_h = 3, 9, 4
        row_h = cell_h + gap_y + caption_h
        x0 = 10
        y = self.get_y() + 2
        for i, path in enumerate(frames):
            col = i % cols
            if col == 0 and i > 0:
                y += row_h
            if col == 0 and y + row_h > 280:
                self.add_page()
                y = self.get_y()
            x = x0 + col * (cell_w + gap_x)
            self.image(path, x=x, y=y, w=cell_w)
            self.set_xy(x, y + cell_h + 0.5)
            self.set_font("ArialU", "", 7)
            self.set_text_color(110, 110, 110)
            self.cell(cell_w, 3.5, f"frame {i + 1}", align="C")
        self.set_xy(10, y + row_h)


def frames_for(folder):
    return sorted(glob.glob(f"{SIMS}/{folder}/*.png"))


pdf = PDF()
pdf.set_auto_page_break(auto=True, margin=18)
pdf.add_font("ArialU", "", FONT)
pdf.add_font("ArialB", "", FONT_BOLD)

# Title page
pdf.add_page()
pdf.ln(28)
pdf.set_font("ArialB", "", 22)
pdf.set_text_color(0, 51, 102)
pdf.multi_cell(0, 12, "Pedestrians Movement on Crossings", align="C")
pdf.ln(5)
pdf.set_font("ArialU", "", 16)
pdf.set_text_color(80, 80, 80)
pdf.cell(0, 10, "Example Simulation Sessions", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.ln(12)
pdf.set_draw_color(0, 51, 102)
pdf.line(60, pdf.get_y(), 150, pdf.get_y())
pdf.ln(10)
pdf.set_font("ArialU", "", 11)
pdf.set_text_color(40, 40, 40)
pdf.cell(0, 7, "Course: Agent Systems", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.ln(4)
pdf.set_font("ArialB", "", 11)
pdf.cell(0, 7, "Authors:", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.set_font("ArialU", "", 11)
pdf.cell(
    0,
    7,
    "Kamil Barszczak, Radoslaw Barszczak, Szymon Klempert",
    align="C",
    new_x="LMARGIN",
    new_y="NEXT",
)
pdf.ln(4)
pdf.set_font("ArialU", "", 10)
pdf.set_text_color(100, 100, 100)
pdf.cell(0, 6, "June 2026", align="C", new_x="LMARGIN", new_y="NEXT")

for folder, title, desc in SIMULATIONS:
    frames = frames_for(folder)
    if not frames:
        continue
    pdf.add_page()
    pdf.chapter_title(title)
    pdf.body_text(desc)
    pdf.frame_grid(frames, cols=3)

pdf.output(f"{DOC}/Simulation_Sessions.pdf")
print("PDF generated:", f"{DOC}/Simulation_Sessions.pdf")
