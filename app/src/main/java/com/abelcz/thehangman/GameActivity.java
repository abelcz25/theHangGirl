package com.abelcz.thehangman;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private String[] words;
    private Random random;
    private String cWord;
    private TextView[] charViews;
    private LinearLayout wordLayout;
    private LetterAdapter adapter;
    private GridView gridView;
    private int nCorr;
    private int nChars;
    private ImageView[]parts;
    private int sizeParts = 6;
    private int cPart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        words=getResources().getStringArray(R.array.words);
        wordLayout=findViewById(R.id.words);
        gridView=findViewById(R.id.letters);
        random = new Random();

        parts=new ImageView[sizeParts];
        parts[0] = findViewById(R.id.head);
        parts[1] = findViewById(R.id.body);
        parts[2] = findViewById(R.id.armLeft);
        parts[3] = findViewById(R.id.armRight);
        parts[4] = findViewById(R.id.legLeft);
        parts[5] = findViewById(R.id.legRight);

        playGame();
    }

    private void playGame(){
        String newWord = words[random.nextInt(words.length)];
        while(newWord.equals(cWord))newWord=words[random.nextInt(words.length)];
        cWord = newWord;
        charViews = new TextView[cWord.length()];

        wordLayout.removeAllViews();

        for (int i=0; i<cWord.length(); i++){
            charViews[i] = new TextView(this);
            charViews[i].setText(""+cWord.charAt(i));
            charViews[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            charViews[i].setGravity(Gravity.CENTER);
            charViews[i].setTextColor(Color.WHITE);
            charViews[i].setBackgroundResource(R.drawable.letter_bg);
            wordLayout.addView(charViews[i]);
        }
        adapter=new LetterAdapter(this);
        gridView.setAdapter(adapter);
        nCorr=0;
        cPart=0;
        nChars=cWord.length();

        for (int i = 0; i<sizeParts; i ++){
            parts[i].setVisibility(View.INVISIBLE);
        }
    }

    public void letterPressed(View view) {
        String letter = ((TextView) view).getText().toString();
        char letterChar = letter.charAt(0);
        view.setEnabled(false);
        boolean correct = false;

        for (int i = 0; i < cWord.length(); i++) {
            if (cWord.charAt(i) == letterChar) {
                correct = true;
                nCorr++;
                charViews[i].setTextColor(Color.BLACK);
            }
        }
        if (correct){
            if (nCorr == nChars) {
                disableButtons();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Haz Ganado!!");
                builder.setMessage("Enhorabuena!!\n\n" + cWord + "\n\nFUE LA RESPUESTA CORRECTA");
                builder.setPositiveButton("Nueva partida", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity.this.playGame();
                    }
                });
                builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity.this.finish();
                    }
                });
                builder.show();
            }
        }
        else if (cPart < sizeParts) {
            parts[cPart].setVisibility(View.VISIBLE);
            cPart++;
        } else{
            disableButtons();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Haz Perdido :C");
            builder.setMessage("Lo sentimos\n\n" + cWord + "\n\nERA LA RESPUESTA CORRECTA");
            builder.setNegativeButton("Intentar de nuevo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.playGame();
                }
            });
            builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.finish();
                }
            });
            builder.show();
        }
    }
    public void  disableButtons(){
        for (int i = 0; i < gridView.getChildCount(); i++){
            gridView.getChildAt(i).setEnabled(false);
        }
    }
}
