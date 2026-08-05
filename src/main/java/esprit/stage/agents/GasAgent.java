package esprit.stage.agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;
import java.util.Map;

public class GasAgent extends Agent {

    // Violations restantes par contrat — dynamique
    private Map<String, Integer> violationsMap = new HashMap<>();

    @Override
    protected void setup() {
        System.out.println("=== GAgent démarré : " + getAID().getName() + " ===");

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = myAgent.receive(mt);

                if (msg != null) {
                    // Désérialiser les infos du contrat
                    ContractInfo contract = ContractInfo.deserialize(msg.getContent().trim());

                    // Initialiser le compteur si nouveau contrat
                    violationsMap.putIfAbsent(contract.address, contract.nbViolations);

                    System.out.println("\n[GAgent] ◀── SC reçu : " + contract.name);
                    System.out.println("[GAgent]    maxGas = " + contract.maxGas
                        + " | violations autorisées = " + contract.nbViolations);

                    // Mesurer et décider
                    String result = measureAndDecide(contract);

                    // Répondre à PAgent
                    ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                    reply.addReceiver(new AID("PAgent", AID.ISLOCALNAME));
                    int remaining = violationsMap.get(contract.address);
                    reply.setContent("GAS:" + result + " | " + contract.name
                        + " | Violations restantes: " + remaining);
                    send(reply);
                    System.out.println("[GAgent] ──▶ Résultat envoyé à PAgent : " + result);

                } else {
                    block();
                }
            }
        });
    }

    private String measureAndDecide(ContractInfo contract) {
        // Simuler gas consommé
        float gasConsumed = 98000.0f + (contract.address.hashCode() % 20000);
        System.out.println("[GAgent] Gas mesuré : " + gasConsumed
            + " | Max autorisé : " + contract.maxGas);

        // Analyser via moteur IA NGLS
        String decision = DecisionEngine.analyzeGas(gasConsumed, contract.maxGas);

        int remaining = violationsMap.get(contract.address);

        if (decision.equals("OK")) {
            DecisionEngine.applyDecision(decision, "GAgent", contract, remaining);

        } else {
            remaining--;
            violationsMap.put(contract.address, remaining);

            if (remaining <= 0) {
                // Violations épuisées → CRITICAL → pénalité
                decision = "CRITICAL";
                violationsMap.put(contract.address, contract.nbViolations);
            }

            DecisionEngine.applyDecision(decision, "GAgent", contract, remaining);
        }

        return decision;
    }

    @Override
    protected void takeDown() {
        System.out.println("[GAgent] Agent Gas arrêté.");
    }
}