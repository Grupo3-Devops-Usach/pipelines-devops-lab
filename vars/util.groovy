import com.util.Constants

def validStages(pipeline_type) {
    def valid_stages

    switch(pipeline_type) {
        case Constants.IC:
            valid_stages = Constants.IC_STAGES
            break
        case Constants.RELEASE:
            valid_stages = Constants.IC_STAGES
            break
    }

    println "Valid Stages: ${valid_stages}"
    return valid_stages
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
        tool = Constants,GRADLE;
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