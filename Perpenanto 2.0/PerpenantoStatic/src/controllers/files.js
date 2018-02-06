const fs = require('fs');
const paths = require('path');

const readFilesLink = (dir) =>
    fs.readdirSync(dir)
      .reduce((files, file) =>
          fs.statSync(paths.join(dir, file)).isDirectory() ?
              files.concat(readFilesLink(paths.join(dir, file))) : files.concat(paths.join(dir, file)), []
      );

module.exports.getFiles = function(req, resp){

    let path = 'src/public';
    let result = readFilesLink(path);

    const replace1 = "src\\public";
    const replace1_res = "http://localhost:8081";

    for(let i = 0; i < result.length; i++){
        result[i] = result[i].replace(replace1, replace1_res);
    }

    resp.render('files_list', {
        title: 'Список файлов сервиса',
        files: result
    });

};