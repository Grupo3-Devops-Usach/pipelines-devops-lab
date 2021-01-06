import pipeline.*

def call(String choseStages) {

	figlet 'maven'

	def pipelineStages = ['compile','test','jar','runJar','sonar','nexus','hola']

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

def nexus(){
	nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', 
	packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', 
	filePath: 'build/DevOpsUsach2020-0.0.1.jar']], 
	mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
}
return this;