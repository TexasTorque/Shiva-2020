#include <Wire.h>
#include "Adafruit_TCS34725.h"

// Pick analog outputs, for the UNO these three work well
// use ~560  ohm resistor between Red & Blue, ~1K for green (its brighter)
#define redpin 3
#define greenpin 5
#define bluepin 6
// for a common anode LED, connect the common pin to +5V
// for common cathode, connect the common to ground

// set to false if using a common cathode LED
#define commonAnode true

// our RGB -> eye-recognized gamma color
byte gammatable[256];
String color = "";

Adafruit_TCS34725 tcs = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_50MS, TCS34725_GAIN_4X);

void setup() {
  pinMode(8, OUTPUT);
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  Serial.begin(9600);
  //Serial.println("Color View Test!");

  if (tcs.begin()) {
    //Serial.println("Found sensor");
  } else {
    Serial.println("No TCS34725 found ... check your connections");
    while (1); // halt!
  }

  // use these three pins to drive an LED
#if defined(ARDUINO_ARCH_ESP32)
  ledcAttachPin(redpin, 1);
  ledcSetup(1, 12000, 8);
  ledcAttachPin(greenpin, 2);
  ledcSetup(2, 12000, 8);
  ledcAttachPin(bluepin, 3);
  ledcSetup(3, 12000, 8);
#else
  pinMode(redpin, OUTPUT);
  pinMode(greenpin, OUTPUT);
  pinMode(bluepin, OUTPUT);
#endif

  // thanks PhilB for this gamma table!
  // it helps convert RGB colors to what humans see
  for (int i=0; i<256; i++) {
    float x = i;
    x /= 255;
    x = pow(x, 2.5);
    x *= 255;

    if (commonAnode) {
      gammatable[i] = 255 - x;
    } else {
      gammatable[i] = x;
    }
    //Serial.println(gammatable[i]);
  }
}

void loop() {
  float red, green, blue;
 
  tcs.setInterrupt(false);  // turn on LED

  delay(60);  // takes 50ms to read

  tcs.getRGB(&red, &green, &blue);

  if(red> 90 && green >90){
    color = "Yellow";
    digitalWrite(8,HIGH);
    digitalWrite(9,LOW);
    digitalWrite(10,LOW);  
  }
  else if (red > 150){
    color = "Red";
    digitalWrite(8,LOW);
    digitalWrite(9,HIGH);
    digitalWrite(10,LOW);
  }
  else if (green > 100){
    color = "green";
    digitalWrite(8,HIGH);
    digitalWrite(9,HIGH);
    digitalWrite(10,LOW);
  }
  else if (blue > 100){
    color = "blue";
    digitalWrite(8,LOW);
    digitalWrite(9,LOW);
    digitalWrite(10,HIGH);
  }
  else{
    color = "none";
    digitalWrite(8,LOW);
    digitalWrite(9,LOW);
    digitalWrite(10,LOW);
  }
  
  tcs.setInterrupt(true);  // turn off LED
Serial.print(color);
//  Serial.print("R:\t"); Serial.print(int(red)); 
//  Serial.print("\tG:\t"); Serial.print(int(green)); 
//  Serial.print("\tB:\t"); Serial.print(int(blue));


  Serial.print("\n");


#if defined(ARDUINO_ARCH_ESP32)
  ledcWrite(1, gammatable[(int)red]);
  ledcWrite(2, gammatable[(int)green]);
  ledcWrite(3, gammatable[(int)blue]);
#else
  analogWrite(redpin, gammatable[(int)red]);
  analogWrite(greenpin, gammatable[(int)green]);
  analogWrite(bluepin, gammatable[(int)blue]);
#endif
}
