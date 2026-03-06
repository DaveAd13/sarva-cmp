# 🌌 Sarva | Compose Multiplatform Lifehub
### 🛠 A Technical Portfolio Exploration in Kotlin Multiplatform

**Sarva** (Sanskrit for *all* or *universal*) is a unified "lifehub" application designed to consolidate your daily digital footprint—fitness, finances, and tasks—into a single, fluid experience. This project serves as a showcase for high-performance **Compose Multiplatform (CMP)** development, focusing on modularity, custom graphics, and the latest Jetpack libraries.

> [!IMPORTANT]
> **Project Status: Under Active Development 🚧**
> This repository is a portfolio piece designed to showcase technical problem-solving and architectural decisions. Features are released iteratively as I bridge platform-specific APIs (such as iOS HealthKit).

---

## 🏗 The Tech Stack
* **UI Framework:** [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) (Android, iOS, Desktop)
* **Navigation:** **Compose Navigation 3** — Implementing the latest type-safe, multiplatform navigation paradigm.
* **Architecture:** **MVI (Model-View-Intent)** for predictable, unidirectional data flow.
* **Local Database:** **Room Multiplatform** for cross-platform persistence.
* **Networking:** **Ktor** (Multiplatform Client).
* **Dependency Injection:** **Koin**.
* **Theming:** Full Dynamic Light and Dark mode support.

---

## 📱 Feature Spotlight & Preview

### 🏠 Universal Dashboard
The core of Sarva is a widget-based dashboard that provides a high-level overview of all life pillars, from fitness progress to financial status.

| Home Dashboard (Dark) | Home Dashboard 2 (Dark) | Home Dashboard (Light) |
| :---: | :---: | :---: |
| <img width="225" alt="Screenshot_20260306_174802" src="https://github.com/user-attachments/assets/3d3e467f-5907-4421-a666-b194c1d35a01" /> | <img width="225" alt="Screenshot_20260306_174811" src="https://github.com/user-attachments/assets/1ec65ab8-4637-4787-b317-6b1e199bab99" /> | <img width="225" alt="Screenshot_20260306_175314" src="https://github.com/user-attachments/assets/e8dd8090-80f6-409a-a009-9912dd25dd87" /> |

---

### 🏃 Fitness & Activity Tracking
Deep integration with system-level health data to monitor daily movement and long-term trends.

| Daily Activity (Dark) | Daily Chart (Dark) | Weekly Chart (Dark) | Daily Chart (Light) |
| :---: | :---: | :---: | :---: |
| <img width="225" alt="Screenshot_1" src="https://github.com/user-attachments/assets/85c4b24a-449f-44a6-99b9-1f619f191dc0" /> | <img width="225" alt="Screenshot_2" src="https://github.com/user-attachments/assets/46f2df34-a9b4-49dc-aeb0-9ff1bbb9fead" /> | <img width="225" alt="Screenshot_3" src="https://github.com/user-attachments/assets/4698b6f5-cc5c-4b53-acac-653b56423906" /> | <img width="225" alt="Screenshot_4" src="https://github.com/user-attachments/assets/dcff143a-67ab-4087-9a61-71ec914f0edc" /> |


* **Custom Data Visualization:** All charts (Bar and Circular) are built from scratch using the **Compose Canvas API**.
* **Zero Dependencies:** By avoiding 3rd-party charting libraries, the app maintains a tiny footprint and offers total design flexibility.
* **Status:** `~85% Complete` — Currently implementing the **iOS HealthKit** bridge.

---

### 💸 Expense Management
A robust financial logger supporting itemized breakdowns and multi-currency transactions.

| Expense List (Dark) | Detailed Breakdown (Dark) | Add Expense (Dark) | Expense List (Light) |
| :---: | :---: | :---: | :---: |
| <img width="225" alt="Screenshot_5" src="https://github.com/user-attachments/assets/9ea132ba-59bb-485f-8224-d37eac8cb188" /> | <img width="225" alt="Screenshot_6" src="https://github.com/user-attachments/assets/ee69a045-ca3d-425c-bbb0-89bf6638e1cd" /> | <img width="225" alt="Screenshot_7" src="https://github.com/user-attachments/assets/78c38b47-3b9d-440c-904e-0c9009bdb703" /> | <img width="225" alt="Screenshot_8" src="https://github.com/user-attachments/assets/560eced6-dcbc-4b8a-87d6-0c804cfce3f4" /> |

* **Granular Tracking:** Support for itemized sub-lists within a single transaction (e.g., splitting a single bill into multiple categories).
* **Status:** `Nearly Complete` — Refining multi-currency persistence and UI polish.

---

## 🗺 Feature Roadmap

Sarva is built as a modular monolith, prioritizing platform-specific design patterns for each "pillar."

| Feature | Status | Target Platforms |
| :--- | :--- | :--- |
| **🏃 Fitness** | `In Progress` | Android (Active), iOS (Bridging) |
| **💸 Expenses** | `Nearly Done` | Android, iOS, Desktop |
| **📝 Notes** | `Planned` | Markdown Support |
| **📋 Tasks** | `Planned` | Priority-based To-Dos |
| **📅 Calendar** | `Planned` | Unified Event Timeline |
| **📍 My Journey** | `Planned` | Platform-native Maps |

---

## 🛠 Technical Deep Dive

### 🎨 Custom Graphics via Canvas
To ensure maximum performance and pixel-perfect design, the data visualization layer is implemented purely with the **Compose Canvas API**. This demonstrates proficiency in:
* Manual coordinate space calculations.
* Scaling logic for dynamic data sets.
* Performance-optimized rendering within the Compose graphics layer.

[Image of Jetpack Compose Canvas coordinate system mapping]

### 🚀 Navigation 3 Implementation
Sarva is an early adopter of **Navigation 3**. This implementation highlights the ability to work with bleeding-edge libraries to provide type-safe, declarative navigation that handles complex backstacks seamlessly across Mobile and Desktop environments.

### 🔄 Unidirectional Data Flow (MVI)
By strictly following the **MVI pattern**, the app ensures that the UI state remains predictable and testable across all targets. This is particularly vital for complex form states, such as the dynamic expense entry system.

---

*Developed with ❤️ as a CMP Portfolio Project.*
