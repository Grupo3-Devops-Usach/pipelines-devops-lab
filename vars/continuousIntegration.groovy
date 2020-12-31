def call(String run = '') {

// Transforma lista de stages ingresadas (env.stagesString) en array.
String[] stagesList = env.stagesString.split(';');
// True si stageList contiene strings vacíos.
stagesListEmpty = stagesList.any {x -> x == ''};

switch(run) {
  case 'runGradle':
    // Validación y ejecución de stages Gradle.
    if (stagesList.contains('gitcreaterelease') && (env.branch != 'develop')) {
      throw new Exception ('Stage gitCreateRelease solo disponible en rama develop');
    }	else if (stagesList.contains('build')
        || stagesList.contains('sonar')
        || stagesList.contains('run')
        || stagesList.contains('test')
        || stagesList.contains('nexus')
        || stagesList.contains('gitcreaterelease')) {
        println 'Stages ingresadas válidas';
        // Ejecuta stages ingresadas.
        for (String values : stagesList) {
            println 'Ejecutando etapa ' + values;
            gradleScript values;
        }

    // Validación de input de stages vacío o con espacios en blanco, para ejecutar todas las stages.
    } else if (!stagesList || stagesListEmpty) {
        println 'Stages en blanco';
        println 'Selección por defecto, ejecutando todas las etapas Gradle';
        gradleScript 'build';
        gradleScript 'sonar';
        gradleScript 'run';
        gradleScript 'test';
        gradleScript 'nexus';
        if (env.branch == 'develop') {
          mavenScript 'gitcreaterelease';
      }
// Envía error a la consola en caso de no cumplir las condiciones.
// Se almacena la información en env.stage para enviar a mensaje Slack.
    } else {
        env.stage = env.stagesString;
        throw new Exception('Stages no ingresadas correctamente para Gradle');
    };
    break;

  case 'runMaven':
// Determinar si las stages seleccionadas para Maven son válidas.
  if (stagesList.contains('gitcreaterelease') && (env.branch != 'develop')) {
    throw new Exception ('Stage gitCreateRelease solo disponible en rama develop');
  }	else if (stagesList.contains('compile')
    || stagesList.contains('unit')
    || stagesList.contains('jar')
		|| stagesList.contains('sonar')
		|| stagesList.contains('nexus')
    || stagesList.contains('gitcreaterelease')) {
    println 'Stages ingresadas válidas';
    // Ejecuta stages ingresadas.
    for (String values : stagesList) {
      println 'Ejecutando etapa ' + values;
      mavenScript values;
    }
// Validación de input de stages vacío o con espacios en blanco, para ejecutar todas las stages.
  } else if (!stagesList || stagesListEmpty) {
      println 'Sin información ingresada.';
      println 'Selección por defecto, ejecutando todas las etapas Maven';
      mavenScript 'compile';
      mavenScript 'unit';
      mavenScript 'jar';
      mavenScript 'sonar';
      mavenScript 'nexus';
      if (env.branch == 'develop') {
        mavenScript 'gitcreaterelease';
    }
// Envía error a la consola en caso de no cumplir las condiciones.
// Se almacena la información en env.stage para enviar mensaje a Slack.
  } else {
      env.stage = stagesString;
      throw new Exception('Stages no ingresadas correctamente para Maven');
    }

  }
}
