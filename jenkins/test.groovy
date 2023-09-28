timeout(60){
    node("maven-slave"){
        stage("Checkout"){
            checkout scm
        }
        stage("Run tests"){
            def exitCode = sh(
                    returnStatus: true,
                    script: """
                    gradle test
                    """
            )
            if (exitCode == 1){
                currentBuild.result = "UNSTABLE"
            }
        }
        stage("Publish allure results"){
            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'allure-results']]
            ])
        }
    }
}