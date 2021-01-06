import pipeline.*

def call(String chosenStages, String pipelineType) {

	figlet 'gradle'
	def pipelineStages = 'CI' ==~ pipelineType ? ['buildAndTest','sonar','runJar','rest','nexusCICD'] : ['downloadNexus','runDownloadJar','rest','nexusCICD']

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

def nexusCICD(){
	nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', 
							packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', 
							filePath: env.PATHJAR+'DevOpsUsach2020-0.0.1.jar']], 
							mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: env.VERSIONCICD+'-'+env.BRANCH_NAME]]]
}

def downloadNexus(){
	bat 'mvn org.apache.maven.plugins:maven-dependency-plugin:2.4:get -DrepoUrl=https://http://localhost:8081/repository/test-repo/ -Dartifact=com.devopsusach2020:DevOpsUsach2020:0.0.1 -Ddest=DevOpsUsach2020-0.0.1.jar'
}

def runDownloadJar(){
	 bat 'start java -Dserver.port=8083 -jar DevOpsUsach2020-0.0.1.jar'
	 sleep 20
}

return this;