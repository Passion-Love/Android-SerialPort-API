package android.serialport.sample;

import static java.lang.Thread.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
//[121.389688, 31.218573]
//[121.389688, 31.218564]
//[121.389688, 31.218601]
//[121.389688, 31.218500]
public class BoatActivity extends SerialPortActivity{
    SendingThread mSendingThread;
    ReadThread readThread;
    WebThread webThread;
    String vesselName="none";
    private InputStream mInputStream;
    byte[] buffer = new byte[35];
    AIS message =new AIS();
    PostThread postThread;
    Boolean geted1=false;
    Boolean geted2=false;
    Boolean geted3=false;
    byte[] mBuffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boat);

        Log.d("NOW", "1" );
        message.setVesselName("none");
        if (mSerialPort != null) {

            readThread = new ReadThread();
            readThread.start();
        }

//        Log.e("ship:","ship");
        postThread = new PostThread();
        postThread.start();
        webThread = new WebThread();
        webThread.start();
        mSendingThread = new SendingThread();
        mSendingThread.start();
    }


    @Override
    protected void onDataReceived(final byte[] buffer, int size) {
        //notify();
        BoatActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TableRow tab2 = (TableRow) findViewById(R.id.tab2);
                TextView text2=new TextView(getApplicationContext());
                text2.setText(String.format("%02x", buffer[0])+" "+String.format("%02x", buffer[1]));
                tab2.addView(text2);
                TableRow tab3 = (TableRow) findViewById(R.id.tab3);
                TextView text3=new TextView(getApplicationContext());
                text3.setText(String.format("%02x", buffer[2]));
                tab3.addView(text3);
                vesselName=String.format("%02x", buffer[2]);
                TableRow tab4 = (TableRow) findViewById(R.id.tab4);
                TextView text4=new TextView(getApplicationContext());
                text4.setText(String.format("%02x", buffer[3]));
                tab4.addView(text4);
                TableRow tab5 = (TableRow) findViewById(R.id.tab5);
                TextView text5=new TextView(getApplicationContext());
                //text5.setText(String.format("%02x", buffer[4])+" "+String.format("%02x", buffer[5])+" "+String.format("%02x", buffer[6])+" "+String.format("%02x", buffer[7])+" "+String.format("%02x", buffer[8])+" "+String.format("%02x", buffer[9])+" "+String.format("%02x", buffer[10])+" "+String.format("%02x", buffer[11]));
                text5.setText("("+String.format("%02x", buffer[4])+String.format("%02x", buffer[5])+"."+String.format("%02x", buffer[6])+String.format("%02x", buffer[7])+String.format("%02x", buffer[8])+","+String.format("%02x", buffer[9])+String.format("%02x", buffer[10])+"."+String.format("%02x", buffer[11])+String.format("%02x", buffer[12])+String.format("%02x", buffer[13])+")");
                tab5.addView(text5);
                double[] loc={Double.valueOf(String.format("%02x", buffer[4])+String.format("%02x", buffer[5])+"."+String.format("%02x", buffer[6])+String.format("%02x", buffer[7])+String.format("%02x", buffer[8])),Double.valueOf(String.format("%02x", buffer[9])+String.format("%02x", buffer[10])+"."+String.format("%02x", buffer[11])+String.format("%02x", buffer[12])+String.format("%02x", buffer[13]))};
                message.setLocation(loc);

                TableRow tab6 = (TableRow) findViewById(R.id.tab6);
                TextView text6=new TextView(getApplicationContext());
                //text6.setText(String.format("%02x", buffer[12])+" "+String.format("%02x", buffer[13])+" "+String.format("%02x", buffer[14])+" "+String.format("%02x", buffer[15])+" "+String.format("%02x", buffer[16])+" "+String.format("%02x", buffer[17])+" "+String.format("%02x", buffer[18])+" "+String.format("%02x", buffer[19])+" "+String.format("%02x", buffer[20])+" "+String.format("%02x", buffer[21])+" "+String.format("%02x", buffer[22])+" "+String.format("%02x", buffer[23]));
                text6.setText(String.format("%02x", buffer[14])+String.format("%02x", buffer[15])+"年"+String.format("%02x", buffer[16])+"月"+String.format("%02x", buffer[17])+"日"+String.format("%02x", buffer[18])+"时"+String.format("%02x", buffer[19])+"分"+String.format("%02x", buffer[20])+"秒");
                tab6.addView(text6);

                TableRow tab7 = (TableRow) findViewById(R.id.tab7);
                TextView text7=new TextView(getApplicationContext());
                text7.setText(String.format("%02x", buffer[26])+"."+String.format("%02x", buffer[27]));
                tab7.addView(text7);
                message.setHeading(Double.valueOf(String.format("%02x", buffer[26])+"."+String.format("%02x", buffer[27])));

                TableRow tab8 = (TableRow) findViewById(R.id.tab8);
                TextView text8=new TextView(getApplicationContext());
                text8.setText(String.format("%02x", buffer[28])+String.format("%02x", buffer[29]));
                tab8.addView(text8);
                TableRow tab9 = (TableRow) findViewById(R.id.tab9);
                TextView text9=new TextView(getApplicationContext());
                if(String.format("%02x", buffer[30]).equals("01")){
                    text9.setText("正常");
                }
                else{
                    text9.setText("异常");
                }
                tab9.addView(text9);
                TableRow tab10 = (TableRow) findViewById(R.id.tab10);
                TextView text10=new TextView(getApplicationContext());
                text10.setText(String.format("%02x", buffer[31]));
                tab10.addView(text10);

                message.setAreaShape(Integer.valueOf(String.format("%02x", buffer[32])+String.format("%02x", buffer[33])));

                TableRow tab11 = (TableRow) findViewById(R.id.tab11);
                TextView text11=new TextView(getApplicationContext());
                text11.setText(String.format("%02x", buffer[34]));
                tab11.addView(text11);

                message.setAISMessageType(1);
                message.setMMSI("testShip");

                //船A初始位置 东27
//                double[] loc1={10.0,12.0};
//                message.setLocation(loc1);
                message.setVesselName(String.format("%02x", buffer[2]));
                message.setShipid(Integer.valueOf(String.format("%02x", buffer[2])));
//                Log.e("ship1:",message.getVesselName());
                mBuffer = new byte[63];
                Arrays.fill(mBuffer, (byte) 0x66);
                mBuffer[0]=(byte) 0x7E;
                mBuffer[1]=(byte) 0x7E;
                mBuffer[2]=(byte) 0xA6;

                geted1=true;
            }
        });
    }

    public class ReadThread extends Thread {
        @Override
        public void run() {

            while (!isInterrupted()) {
                int size;
                try {

                    Log.e("n:","1");
                    if (mInputStream == null){
                        Log.e("n:","5");
                        return;
                    }
                    Log.e("n:","2");
                    size = mInputStream.read(buffer);
                    Log.e("n:","3");
                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    };

    public class WebThread extends Thread {
        @Override
        public void run() {
            while(!isInterrupted()){
                while(!geted2){

                }
                URI serverURI = URI.create("ws://10.0.0.14:8081/ws/"+vesselName);
                WebSocketClient webSocketClient = new WebSocketClient(serverURI){
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
//                    Log.d("mes1","mes");
                    }

                    @Override
                    public void onMessage(final String message) {
                        Log.d("mes2",message);
                        BoatActivity.this.runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                TableLayout tableLayout = (TableLayout) findViewById(R.id.tablay1);
                                TableRow tab = new TableRow(getApplicationContext());
                                TextView text=new TextView(getApplicationContext());
                                text.setText(message);
                                tab.addView(text);
                                tableLayout.addView(tab);
                            }
                        });
                    }


                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.d("mes3",reason);

                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.d("mes4","mes");
                    }
                };
                webSocketClient.connect();
                geted2=false;
                geted3=true;
            }
        }
    };

    public class PostThread extends Thread {

        @Override
        public void run() {
            while(!interrupted()){
                while(!geted1){

                }
//            Log.d("NOW", "2" );
                MyHttpClient httpClient = new MyHttpClient(getApplicationContext());
                String url = "http://10.0.0.14:8081/boat";
                //第二步：生成使用POST方法的请求对象
                HttpPost httpPost = new HttpPost(url);

                Gson gson = new Gson();
                String json = gson.toJson(message);
                try {
                    StringEntity requestEntity = new StringEntity(json, HTTP.UTF_8);
                    httpPost.setEntity(requestEntity);
                    httpPost.addHeader("Content-Type", "application/json");
                    //执行请求对象
                    try {
                        //第三步：执行请求对象，获取服务器发还的相应对象
                        HttpResponse response = httpClient.execute(httpPost);
                        //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                        Log.d("NO",Integer.toString(response.getStatusLine().getStatusCode() ) );
                        if (response.getStatusLine().getStatusCode() == 200) {
                            //第五步：从相应对象当中取出数据，放到entity当中
                            HttpEntity entity = response.getEntity();
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(entity.getContent()));
                            String result = reader.readLine();
                            //Log.d("HTTP", "POST:" + result);
                        }
                        else{
                            Log.d("NO",Integer.toString(response.getStatusLine().getStatusCode() ) );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                geted1=false;
                geted2=true;
            }
        }
    };

    private class SendingThread extends Thread {
        @Override
        public void run() {
            while(!interrupted()){
                while(!geted3){

                }
                try {
                    if (mOutputStream != null) {
                        mOutputStream.write(mBuffer);
                    } else {
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                geted3=false;
            }
        }
    }

    public void retm(View view){
        Intent i = new Intent(BoatActivity.this , SerialPortPreferences.class);
        startActivity(i);    }

}
