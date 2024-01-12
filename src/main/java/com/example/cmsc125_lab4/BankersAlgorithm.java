package com.example.cmsc125_lab4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BankersAlgorithm {

    private int[][] allocation;
    private int[][] max;
    private int[] available;
    private int[] work;
    private boolean[] finish;
    private List<Integer> safeSequence;

    public BankersAlgorithm(String filePath) {
        readCSV(filePath);
        safeSequence = new ArrayList<>();
    }

    private void readCSV(String filePath) {
        List<ArrayList<String>> allocationList = new ArrayList<>();
        List<ArrayList<String>> maxList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int matrixCount = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                ArrayList<String> row = new ArrayList<>();

                for (String value : values) {
                    row.add(value);
                }

                if (line.isEmpty()) {
                    matrixCount += 1;
                }

                if (matrixCount == 0) {
                    allocationList.add(row);
                } else if (matrixCount == 1 && !line.isEmpty()) {
                    maxList.add(row);
                } else if (matrixCount == 2 && !line.isEmpty()) {
                    available = row.stream().mapToInt(Integer::parseInt).toArray();
                }
            }

            if (allocationList.isEmpty() || maxList.isEmpty() || available == null) {
                System.out.println("ERROR: Allocation, Max, or Available arrays are null.");
                System.exit(0);
            }

            if (allocationList.size() != maxList.size()) {
                System.out.println("ERROR: Allocation and Max matrices should have the same number of rows.");
                System.exit(0);
            }

            if (!checkForIntegerValues(allocationList) || !checkForIntegerValues(maxList)) {
                System.out.println("ERROR: Character found in input.");
                System.exit(0);
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("ERROR!");
        }
        allocation = convertToIntArray(allocationList);
        max = convertToIntArray(maxList);
        work = available.clone();
        finish = new boolean[allocation.length];
    }

    private boolean checkForIntegerValues(List<ArrayList<String>> list) {
        for (ArrayList<String> row : list) {
            for (String val : row) {
                try {
                    Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[][] convertToIntArray(List<ArrayList<String>> list) {
        int[][] result = new int[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            ArrayList<String> row = list.get(i);
            int[] intRow = row.stream().mapToInt(Integer::parseInt).toArray();
            result[i] = intRow;
        }
        return result;
    }

    public void executeBankersAlgorithm() {
        int processes = allocation.length;
        int resources = available.length;
        int count = 0;

        while (count < processes) {
            boolean found = false;
            for (int i = 0; i < processes; i++) {
                if (!finish[i]) {
                    int j;
                    for (j = 0; j < resources; j++) {
                        if (max[i][j] - allocation[i][j] > work[j])
                            break;
                    }
                    if (j == resources) {
                        for (int k = 0; k < resources; k++)
                            work[k] += allocation[i][k];
                        safeSequence.add(i);
                        finish[i] = true;
                        found = true;
                        count++;
                    }
                }
            }
            if (!found)
                break;
        }

        if (count < processes) {
            System.out.println("STATE: Unsafe");
        } else {
            System.out.print("STATE: Safe - ");
            for (int i = 0; i < safeSequence.size(); i++) {
                System.out.print("P");
                System.out.print(safeSequence.get(i));
                if (i != safeSequence.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        BankersAlgorithm bankersAlgorithm = new BankersAlgorithm("input_file3.csv");
        bankersAlgorithm.executeBankersAlgorithm();
    }
}
