package com.example.main.paperviewtester;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaperItemView extends LinearLayout{

    private Activity mActivity;
    private TextView mCaption;
    private TextView mUsername;
    private ImageView mDrawing;


    public PaperItemView(Context context, AttributeSet attrs){
        super(context, attrs);

        mActivity = (Activity) context;
        Init();
    }

    public PaperItemView(Context context){
        super(context);
        mActivity = (Activity) context;
        Init();
    }

    public PaperItemView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        Init();
    }

    private void Init(){
        inflate(getContext(), R.layout.paper_view_item, this);

        mCaption = (TextView) findViewById(R.id.text_caption);
        mDrawing = (ImageView) findViewById(R.id.image_drawing);
        mUsername = (TextView) findViewById(R.id.text_username);

    }

    public void SetData(String caption, String username){
        mCaption.setText(caption);
        mUsername.setText("-" + username);

        mCaption.setVisibility(VISIBLE);
        mUsername.setVisibility(VISIBLE);
        this.setVisibility(VISIBLE);
    }

    private Bitmap drawBit;
    private String userText;

    public void SetData(Bitmap drawing, String username){
        drawBit = drawing;
        userText = username;

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDrawing.setImageBitmap(drawBit);
                mUsername.setText("-" + userText);

                mDrawing.setVisibility(VISIBLE);
                mUsername.setVisibility(VISIBLE);
                setVisibility(VISIBLE);
            }
        });
    }
}
