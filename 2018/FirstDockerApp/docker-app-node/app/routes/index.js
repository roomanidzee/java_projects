const userController = require('../controllers/user');
const bodyParser = require('body-parser').json();

const express = require('express');
const router = express.Router();

router.post('/register', bodyParser, userController.registerUser);
router.get('/index', userController.indexPage);

module.exports = router;