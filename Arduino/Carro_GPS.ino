#include<SoftwareSerial.h>
#include<TinyGPS.h>
// Motor A
char accion; 
int IN1 = 9;
int IN2 = 10;

// Motor B

int IN3 = 7;
int IN4 = 6;

TinyGPS gps;
SoftwareSerial sgps(4, 3);

void setup ()
{
 // Declaramos todos los pines como salidas

 pinMode (IN1, OUTPUT);
 pinMode (IN2, OUTPUT);
 pinMode (IN3, OUTPUT);
 pinMode (IN4, OUTPUT);
 Serial.begin(9600);
 sgps.begin(9600);
}
void Adelante ()
{
 //Direccion motor A
 digitalWrite (IN1, HIGH);
 digitalWrite (IN2, LOW);

 //Direccion motor B
 digitalWrite (IN3, LOW);
 digitalWrite (IN4, HIGH);

 
}
void Atras ()
{
 //Direccion motor A
 digitalWrite (IN1, LOW);
 digitalWrite (IN2, HIGH);
 //Direccion motor B
 digitalWrite (IN3, HIGH);
 digitalWrite (IN4, LOW);
Serial.print("ATRAS");
Serial.print("#");
}
void Derecha ()
{
 //Direccion motor A
 digitalWrite (IN1, LOW);
 digitalWrite (IN2, LOW);
 //Direccion motor B
 digitalWrite (IN3, LOW);
 digitalWrite (IN4, HIGH);
Serial.print("DERECHA");
Serial.print("#");
}

void Izquierda ()
{
 //Direccion motor A
 digitalWrite (IN1, HIGH);
 digitalWrite (IN2, LOW);
 //Direccion motor B
 digitalWrite (IN3, LOW);
 digitalWrite (IN4, LOW);
Serial.print("IZQUIERDA");
Serial.print("#");
}
void Parar ()
{
 //Direccion motor A
 digitalWrite (IN1, LOW);
 digitalWrite (IN2, LOW);
 //Direccion motor B
 digitalWrite (IN3, LOW);
 digitalWrite (IN4, LOW);
Serial.print("PARAR");
Serial.print("#");
}

void GPS(){
   bool newData = false;
unsigned long chars;
unsigned short sentences, failed;

  if (sgps.available())

  {
    while (sgps.available())
    {

      char c = sgps.read();
      
      if (gps.encode(c)) // Monitorea si un nuevo valor ha entrado al GPS

        newData = true;

    }
    float flat, flon;

    unsigned long age;

    gps.f_get_position(&flat, &flon, &age);
    //Serial.println();
    Serial.print("LATITUD,LONGITUD=*");
    Serial.print(flon == TinyGPS::GPS_INVALID_F_ANGLE ? 0.0 : flon, 6);//Imprime el valor de la longitud con respecto al oeste.
    Serial.print(",");
    Serial.print(flat == TinyGPS::GPS_INVALID_F_ANGLE ? 0.0 : flat, 6); //Imprime el valor de la latitud con respecto al norte
    Serial.println("&");
    /*Serial.print(" SAT=");

    Serial.print(gps.satellites() == TinyGPS::GPS_INVALID_SATELLITES ? 0 : gps.satellites()); //Imprime el número de satelites a los que se conecto.

    Serial.print(" PREC="); //Imprime la precisión de los datos.

    Serial.print(gps.hdop() == TinyGPS::GPS_INVALID_HDOP ? 0 : gps.hdop());
*/
  }

  //se imprimen los estados de la señal recibida.

  gps.stats(&chars, &sentences, &failed);

  Serial.print(" CHARS=");

  Serial.print(chars);

  Serial.print(" SENTENCIAS=");

  Serial.print(sentences);

  Serial.print(" CSUM ERR=");

  Serial.print(failed);

  if (chars == 0)

    Serial.print("** No se ha recibido información por parte del GPS, verifica tus conexiones **");

  }
  
void loop ()
{
  
  if (Serial.available())
  {
   
    accion = Serial.read();
    switch (accion)
    {
      case '0':
      Parar ();
      delay (4000);
      break;
      case '1':
      Adelante ();
        delay (5000);
        break;
      case '2':
      Derecha ();
      delay (2000);
      break;
      case '3':
       Izquierda ();
 delay (2000);
 break;
      case '4':
 Atras ();
 delay (3000);
    
    }
  }
  GPS();
  delay (2000); 
}
