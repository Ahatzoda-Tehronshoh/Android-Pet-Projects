const express = require('express');
const router = express();

const placeController = require('../controllers/place.controller');

router.post('/list', placeController.listOfPlaces);


module.exports = router