package com.example.controlbt;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.controlbt.Objetos.ObjUsuario;

import com.example.controlbt.UtilidadesSQl.TablaUSER;

import java.util.ArrayList;

public class UsersCRUD extends AppCompatActivity {
    Button btnAdd, btnEdit;
    EditText etNombre, etContraseña, etContraseña2;
    ListView lv;
    ArrayList<ObjUsuario> ListaUsuarios;
    ArrayAdapter<ObjUsuario> Adapter;
    BaseDeDatos db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_crud);

        etNombre = (EditText) findViewById(R.id.et_nombre);
        etContraseña = (EditText) findViewById(R.id.et_contraselaña2);
        etContraseña2 = (EditText) findViewById(R.id.et_contraseña);


        btnAdd = (Button) findViewById(R.id.btn_add);
        btnEdit = (Button) findViewById(R.id.btn_edit);


        lv = (ListView) findViewById(R.id.lv);
        db = new BaseDeDatos(getApplicationContext());
        consultarListaUsuarios();




    }


    public void onClick(View v)
        {
            switch (v.getId())
                {
                    case R.id.btn_add:
                        addUser();
                        break;
                    case R.id.btn_edit:

                        break;
                }
        }
     //-----------Registrar Usuarios------------//
    public void addUser()
        {
            String nombre = etNombre.getText().toString();
            String contraseña = etContraseña.getText().toString();
            String contraseña2 = etContraseña2.getText().toString();

            if (contraseña.equals(contraseña2))
                {
                    TablaUSER.addUsuario(nombre,contraseña,this);
                }
            else
                {
                    Toast.makeText(this,"Las Contraseñas son diferentes",Toast.LENGTH_LONG).show();
                }

        }
        private void consultarListaUsuarios()
            {
                SQLiteDatabase bd = db.getReadableDatabase();
                ObjUsuario usuario = null;
                ListaUsuarios=new ArrayList<ObjUsuario>();
                Cursor cursor = bd.rawQuery("SELECT * FROM "+TablaUSER.Tabla_Name,null);

                while(cursor.moveToFirst()){
                    ;
                }
            }
}

