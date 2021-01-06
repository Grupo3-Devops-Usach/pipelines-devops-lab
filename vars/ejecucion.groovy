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
