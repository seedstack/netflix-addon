---
title: "Basics"
name: "Netflix"
repo: "https://github.com/seedstack/netflix-addon"
author: "Adrien DOMURADO"
description: "Provides integration of various Netflix open-source components with SeedStack."
min-version: "16.11+"
backend: true
weight: -1
tags:
    - "netflix"
    - "cloud"
    - "microservices"
    - "api"
zones:
    - Addons
menu:
    AddonNetflix:
        weight: 10
---

Seed Netflix add-on contains several Netflix open-source components to allow cloud and microservices achitectures in Seed applications.

If you want to add all the components in one go, use the following dependency:
{{< dependency g="org.seedstack.addons.netflix" a="netflix" >}}

However, you can add only the components that are relevant for your project.
