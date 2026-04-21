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

# Strona tytu\u0142owa
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
    "Przegl\u0105d najwa\u017cniejszych modeli i metod symulacji ruchu pieszych",
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
pdf.cell(0, 7, "Przedmiot: Agent Systems", align="C", new_x="LMARGIN", new_y="NEXT")
pdf.ln(5)
pdf.set_font("ArialB", "", 11)
pdf.cell(0, 7, "Autorzy:", align="C", new_x="LMARGIN", new_y="NEXT")
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
pdf.cell(0, 6, "Kwiecie\u0144 2026", align="C", new_x="LMARGIN", new_y="NEXT")

# --- Strona 2: Wprowadzenie + SFM ---
pdf.add_page()

pdf.chapter_title("1. Wprowadzenie")
pdf.body_text(
    "Wraz ze wzrostem liczby imprez masowych i rosn\u0105c\u0105 g\u0119sto\u015bci\u0105 zaludnienia "
    "w miastach, modelowanie ruchu pieszych staje si\u0119 coraz wa\u017cniejszym obszarem "
    "bada\u0144. Jest ono przydatne w wielu dziedzinach, takich jak projektowanie "
    "budynk\u00f3w, grafika komputerowa, planowanie ruchu robot\u00f3w czy organizacja "
    "ruchu drogowego. Bior\u0105c pod uwag\u0119, \u017ce ka\u017cdy pieszy ma sw\u00f3j osobny cel "
    "podr\u00f3\u017cy, charakter i uwarunkowania kulturowe, przewidywanie ruchu t\u0142umu "
    "wydaje si\u0119 by\u0107 zbyt trudnym problemem. W rzeczywisto\u015bci okazuje si\u0119 to "
    "jednak wykonywalne dzi\u0119ki modelom symulacyjnym."
)
pdf.body_text(
    "Pocz\u0105tkowo pr\u00f3bowano badac zachowania pieszych bezpo\u015brednio, obserwuj\u0105c "
    "ich, analizuj\u0105c fotografie i nagrania z kamer ulicznych. Z czasem jednak "
    "skupiono si\u0119 na modelach symulacyjnych, kt\u00f3re pozwalaj\u0105 odwzorowa\u0107 "
    "z\u0142o\u017cone dynamiki t\u0142umu obliczeniowo. Poni\u017cej przedstawiono "
    "przegl\u0105d najwa\u017cniejszych modeli i metod opracowanych do symulacji ruchu "
    "pieszych, ze szczeg\u00f3lnym uwzgl\u0119dnieniem scenariuszy przej\u015b\u0107 "
    "i w\u0105skich garde\u0142."
)

pdf.chapter_title("2. Social Force Model (SFM)")

pdf.section_title("2.1 Geneza i podstawowa koncepcja")
pdf.body_text(
    "Social Force Model (SFM), zaproponowany przez "
    "Dirka Helbinga i Petera Molnara w 1998 roku, jest jednym z najbardziej "
    "rozpoznawalnych i szeroko badanych modeli dynamiki pieszych. Zosta\u0142 on "
    "gruntownie przebadany przez naukowc\u00f3w z ca\u0142ego \u015bwiata i znalaz\u0142 "
    "zastosowania komercyjne. SFM jest modelem ci\u0105g\u0142ym, kt\u00f3ry sugeruje, "
    "\u017ce piesi s\u0105 poddani si\u0142om socjalnym - si\u0142y te stanowi\u0105 miar\u0119 "
    "wewn\u0119trznych motywacji cz\u0142owieka do wykonywania okre\u015blonych czynno\u015bci."
)

pdf.section_title("2.2 Si\u0142y dzia\u0142aj\u0105ce na pieszych")
pdf.body_text(
    "W Social Force Model ka\u017cdy pieszy podlega wp\u0142ywowi trzech "
    "kategorii si\u0142:"
)
pdf.bullet(
    "Si\u0142a nap\u0119dowa w po\u017c\u0105danym kierunku ruchu - przyspieszenie w kierunku "
    "celu pieszego z preferowana predko\u015bci\u0105."
)
pdf.bullet(
    "Si\u0142y od innych pieszych - odpychaj\u0105ce interakcje utrzymuj\u0105ce przestrze\u0144 "
    "osobist\u0105 oraz efekt przyci\u0105gania, kt\u00f3ry zachodzi w\u015br\u00f3d znajomych."
)
pdf.bullet(
    "Si\u0142y od granic i przeszk\u00f3d - si\u0142y odpychaj\u0105ce od \u015bcian, barierek "
    "i innych fizycznych granic w otoczeniu."
)
pdf.body_text(
    "Model mo\u017cna rozszerzy\u0107 o dodatkowe zachowania, takie jak wyprzedzanie, "
    "czekanie, zwalnianie, unikanie zderzenia, pod\u0105\u017canie za kim\u015b, indywidualne "
    "preferencje pieszych (np. ch\u0119\u0107 poruszania si\u0119 w grupie) czy dynamiczny "
    "wyb\u00f3r trasy na otwartej przestrzeni. Takiej reprezentacji nie mo\u017cna jednak "
    "wykorzysta\u0107 w wi\u0119kszych symulacjach komputerowych z powodu z\u0142o\u017cono\u015bci "
    "obliczeniowej, kt\u00f3ra ro\u015bnie kwadratowo wraz ze wzrostem liczby agent\u00f3w."
)

pdf.section_title("2.3 Zalety i ograniczenia")
pdf.body_text(
    "Zalet\u0105 tej metodologii jest du\u017ca dok\u0142adno\u015b\u0107 symulacji oraz precyzja "
    "odwzorowania mikroskopowych oddzia\u0142ywa\u0144 mi\u0119dzy pojedynczymi obiektami "
    "a \u015brodowiskiem. Komputerowe symulacje t\u0142um\u00f3w pokazuj\u0105, \u017ce model "
    "odwzorowuje niekt\u00f3re zachowania pieszych bardzo realistycznie. Do wad "
    "nale\u017cy relatywnie ma\u0142a wydajno\u015b\u0107 obliczeniowa i trudno\u015bci z odwzorowaniem "
    "niekt\u00f3rych zdarze\u0144. Przez wzgl\u0105d na wysokie nak\u0142ady obliczeniowe metoda "
    "ta ma ograniczone mo\u017cliwo\u015bci skalowania."
)

pdf.section_title('2.4 Dystans interpersonalny i efekt "szybciej-znaczy-wolniej"')
pdf.body_text(
    "Social Force Model umo\u017cliwia definiowanie minimalnego dystansu "
    "mi\u0119dzyludzkiego, dzi\u0119ki czemu mo\u017cemy zaobserwowa\u0107 efekt "
    '"szybciej-znaczy-wolniej". W normalnej sytuacji przep\u0142yw t\u0142umu przez '
    "w\u0105ski przesmyk jest zmniejszony, ale szansa na fizyczne interakcje "
    "pozostaje niska. W sytuacji stresowej dystans maleje, co zwi\u0119ksza "
    "przep\u0142yw, ale te\u017c szans\u0119 na fizyczny kontakt. W skrajnych przypadkach "
    "dystans interpersonalny jest na tyle ma\u0142y, \u017ce fizyczne interakcje mi\u0119dzy "
    "obiektami powoduj\u0105 ich wzajemne blokowanie, co uniemo\u017cliwia przej\u015bcie. "
    'Zjawisko to okre\u015bla si\u0119 jako "freezing by heating" - gdy t\u0142um zbyt '
    "mocno napiera, piesi znacznie zwalniaj\u0105 przy napotkaniu t\u0142oku."
)

# --- Strona 3: Model heurystyk behawioralnych ---
pdf.chapter_title("3. Model heurystyk behawioralnych")

pdf.section_title("3.1 Przegl\u0105d")
pdf.body_text(
    "Wa\u017cnym rozszerzeniem oryginalnego SFM jest model zaproponowany przez "
    'Moussa\u00efd\u0119, Helbinga i Theraulaza w artykule "How simple rules determine '
    'pedestrian behavior and crowd disasters". Model ten rozwi\u0105zuje cz\u0119\u015b\u0107 '
    "problem\u00f3w zwi\u0105zanych z oryginalnym Social Force Model, bazuj\u0105c "
    "na heurystykach behawioralnych zamiast czysto fizycznych oblicze\u0144 si\u0142."
)

pdf.section_title("3.2 Kluczowe heurystyki")
pdf.body_text(
    "Model wykorzystuje dwie g\u0142\u00f3wne heurystyki steruj\u0105ce zachowaniem pieszych:"
)
pdf.body_text(
    "Heurystyka kierunku: Pieszy wybiera po\u017c\u0105dany kierunek ruchu \u03b1_des, "
    "zwi\u0105zany z jak najbardziej bezpo\u015bredni\u0105 drog\u0105 do celu, bior\u0105c pod "
    "uwag\u0119 mo\u017cliwe kolizje. Kierunek ten jest obliczany przez minimalizacj\u0119 "
    "funkcji dystansu d(\u03b1), kt\u00f3ra uwzgl\u0119dnia odleg\u0142o\u015b\u0107 do pierwszej "
    "przeszkody w ka\u017cdym kandydackim kierunku, w po\u0142\u0105czeniu z odchyleniem "
    "od bezpo\u015bredniej \u015bcie\u017cki do celu:"
)
pdf.formula(
    r"d(\alpha) = d_{max}^2 + f(\alpha)^2 - 2 \cdot d_{max} \cdot f(\alpha) \cdot \cos(\alpha_0 - \alpha)"
)
pdf.body_text(
    "Heurystyka dystansu: Pieszy zachowuje dystans do pierwszej przeszkody "
    "na swojej drodze, kt\u00f3ry zapewnia czas do kolizji r\u00f3wny co najmniej "
    "\u03c4 (czas relaksacji). Pr\u0119dko\u015b\u0107 po\u017c\u0105dana jest obliczana jako:"
)
pdf.formula(r"v_{des}(t) = \min(v^0_i,\; d_h \,/\, \tau)")
pdf.body_text(
    "gdzie v0i to komfortowa pr\u0119dko\u015b\u0107 poruszania si\u0119, "
    "a dh to dystans do najbli\u017cszej przeszkody. Przyspieszenie pieszego "
    "wynikaj\u0105ce z tej heurystyki wynosi:"
)
pdf.formula(r"a_i = \frac{v_{des} - v_i}{\tau}")

pdf.section_title("3.3 Efekty kolizji")
pdf.body_text(
    "W sytuacjach du\u017cego zag\u0119szczenia model uwzgl\u0119dnia r\u00f3wnie\u017c si\u0142y "
    "kontaktu fizycznego mi\u0119dzy pieszymi. Si\u0142a zderzenia dw\u00f3ch pieszych "
    "i oraz j jest obliczana jako:"
)
pdf.formula(r"f_{ij} = k \cdot g(r_i + r_j - d_{ij}) \cdot n_{ij}")
pdf.body_text(
    "gdzie k to wsp\u00f3\u0142czynnik skaluj\u0105cy, g(x) to funkcja zwracaj\u0105ca 0 gdy "
    "piesi si\u0119 nie dotykaj\u0105 (w przeciwnym wypadku x), r_i to promie\u0144 cia\u0142a "
    "(proporcjonalny do masy: m_i / 320), d_ij to dystans mi\u0119dzy \u015brodkami "
    "ci\u0119\u017cko\u015bci, a n_ij to wektor jednostkowy. Analogicznie si\u0142a zderzenia "
    "ze \u015bcian\u0105:"
)
pdf.formula(r"f_{iW} = k \cdot g(r_i - d_{iW}) \cdot n_{iW}")

pdf.section_title("3.4 Ko\u0144cowe r\u00f3wnanie przyspieszenia")
pdf.body_text(
    "Ostateczny wektor przyspieszenia pieszego \u0142\u0105czy wszystkie sk\u0142adowe - "
    "przyspieszenie w kierunku po\u017c\u0105danej pr\u0119dko\u015bci oraz si\u0142y kolizji "
    "fizycznych od innych pieszych i \u015bcian:"
)
pdf.formula(
    r"a_i = \frac{v_{des} - v_i}{\tau} + \sum_j \frac{f_{ij}}{m_i} + \sum_W \frac{f_{iW}}{m_i}"
)

# --- Strona 4: Podej\u015bcia agentowe, por\u00f3wnanie, wnioski ---
pdf.chapter_title("4. Podej\u015bcia agentowe i automaty kom\u00f3rkowe")

pdf.section_title("4.1 \u0141\u0105czenie SFM z programowaniem agentowym")
pdf.body_text(
    "Social Force Model nadaje si\u0119 do \u0142\u0105czenia z programowaniem agentowym "
    "oraz automatami kom\u00f3rkowymi. O ile \u0142\u0105czenie tego modelu z klasycznym "
    "automatem synchronicznym i homogenicznym nie przynosi dobrych efekt\u00f3w, "
    "o tyle wykorzystanie tzw. rozszerzonego automatu daje du\u017co lepsze wyniki. "
    "Znacznie bardziej efektywne jest wykorzystanie system\u00f3w agentowych, "
    "poniewa\u017c pozwalaj\u0105 one na odwzorowanie z\u0142o\u017conych interakcji."
)

pdf.section_title("4.2 Makroskopowy charakter jednostek agentowych")
pdf.body_text(
    "W ramach Social Force Model ka\u017cda jednostka ma charakter makroskopowy "
    "- ka\u017cda osoba jest reprezentowana przez wirtualnego agenta posiadaj\u0105cego "
    "indywidualny zestaw zmiennych. Otwarty charakter modelu pozwala definiowa\u0107 "
    "dedykowane zestawy zachowa\u0144 dla konkretnych scenariuszy: odpowiednie "
    "omijanie przeszk\u00f3d, wyb\u00f3r odpowiedniej pr\u0119dko\u015bci, wyszukiwanie najlepszej "
    "drogi do celu oraz pewne charakterystyczne wzorce. Dzi\u0119ki temu mo\u017cliwe "
    "jest symulowanie ruchu os\u00f3b o r\u00f3\u017cnych rolach w t\u0142umie (np. policjant, "
    "matka z dzieckiem, dziecko, pojedyncza osoba, grupa znajomych)."
)

pdf.chapter_title("5. Bibliografia")
pdf.set_font("ArialU", "", 9)
refs = [
    "1. Helbing, D. & Molnar, P. (1998). Social Force Model for Pedestrian Dynamics. Physical Review E.",
    '2. Moussa\u00efda, M., Helbing, D., & Theraulaz, G. "How simple rules determine pedestrian behavior and crowd disasters." PNAS. (doi: 10.1073/pnas.1016507108)',
    '3. "Understanding Social-Force Model in Psychological Principles of Collective Behavior." ResearchGate.',
    '4. "Crowd Management - Social Force Model." IJERT.',
    '5. "Deep Social Force." ResearchGate.',
    '6. "Social Force Model for Pedestrian Dynamics." ResearchGate.',
    '7. "Passenger and Pedestrian Modelling at Transport Facilities." ResearchGate.',
]
for r in refs:
    pdf.set_x(15)
    pdf.multi_cell(175, 5, r)
    pdf.ln(1)

pdf.output("/Users/kamilbarszczak/Desktop/Crowd_pressure/doc/State_of_the_Art.pdf")
print("PDF wygenerowany pomyslnie.")
