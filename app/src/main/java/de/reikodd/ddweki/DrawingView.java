package de.reikodd.ddweki;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DrawingView extends View {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    static long startTime = 0;
    String reqName = "AndroidApp";
    static List<String> archive = new ArrayList<String>();
    static int strokes = 0;
    static int characterChallenge=0;
    static boolean allCharacter=false;
    static DecimalFormat decimalFormat = new DecimalFormat("0.0", new DecimalFormatSymbols(Locale.US));
    JSONCreate jsonCreate = new JSONCreate();
    Context context;

    public DrawingView(Context context) {
        super(context);
        this.context = context;
        setUpDrawing();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setUpDrawing();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setUpDrawing();
    }

    private void setUpDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setColor(Color.BLACK);
        drawPaint.setStrokeWidth(3);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStyle(Paint.Style.STROKE);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h,
                Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        drawCanvas.drawColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;

            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);

                double showTimeDouble = (double) (System.currentTimeMillis() - startTime);
                try {
                    JSONObject jsonXYTP = new JSONObject();
                    jsonXYTP.put("x", decimalFormat.format(event.getX()));
                    jsonXYTP.put("y", decimalFormat.format(event.getY()));
                    jsonXYTP.put("t", decimalFormat.format(showTimeDouble));
                    jsonXYTP.put("p", decimalFormat.format(event.getPressure()));
                    Button   nextButton = (Button ) ((Activity) context).findViewById(R.id.next);
                    Button saveButton = (Button) ((Activity) context).findViewById(R.id.save);
                    Button cancelButton = (Button) ((Activity) context).findViewById(R.id.cancel);

                    if(allCharacter==true) {
                        saveButton.setEnabled(true);
                    }
                    else
                    {
                        nextButton.setEnabled(true);
                    }
                    jsonCreate.addStroke(jsonXYTP.toString());
                    break;
                } catch (JSONException e) {
                }

            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                jsonCreate.endStroke();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void New(String content, int numberChallenge) {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        TextView txtViewData = (TextView) ((Activity) context).findViewById(R.id.Data);
        txtViewData.setTextSize(18);
        txtViewData.setText("Draw a \"" + JSONParser.getCharacterDescSet(content, numberChallenge, characterChallenge)+ "\"");
        if (strokes!=0)
        {
            archive.add(jsonCreate.getJSON(JSONParser.getCharacterDescSet(content, numberChallenge, characterChallenge-1)));
        }
        if((JSONParser.getNumberDescSet(content,numberChallenge)-1)==characterChallenge)
        {
            allCharacter=true;
        }
        strokes++;
        characterChallenge++;
        jsonCreate.clear();
        invalidate();
        startTime = System.currentTimeMillis();
    }

    public void Cancel()
    {
        strokes = 0;
        characterChallenge=0;
        allCharacter=false;
        archive.clear();
    }


    public void Save(String content, int numberChallenge) {
        archive.add(jsonCreate.getJSON(JSONParser.getCharacterDescSet(content, numberChallenge, characterChallenge-1)));
        StringBuilder sb = new StringBuilder();
        sb.append("{\"submission\":{\"archive\":");
        sb.append(archive);
        sb.append(",");
        sb.append("\"author\":\""+ reqName + "\"");
        sb.append(",");
        sb.append("\"challenge_id\":" + JSONParser.getChallengeID(content, numberChallenge));
        sb.append("}}");
        strokes = 0;
        characterChallenge=0;
        allCharacter=false;
        archive.clear();
        new URLPost(context).execute("https://touchrecorderweb.herokuapp.com/api/submissions", sb.toString());
    }
}