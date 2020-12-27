import com.util.Constants

def validStages(pipeline_type) {
    def valid_stages

    switch(pipeline_type) {
        case Constants.IC:
            valid_stages = Constants.IC_STAGES
            break
        case Constants.RELEASE:
            valid_stages = Constants.RELEASE_STAGES
            break
    }

    println "Valid Stages: ${valid_stages}"
    return valid_stages
}

def validateStages(stages){

    def valid_stages = this.validStages(env.PIPELINE_TYPE)

    if(stages.trim() == ''){
        println "Stages a ejecutar [TODOS]"
    }else{
        println "Stages a ejecutar [${stages}]"

        def stage_list = stages.split(Constants.SPLIT_SYMBOL);

        for(String value in stage_list){
            if (!valid_stages.contains(value.trim())){
                env.STG_NAME = "${value} (no valido)"
                error "Stage no valido: ${value}"
            }
        }
    }
}

def baseOS(){
    def os = ''

    if(isUnix()){
        os = 'Unix/Linux/MacOS'    
    } else {
        os = 'Windows'
    }

    println "Jenkins OS [${os}]"
}

def buildTool(){
    def tool = ''

    def file = new File("build.gradle")
    
    if (file.exists()){
        tool = Constants.GRADLE;
    }
    else {
        file = new File("pom.xml")
        tool = Constants.MAVEN;
    }

    println "Build Tool [${tool}]"

    return tool
}

def pipelineType(branch_name){
    def pipeline_type = ''

    if(branch_name ==~ /develop/ || branch_name ==~ /feature-.*/){
        pipeline_type = Constants.IC
    } else if(branch_name ==~ /release-v{\d*}-{\d*}-{\d*}/){
        pipeline_type = Constants.RELEASE
    }

    println "Pipeline Type [${pipeline_type}]"

    return pipeline_type
}