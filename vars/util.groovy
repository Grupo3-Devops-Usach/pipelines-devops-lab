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

def branchType(){
    def branch = ''
}