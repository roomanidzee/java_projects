const fileController = require('../controllers/files');
const express = require('express');
const router = express.Router();

router.get('/files', fileController.getFiles);

module.exports = router;