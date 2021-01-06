package pipeline.test

def getValidatedStages(String chosenStages, ArrayList pipelineStages){

	def stages = []

	if(chosenStages?.trim()){
		chosenStages.split(';').each{
			if(it in pipelineStages){
				stages.add(it)
			}else{
				error "${it} no existe como Stage, Stages disponibles para ejecutar: ${pipelineStages}"
			}
		}
		println "Vlidación de stages correcta. Se ejecutrán los siguientes stages en orden: ${stages}"
	}else{
		stages = pipelineStages
		println "Parámetro de stages vacío. Se ejecutrán todos los stages en el siguiente orden: ${stages}"
	}
	return stages
}

def hola(){
	println 'hola'
}

def pipelineType(branch_name){
    def pipeline_type = ''

    if(branch_name ==~ /develop/ || branch_name ==~ /feature-.*/){
        pipeline_type = 'CI'
    } else if(branch_name ==~ /^release-v\d{1,}-\d{1,}-\d{1,}$/){
        pipeline_type = 'CD'
    }

    println "Pipeline Type [${pipeline_type}]"

    return pipeline_type
}

def setVaribales(String pipelineType){
	figlet 'CI' ==~ pipelineType ? 'Integracion' : 'Despliegue'
	env.PATHJAR = 'CI' ==~ pipelineType ? 'build/libs/' : ''; 
	env.VERSIONCICD= 'CI' ==~ pipelineType ? '0.0.1' : '1.0.0'
}

return this