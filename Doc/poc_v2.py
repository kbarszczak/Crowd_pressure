from fpdf import FPDF

FONT = "/System/Library/Fonts/Supplemental/Arial Unicode.ttf"
FONT_BOLD = "/System/Library/Fonts/Supplemental/Arial Bold.ttf"
DOC = "/Users/kamilbarszczak/Desktop/studies/Crowd_pressure/Doc"
SS = f"{DOC}/poc_screenshots"

FRAMES = [
    (f"{SS}/Screenshot 2026-05-30 at 10.56.28.png", "t ≈ 43 s"),
    (f"{SS}/Screenshot 2026-05-30 at 10.56.48.png", "t ≈ 63 s"),
    (f"{SS}/Screenshot 2026-05-30 at 10.56.56.png", "t ≈ 70 s"),
    (f"{SS}/Screenshot 2026-05-30 at 10.57.00.png", "t ≈ 84 s"),
    (f"{SS}/Screenshot 2026-05-30 at 10.57.21.png", "t ≈ 96 s"),
    (f"{SS}/Screenshot 2026-05-30 at 10.57.31.png", "t ≈ 109 s"),
    (f"{SS}/Screenshot 2026-05-30 at 10.57.54.png", "t ≈ 129 s"),
    (f"{SS}/Screenshot 2026-05-30 at 10.58.09.png", "t ≈ 143 s"),
    (f"{SS}/Screenshot 2026-05-30 at 10.58.20.png", "t ≈ 158 s"),
]


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
        self.ln(3)

    def body_text(self, text):
        self.set_font("ArialU", "", 10)
        self.set_text_color(30, 30, 30)
        self.multi_cell(0, 5.5, text, align="J")
        self.ln(2)

    def bullet(self, text):
        self.set_font("ArialU", "", 10)
        self.set_text_color(30, 30, 30)
        self.cell(6, 5.5, "•")
        self.multi_cell(0, 5.5, text, align="J")
        self.ln(1)

    def grid_3x3(self, frames):
        cell_w = 60
        cell_h = cell_w * 0.6
        gap_x = 3
        gap_y = 5
        x0 = (210 - 3 * cell_w - 2 * gap_x) / 2
        y0 = self.get_y()
        for i, (path, cap) in enumerate(frames):
            row = i // 3
            col = i % 3
            x = x0 + col * (cell_w + gap_x)
            y = y0 + row * (cell_h + gap_y + 4)
            self.image(path, x=x, y=y, w=cell_w)
            self.set_xy(x, y + cell_h + 1)
            self.set_font("ArialU", "", 8)
            self.set_text_color(100, 100, 100)
            self.cell(cell_w, 4, cap, align="C")
        self.set_xy(self.l_margin, y0 + 3 * (cell_h + gap_y + 4))
        self.set_text_color(30, 30, 30)


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
    "Minimal working prototype of the pedestrian simulation",
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

# --- Page 2: Result + screenshots ---
pdf.add_page()

pdf.chapter_title("1. Result")
pdf.body_text(
    "The simulation engine and the Social Force based physical model have "
    "been successfully implemented and run end-to-end on a single bottleneck "
    "map. Agents are spawned on the left, navigate around a vertical wall "
    "and reach the destination on the right. The frames below show one full "
    "run captured at increasing simulation times."
)

pdf.chapter_title("2. Screenshots")
pdf.grid_3x3(FRAMES)

pdf.chapter_title("3. Next steps")
pdf.bullet("Application menu and runtime controls.")
pdf.bullet("Additional map layouts (crossings, corridors, multi-group scenarios).")
pdf.bullet("Parallel computation of agent updates (multi-threaded engine).")
pdf.bullet(
    "Remaining features from the Agent Definition and State of the Art "
    "documents (crowd pressure visualisation, lane formation, "
    "freezing-by-heating validation)."
)

pdf.output(f"{DOC}/Proof_of_Concept_v2.pdf")
print("PDF generated successfully.")
