package org.polytech.statistiques;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;

public class ExcelGenerator {
    public ExcelGenerator() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Statistiques simulations");

        int rownum = 0;
        Cell cell;
        Row row;

        row = sheet.createRow(rownum);

        // EmpNo
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("EmpNo");
        // EmpName
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("EmpNo");
        // Salary
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Salary");
        // Grade
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Grade");
        // Bonus
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Bonus");

        // Data
//        for (Employee emp : list) {
//            rownum++;
//            row = sheet.createRow(rownum);
//
//            // EmpNo (A)
//            cell = row.createCell(0, CellType.STRING);
//            cell.setCellValue(emp.getEmpNo());
//            // EmpName (B)
//            cell = row.createCell(1, CellType.STRING);
//            cell.setCellValue(emp.getEmpName());
//            // Salary (C)
//            cell = row.createCell(2, CellType.NUMERIC);
//            cell.setCellValue(emp.getSalary());
//            // Grade (D)
//            cell = row.createCell(3, CellType.NUMERIC);
//            cell.setCellValue(emp.getGrade());
//            // Bonus (E)
//            String formula = "0.1*C" + (rownum + 1) + "*D" + (rownum + 1);
//            cell = row.createCell(4, CellType.FORMULA);
//            cell.setCellFormula(formula);
//        }
        File file = new File("../../../../../../demo.xls");
        file.getParentFile().mkdirs();

        try {
            FileOutputStream outFile = new FileOutputStream(file);
            workbook.write(outFile);
            System.out.println("Created file: " + file.getAbsolutePath());
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
