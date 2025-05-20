package com.example.app_mqtt_conexion;

import androidx.appcompat.app.AppCompatActivity;

/*
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
*/


//////////////////////////////////////////////////////////////////////////////////////


import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.eclipse.paho.android.service.MqttAndroidClient;
        import org.eclipse.paho.client.mqttv3.IMqttActionListener;
        import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
        import org.eclipse.paho.client.mqttv3.IMqttToken;
        import org.eclipse.paho.client.mqttv3.MqttCallback;
        import org.eclipse.paho.client.mqttv3.MqttClient;
        import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
        import org.eclipse.paho.client.mqttv3.MqttException;
        import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity2 extends AppCompatActivity {
    static String MQTTHOST = "tcp://mudviper133.cloud.shiftr.io"; //el broker
    static String USERNAME = "mudviper133";//el token de acceso
    static String PASSWORD = "tccIncubadoraDePlantas3P1";    //la contraceña del token
    MqttAndroidClient client;
    TextView txtStatusTemp, txtStatusUmi;
    String topicoTemperatura = "TEMPERATURA";
    String topicoUmidade = "UMIDADE";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Nesse bloco o modelo e fabricante do celular apacera no servidor
        String nombre_Dispositivo;
        String fabricante = Build.MANUFACTURER;
        String modelo = Build.MODEL;
        nombre_Dispositivo=fabricante+" "+modelo;
        String clientId = nombre_Dispositivo;

        //String clientId = MqttClient.generateClientId();
        client =  new MqttAndroidClient (this.getApplicationContext(), MQTTHOST, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        txtStatusTemp = findViewById(R.id.textView5);
        txtStatusUmi = findViewById(R.id.textView6);
        setTitle("SEGUNDA TELA");

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity2.this, "Conectado ao Servidor", Toast.LENGTH_SHORT).show();
                    setSubscrition1();
                    setSubscrition2();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity2.this, "Desconectado", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                if(topic.toString().equals("TEMPERATURA")){
                    txtStatusTemp.setText(new String(message.getPayload()));
                }
                if(topic.toString().equals("UMIDADE")){
                    txtStatusUmi.setText(new String(message.getPayload()));
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }
    private void setSubscrition1(){
        try{
            client.subscribe(topicoTemperatura,0);
        }catch (MqttException e) {
            e.printStackTrace();
        }
    }
    private void setSubscrition2(){
        try{
            client.subscribe(topicoUmidade,0);
        }catch (MqttException e) {
            e.printStackTrace();
        }
    }
}