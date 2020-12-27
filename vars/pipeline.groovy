import com.util.Constants

def execute(valid_stages, stages){
    def iStages = stages.split(Constants.SPLIT_SYMBOL);

    for(String stage : valid_stages){
        if(stages.trim() == '' || str.contains(stage)){
            env.STG_NAME = stage

            println "Stage: ${stage}"

        }
    }
}