import pipeline.*

def call(String chosenStages) {

	figlet 'gradle'

	def pipelineStages = ['buildAndTest','sonar','runJar','rest','nexus']

	def utils =  new test.UtilMethods()
	def stages = utils.getValidatedStages(chosenStages, pipelineStages)

	stages.each{
		stage(it){
			try{
				"${it}"()
				}catch(Exception e){
					error "Stage ${it} tiene problemas: ${e}"
				}
		}
	}
	
}

def buildAndTest(){
	bat './gradlew clean build'
}

def sonar(){
	def scannerHome = tool 'sonar-scanner'
	bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build" 
}

def runJar(){
	bat 'start gradlew bootRun &'
	sleep 20
}

def rest(){
	bat 'curl http://localhost:8083/rest/mscovid/estadoMundial'
}

def nexus(){
	nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', 
							packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', 
							filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], 
							mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
}
return this;