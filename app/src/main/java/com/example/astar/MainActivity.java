package com.example.astar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[10][6];

    private int startingPoint = 0;
    private boolean blokade = false;
    private int startingRow;
    private int startingColumn;
    private int endingRow;
    private int endingColumn;
    private int currentRow;
    private int currentColumn;
    double lowestFCost;
    int[][] parentX = new int[10][6];
    int[][] parentY = new int[10][6];
    boolean[][] wasHere = new boolean[10][6];
    double[][] hCost = new double[10][6];
    double[][] gCost = new double[10][6];
    double[][] fCost = new double[10][6];
    double tempGCost;
    int parrentRow;
    int parrentColumn;
    private int tempCurrentRow;
    private int tempCurrentColumn;
    static String blockadeString = "Blokada";
    static String startString = "Start";
    static String endString = "Koniec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
                fCost[i][j] = 1000;
                gCost[i][j] = 1000;
            }
        }

        final Button buttonBlokada = findViewById(R.id.button_blokada);
        buttonBlokada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!blokade) {
                    buttonBlokada.setText("Wylacz Blokade");
                }
                if (blokade) {
                    buttonBlokada.setText("Wlacz Blokade");
                }
                blokade = !blokade;
            }
        });
        final Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startingPoint = 0;
                for (Button[] button : buttons) {
                    for (Button but : button) {
                        but.setText("");
                    }
                }
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 6; j++) {
                        fCost[i][j] = 1000;
                        gCost[i][j] = 1000;
                        wasHere[i][j] = false;
                    }
                }
            }
        });
        final Button buttonZnajdzDroge = findViewById(R.id.button_droga);
        buttonZnajdzDroge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                znajdzDroge();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("") && !((Button) v).getText().toString().equals(blockadeString)) {
            return;
        }

        if (startingPoint == 0) {
            ((Button) v).setText(startString);
            ((Button) v).setTextColor(Color.BLACK);
            startingPoint++;
            String data = ((Button) v).toString();
            String x = data.substring(data.length() - 3);
            String z = x.substring(0, 2);
            startingRow = Integer.parseInt(z.substring(0, 1));
            startingColumn = Integer.parseInt(z.substring(1, 2));


        } else if (startingPoint == 2) {
            startingPoint++;
            ((Button) v).setText(endString);
            ((Button) v).setTextColor(Color.BLACK);
            String data = ((Button) v).toString();
            String x = data.substring(data.length() - 3);
            String z = x.substring(0, 2);
            endingRow = Integer.parseInt(z.substring(0, 1));
            endingColumn = Integer.parseInt(z.substring(1, 2));
        }
        startingPoint++;

        if (blokade == true && startingPoint > 2 && ((Button) v).getText() != startString && ((Button) v).getText() != endString) {
            ((Button) v).setText(blockadeString);
            ((Button) v).setTextColor(Color.BLACK);
        }
        if (blokade == false && startingPoint > 2 && ((Button) v).getText() != startString && ((Button) v).getText() != endString) {
            ((Button) v).setText("");
        }
    }

    private boolean znajdzDroge() {
        currentRow = startingRow;
        currentColumn = startingColumn;
        gCost[startingRow][startingColumn] = 0;
        do {
            sprawdzOtoczenie(currentRow, currentColumn);
            if (hCost[currentRow][currentColumn] != 0) {
                buttons[currentRow][currentColumn].setText("h" + Double.toString(hCost[currentRow][currentColumn]) + "\n g" + Double.toString(gCost[currentRow][currentColumn]));
                buttons[currentRow][currentColumn].setTextColor(Color.BLACK);
            }
        } while (currentRow != endingRow || currentColumn != endingColumn);
        while (currentRow != startingRow || currentColumn != startingColumn) {
            tempCurrentRow = parentX[currentRow][currentColumn];
            tempCurrentColumn = parentY[currentRow][currentColumn];
            currentRow = tempCurrentRow;
            currentColumn = tempCurrentColumn;
            if (buttons[tempCurrentRow][tempCurrentColumn].getText() != startString)
                buttons[tempCurrentRow][tempCurrentColumn].setTextColor(Color.RED);
        }
        ;


        return false;
    }

    private double obliczDystans(int x1, int y1, int x2, int y2) {
        double result = 0;
        int x = abs(x1 - x2);
        int y = abs(y1 - y2);
        while (x > 0 && y > 0) {
            result += 1.42;
            x--;
            y--;
        }
        result = result + x + y;
        return result;
    }

    private void sprawdzOtoczenie(int x, int y) {
        lowestFCost = 1000;
        parrentRow = x;
        parrentColumn = y;
        wasHere[x][y] = true;
        if (x < 9) {
            calculateDistanceAndCheck(x + 1, y, 1);
            if (y < 5) {
                calculateDistanceAndCheck(x + 1, y + 1, 1.42);
            }
            if (y > 0) {
                calculateDistanceAndCheck(x + 1, y - 1, 1.42);
            }
        }
        if (x > 0) {
            calculateDistanceAndCheck(x - 1, y, 1);

            if (y < 5) {
                calculateDistanceAndCheck(x - 1, y + 1, 1.42);
            }
            if (y > 0) {
                calculateDistanceAndCheck(x - 1, y - 1, 1.42);
            }
        }
        if (y < 5) {
            calculateDistanceAndCheck(x, y + 1, 1);
        }

        if (y > 0) {
            calculateDistanceAndCheck(x, y - 1, 1);
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                if (fCost[i][j] < lowestFCost && !wasHere[i][j]) {
                    currentRow = i;
                    currentColumn = j;
                    lowestFCost = fCost[i][j];
                } else if (fCost[i][j] == lowestFCost && !wasHere[i][j]) {
                    if (hCost[currentRow][currentColumn] > hCost[i][j]) {
                        currentRow = i;
                        currentColumn = j;
                    }
                }
            }
        }


    }

    private void calculateDistanceAndCheck(int _x, int _y, double g) {
        if (!buttons[_x][_y].getText().equals(blockadeString)) {
            hCost[_x][_y] = obliczDystans(endingRow, endingColumn, _x, _y);

            tempGCost = gCost[parrentRow][parrentColumn] + g;
            if (tempGCost < gCost[_x][_y]) {
                gCost[_x][_y] = tempGCost;
                parentX[_x][_y] = parrentRow;
                parentY[_x][_y] = parrentColumn;
            }
            fCost[_x][_y] = gCost[_x][_y] + hCost[_x][_y];
        }


    }
}