package org.polytech.statistiques.excel;

import org.apache.poi.ss.usermodel.*;

public class ExcelStyles {
    public static Font getParamLeadFont(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setBold(true);
        return font;
    }

    public static CellStyle getParamsStyle(Workbook wb, boolean lead) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        if (lead) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            Font font = getParamLeadFont(wb);
            cellStyle.setFont(font);
        }
        return cellStyle;
    }

    public static Font getEvaluationLeadFont(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setBold(true);
        return font;
    }

    public static CellStyle getEvaluationStyle(Workbook wb, boolean lead) {
        CellStyle cellStyle = wb.createCellStyle();

        if (lead) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            Font font = getEvaluationLeadFont(wb);
            cellStyle.setFont(font);
        }
        return cellStyle;
    }
}
