package esprit.stage.agents;

/**
 * Moteur de décision IA basé sur les règles (NGLS)
 * Next Generation Logic Systems
 *
 * Règles :
 * OK       → service conforme au SLA
 * WARNING  → violation légère → surveillance renforcée
 * CRITICAL → violation grave  → pénalité appliquée automatiquement
 */
public class DecisionEngine {

    // ── RÈGLES GAS ────────────────────────────────────────────────────────

    public static String analyzeGas(float gasConsumed, float maxGas) {
        // Compare to the threshold directly (avoids float rounding on the 120% boundary)
        if (gasConsumed <= maxGas)          return "OK";
        else if (gasConsumed <= maxGas * 1.2f) return "WARNING";
        else                                return "CRITICAL";
    }

    // ── RÈGLES TEMPS DE RÉPONSE ───────────────────────────────────────────

    public static String analyzeResponseTime(float rt, float maxRT) {

        if (rt <= maxRT)                return "OK";
        else if (rt <= maxRT * 1.5f)   return "WARNING";
        else                            return "CRITICAL";
    }

    // ── APPLIQUER LA DÉCISION ─────────────────────────────────────────────

    public static String applyDecision(String decision, String agentName,
                                       ContractInfo contract, int violationsLeft) {
        switch (decision) {

            case "OK":
                System.out.println("[" + agentName + "] ✅ OK — "
                    + contract.name + " conforme au SLA.");
                break;

            case "WARNING":
                System.out.println("[" + agentName + "] ⚠️  WARNING — "
                    + contract.name + " : violation légère !");
                System.out.println("[" + agentName + "]    → Violations restantes : "
                    + violationsLeft);
                break;

            case "CRITICAL":
                System.out.println("[" + agentName + "] 🚨 CRITICAL — "
                    + contract.name + " : violation grave !");
                // Appliquer la pénalité directement dans le moteur de décision
                applyPenalty(contract, agentName);
                break;
        }

        return decision;
    }

    // ── APPLIQUER LA PÉNALITÉ ─────────────────────────────────────────────

    public static void applyPenalty(ContractInfo contract, String agentName) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║         🚨 PÉNALITÉ APPLIQUÉE        ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ Contrat    : " + contract.name);
        System.out.println("║ Adresse SC : " + contract.address.substring(0, 10) + "...");
        System.out.println("║ Décidé par : " + agentName);
        System.out.println("║ Action     : Transfert 2 ETH");
        System.out.println("║ Vers       : " + contract.consumerAddress.substring(0, 10) + "...");
        System.out.println("╚══════════════════════════════════════╝");

        // Ici on appellerait Web3j pour vrai transfert ETH
        // contract.transferEther(2 ETH, contract.consumerAddress)
        simulateTransfer(contract);
    }

    // ── SIMULER LE TRANSFERT ETH ──────────────────────────────────────────

    private static void simulateTransfer(ContractInfo contract) {
        System.out.println("[DecisionEngine] 💸 Simulation transfert ETH :");
        System.out.println("[DecisionEngine]    SC      : " + contract.name);
        System.out.println("[DecisionEngine]    Montant : 2 ETH");
        System.out.println("[DecisionEngine]    Vers    : " + contract.consumerAddress);
        System.out.println("[DecisionEngine] ✅ Compensation effectuée !");
    }
}