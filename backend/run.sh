#!/bin/bash
# Download JavaFX SDK if not present
if [ ! -d "javafx-sdk-17.0.2" ]; then
    curl -L https://download2.gluonhq.com/openjfx/17.0.2/openjfx-17.0.2_osx-x64_bin-sdk.zip -o javafx.zip
    unzip javafx.zip
    rm javafx.zip
fi

# Compile with JavaFX modules
javac --module-path javafx-sdk-17.0.2/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics BudgetifyApp.java

# Run with JavaFX modules
java --module-path javafx-sdk-17.0.2/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics BudgetifyApp 