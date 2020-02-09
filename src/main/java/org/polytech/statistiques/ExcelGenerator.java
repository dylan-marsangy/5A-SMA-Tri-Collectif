package org.polytech.statistiques;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.polytech.ExecutionParameters;
import org.polytech.environnement.block.BlockValue;

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
    private final String fileName = "demo.xls";

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
        // Nb blocs B
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Nb Blocs B");
        // Nb agents
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Nb Agents");
        // Nb lignes
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Nb Lignes");
        // Nb colonnes
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Nb Colonnes");
        // Taille mémoire
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Taille  mémoire");
        // Nb mouvements successifs
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Nb Mouvements Successifs");
        // K -
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("K -");
        // K +
        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue("K +");
        // Erreur
        cell = row.createCell(9, CellType.STRING);
        cell.setCellValue("Erreur");
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
        // Nb blocs B
        cell = row.createCell(1, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getNumberBlocksB());
        // Nb agents
        cell = row.createCell(2, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getNumberAgents());
        // Nb lignes
        cell = row.createCell(3, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getGridRows());
        // Nb colonnes
        cell = row.createCell(4, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getGridColumns());
        // Taille mémoire
        cell = row.createCell(5, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getMemorySize());
        // Nb mouvements successifs
        cell = row.createCell(6, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getSuccessiveMovements());
        // K -
        cell = row.createCell(7, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getkMinus());
        // K +
        cell = row.createCell(8, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getkPlus());
        // Erreur
        cell = row.createCell(9, CellType.NUMERIC);
        cell.setCellValue(executionParameters.getError());
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
        // Nombre de blocs A
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Nombre de blocs A");
        // Nombre de blocs B
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Nombre de blocs B");
        // A voisin avec A
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("A voisin avec A");
        // A voisin avec B
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("A voisin avec B");
        // B voisin avec B
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("B voisin avec B");
        // Nombre de colonies
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Nombre de colonies");
        // Taille moyenne d'une colonie
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("Taille moyenne d'une colonie");
        // Proportion de A par colonie
        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue("Proportion de A par colonie");
        // Proportion de B par colonie
        cell = row.createCell(9, CellType.STRING);
        cell.setCellValue("Proportion de B par colonie");
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
        // Nombre de A
        cell = row.createCell(1, CellType.NUMERIC);
        cell.setCellValue(evaluation.getTotalBlockWithValue(BlockValue.A));
        // Nombre de B
        cell = row.createCell(2, CellType.NUMERIC);
        cell.setCellValue(evaluation.getTotalBlockWithValue(BlockValue.B));
        // A voisin de A
        cell = row.createCell(3, CellType.NUMERIC);
        cell.setCellValue(evaluation.getNeighborhoodPercentage(BlockValue.A, BlockValue.A));
        // A voisin de B
        cell = row.createCell(4, CellType.NUMERIC);
        cell.setCellValue(evaluation.getNeighborhoodPercentage(BlockValue.A, BlockValue.B));
        // B voisin de B
        cell = row.createCell(5, CellType.NUMERIC);
        cell.setCellValue(evaluation.getNeighborhoodPercentage(BlockValue.B, BlockValue.B));
        // Nombre de colonies
        cell = row.createCell(6, CellType.NUMERIC);
        cell.setCellValue(evaluation.getNumberOfColonies());
        // Taille moyenne d'une colonie
        cell = row.createCell(7, CellType.NUMERIC);
        cell.setCellValue(evaluation.getAverageSizeOfColonies());
        // Proportion de A par colonie
        cell = row.createCell(8, CellType.NUMERIC);
        cell.setCellValue(evaluation.getAverageColoniesBlockWithValue(BlockValue.A));
        // Proportion de B par colonie
        cell = row.createCell(9, CellType.NUMERIC);
        cell.setCellValue(evaluation.getAverageColoniesBlockWithValue(BlockValue.B));
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
            HSSFWorkbook workbook = new HSSFWorkbook();

            try {
                FileOutputStream outFile = new FileOutputStream(fileName);
                workbook.write(outFile);
                outFile.close();
            }
            catch (IOException exc) {
                System.out.println(exc.getMessage() + ": tentative de création échouée...");
            }
    }
}
