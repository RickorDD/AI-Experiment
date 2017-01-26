package de.reikodd.ddweki;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private DrawingView drawView;
    private Button clearButton,saveButton;
    private TextView txtViewDesc, txtViewData;
    String DescString="";

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
            drawView.New(DescString);
        }

        if(view.getId()==R.id.save)
        {
            drawView.Save(DescString);
            drawView.setVisibility(View.INVISIBLE);
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
                    dialog.dismiss();
                    drawView.setVisibility(View.VISIBLE);
                    DescString = textDesc.getText().toString().replaceAll("\"","");
                    txtViewDesc.setText(DescString);
                }
                else
                {
                    textDesc.setError("set a Letter!");
                    //Toast.makeText(MainActivity.this,"Not fill!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}