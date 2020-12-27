import com.util.Constants

def call(){
    switch(env.STG_NAME){
        case Constants.STAGE_COMPILE:
            stage(Constants.STAGE_COMPILE){
                bat 'mvnw.cmd clean compile -e'
            }
        case Constants.STAGE_UNITTEST:
            stage(Constants.STAGE_UNITTEST){
                bat 'mvnw.cmd clean test -e'
            }
        case Constants.STAGE_JAR:
            stage(Constants.STAGE_JAR){
                bat 'mvnw.cmd clean package -e'
            }
        default:
            break
    }
}

return this;