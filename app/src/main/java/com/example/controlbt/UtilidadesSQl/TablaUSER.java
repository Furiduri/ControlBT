package com.example.elgepeese.UtilidadesSQl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.elgepeese.BaseDeDatos;
import com.example.elgepeese.Objetos.ObjUsuario;

import java.util.ArrayList;

public class TablaUSER {

     public static String Tabla_Name = "GPS";
     public static String CAMPO_PK_GPS = "id_GPS";
     public static String Campo_password = "contraseña";
     public static String Campo_ubicacion = "ubicacion";
     public static String Campo_nombre = "nombre";


     //Tabla GPS
     static final public String UP_CREAR_TABLA_USER="CREATE TABLE "+Tabla_Name+" ("
             + CAMPO_PK_GPS +" INTEGER PRIMARY KEY, "
             +Campo_nombre+" TEXT,"
             + Campo_password +" TEXT, "
             +Campo_ubicacion+" TEXT )";

     //Obtener Palabras
     public static ArrayList<ObjUsuario> GET_Users(Context context){
         ArrayList<ObjUsuario> usuarioArrayList = new ArrayList<ObjUsuario>();
         //Creamos nuestra conexion
         BaseDeDatos conn = new BaseDeDatos(context);
         SQLiteDatabase db=conn.getWritableDatabase();
         try {
             //Obtenemos los datos
             Cursor cursor = db.rawQuery(" SELECT "+
                             CAMPO_PK_GPS+","+Campo_nombre+","+Campo_ubicacion+
                             " FROM "+Tabla_Name
                     , null);
             //Si existen datos los aguardamos en un Array List
             cursor.moveToFirst();
             while (!cursor.isAfterLast()){
                 usuarioArrayList.add(new ObjUsuario(
                         Integer.valueOf(cursor.getString(0)),
                         cursor.getString(1),
                         cursor.getString(2)));
                 cursor.moveToNext();
             }
             cursor.close();
         }catch (Exception e){
             //Si no hay datos o ocurre un error se vacia el array, para evitar enviar datos corruptos
             usuarioArrayList.clear();
         }
         return usuarioArrayList;
     }

 }
