/*comando para rodar no terminal:
mvn exec:java -Dexec.mainClass="senai.henrique.motor.dados.App" */

package senai.henrique.motor.dados; 

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence; // Importação adicionada

public class App {
    public static void main(String[] args) {
       
        String broker = "tcp://broker.hivemq.com:1883"; 
        String clientId = "JavaClient_Henrique_" + System.currentTimeMillis(); // inicia sessão nova cada vez que roda
        String topic = "senai/henrique/motor/dados"; // Tópico padronizado conforme o documento

        // Configura o Paho MQTT para usar a memória RAM, evitando a criação daquelas pastas de cache
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            // Passamos o 'persistence' como terceiro parâmetro aqui
            MqttClient client = new MqttClient(broker, clientId, persistence);
            client.connect();
            System.out.println("Conectado ao Broker! Aguardando telemetria do motor...");

            // Inscrição para receber a String simples: "temperatura,vibração,corrente"
            client.subscribe(topic, (t, msg) -> {
                String payload = new String(msg.getPayload());
                
                // Formato de saída 
                System.out.println("Dados de Telemetria Coletados com Sucesso: [" + payload + "]");
            });

        } catch (MqttException e) {
            System.err.println("Erro na conexão MQTT: " + e.getMessage());
        }
    }
}