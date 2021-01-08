def call()
{
    sh 'curl -X GET -u admin:P@ssw0rd2201 http://localhost:8081/repository/test-nexus/DevOpsUsach2020/DevOpsUsach2020/1.0/DevOpsUsach2020-1.0.txt -O'
	
    def archivo = "DevOpsUsach2020-1.0.txt"

	String var_version    = ''
    String var_inversa    = ''
    String var_newversion = ''
    String var_pos        = ''
    Number var_nropos     = 0
    Number var_largo      = 0
    String var_release    = ''
    Number var_newrelease = 0

    var_version = new File(archivo).getText('UTF-8');
    var_largo = var_version.length();
    var_inversa = var_version.reverse();
    var_pos = var_inversa.indexOf('-');
    var_nropos = var_pos.toInteger();
    var_release = var_inversa.substring(0, var_nropos);
    var_newrelease = var_release.toInteger() + 1;
    var_nropos = var_nropos + 1;    
    var_newversion = var_version.substring(0, (var_largo-var_nropos)) + '-' + var_newrelease;

    File file_update = new File(archivo)
    file_update.write(var_newversion);

    nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'txt', filePath: 'DevOpsUsach2020-1.0.txt']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'txt', version: '1.0']]]

    return archivo;
}

return this
