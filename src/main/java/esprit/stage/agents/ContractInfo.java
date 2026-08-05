package esprit.stage.agents;

/**
 * Contient toutes les infos d'un Smart Contract à surveiller.
 * Rend le système générique — on ajoute juste un nouveau ContractInfo
 * sans toucher aux agents.
 */
public class ContractInfo {

    public String address;        // Adresse du Smart Contract
    public String name;           // Nom du contrat
    public float  maxGas;         // Seuil gas max autorisé
    public float  maxResponseTime;// Seuil temps de réponse max (ms)
    public int    nbViolations;   // Nombre de violations autorisées
    public String consumerAddress;// Adresse du consommateur à compenser

    public ContractInfo(String address, String name, float maxGas,
                        float maxResponseTime, int nbViolations,
                        String consumerAddress) {
        this.address         = address;
        this.name            = name;
        this.maxGas          = maxGas;
        this.maxResponseTime = maxResponseTime;
        this.nbViolations    = nbViolations;
        this.consumerAddress = consumerAddress;
    }

    // Sérialiser en String pour envoyer via ACL
    public String serialize() {
        return address + ";" + name + ";" + maxGas + ";"
            + maxResponseTime + ";" + nbViolations + ";" + consumerAddress;
    }

    // Désérialiser depuis String reçu via ACL
    public static ContractInfo deserialize(String data) {
        String[] parts = data.split(";");
        return new ContractInfo(
            parts[0],                    // address
            parts[1],                    // name
            Float.parseFloat(parts[2]),  // maxGas
            Float.parseFloat(parts[3]),  // maxResponseTime
            Integer.parseInt(parts[4]),  // nbViolations
            parts[5]                     // consumerAddress
        );
    }

    @Override
    public String toString() {
        return name + " [maxGas=" + maxGas + ", maxRT=" + maxResponseTime
            + "ms, violations=" + nbViolations + "]";
    }
}