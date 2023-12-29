package com.example.actividadcontrolesseleccion_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PaginaWebAdapter extends ArrayAdapter<PaginaWeb> {
    public PaginaWebAdapter(@NonNull Context context, @NonNull PaginaWeb[] datos) {
        super(context, R.layout.paginaweb_layout, datos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PaginaWeb pagina = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.paginaweb_layout, null);
        ImageView ivLogo = view.findViewById(R.id.ivLogoWeb);
        ivLogo.setImageResource(pagina.getImagen());

        TextView tvNombre = view.findViewById(R.id.tvNombreWeb);
        tvNombre.setText(pagina.getNombre());
        TextView tvDireccion = view.findViewById(R.id.tvDireccionWeb);
        tvDireccion.setText(pagina.getUrl());

        return view;
    }
}
