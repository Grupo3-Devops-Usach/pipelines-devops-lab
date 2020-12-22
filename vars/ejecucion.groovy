def call(){
    pipeline {
        agent any

        parameters {
            choice(name: 'CHOICE', choices: ['maven', 'gradle'], description: 'Herramienta de construccion')
            string(name: 'STAGE', defaultValue: '', description: 'Stages a ejecutar')
        }

        stages {
            stage('Pipeline') {
                steps {
                    script{
                        env.STG_NAME = ''

                        //Validar tipo de rama a ejecutar develop, feature, release
                        //develop, feature deben ejecutar pipeline IC
                        //release ejecuta pipeline CD
                            
                            //Validar si debe ejecutar gradle o maven

                    }
                }
            }
        }
        post {
            success {
                slackSend color: 'good', message: "Build Success: [User Name][${env.JOB_NAME}][${params.CHOICE}] Ejecución exitosa.", teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack-token-diplomado'
            }
            failure {
                slackSend color: 'danger', message: "Build Failure: [User Name][${env.JOB_NAME}][${params.CHOICE}] Ejecución fallida en stage [${env.STG_NAME}].", teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack-token-diplomado'
            }
        }
    }
}

return this;