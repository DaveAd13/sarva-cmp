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

| Home Dashboard (Dark) | Home Dashboard (Light) |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/cc480f70-7dd3-44f9-9ea0-6126be5c861f" width="280" /> | <img src="https://github.com/user-attachments/assets/4b302f87-4c5a-45a4-baad-9f220592c16c" width="280" /> |

---

### 🏃 Fitness & Activity Tracking
Deep integration with system-level health data to monitor daily movement and long-term trends.

| Daily Activity | Daily Chart | Weekly Chart |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/b12ce089-6c45-4952-bc12-804d7f95c1f1" width="280" /> | <img src="https://github.com/user-attachments/assets/aeed6ac8-d0cc-41d6-853f-d60955111043" width="280" /> | <img src="https://github.com/user-attachments/assets/96d1e020-d9d9-4305-a699-1a38f2f13e29" width="280" /> |

* **Custom Data Visualization:** All charts (Bar and Circular) are built from scratch using the **Compose Canvas API**.
* **Zero Dependencies:** By avoiding 3rd-party charting libraries, the app maintains a tiny footprint and offers total design flexibility.
* **Status:** `~85% Complete` — Currently implementing the **iOS HealthKit** bridge.

---

### 💸 Expense Management
A robust financial logger supporting itemized breakdowns and multi-currency transactions.

| Expense List | Detailed Breakdown | Add Expense |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/aa97a1d7-2059-4537-9f20-683bb8e68e0e" width="210" /> | <img src="https://github.com/user-attachments/assets/fc32cfcc-3135-4781-b506-c2d209682f8e" width="210" /> | <img src="https://github.com/user-attachments/assets/c71858c0-1217-4d98-a6f3-faff6fd229b5" width="210" /> |

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
