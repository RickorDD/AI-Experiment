package de.reikodd.ddweki;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * zeichnet eine Linie in ein Canvas
 */
public class DrawingView extends View {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    static long startTime = 0;
    static String DeviceModel = Build.MODEL;

    static DecimalFormat decimalFormat = new DecimalFormat("0.00000", new DecimalFormatSymbols(Locale.US));
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

    public void startNew() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        TextView txtView = (TextView) ((Activity) context).findViewById(R.id.Data);
        txtView.setText("");
        jsonCreate.clear();
        invalidate();
        startTime = System.currentTimeMillis();
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

    static String JsonHashEntry(String key, double value) {
        return "\"" + key + "\":" + decimalFormat.format(value);
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

                double showTimeDouble = (double) (System.currentTimeMillis() - startTime) / 1000.;
                String jsonOutput = "{" + TextUtils.join(",", new String[]{
                    JsonHashEntry("x", event.getX()),
                    JsonHashEntry("y", event.getY()),
                    JsonHashEntry("t", showTimeDouble),
                    JsonHashEntry("p", event.getPressure())
                }) + "}";

                TextView txtView = (TextView) ((Activity) context).findViewById(R.id.Data);
                txtView.setText(jsonOutput);

                jsonCreate.addStroke(jsonOutput);
                break;

            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                jsonCreate.endStroke();
                Log.i("Reiko", "neuer Stroke");
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void postJson(){
            TextView txtView = (TextView) ((Activity) context).findViewById(R.id.description);
            StringBuilder sb = new StringBuilder();

            sb.append("[{\"strokes\":");
            sb.append(jsonCreate.getJSON());
            sb.append(",");

            sb.append("\"description\":\"");
            sb.append(txtView.getText().toString());
            sb.append("\",");

            sb.append("\"client\":\"");
            sb.append(DeviceModel);
            sb.append("\"");
            sb.append("}]");

            new URLConnection().execute("http://52.212.255.218/datas/",sb.toString());
            //new URLConnection().execute("http://groens.ch/ai-experment-api/datas",sb.toString());
        }
}
