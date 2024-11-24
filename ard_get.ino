#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include <ESP32Servo.h>
#include <SPI.h>
#include <MFRC522.h>
#include <DHT.h>

#define SS_PIN 5 
#define RST_PIN 27 
#define SS_PIN 5
#define RST_PIN 27 
#define redOne 14
#define greenOne 12
#define blueOne 13
#define serv 2
#define DHTPIN 22
#define DHTTYPE DHT11
#define POWER_PIN 21
#define SIGNAL_PIN 34
#define EXE_INTERVAL 5000

unsigned long lastExecutedMillis = 0;
const char* ssid = "FM";
const char* password = "pashaejojo123";
byte keyTagUID[4] = {0x1C, 0xD9, 0x8D, 0x64};

MFRC522 rfid(SS_PIN, RST_PIN);
Servo myservo;
WiFiServer server(80);
DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, password);
  pinMode(redOne, OUTPUT);
  pinMode(greenOne, OUTPUT);
  pinMode(blueOne, OUTPUT);
  myservo.attach(serv);
  pinMode(POWER_PIN, OUTPUT);
  digitalWrite(POWER_PIN, LOW);
  
  dht.begin();
  SPI.begin(); // init SPI bus
  rfid.PCD_Init(); // init MFRC522

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  doorGET();
  delay(1000);
  lampGET();
  delay(1000);
  tempPOST();
  delay(1000);
  waterPOST();
  delay(1000);
}

void doorGET() {
  unsigned long currentMillis = millis();
  HTTPClient http;
  WiFiClient client;
  int door, card, i = 0, j = 0;

  http.begin(client, "http://192.168.1.7:5000/doorGET");
  int httpCode = http.GET();

  if (rfid.PICC_IsNewCardPresent()) { // new tag is available
    if (rfid.PICC_ReadCardSerial()) { // NUID has been readed
      MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);

      if (rfid.uid.uidByte[0] == keyTagUID[0] &&
          rfid.uid.uidByte[1] == keyTagUID[1] &&
          rfid.uid.uidByte[2] == keyTagUID[2] &&
          rfid.uid.uidByte[3] == keyTagUID[3] ) {
          Serial.println("Access is granted");
          myservo.write(180);
          door = 1;
          card = 1;
          if (currentMillis - lastExecutedMillis >= EXE_INTERVAL) {
              lastExecutedMillis = currentMillis;
              door = 0;
              card = 0;
          }
      }
      else
      {
        Serial.print("Access denied, UID:");
        for (int i = 0; i < rfid.uid.size; i++) {
          Serial.print(rfid.uid.uidByte[i] < 0x10 ? " 0" : " ");
          Serial.print(rfid.uid.uidByte[i], HEX);
          myservo.write(0);
        }
      }

      rfid.PICC_HaltA(); // halt PICC
      rfid.PCD_StopCrypto1(); // stop encryption on PCD
    }
  }
  
  if (httpCode > 0) {
    String response = http.getString();

    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, response);
    
    if (error) {
        Serial.println("Error parsing JSON");
    } else {
        door = doc["doorValue"][0];
        Serial.println("door : " + String(door));
        
        if (door == 1 || card == 1) {
            if (currentMillis - lastExecutedMillis >= EXE_INTERVAL) {
              lastExecutedMillis = currentMillis;
              i++;
              if(i <= 1){
                myservo.write(180);
                Serial.println("Open");
                card = 0;
              }
            }
        }
        if (door == 0 || card == 0) {
            if (currentMillis - lastExecutedMillis >= EXE_INTERVAL) {
              lastExecutedMillis = currentMillis;
              j++;
              if(j <= 1){
                myservo.write(0);
                Serial.println("Closed");
              }
            }
        }
    }
  } else {
  Serial.println("Error on HTTP request");
  }
  Serial.println("card : ");
  Serial.print(card);
  http.end();
}

void lampGET(){
  HTTPClient http;
  WiFiClient client;

  int r,g,b,intensity;

  // send the GET request
  http.begin(client, "http://192.168.1.7:5000/lampMenuGET");
  int httpCode = http.GET();

   if (httpCode > 0) {
    String response = http.getString();

    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, response);

    if (error) {
      Serial.println("Error parsing JSON");
    } else {
      r = doc["r"][0];
      g = doc["g"][0];
      b = doc["b"][0];
      intensity = doc["intensity"][0];

      Serial.println("r: " + String(r));
      Serial.println("g: " + String(g));
      Serial.println("b: " + String(b));
      Serial.println("intensity: " + String(intensity));

      analogWrite(redOne, r*intensity/4);
      analogWrite(greenOne, g*intensity/4);
      analogWrite(blueOne, b*intensity/4);
    }
  } else {
    Serial.println("Error on HTTP request");
  }  
  http.end();
}

void tempPOST(){
  HTTPClient http;
  WiFiClient client;

  float fTemperature = dht.readTemperature();
  int temperature = int(fTemperature);
  Serial.println("Temp" + temperature);
  
  http.begin(client, "http://192.168.1.7:5000/tempPOST");  
  http.addHeader("Content-Type", "application/json");
  String payload = "{\"temp_C\":" + String(temperature) + "}";
  int httpResponseCode = http.POST(payload);

  if (httpResponseCode == 200) {
    Serial.println("Temperature value sent to server successfully");
  } else {
    Serial.println("Failed to send temperature value to server, HTTP response code: " + httpResponseCode);
  }
  
  http.end();
}

void waterPOST(){
  HTTPClient http;
  WiFiClient client;

  int value;

  digitalWrite(POWER_PIN, HIGH);
  delay(10); 
  value = analogRead(SIGNAL_PIN);
  digitalWrite(POWER_PIN, LOW);
  
  Serial.print("Water : ");
  Serial.println(value);
  
  http.begin(client, "http://192.168.1.7:5000/waterPOST");  
  http.addHeader("Content-Type", "application/json");
  String payload = "{\"water_V\":" + String(value) + "}";
  int httpResponseCode = http.POST(payload);

  if (httpResponseCode == 200) {
    Serial.println("Water value sent to server successfully");
  } else {
    Serial.println("Failed to send temperature value to server, HTTP response code: " + httpResponseCode);
  }
  
  http.end();
}
