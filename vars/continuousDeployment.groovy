def call() {


  stage('gitDiff') {
  	env.stage = "${env.STAGE_NAME}"
    //sh (script: 'git diff', returnStdout: true);
    sh ('git diff');
    sh ('git config --add remote.origin.fetch +refs/heads/main:refs/remotes/origin/main');
    sh ('git fetch --no-tags');
    sh ('git diff origin/main..origin/${env.BRANCH_NAME}');
  }

  stage('nexusDownload') {
    sh ('start java -jar DevOpsUsach2020-0.0.1.jar');
  }

  stage('run') {
    withMaven {
    sh ('nohup bash mvn spring-boot:run &');
      }
  }

  stage('test') {
    sleep 20;
    sh ('curl http://localhost:8081/rest/mscovid/test?msg=testing');
    sh ('curl http://localhost:8082/rest/mscovid/estadoMundial');
  }


  stage('gitMergeMaster') {
    sh ('git checkout main');
    sh ('git fetch --all');
    sh ('git merge origin/${env.BRANCH_NAME} --commit');
    sh ('git push origin main');
  }

  stage('gitMergeDevelop') {
    sh "git config --add remote.origin.fetch +refs/heads/develop:refs/remotes/origin/develop"
    sh "git fetch --all"
    sh "git checkout develop"
    sh "git merge origin/${env.BRANCH_NAME} --commit"
    sh "git push origin develop"
  }

  stage('gitTagMaster') {
  }

}
return this;
