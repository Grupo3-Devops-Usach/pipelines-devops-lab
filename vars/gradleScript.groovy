def call(String selectStage = '') {

	switch (selectStage) {

		case 'build':
		stage('Build') {
			env.stage = "${env.STAGE_NAME}";
			sh './gradlew clean build';
		}
		break;

		case 'sonar':
		stage('Sonar') {
			env.stage = "${env.STAGE_NAME}";
	   	def scannerHome = tool 'sonar';
	   	withSonarQubeEnv('sonar') {
				sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build";
			}
		}
		break;

		case 'run':
		stage('run') {
			env.stage = "${env.STAGE_NAME}";
			sh 'nohup bash gradlew bootRun &';
			sleep 20
		}
		break;

		case 'test':
		stage('test') {
			env.stage = "${env.STAGE_NAME}";
			sh 'curl -X GET http://localhost:8082/rest/mscovid/test?msg=testing';
		}
		break;

		case 'nexus':
		stage('nexus') {
			env.stage = "${env.STAGE_NAME}"
			nexusPublisher nexusInstanceId: 'nexus',
			nexusRepositoryId: 'test-nexus',
			packages:
				[[$class: 'MavenPackage',
				mavenAssetList:
					[[classifier: '',
					extension: '',
					filePath: '/home/jm/Workspace/ejemplo-maven/build/DevOpsUsach2020-0.0.1.jar']],
				mavenCoordinate:
					[artifactId: 'DevOpsUsach2020',
					groupId: 'com.devopsusach2020',
					packaging: 'jar',
					version: '0.0.1']]]
		}
		break

		case 'gitcreaterelease':
		stage('gitCreateRelease') {
			env.stage = "${env.STAGE_NAME}";
			sh ('git checkout ' + env.releaseBranch);
			env.releaseBranch = sh (script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim();
			println 'Rama Release ' + releaseBranch + 'creada con éxito.'
		}
		break

	}
}
return this;
