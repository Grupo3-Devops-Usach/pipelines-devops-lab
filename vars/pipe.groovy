def call() {

	pipeline {
		agent any

		parameters {
			choice(
				name: 'Herramienta',
				choices: ['Gradle', 'Maven'],
				description: 'Selección herramienta de construcción')
			string(
				name: 'Stage',
				defaultValue: '',
				description:
'''Selección de stage.
Opciones para Gradle: Build; Sonar; Run; Test; Nexus; gitCreateRelease.
Opciones para Maven: Compile; Unit; Jar; Sonar; Test; gitCreateRelease''')
			}

stages {
	stage('Pipeline') {

    environment {
    	GIT_AUTH = credentials('ff3041ab-a54a-4ed8-af6b-a335d6185635')
    }

		steps {
			script {

				// Declaración de variables
				tool = params.Herramienta;
				env.user = sh (script: 'git config user.name', returnStdout: true).trim();
				env.branch = env.GIT_BRANCH;
				env.stage = '';
				env.stagesString = params.Stage.toLowerCase();
				String[] stagesList = env.stagesString.split(';');

				// Inicialización git.
				sh ('git status');
				sh ('git branch --all');
				sh ('git checkout ' + env.GIT_BRANCH);
				sh ('git pull --verbose');
//				sh ('git add .; git commit -m "Actualización"; git push origin ' + branch);
				sh ('git status');
				sh ('git config user.name');
				sh ('git config user.email');

				// Versionamiento release.
				env.mayorVersion = '1';
				env.minorVersion = '0';
				env.patchVersion = '0';
				env.releaseBranch = 'release' + env.mayorVersion + '.' + env.minorVersion + '.' + env.patchVersion;

				// Print de información.
				println 'Usuario ' + env.user;
				println 'Herramienta seleccionada: ' + tool;
				println 'Stages seleccionadas: ' + stagesList;
				println 'Ejecutando en rama: ' + branch;

				// Integración continua ramas feature y develop.
				if (branch.matches('(.*)feature(.*)') || (branch.matches('develop'))) {
					println 'Inicio Integración Continua rama ' + branch;
					if (tool == 'Gradle') {
						continuousIntegration 'runGradle';
					}
					else if (tool == 'Maven') {
						continuousIntegration 'runMaven';
					}
					println 'Integración Continua completada con éxito bajo herramienta ' + tool + ' en rama ' + branch;
				}

				// Despliegue continuo rama release a partir de rama develop.
				if (branch.matches('develop')) {
					println 'Inicio despliegue continuo rama release';
					continuousDeployment.call();
//								sh ('git push --set-upstream origin ' + releaseBranch);
					//sh('git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"');
					//sh('git push origin HEAD:' + releaseBranch);
				}

				}
			}
		}
	}

		post {
			success {
				slackSend	channel: 'U01DK4YQ15J', color: 'good', message: """
[Usuario: ${env.user}]
[Grupo 3]
[Rama ${branch}]
[Stage: ${stage}]
[Resultado: OK]
[Job: ${JOB_NAME}]
[Herramienta de Construcción: ${params.Herramienta}]
[Ejecución exitosa]""",
				teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack-token'
			}
			unsuccessful {
				slackSend channel: 'U01DK4YQ15J', color: 'danger', message: """
[Usuario: ${env.user}]
[Grupo 3]
[Rama ${branch}]
[Stage: ${stage}]
[Resultado: no OK]
[Job: ${JOB_NAME}]
[Herramienta de Construcción: ${params.Herramienta}]
[Ejecución fallida en stage: ${stage}]""",
				teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack-token'
			}
		}


	}
}
return this;