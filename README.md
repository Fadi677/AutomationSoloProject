# IMDb Web Automation Project

A robust, enterprise-grade web automation framework designed to test search functionalities, data-driven filtering, and chart sorting mechanisms on the IMDb platform. Built using **Java 17**, **Selenium WebDriver**, and **TestNG**, this project strictly follows the **Page Object Model (POM)** design pattern to deliver highly maintainable and clean test automation.

---

## 🛠️ Tech Stack & Architecture
* **Core Language:** Java 17 (JavaSE-17)
* **Automation Tool:** Selenium WebDriver (4.x)
* **Testing Framework:** TestNG
* **Build Engine:** Apache Maven
* **Design Pattern:** Page Object Model (POM)

---

## 📂 Project Directory Structure

```text
imdb-automation-project/
├── src/
│   ├── main/java/                     # Page Objects & Core Browser Logic
│   │   └── solo.fadi.imdb_automation_project/
│   │       └── ImdbPage.java          # IMDb Locators & Action Flows
│   └── test/java/                     # Test Suites & Assertions
│       └── solo.fadi.imdb_automation_project/
│           ├── BaseTest.java          # Driver initialization & tear-down hooks
│           ├── ChartSortingWorkflowTest.java
│           └── SearchWorkflowTest.java
├── pom.xml                            # Dependencies & Maven execution profiles
├── testng.xml                         # Test Suite Controller Orchestrator
└── search_data.csv                    # External Data-Driven Test Inputs# AutomationSoloProject
IMDB website automation testing using Selenium with Java and TestNG
