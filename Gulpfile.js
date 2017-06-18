var gulp = require('gulp');
var sass = require('gulp-sass');
var sourcemaps = require('gulp-sourcemaps');
var clean = require('gulp-clean');
var plumber = require('gulp-plumber');
var gutil = require("gulp-util")


//arborescence relative
var inputWebapp = './webapp/**/**';
var outputWebapp = 'dist/';

var inputScss = './webapp/scss/**/*.scss';
var outputCss = './dist/css';

var onError = function (err) {
  gutil.beep();
  console.log(err);
};

var sassOptions = {
	errLogToConsole: true,
	outputStyle: 'expanded'
};

gulp.task('clean', function() {
	return gulp.src(outputWebapp)
	.pipe(clean());
});

gulp.task('scss', function () {
	return gulp
		// Find all `.scss` files from the `css/` folder
		.src(inputScss)
		.pipe(plumber())
		.pipe(sourcemaps.init())
		// Run Sass on those files
		.pipe(sass(sassOptions).on('error', sass.logError))
		.pipe(sourcemaps.write('maps'))
		// Write the resulting CSS in the output folder
		.pipe(gulp.dest(outputCss));
});

gulp.task('watchScss', ['clean'], function() {
	return gulp
		// Watch the input folder for change, and run `scss` task when something happens
		.watch(inputScss, ['scss'])
		.on('change', function(event) {
			console.log('File ' + event.path + ' was ' + event.type + ', running tasks...');
		});
});

gulp.task('dist', function () {
	return gulp
		// Find all files from the `webapp/` folder
		.src(inputWebapp)
		.pipe(gulp.dest(outputWebapp));
});

gulp.task('watchDist', ['watchScss'], function() {
	return gulp
		// Watch the input folder for change
		.watch(inputWebapp, ['dist'])
		.on('change', function(event) {
			console.log('File ' + event.path + ' was ' + event.type + ', running tasks...');
		});
});


gulp.task('default', ['watchDist'], function(){
	gulp.start('clean');
	gulp.start('dist');
	gulp.start('scss');
});