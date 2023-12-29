package com.example.actividadcontrolesseleccion_2;

public class PaginaWeb {
    private String nombre;
    private String url;
    private Integer imagen;
    private Integer id;

    public String getNombre() {
        return nombre;
    }

    public PaginaWeb setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PaginaWeb setUrl(String url) {
        this.url = url;
        return this;
    }

    public Integer getImagen() {
        return imagen;
    }

    public PaginaWeb setImagen(Integer imagen) {
        this.imagen = imagen;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public PaginaWeb setId(Integer id) {
        this.id = id;
        return this;
    }
}
