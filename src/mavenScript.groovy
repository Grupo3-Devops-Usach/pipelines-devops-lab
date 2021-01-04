def call(String selectStage = '') {

	switch (selectStage) {

		case 'compile':
		stage('compile') {
			println 'Compile Maven';
			env.stage = "${env.STAGE_NAME}";
			sh 'mvn clean compile -e';
		}
		break;

		case 'unit':
		stage('unit') {
			env.stage = "${env.STAGE_NAME}";
			sh 'mvn clean test -e';
		}
		break;

		case 'jar':
		stage('jar') {
			env.stage = "${env.STAGE_NAME}";
			sh 'mvn clean package -e';
		}
		break

		case 'sonar':
		stage('sonar') {
			env.stage = "${env.STAGE_NAME}";
			script {
			withSonarQubeEnv('sonar') {
				sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar';
	  			}
			}
		}
		break

		case 'nexus':
	  stage('nexus') {
	 		env.stage = "${env.STAGE_NAME}";
			nexusPublisher nexusInstanceId: 'nexus',
			nexusRepositoryId: 'test-nexus',
			packages:
				[[$class: 'MavenPackage',
				mavenAssetList:
					[[classifier: '',
					extension: '',
					filePath: 'build/DevOpsUsach2020-0.0.1.jar']],
				mavenCoordinate:
					[artifactId: 'DevOpsUsach2020',
					groupId: 'com.devopsusach2020',
					packaging: 'jar',
					version: '0.0.1']]];
		}
		break

		case 'gitcreaterelease':
		stage('gitCreateRelease') {
			env.stage = "${env.STAGE_NAME}";
				sh ('git checkout release');
		}
		break

	}
}
return this;
