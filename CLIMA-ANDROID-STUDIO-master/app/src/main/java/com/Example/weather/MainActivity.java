package com.Example.weather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Example.weather.services.model.Root;
import com.Example.weather.services.model.WeatherService;

public class MainActivity extends AppCompatActivity {

    private EditText txtCountryISOCode = null;
    private EditText txtCityName = null;

    private TextView lblCurrent = null;

    private WeatherService service = null;

    ImageView imageView2;

    int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView2=findViewById(R.id.imageView2);

        initViews();
        service = new WeatherService("a9cf0f7a3cc84a884d84d4df48f057c2");
        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        txtCountryISOCode=(EditText)findViewById(R.id.txtCountryISOCode);
        txtCountryISOCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Aqui coloca el identificador del pais, ej: 'MX' de México",Toast.LENGTH_SHORT).show();

            }
        });

        txtCityName=(EditText)findViewById(R.id.txtCityName);
        txtCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Aqui coloca la ciudad correspondiente al pais",Toast.LENGTH_SHORT).show();

            }
        });

    }


    public  void initViews(){
        txtCountryISOCode = findViewById(R.id.txtCountryISOCode);
        txtCityName = findViewById(R.id.txtCityName);

        lblCurrent = findViewById(R.id.lblCurrent);
    }

    public void btnGetInfoOnClick(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        StringBuilder text = new StringBuilder();

        if(txtCountryISOCode.getText().toString().isEmpty() || txtCityName.getText().toString().isEmpty()){
            text.append(getString(R.string.Fields_cannot_be_empty));
            alert.setMessage(text);
            alert.setPositiveButton("Corrige porfi",null);

            alert.show();
        }else{
            getWeatherInfo(txtCityName.getText().toString(),txtCountryISOCode.getText().toString());
            contador++;

        }

        if (contador==1){

            imageView2.setImageResource(R.drawable.pngwing);

        }else if(contador==2){

            imageView2.setImageResource(R.drawable.clima);

        }else if(contador==3){

            imageView2.setImageResource(R.drawable.nube);

        }else if(contador==4){

            imageView2.setImageResource(R.drawable.nublado);
            contador=0;
        }

    }

    public void getWeatherInfo(String cityName, String countryISOCode){
        service.requestWeatherData(cityName, countryISOCode,(isNetworkError,statusCode, root) -> {
            if(!isNetworkError){
                if(statusCode == 200){
                    showWeatherInfo(root);
                }else{
                    Log.d("Weather", "Service error");
                }
            }else{
                Log.d("Weather", "Network error");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public  void showWeatherInfo(Root root) {
        String temp =  String.valueOf(root.getMain().getTemp());

        lblCurrent.setText(getString(R.string.current)+" "+temp);
    }
}