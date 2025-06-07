#!/bin/bash

# Jenkins Setup Script for API Automation Framework
# This script configures Jenkins for the API automation project

echo "Setting up Jenkins for API Automation Framework..."

# Install required Jenkins plugins
JENKINS_URL="http://localhost:8080"
JENKINS_USER="admin"
JENKINS_PASSWORD="admin"

# List of required plugins
PLUGINS=(
    "maven-plugin"
    "allure-jenkins-plugin"
    "html-publisher-plugin"
    "testng-plugin"
    "cucumber-reports"
    "jacoco"
    "email-ext"
    "build-timeout"
    "github"
    "pipeline-stage-view"
    "workflow-aggregator"
)

echo "Installing Jenkins plugins..."
for plugin in "${PLUGINS[@]}"; do
    echo "Installing plugin: $plugin"
    curl -X POST \
        -u $JENKINS_USER:$JENKINS_PASSWORD \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "plugin=$plugin" \
        "$JENKINS_URL/pluginManager/installNecessaryPlugins"
done

# Configure Maven
echo "Configuring Maven..."
curl -X POST \
    -u $JENKINS_USER:$JENKINS_PASSWORD \
    -H "Content-Type: application/xml" \
    -d @jenkins/maven-config.xml \
    "$JENKINS_URL/configSubmit"

# Configure JDK
echo "Configuring JDK..."
curl -X POST \
    -u $JENKINS_USER:$JENKINS_PASSWORD \
    -H "Content-Type: application/xml" \
    -d @jenkins/jdk-config.xml \
    "$JENKINS_URL/configSubmit"

# Create job
echo "Creating Jenkins job..."
curl -X POST \
    -u $JENKINS_USER:$JENKINS_PASSWORD \
    -H "Content-Type: application/xml" \
    -d @jenkins/job-config.xml \
    "$JENKINS_URL/createItem?name=API-Automation-Tests"

echo "Jenkins setup completed!"
echo "Access Jenkins at: $JENKINS_URL"
echo "Login with: $JENKINS_USER/$JENKINS_PASSWORD"