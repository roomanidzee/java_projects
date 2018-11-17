const dockerController = require('../controllers/docker');

const express = require('express');
const router = express.Router();

router.get('/index', dockerController.indexPage);

module.exports = router;