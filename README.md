### **KtorAdmin – The All-in-One Admin Panel for Ktor 🚀**

![KtorAdmin Banner](/art/banner.jpg)

![Maven Central](https://img.shields.io/maven-central/v/io.github.amirroid/KtorAdmin)
![Ktor](https://img.shields.io/badge/Ktor-%E2%9C%94-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-%E2%9C%94-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-%E2%9C%94-brightgreen)
![MongoDB](https://img.shields.io/badge/MongoDB-%E2%9C%94-brightgreen)

## **🌟 Overview**

**KtorAdmin** is a **powerful and flexible** library that effortlessly creates an **admin panel** for your **Ktor-based** applications. Unlike traditional admin panels that rely on **predefined database schemas**, KtorAdmin **dynamically detects** and adapts to your ORM structure, making it a perfect solution for managing both **relational and NoSQL databases**.

| Name      | Dark Mode                        | Light Mode                         |
|-----------|----------------------------------|------------------------------------|
| Dashboard | ![Dark](/art/dark_dashboard.png) | ![Light](/art/light_dashboard.png) |
| Panel     | ![Dark](/art/panel_dark.png)     | ![Light](/art/panel_light.png)     |
| Add, Edit | ![Dark](/art/upsert_dark.png)    | ![Light](/art/upsert_light.png)    |

## **🔥 Key Features**

✅ **Schema-Free Setup** – No need for predefined database schemas! KtorAdmin automatically detects ORM structures.  
✅ **Multi-ORM Support** – Works seamlessly with **Hibernate, Exposed, and MongoDB**.  
✅ **Dynamic UI Generation** – Auto-generates an **admin panel** based on entity definitions.  
✅ **Event-Driven Architecture** – Supports **event listeners** for insert, update, and delete operations.  
✅ **File Handling** – Easily manage **file uploads**, including automatic **thumbnail generation**.  
✅ **Role-Based Access Control (RBAC)** – Fine-grained **user roles & permissions** management.  
✅ **Custom Actions** – Add your own **admin actions** tailored to your needs.  
✅ **Multiple Authentication Providers** – Compatible with **various authentication methods**.  
✅ **Data Export** – Export data in multiple formats **with ease**.  
✅ **Rich Text Editor** – Built-in **content editing** support.  
✅ **Advanced Filtering & Search** – Quickly **filter & find data** with powerful search capabilities.  
✅ **Optimized for Performance** – Designed for **high performance** in **production environments**.

---

## **💡 Why Choose KtorAdmin?**

KtorAdmin removes **unnecessary boilerplate code** and provides a **schema-independent, dynamic, and extensible** solution for admin panel creation. Whether you're handling **relational databases** like **MySQL & PostgreSQL** or working with **NoSQL databases** like **MongoDB**, KtorAdmin gives you an effortless way to **manage and monitor** your app’s data.

📖 **Documentation:** [Read the Docs](https://amirreza-gholami.gitbook.io/ktor-admin) 🚀

---

## **🔧 Installation**

To include **KtorAdmin** in your **Ktor** application, add the following dependencies to your **build.gradle.kts** file:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

repositories {
    mavenCentral()
}

dependencies {
    // KtorAdmin library
    implementation("io.github.amirroid:KtorAdmin:0.0.1-alpha")
    ksp("io.github.amirroid:KtorAdmin:0.0.1-alpha")
}
```

---
## **License**
This package is created and modified by [Amirroid](https://github.com/Amirroid) and has been released under the MIT License.