package esprit.stage.agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import javax.swing.*;
import java.awt.*;

public class PrincipalAgent extends Agent {

    private JTextArea textAreaLog;
    private JLabel    labelResult;

    @Override
    protected void setup() {
        System.out.println("=== Agent Principal démarré : " + getAID().getName() + " ===");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Agent Principal — Surveillance SLA ("
                + SLAConfig.CONTRACT_LIST.size() + " Contrats)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(650, 550);
            frame.setLayout(new BorderLayout(5, 5));

            // Panel contrôles
            JPanel topPanel = new JPanel(new GridLayout(3, 2, 5, 5));

            JLabel labelQos  = new JLabel("  Type QoS :");
            String[] options = {"Gas_Consuption", "Response_Time", "Les deux"};
            JComboBox<String> cbQos = new JComboBox<>(options);

            JButton btnSurveiller = new JButton("▶ Surveiller tous les contrats");
            btnSurveiller.setBackground(new Color(0, 150, 0));
            btnSurveiller.setForeground(Color.WHITE);

            JButton btnReset = new JButton("↺ Reset");
            btnReset.setBackground(new Color(200, 50, 50));
            btnReset.setForeground(Color.WHITE);

            labelResult = new JLabel("  En attente...");

            topPanel.add(labelQos);
            topPanel.add(cbQos);
            topPanel.add(btnSurveiller);
            topPanel.add(btnReset);
            topPanel.add(new JLabel("  Statut :"));
            topPanel.add(labelResult);

            // Log console
            textAreaLog = new JTextArea();
            textAreaLog.setEditable(false);
            textAreaLog.setBackground(Color.BLACK);
            textAreaLog.setForeground(Color.GREEN);
            textAreaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textAreaLog);
            scrollPane.setBorder(BorderFactory.createTitledBorder("📋 Log de surveillance"));

            btnSurveiller.addActionListener(e -> {
                String qosType = (String) cbQos.getSelectedItem();
                labelResult.setText("  Surveillance de "
                    + SLAConfig.CONTRACT_LIST.size() + " contrats...");
                textAreaLog.append("=== Démarrage — " + qosType + " ===\n");
                addBehaviour(new SendToAllBehaviour(qosType));
            });

            btnReset.addActionListener(e -> {
                textAreaLog.setText("");
                labelResult.setText("  Reset effectué.");
            });

            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });

        // Recevoir résultats de GAgent/RAgent
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage response = myAgent.receive(mt);

                if (response != null) {
                    String content = response.getContent();
                    String sender  = response.getSender().getLocalName();
                    System.out.println("[PAgent] ◀── " + sender + " : " + content);

                    SwingUtilities.invokeLater(() -> {
                        if (textAreaLog != null) {
                            // CRITICAL → pénalité
                            if (content.contains("CRITICAL")) {
                                textAreaLog.append("🚨 PÉNALITÉ → [" + sender + "] "
                                    + content + "\n");
                            // WARNING → avertissement
                            } else if (content.contains("WARNING")) {
                                textAreaLog.append("⚠️  [" + sender + "] "
                                    + content + "\n");
                            // OK → conforme
                            } else {
                                textAreaLog.append("✅ [" + sender + "] "
                                    + content + "\n");
                            }
                            textAreaLog.setCaretPosition(
                                textAreaLog.getDocument().getLength());
                        }
                        if (labelResult != null) {
                            labelResult.setText("  [" + sender + "] "
                                + content.substring(0, Math.min(40, content.length())));
                        }
                    });
                } else {
                    block();
                }
            }
        });
    }

    // Envoyer à tous les contrats de la liste SLAConfig
    private class SendToAllBehaviour extends jade.core.behaviours.OneShotBehaviour {
        private String qosType;

        public SendToAllBehaviour(String qosType) {
            this.qosType = qosType;
        }

        @Override
        public void action() {
            for (ContractInfo contract : SLAConfig.CONTRACT_LIST) {
                String content = contract.serialize();

                if (qosType.equals("Gas_Consuption") || qosType.equals("Les deux")) {
                    ACLMessage msgG = new ACLMessage(ACLMessage.INFORM);
                    msgG.addReceiver(new AID("GAgent", AID.ISLOCALNAME));
                    msgG.setContent(content);
                    send(msgG);
                    System.out.println("[PAgent] ──▶ GAgent : " + contract.name);
                }

                if (qosType.equals("Response_Time") || qosType.equals("Les deux")) {
                    ACLMessage msgR = new ACLMessage(ACLMessage.INFORM);
                    msgR.addReceiver(new AID("RAgent", AID.ISLOCALNAME));
                    msgR.setContent(content);
                    send(msgR);
                    System.out.println("[PAgent] ──▶ RAgent : " + contract.name);
                }
            }
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("[PAgent] Agent Principal arrêté.");
    }
}