import com.util.Constants

def execute(valid_stages, stages){
    def iStages = stages.split(Constants.SPLIT_SYMBOL);

    println "${valid_stages}"
    println "${stages}"

    for(String value in valid_stages){
        if(stages.trim() == '' || iStages.contains(value)){
            env.STG_NAME = value

            println "Stage: ${value}"

        }
    }
}