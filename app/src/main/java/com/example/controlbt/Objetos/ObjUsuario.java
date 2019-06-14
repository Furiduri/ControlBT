package com.example.controlbt.Objetos;

public class ObjUsuario {

    private Integer ID;
    private String Name;
    private String Ubicacion;

    public ObjUsuario(Integer ID, String name, String ubicacion) {
        this.ID = ID;
        Name = name;
        Ubicacion = ubicacion;
    }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getUbicacion() {
        return Ubicacion;
    }
}
