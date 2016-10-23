package com.learn_weather.sun.learnweather.util;

import android.util.Log;

import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sun on 2016/10/20.
 */

public class HttpUtil {
    private static final String TAG="TT";
    private static final String localTag="HttpUtil" + "__";


    public static void sendHttpRequest(final String address, final
    HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                HttpURLConnection connection=null;
                Log.d(TAG, localTag + "run: begin to connect the address:" +
                        address);
//                BufferedReader reader=null;
                InputStream in=null;

                try {
                    URL url=new URL(address);
                    connection=(HttpURLConnection) url.openConnection();
                    Log.d(TAG, localTag + "run: connection has been " +
                            "build.....");
                    connection.setRequestMethod("GET");
//                    connection.addRequestProperty("Accept-Encoding", "gzip");
                    connection.setRequestProperty("Content-Type", "text/html;" +
                            "" + " charset=UTF-8");


                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

//                        connection.setRequestProperty("Content-type",
//                                "application/x-java-serialized-object");
                    /*if (connection.getResponseCode() != HttpURLConnection
                            .HTTP_OK) {
                        Log.d(TAG, localTag + "run: can't connect the url!");
                        throw new ConnectException("fail to connect the " +
                                "url:" + address);
                    }*/
                    connection.connect();
                    String mimeType=connection.getContentType();
                    String encoding=connection.getContentEncoding();
                    Log.d(TAG, localTag + "run: \nencoding=" + encoding + "; " +
                            " " +
                            "mimeType=" + mimeType);

                    in=connection.getInputStream();
                    Log.d(TAG, localTag + "run: in=" + in.toString());


//                    BufferedInputStream buffIn=new BufferedInputStream(in);

                   /* buffIn.mark(100);



                    for (int i=0; i < 10; i++) {
                        int b=buffIn.read();
                        char c=(char) b;
                        Log.d(TAG, localTag + "run: read the inputstream:" +
                                "b[" + i + "]=" + b + "; c[" + i + "]=" + c);
                    }
                    Log.d(TAG, localTag + "run: before buffIN reset");
                    buffIn.reset();*/





                /*    int b;
                    byte[] tempBytes=new byte[200];
                    int k=0;

                    try {
                        while ((b=buffIn.read()) != -1 && k < tempBytes.length) {
                            tempBytes[k]=(byte) b;
                            Log.d(TAG, localTag + "char: " + (char) tempBytes[k]);
                            k++;
                        }
                    } catch (EOFException e) {
                        Log.d(TAG, localTag + "run: arrive the end of file!");
                    }
                    byte[] bytes=new byte[k];
                    for (int i=0; i < k; i++) {
                        bytes[i]=tempBytes[i];
                    }*/


                    InputStreamReader reader=new InputStreamReader(in);
                    StringBuilder response=new StringBuilder();
                    int character;
                    try {
                        while ((character=reader.read())!=-1){
                            response.append((char)character);
                            Log.d(TAG, localTag + "InputStreamReader " +
                                    "read() char: "+ (char)character);
                        }
                    } catch (EOFException e) {
                        Log.d(TAG, localTag + "run: arrive to the end of the " +
                                "file");
                        e.printStackTrace();
                    }

                  /*  int b;
                    byte[] tempBytes=new byte[200];
                    int k=0;

                    try {
                        while ((b=in.read()) != -1 && k < tempBytes.length) {
                            tempBytes[k]=(byte) b;
                            Log.d(TAG, localTag + "char: " + (char) tempBytes[k]);
                            k++;
                        }
                    } catch (EOFException e) {
                        Log.d(TAG, localTag + "run: arrive the end of file!");
                        e.printStackTrace();
                    }
                    byte[] bytes=new byte[k];
                    for (int i=0; i < k; i++) {
                        bytes[i]=tempBytes[i];
                    }

                    String response=new String(bytes);
                    Log.d(TAG, localTag + "run: response=" + response);
                    listener.onFinish(response);*/

                    Log.d(TAG, localTag + "run: response=" + response.toString());
                    listener.onFinish(response.toString());


                } catch (Exception e) {
                    if (listener != null) {
                        Log.d(TAG, localTag + "run: catch some error:" + e
                                .toString());
                        e.printStackTrace();
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        Log.d(TAG, localTag + "run: disconnect");
                        connection.disconnect();
                    }
                   /* try {
                        if (in != null && reader != null) {
                            in.close();
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                }
            }
        }).start();
    }

}
