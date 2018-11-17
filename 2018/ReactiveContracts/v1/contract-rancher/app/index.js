const app = require('./app_config');
const dotenv = require("dotenv");

dotenv.config();

const port = process.env.PORT;

app.listen(port, function () {
    console.log("Приложение запущено на порте " + port);
});