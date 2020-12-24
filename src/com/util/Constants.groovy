package com.util

class Constants {
    public static final String IC = 'IC'
    public static final String RELEASE = 'RELEASE'
    public static final String MAVEN = 'MAVEN'
    public static final String GRADLE = 'MAVEN'
    public static final String STAGE_COMPILE = 'compile'
    public static final IC_STAGES = [STAGE_COMPILE, 'unitTest', 'jar', 'sonar', 'nexusUpload', 'gitCreateRelease']
    public static final IC_RELEASE = ['gitDiff', 'nexusDownload', 'run', 'test', 'gitMergeMaster', 'gitMergeDevelop', 'gitTagMaster']
}