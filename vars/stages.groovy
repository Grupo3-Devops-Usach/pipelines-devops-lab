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
                def repoName = util.getRepoName(env.URL_REPO)
                def projectName = "${repoName}-${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
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
            if(util.isDevelopBranch(env.BRANCH_NAME)){
                stage(Constants.STAGE_GITCREATERELEASE){
                    def merge = bat (script: "git show -s --pretty=%%P", returnStdout: true)
                    def releaseBranch = 'release-v1-0-0'

                    println env.GIT_COMMIT
                    println env.GIT_PREVIOUS_COMMIT
                    println env.GIT_PREVIOUS_SUCCESSFUL_COMMIT
                    println merge.split("\s").length
                    //bat "git checkout -b ${releaseBranch} ${env.GIT_COMMIT_SHORT}"
                    //bat "git push origin ${releaseBranch}"
                }
            }
            break
        case Constants.STAGE_GITDIFF:
            stage(Constants.STAGE_GITDIFF){
                bat "git config --add remote.origin.fetch +refs/heads/main:refs/remotes/origin/main"
                bat "git fetch --no-tags"
                bat "git diff origin/main..origin/${env.BRANCH_NAME}"
            }
            break
        case Constants.STAGE_NEXUSDOWNLOAD:
            stage(Constants.STAGE_NEXUSDOWNLOAD){
                bat 'curl -X GET -u admin:P@ssw0rd2201 http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
            }
            break
        case Constants.STAGE_RUN:
            stage(Constants.STAGE_RUN){
                bat 'start java -jar DevOpsUsach2020-0.0.1.jar'
            }
            break
        case Constants.STAGE_TEST:
            stage(Constants.STAGE_TEST){
                sleep 10
                bat 'curl http://localhost:8082/rest/mscovid/estadoMundial'
                bat 'curl http://localhost:8082/rest/mscovid/test?msg=testing'
            }
            break
        case Constants.STAGE_GITMERGEMASTER:
            stage(Constants.STAGE_GITMERGEMASTER){
                bat "git checkout main"
                bat "git fetch --all"
                bat "git merge origin/${env.BRANCH_NAME} --commit"
                bat "git push origin main"
            }
            break
        case Constants.STAGE_GITMERGEDEVELOP:
            stage(Constants.STAGE_GITMERGEDEVELOP){
                bat "git config --add remote.origin.fetch +refs/heads/develop:refs/remotes/origin/develop"
                bat "git fetch --all"
                bat "git checkout develop"
                bat "git merge origin/${env.BRANCH_NAME} --commit"
                bat "git push origin develop"
            }
            break
        case Constants.STAGE_GITTAGMASTER:
            stage(Constants.STAGE_GITTAGMASTER){
            }
            break
        default:
            break
    }
}

return this;