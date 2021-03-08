# ExposureCalc
**Cross-platform tool for converting camera's exposure parameters**

The application is written in C++ and QML with Qt framework. This allows to deploy on multiple systems including: Windows, macOS, Android and iOS.
Application is translated into English and Polish. 

![Main Screen](screenshots/macOS_mainpage.png "App main screen") ![Settings Screen](screenshots/macOS_settings.png "App settings screen")

## Motivation
The goal was to create a cross-platform app using only one source code with as little as possible code modifications, learning Qt framework in the process.

## Built with
* [Qt 5.9.5](https://www.qt.io/) - The cross-platform software development framework

## Release
There are ready to use binaries for below platforms:
* [Windows](https://github.com/MarcinPis/ExposureCalc/releases/tag/v1.0.0) – compiled on Windows 10 with MinGW 5.3.0. Tested on Windows 10 and Windows 8.1 64 bits systems.
* [macOS](https://github.com/MarcinPis/ExposureCalc/releases/tag/v1.0.0) – compiled on macOS High Sierra (10.13) with Clang. Tested on macOS High Sierra (10.13) system.
* [Android armeabi-v7a](https://github.com/MarcinPis/ExposureCalc/releases/tag/v1.0.0) – compiled on Windows 10 with GCC 4.9. Minimum system requirement is Android 4.1 (API 16). Tested on Android 4.3 and 7.0 systems.

Due to Apple restrictions there is no build on iOS device. This can be achieved by building from source.

## Deployment
The code is prepared for Windows, MacOS, Android and iOS platforms. 

To be able to build the app from the source Qt 5.9.5 or later is needed as well as below packages:

**To build for Windows:**
* C++ compiler with C++ 14 support – MinGW 5.3.0 or MS Visual C++ Compiler 14.0 or later.

**To build for Android:**
* Java SE Development Kit 8 (JDK),
* Android SDK,
* Android NDK.

**To build for MacOS/iOS:**
* The newest version of Xcode,
* Clang C++ compiler.

## Author
Marcin Pisarski