package com.example.main.paperviewtester;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParsePaper");
        query.whereEqualTo("completed", true);
        query.include("user_0");
        query.include("user_1");
        query.include("user_2");
        query.include("user_3");
        query.include("user_4");
        query.include("user_5");
        query.include("user_6");

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                System.out.println("Query done. Error? " + (e == null));
                if(e == null){
                    LoadPaper(parseObject);
                }else{
                    System.out.println("--Error code " + e.getCode());
                }
            }
        });
    }

    private void LoadPaper(ParseObject paper){
        System.out.println("Loading paper");
        LoadData("part_0_caption", paper.getString("part_0_caption"),
                paper.getParseUser("user_0").getUsername());

        new Thread(new DrawingLoader("part_0_drawing", paper.getParseFile("part_0_drawing"),
                paper.getParseUser("user_0").getUsername())).start();

        LoadData("part_1_caption", paper.getString("part_1_caption"),
                paper.getParseUser("user_1").getUsername());

        new Thread(new DrawingLoader("part_2_drawing", paper.getParseFile("part_2_drawing"),
                paper.getParseUser("user_2").getUsername())).start();

        LoadData("part_3_caption", paper.getString("part_3_caption"),
                paper.getParseUser("user_3").getUsername());

        new Thread(new DrawingLoader("part_4_drawing", paper.getParseFile("part_4_drawing"),
                paper.getParseUser("user_4").getUsername())).start();

        LoadData("part_5_caption", paper.getString("part_5_caption"),
                paper.getParseUser("user_5").getUsername());

        new Thread(new DrawingLoader("part_6_drawing", paper.getParseFile("part_6_drawing"),
                paper.getParseUser("user_6").getUsername())).start();
    }

    private void LoadData(String partName, String caption, String username){
        int id = getResources().getIdentifier(partName, "id", getPackageName());

        PaperItemView v = (PaperItemView) findViewById(id);
        v.SetData(caption, username);
    }

    private void LoadDrawing(String partName, ParseFile imgFile, String username){
        URL url = null;
        try {
            url = new URL(imgFile.getUrl());
        }catch(MalformedURLException e){
            System.out.println("Malformed url error " + e.getMessage());
        }

        if(url == null) return;
        URLConnection connection = null;

        try {
            connection = url.openConnection();
        }catch(IOException e){
            System.out.println("IO error " + e.getMessage());
        }

        if(connection == null) return;
        InputStream stream = null;

        try {
            stream = connection.getInputStream();
        }catch(IOException e){
            System.out.println("IO error " + e.getMessage());
        }

        Bitmap img = BitmapFactory.decodeStream(stream);

        LoadData(partName, img, username);

    }

    private void LoadData(String partName, Bitmap drawing, String username){
        int id = getResources().getIdentifier(partName, "id", getPackageName());

        PaperItemView v = (PaperItemView) findViewById(id);
        v.SetData(drawing, username);
    }

    private class DrawingLoader implements Runnable{
        private String mPartName;
        private String mUsername;
        private ParseFile mImgFile;

        public DrawingLoader(String partName, ParseFile imgFile, String username){
            mPartName = partName;
            mUsername = username;

            mImgFile = imgFile;

        }

        @Override
        public void run(){
            LoadDrawing(mPartName, mImgFile, mUsername);
        }
    }
}
