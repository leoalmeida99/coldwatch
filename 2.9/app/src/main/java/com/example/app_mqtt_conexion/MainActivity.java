package com.example.app_mqtt_conexion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Build;//para obtener el nombre del dispositivo
import android.widget.TextView;
import android.widget.Toast;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import android.view.View;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    static String MQTTHOST = "tcp://mudviper133.cloud.shiftr.io"; //el broker
    static String USERNAME = "mudviper133";//el token de acceso
    static String PASSWORD = "tccIncubadoraDePlantas3P1";    //la contraceña del token
    MqttAndroidClient client;
    TextView txtStatusTemp;
    String topicoTemperatura = "Temperatura";
    String topicStr1 = "TEMPERATURA";
    String topicStr2 = "UMIDADE";
    String topicStr3 = "ALERTA";
    String topicStr4 = "COR_DA_LED";


    private TextView textView;
    private ProgressBar progressBar;
    private SeekBar seekBarA;

    private TextView textView2;
    private ProgressBar progressBar2;
    private SeekBar seekBarB;

    private TextView textView3;
    private ProgressBar progressBar3;
    private SeekBar seekBar3;

    private TextView caixa;
    private SeekBar colorSlider;

    TextView TextDasCores, resultTv2;
    ImageView mImageView;
    int bitmap;
    Button botaoUmidade, botaoAbrirTelaCor, botaoTemperatura, botaoReservatorio, botaoAplicarCor;
    Button botaoTempRecomendada, botaoUmidadeRecomendada, botaoAlertaRecomendado, botaoCorRecomendada;

    int progressTemperatura, progressUmidade, progressAlerta;
    int Red, Green, Blue;
    int valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Nesse bloco o modelo e fabricante do celular apacera no servidor
        String nombre_Dispositivo;
        String fabricante = Build.MANUFACTURER;
        String modelo = Build.MODEL;
        nombre_Dispositivo=fabricante+" "+modelo;
        String clientId = nombre_Dispositivo;

        /**/
        //String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        //txtStatusTemp = findViewById(R.id.statusTemp);

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this, "Conectado ao Servidor", Toast.LENGTH_SHORT).show();
                    setSubscrition();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this, "Desconectado", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


        setTitle("TCC");



        //colorSeekBar = findViewById(R.id.color_seekbar);
        TextDasCores = findViewById(R.id.TextDasCores);
        resultTv2 = findViewById(R.id.resultTv2);

        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        seekBarA = (SeekBar) findViewById(R.id.seekBarA);

        textView2 = (TextView) findViewById(R.id.textView2);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        seekBarB = (SeekBar) findViewById(R.id.seekBarB);

        textView3 = (TextView) findViewById(R.id.textView3);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        seekBar3 = (SeekBar) findViewById(R.id.seekBar3);

        botaoAplicarCor = findViewById(R.id.btnAplicarCor);
        botaoAbrirTelaCor = findViewById(R.id.btnCor);
        botaoUmidade = findViewById(R.id.btnUmidade);
        botaoTemperatura=findViewById(R.id.btnTemperatura);
        botaoReservatorio = findViewById(R.id.btnReservatorio);

        botaoTempRecomendada = findViewById(R.id.btnTemperaturaRecomendada);
        botaoUmidadeRecomendada = findViewById(R.id.btnUmidadeRecomendada);
        botaoAlertaRecomendado = findViewById(R.id.btnAlarmeRecomendada);
        botaoCorRecomendada = findViewById(R.id.btnCorRecomendada);

        caixa = findViewById(R.id.TextDasCores);
        colorSlider = findViewById(R.id.color_seekbarDasCores);


        //ESTADO INICIAL DOS BOTÕES
        botaoTemperatura.setEnabled(false);
        botaoUmidade.setEnabled(false);
        botaoReservatorio.setEnabled(false);
        botaoAplicarCor.setEnabled(false);

        //TEMPERATURA
        seekBarA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress1, boolean fromUser) {
                progressBar.setProgress(progress1);
                textView.setText("" + progress1/2 + "ºC");
                progressTemperatura = progress1/2;
                botaoTemperatura.setEnabled(true);
                botaoTemperatura.setText("Aplicar");

                if(progress1 == 0) {
                    botaoTemperatura.setEnabled(false);
                }else{
                    botaoTemperatura.setEnabled(true);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //UMIDADE
        seekBarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar2.setProgress(progress);
                textView2.setText("" + progress + "%");
                progressUmidade = progress;
                botaoUmidade.setEnabled(true);
                botaoUmidade.setText("Aplicar");

                if(progress == 0) {
                    botaoUmidade.setEnabled(false);
                }else{
                    botaoUmidade.setEnabled(true);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //ALERTA
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar3.setProgress(progress);
                textView3.setText("" + progress + "%");
                progressAlerta = progress;
                botaoReservatorio.setEnabled(true);
                botaoReservatorio.setText("Aplicar");

                if(progress == 0) {
                    botaoReservatorio.setEnabled(false);
                }else{
                    botaoReservatorio.setEnabled(true);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //BOTÃO QUE DEFINE A TEMPERATURA
        botaoTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = topicStr1;
                String message = progressTemperatura + "";
                try {
                    client.publish(topic, message.getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                botaoTemperatura.setText("Aplicado");
                botaoTemperatura.setEnabled(false);
                Toast.makeText(MainActivity.this, "Temperatura Aplicada em  " + progressTemperatura + "ºC", Toast.LENGTH_SHORT).show();
            }
        });

        //BOTÃO QUE DEFINE A TEMPERATURA RECOMENDADADA
        botaoTempRecomendada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seekBarA.setProgress(64);

                Toast.makeText(MainActivity.this, "Temperatura está em  " + progressTemperatura + "ºC", Toast.LENGTH_SHORT).show();
            }
        });

        //BOTÃO QUE DEFINE A UMIDADE
        botaoUmidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = topicStr2;
                String message = progressUmidade + "";
                try {
                    client.publish(topic, message.getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                botaoUmidade.setText("Aplicado");
                botaoUmidade.setEnabled(false);
                Toast.makeText(MainActivity.this, "Umidade Aplicada em " + progressUmidade + "%", Toast.LENGTH_SHORT).show();
            }
        });

        //BOTÃO QUE DEFINE A UMIDADE RECOMENDADA
        botaoUmidadeRecomendada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seekBarB.setProgress(50);

                Toast.makeText(MainActivity.this, "Umidade está em " + progressUmidade + "%", Toast.LENGTH_SHORT).show();
            }
        });


        //BOTÃO QUE DEFINE O RESERVATORIO
        botaoReservatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = topicStr3;
                String message = progressAlerta + "";
                try {
                    client.publish(topic, message.getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                botaoReservatorio.setText("Aplicado");
                botaoReservatorio.setEnabled(false);
                Toast.makeText(MainActivity.this, "Alerta Aplicado em  " + progressAlerta + "%", Toast.LENGTH_SHORT).show();
            }
        });

        //BOTÃO QUE DEFINE O ALERTA DO RESERVATORIO
        botaoAlertaRecomendado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seekBar3.setProgress(20);

                Toast.makeText(MainActivity.this, "Alerta está em  " + progressAlerta + "%", Toast.LENGTH_SHORT).show();
            }
        });



        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{
                Color.rgb(255,0,0),
                Color.rgb(255,255,0),
                Color.rgb(0,255,0),
                Color.rgb(0,255,255),
                Color.rgb(0,0,255),
                Color.rgb(255,0,255),
                Color.rgb(255,0,0),

        });

        gradientDrawable.setCornerRadius(15);

        colorSlider.setProgressDrawable(gradientDrawable);

        colorSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (0 < progress && progress < 256) {
                    int red = 255;
                    int green = progress - 0;
                    int blue = 0;

                    Red = red;
                    Green = green;
                    Blue = blue;

                    caixa.setBackgroundColor(Color.rgb(red, green, blue));
                    resultTv2.setText(/*"HEX: " + hex + "    " +*/ "RGB: " + red + ", " + green + ", " + blue);
                } else if (255 < progress && progress < 511) {
                    int red = 510 - progress;
                    int green = 255;
                    int blue = 0;

                    Red = red;
                    Green = green;
                    Blue = blue;

                    caixa.setBackgroundColor(Color.rgb(red, green, blue));
                    resultTv2.setText(/*"HEX: " + hex + "    " +*/ "RGB: " + red + ", " + green + ", " + blue);
                } else if (510 < progress && progress < 766) {
                    int red = 0;
                    int green = 255;
                    int blue = progress - 510;

                    Red = red;
                    Green = green;
                    Blue = blue;

                    caixa.setBackgroundColor(Color.rgb(red, green, blue));
                    resultTv2.setText(/*"HEX: " + hex + "    " +*/ "RGB: " + red + ", " + green + ", " + blue);
                } else if (766 < progress && progress < 1021) {
                    int red = 0;
                    int green = 1020 - progress;
                    int blue = 255;

                    Red = red;
                    Green = green;
                    Blue = blue;

                    caixa.setBackgroundColor(Color.rgb(red, green, blue));
                    resultTv2.setText(/*"HEX: " + hex + "    " +*/ "RGB: " + red + ", " + green + ", " + blue);
                } else if (1020 < progress && progress < 1276) {
                    int red = progress - 1020;
                    int green = 0;
                    int blue = 255;

                    Red = red;
                    Green = green;
                    Blue = blue;

                    caixa.setBackgroundColor(Color.rgb(red, green, blue));
                    resultTv2.setText(/*"HEX: " + hex + "    " +*/ "RGB: " + red + ", " + green + ", " + blue /*+ "aqui" + progress*/);
                } else if (1275 < progress && progress <= 1530) {
                    int red = 255;
                    int green = 0;
                    int blue = 1530 - progress;

                    Red = red;
                    Green = green;
                    Blue = blue;

                    caixa.setBackgroundColor(Color.rgb(red, green, blue));
                    resultTv2.setText(/*"HEX: " + hex + "    " +*/ "RGB: " + red + ", " + green + ", " + blue /*+ "aqui" + progress*/);
                }

                botaoAplicarCor.setEnabled(true);
                botaoAplicarCor.setText("Aplicar");

                if(progress == 0) {
                    botaoAplicarCor.setEnabled(false);
                }else{
                    botaoAplicarCor.setEnabled(true);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //BOTÃO QUE DEFINE A COR DO TEXTVIEW
        botaoAplicarCor.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String topic = topicStr4;
                String message = "L4, cor funcionando";
                try {
                    client.publish(topic, message.getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                botaoAplicarCor.setText("Aplicado");
                botaoAplicarCor.setEnabled(false);
                Toast.makeText(MainActivity.this, "Cor aplicada em RGB: " + Red + "," + Green + "," + Blue, Toast.LENGTH_SHORT).show();
            }
        });

        //BOTÃO QUE DEFINE A COR DO TEXTVIEW
        botaoCorRecomendada.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                TextDasCores.setBackgroundColor(getResources().getColor(R.color.Violeta));
                colorSlider.setProgress(1275);

                Toast.makeText(MainActivity.this, "Cor aplicada em RGB: " + Red + "," + Green + "," + Blue, Toast.LENGTH_SHORT).show();
            }
        });

        botaoAbrirTelaCor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent activity_main2 = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(activity_main2);


            }
        });
    }

    private void setSubscrition() {
        try {
            client.subscribe(topicoTemperatura, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    /* ********************************************************************************************** */

    public void ligar1(View v) {
        String topic = topicStr1;
        String message = "L1, temp funcionando";
        try {
            client.publish(topic, message.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void ligar2(View v) {
        String topic = topicStr2;
        String message = "L2, umidade funcionando";
        try {
            client.publish(topic, message.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void ligar3(View v) {
        String topic = topicStr3;
        String message = "L3, alerta funcionando";
        try {
            client.publish(topic, message.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void ligar4(View v) {
        String topic = topicStr4;
        String message = "L4, cor funcionando";
        try {
            client.publish(topic, message.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}