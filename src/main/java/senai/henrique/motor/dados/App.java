package br.com.senai.automacao;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class App {
    public static void main(String[] args) {
        // Configurações do Broker conforme o manual [cite: 12, 13]
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "JavaClient_SeuNome"; // Substitua pelo seu nome 
        String topic = "senai/seu_nome/motor/dados"; // Use o tópico do Anexo 1 

        try {
            MqttClient client = new MqttClient(broker, clientId);
            client.connect();
            System.out.println("Conectado ao Broker! Aguardando telemetria...");

            // Inscrição no tópico de telemetria 
            client.subscribe(topic, (t, msg) -> {
                String payload = new String(msg.getPayload());
                
                // Exibe a mensagem exatamente como o desafio solicita 
                System.out.println("Dados de Telemetria Coletados com Sucesso: [" + payload + "]");
            });

        } catch (MqttException e) {
            System.err.println("Erro ao conectar ou receber dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}