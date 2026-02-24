#include <WiFi.h>
#include <PubSubClient.h>
#include <DHT.h>
#include <LiquidCrystal_I2C.h>

//CONFIGURAÇÕES DE REDE E MQTT 
const char* ssid = "Wokwi-GUEST";  //nome da rede gratis
const char* password = ""; //sem senha
const char* mqtt_server = "broker.hivemq.com"; //broker
const char* topic_telemetria = "senai/henrique/motor/dados"; //telemetria padronizada

//DEFINIÇÃO DE PINOS 
#define PIN_DHT 15 
#define PIN_VIBRA 34
#define PIN_CORRENTE 35
#define PIN_LED 12

// INICIALIZAÇÃO DE OBJETOS
DHT dht(PIN_DHT, DHT22);
LiquidCrystal_I2C lcd(0x27, 16, 2);
WiFiClient espClient;
PubSubClient client(espClient);

void setup_wifi() {
  Serial.print("Conectando ao Wi-Fi...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWi-Fi Conectado!");
}

void reconnect() { //FUNÇÃO PRA CONECTAR O CLIENTE CASO NAO ESTEJA
  while (!client.connected()) {
    Serial.print("Tentando conexão MQTT...");
    if (client.connect("ESP32_Motor_Client")) {
      Serial.println("Conectado!");
    } else {
      delay(5000);
    }
  }
}

void setup() {
  pinMode(PIN_LED, OUTPUT);
  Serial.begin(115200);
  dht.begin();
  lcd.init();
  lcd.backlight();
  setup_wifi();
  client.setServer(mqtt_server, 1883);
}

void loop() { //FUNÇÃO PRA CONECTAR O CLIENTE CASO DESCONECTE
  if (!client.connected()) reconnect();
  client.loop();

  // 1. LEITURA DOS SENSORES
  float temp = dht.readTemperature();
  // Mapeia os valores analógicos (0-4095) para escalas didáticas
  int vibra = map(analogRead(PIN_VIBRA), 0, 4095, 0, 100); 
  int corrente = map(analogRead(PIN_CORRENTE), 0, 4095, 0, 30); // Simula 0-30 Amperes

  // 2. EXIBIÇÃO LOCAL (LCD)
  lcd.setCursor(0, 0);
  lcd.print("T:"); lcd.print(temp); lcd.print(" V:"); lcd.print(vibra);
  lcd.setCursor(0, 1);
  lcd.print("Corrente: "); lcd.print(corrente); lcd.print("A");

  // 3. ENVIO DOS DADOS (MQTT)
  digitalWrite(PIN_LED, HIGH); // Liga ao iniciar envio [cite: 442]
  
  // Formato: "temp,vibra,corrente" [cite: 460]
  String payload = String(temp) + "," + String(vibra) + "," + String(corrente);
  
  Serial.print("Enviando: ");
  Serial.println(payload);
  client.publish(topic_telemetria, payload.c_str());
  
  delay(500); // Breve pausa para o LED ser visível
  digitalWrite(PIN_LED, LOW);

  delay(2000); // Aguarda 2 segundos para a próxima leitura
}