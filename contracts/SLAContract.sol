// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract SLAContract {

    // ── Propriétaire du contrat (fournisseur) ──────────────────────────────
    address public owner;

    // ── Paramètres QoS du SLA ─────────────────────────────────────────────
    uint256 public maxGasAllowed;
    uint256 public maxResponseTime;
    uint256 public nbViolationsAllowed;

    // ── Métriques mesurées ────────────────────────────────────────────────
    uint256 public endGas;
    uint256 public responseTime;

    // ── Compteurs ─────────────────────────────────────────────────────────
    uint256 public violationCount;

    // ── Événements ────────────────────────────────────────────────────────
    event ViolationDetected(string qosType, uint256 measuredValue, uint256 threshold);
    event CompensationSent(address consumer, uint256 amount);

    // ── Constructeur ──────────────────────────────────────────────────────
    constructor(
        uint256 _maxGas,
        uint256 _maxResponseTime,
        uint256 _nbViolations
    ) payable {
        owner = msg.sender;
        maxGasAllowed = _maxGas;
        maxResponseTime = _maxResponseTime;
        nbViolationsAllowed = _nbViolations;
        violationCount = 0;
    }

    // ── Simuler l'exécution du contrat ────────────────────────────────────
    function executeService(uint256 startTime) public returns (bool) {
        uint256 gasStart = gasleft();

        // Simulation d'un traitement
        uint256 result = 0;
        for (uint256 i = 0; i < 100; i++) {
            result += i;
        }

        // Calcul des métriques
        endGas = gasStart - gasleft();
        responseTime = block.timestamp - startTime;

        return true;
    }

    // ── Vérifier l'état QoS du contrat ────────────────────────────────────
    function getGasConsumed() public view returns (uint256) {
        return endGas;
    }

    function getResponseTime() public view returns (uint256) {
        return responseTime;
    }

    function getViolationCount() public view returns (uint256) {
        return violationCount;
    }

    // ── Appliquer la pénalité (compensation automatique) ──────────────────
    function transferEther(uint256 amount, address payable consumer) public payable {
        require(address(this).balance >= amount, "Solde insuffisant");

        violationCount++;
        emit ViolationDetected("QoS", amount, maxGasAllowed);

        (bool success, ) = consumer.call{value: amount}("");
        require(success, "Transfer failed");

        emit CompensationSent(consumer, amount);
    }

    // ── Consulter le solde du contrat ─────────────────────────────────────
    function getBalance() public view returns (uint256) {
        return address(this).balance;
    }

    // ── Recevoir des fonds ────────────────────────────────────────────────
    receive() external payable {}
}