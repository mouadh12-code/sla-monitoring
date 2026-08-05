package esprit.stage.agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;
import java.util.Map;

public class ResponseTimeAgent extends Agent {

    private Map<String, Integer> violationsMap = new HashMap<>();

    @Override
    protected void setup() {
        System.out.println("=== RAgent démarré : " + getAID().getName() + " ===");

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = myAgent.receive(mt);

                if (msg != null) {
                    ContractInfo contract = ContractInfo.deserialize(msg.getContent().trim());

                    violationsMap.putIfAbsent(contract.address, contract.nbViolations);

                    System.out.println("\n[RAgent] ◀── SC reçu : " + contract.name);
                    System.out.println("[RAgent]    maxRT = " + contract.maxResponseTime
                        + "ms | violations autorisées = " + contract.nbViolations);

                    String result = measureAndDecide(contract);

                    ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                    reply.addReceiver(new AID("PAgent", AID.ISLOCALNAME));
                    int remaining = violationsMap.get(contract.address);
                    reply.setContent("RT:" + result + " | " + contract.name
                        + " | Violations restantes: " + remaining);
                    send(reply);
                    System.out.println("[RAgent] ──▶ Résultat envoyé à PAgent : " + result);

                } else {
                    block();
                }
            }
        });
    }

    private String measureAndDecide(ContractInfo contract) {
        long start = System.currentTimeMillis();
        try { Thread.sleep(30); } catch (InterruptedException e) {}
        long end = System.currentTimeMillis();
        float responseTime = end - start;

        System.out.println("[RAgent] Temps mesuré : " + responseTime
            + "ms | Max autorisé : " + contract.maxResponseTime + "ms");

        String decision = DecisionEngine.analyzeResponseTime(
            responseTime, contract.maxResponseTime);

        int remaining = violationsMap.get(contract.address);

        if (decision.equals("OK")) {
            DecisionEngine.applyDecision(decision, "RAgent", contract, remaining);

        } else {
            remaining--;
            violationsMap.put(contract.address, remaining);

            if (remaining <= 0) {
                decision = "CRITICAL";
                violationsMap.put(contract.address, contract.nbViolations);
            }

            DecisionEngine.applyDecision(decision, "RAgent", contract, remaining);
        }

        return decision;
    }

    @Override
    protected void takeDown() {
        System.out.println("[RAgent] Agent Temps de Réponse arrêté.");
    }
}