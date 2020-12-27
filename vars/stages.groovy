import com.util.Constants

def call(){
    switch(env.STG_NAME){
        case Constants.STAGE_COMPILE:
            stage(Constants.STAGE_COMPILE){
                bat 'mvnw.cmd clean compile -e'
            }
            break
        case Constants.STAGE_UNITTEST:
            stage(Constants.STAGE_UNITTEST){
                bat 'mvnw.cmd clean test -e'
            }
            break
        case Constants.STAGE_JAR:
            stage(Constants.STAGE_JAR){
                bat 'mvnw.cmd clean package -e'
            }
            break
        case Constants.STAGE_SONAR:
            stage(Constants.STAGE_SONAR){
                def projectName = "repo-${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
                def scannerHome = tool 'sonar-scanner';
                withSonarQubeEnv('sonar') {
                    bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectName=${projectName} -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build -Dsonar.login=75a0e9b0613f563c0e69a23174cf79eb5d4d74c7"
                }
            }
            break
        case Constants.STAGE_NEXUSUPLOAD:
            stage(Constants.STAGE_NEXUSUPLOAD){
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'build\\DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
            }
            break
        case Constants.STAGE_GITCREATERELEASE:
            stage(Constants.STAGE_GITCREATERELEASE){
            }
            break
        default:
            break
    }
}

return this;