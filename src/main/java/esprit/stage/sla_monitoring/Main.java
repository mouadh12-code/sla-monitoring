package esprit.stage.sla_monitoring;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import esprit.stage.agents.SLAConfig;
import java.net.ServerSocket;

public class Main {

    private static int findFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (Exception e) {
            return 2000;
        }
    }

    public static void main(String[] args) {
        try {
            SLAConfig.printSLA();

            int freePort = findFreePort();
            System.out.println("=== Port JADE : " + freePort + " ===");

            Runtime rt = Runtime.instance();
            Profile p = new ProfileImpl();
            p.setParameter(Profile.GUI, "true");
            p.setParameter(Profile.LOCAL_HOST, "localhost");
            p.setParameter(Profile.LOCAL_PORT, String.valueOf(freePort));

            AgentContainer container = rt.createMainContainer(p);

            AgentController gAgent = container.createNewAgent(
                "GAgent", "esprit.stage.agents.GasAgent", null);
            gAgent.start();

            AgentController rAgent = container.createNewAgent(
                "RAgent", "esprit.stage.agents.ResponseTimeAgent", null);
            rAgent.start();

            AgentController pAgent = container.createNewAgent(
                "PAgent", "esprit.stage.agents.PrincipalAgent", null);
            pAgent.start();

            System.out.println("=== Plateforme JADE démarrée ===");
            System.out.println("=== GAgent, RAgent, PAgent lancés ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}