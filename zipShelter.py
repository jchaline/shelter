import zipfile

pathDirectory="D:\\projets\\repositories\\shelter\\"
filesNames=["src","webapp", "Gulpfile.js", "package.json", "pom.xml", "README.md", "todo.txt", "usecase.txt", "zipShelter.py" ]
outputFile = pathDirectory+"prospect"

print('creating archive')
zf = zipfile.ZipFile('zipfile_write.zip', mode='w')
try:
	print('adding README.txt')
	zf.write(pathDirectory)
finally:
	print('closing')
	zf.close()

print
