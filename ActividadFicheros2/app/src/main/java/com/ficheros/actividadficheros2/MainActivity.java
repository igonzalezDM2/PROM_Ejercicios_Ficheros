package com.ficheros.actividadficheros2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner provSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        provSpinner = findViewById(R.id.prov_spinner);

        ArrayAdapter<String> provinciaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, leerProvincias());
        provinciaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provSpinner.setAdapter(provinciaAdapter);

    }

    private String[] leerProvincias() {
        List<String> provs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.provincias)))) {
            String prov = br.readLine();
            while (prov != null) {
                provs.add(prov);
                prov = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return provs.toArray(new String[provs.size()]);
    }

}