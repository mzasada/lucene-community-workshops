module.exports = function (grunt) {

  grunt.initConfig({
    jshint: {
      files: [
        'Gruntfile.js',
        'package.json'
      ],
      options: {
      }
    },
    connect: {
      server: {
        options: {
          port: 8000,
          keepalive: true
        }
      }
    }
  });

  grunt.registerTask('www', ['connect:server']);

  // load standard Grunt tasks
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-connect');
};