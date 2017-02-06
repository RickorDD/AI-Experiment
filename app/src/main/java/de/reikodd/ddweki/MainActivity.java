package de.reikodd.ddweki;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener, URLInterface {

    private DrawingView drawView;
    private Button nextButton,saveButton,challengeButton;
    private TextView txtViewDesc, txtViewData;
    static private Context context;
    public String jsData;
    public int numberChallenge;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getBaseContext();
        setContentView(R.layout.activity_main);
        drawView = (DrawingView)findViewById(R.id.drawing);
        nextButton = (Button)findViewById(R.id.next);
        nextButton.setOnClickListener(this);
        nextButton.setEnabled(false);

        saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(this);
        saveButton.setEnabled(false);

        challengeButton = (Button) findViewById(R.id.challenges);
        challengeButton.setOnClickListener(this);
        txtViewDesc = (TextView) findViewById(R.id.description);
        txtViewData = (TextView) findViewById(R.id.Data);
        drawView.setVisibility(View.INVISIBLE);
        getActionBar().setTitle("DDWEKI     Version:" + BuildConfig.VERSION_NAME);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.next)
        {
            nextButton.setEnabled(false);
            drawView.New(jsData, numberChallenge);
        }

        if(view.getId()==R.id.save)
        {
            drawView.Save(jsData, numberChallenge);
            saveButton.setEnabled(false);
            txtViewData.setText("");
            drawView.setVisibility(View.INVISIBLE);
        }

        if(view.getId()==R.id.challenges)
        {
            txtViewDesc.setText("Loading...");
            challengeButton.setEnabled(false);
            putURLConnection();
        }
    }

    private void putURLConnection(){
        new URLConnection(MainActivity.this).execute("https://sheltered-fjord-46378.herokuapp.com/api/challenges");
    }


    @Override
    public void receivedContent(String content) {
        jsData=content;
        if (JSONParser.getNumberChallenges(jsData)>0) {
            showDialogChallenge();
        }
        else
        {
            showNoConnection();
        }
    }

    private void showNoConnection() {

        txtViewDesc.setText("no challenges or not connection!");
        challengeButton.setEnabled(true);
    }


    private void showDialogChallenge()
    {
        txtViewDesc.setText("");
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.challenge_dialog, null);
        final TextView txtViewNumberChallenges = (TextView) mView.findViewById(R.id.txtChallenge);

        final ListView lstChallenges = (ListView) mView.findViewById(R.id.listChallenge);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        ArrayAdapter listAdapater = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, IntToArray.getArray(JSONParser.getNumberChallenges(jsData)))
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(JSONParser.getNameChallenge(jsData,position));
                text2.setText(JSONParser.getDetailsChallenge(jsData,position));

                return view;
            }
        };

        lstChallenges.setAdapter(listAdapater);
        lstChallenges.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                numberChallenge=lstChallenges.getPositionForView(view);
                txtViewDesc.setText(JSONParser.getNameChallenge(jsData, lstChallenges.getPositionForView(view))
                + " - " + JSONParser.getDetailsChallenge(jsData, lstChallenges.getPositionForView(view)));
                drawView.setVisibility(View.VISIBLE);
                drawView.New(jsData, lstChallenges.getPositionForView(view));
                dialog.dismiss();
            }
        });

        if (numberChallenge>1) {
            txtViewNumberChallenges.setText(JSONParser.getNumberChallenges(jsData) + " Challenges");
        }
        else
        {
            txtViewNumberChallenges.setText(JSONParser.getNumberChallenges(jsData) + " Challenge");
        }

        dialog.setCancelable(false);
        dialog.show();
    }
}