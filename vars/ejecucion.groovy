<<<<<<< HEAD
import pipeline.*

def call(){

    pipeline {
    agent any

    parameters{
        choice(name: 'buildtool', choices: ['gradle','maven'], description: 'Elección de herramienta de construcción para aplicación covid')
        string(name: 'stages', defaultValue:'' , description:'Escribir stages a ejecutar en formato: stage1; stage2; stage3. Si stage es vacío, se ejeutarán los stages')
    }


    stages {
        stage('Pipeline') {
            steps{
                script{
                    env.PIPELINE_TYPE = ''
                    env.PATHJAR= ''
                    env.VERSIONCICD= ''

                    def utils =  new test.UtilMethods()
                    env.PIPELINE_TYPE = utils.pipelineType(env.BRANCH_NAME)
                    utils.setVaribales(env.PIPELINE_TYPE)

                    println 'Herramienta de ejecución seleccionada: '+ params.buildtool + ' '+ env.BRANCH_NAME

                    if(params.buildtool == 'gradle'){
                            gradle "${params.stages}" , env.PIPELINE_TYPE
                        }else{
                            maven "${params.stages}" , env.PIPELINE_TYPE
                        }
                    }
                }
            }

        }   

    }

}

return this;
=======
def call(){
    pipeline {
        agent any

        parameters {
            string(name: 'STAGE', defaultValue: '', description: 'Stages a ejecutar')
        }

        stages {
            stage('Pipeline') {
                steps {
                    script{
                        env.STG_NAME = ''
                        env.BUILD_TOOL = ''
                        env.PIPELINE_TYPE = ''
                        env.VALID_STAGES = ''
                        env.URL_REPO = ''
                        env.GIT_COMMIT_SHORT = ''
                        env.BATCH_COMMAND = ''

                        util.baseOS()
                        env.BUILD_TOOL = util.buildTool()
                        env.PIPELINE_TYPE = util.pipelineType(env.BRANCH_NAME)
                        env.VALID_STAGES = util.validStages(env.PIPELINE_TYPE)
                        env.URL_REPO = scm.getUserRemoteConfigs()[0].getUrl()
                        env.GIT_COMMIT_SHORT = env.GIT_COMMIT.substring(0,7)
                        util.validateStages(params.STAGE)

                        println "Valid Stages: ${env.VALID_STAGES}" 
                        
                        pipelines.execute(params.STAGE)
                        

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
                slackSend color: 'good', message: "[Grupo 3][Pipeline ${env.PIPELINE_TYPE}][Rama: ${env.BRANCH_NAME}][Stage: ${env.STG_NAME}][Resultado: Ok]", teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack-token-diplomado'
            }
            failure {
                slackSend color: 'danger', message: "[Grupo 3][Pipeline ${env.PIPELINE_TYPE}][Rama: ${env.BRANCH_NAME}][Stage: ${env.STG_NAME}][Resultado: No Ok]", teamDomain: 'dipdevopsusach2020', tokenCredentialId: 'slack-token-diplomado'
            }
        }
    }
}

return this;
>>>>>>> fetature-inicial
