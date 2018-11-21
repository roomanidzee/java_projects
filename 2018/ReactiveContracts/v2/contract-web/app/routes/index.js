const contractController = require('../controllers/contract');

const express = require('express');
const bodyParser = require('body-parser');
const router = express.Router();

router.get('/index', contractController.indexPage);
router.post('/send', bodyParser.json(), contractController.contractSend);

module.exports = router;