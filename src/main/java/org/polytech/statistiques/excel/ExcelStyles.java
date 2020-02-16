package org.polytech.statistiques.excel;

import org.apache.poi.ss.usermodel.*;

/**
 * Classe utilitaire générant des styles à appliquer dans les fichiers Excel.
 */
public class ExcelStyles {

    /**
     * Renvoie la police spécifique à utiliser pour les en-têtes des paramètres d'exécution de l'algorithme.
     *
     * @param wb Workbook où appliquer le style
     * @return Font à appliquer aux cellules
     */
    private static Font getParamLeadFont(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setBold(true);
        return font;
    }

    /**
     * Renvoie le style à appliquer aux cellules contenant les paramètres d'exécution de l'algorithme.
     *
     * @param wb   Workbook où appliquer le style
     * @param lead Indique si la cellule est dans les en-têtes ou non
     * @return Style à appliquer aux cellules
     */
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

    /**
     * Retourne la police à utiliser pour les en-têtes des résultats des évaluations des exécutions de l'algorithme.
     *
     * @param wb Workbook où appliquer la police
     * @return Font à appliquer aux cellules
     */
    private static Font getEvaluationLeadFont(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setBold(true);
        return font;
    }


    /**
     * Renvoie le style à appliquer sur une cellule contenant les résultats des évaluations des exécutions de l'algorithme.
     *
     * @param wb   Workbook où appliquer la police
     * @param lead Indique si la cellule est dans les en-têtes ou non
     * @return CellStyle
     */
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
