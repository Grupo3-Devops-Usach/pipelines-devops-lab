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
Opciones para Maven: Compile; Unit; Jar; Sonar; Sonar; Test; gitCreateRelease''')
			}


		stages {
			stage('Pipeline') {

				    environment {
				    GIT_AUTH = credentials('ff3041ab-a54a-4ed8-af6b-a335d6185635')
				    }

				steps {
					script {

							// Captura herramienta de construcción seleccionada.
							tool = params.Herramienta;
							// Captura usuario desde git.
							env.user = sh (script: 'git config user.name', returnStdout: true).trim();
							// Captura la rama actual.
							env.branch = env.GIT_BRANCH;
							// Inicializa env.stage global para capturar nombre de etapa.
							env.stage = '';
							// Asigna las etapas seleccionadas a variable global env.stagesString.
							env.stagesString = params.Stage.toLowerCase();
							// Transforma stagesString en array.
							String[] stagesList = env.stagesString.split(';');
							// Define número de release.
							env.versionRelease = '1.0.1';
							env.mayorVersion = '1';
							env.minorVersion = '0';
							env.patchVersion = '0';
							env.releaseBranch = 'release' + env.mayorVersion + '.' + env.minorVersion + '.' + env.patchVersion;

							sh ('git branch --all');
							sh ('git status');
							// Checkout de HEAD detached a rama local.
							sh ('git checkout ' + branch);
							sh ('git pull --verbose');
							sh ('git status');
							sh ('git config user.name');
							sh ('git config user.email');

							println 'Usuario ' + env.user;
							println 'Herramienta seleccionada: ' + tool;
							println 'Stages seleccionadas: ' + stagesList;
							println 'Ejecutando en rama: ' + branch;

							// Integración continua rama Feature*.
							if (branch.matches('feature(.*)') || (branch.matches('develop'))) {
								println 'Inicio Integración Continua rama ' + branch;
								// Checkout a rama develop y merge de rama feature.
								if (tool == 'Gradle') {
									continuousIntegration 'runGradle';
								}
								else if (tool == 'Maven') {
									continuousIntegration 'runMaven';
								}
								println 'Integración Continua completada con éxito bajo herramienta ' + tool + ' en rama ' + branch;
							}

							// Integración continua rama Develop.
							if (branch.matches('develop')) {
								// Checkout a rama develop local.
								println 'Inicio Despliegue continuo rama release';
								continuousDeployment.call();
								sh ('git push --set-upstream origin ' + releaseBranch);
								//sh('git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"');
								//sh('git push origin HEAD:' + releaseBranch);
							}

						}
					}
				}
			}

600 686 0888
		post {
			success {
				slackSend	channel: 'U01DK4YQ15J', color: 'good', message: """
[Alumno: ${env.user}]
[Job: ${JOB_NAME}]
[Herramienta de Construcción: ${params.Herramienta}]
[Ejecución exitosa]""",
				teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack-token'
			}
			unsuccessful {
				slackSend channel: 'U01DK4YQ15J', color: 'danger', message: """
[Alumno: ${env.user}]
[Job: ${JOB_NAME}]
[Herramienta de Construcción: ${params.Herramienta}]
[Ejecución fallida en stage: ${stage}]""",
				teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack-token'
			}
		}


	}
}
return this;
