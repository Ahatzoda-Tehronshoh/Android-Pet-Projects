const express = require('express');
const router = express();

const authController = require('../controllers/auth.controller');

router.post('/register', authController.register);
router.post('/login', authController.logIn);
router.post('/user', authController.getUserById);

module.exports = router