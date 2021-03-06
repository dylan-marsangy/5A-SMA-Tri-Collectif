package org.polytech.statistiques.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.polytech.environment.block.BlockValue;
import org.polytech.statistiques.Evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Classe utilitaire construisant un fichier Excel avec les résultats (statistiques) des évaluations faites sur les exécutions de l'algorithme de tri.
 * Implémente le pattern singleton.
 */
public class ExcelGenerator {

    /**
     * Instance unique de la classe.
     */
    private static final ExcelGenerator instance = new ExcelGenerator();

    /**
     * Nom du fichier Excel dans lequel sauvegarder les statistiques.
     */
    private final String FILE_NAME = "extern/stats/demo.xlsx";

    private ExcelGenerator() {
        cleanExcel();
    }

    public static ExcelGenerator getInstance() {
        return instance;
    }

    /**
     * Remplit une ligne du fichier Excel avec les en-têtes des paramètres d'exécution de l'algorithme de tri.
     *
     * @param sheet    Feuille Excel à remplir
     * @param rowIndex Numéro de la ligne d'en-têtes à remplir
     */
    private void fillParamsLead(Sheet sheet, int rowIndex) {
        Row row = sheet.createRow(rowIndex);

        // Nb Blocs A
        Cell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Nb Blocs A");
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), true));
        // Nb blocs B
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Nb Blocs B");
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), true));
        // Nb agents
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Nb Agents");
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), true));
        // Nb lignes
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Nb Lignes");
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), true));
        // Nb colonnes
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Nb Colonnes");
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), true));
        // Taille mémoire
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Taille  mémoire");
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), true));
        // Nb mouvements successifs
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Nb Mouvements Successifs");
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), true));
        // K -
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("K -");
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), true));
        // K +
        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue("K +");
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), true));
        // Erreur
        cell = row.createCell(9, CellType.STRING);
        cell.setCellValue("Erreur");
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), true));
    }


    /**
     * Remplit une ligne du fichier Excel avec les valeurs des paramètres d'exécution de l'algorithme de tri.
     *
     * @param executionParameters Paramètres d'exécution de l'algorithme
     * @param sheet               Feuille à remplir
     * @param rowIndex            Numéro de la ligne à remplir
     */
    private void fillParamsValues(ExecutionParameters executionParameters, Sheet sheet, int rowIndex) {
        Row row = sheet.createRow(rowIndex);

        // Nb Blocs A
        Cell cell = row.createCell(0, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getNumberBlocksA());
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), false));
        // Nb blocs B
        cell = row.createCell(1, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getNumberBlocksB());
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), false));
        // Nb agents
        cell = row.createCell(2, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getNumberAgents());
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), false));
        // Nb lignes
        cell = row.createCell(3, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getGridRows());
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), false));
        // Nb colonnes
        cell = row.createCell(4, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getGridColumns());
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), false));
        // Taille mémoire
        cell = row.createCell(5, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getMemorySize());
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), false));
        // Nb mouvements successifs
        cell = row.createCell(6, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getSuccessiveMovements());
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), false));
        // K -
        cell = row.createCell(7, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getkMinus());
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), false));
        // K +
        cell = row.createCell(8, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getkPlus());
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), false));
        // Erreur
        cell = row.createCell(9, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getError());
        cell.setCellStyle(ExcelStyles.getParamsStyle(sheet.getWorkbook(), false));
    }

    /**
     * Remplit une ligne du fichier Excel avec les en-têtes de l'évaluation du résultat de l'exécution de l'algorithme de tri.
     *
     * @param sheet    Feuille à remplir
     * @param rowIndex Numéro de la ligne à remplir
     */
    private void fillLead(Sheet sheet, int rowIndex) {
        Row row = sheet.createRow(rowIndex);

        // Itération
        Cell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Exécution");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Nombre de blocs A
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Nombre de blocs A");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Nombre de blocs B
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Nombre de blocs B");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // A voisin avec A
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("A voisin avec A");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // A voisin avec B
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("A voisin avec B");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // B voisin avec B
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("B voisin avec B");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Nombre de colonies
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Nombre de colonies");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Taille moyenne d'une colonie
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("Taille moyenne d'une colonie");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Proportion de A par colonie
        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue("Proportion de A par colonie");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Proportion de B par colonie
        cell = row.createCell(9, CellType.STRING);
        cell.setCellValue("Proportion de B par colonie");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
    }

    /**
     * Remplit une ligne du fichier Excel avec l'évaluation du résultat de l'exécution de l'algorithme de tri.
     *
     * @param evaluation     Évaluation du résultat de l'algorithme de tri
     * @param sheet          Feuille à remplir
     * @param rowIndex       Numéro de la ligne à remplir
     * @param executionIndex Numéro de l'exécution
     */
    private void fillEvaluationRow(Evaluation evaluation, Sheet sheet, int rowIndex, int executionIndex) {
        Row row = sheet.createRow(rowIndex);

        // Itération
        Cell cell = row.createCell(0, CellType.NUMERIC);
        cell.setCellValue(executionIndex);
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), false));
        // Nombre de A
        cell = row.createCell(1, CellType.NUMERIC);
        cell.setCellValue(evaluation.getTotalBlockWithValue(BlockValue.A));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), false));
        // Nombre de B
        cell = row.createCell(2, CellType.NUMERIC);
        cell.setCellValue(evaluation.getTotalBlockWithValue(BlockValue.B));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), false));
        // A voisin de A
        cell = row.createCell(3, CellType.NUMERIC);
        cell.setCellValue(evaluation.getNeighborhoodPercentage(BlockValue.A, BlockValue.A));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), false));
        // A voisin de B
        cell = row.createCell(4, CellType.NUMERIC);
        cell.setCellValue(evaluation.getNeighborhoodPercentage(BlockValue.A, BlockValue.B));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), false));
        // B voisin de B
        cell = row.createCell(5, CellType.NUMERIC);
        cell.setCellValue(evaluation.getNeighborhoodPercentage(BlockValue.B, BlockValue.B));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), false));
        // Nombre de colonies
        cell = row.createCell(6, CellType.NUMERIC);
        cell.setCellValue(evaluation.getNumberOfColonies());
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), false));
        // Taille moyenne d'une colonie
        cell = row.createCell(7, CellType.NUMERIC);
        cell.setCellValue(evaluation.getAverageSizeOfColonies());
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), false));
        // Proportion de A par colonie
        cell = row.createCell(8, CellType.NUMERIC);
        cell.setCellValue(evaluation.getAverageColoniesBlockWithValue(BlockValue.A));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), false));
        // Proportion de B par colonie
        cell = row.createCell(9, CellType.NUMERIC);
        cell.setCellValue(evaluation.getAverageColoniesBlockWithValue(BlockValue.B));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), false));
    }

    /**
     * Génère la formule de la moyenne à appliquer dans une colonne.
     *
     * @param columnLetter Colonne sur laquelle effectuer une moyenne
     * @param from         Ligne du premier nombre de la colonne
     * @param to           Ligne du dernier nombre de la colonne
     * @return Formule de la moyenne
     */
    private String generateAverageFormula(String columnLetter, int from, int to) {
        return "AVERAGE(" + columnLetter + from + ":" + columnLetter + to + ")";
    }

    /**
     * Remplit une ligne contenant les moyennes des résultats des exécutions d'un algorithme de tri pour un jeu de paramètres.
     *
     * @param executionParameters Jeu de paramètres utilisé pour la simulation
     * @param sheet               Feuille à remplir
     * @param rowIndex            Numéro de la ligne à remplir
     */
    private void fillEvaluationAvgRow(ExecutionParameters executionParameters, Sheet sheet, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        int from = rowIndex - executionParameters.getNumberRuns() + 1;

        // Itération
        Cell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Moyenne");
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Nombre de A
        cell = row.createCell(1, CellType.FORMULA);
        cell.setCellFormula(generateAverageFormula("B", from, rowIndex));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Nombre de B
        cell = row.createCell(2, CellType.FORMULA);
        cell.setCellFormula(generateAverageFormula("C", from, rowIndex));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // A voisin de A
        cell = row.createCell(3, CellType.FORMULA);
        cell.setCellFormula(generateAverageFormula("D", from, rowIndex));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // A voisin de B
        cell = row.createCell(4, CellType.FORMULA);
        cell.setCellFormula(generateAverageFormula("E", from, rowIndex));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // B voisin de B
        cell = row.createCell(5, CellType.FORMULA);
        cell.setCellFormula(generateAverageFormula("F", from, rowIndex));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Nombre de colonies
        cell = row.createCell(6, CellType.FORMULA);
        cell.setCellFormula(generateAverageFormula("G", from, rowIndex));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Taille moyenne d'une colonie
        cell = row.createCell(7, CellType.FORMULA);
        cell.setCellFormula(generateAverageFormula("H", from, rowIndex));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Proportion de A par colonie
        cell = row.createCell(8, CellType.FORMULA);
        cell.setCellFormula(generateAverageFormula("I", from, rowIndex));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
        // Proportion de B par colonie
        cell = row.createCell(9, CellType.FORMULA);
        cell.setCellFormula(generateAverageFormula("J", from, rowIndex));
        cell.setCellStyle(ExcelStyles.getEvaluationStyle(sheet.getWorkbook(), true));
    }

    /**
     * Remplit un fichier Excel avec les résultats des exécutions d'un algorithme de tri.
     *
     * @param evaluations         Évaluations des résultats de l'algorithme de tri
     * @param executionParameters Jeu de paramètres utilisé pour les exécutions
     * @param executionName       Nom de la feuille à créer et remplir
     */
    public void save(List<Evaluation> evaluations, ExecutionParameters executionParameters, String executionName) {
        Workbook workbook;
        FileInputStream file;

        try {
            // Ouvre l'Excel contenant les résultats
            file = new FileInputStream(FILE_NAME);
            workbook = WorkbookFactory.create(file);

            Sheet sheet;
            int rownum;
            int iteration = 1;

            // Récupère la feuille existante ou la crée au besoin
            if (workbook.getSheet(executionName) != null) {
                sheet = workbook.getSheet(executionName);
                rownum = sheet.getLastRowNum() + 4;
            } else {
                sheet = workbook.createSheet(executionName);
                rownum = 0;
            }

            // Entête des paramètres
            fillParamsLead(sheet, rownum);
            ++rownum;

            // Paramètres utilisés
            fillParamsValues(executionParameters, sheet, rownum);
            ++rownum;

            // Entête de l'évaluation
            fillLead(sheet, rownum);

            // Evaluation
            for (Evaluation evaluation : evaluations) {
                rownum++;
                fillEvaluationRow(evaluation, sheet, rownum, iteration);
                ++iteration;
            }

            if (evaluations.size() > 1) {
                ++rownum;

                // Moyenne des évaluations si nécessaire
                fillEvaluationAvgRow(executionParameters, sheet, rownum);
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
            }

            for (int i = 0; i < 10; i++) {
                sheet.autoSizeColumn(i);
            }

            file.close();
        } catch (IOException ex) {
            return;
        }

        // Enregistre les modifications dans le fichier
        try {
            FileOutputStream outFile = new FileOutputStream(FILE_NAME);
            workbook.write(outFile);
            outFile.close();
            System.out.println("Results saved successfully in " + FILE_NAME);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Vide les données présentes du fichier Excel en vue d'une nouvelle simulation.
     */
    public void cleanExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();

        try {
            File file = new File(FILE_NAME);
            file.getParentFile().mkdirs();

            FileOutputStream outFile = new FileOutputStream(file);
            workbook.write(outFile);
            outFile.close();
        } catch (IOException exc) {
            System.out.println(exc.getMessage() + ": tentative de création échouée...");
        }
    }

}
