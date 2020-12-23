final IC = 'IC'

def validStages(pipeline_type) {
    def stages_list = ['build', 'test', 'jar', 'sonar', 'run', 'testing', 'nexus']

    switch(pipeline_type) {
        case IC:
            
            break
        case 'RELEASE':
            
            break
    }

    return stages_list
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
        tool = 'GRADLE';
    }
    else {
        file = new File("pom.xml")
        tool = 'MAVEN';
    }

    println "Build Tool [${tool}]"

    return tool
}

def pipelineType(branch_name){
    def pipeline_type = ''

    if(branch_name ==~ /develop/ || branch_name ==~ /feature-.*/){
        pipeline_type = 'IC'
    } else if(branch_name ==~ /release-v{\d*}-{\d*}-{\d*}/){
        pipeline_type = 'RELEASE'
    }

    println "Pipeline Type [${pipeline_type}]"

    return pipeline_type
}