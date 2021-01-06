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

return this