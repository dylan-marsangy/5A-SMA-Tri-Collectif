package org.polytech.statistiques.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.polytech.ExecutionParameters;
import org.polytech.environnement.block.BlockValue;
import org.polytech.statistiques.Evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Construit un Excel avec les résultat des évaluations faites sur les exécutions de l'algorithme
 * Implémente le pattern singleton
 */
public class ExcelGenerator {

    private ExcelGenerator() {
        cleanExcel();
    }

    private static final ExcelGenerator instance = new ExcelGenerator();

    // nom de l'Excel dans lequel écrire les résultats
    private final String fileName = "extern/demo.xlsx";

    public static ExcelGenerator getInstance()
    {
        return instance;
    }

    /**
     * Remplit la ligne numérotée rownum avec les entêtes des paramètres d'exécution
     * @param sheet  Sheet
     * @param rownum int
     */
    private void fillParamsLead(Sheet sheet, int rownum) {
        Row row = sheet.createRow(rownum);

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
     * Remplit la ligne numérotée rownum avec les valeurs d'exécution spécifiées
     * @param executionParameters ExecutionParameters
     * @param sheet               Sheet
     * @param rownum              int
     */
    private void fillParamsValues(ExecutionParameters executionParameters, Sheet sheet, int rownum) {
        Row row = sheet.createRow(rownum);

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
     * Remplit l'entête de la feuille en paramètre au numéro de ligne indiqué
     * @param sheet  Sheet
     * @param rownum int
     */
    private void fillLead(Sheet sheet, int rownum) {
        Row row = sheet.createRow(rownum);

        // Itération
        Cell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Itération");
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
     * Remplit une ligne de l'Excel avec les résultats d'une évaluation
     * @param evaluation Evaluation
     * @param sheet      Sheet où écrire les résultats
     * @param rownum     int correspondant au numéro de ligne où écrire
     * @param iteration  int correspondant au numéro de l'exécution
     */
    private void fillEvaluationRow(Evaluation evaluation, Sheet sheet, int rownum, int iteration) {
        Row row = sheet.createRow(rownum);

        // Itération
        Cell cell = row.createCell(0, CellType.NUMERIC);
        cell.setCellValue(iteration);
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

    public void fillExcel(List<Evaluation> evaluations, ExecutionParameters executionParameters, String executionName) {
        Workbook workbook;
        FileInputStream file;

        try {
            // Ouvre l'Excel contenant les résultats
            file = new FileInputStream(fileName);
            workbook = WorkbookFactory.create(file);

            Sheet sheet;
            int rownum;
            int iteration = 1;

            // Récupère la feuille existante ou la crée au besoin
            if (workbook.getSheet(executionName) != null) {
                sheet = workbook.getSheet(executionName);
                rownum = sheet.getLastRowNum() + 4;
            }
            else {
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

            for (int i = 0 ; i < 10 ; i++) {
                sheet.autoSizeColumn(i);
            }

            file.close();
        } catch (IOException ex) {
            return;
        }

        // Enregistre les modifications dans le fichier
        try {
            FileOutputStream outFile = new FileOutputStream(fileName);
            workbook.write(outFile);
            outFile.close();
            System.out.println("Results saved successfully in " + fileName);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Supprime les données présentes dans l'Excel en vue d'une nouvelle exécution
     */
    public void cleanExcel() {
            XSSFWorkbook workbook = new XSSFWorkbook();

            try {
                File file = new File(fileName);
                file.getParentFile().mkdirs(); // Créer le dossier si nécessaire
                FileOutputStream outFile = new FileOutputStream(file);

                workbook.write(outFile);
                outFile.close();
            } catch (IOException exc) {
                System.out.println(exc.getMessage() + " : tentative de création échouée...");
            }
    }
}
