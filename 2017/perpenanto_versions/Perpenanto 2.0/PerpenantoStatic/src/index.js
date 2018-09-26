const app = require('./config/app');

app.set('port', 8081);

app.listen(app.get('port'), function () {
   console.log("Сервер запущен на порте " + app.get('port'));
});