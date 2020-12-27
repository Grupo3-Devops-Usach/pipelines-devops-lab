import com.util.Constants

def execute(stage){
    switch(stage){
        case Constants.STAGE_COMPILE:
            stage(Constants.STAGE_COMPILE){
                bat 'mvnw.cmd clean compile -e'
            }
        default:
            break
    }
}