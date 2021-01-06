import pipeline.*

def call(String choseStages, String pipelineType) {

	figlet 'maven'
	def pipelineStages = 'CI' ==~ pipelineType ? ['compile','test','jar','runJar','sonar','nexus','hola'] : ['downloadNexus','runDownloadJar','rest','nexusCICD']

	def utils = new test.UtilMethods()
	def stages =utils.getValidatedStages(choseStages, pipelineStages)

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

def hola(){
	println 'hola'
}

def compile(){
	bat './mvnw.cmd clean compile -e'
}

def test(){
	bat './mvnw.cmd clean test -e'  
}

def jar(){
	bat './mvnw.cmd clean package -e'
}

def runJar(){
	bat 'start mvnw.cmd spring-boot:run &'
}

def sonar(){
	def scannerHome = tool 'sonar-scanner';
    bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build" 
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