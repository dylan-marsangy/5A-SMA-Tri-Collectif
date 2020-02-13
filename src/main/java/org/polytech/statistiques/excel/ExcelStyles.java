package org.polytech.statistiques.excel;

import org.apache.poi.ss.usermodel.*;


/**
 * Classe générant des styles à appliquer dans l'Excel
 */
public class ExcelStyles {

    /**
     * Crée une police spécifique pour les entêtes des paramètres
     *
     * @param wb Workbook où appliquer le style
     * @return Font à appliquer au cellStyle
     */
    public static Font getParamLeadFont(Workbook wb) {
        Font font = wb.createFont();
        // police et taille
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        // met en gras
        font.setBold(true);
        return font;
    }


    /**
     * Récupère le style à appliquer aux cellules contenant les paramètres d'exécution
     *
     * @param wb   Workbook où appliquer le style
     * @param lead boolean à true si la cellule est dans les entêtes
     * @return CellStyle
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
     * Retourne la police à utiliser pour les entêtes des résultats d'exécution
     *
     * @param wb Workbook où appliquer la police
     * @return Font
     */
    public static Font getEvaluationLeadFont(Workbook wb) {
        Font font = wb.createFont();
        // police et taille
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setBold(true);
        return font;
    }


    /**
     * Retourne le style à appliquer sur une cellule contenant les résultats d'exécution
     *
     * @param wb   Workbook
     * @param lead boolean à true si le style doit être appliqué sur un entête
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
