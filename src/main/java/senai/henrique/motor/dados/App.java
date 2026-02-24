/*comando
mvn exec:java -Dexec.mainClass="senai.henrique.motor.dados.App" */

package senai.henrique.motor.dados; 

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class App {
    public static void main(String[] args) {
        // Configurações extraídas do Manual de Referência Técnica (Anexo 1)
        String broker = "tcp://broker.hivemq.com:1883"; 
        String clientId = "JavaClient_Henrique_" + System.currentTimeMillis(); // ID exclusivo (C7)
        String topic = "senai/henrique/motor/dados"; // Tópico padronizado

        try {
            MqttClient client = new MqttClient(broker, clientId);
            client.connect();
            System.out.println("Conectado ao Broker! Aguardando telemetria do motor...");

            // Inscrição para receber a String simples: "temp,vibra,corrente"
            client.subscribe(topic, (t, msg) -> {
                String payload = new String(msg.getPayload());
                
                // Formato de saída obrigatório conforme o Desafio (C11)
                System.out.println("Dados de Telemetria Coletados com Sucesso: [" + payload + "]");
            });

        } catch (MqttException e) {
            System.err.println("Erro na conexão MQTT: " + e.getMessage());
        }
    }
}