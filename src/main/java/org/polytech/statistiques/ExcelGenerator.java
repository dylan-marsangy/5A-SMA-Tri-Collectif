package org.polytech.statistiques;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.polytech.environnement.block.BlockValue;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Construit un Excel avec les résultat des évaluations faites sur les exécutions de l'algorithme
 */
public class ExcelGenerator {

    private ExcelGenerator() {
        cleanExcel();
    }

    private static final ExcelGenerator instance = new ExcelGenerator();

    public static final ExcelGenerator getInstance()
    {
        return instance;
    }

    public void fillExcel(List<Evaluation> evaluations, String executionName) {
        Workbook workbook = new HSSFWorkbook();
        FileInputStream file;
        try {
            file = new FileInputStream("demo.xls");
            workbook = WorkbookFactory.create(file);

            Sheet sheet;
            int rownum;

            if (workbook.getSheet(executionName) != null) {
                sheet = workbook.getSheet(executionName);
                rownum = sheet.getLastRowNum() + 2;
            }
            else {
                sheet = workbook.createSheet(executionName);
                rownum = 0;
            }

            Cell cell;
            Row row;

            row = sheet.createRow(rownum);

            // Itération
            cell = row.createCell(0, CellType.STRING);
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


            // Data
            for (Evaluation evaluation : evaluations) {
                rownum++;
                row = sheet.createRow(rownum);

                // Itération
                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(rownum);
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
//            // Bonus (E)
//            String formula = "0.1*C" + (rownum + 1) + "*D" + (rownum + 1);
//            cell = row.createCell(4, CellType.FORMULA);
//            cell.setCellFormula(formula);
            }

            file.close();
        } catch (IOException ex) {
            return;
        }

        try {
            FileOutputStream outFile = new FileOutputStream("demo.xls");
            workbook.write(outFile);
            outFile.close();
            System.out.println("Results saved successfully in demo.xls");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void cleanExcel() {

            HSSFWorkbook workbook = new HSSFWorkbook();

            try {
                FileOutputStream outFile = new FileOutputStream("demo.xls");
                workbook.write(outFile);
                outFile.close();
            }
            catch (IOException exc) {
                System.out.println(exc.getMessage() + ": tentative de création échouée...");
            }

    }
}
