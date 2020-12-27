import com.util.Constants

def execute(stages){
    def iStages = stages.split(Constants.SPLIT_SYMBOL)
    def valid_stages = util.validStages(env.PIPELINE_TYPE)

    println "${valid_stages}"
    println "${stages}"

    for(String value in valid_stages){
        if(stages.trim() == '' || iStages.contains(value)){
            env.STG_NAME = value

            println "Stage: ${value}"

        }
    }
}