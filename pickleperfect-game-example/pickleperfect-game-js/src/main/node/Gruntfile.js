module.exports = function(grunt) {
    grunt.initConfig({
        uglify: {
            report: 'gzip',
            core: {
                files: [{
                    expand: true,
                    cwd: '../js',
                    src: '**/*.js',
                    dest: "../min-js",
                    ext: '.min.js'
                }]
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.registerTask('default', ['uglify']);
};