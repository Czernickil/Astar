package com.example.astar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    double najkrotszyDystans;
    double[][] dystansOdStartu = new double[10][6];
    double[][] dystansDoKonca = new double[10][6];
    double[][] sumaDystansow = new double[10][6];
    boolean[][] najmniejszy = new boolean[10][6];


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
            }
        }

        final Button buttonBlokada = findViewById(R.id.button_blokada);
        buttonBlokada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!blokade){
                    buttonBlokada.setText("Wylacz Blokade");
                }
                if(blokade){
                    buttonBlokada.setText("Wlacz Blokade");
                }
                blokade =! blokade;
            }
        });
        final Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startingPoint =0;
                for (Button[] button : buttons) {
                    for (Button but : button)
                        but.setText("");
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
        if (!((Button) v).getText().toString().equals("") && !((Button) v).getText().toString().equals("Blokada")) {
            return;
        }

        if (startingPoint==0) {
            ((Button) v).setText("Start");
            startingPoint++;
            String data =((Button) v).toString();
            String x =data.substring(data.length() - 3);
            String z =x.substring(0,2);
            startingRow = Integer.parseInt(z.substring(0,1));
            startingColumn = Integer.parseInt(z.substring(1,2));

        } else if (startingPoint==2) {
            startingPoint++;
            ((Button) v).setText("Koniec");
            String data =((Button) v).toString();
            String x =data.substring(data.length() - 3);
            String z =x.substring(0,2);
            endingRow = Integer.parseInt(z.substring(0,1));
            endingColumn = Integer.parseInt(z.substring(1,2));
        }
        startingPoint++;

         if (blokade == true && startingPoint>2 && ((Button) v).getText()!="Start" && ((Button) v).getText()!="Koniec") {
            ((Button) v).setText("Blokada");
        }
        if (blokade == false && startingPoint>2 && ((Button) v).getText()!="Start" && ((Button) v).getText()!="Koniec") {
            ((Button) v).setText("");
        }
    }

    private boolean znajdzDroge() {
        currentRow=startingRow;
        currentColumn=startingColumn;
        do {
            sprawdzOtoczenie(currentRow, currentColumn);
            if(dystansDoKonca[currentRow][currentColumn]!=0)
            buttons[currentRow][currentColumn].setText(Double.toString(dystansDoKonca[currentRow][currentColumn]));
        }while(currentRow!=endingRow || currentColumn !=endingColumn);




        return false;
    }

    private double obliczDystans(int x1, int y1, int x2, int y2) {
        double result=0;
        int x=abs(x1-x2);
        int y=abs(y1-y2);
        while(x>0 && y>0){
            result+=1.42;
            x--;
            y--;
        }
        result=result+x+y;
        return result;
    }

    private void sprawdzOtoczenie(int x, int y) {
        najkrotszyDystans =1000;
        if(x<9){
            calculateDistanceAndCheck( x+1, y);
            if(y<5) {
                calculateDistanceAndCheck( x+1, y+1);
            }
            if(y>0){
                calculateDistanceAndCheck( x+1, y-1);
            }
        }
        if(x>0){
            calculateDistanceAndCheck( x-1, y);

            if(y<5){
                calculateDistanceAndCheck( x-1, y+1);
            }
            if(y>0){
                calculateDistanceAndCheck( x-1, y-1);
            }
        }
        if(y<5){
            calculateDistanceAndCheck( x, y+1);
        }

        if(y>0){
            calculateDistanceAndCheck( x, y-1);
        }

    }
    private void calculateDistanceAndCheck(int _x, int _y) {
        dystansDoKonca[_x][_y] = obliczDystans(endingRow, endingColumn, _x, _y);
        double tempDist=obliczDystans(startingRow, startingColumn, _x, _y);
        if(dystansOdStartu[_x][_y]>tempDist || dystansOdStartu[_x][_y]==0)
        dystansOdStartu[_x][_y] = tempDist;
        if(najkrotszyDystans>dystansDoKonca[_x][_y]){
            najkrotszyDystans=dystansDoKonca[_x][_y];
            currentRow=_x;
            currentColumn=_y;
        }


    }
}