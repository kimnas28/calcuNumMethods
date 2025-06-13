package numMethods;

import java.util.Scanner;
import java.util.Arrays;

public class MatrixMulti {
    Scanner strinp;
    String columns;
    String rows;
    String[] listcol;
    String[] listrow;
    int[] arrcol;
    int[] arrrow;
    int[][] final_matrix;
    
    public void run(){
        try{
            strinp = new Scanner(System.in);
            System.out.println("Columns: ");
            columns = strinp.nextLine();

            System.out.println("Rows: ");
            rows = strinp.nextLine();

            listcol = columns.split(" ");
            listrow = rows.split(" ");
            arrcol = new int[listcol.length];
            arrrow = new int[listrow.length];

            int i = 0;
            for (String item : listcol){
                arrcol[i] = Integer.parseInt(item);
                i++;
            }

            i = 0;
            for (String item : listrow){
                arrrow[i] = Integer.parseInt(item);
                i++;
            }

            final_matrix = new int[arrrow.length][arrcol.length];

            for (int y = 0; y < arrrow.length; y++){
                for (int x = 0; x < arrcol.length; x++){
                    final_matrix[y][x] = arrrow[y] * arrcol[x];
                }
            }

            System.out.println();

            for (int[] item : final_matrix){
                System.out.println(Arrays.toString(item));
            }
        }
        catch (Exception NumberFormatException){
            System.out.println("Invalid Input");
        }
        
    }
}