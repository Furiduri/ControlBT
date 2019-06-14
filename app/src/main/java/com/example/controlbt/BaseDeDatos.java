package com.example.controlbt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.controlbt.UtilidadesSQl.TablaUSER;

public class BaseDeDatos extends SQLiteOpenHelper {
    public BaseDeDatos(Context context) {
        super(context, "Arduino", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //STAR TABLE USUARIOS
        db.execSQL(TablaUSER.UP_CREAR_TABLA_USER);

        db.execSQL("insert into "+ TablaUSER.Tabla_Name+"("+TablaUSER.Campo_nombre+","+TablaUSER.Campo_password+","+TablaUSER.Campo_ubicacion+") values('Pablo','1234','0.0000,1.11111')");
        db.execSQL("insert into "+ TablaUSER.Tabla_Name+
                "("+TablaUSER.Campo_nombre+","+TablaUSER.Campo_password+","+TablaUSER.Campo_ubicacion+")"+
                " values('Uziel','12345','1.0000,1.11111')");
        db.execSQL("insert into "+ TablaUSER.Tabla_Name+
                "("+TablaUSER.Campo_nombre+","+TablaUSER.Campo_password+","+TablaUSER.Campo_ubicacion+")"+
                " values('Alo','12346','2.0000,1.11111')");
        db.execSQL("insert into "+ TablaUSER.Tabla_Name+
                "("+TablaUSER.Campo_nombre+","+TablaUSER.Campo_password+","+TablaUSER.Campo_ubicacion+")"+
                " values('Furro','12347','3.0000,1.11111')");
        db.execSQL("insert into "+ TablaUSER.Tabla_Name+
                "("+TablaUSER.Campo_nombre+","+TablaUSER.Campo_password+","+TablaUSER.Campo_ubicacion+")"+
                " values('Andy','12348','4.0000,1.11111')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
