package de.reikodd.ddweki;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private DrawingView drawView;
    private Button clearButton,saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = (DrawingView)findViewById(R.id.drawing);
        clearButton = (Button)findViewById(R.id.clear);
        clearButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(this);
        getActionBar().setTitle("DDWEKI     Version:" + BuildConfig.VERSION_NAME);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.clear)
        {
            showDialog();
            drawView.startNew();
        }

        if(view.getId()==R.id.save)
        {
            drawView.postJson();
            Toast.makeText(MainActivity.this,
                    "JSON send to database", Toast.LENGTH_LONG).show();
            drawView.startNew();
        }
      }

    private void showDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.desc_dialog, null);
        final EditText textDesc = (EditText) mView.findViewById(R.id.txtDesc);
        Button bDesc = (Button) mView.findViewById(R.id.okButton);
        bDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!textDesc.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Succesfull",Toast.LENGTH_SHORT).show();
                    Log.i("Reiko",textDesc.getText().toString());
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Not fill!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
}