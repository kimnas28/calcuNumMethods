package numMethods;

import java.util.Scanner;

public class CramersRule {
    Scanner strinp;
    String[] equations = new String[3];

    public void run(){
        try {
            strinp = new Scanner(System.in);

            System.out.println("Equation 1:");
            equations[0] = strinp.nextLine();

            System.out.println("Equation 2:");
            equations[1] = strinp.nextLine();

            System.out.println("Equation 3:");
            equations[2] = strinp.nextLine();

            int[][] matrix = new int[3][5];
            matrix[0] = str_to_int(equations[0]);
            matrix[1] = str_to_int(equations[1]);
            matrix[2] = str_to_int(equations[2]);


            int x = matrix[0][3];
            int y = matrix[1][3]; 
            int z = matrix[2][3]; 

            float A = determinant(matrix);
            System.out.println();
            System.out.println("|A|: " + A);
            System.out.println();

            int[][] xmatrix = new int[matrix.length][];
            for(int i = 0; i < matrix.length; i++){
                xmatrix[i] = matrix[i].clone();
            }

            int[][] ymatrix = new int[matrix.length][];
            for(int i = 0; i < matrix.length; i++){
                ymatrix[i] = matrix[i].clone();
            }

            int[][] zmatrix = new int[matrix.length][];
            for(int i = 0; i < matrix.length; i++){
                zmatrix[i] = matrix[i].clone();
            }
            xmatrix[0][0] = x;
            xmatrix[1][0] = y;
            xmatrix[2][0] = z;

            float A_x = determinant(xmatrix);
            System.out.println("|A_x|: " + A_x);
            System.out.println();

            ymatrix[0][1] = x;
            ymatrix[1][1] = y;
            ymatrix[2][1] = z;

            float A_y = determinant(ymatrix);
            System.out.println("|A_y|: " + A_y);
            System.out.println();

            zmatrix[0][2] = x;
            zmatrix[1][2] = y;
            zmatrix[2][2] = z;

            float A_z = determinant(zmatrix);
            System.out.println("|A_z|: " + A_z);
            System.out.println();

            float ansx = A_x / A;

            float ansy = A_y / A;

            float ansz = A_z / A;

            System.out.println("x = A_x / A");
            System.out.println("x = " + ansx);
            System.out.println();

            System.out.println("y = A_y / A");
            System.out.println("y = " + ansy);
            System.out.println();

            System.out.println("z = A_z / A");
            System.out.println("z = " + ansz);
        }
        catch (Exception NumberFormatException){
            System.out.println("Invalid Input");
        }
    }
    
    int[] str_to_int(String eq){
        eq = eq.replaceAll("\\+ ", "\\+");
        eq = eq.replaceAll("- ", "-");
        eq = eq.replaceAll("= ", "");
        
        String[] eqarr = eq.split(" ");
 
        for (int i = 0; i < eqarr.length; i++){
            if (i != 0){
                if (eqarr[i].length() == 2){
                    eqarr[i] = eqarr[i].replaceAll("y","1");
                    eqarr[i] = eqarr[i].replaceAll("z","1");
                }
                else{
                    eqarr[i] = eqarr[i].replaceAll("y","");
                    eqarr[i] = eqarr[i].replaceAll("z","");
                }
            }
            else{
                if (eqarr[i].length() == 2){
                    if (Character.isDigit(eqarr[i].charAt(0))){
                        eqarr[i] = eqarr[i].replaceAll("x","");
                    }
                    else {
                        eqarr[i] = eqarr[i].replaceAll("x","1");
                    }
                    
                }
                else{
                    eqarr[i] = eqarr[i].replaceAll("x","");
                }
            }
        }

        int[] numerical_eq = new int[4];
        int j = 0;
        
        for (String item : eqarr){
            numerical_eq[j] = Integer.parseInt(item);
            j++;
        }

        return numerical_eq;
    }
    
    int determinant(int[][] matrix){
        int dg1 = matrix[0][0] * matrix[1][1] * matrix [2][2];
        int dg2 = matrix[0][1] * matrix[1][2] * matrix [2][0];
        int dg3 = matrix[0][2] * matrix[1][0] * matrix [2][1];
        int dg4 = matrix[0][1] * matrix[1][0] * matrix [2][2];
        int dg5 = matrix[0][0] * matrix[1][2] * matrix [2][1];
        int dg6 = matrix[0][2] * matrix[1][1] * matrix [2][0];
        
        return ((dg1) + (dg2) + (dg3)) - ((dg4) + (dg5) + (dg6));
    }
}