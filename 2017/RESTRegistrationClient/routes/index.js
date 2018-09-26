const userController = require('../controllers/user');

const express = require('express');
const router = express.Router();

router.get('/register', userController.registerForm);
router.get('/profile', userController.profileRedirect);

module.exports = router;