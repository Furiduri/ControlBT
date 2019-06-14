package com.example.controlbt.UtilidadesSQl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.controlbt.BaseDeDatos;
import com.example.controlbt.Objetos.ObjUsuario;
import com.example.controlbt.Ubicacion;
import com.example.controlbt.map;

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

     //Obtener Usuarios
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

    //Login Check User
    public static Integer Login(Context context, String User,String Password){
        //Creamos nuestra conexion
        BaseDeDatos conn = new BaseDeDatos(context);
        SQLiteDatabase db=conn.getWritableDatabase();
        try {
            //Bucamos el usuario
            Cursor cursor = db.rawQuery(" SELECT "+
                            CAMPO_PK_GPS+","+Campo_nombre+","+Campo_ubicacion+
                            " FROM "+Tabla_Name+
                    " WHERE "+Campo_nombre+" LIKE '"+User+"' AND "+Campo_password+" LIKE '"+Password+"'"
                    , null);
            //Si existen datos los aguardamos en un Array List
            cursor.moveToFirst();
            Integer ID  = -1;
                if(!cursor.getString(0).isEmpty()){

                ID = cursor.getInt(0);
                    cursor.close();
                return ID;
            }else{
                cursor.close();
                return ID;
            }
        }catch (Exception e){
            //Si no hay datos o ocurre un error se vacia el array, para evitar enviar datos corruptos
            return -1;
        }
    }

    //Get details by ID User
    public static ObjUsuario GET_User(Context context, Integer ID){
        //Creamos nuestra conexion
        BaseDeDatos conn = new BaseDeDatos(context);
        SQLiteDatabase db=conn.getWritableDatabase();
        try {
            //Bucamos el usuario
            Cursor cursor = db.rawQuery(" SELECT "+
                            CAMPO_PK_GPS+","+Campo_nombre+","+Campo_ubicacion+
                            " FROM "+Tabla_Name+
                            " WHERE "+CAMPO_PK_GPS+" = "+ID
                    , null);
            //Si existen datos los aguardamos en un Array List
            cursor.moveToFirst();
            ObjUsuario objUser = new ObjUsuario(
                    Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2));
            cursor.close();
            return objUser;
        }catch (Exception e){
            //Si no hay datos o ocurre un error se vacia el array, para evitar enviar datos corruptos
            return new ObjUsuario(-1,"","");
        }
    }

    //Edita la Ubicacion
    public static void UPDATE_Ubicacion(String ID, String Ubicacion, Context context){

        //Creamos nuestra conexion
        BaseDeDatos conn = new BaseDeDatos(context);
        SQLiteDatabase db=conn.getWritableDatabase();

        //Metodo insert para insertar datos en la tabla Words
        try{

            //Actualiza la Ubicacion
            //Parametros
            if(Integer.valueOf(ID) > 0) {
                ContentValues Values = new ContentValues();
                Values.put(Campo_ubicacion, Ubicacion);
                db.update(Tabla_Name, Values, CAMPO_PK_GPS + "=" + ID, null);
            }
        }catch (Exception e){
            Toast.makeText(context,"ERROR: "+e.getMessage()+"\nCasuse: "+e.getCause(),Toast.LENGTH_LONG).show();
        }
    }

    //Get Ubication
    public static map GET_Ubicacion(Context context, String ID){
        //Creamos nuestra conexion
        BaseDeDatos conn = new BaseDeDatos(context);
        SQLiteDatabase db=conn.getWritableDatabase();
        try {
            map condenadas = null;
            //Bucamos el usuario
            Cursor cursor = db.rawQuery(" SELECT "+Campo_ubicacion+
                            " FROM "+Tabla_Name+
                            " WHERE "+CAMPO_PK_GPS+" = "+ID
                    , null);
            //Si existen datos los aguardamos en un Array List
            cursor.moveToFirst();
            String cordenS = cursor.getString(0);
            cursor.close();
            if(!cordenS.isEmpty())
                condenadas = new map(cordenS);

            return condenadas;
        }catch (Exception e){
            //Si no hay datos o ocurre un error se vacia el array, para evitar enviar datos corruptos
            return null;
        }
    }

    //Agregar Nuevas palabras
    public static String addUsuario(String nombre, String contraseña, Context context){
        String ResID = "0";
        //Creamos nuestra conexion
        BaseDeDatos conn = new BaseDeDatos(context);
        SQLiteDatabase db=conn.getWritableDatabase();
        //Parametros
        ContentValues Values = new ContentValues();
        Values.put(Campo_nombre,nombre.toUpperCase());
        Values.put(Campo_password,contraseña);
        //Metodo insert para insertar datos en la tabla Words
        try{
            //Busca primero que no exista la palabra
            Cursor cursor = db.rawQuery(" SELECT "+CAMPO_PK_GPS+
                            " FROM "+Tabla_Name+" WHERE "+Campo_nombre+" LIKE '"+nombre+"' "
                    , null);
            //Si existe retorna el ID y el nivel de la palabra
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                ResID = cursor.getString(0) + " (YA EXISTE!)";
                cursor.close();
            }else{
                cursor.close();
                //si no existe la agrega
                db.execSQL("insert into "+ TablaUSER.Tabla_Name+
                        "("+TablaUSER.Campo_nombre+","+TablaUSER.Campo_password+","+TablaUSER.Campo_ubicacion+")"+
                        " values('"+nombre+"','"+contraseña+"','0,0')");
                cursor = db.rawQuery(" SELECT "+CAMPO_PK_GPS+
                                " FROM "+Tabla_Name+" WHERE "+Campo_nombre+" LIKE '"+nombre+"' "
                        , null);
                if(cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    ResID = cursor.getString(0) + " (Creado!)";
                    cursor.close();
                }else{
                   return  "Error al crear!";
                }
                db.close();
                //Retorno del ID y la comprobacion de los datos ingresados

            }
        }catch (Exception e){
            return ResID + "\n"+e.getMessage()+"\nCause: "+e.getCause();
        }
        return ResID + ", El nombre: "+nombre;
    }

    //Edita la Ubicacion
    public static boolean editUsuario(String ID, String nombre, String newContra, String oldContra, Context context)
        {

            //Creamos nuestra conexion
            BaseDeDatos conn = new BaseDeDatos(context);
            SQLiteDatabase db=conn.getWritableDatabase();

            //Metodo insert para insertar datos en la tabla Words
            try
                {
                    //Bucamos el usuario
                    Cursor cursor = db.rawQuery(" SELECT "+
                                    CAMPO_PK_GPS+
                                    " FROM "+Tabla_Name+
                                    " WHERE "+CAMPO_PK_GPS+" = "+ID+" AND "+Campo_password+" LIKE '"+oldContra+"'"
                            , null);
                    //Si existen datos los aguardamos en un Array List
                    boolean isValid = false;
                    if(cursor.getCount() > 0)
                        isValid = true;



                //Actualiza el usuario
                //Parametros
                if(isValid)
                    {
                        ContentValues Values = new ContentValues();
                        Values.put(Campo_nombre, nombre);
                        Values.put(Campo_password, newContra);
                        db.update(Tabla_Name, Values, CAMPO_PK_GPS + "=" + ID, null);
                        db.close();
                        return true;
                    }else{
                    db.close();
                    return false;
                }

                }catch (Exception e)
                    {
                    Toast.makeText(context,"ERROR: "+e.getMessage()+"\nCasuse: "+e.getCause(),Toast.LENGTH_LONG).show();
                        db.close();
                        return false;
                    }

        }
}
