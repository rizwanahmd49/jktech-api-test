# API Automation Framework

A comprehensive API automation framework built with RestAssured, TestNG, Cucumber, and JUnit for robust API testing with dual reporting capabilities (Allure and Extent Reports).

## Framework Architecture

```
api-automation-framework/
├── src/
│   ├── main/java/com/automation/
│   │   ├── config/          # Configuration management
│   │   ├── clients/         # API client implementations
│   │   ├── models/          # Data models and POJOs
│   │   └── utils/           # Utility classes
│   └── test/
│       ├── java/com/automation/
│       │   ├── tests/       # TestNG test classes
│       │   ├── steps/       # Cucumber step definitions
│       │   └── runners/     # Test runners
│       └── resources/
│           ├── features/    # Cucumber feature files
│           ├── schemas/     # JSON schema validation files
│           ├── testdata/    # Test data files
│           └── config-*.properties # Environment configurations
├── jenkins/                 # Jenkins pipeline configurations
├── reports/                # Generated test reports
└── target/                 # Build outputs and reports
```

## Key Features

- **Multi-Framework Integration**: RestAssured + TestNG + Cucumber + JUnit
- **Dual Reporting**: Allure Reports and Extent Reports
- **Environment Management**: Configurable for dev/qa/prod environments
- **Request Chaining**: Output of one API call used as input for another
- **Comprehensive Test Coverage**: CRUD operations, positive/negative scenarios
- **Performance Testing**: Response time validation
- **JSON Schema Validation**: Automatic response structure validation
- **CI/CD Ready**: Jenkins pipeline configuration included
- **Parallel Execution**: Configurable parallel test execution

## Testing Strategy

### Test Flow Approach

1. **Layered Testing Architecture**
   - **Unit Tests**: Individual API endpoint validation
   - **Integration Tests**: End-to-end workflow testing
   - **Contract Tests**: JSON schema validation ensures API contract compliance

2. **Test Categorization**
   - **Smoke Tests**: Critical path validation for quick feedback
   - **Regression Tests**: Comprehensive feature coverage
   - **Performance Tests**: Response time and load validation
   - **Negative Tests**: Error handling and edge case validation

3. **Request Chaining Strategy**
   - Create → Retrieve → Update → Delete workflow
   - Dynamic data usage between test steps
   - State management across dependent tests

### Reliability and Maintainability

1. **Configuration Management**
   - Environment-specific property files
   - Centralized configuration through ConfigManager
   - Runtime parameter override capability

2. **Robust Test Design**
   - Page Object Model adaptation for API clients
   - Reusable API client with built-in retry mechanism
   - Soft assertions for comprehensive validation
   - Independent test design with proper setup/teardown

3. **Error Handling**
   - Comprehensive logging with LogManager
   - Graceful failure handling with detailed error messages
   - Screenshot and request/response capture for debugging

4. **Code Maintainability**
   - Clean separation of concerns
   - Reusable components and utilities
   - Comprehensive documentation and naming conventions
   - Version-controlled test data and configurations

### Challenges and Solutions

1. **Challenge: Test Data Management**
   - **Solution**: Implemented dynamic test data generation and cleanup mechanisms
   - **Approach**: Used request chaining to create dependencies dynamically

2. **Challenge: Environment Configuration**
   - **Solution**: Created flexible configuration management system
   - **Approach**: Property files with runtime override capabilities

3. **Challenge: Flaky Tests**
   - **Solution**: Implemented retry mechanisms and proper wait strategies
   - **Approach**: Added response time validation and timeout configurations

4. **Challenge: Reporting Integration**
   - **Solution**: Integrated both Allure and Extent Reports
   - **Approach**: Multiple reporting plugins with comprehensive test documentation

5. **Challenge: CI/CD Integration**
   - **Solution**: Created Jenkins pipeline with proper artifact management
   - **Approach**: Containerized execution with report publishing

## Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Jenkins (for CI/CD)

### Installation
```bash
git clone https://github.com/rizwanahmd49/jktech-api-test.git
cd api-automation-framework
mvn clean install
```

### Running Tests

#### TestNG Tests
```bash
# Run all tests
mvn test

# Run specific test suite
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml

# Run with specific environment
mvn test -Denv=qa
```

#### Cucumber Tests
```bash
# Run Cucumber tests
mvn test -Pcucumber

# Run specific tags
mvn test -Pcucumber -Dcucumber.filter.tags="@smoke"
```

#### Performance Tests
```bash
mvn test -Dgroups=performance
```

### Environment Configuration

Create environment-specific property files:

```properties
# config-dev.properties
api.base.url=https://reqres.in/
api.timeout=30000
api.retry.enabled=true
api.max.retries=3
```

### Reporting

#### Generate Allure Reports
```bash
mvn allure:serve
```

#### View Extent Reports
Reports are automatically generated in `target/extent-reports/`

## Test Execution Examples

### CRUD Operations Coverage

1. **Create Post** - POST /posts
2. **Read Post** - GET /posts/{id}
3. **Read All Posts** - GET /posts
4. **Update Post** - PUT /posts/{id}
5. **Partial Update** - PATCH /posts/{id}
6. **Delete Post** - DELETE /posts/{id}

### Validation Coverage

- Status code validation
- Response payload validation
- JSON schema validation
- Response time validation
- Error handling validation
- Header validation

## CI/CD Integration

### Jenkins Pipeline

The framework includes a complete Jenkins pipeline configuration:

1. **Environment Setup**
2. **Dependency Installation**
3. **Test Execution**
4. **Report Generation**
5. **Artifact Publishing**

### Pipeline Features

- Parallel test execution
- Environment-specific deployments
- Automatic report publishing
- Failure notifications
- Code coverage reporting

## Contributing

1. Follow the established project structure
2. Add comprehensive test coverage for new features
3. Update documentation for any configuration changes
4. Ensure all tests pass before submitting pull requests
5. Follow naming conventions and coding standards

## Support

For issues and questions:
1. Check existing documentation
2. Review test logs and reports
3. Verify environment configuration
4. Contact the development team

## Version History

- **v1.0.0**: Initial framework release with full CRUD coverage
- Comprehensive reporting integration
- Jenkins CI/CD pipeline
- Multi-environment support
