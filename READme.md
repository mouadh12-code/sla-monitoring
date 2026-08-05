# 🤖 SLA Monitoring — Système Multi-Agents JADE

> Projet de stage d'été 2026 — ESPRIT-TECH / Département RDI

| | |
|---|---|
| **Étudiant** | Laajili Mouadh — 4ème année TWIN, ESPRIT |
| **Encadrante** | Mme Nawal HAMDI |
| **Période** | 1 Juillet – 31 Août 2026 |

---

## 📋 Description

Système intelligent de surveillance des Smart Contracts Ethereum basé sur les **systèmes multi-agents JADE**. Le système surveille la qualité de service **(QoS)** de 6 Smart Contracts en temps réel, détecte automatiquement les violations du **SLA** et applique des compensations automatiques via le **moteur IA NGLS**.

---

## 🏗️ Architecture

[PAgent] ──ACL INFORM──▶ [GAgent] → Surveillance Gas
[PAgent] ──ACL INFORM──▶ [RAgent] → Surveillance Temps de réponse
[GAgent/RAgent] ──▶ [DecisionEngine NGLS] → OK / WARNING / CRITICAL
[DecisionEngine] ──▶ applyPenalty() → Compensation ETH automatique

---

## 🛠️ Technologies utilisées

| Technologie | Rôle |
|---|---|
| **Java 17** | Langage principal |
| **JADE 4.6.1** | Framework multi-agents |
| **Solidity** | Smart Contracts Ethereum |
| **Remix IDE** | Déploiement Smart Contracts |
| **Ganache** | Blockchain Ethereum locale |
| **Web3j 4.9.8** | Communication Java ↔ Ethereum |
| **Maven** | Gestion des dépendances |

---

## 🤖 Agents

| Agent | Rôle |
|---|---|
| **PrincipalAgent** | Interface GUI + coordination |
| **GasAgent** | Surveillance consommation Gas |
| **ResponseTimeAgent** | Surveillance temps de réponse |
| **DecisionEngine** | Moteur IA NGLS — prise de décision + pénalité |

---

## 📊 Smart Contracts surveillés (6)

| # | Nom | maxGas | maxRT | Violations autorisées |
|---|---|---|---|---|
| SC1 | Normal | 100 000 | 30 ms | 3 |
| SC2 | Strict | 80 000 | 20 ms | 2 |
| SC3 | Très strict | 50 000 | 10 ms | 1 |
| SC4 | Souple | 120 000 | 50 ms | 5 |
| SC5 | Très souple | 150 000 | 100 ms | 10 |
| SC6 | Ultra strict | 30 000 | 5 ms | 1 |

---

## 🧠 Moteur IA NGLS — Règles de décision

SI gas <= maxGas → ✅ OK → service conforme
SI gas entre 100% et 120% de max → ⚠️ WARNING → surveillance renforcée
SI gas > 120% de max → 🚨 CRITICAL → pénalité appliquée

SI RT <= maxRT → ✅ OK → service conforme
SI RT entre 100% et 150% de max → ⚠️ WARNING → surveillance renforcée
SI RT > 150% de max → 🚨 CRITICAL → pénalité appliquée

---

## 🚀 Lancement du projet

### Prérequis
- Java JDK 17
- Eclipse IDE
- Maven
- JADE JAR → ajouter `de.enflexit.jade-4.6.1.jar` dans `lib/`

### Étapes

1. Cloner le repository :
```bash
git clone https://github.com/mouadh12-code/sla-monitoring.git
```
2. Importer dans Eclipse → **Maven Project**
3. Ajouter `jade.jar` dans **Build Path**
4. Lancer `Main.java` → **Run As Java Application**
5. Sélectionner le type QoS → cliquer **"Surveiller tous les contrats"**

---

## 📁 Structure du projet

sla-monitoring/
├── src/main/java/
│ ├── esprit.stage.agents/
│ │ ├── PrincipalAgent.java
│ │ ├── GasAgent.java
│ │ ├── ResponseTimeAgent.java
│ │ ├── DecisionEngine.java
│ │ ├── ContractInfo.java
│ │ └── SLAConfig.java
│ └── esprit.stage.sla_monitoring/
│ └── Main.java
├── .gitignore
├── README.md
└── pom.xml

---

## 👤 Auteur

**Laajili Mouadh**
- 🎓 Étudiant ingénieur 4ème année — ESPRIT TWIN
- 💼 LinkedIn : [linkedin.com/in/mouadh-laajili](https://linkedin.com/in/mouadh-laajili)
- 💻 GitHub : [github.com/mouadh12-code](https://github.com/mouadh12-code)
- 📧 laajilimouaadh@gmail.com