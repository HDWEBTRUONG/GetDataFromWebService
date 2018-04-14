package com.example.pctruong.getdatafromwebservice;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        DowloadJsonGet dowloadJson=new DowloadJsonGet();
//        dowloadJson.execute("http://192.168.1.90/weblazada/loaisanpham.php?maloaicha=0");
        DowloadJsonPost dowloadJson=new DowloadJsonPost();
        dowloadJson.execute("http://192.168.1.90/GetDataServer/loaisanpham.php","1");
    }
    public class DowloadJsonGet extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... Url) {
            StringBuilder builder = null;
            try {
                URL url=new URL(Url[0]);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection(); // dán đường dẫn vào trình duyệt
                httpURLConnection.connect();//Enter trên trình duyệt
                InputStream inputStream=httpURLConnection.getInputStream();// dữ liệu ở dạng luồng stream
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);// Đọc từng dòng dữ liệu
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);// Bộ nhớ đệm
                String line="";
                builder=new StringBuilder();
                while ((line=bufferedReader.readLine())!=null){
                    builder.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  builder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("gggg",s);
        }
    }

    public class DowloadJsonPost extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... Url) {
            StringBuilder builder = null;
            try {
                URL url=new URL(Url[0]);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection(); // dán đường dẫn vào trình duyệt
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                Uri.Builder uri=new Uri.Builder();
                uri.appendQueryParameter("maloaicha",Url[1]);
                String postdulieu=uri.build().getEncodedQuery();




                OutputStream outputStream=httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream);
                BufferedWriter writer=new BufferedWriter(outputStreamWriter);
                writer.write(postdulieu);

                writer.flush();
                writer.close();
                outputStream.close();
                outputStreamWriter.close();

                httpURLConnection.connect();//Enter trên trình duyệt

                InputStream inputStream=httpURLConnection.getInputStream();// dữ liệu ở dạng luồng stream
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);// Đọc từng dòng dữ liệu
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);// Bộ nhớ đệm
                String line="";
                builder=new StringBuilder();
                while ((line=bufferedReader.readLine())!=null){
                    builder.append(line);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  builder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            List<LoaiSP> list=new ArrayList<>();
            LoaiSP loaiSP=null;
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONArray jsonArray=jsonObject.getJSONArray("LOAISANPHAM");
                int count=jsonArray.length();
                for(int i=0 ;i<count;i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    loaiSP=new LoaiSP();
                    loaiSP.setMaloaicha(object.getString("MALOAISP"));
                    loaiSP.setTenloaisp(object.getString("TENLOAISP"));
                    loaiSP.setMaloaicha(object.getString("MALOAICHA"));
                    list.add(loaiSP);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, ""+list.size(), Toast.LENGTH_SHORT).show();

        }
    }
}
