package com.example.controlbt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.regex.Pattern;

import com.example.controlbt.Objetos.ObjUsuario;
import com.example.controlbt.UtilidadesSQl.TablaUSER;
import com.example.controlbt.map;

public class UserInterfaz extends AppCompatActivity {
Button btnEncender, btnApagar, btnDesconectar, btnDerecha, btnIzquierda, btnReversa, btnMaps;
TextView txtBufferIn;
TextView lblUserActual, lblIDUser, lblUbicacion;

    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN ;
    private ConnectedThread MyConexionBT;
    private ObjUsuario UserLogin;
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interfaz);

        btnEncender =(Button) findViewById(R.id.btnEncender);
        btnApagar =(Button) findViewById(R.id.btnApagar);
        btnDesconectar =(Button) findViewById(R.id.btnDesconectar);
        btnReversa =(Button) findViewById(R.id.btnReversa);
        btnMaps=(Button)findViewById(R.id.btnMaps);
        txtBufferIn=(TextView) findViewById(R.id.txtBufferIn);
        lblUserActual = (TextView)findViewById(R.id.lblUsertLogin);
        lblIDUser = (TextView)findViewById(R.id.lblIDUser);
        lblUbicacion = (TextView)findViewById(R.id.lblUbication);
        btnDerecha=(Button) findViewById(R.id.btnDerecha);
        btnIzquierda=(Button) findViewById(R.id.btnIzquierda);
        //Get Data User
        Integer ID = getIntent().getExtras().getInt("ID");
        if( ID > -1){
             UserLogin = TablaUSER.GET_User(this,ID);
             lblUserActual.setText(
                     "Usuario: "+UserLogin.getName()+" \nUltima Ubicacion: "+UserLogin.getUbicacion()
             );
             lblIDUser.setText(UserLogin.getID().toString());
             lblUbicacion.setText(UserLogin.getUbicacion());
         }
        //Bluetooth
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                try{
                if (!msg.obj.toString().isEmpty()) {
                    String readMessage = (String) msg.obj;
                    DataStringIN = new StringBuilder();
                    DataStringIN.append(readMessage);
                    if(DataStringIN.length() > 15){
                    int startOfLineIndexlat = DataStringIN.indexOf("*");
                    int endOfLineIndexlog = DataStringIN.indexOf("&");
                    //Envia datos a maps
                    if (startOfLineIndexlat > 0 && endOfLineIndexlog > startOfLineIndexlat) {
                        String DataBlue = DataStringIN.substring(0);
                        DataStringIN.delete(0,DataStringIN.length());
                        String Coordenadas = DataBlue.substring(startOfLineIndexlat+1, endOfLineIndexlog);
                        if(validarCoordenada(Coordenadas)&& lblUbicacion.getText().toString() != Coordenadas){
                            lblUbicacion.setText(Coordenadas);
                            txtBufferIn.setText("Dato: " + Coordenadas);//<-<- PARTE A MODIFICAR >->-
                            //TablaUSER.UPDATE_Ubicacion(lblIDUser.getText().toString(),Coordenadas,getParent().getApplicationContext());
                        }
                    }
                }
                }
                }
                catch (Exception e){
                    Toast.makeText(getParent().getApplicationContext(),"ERROR: "+e.getMessage()+"\nCause: "+e.getCause(),Toast.LENGTH_LONG).show();
                }
            }

        };
        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        VerificarEstadoBT();
        btnEncender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("1");
            }
        });
        btnApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("0");
            }
        });
        btnDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("2");
            }
        });
        btnIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("3");
            }
        });
        btnReversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("4");
            }
        });
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map condenadas = new map(lblUbicacion.getText().toString());
                if (condenadas.getLat() != 0 && condenadas.getLog() != 0) {
                    TablaUSER.UPDATE_Ubicacion(lblIDUser.getText().toString(),lblUbicacion.getText().toString(),getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), Ubicacion.class);
                    intent.putExtra("lat", condenadas.getLat());
                    intent.putExtra("log", condenadas.getLog());
                    startActivity(intent);
                }
            }
        });
        btnDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket!=null)
                {
                    try {btSocket.close();}
                    catch (IOException e)
                    { Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();;}
                }
                finish();
            }
        });

    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException
    {
        //crea un conexion de salida segura para el dispositivo
        //usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        //Consigue la direccion MAC desde DeviceListActivity via intent
        Intent intent = getIntent();
        //Consigue la direccion MAC desde DeviceListActivity via EXTRA
        address = intent.getStringExtra(DispositivosBT.EXTRA_DEVICE_ADDRESS);//<-<- PARTE A MODIFICAR >->->
        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try
        {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establece la conexión con el socket Bluetooth.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {}
        }
        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        try
        { // Cuando se sale de la aplicación esta parte permite
            // que no se deje abierto el socket
            btSocket.close();
        } catch (IOException e2) {}
    }

    //Comprueba que el dispositivo Bluetooth Bluetooth está disponible y solicita que se active si está desactivado
    private void VerificarEstadoBT() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //Crea la clase que permite crear el evento de conexion
    private class ConnectedThread extends Thread
    {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            byte[] buffer = new byte[256];
            int bytes;

            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    // Envia los datos obtenidos hacia el evento via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //Envio de trama
        public void write(String input)
        {
            try {
                mmOutStream.write(input.getBytes());
            }
            catch (IOException e)
            {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    //Validar correcto patron de cordenadas
    public boolean validarCoordenada(String Cordenada){
        String REGEX_NUMEROS = "^[\\-?\\+?0-9]+\\.[\\-?\\+?0-9]+[,][0-9]+\\.[0-9]+$";
        Pattern patron = Pattern.compile(REGEX_NUMEROS);
        if(patron.matcher(Cordenada).matches()){
            return true;
        }else{
            return false;
        }
    }

    //GO to CRUD USERS
    public void btnCRUDUsers(View view){
        Intent intentCRUD = new Intent(getApplicationContext(),UsersCRUD.class);
        startActivity(intentCRUD);
        onPause();
    }
}