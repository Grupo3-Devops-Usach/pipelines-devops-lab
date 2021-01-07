def call(){
    pipeline {
        agent any
        // parametros
        parameters {
            string(name: 'STAGE', defaultValue: '', description: 'Stages a ejecutar')
        }

        stages {
            stage('Pipeline') {
                steps {
                    script{
                        //fefinicion de var
                        env.STG_NAME = ''
                        env.BUILD_TOOL = ''
                        env.PIPELINE_TYPE = ''
                        env.VALID_STAGES = ''
                        env.URL_REPO = ''
                        env.GIT_COMMIT_SHORT = ''
                        //funcion reconoce OS 
                        util.baseOS()
                        //funcion reconoce OS 
                        env.BUILD_TOOL = util.buildTool()
                        //Validador de rama y muestra el tipo si es ci o release
                        env.PIPELINE_TYPE = util.pipelineType(env.BRANCH_NAME)
                        //valida los  stage
                        env.VALID_STAGES = util.validStages(env.PIPELINE_TYPE)
                        //Obtiene URL del repo  
                        env.URL_REPO = scm.getUserRemoteConfigs()[0].getUrl()
                        //Obtuene commits de git
                        env.GIT_COMMIT_SHORT = env.GIT_COMMIT.substring(0,7)
                        // valida el stage del pipeline
                        util.validateStages(params.STAGE)
                        //imprime stages validos
                        println "Valid Stages: ${env.VALID_STAGES}" 
                        //ejecuta el parametro en el pipeline
                        pipelines.execute(params.STAGE)
                        

                        //Validar tipo de rama a ejecutar develop, feature, release
                        //develop, feature deben ejecutar pipeline IC
                        //release ejecuta pipeline CD
                            
                            //Validar si debe ejecutar gradle o maven

                    }
                }
            }
        }
        // mensaje para slack con estado de ejecucion, informacion de pipelines como branch y estado.
        post {
            success {
                slackSend color: 'good', message: "[Grupo 3][Pipeline ${env.PIPELINE_TYPE}][Rama: ${env.BRANCH_NAME}][Stage: ${env.STG_NAME}][Resultado: Ok]", teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack-token-diplomado'
            }
            failure {
                slackSend color: 'danger', message: "[Grupo 3][Pipeline ${env.PIPELINE_TYPE}][Rama: ${env.BRANCH_NAME}][Stage: ${env.STG_NAME}][Resultado: No Ok]", teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack-token-diplomado'
            }
        }
    }
}

return this;