package esprit.stage.agents;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration SLA générique.
 * Pour ajouter un nouveau contrat → ajouter une ligne dans CONTRACT_LIST.
 * Aucun changement dans les agents nécessaire.
 */
public class SLAConfig {

    // ── Adresse consommateur par défaut ───────────────────────────────────
    public static final String DEFAULT_CONSUMER =
        "0x5B38Da6a701c568545dCfcB03FcB875f56beddC4";

    // ── Liste générique des contrats à surveiller ─────────────────────────
    // Pour ajouter un contrat → ajoute juste une ligne ici !
    public static final List<ContractInfo> CONTRACT_LIST = new ArrayList<>();

    static {
        CONTRACT_LIST.add(new ContractInfo(
            "0xd2a5bC10698FD955D1Fe6cb468a17809A08fd005",
            "SC1-Normal", 100000, 30, 3, DEFAULT_CONSUMER));

        CONTRACT_LIST.add(new ContractInfo(
            "0xddaAd340b0f1Ef65169Ae5E41A8b10776a75482d",
            "SC2-Strict", 80000, 20, 2, DEFAULT_CONSUMER));

        CONTRACT_LIST.add(new ContractInfo(
            "0x0fC5025C764cE34df352757e82f7B5c4Df39A836",
            "SC3-TrèsStrict", 50000, 10, 1, DEFAULT_CONSUMER));

        CONTRACT_LIST.add(new ContractInfo(
            "0xb27A31f1b0AF2946B7F582768f03239b1eC07c2c",
            "SC4-Souple", 120000, 50, 5, DEFAULT_CONSUMER));

        CONTRACT_LIST.add(new ContractInfo(
            "0xcD6a42782d230D7c13A74ddec5dD140e55499Df9",
            "SC5-TrèsSouple", 150000, 100, 10, DEFAULT_CONSUMER));

        CONTRACT_LIST.add(new ContractInfo(
            "0xaE036c65C649172b43ef7156b009c6221B596B8b",
            "SC6-UltraStrict", 30000, 5, 1, DEFAULT_CONSUMER));
    }

    // ── Affichage ─────────────────────────────────────────────────────────
    public static void printSLA() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      SYSTÈME DE SURVEILLANCE SLA     ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ Contrats surveillés : " + CONTRACT_LIST.size());
        System.out.println("╠══════════════════════════════════════╣");
        for (ContractInfo c : CONTRACT_LIST) {
            System.out.println("║ → " + c.toString());
        }
        System.out.println("╚══════════════════════════════════════╝");
    }
}