package com.util

class Constants {
    public static final String IC = 'IC'
    public static final String RELEASE = 'RELEASE'
    public static final String MAVEN = 'MAVEN'
    public static final String GRADLE = 'MAVEN'
    public static final String[] IC_STAGES = ['compile', 'unitTest', 'jar', 'sonar', 'nexusUpload', 'gitCreateRelease']
    public static final String[] IC_STAGES = ['gitDiff', 'nexusDownload', 'run', 'test', 'gitMergeMaster', 'gitMergeDevelop', 'gitTagMaster']
}