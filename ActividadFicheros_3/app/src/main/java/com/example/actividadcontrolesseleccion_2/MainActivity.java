package com.example.actividadcontrolesseleccion_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<PaginaWeb> paginas = new LinkedList<>();
    private ListView lvPaginas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvPaginas = findViewById(R.id.lvPaginas);
        obtenerPaginas();
        PaginaWebAdapter adaptador = new PaginaWebAdapter(this, (PaginaWeb[]) paginas.toArray(new PaginaWeb[paginas.size()]));
        lvPaginas.setAdapter(adaptador);
    }

    private void obtenerPaginas() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.webs)))) {
            String linea = br.readLine();
            while (linea != null) {
                String[] arr = linea.split(";");
                String imgname = arr[2];
                String pkgname = getPackageName();
                int logoId = getResources().getIdentifier(imgname, "drawable", pkgname);
                paginas.add(new PaginaWeb().setNombre(arr[0]).setUrl(arr[1]).setImagen(logoId).setId(Integer.parseInt(arr[3])));

                linea = br.readLine();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}