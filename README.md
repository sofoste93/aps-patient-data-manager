# APS Patient Data Manager

[![CI](https://github.com/sofoste93/aps-patient-data-manager/actions/workflows/ci.yml/badge.svg)](https://github.com/sofoste93/aps-patient-data-manager/actions/workflows/ci.yml)
[![Java 17](https://img.shields.io/badge/Java-17-1769aa)](https://adoptium.net/)

A multilingual JavaFX form for recording a compact patient profile and
generating a QR code. Version 1.0.0 is the repaired release of the original
application.

![APS Patient Data Manager](https://user-images.githubusercontent.com/28387985/225116471-1e9de0d0-00e7-4992-a08f-2500c165087d.PNG)

## Download

Open the [latest release](https://github.com/sofoste93/aps-patient-data-manager/releases/latest)
and choose the package for Windows, Linux, macOS Intel or macOS Apple Silicon.
The Java runtime is included.

## Features

- English, French, German and Spanish interfaces;
- patient identity, allergies, medication and medical-history fields;
- validation of required names and age;
- local JSON records grouped by language;
- QR-code preview for an explicitly entered profile;
- no network transmission.

Records are written below the current user's home folder in
`.aps-patient-data-manager/data`. They are plain JSON files. This educational
application is not a medical device and should not be used for real sensitive
health records without appropriate encryption, access control and compliance
review.

## Run from source

Requirements: JDK 17 and Maven 3.9 or newer.

```bash
mvn clean verify
mvn javafx:run
```

The tests cover form validation, safe language filenames and repeated local
saves. On Windows, `scripts\package.ps1` builds the standalone application.

## Maintenance release

The original project could not resolve one dependency and combined JavaFX 18,
JavaFX 19 and Java 19 settings. Version 1.0.0 aligns the build on Java 17,
removes unused libraries, fixes language selection and avoids partial JSON
writes. The original sample data remains under `examples/`.

Licensed under the [MIT License](LICENSE).
