#!/bin/bash

# API Automation Framework Test Execution Script
# This script provides easy commands to run different test suites

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default values
ENVIRONMENT="dev"
TEST_SUITE="smoke"
PARALLEL="false"
GENERATE_REPORTS="true"

# Function to display usage
usage() {
    echo -e "${BLUE}API Automation Framework Test Runner${NC}"
    echo ""
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -e, --env ENVIRONMENT     Environment to run tests against (dev, qa, prod)"
    echo "  -s, --suite TEST_SUITE    Test suite to execute (smoke, regression, performance, all)"
    echo "  -p, --parallel           Enable parallel execution"
    echo "  -r, --reports            Generate reports (default: true)"
    echo "  -h, --help               Display this help message"
    echo ""
    echo "Examples:"
    echo "  $0 --env qa --suite smoke"
    echo "  $0 --env prod --suite regression --parallel"
    echo "  $0 --suite all --parallel --reports"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -e|--env)
            ENVIRONMENT="$2"
            shift 2
            ;;
        -s|--suite)
            TEST_SUITE="$2"
            shift 2
            ;;
        -p|--parallel)
            PARALLEL="true"
            shift
            ;;
        -r|--reports)
            GENERATE_REPORTS="$2"
            shift 2
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            usage
            exit 1
            ;;
    esac
done

echo -e "${BLUE}Starting API Automation Tests...${NC}"
echo -e "${YELLOW}Environment: $ENVIRONMENT${NC}"
echo -e "${YELLOW}Test Suite: $TEST_SUITE${NC}"
echo -e "${YELLOW}Parallel Execution: $PARALLEL${NC}"

# Clean previous results
echo -e "${BLUE}Cleaning previous test results...${NC}"
mvn clean

# Compile project
echo -e "${BLUE}Compiling project...${NC}"
mvn compile test-compile

# Prepare Maven options
MAVEN_OPTS="-Denv=$ENVIRONMENT"
if [ "$PARALLEL" = "true" ]; then
    MAVEN_OPTS="$MAVEN_OPTS -Dparallel=methods -DthreadCount=5"
fi

# Execute tests based on suite selection
case $TEST_SUITE in
    "smoke")
        echo -e "${GREEN}Running Smoke Tests...${NC}"
        mvn test $MAVEN_OPTS -Dgroups=smoke
        ;;
    "regression")
        echo -e "${GREEN}Running Regression Tests...${NC}"
        mvn test $MAVEN_OPTS -Dgroups=regression
        echo -e "${GREEN}Running Cucumber BDD Tests...${NC}"
        mvn test -Pcucumber $MAVEN_OPTS -Dcucumber.filter.tags="@regression"
        ;;
    "performance")
        echo -e "${GREEN}Running Performance Tests...${NC}"
        mvn test $MAVEN_OPTS -Dgroups=performance
        ;;
    "all")
        echo -e "${GREEN}Running All Test Suites...${NC}"
        mvn test $MAVEN_OPTS -Dgroups=smoke
        mvn test $MAVEN_OPTS -Dgroups=regression
        mvn test -Pcucumber $MAVEN_OPTS
        mvn test $MAVEN_OPTS -Dgroups=performance
        ;;
    *)
        echo -e "${RED}Invalid test suite: $TEST_SUITE${NC}"
        usage
        exit 1
        ;;
esac

# Generate reports if requested
if [ "$GENERATE_REPORTS" = "true" ]; then
    echo -e "${GREEN}Generating Test Reports...${NC}"
    
    # Generate Allure report
    echo -e "${BLUE}Generating Allure Report...${NC}"
    mvn allure:report
    
    # Generate code coverage
    echo -e "${BLUE}Generating Code Coverage Report...${NC}"
    mvn jacoco:report
    
    echo -e "${GREEN}Reports generated successfully!${NC}"
    echo -e "${YELLOW}Allure Report: target/site/allure-maven-plugin/index.html${NC}"
    echo -e "${YELLOW}Extent Report: target/extent-reports/index.html${NC}"
    echo -e "${YELLOW}Coverage Report: target/site/jacoco/index.html${NC}"
fi

echo -e "${GREEN}Test execution completed successfully!${NC}"