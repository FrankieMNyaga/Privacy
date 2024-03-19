import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Privacy {
    private List<String[]> table;
    public Privacy(List<String[]> table) {
        this.table = table;
    }
     /*Returns contents of CSV file as a List<String[]> 
      * file - String name of input file
     */
    public static List<String[]> readCSV(String file) {
        List<String[]> values = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] row  = line.split(",");
                values.add(row);
            }
            return values;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /*Creates output text file of given List<String[]>
     * values - Input List<String[]> to be written to text file
     * txtFile - String name of output file 
     */
    public static void writeTxt(List<String[]> values, String txtFile) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(txtFile))) {
            for (String[] row : values) {
                for (String cell : row) {
                    bw.write(cell + " | ");
                }
                bw.newLine();
            }
            System.out.println("Success");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /* Returns String of data at given row
     * rowIndex - Index of row to be returned
    */
    public String getRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < this.table.size()) {
            StringBuilder builder = new StringBuilder();
            String[] row = this.table.get(rowIndex);
            for (int i = 0; i < row.length; i++) {
                builder.append(row[i]);
                if (i < row.length - 1) {
                    builder.append(" | ");
                }
            }
            return builder.toString();
        }
        else {
            return null;
        }
    }
    
    /* Returns String of data at given row and column
     * rowIndex - Index of row where desired cell is found
     * col - Column index where desired cell is found
     */
    public String getCell(int rowIndex, int col) {
        String rowStr = this.getRow(rowIndex);
        if(rowStr != null) {
            String[] row = rowStr.split(" | ");
            if (col >= 0 && col < row.length) {
                return row[col];
            }
        }
        return null;
    }

    /* Updates String of a given cell
     * rowIndex - Index of row where desired cell is found
     * col - Column index where desired cell is found
     * updateString - String which replaces current cell contents
     */
    public void setCell(int rowIndex, int col, String updateString) {
        if(rowIndex >= 0 && rowIndex < this.table.size()) {
            String[] row = this.table.get(rowIndex);
            if (col >= 0 && col < row.length) {
                row[col] = updateString;
            }
        }
    }

    /* Uses '*' to anonymize cells
     * rowIndex - Index of row to be anonymized
     * col - Index of column to be anonymized
     * degree - 0 = every other character is anonymized
     *          1 = everything between first and last char is anonymized
     *          2 = everything after first char is anonymized
     *          3 = everything is anonymized
     */
    public void anonymizeCell(int rowIndex, int col, int degree) {
        StringBuilder builder = new StringBuilder();
        String cell = this.getCell(rowIndex, col);
        if(this.table != null && degree == 3) {
            for(int i = 0; i < cell.length(); i++) {
                builder.append("*");
            }
        }
        if(this.table != null && degree == 2) {
            for(int i = 0; i < cell.length(); i++) {
                if(i ==0) {
                    builder.append(cell.charAt(0));
                }
                else {
                    builder.append("*");
                }
            }
        }
        if(this.table != null && degree == 1) {
            for(int i = 0; i < cell.length(); i++) {
                if(i ==0) {
                    builder.append(cell.charAt(0));
                }
                if(i == cell.length() - 1) {
                    builder.append(cell.charAt(cell.length() - 1));
                }
                else {
                    builder.append("*");
                }
            }
        }
        if(this.table != null && degree == 0) {
            for(int i = 0; i < cell.length(); i++) {
                if(i == 0) {
                    builder.append(cell.charAt(0));
                }
                if(i % 2 != 0) {
                    builder.append(cell.charAt(i));
                }
                else {
                    builder.append("*");
                }
            }
        }
        this.setCell(rowIndex, col, builder.toString());
    }

    /* Anonymized every cell in a column after given row
     * rowStart - Row where anonymization starts
     * col - Column to be anonymized
     * degree - 0 = every other character is anonymized
     *          1 = everything between first and last char is anonymized
     *          2 = everything after first char is anonymized
     *          3 = everything is anonymized
     */
    public void anonymizeCol(int rowStart, int col, int degree) {
        for (int i = 0; i < this.table.size(); i++) {
            for (int j = 0; j < this.table.get(i).length; j++) {
                if((col >= 0) && (j == col) && (i >= rowStart)) {
                    this.anonymizeCell(i, col, degree);
                }
            }
        }
    }

    public static void main(String[] args) {
        String job = "resources/job_data.csv";
        String personal = "resources/personal_data.csv";
        String jobTxt = "job_data.txt";
        String personalTxt = "personal_data.txt";
        List<String[]> jobList = readCSV(job);
        List<String[]> personalList = readCSV(personal);
        Privacy jobPrivacy = new Privacy(jobList);
        Privacy personalPrivacy = new Privacy(personalList);
        writeTxt(jobList, jobTxt);
        writeTxt(personalList, personalTxt);
    }
}