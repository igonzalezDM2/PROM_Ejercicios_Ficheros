package com.example.actividadficheros_1;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
public class MainActivity extends AppCompatActivity {
    private static final int SOLICITUD_PERMISO_WRITE_SD = 0;
    private static final int SOLICITUD_PERMISO_READ_SD = 1;
    private static final int SOLICITUD_PERMISO_NI_IDEA_YA = 2;

    private static String NOM_FICHERO_SD = "prueba_sd.txt";
    private static String NOM_FICHERO = "prueba_int.txt";

    private TextView tvContenidoFichero;
    private Button btnEscribirMemSD;
    private Button btnLeerMemSD;

    private Button btnBorrarMemSD;
    private Button btnBorrarMemInt;

    private EditText etContenido;

    private OPERACION_SD operacionSd = null;

    private enum OPERACION_SD {
        ESCRIBIR,
        LEER,
        BORRAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvContenidoFichero = findViewById(R.id.LblContenidoFichero);
        btnEscribirMemSD = findViewById(R.id.BtEscribirMemoriaSD);
        etContenido = findViewById(R.id.etContenido);
        btnEscribirMemSD.setOnClickListener(v -> {

            if (sdDisponible()) {
                operacionSd = OPERACION_SD.ESCRIBIR;
                if (!checkStoragePermissions()) {
                    requestForStoragePermissions();
                } else {
                    escribirEnSD();
                }
            }

        });
        btnLeerMemSD = findViewById(R.id.BtLeerMemoriaSD);
        btnLeerMemSD.setOnClickListener(v -> {

            if (sdDisponible()) {
                operacionSd = OPERACION_SD.ESCRIBIR;
                if (!checkStoragePermissions()) {
                    requestForStoragePermissions();
                } else {
                    leerDeSD();
                }
            }

        });

        btnBorrarMemSD = findViewById(R.id.BtBorrarMemoriaSD);
        btnBorrarMemSD.setOnClickListener(v -> {
            if (sdDisponible()) {
                operacionSd = OPERACION_SD.ESCRIBIR;
                if (!checkStoragePermissions()) {
                    requestForStoragePermissions();
                } else {
                    borrarDeSD();
                }
            }
        });

        btnBorrarMemInt = findViewById(R.id.BtBorrarMemoria);
        btnBorrarMemInt.setOnClickListener(v -> {
            if (sdDisponible()) {
                borrarInterno();
            }
        });

    }
    public void escribirFichero(View view) {
        try{
            OutputStreamWriter osw=
                    new OutputStreamWriter(openFileOutput(NOM_FICHERO,
                            Context.MODE_PRIVATE));
            osw.write(etContenido.getText().toString());
            Log.i ("Ficheros", "Escribiendo en fichero de memoria interna");
            osw.close();
        }
        catch (Exception e) {
            Log.e ("Ficheros",
                    "ERROR!! al escribir fichero a memoria interna");
        }
    }
    public void leerFichero (View view){
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput(NOM_FICHERO)));
            String texto="";
            String linea= fin.readLine();
            while (linea!=null){
                texto=texto+linea+"\n";
                Log.i("Ficheros", linea);
                linea=fin.readLine();
            }
            fin.close();
            tvContenidoFichero.setText(texto);
        }
        catch (Exception ex)
        {
            tvContenidoFichero.setText("");
            Toast.makeText(MainActivity.this, "No existe el fichero", Toast.LENGTH_SHORT).show();
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
    }
    public void leerRecurso(View view){
        try {
            InputStream fraw =
                    getResources().openRawResource(R.raw.prueba_raw);
            BufferedReader brin =
                    new BufferedReader( new InputStreamReader(fraw));
            String texto="";
            String linea= brin.readLine();
            while (linea!=null){
                texto=texto+linea+"\n";
                Log.i("Ficheros", linea);
                linea=brin.readLine();
            }
            fraw.close();
            tvContenidoFichero.setText(texto);
        }
        catch (Exception ex) {
            Log.e ("Ficheros", "Error al leer fichero desde recurso raw");
        }
    }

    public boolean checkStoragePermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager();
        }else {
            //Below android 11
            int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
        }
    }

    private static final int STORAGE_PERMISSION_CODE = 23;

    private void requestForStoragePermissions() {
        //Android is 11 (R) or above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            }catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        }else{
            //Below android 11
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }
    }

    private ActivityResultLauncher<Intent> storageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>(){

                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                                //Android is 11 (R) or above
                                if(Environment.isExternalStorageManager()){
                                    if (checkStoragePermissions()) { //Se vuelve a comprobar
                                        if (OPERACION_SD.ESCRIBIR.equals(operacionSd)) {
                                            escribirEnSD();
                                        } else if (OPERACION_SD.LEER.equals(operacionSd)) {
                                            leerDeSD();
                                        } else if (OPERACION_SD.BORRAR.equals(operacionSd)) {
                                            borrarDeSD();
                                        }

                                    }
                                    //Manage External Storage Permissions Granted
                                    Log.d("TAG", "onActivityResult: Manage External Storage Permissions Granted");
                                }else{
                                    Toast.makeText(MainActivity.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //Below android 11

                            }
                        }
                    });

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0){
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(read && write){
                    Toast.makeText(MainActivity.this, "Storage Permissions Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean sdDisponible (){
        boolean sdDisponible = false;
        boolean sdAccesoEscritura= false;
//Comprobamos el estado de la memoria externa
        String estado = Environment.getExternalStorageState();
        Log.i("Memoria", estado);
        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponible = true;
            sdAccesoEscritura = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDisponible = true;
            sdAccesoEscritura = false;
        } else {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }

        if (sdDisponible && sdAccesoEscritura)
            return true;
        else
            return false;
    }
    private void escribirEnSD (){
        try {
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(), NOM_FICHERO_SD);

            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter osw =
                    new OutputStreamWriter(new FileOutputStream(f));
            osw.write(etContenido.getText().toString());
            osw.close();
            Log.i("Ficheros", "fichero escrito correctamente");
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Ficheros", "Error al escribir fichero en tarjeta SD");
        }
    }

    private void borrarDeSD () {
        try {
            tvContenidoFichero.setText("");
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(), NOM_FICHERO_SD);

            if (f.exists()) {
                f.delete();
                Toast.makeText(MainActivity.this, "fichero borrado correctamente", Toast.LENGTH_SHORT).show();
                Log.i("Ficheros", "fichero borrado correctamente");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Ficheros", "Error al escribir fichero en tarjeta SD");
        }
    }

    private void borrarInterno () {
        try {
            tvContenidoFichero.setText("");
            File f = new File(getFilesDir(), NOM_FICHERO);

            if (f.exists()) {
                f.delete();
                Toast.makeText(MainActivity.this, "fichero borrado correctamente", Toast.LENGTH_SHORT).show();
                Log.i("Ficheros", "fichero borrado correctamente");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Ficheros", "Error al escribir fichero en tarjeta SD");
        }
    }

    private void leerDeSD(){
        try {
            File ruta_sd = Environment.getExternalStorageDirectory();
            File f = new File(ruta_sd.getAbsolutePath(), NOM_FICHERO_SD);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(f)));
            String texto = "";
            String linea = br.readLine();
            while (linea != null) {
                texto = texto + linea + "\n";
                linea = br.readLine();
            }
            br.close();
            Log.i("Ficheros", texto);
            tvContenidoFichero.setText(texto);
        } catch (Exception ex) {
            tvContenidoFichero.setText("");
            Toast.makeText(MainActivity.this, "No existe el fichero", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
            Log.e("Ficheros", "ERROR!! en la lectura del fichero en SD");
        }
    }
    private static void solicitarPermiso (final String permiso,
                                          String justificacion,
                                          final int requestCode,
                                          final Activity actividad){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                actividad, permiso)){
//Informamos al usuario para qué y por qué
//se necesitan los permisos
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ActivityCompat.requestPermissions(actividad,
                                            new String[]{permiso},requestCode);
                                }
                            }).show();
        }
        else {
//Muestra el cuadro de dialogo para la solicitud de permisos y
//registra el permiso según respuesta del usuario
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }
}