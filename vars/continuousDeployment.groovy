def call() {


  stage('gitDiff') {
  	env.stage = "${env.STAGE_NAME}"
    //sh (script: 'git diff', returnStdout: true);
    sh ('git diff');
  }

  stage('nexusDownload') {
  }

  stage('run') {

  }

  stage('test') {

  }

  stage('gitMergeMaster') {

  }

  stage('gitMergeDevelop') {

  }

  stage('gitTagMaster') {

  }

}
return this;
