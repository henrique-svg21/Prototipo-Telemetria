# Prototipo-Telemetria

# 🚀 Telemetria de Motores Elétricos - IoT & Java

Sistema de monitoramento industrial inteligente para coleta e análise de dados de motores elétricos em tempo real. Este projeto integra hardware (ESP32) e software (Java) utilizando o protocolo **MQTT** para garantir uma comunicação leve e eficiente.

## 📋 Sobre o Projeto

O protótipo foi desenvolvido para monitorar três parâmetros críticos de um motor:
1. **Temperatura** (via sensor DHT22)
2. **Vibração** (simulada via entrada analógica/potenciômetro)
3. **Corrente Elétrica** (simulada via entrada analógica/potenciômetro)

Os dados são processados no "Edge" (ESP32), enviados para um Broker MQTT e consumidos por uma aplicação Java que atua como centro de monitoramento.

## 🛠️ Tecnologias e Ferramentas

* **Linguagem C++:** Firmware do microcontrolador ESP32.
* **Java (JDK 17):** Backend para recepção e tratamento de dados.
* **Maven:** Gestão de dependências Java.
* **MQTT (Protocolo):** Comunicação de baixo consumo de banda.
* **HiveMQ:** Broker público utilizado para a mensageria.
* **Wokwi:** Simulador de hardware.

## 🏗️ Arquitetura do Sistema

* **Firmware (Edge):** Realiza a leitura dos sensores, trata os valores analógicos (ADC 12 bits) para escalas reais e publica uma `String` formatada (ex: `25.5,10,15`).
* **Broker (Nuvem):** Ponte de comunicação (`broker.hivemq.com`).
* **Java Client (Backend):** Realiza o *subscribe* no tópico, processa o payload e exibe as informações com log de sucesso.

## 🚀 Como Executar

### 1. Simulação do Hardware (Wokwi)
1.  Abra o arquivo `diagram.json` no simulador Wokwi.
2.  Certifique-se de que o arquivo `libraries.txt` contém:
    ```text
    DHT sensor library
    PubSubClient
    LiquidCrystal I2C
    ```
3.  Inicie a simulação para começar a transmitir os dados.

### 2. Execução do Backend (Java)
No terminal da sua IDE (VS Code/IntelliJ), dentro da pasta do projeto, execute:
```bash
mvn exec:java -Dexec.mainClass="senai.henrique.motor.dados.App"
