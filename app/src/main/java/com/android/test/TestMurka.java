package com.android.gpstest;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestMurka extends Fragment {

    private View v ;
    public final static String TAG = "MURKA";

    private String currenttime ;
    private String Values1, Values2, Values3;
    private String time;

    private TextView mCurrentTime1, mCurrentTime2, mCurrentTime3 ;
    private EditText mValues1, mValues2, mValues3;

    private DatabaseHelper databaseHelper;
    private Context context;

    private boolean isOnline;
    private boolean isWaiting;
    private Values values;

    public static final String DEFAULT_STORAGE_LOCATION = Environment.getExternalStorageDirectory().getPath();
    public static final String TEST_FOLDER_RELATIVE = "/TEST/";
    public static final String TEST_STORAGE_LOCATION = DEFAULT_STORAGE_LOCATION + TEST_FOLDER_RELATIVE;
    private static final String FILENAME = "test.txt";

    @SuppressLint("SimpleDateFormat") // See #117
            SimpleDateFormat mDateFormat = new SimpleDateFormat(
            DateFormat.is24HourFormat(Application.get().getApplicationContext())
                    ? "HH:mm:ss" : "hh:mm:ss a");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.data_table_test, container,false);
        L.i(TAG, "Запуск фрагмента для просмотра значений надоя");



        mCurrentTime1 = v.findViewById(R.id.data_current);
        mCurrentTime2 = v.findViewById(R.id.data_current2);
        mCurrentTime3 = v.findViewById(R.id.data_current3);

        mValues1 = v.findViewById(R.id.edit_value1);
        mValues2 = v.findViewById(R.id.edit_value2);
        mValues3 = v.findViewById(R.id.edit_value3);

        Values1 = mValues1.getText().toString().trim();
        Values2 = mValues2.getText().toString().trim();
        Values3 = mValues3.getText().toString().trim();


        currenttime = String.valueOf(Calendar.getInstance().getTime());

        mCurrentTime1.setText(currenttime);
        mCurrentTime2.setText(currenttime);
        mCurrentTime3.setText(currenttime);


        Button saveBtn1 = (Button) v.findViewById(R.id.btn_save1);
        Button saveBtn2 = (Button) v.findViewById(R.id.btn_save2);
        Button saveBtn3 = (Button) v.findViewById(R.id.btn_save3);

        saveBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Application.get(), "Сохраняю значение надоя", Toast.LENGTH_LONG).show();
                //databaseHelper = new DatabaseHelper(context);
                write(Values1);
                L.i(TAG, "Значение записано");
                new HttpAsyncTask().execute("http://jsonplaceholder.typicode.com/");
            }

        });
        saveBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Application.get(), "Сохраняю значение веса", Toast.LENGTH_LONG).show();
                write(Values2);
                new HttpAsyncTask().execute("http://jsonplaceholder.typicode.com/");
            }

        });

        saveBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Application.get(), "Сохраняю значение температуры", Toast.LENGTH_LONG).show();
                write(Values3);
                new HttpAsyncTask().execute("http://jsonplaceholder.typicode.com/");
            }

        });




        return v;
    }




    private void write(String value) {

        Log.i("write", value);
        String filename = TEST_STORAGE_LOCATION + getFilename();

        try(FileWriter writer = new FileWriter(filename, false))
        {
            // запись всей строки
            L.i("write", "Записываю значение");
            writer.write(value);
//            // запись по символам
//            writer.append('\n');
//            writer.append('E');

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

//        databaseHelper.insertValuesAsync(value, new DatabaseHelper.DatabaseHandler<Void>() {
//            @Override
//            public void onComplete(boolean success, Void result) {
//                if (success) {
//                    if (isOnline && isWaiting) {
//                        isWaiting = false;
//                    }
//                }
//
//            }
//        });
    }

    private boolean isDirExist() {
        File dir  = new File(DEFAULT_STORAGE_LOCATION, TEST_FOLDER_RELATIVE);
        if(!dir.exists()){
            // :: если директория не существует, пробуем создать
            try {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
                L.i(TAG, "Директория успешно создана по адресу " + dir);
                // :: теперь существует, иначе выпадет Exception
                return true;
            } catch (Exception e) {
                L.e(TAG, "Невозможно содать директорию " + dir + " :: " + e);
                return false;
            }
        }
        else return true;
    }

    private String getFilename(){
        if (isDirExist()) {
            File file = new File(DEFAULT_STORAGE_LOCATION, FILENAME);
            if (file.exists())
                file.delete();
            return FILENAME;
        }
        else return null;
    }

/// отправка на сервер данных через JSON

    public String POST(String url, Values values){
        InputStream inputStream = null;
        String result = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) Application.get().getSystemService(Context.TELEPHONY_SERVICE);

            // создаю HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // сделать запрос POST указанному URL-адресу
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            JSONObject jsonObject = new JSONObject();




            jsonObject.accumulate("time", "null");
            jsonObject.accumulate("Values1", "null");
            jsonObject.accumulate("Values2", "null");
            jsonObject.accumulate("Values3", "null");


            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);


            httpPost.setEntity(se);


            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();


            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Не работает!";

        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
            e.printStackTrace();
        }


        return result;
    }


    // send data to json

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) Application.get().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {



            return POST(urls[0],values);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Application.get().getApplicationContext(), "Данные отправлены!", Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


}
