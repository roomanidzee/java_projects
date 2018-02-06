const express = require('express');
const routes = require('../routes/index');
const path = require('path');
const app = express();

app.use(express.static(path.resolve('src/public')));
app.set('views', path.resolve('src/views'));
app.set('view engine', 'pug');
app.use('/', routes);

module.exports = app;
