const app = require('./app');
app.set('port', 8081);

app.listen(app.get('port'), function () {
    console.log("Приложение запущено на порте " + app.get('port'));
});