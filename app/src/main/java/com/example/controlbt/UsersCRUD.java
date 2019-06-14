package com.example.controlbt;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.controlbt.Objetos.ObjUsuario;

import com.example.controlbt.UtilidadesSQl.TablaUSER;

import java.util.ArrayList;
import java.util.List;

public class UsersCRUD extends AppCompatActivity {
    private Button btnAdd, btnEdit;
    private EditText etNombre, etContraseña, etContraseña2;
    private ListView lv;
    private TextView lblUserID;
    private ArrayList<ObjUsuario> listUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_crud);

        etNombre = (EditText) findViewById(R.id.et_nombre);
        etContraseña = (EditText) findViewById(R.id.et_contraselaña2);
        etContraseña2 = (EditText) findViewById(R.id.et_contraseña);


        btnAdd = (Button) findViewById(R.id.btn_add);
        btnEdit = (Button) findViewById(R.id.btn_edit);

        lblUserID = (TextView)findViewById(R.id.lblUserID);
        lblUserID.setText("0");
        lv = (ListView) findViewById(R.id.lv);
        Show_Users();
    }


    public void onClick(View v)
        {
            switch (v.getId())
                {
                    case R.id.btn_add:
                        lblUserID.setText("0");
                        etContraseña2.setHint("Confirma Contraseña");
                        addUser();
                        break;
                    case R.id.btn_edit:
                        etContraseña2.setHint("Contraseña Actual");
                        editUser();
                        break;
                }
        }

    private void editUser() {
        try{
            Integer UserID = Integer.valueOf(lblUserID.getText().toString());
            if(UserID > 0){
                if(!etNombre.getText().toString().isEmpty()){
                    String name = etNombre.getText().toString().trim();
                    String password = etContraseña.getText().toString().trim();
                    String oldpassword = etContraseña2.getText().toString().trim();

                     if(TablaUSER.editUsuario(UserID.toString(),name,password,oldpassword,this))
                        Toast.makeText(getApplicationContext(),
                            "Actualizado!",
                            Toast.LENGTH_SHORT).show();
                        else {
                         Toast.makeText(getApplicationContext(),
                                 "ERROR!",
                                 Toast.LENGTH_SHORT).show();
                     }
                    Show_Users();
                    etNombre.setText("");
                    etContraseña.setText("");
                    etContraseña2.setText("");
                    lblUserID.setText("0");
                }
            }else {
                Toast.makeText(this,"Por favor selecione un nombre de la lista.",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(this,"ERROR: "+e.getMessage()+"\nCasuse: "+e.getCause(),Toast.LENGTH_LONG).show();
        }
    }

    //-----------Registrar Usuarios------------//
    public void addUser()
        {
            String nombre = etNombre.getText().toString().trim();
            String contraseña = etContraseña.getText().toString().trim();
            String contraseña2 = etContraseña2.getText().toString().trim();

            if (contraseña.equals(contraseña2) && !nombre.isEmpty())
                {
                    String ResID = TablaUSER.addUsuario(nombre,contraseña,this);
                    Toast.makeText(getApplicationContext(),
                            "ID: "+ResID,
                            Toast.LENGTH_SHORT).show();
                    Show_Users();
                    etNombre.setText("");
                    etContraseña.setText("");
                    etContraseña2.setText("");
                }
            else
                {
                    Toast.makeText(this,"Las Contraseñas son diferentes, o tiene campos sin llenar",Toast.LENGTH_LONG).show();
                }

        }

    private void Show_Edit(String resID) {
        ObjUsuario Select = TablaUSER.GET_User(this,Integer.valueOf(resID));
        if(Select != null){
            etNombre.setText(Select.getName());
            lblUserID.setText(Select.getID().toString());
        }else{
            Toast.makeText(this,"No se logro obtener datos",Toast.LENGTH_LONG).show();
        }
    }

    private void Show_Users()
            {
                listUsers =  TablaUSER.GET_Users(this);
                if(!listUsers.isEmpty()){
                    ArrayList<String> listaInformacion=new ArrayList<String>();

                    for (int i=0; i<listUsers.size();i++){
                        listaInformacion.add("Nombre: "+listUsers.get(i).getName()+"\n "
                                +"Ultima Ubicacion: \n"+listUsers.get(i).getUbicacion());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(this,
                            R.layout.support_simple_spinner_dropdown_item,
                            listaInformacion);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            String ID= String.valueOf(listUsers.get(pos).getID());
                            Show_Edit(ID);
                            Toast.makeText(getApplicationContext(),ID,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),
                            "Usuarios en la base de datos",Toast.LENGTH_SHORT).show();
                }
            }
}

