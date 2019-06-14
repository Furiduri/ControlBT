package com.example.controlbt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controlbt.Objetos.ObjUsuario;
import com.example.controlbt.UtilidadesSQl.TablaUSER;

public class Login extends AppCompatActivity {
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button)findViewById(R.id.btnLogin);
    }

    public void btnLogin_Onclik(View view){
        try {
            EditText txtUser = (EditText)findViewById(R.id.txtUser);
            EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
            String User = txtUser.getText().toString();
            String Password = txtPassword.getText().toString();
            if(!User.isEmpty() && !Password.isEmpty()){
                ObjUsuario objUser = TablaUSER.Login(this,User,Password);
                if(objUser.getID() > -1){
                    Toast.makeText(this,"Bienvenido "+objUser.getName(),Toast.LENGTH_SHORT).show();
                    Intent nextView = new Intent(this,DispositivosBT.class);
                    nextView.putExtra("ID",objUser.getID());
                    startActivity(nextView);
                    finish();
                    return;
                }else{
                    Toast.makeText(this,"Usuario o Contraseña incorrectos!!",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Favor de llenar los todos los campos",Toast.LENGTH_SHORT).show();
            }
        }catch ( Exception e){
            Toast.makeText(this,"ERORR: "+e.getMessage()+"\nCasuse: "+e.getCause(),Toast.LENGTH_SHORT).show();
        }
    }
}
