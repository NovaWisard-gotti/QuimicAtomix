#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Convierte los documentos Markdown clave de QuimicAtomix en PDF reales usando reportlab.
No requiere Android SDK ni red bloqueada: es una herramienta de documentación independiente.

Uso: python3 tools/render_pdfs.py
"""
import os
import re
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import cm
from reportlab.lib.enums import TA_LEFT
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, Preformatted, PageBreak
)
from reportlab.pdfbase.pdfmetrics import stringWidth

ROOT = os.path.join(os.path.dirname(__file__), "..")
DOCS_DIR = os.path.join(ROOT, "docs")
OUT_DIR = os.path.join(DOCS_DIR, "pdf")

DOCUMENTS = [
    ("MEMORIA_DESCRIPTIVA.md", "MEMORIA_DESCRIPTIVA.pdf", "QuimicAtomix — Memoria Descriptiva"),
    ("MANUAL_USUARIO.md", "MANUAL_USUARIO.pdf", "QuimicAtomix — Manual de Usuario"),
    ("MANUAL_TECNICO.md", "MANUAL_TECNICO.pdf", "QuimicAtomix — Manual Técnico"),
]

styles = getSampleStyleSheet()
styles.add(ParagraphStyle(name="H1Custom", parent=styles["Heading1"], spaceBefore=18, spaceAfter=8, textColor=colors.HexColor("#10163A")))
styles.add(ParagraphStyle(name="H2Custom", parent=styles["Heading2"], spaceBefore=14, spaceAfter=6, textColor=colors.HexColor("#14877A")))
styles.add(ParagraphStyle(name="H3Custom", parent=styles["Heading3"], spaceBefore=10, spaceAfter=4, textColor=colors.HexColor("#2E7DFF")))
styles.add(ParagraphStyle(name="BodyCustom", parent=styles["BodyText"], fontSize=10.5, leading=15, spaceAfter=6, alignment=TA_LEFT))
styles.add(ParagraphStyle(name="BulletCustom", parent=styles["BodyText"], fontSize=10.5, leading=15, spaceAfter=3, leftIndent=14, bulletIndent=4))
styles.add(ParagraphStyle(name="CodeCustom", parent=styles["Code"], fontSize=8.2, leading=10.5, backColor=colors.HexColor("#F1F3F9")))
styles.add(ParagraphStyle(name="CoverTitle", parent=styles["Title"], fontSize=26, textColor=colors.HexColor("#10163A")))
styles.add(ParagraphStyle(name="CoverSubtitle", parent=styles["Normal"], fontSize=13, textColor=colors.HexColor("#2E7DFF"), spaceBefore=8))


def inline_md(text: str) -> str:
    """Convierte negrita/código en línea de Markdown a marcado XML de reportlab, y escapa el resto."""
    # Escapar caracteres XML especiales primero, preservando marcadores propios
    text = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
    text = re.sub(r"\*\*(.+?)\*\*", r"<b>\1</b>", text)
    text = re.sub(r"`([^`]+?)`", r"<font face='Courier'>\1</font>", text)
    return text


def parse_table(lines, start_idx):
    """Parsea una tabla Markdown (| a | b |) a partir de start_idx. Devuelve (filas, siguiente_indice)."""
    rows = []
    i = start_idx
    while i < len(lines) and lines[i].strip().startswith("|"):
        raw = lines[i].strip()
        if re.match(r"^\|[\s:\-|]+\|$", raw):
            i += 1
            continue
        cells = [c.strip() for c in raw.strip("|").split("|")]
        rows.append(cells)
        i += 1
    return rows, i


def markdown_to_story(md_text: str):
    story = []
    lines = md_text.split("\n")
    i = 0
    in_code = False
    code_buffer = []
    while i < len(lines):
        line = lines[i]

        if line.strip().startswith("```"):
            if not in_code:
                in_code = True
                code_buffer = []
            else:
                in_code = False
                code_text = "\n".join(code_buffer)
                if len(code_text) > 3000:
                    code_text = code_text[:3000] + "\n... (contenido truncado en esta version PDF; ver el .md fuente)"
                story.append(Preformatted(code_text, styles["CodeCustom"]))
                story.append(Spacer(1, 6))
            i += 1
            continue

        if in_code:
            code_buffer.append(line)
            i += 1
            continue

        stripped = line.strip()

        if stripped.startswith("| ") or (stripped.startswith("|") and stripped.endswith("|")):
            rows, next_i = parse_table(lines, i)
            if rows:
                col_count = len(rows[0])
                page_width = A4[0] - 4 * cm
                col_width = page_width / col_count
                table_data = [[Paragraph(inline_md(c), styles["BodyCustom"]) for c in row] for row in rows]
                t = Table(table_data, colWidths=[col_width] * col_count, repeatRows=1)
                t.setStyle(TableStyle([
                    ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#10163A")),
                    ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
                    ("GRID", (0, 0), (-1, -1), 0.5, colors.HexColor("#CBD3EA")),
                    ("VALIGN", (0, 0), (-1, -1), "TOP"),
                    ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#F4F6FB")]),
                ]))
                story.append(t)
                story.append(Spacer(1, 10))
            i = next_i
            continue

        if stripped.startswith("#### "):
            story.append(Paragraph(inline_md(stripped[5:]), styles["H3Custom"]))
        elif stripped.startswith("### "):
            story.append(Paragraph(inline_md(stripped[4:]), styles["H3Custom"]))
        elif stripped.startswith("## "):
            story.append(Paragraph(inline_md(stripped[3:]), styles["H2Custom"]))
        elif stripped.startswith("# "):
            story.append(Paragraph(inline_md(stripped[2:]), styles["H1Custom"]))
        elif stripped.startswith("> "):
            story.append(Paragraph(inline_md(stripped[2:]), styles["BodyCustom"]))
        elif re.match(r"^[-*]\s+", stripped):
            content = re.sub(r"^[-*]\s+", "", stripped)
            story.append(Paragraph("• " + inline_md(content), styles["BulletCustom"]))
        elif re.match(r"^\d+\.\s+", stripped):
            story.append(Paragraph(inline_md(stripped), styles["BulletCustom"]))
        elif stripped == "":
            story.append(Spacer(1, 4))
        elif stripped == "---":
            story.append(Spacer(1, 10))
        else:
            story.append(Paragraph(inline_md(stripped), styles["BodyCustom"]))
        i += 1

    return story


def build_pdf(md_path, pdf_path, title):
    with open(md_path, "r", encoding="utf-8") as f:
        content = f.read()

    doc = SimpleDocTemplate(
        pdf_path, pagesize=A4,
        leftMargin=2 * cm, rightMargin=2 * cm, topMargin=2 * cm, bottomMargin=2 * cm,
        title=title, author="QuimicAtomix"
    )

    story = []
    story.append(Spacer(1, 4 * cm))
    story.append(Paragraph(title, styles["CoverTitle"]))
    story.append(Paragraph("Aplicación educativa de química para niños de 8 a 12 años", styles["CoverSubtitle"]))
    story.append(Paragraph("Versión 1.0.0 · com.educalab.quimicatomix", styles["CoverSubtitle"]))
    story.append(PageBreak())

    story.extend(markdown_to_story(content))

    doc.build(story)
    size_kb = os.path.getsize(pdf_path) / 1024
    print(f"  OK: {pdf_path} ({size_kb:.1f} KB)")


def main():
    os.makedirs(OUT_DIR, exist_ok=True)
    print("Generando PDFs reales desde Markdown...")
    for md_name, pdf_name, title in DOCUMENTS:
        md_path = os.path.join(DOCS_DIR, md_name)
        pdf_path = os.path.join(OUT_DIR, pdf_name)
        build_pdf(md_path, pdf_path, title)
    print("Listo.")


if __name__ == "__main__":
    main()
