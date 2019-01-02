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
  }

  if( val == 'C' ){
    int chk = DHT11.read(DHT11PIN);
  
    //Serial.print("Read sensor: ");
    switch (chk)
    {
      case DHTLIB_OK: 
                  //Serial.println("OK"); 
                  break;
      case DHTLIB_ERROR_CHECKSUM: 
                  Serial.println("Checksum error"); 
                  break;
      case DHTLIB_ERROR_TIMEOUT: 
                  Serial.println("Time out error"); 
                  break;
      default: 
                  Serial.println("Unknown error"); 
                  break;
    }
    
    //Serial.print("Humidity (%): ");
    BT.print((int)DHT11.humidity);
  
    BT.print(",");
    //Serial.print("Temperature (oC): ");
    BT.println((int)DHT11.temperature);
  }
  delay(2000);
}
