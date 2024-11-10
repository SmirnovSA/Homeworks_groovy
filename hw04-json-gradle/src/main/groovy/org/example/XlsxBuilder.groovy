package org.example

import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.streaming.SXSSFWorkbook

import java.awt.Color

class XlsxBuilder {
    private Workbook workbook
    private Sheet currentSheet
    def filename

    XlsxBuilder(filename) {
        this.filename = filename
        this.workbook = new SXSSFWorkbook()
    }

    def sheet(name,@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SheetDelegate) Closure closure) {
        currentSheet = workbook.createSheet(name as String)
        closure.delegate = new SheetDelegate(currentSheet)
        closure()
        save(filename)
    }

    def save(String fileName) {
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream)
            workbook.close()
        }
    }
}

class SheetDelegate {
    Sheet sheet

    SheetDelegate(Sheet sheet) {
        this.sheet = sheet
    }

    def row(int idx,@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = RowDelegate) Closure closure) {
        Row row = sheet.createRow(idx)
        closure.delegate = new RowDelegate(row)
        closure()
    }
}
class RowDelegate {
    Row row
    int cellIndex = 0

    RowDelegate(Row row) {
        this.row = row
    }

    def cell(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = CellDelegate) Closure closure) {
        Cell cell = row.createCell(++cellIndex)
        closure.delegate = new CellDelegate(cell)
        closure()
    }
}

class CellDelegate {
    Cell cell
    String value
    def style

    CellDelegate(Cell cell) {
        this.cell = cell
    }

    def value(Object value) {
        cell.setCellValue(value.toString())
    }

    def style(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = CellStyleSpec) Closure closure) {
        CellStyle style = cell.getSheet().getWorkbook().createCellStyle()
        closure.delegate = new CellStyleSpec(cell,style)
        closure()
    }
}

class CellStyleSpec{
    private Cell cell
    private CellStyle cellStyle

    CellStyleSpec(Cell cell,CellStyle cellStyle){
        this.cell = cell
        this.cellStyle = cellStyle
    }

    def colorValue(String colorName) {
        // Определение цвета
        Color color;
        switch (colorName.toUpperCase()) {
            case "BLACK":
                return IndexedColors.BLACK.getIndex();
            case "WHITE":
                return IndexedColors.WHITE.getIndex();
            case "RED":
                return IndexedColors.RED.getIndex();
            case "GREEN":
                return IndexedColors.GREEN.getIndex();
            case "BLUE":
                return IndexedColors.BLUE.getIndex();
            case "YELLOW":
                return IndexedColors.YELLOW.getIndex();
            case "PINK":
                return IndexedColors.PINK.getIndex();
            case "ORANGE":
                return IndexedColors.ORANGE.getIndex();
            case "BROWN":
                return IndexedColors.BROWN.getIndex()
            default:
                return IndexedColors.AUTOMATIC.getIndex();
        }
    }

    def backgroundColor(String backgroundColor){
        def nameColor = colorValue(backgroundColor)
        cellStyle.setFillBackgroundColor(nameColor)
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(cellStyle)
    }
    def color(String color){
        def nameColor = colorValue(color)
        cellStyle.setFillForegroundColor(nameColor)
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(cellStyle)
    }
    def font(String font){
        def nameColor = colorValue(font)
        Font fontColor = cell.getSheet().getWorkbook().createFont();
        fontColor.setColor(nameColor)
        cellStyle.setFont(fontColor)
        cell.setCellStyle(cellStyle)
    }
}

