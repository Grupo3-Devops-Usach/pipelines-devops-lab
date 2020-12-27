import com.util.Constants

def execute(stages_list){
    def iStages = stages_list.split(Constants.SPLIT_SYMBOL)
    def valid_stages = util.validStages(env.PIPELINE_TYPE)

    for(String value in valid_stages){
        if(stages_list.trim() == '' || iStages.contains(value)){
            if(Constants.STAGE_GITCREATERELEASE && util.isDevelopBranch(env.BRANCH_NAME)){
                env.STG_NAME = value
                println "Stage: ${value}"
                stages.call()
            }else{
                env.STG_NAME = value
                println "Stage: ${value}"
                stages.call()
            }
        }
    }
}