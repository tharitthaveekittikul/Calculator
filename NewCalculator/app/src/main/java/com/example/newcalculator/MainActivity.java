package com.example.newcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity {

    TextView workingsTV;
    TextView resultsTV;
    TextView historyTV;
    String historyText = "";
    String workings = "";
    boolean leftBracket = true;
    String formula = "";
    String tempFormula = "";
    FileOutputStream fos = null;
    FileInputStream fis = null;
    int count = 0;

    private static final String FILE_NAME = "historyData.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTextViews();
        loadFileHistory();
    }

    private void initTextViews() {
        workingsTV = (TextView) findViewById(R.id.workingsTextView);
        resultsTV = (TextView) findViewById(R.id.resultTextView);
        historyTV = (TextView) findViewById(R.id.historyTextView);
    }

    private void setWorkings(String givenValue) {
        workings = workings + givenValue;
        workingsTV.setText(workings);
    }

    public void clearOnClick(View view) {
        workingsTV.setText("");
        workings = "";
        resultsTV.setText("");
        leftBracket = true;
    }

    public void bracketsOnClick(View view) {
        if( leftBracket ){
            setWorkings("(");
            leftBracket = false;
        }
        else{
            setWorkings(")");
            leftBracket = true;
        }
    }

    public void loadFileHistory(){
        fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            historyTV.setText(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void equalOnClick(View view){
        Double result = null;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
        checkForPowerOf();

        try {
            result = (double)engine.eval(formula);
        } catch (ScriptException e) {
            Toast.makeText(this,"Invalid Input",Toast.LENGTH_SHORT).show();
        }

        if( result != null ) {
            resultsTV.setText(String.valueOf(result.doubleValue()));
            if(count == 4){
                historyText = "";
                count = 0;
            }
            historyText = historyText + workingsTV.getText().toString() + " = " + resultsTV.getText().toString() + "\n";
            String lineSeparator = System.getProperty("line.separator");
            try {
                fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
                fos.write(historyText.getBytes());
                fos.write(lineSeparator.getBytes());
                fos.flush();
                count += 1;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if( fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        loadFileHistory();

        workingsTV.setText("");
        workings = "";

    }

    private void checkForPowerOf() {
        ArrayList<Integer> indexOfPowers = new ArrayList<>();
        for( int i = 0 ; i < workings.length(); i++ ){
            if( workings.charAt(i) == '^' ){
                indexOfPowers.add(i);
            }
        }

        formula = workings;
        tempFormula = workings;
        for( Integer index : indexOfPowers ){
            changeFormula(index);
        }
        formula = tempFormula;
    }

    private void changeFormula(Integer index) {
        String numberLeft = "";
        String numberRight = "";

        for( int i = index + 1 ; i < workings.length() ; i++ ){
            if( isNumeric(workings.charAt(i))){
                numberRight = numberRight + workings.charAt(i);
            }
            else{
                break;
            }
        }

        for( int i = index - 1 ; i >= 0 ; i-- ){
            if( isNumeric(workings.charAt(i))){
                numberLeft = numberLeft + workings.charAt(i);
            }
            else{
                break;
            }
        }

        String original = numberLeft + "^" + numberRight ;
        String changed = "Math.pow(" + numberLeft + "," + numberRight + ")";
        tempFormula = tempFormula.replace(original,changed);
    }

    private boolean isNumeric(char c){
        if( c >= '0' && c <= '9' || c == '.' ){
            return true;
        }
        return false;
    }

    public void powerOnClick(View view) {
        setWorkings("^");
    }

    public void divOnClick(View view) {
        setWorkings("/");
    }

    public void sevenOnClick(View view) {
        setWorkings("7");
    }

    public void eightOnClick(View view) {
        setWorkings("8");
    }

    public void nineOnClick(View view) {
        setWorkings("9");
    }

    public void timesOnClick(View view) {
        setWorkings("*");
    }

    public void fourOnClick(View view) {
        setWorkings("4");
    }

    public void fiveOnClick(View view) {
        setWorkings("5");
    }

    public void sixOnClick(View view) {
        setWorkings("6");
    }

    public void subOnClick(View view) {
        setWorkings("-");
    }

    public void oneOnClick(View view) {
        setWorkings("1");
    }

    public void twoOnClick(View view) {
        setWorkings("2");
    }

    public void threeOnClick(View view) {
        setWorkings("3");
    }

    public void addOnClick(View view) {
        setWorkings("+");
    }

    public void dotOnClick(View view) {
        setWorkings(".");
    }

    public void zeroOnClick(View view) {
        setWorkings("0");
    }
}