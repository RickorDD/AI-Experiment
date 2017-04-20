package de.reikodd.ddweki;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener, URLInterface {

    private DrawingView drawView;
    private Button nextButton, saveButton, challengeButton, cancelButton;
    private TextView txtViewDesc, txtViewData;
    public String jsData;
    public int numberChallenge;
    private ProgressDialog pdGetChallenges;
    private ProgressDialog pdSendChallenge;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = (DrawingView) findViewById(R.id.drawing);
        nextButton = (Button) findViewById(R.id.next);
        nextButton.setOnClickListener(this);
        nextButton.setEnabled(false);

        saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(this);
        saveButton.setEnabled(false);

        challengeButton = (Button) findViewById(R.id.challenges);
        challengeButton.setOnClickListener(this);
        challengeButton.setEnabled(false);

        cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(this);
        cancelButton.setEnabled(false);

        txtViewDesc = (TextView) findViewById(R.id.description);
        txtViewData = (TextView) findViewById(R.id.Data);
        drawView.setVisibility(View.INVISIBLE);
        getActionBar().setTitle("TouchRecorder - V" + BuildConfig.VERSION_NAME);
        putURLConnection();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.next) {
            nextButton.setEnabled(false);
            drawView.New(jsData, numberChallenge);
        }

        if (view.getId() == R.id.save) {
            drawView.Save(jsData, numberChallenge);
            saveButton.setEnabled(false);
            cancelButton.setEnabled(false);
            txtViewDesc.setTextSize(22);
            txtViewDesc.setText("send to server...");
            txtViewData.setText("");
            drawView.setVisibility(View.INVISIBLE);
            pdSendChallenge = new ProgressDialog(MainActivity.this);
            pdSendChallenge.setMessage("Send Challenges to Server...");
            pdSendChallenge.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdSendChallenge.show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    while (txtViewDesc.getText().equals("Send Challenges to Server..."))
                    {}
                    pdSendChallenge.dismiss();
                }
            }, 1500);
        }


        if (view.getId() == R.id.challenges) {
            txtViewDesc.setTextSize(22);
            challengeButton.setEnabled(false);
            putURLConnection();
        }

        if (view.getId() == R.id.cancel) {
            txtViewDesc.setText("");
            txtViewData.setText("");
            nextButton.setEnabled(false);
            saveButton.setEnabled(false);
            cancelButton.setEnabled(false);
            challengeButton.setEnabled(true);
            drawView.setVisibility(View.INVISIBLE);
            drawView.Cancel();
        }
    }

    private void putURLConnection() {
        pdGetChallenges = new ProgressDialog(MainActivity.this);
        pdGetChallenges.setMessage("Load Challenges from Server...");
        pdGetChallenges.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdGetChallenges.show();
        txtViewDesc.setText("");
        new URLConnection(MainActivity.this).execute("https://touchrecorderweb.herokuapp.com/api/challenges");
    }


    @Override
    public void receivedContent(String content) {
        jsData = content;
        if (JSONParser.getNumberChallenges(jsData) > 0) {
            showDialogChallenge();
        } else {
            showNoConnection();
        }
    }

    private void showNoConnection() {

        txtViewDesc.setText("no challenges or not connection!");
        challengeButton.setEnabled(true);
        pdGetChallenges.dismiss();
    }


    private void showDialogChallenge() {
        txtViewDesc.setText("");
        pdGetChallenges.dismiss();
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.challenge_dialog, null);
        final TextView txtViewNumberChallenges = (TextView) mView.findViewById(R.id.txtChallenge);
        final ListView lstChallenges = (ListView) mView.findViewById(R.id.listChallenge);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                challengeButton.setEnabled(true);
            }
        });


        ArrayAdapter listAdapater = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, IntToArray.getArray(JSONParser.getNumberChallenges(jsData))) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(JSONParser.getNameChallenge(jsData, position));
                text2.setText(JSONParser.getDetailsChallenge(jsData, position));

                return view;
            }
        };

        lstChallenges.setAdapter(listAdapater);
        lstChallenges.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                numberChallenge = lstChallenges.getPositionForView(view);
                txtViewDesc.setTextSize(15);
                txtViewDesc.setText(JSONParser.getNameChallenge(jsData, lstChallenges.getPositionForView(view))
                        + " - " + JSONParser.getDetailsChallenge(jsData, lstChallenges.getPositionForView(view)));
                drawView.setVisibility(View.VISIBLE);
                drawView.New(jsData, lstChallenges.getPositionForView(view));
                cancelButton.setEnabled(true);
                challengeButton.setEnabled(false);
                dialog.dismiss();
            }
        });


        if (numberChallenge > 1) {
            txtViewNumberChallenges.setText(JSONParser.getNumberChallenges(jsData) + " Challenges");
        } else {
            txtViewNumberChallenges.setText(JSONParser.getNumberChallenges(jsData) + " Challenge");
        }
        dialog.setCancelable(true);
        dialog.show();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.95);
        dialog.getWindow().setLayout(width, height);
    }
}