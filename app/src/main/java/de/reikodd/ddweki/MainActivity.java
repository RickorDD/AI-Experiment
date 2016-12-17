package de.reikodd.ddweki;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private DrawingView drawView;
    private Button clearButton,saveButton;
    private TextView txtViewDesc, txtViewData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = (DrawingView)findViewById(R.id.drawing);
        clearButton = (Button)findViewById(R.id.clear);
        clearButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(this);
        txtViewDesc = (TextView) findViewById(R.id.description);
        txtViewData = (TextView) findViewById(R.id.Data);
        drawView.setVisibility(View.INVISIBLE);
        getActionBar().setTitle("DDWEKI     Version:" + BuildConfig.VERSION_NAME);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.clear)
        {
            txtViewDesc.setText("");
            txtViewData.setText("");
            drawView.setVisibility(View.INVISIBLE);
            showDialog();

        }

        if(view.getId()==R.id.save)
        {
            drawView.postJson();
            Toast.makeText(MainActivity.this,
                    "JSON send to database", Toast.LENGTH_LONG).show();
            drawView.setVisibility(View.INVISIBLE);
            txtViewDesc.setText("");
            txtViewData.setText("");

        }
      }

    private void showDialog() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.desc_dialog, null);
        final EditText textDesc = (EditText) mView.findViewById(R.id.txtDesc);
        Button bDesc = (Button) mView.findViewById(R.id.okButton);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.show();
        bDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!textDesc.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Sucessfull",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    drawView.setVisibility(View.VISIBLE);
                    drawView.startNew();
                    txtViewDesc.setText(textDesc.getText().toString().replaceAll("\"",""));
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Not fill!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}