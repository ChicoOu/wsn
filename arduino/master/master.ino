#include <dht11.h>
#include <SoftwareSerial.h>

dht11 DHT11;

#define DHT11PIN 2
char val = 'O';

SoftwareSerial BT(8,9);

void setup()
{
  Serial.begin(9600);
  BT.begin(9600);
}

void loop()
{
  //Serial.println("\n");

  if( Serial.available() ){
    val = Serial.read();
    Serial.print(val);
    BT.print(val);
  }

  if( BT.available() ){
    val = BT.read();
    Serial.print(val);
  }
  delay(2000);
}
